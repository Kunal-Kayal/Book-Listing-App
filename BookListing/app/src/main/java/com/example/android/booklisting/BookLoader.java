package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<Book> loadInBackground() {
        if(url==null)return null;
        return JsonQuery.fetchDataFromUrl(url);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
