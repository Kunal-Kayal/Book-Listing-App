package com.example.android.booklisting;

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
import java.util.ArrayList;
import java.util.List;

public class JsonQuery {
    private JsonQuery(){

    }
    private static  final String Log_Tag = JsonQuery.class.getSimpleName();
    private static List<Book> ExtractBooks(String jsonResponse){
      List<Book> books = new ArrayList<>();

      try{
          JSONObject root = new JSONObject(jsonResponse);
          JSONArray items = root.optJSONArray("items");

          for(int i=0;i<items.length();i++){
              JSONObject temp = items.getJSONObject(i);
              JSONObject volumeInfo =temp.getJSONObject("volumeInfo");

              String bookTitle = volumeInfo.getString("title");
              JSONArray authors = volumeInfo.optJSONArray("authors");

              String authorsname="";
              if(authors!=null && authors.length()!=0) {
                  for (int j = 0; j < authors.length(); j++) {
                      authorsname += authors.optString(j) + " ";
                  }
              }
              String publishedDate = volumeInfo.optString("publishedDate","Not Avilable");

              String rating = volumeInfo.optString("averageRating","3");

              JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");

              String image_url = imageLinks.optString("smallThumbnail","http://www.indiaspora.org/wp-content/uploads/2018/10/image-not-available.jpg");


              String webReaderLink = volumeInfo.getString("previewLink");

              books.add(new Book(rating,image_url,bookTitle,authorsname,publishedDate,webReaderLink));

          }
      }catch (JSONException e){
          Log.e(Log_Tag,"Problem while parsing Json Object",e);

      }
      return books;
    }

    public static List<Book> fetchDataFromUrl(String url){
        URL mUrl = createUrl(url);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(mUrl);
        } catch (IOException e) {
            Log.e(Log_Tag, "Error closing input stream", e);
        }
        return ExtractBooks(jsonResponse);
    }
    static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(Log_Tag, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(Log_Tag, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(Log_Tag, "Problem retrieving books JSON results.", e);
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

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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
}
