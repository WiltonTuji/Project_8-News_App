package com.example.android.project8_v2_wiltontuji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adailto on 18/05/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context,0,news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        TextView newsNameTextView = (TextView) convertView.findViewById(R.id.news_web_title);
        TextView newsTypeTextView = (TextView) convertView.findViewById(R.id.news_type);
        TextView newsDateTextView = (TextView) convertView.findViewById(R.id.news_publication_date);
        TextView newsHourTextView = (TextView) convertView.findViewById(R.id.news_hour);

        newsNameTextView.setText(news.getNewsTitle());
        newsTypeTextView.setText(getContext().getResources().getString(R.string.news_type) + " " + news.getNewsType());
        newsDateTextView.setText(getContext().getResources().getString(R.string.news_date) + " " + news.getNewsDate());
        newsHourTextView.setText(getContext().getResources().getString(R.string.news_hour) + " " + news.getNewsHour());

        return convertView;
    }
}
