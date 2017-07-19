package com.example.android.project8_v2_wiltontuji;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Adailto on 18/05/2017.
 */

public class QueryUtils {

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestURL) {
        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("fetchNewsData", "Problem making the HTTP request.");
        }
        List<News> news = extractFromJson(jsonResponse);
        return news;
    }

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e("createURL", "Problem building the URL ");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("makeHTTPRequest", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("makeHTTPRequest", "Problem retrieving the earthquake JSON results.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = responseObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentNews = resultsArray.getJSONObject(i);
                String newsTitle = currentNews.getString("webTitle");
                String newsTypeLowerCase = currentNews.getString("type");
                String newsType = Character.toString(newsTypeLowerCase.charAt(0)).toUpperCase()+newsTypeLowerCase.substring(1);
                String newsDateString = currentNews.getString("webPublicationDate");

                SimpleDateFormat utfFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                utfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date parsedDate = utfFormat.parse(newsDateString);
                SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat localHourFormat = new SimpleDateFormat("HH:mm:ss");
                String newsDate = localDateFormat.format(parsedDate);
                String newsHour = localHourFormat.format(parsedDate);

                String newsUrl = currentNews.getString("webUrl");
                News newsToAdd = new News(newsTitle, newsType, newsHour, newsDate, newsUrl);
                news.add(newsToAdd);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return news;
    }
}
