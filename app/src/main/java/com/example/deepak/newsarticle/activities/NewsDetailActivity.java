package com.example.deepak.newsarticle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.example.deepak.newsarticle.R;
import com.example.deepak.newsarticle.models.Article1;
import com.example.deepak.newsarticle.models.Article1;
import com.example.deepak.newsarticle.models.Article2;
import com.example.deepak.newsarticle.utils.ApplicationHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity implements ApplicationHelper.AlertDialogListener {

    private ShareActionProvider miShareAction;
    String url = "";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.pbProgressAction) ProgressBar mProgressItem;
    @Bind(R.id.webView) WebView wvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");

        ApplicationHelper.setContext(NewsDetailActivity.this);

        Intent i = getIntent();

        if(i.getParcelableExtra("article") instanceof Article1){
            Article1 a1 = i.getParcelableExtra("article");
            url = a1.getWebUrl();
        }

        if(i.getParcelableExtra("article") instanceof Article2){
            Article2 a2 = i.getParcelableExtra("article");
            url = a2.getWebUrl();
        }


        wvView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressItem.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.i("info","page load error1");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                Log.i("info","page load error2");

            }

        });

        loadRemotePage();

    }

    public void loadRemotePage(){
        if(!ApplicationHelper.isNetworkAvailable(getApplicationContext()) || !ApplicationHelper.isOnline()){
            ApplicationHelper.showWarning(NewsDetailActivity.this);
        } else {
            mProgressItem.setVisibility(View.VISIBLE);
            wvView.loadUrl(url);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_news_detail, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, wvView.getUrl());

        miShareAction.setShareIntent(shareIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_item_share){
            //Toast.makeText(this,"share selected",Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    /* this method is overridden to prevent the UP button from creating a new activity
       instead of showing the old activity */
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

    @Override
    public void onTryAgain() {
        loadRemotePage();
    }

    @Override
    public void onCancel() {
        mProgressItem.setVisibility(View.GONE);
    }
}
