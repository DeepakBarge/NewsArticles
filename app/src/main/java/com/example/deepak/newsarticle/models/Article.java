package com.example.deepak.newsarticle.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by deepak on 2/10/16.
 */
public class Article implements Parcelable {

    public String articleHeadline;
    public String []thumbnailUrls;
    public String webUrl;

    public String getArticleHeadline() {
        return articleHeadline;
    }

    public String getThumbnailUrl(int pos) {
        return thumbnailUrls[pos];
    }

    public String getWebUrl() {
        return webUrl;
    }


    public Article(JSONObject articleJSON){

        try {
            this.articleHeadline = articleJSON.getJSONObject("headline").getString("main");
            this.webUrl = articleJSON.getString("web_url");

            JSONArray multimedia = articleJSON.getJSONArray("multimedia");

            if(multimedia.length()>0) {
                thumbnailUrls = new String[multimedia.length()];
                for (int i = 0; i < multimedia.length(); i++) {
                    JSONObject multimediaJSON = multimedia.getJSONObject(i);
                    this.thumbnailUrls[i] = "http://www.nytimes.com/" + multimediaJSON.getString("url");
                }

            } else {
                thumbnailUrls = new String[0];
            }
            Log.i("info",this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //swipeContainer.setRefreshing(false);
        //adapter.notifyDataSetChanged();
    }

    public static ArrayList<Article> getAllArticles(JSONArray articlesJSON){
        ArrayList<Article> articles = new ArrayList<>();
        for(int i=0; i<articlesJSON.length(); i++) {
            try{
                articles.add(new Article(articlesJSON.getJSONObject(i)));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        return articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.articleHeadline);
        dest.writeStringArray(this.thumbnailUrls);
        dest.writeString(this.webUrl);
    }

    protected Article(Parcel in) {
        this.articleHeadline = in.readString();
        this.thumbnailUrls = in.createStringArray();
        this.webUrl = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}

