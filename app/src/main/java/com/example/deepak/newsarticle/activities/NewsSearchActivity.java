package com.example.deepak.newsarticle.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.deepak.newsarticle.R;
import com.example.deepak.newsarticle.adapters.ArticleAdapter;
import com.example.deepak.newsarticle.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class NewsSearchActivity extends AppCompatActivity {


    final static String BASE_URI = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    final static String API_KEY = "8003c034b43c89e39cd38d9cb0c4bc89:9:74356829";
    ArticleAdapter adapter;
    ArrayList<Article> articles;
    RecyclerView rvArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("News");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        articles = new ArrayList<>();

        // Lookup the recyclerview in activity layout
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        // Create adapter passing in the sample user data
        adapter = new ArticleAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(adapter);

        // Set layout manager to position the items
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //rvArticles.setLayoutManager(new GridLayoutManager(this, 2));
        //gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticles.setLayoutManager(gridLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                Log.i("info", "Query: " + query);
                fetchArticles();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void fetchArticles(){

        SearchView sv = (SearchView) findViewById(R.id.action_search);
        String query = sv.getQuery().toString();
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("q", query);
        params.put("api-key", API_KEY);
        //newest or oldest
        params.put("page", 0);

        client.get(BASE_URI, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("info",response.toString());

                try {
                    JSONArray articleJSON = response.getJSONObject("response").getJSONArray("docs");
                    articles = Article.getAllArticles(articleJSON);
                    adapter.updateList(articles);
                    Log.i("info", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Unable to connect to NYTimes. " +
                        "Check your n/w connection. Try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

            // This is the up button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Log.i("info","home button selected");
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
