package com.example.deepak.newsarticle.utils;

import android.util.Log;

import com.example.deepak.newsarticle.models.Article1;
import com.example.deepak.newsarticle.models.Article2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class JsonDeserializer {

    public Object obj;

    public JsonDeserializer(JSONObject articleJSON){

        try {

            JSONArray multimedia = articleJSON.getJSONArray("multimedia");

            if(multimedia.length()>0) {
                Article1 a1 = new Article1();

                String []thumbnailUrls = new String[multimedia.length()];
                for (int i = 0; i < multimedia.length(); i++) {
                    JSONObject multimediaJSON = multimedia.getJSONObject(i);
                    thumbnailUrls[i] = "http://www.nytimes.com/" + multimediaJSON.getString("url");
                }

                a1.setArticleHeadline(articleJSON.getJSONObject("headline").getString("main"));
                a1.setWebUrl(articleJSON.getString("web_url"));
                a1.setThumbnailUrls(thumbnailUrls);
                this.obj =  a1;

            } else {
                Article2 a2 = new Article2();
                a2.setArticleHeadline(articleJSON.getJSONObject("headline").getString("main"));
                a2.setWebUrl(articleJSON.getString("web_url"));
                this.obj = a2;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Object> getAllArticles(JSONArray articlesJSON){
        ArrayList<Object> articles = new ArrayList<>();
        for(int i=0; i<articlesJSON.length(); i++) {
            try{
                JsonDeserializer jsonDeserializer = new JsonDeserializer(articlesJSON.getJSONObject(i));
                articles.add(jsonDeserializer.obj);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return articles;
    }

}
