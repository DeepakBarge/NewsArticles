package com.example.deepak.newsarticle.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.deepak.newsarticle.R;
import com.example.deepak.newsarticle.adapters.ArticleAdapter;
import com.example.deepak.newsarticle.fragments.SearchSettingsFragment;
import com.example.deepak.newsarticle.listeners.EndlessRecyclerViewScrollListener;
import com.example.deepak.newsarticle.models.Article;
import com.example.deepak.newsarticle.models.FilterParameters;
import com.example.deepak.newsarticle.utils.ApplicationHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class NewsSearchActivity extends AppCompatActivity implements SearchSettingsFragment.EditNameDialogListener{

    final static String BASE_URI = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    final static String API_KEY = "8003c034b43c89e39cd38d9cb0c4bc89:9:74356829";
    final static int REFRESH_OPERATION = 1;
    final static int SCROLL_OPERATION = 0;

    @Bind(R.id.rvArticles) RecyclerView rvArticles;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.toolbar) Toolbar toolbar;

    ArticleAdapter adapter;
    ArrayList<Article> fetchedArticles;
    MenuItem miActionProgressItem;
    String searchText;
    FilterParameters fp = new FilterParameters();
    int refreshCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("News");

        fetchedArticles = new ArrayList<>();

        // Create adapter passing in the sample user data
        adapter = new ArticleAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(adapter);

        // Set layout manager to position the items
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        //gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticles.setLayoutManager(staggeredGridLayoutManager);

        refreshCount = 0;

        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager){

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("info","scroll - new items needed "+page);
                fetchArticles(page, SCROLL_OPERATION);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i("info","refresh - new items needed "+refreshCount);
                fetchArticles(refreshCount, REFRESH_OPERATION);
                refreshCount++;
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if(!ApplicationHelper.isNetworkAvailable(getApplicationContext()) || !ApplicationHelper.isOnline()){
            ApplicationHelper.showWarning(NewsSearchActivity.this);
        }

    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchSettingsFragment searchSettings = SearchSettingsFragment.newInstance();
        searchSettings.context = this;
        searchSettings.show(fm, "fragment_search_settings");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);

        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                Log.i("info", "Query: " + query);
                searchText = query;
                searchItem.collapseActionView();

                //set refreshCount = 0 for new search
                refreshCount = 0;

                //must clear the elements of the previous search
                int curSize = adapter.getItemCount();
                adapter.articles.clear();
                adapter.notifyItemRangeRemoved(0, curSize);

                Log.i("info", "Range removed [0-" + curSize + "]");

                //fetch articles with 0th page for a new query
                fetchArticles(0, SCROLL_OPERATION);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void fetchArticles(int page, final int operation){

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("q", searchText);
        params.put("api-key", API_KEY);
        params.put("begin_date",fp.getBeginDate());
        params.put("sort",fp.getSortOrder());
        HashMap<String,Boolean> newsDesk = fp.getNewsDesk();

        if(newsDesk.get("sports") || newsDesk.get("fs") || newsDesk.get("arts")) {
            StringBuilder fq = new StringBuilder();
            fq.append("news_desk:(");
            if(newsDesk.get("sports")) {
                fq.append("\"");
                fq.append("sports");
                fq.append("\"");
            }
            if(newsDesk.get("arts")) {
                if(fq.toString().contains("sports")){ fq.append(",");}
                fq.append("\"");
                fq.append("arts");
                fq.append("\"");
            }
            if(newsDesk.get("fs")) {
                if(fq.toString().contains("sports") || fq.toString().contains("arts")){ fq.append(",");}
                fq.append("\"");
                fq.append("fashion");
                fq.append("\"");
            }
            fq.append(")");
            params.put("fq",fq.toString());
            Log.i("info","fq: "+fq);
        }
        // page[0-9]
        params.put("page", page);
        Log.i("info", params.toString());
        showProgressBar();

        try {
            client.get(BASE_URI, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("info", response.toString());

                    try {
                        JSONArray articleJSON = response.getJSONObject("response").getJSONArray("docs");
                        fetchedArticles = Article.getAllArticles(articleJSON);

                        if (operation == SCROLL_OPERATION) {
                            // get current size of the adapter
                            int curSize = adapter.getItemCount();
                            adapter.appendList(fetchedArticles);
                            //adapter.notifyDataSetChanged();
                            adapter.notifyItemRangeInserted(curSize, adapter.getItemCount() - 1);
                            //adapter.notifyItemRangeInserted(0, fetchedArticles.size() - 1);

                            Log.i("info", fetchedArticles.toString());
                            Log.i("info", "Scroll - Range inserted [" + curSize + "-" + adapter.getItemCount() + "]");
                        } else {
                            // get current size of the adapter
                            int curSize = fetchedArticles.size() - 1;
                            adapter.addAtStartList(fetchedArticles);
                            //adapter.notifyDataSetChanged();
                            adapter.notifyItemRangeInserted(0, curSize);

                            Log.i("info", fetchedArticles.toString());
                            Log.i("info", "REFRESH - Range inserted [ 0-" + curSize + "]");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        hideProgressBar();
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("info", "onfailure1 received");
                    refreshCount--;
                    hideProgressBar();
                    swipeContainer.setRefreshing(false);
                    Log.i("info","Cause: "+throwable.getCause());
                    ApplicationHelper.showWarning(NewsSearchActivity.this);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.i("info", "onfailure2 received");
                    refreshCount--;
                    hideProgressBar();
                    swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i("info", "onfailure3 received");
                    Toast.makeText(getApplicationContext(), "Unable to connect to NYTimes. " +
                            "Check your n/w connection. Try again later", Toast.LENGTH_SHORT).show();
                    refreshCount--;
                    hideProgressBar();
                    swipeContainer.setRefreshing(false);

                }
            });
        } catch (Exception e){
            //Log.i("info"," inside http call: "+e.getCause());
            hideProgressBar();
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                showEditDialog();
                return true;

            // This is the up button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onFinishEditDialog(FilterParameters fp) {
        this.fp = fp;
        //Toast.makeText(this, "Date, " + fp.getBeginDate(), Toast.LENGTH_SHORT).show();
    }
}
