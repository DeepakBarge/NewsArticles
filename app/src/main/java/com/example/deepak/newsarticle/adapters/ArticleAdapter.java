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
import com.example.deepak.newsarticle.models.Article1;
import com.example.deepak.newsarticle.models.Article2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by deepak on 2/10/16.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    public List<Object> articles = new ArrayList<>();

    private final int ARTICLE1 = 0, ARTICLE2 = 1;

    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArticleAdapter(Context context) {
        this.context = context;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.get(position) instanceof Article1) {
            return ARTICLE1;
        } else if (articles.get(position) instanceof Article2) {
            return ARTICLE2;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case ARTICLE1:
                View v1 = inflater.inflate(R.layout.article_list_item, viewGroup, false);
                viewHolder = new ViewHolder1(v1);
                break;
            case ARTICLE2:
                View v2 = inflater.inflate(R.layout.article_list_item_textonly, viewGroup, false);
                viewHolder = new ViewHolder2(v2);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ARTICLE1:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case ARTICLE2:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                break;
        }
    }

    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        Article1 article1 = (Article1) articles.get(position);
        if (article1 != null) {

            vh1.getTvHeadline().setText(article1.getArticleHeadline());

            vh1.getIvImage().setImageResource(0);

            String url = article1.getThumbnailUrl(new Random().nextInt(article1.thumbnailUrls.length));

            Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(vh1.getIvImage());
        }
    }

    private void configureViewHolder2(ViewHolder2 vh2, int position) {
        Article2 article2 = (Article2) articles.get(position);
        if (article2 != null) {
            vh2.getTvHeadline2().setText(article2.getArticleHeadline());
        }
    }

    public void appendList (List<Object> articles) {
        this.articles.addAll(articles);
        Log.i("info", "Number of articles " + this.articles.size());
    }

    public void addAtStartList (List<Object> articles) {

        this.articles.addAll(0, articles);
        Log.i("info", "Number of articles " + this.articles.size());
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivImage;
        private TextView tvHeadline;

        public ViewHolder1(View v) {
            super(v);
            ivImage = (ImageView)v.findViewById(R.id.ivImage);
            tvHeadline = (TextView) v.findViewById(R.id.tvHeadline);
            v.setOnClickListener(this);

        }

        public ImageView getIvImage() {
            return ivImage;
        }

        public void setIvImage(ImageView ivImage) {
            this.ivImage = ivImage;
        }

        public TextView getTvHeadline() {
            return tvHeadline;
        }

        public void setTvHeadline(TextView tvHeadline) {
            this.tvHeadline = tvHeadline;
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition(); // gets item position

            if(articles.get(position) instanceof Article1){
                Article1 a1 = (Article1) articles.get(position);
                Intent i = new Intent(context, NewsDetailActivity.class);
                i.putExtra("article",a1);
                context.startActivity(i);
            }

        }
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvHeadline2;

        public ViewHolder2(View v) {
            super(v);
            tvHeadline2 = (TextView) v.findViewById(R.id.tvHeadline2);
            v.setOnClickListener(this);
        }

        public TextView getTvHeadline2() {
            return tvHeadline2;
        }

        public void setTvHeadline2(TextView tvHeadline2) {
            this.tvHeadline2 = tvHeadline2;
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition(); // gets item position

            if(articles.get(position) instanceof Article2){
                Article2 a2 = (Article2) articles.get(position);
                Intent i = new Intent(context, NewsDetailActivity.class);
                i.putExtra("article",a2);
                context.startActivity(i);
            }

        }
    }

}
