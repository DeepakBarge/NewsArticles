package com.example.deepak.newsarticle.models;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class FilterParameters {

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
}
