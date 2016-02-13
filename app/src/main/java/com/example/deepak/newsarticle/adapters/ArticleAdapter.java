package com.example.deepak.newsarticle.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.deepak.newsarticle.R;
import com.example.deepak.newsarticle.activities.NewsDetailActivity;
import com.example.deepak.newsarticle.models.Article;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by deepak on 2/10/16.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public List<Article> articles = new ArrayList<>();
    private Context context;

    public ArticleAdapter(Context context){
        this.context = context;
    }

    public void appendList (List<Article> articles) {
        // append the new articles to the adapter
        this.articles.addAll(articles);
        Log.i("info", "Number of articles " + this.articles.size());
    }

    public void addAtStartList (List<Article> articles) {
        // append the new articles to the adapter
        this.articles.addAll(0, articles);
        Log.i("info", "Number of articles " + this.articles.size());
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.article_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Log.i("info","bind");

        // Get the data model based on position
        Article article = articles.get(position);

        // Set item views based on the data model
        holder.tvHeadline.setText(article.getArticleHeadline());

        holder.ivImage.setImageResource(0);

        if(article.thumbnailUrls.length>0){
            String url = article.getThumbnailUrl(new Random().nextInt(article.thumbnailUrls.length));

            Glide.with(context)
                .load(url)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_no_thumbail);
        }

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return articles.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivImage;
        public TextView tvHeadline;
        private Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            this.ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            this.tvHeadline = (TextView) itemView.findViewById(R.id.tvHeadline);
            this.context = context;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position

            Article a = articles.get(position);
            //Toast.makeText(context, a.getArticleHeadline(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(context, NewsDetailActivity.class);
            i.putExtra("article",a);
            context.startActivity(i);

        }
    }
}
