package com.example.mybookapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    private static URL createURL(String stringSearch) {
        URL url = null;
        String myUrl = "";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.googleapis.com")
                .appendPath("books")
                .appendPath("v1")
                .appendPath("volumes")
                .appendQueryParameter("q", stringSearch);

        myUrl = builder.build().toString();

        Log.i(LOG_TAG, "The URL is: " + myUrl);

        try {
            url = new URL(myUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL :" , e);
        }
        return url;
    }

    private static String makeHttpRequest (URL url) throws IOException {
        String JsonResponse = "";
        if (url == null){
            return JsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                JsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e){
                Log.e(LOG_TAG, "Problem retrieving book data.");
            } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
            return JsonResponse;
    }



    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
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

    public static ArrayList<MyBooks> extractFeatureFromJson(String newBook){

        String myBooksUrl = "";
        String listAuthor = "";
        String myBooksTitle = "";
        String myBooksDate = "";

        if (TextUtils.isEmpty(newBook)) {
            return null;
        }

        ArrayList<MyBooks> theBooks = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newBook);


            JSONArray myBookArray = baseJsonResponse.getJSONArray("items");
            for (int i = 0; i < myBookArray.length(); i++) {
                JSONObject currentMyBook = myBookArray.getJSONObject(i);
                JSONObject myVolumeInfo = currentMyBook.getJSONObject("volumeInfo");

                if (myVolumeInfo.has("imageLinks")) {
                    JSONObject myImageInfo = myVolumeInfo.getJSONObject("imageLinks");
                    myBooksUrl = myImageInfo.getString("smallThumbnail");
                } else {
                    myBooksUrl = null;
                }

                if (myVolumeInfo.has("authors")) {
                    JSONArray myBooksAuthor = myVolumeInfo.getJSONArray("authors");
                    for (int x = 0; x < myBooksAuthor.length(); x++) {
                        listAuthor = listAuthor + myBooksAuthor.getString(x);
                    }
                } else {
                    listAuthor = "NA";
                }

                myBooksTitle = myVolumeInfo.getString("title");
                myBooksDate = myVolumeInfo.getString("publishedDate");

                MyBooks thisBook = new MyBooks(myBooksUrl, myBooksTitle, listAuthor, myBooksDate);
                theBooks.add(thisBook);
            }


            } catch(JSONException e){
                Log.e(LOG_TAG, "problem pursing the book data", e);
                theBooks = null;
            }


        return theBooks;
    }

    public static ArrayList<MyBooks> fetchMyBookData (String requestUrl){
        URL url = createURL(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "problem making the http request., e ");

        }

        ArrayList<MyBooks> myBookList = extractFeatureFromJson(jsonResponse);
        return myBookList;
    }



}
