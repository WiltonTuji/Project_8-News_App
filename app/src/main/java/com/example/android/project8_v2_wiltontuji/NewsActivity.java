package com.example.android.project8_v2_wiltontuji;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import static android.view.View.GONE;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter mNewsAdapter;
    private final static int NEWS_LOADER_ID = 1;
    private String REQUEST_URL_TEST = "http://content.guardianapis.com/search?q=brazil&api-key=test";
    TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ListView newsListView = (ListView) findViewById(R.id.news_list_view);
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mNewsAdapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mNewsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getNewsWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, REQUEST_URL_TEST);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        mEmptyStateTextView.setText(R.string.no_results);
        mNewsAdapter.clear();
        if (news != null && !news.isEmpty()){
            mEmptyStateTextView.setVisibility(GONE);
            mNewsAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapter.clear();
    }
}
