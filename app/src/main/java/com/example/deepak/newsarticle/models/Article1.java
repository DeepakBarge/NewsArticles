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
public class Article1 implements Parcelable {

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

    public void setArticleHeadline(String articleHeadline) {
        this.articleHeadline = articleHeadline;
    }

    public void setThumbnailUrls(String[] thumbnailUrls) {
        this.thumbnailUrls = thumbnailUrls;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
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

    public Article1() {
    }

    protected Article1(Parcel in) {
        this.articleHeadline = in.readString();
        this.thumbnailUrls = in.createStringArray();
        this.webUrl = in.readString();
    }

    public static final Parcelable.Creator<Article1> CREATOR = new Parcelable.Creator<Article1>() {
        public Article1 createFromParcel(Parcel source) {
            return new Article1(source);
        }

        public Article1[] newArray(int size) {
            return new Article1[size];
        }
    };
}

