package com.example.android.project8_v2_wiltontuji;

/**
 * Created by Adailto on 18/05/2017.
 */

public class News {

    private String mNewsTitle;
    private String mNewsType;
    private String mNewsDate;
    private String mNewsHour;
    private String mNewsWebUrl;

    public News(String newsTitle, String newsType, String newsDate, String newsHour, String newsWebUrl) {
        this.mNewsTitle = newsTitle;
        this.mNewsType = newsType;
        this.mNewsDate = newsDate;
        this.mNewsHour = newsHour;
        this.mNewsWebUrl = newsWebUrl;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getNewsType() {
        return mNewsType;
    }

    public String getNewsDate() {
        return mNewsDate;
    }

    public String getNewsHour(){
        return mNewsHour;
    }

    public String getNewsWebUrl() {
        return mNewsWebUrl;
    }
}
