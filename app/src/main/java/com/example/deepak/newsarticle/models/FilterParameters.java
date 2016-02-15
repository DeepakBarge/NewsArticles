package com.example.deepak.newsarticle.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class FilterParameters implements Parcelable {

    public String beginDate;
    public String sortOrder;
    public HashMap<String,Boolean> newsDesk = new HashMap<>();

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setNewsDesk(HashMap<String, Boolean> newsDesk) {
        this.newsDesk = newsDesk;
    }

    public HashMap<String, Boolean> getNewsDesk() {
        return newsDesk;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public FilterParameters() {
        Calendar t = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        this.beginDate = sdf.format(t.getTime());

        HashMap<String,Boolean> newsDesk = new HashMap<>();
        newsDesk.put("arts", false);
        newsDesk.put("sports", false);
        newsDesk.put("fs", false);
        this.newsDesk = newsDesk;

        this.sortOrder = "newest";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.beginDate);
        dest.writeString(this.sortOrder);
        dest.writeSerializable(this.newsDesk);
    }

    protected FilterParameters(Parcel in) {
        this.beginDate = in.readString();
        this.sortOrder = in.readString();
        this.newsDesk = (HashMap<String, Boolean>) in.readSerializable();
    }

    public static final Parcelable.Creator<FilterParameters> CREATOR = new Parcelable.Creator<FilterParameters>() {
        public FilterParameters createFromParcel(Parcel source) {
            return new FilterParameters(source);
        }

        public FilterParameters[] newArray(int size) {
            return new FilterParameters[size];
        }
    };
}
