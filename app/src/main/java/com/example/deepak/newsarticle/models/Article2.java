package com.example.deepak.newsarticle.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Article2 implements Parcelable {

    public String articleHeadline;
    public String webUrl;

    public String getArticleHeadline() {
        return articleHeadline;
    }

    public void setArticleHeadline(String articleHeadline) {
        this.articleHeadline = articleHeadline;
    }

    public String getWebUrl() {
        return webUrl;
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
        dest.writeString(this.webUrl);
    }

    public Article2() {
    }

    protected Article2(Parcel in) {
        this.articleHeadline = in.readString();
        this.webUrl = in.readString();
    }

    public static final Parcelable.Creator<Article2> CREATOR = new Parcelable.Creator<Article2>() {
        public Article2 createFromParcel(Parcel source) {
            return new Article2(source);
        }

        public Article2[] newArray(int size) {
            return new Article2[size];
        }
    };
}
