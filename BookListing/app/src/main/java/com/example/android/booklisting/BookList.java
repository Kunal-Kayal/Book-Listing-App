package com.example.android.booklisting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BookList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {


    private static String BOOKS_REQUEST_URL="https://www.googleapis.com/books/v1/volumes?maxResults=20&q=";
    private static final  int BOOK_LOADER_ID=1;
    private  static  final String LOG_TAG = BookList.class.getSimpleName();
    ArrayAdapter<Book> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
/*
*This is the url creating portion
 */
        Bundle bundle = getIntent().getExtras();
        String input = bundle.getString("Input");
        String arr[] = input.split(" ");
        String add="";
        for(int i=0;i<arr.length-1;i++)add+=arr[i]+"+";
        add+=arr[arr.length-1];
        BOOKS_REQUEST_URL+=add;
 //..................................................
        List<Book> books = new ArrayList<>();
        ListView booklist =(ListView)findViewById(R.id.book_list);

        adapter = new BookAdapter(this,books);

        booklist.setAdapter(adapter);

        booklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    openWebPage(books.get(i).getUrl());
            }
        });

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            Log.i(LOG_TAG, " This is in the initLoader");
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        }else{
            ProgressBar progressBar =(ProgressBar)findViewById(R.id.progressor_bar);
            progressBar.setVisibility(View.GONE);
            TextView textView =(TextView)findViewById(R.id.empty_textview);
            textView.setText(R.string.no_network_connection);
        }



    }

    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install a web browser or check your URL.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BookLoader(this,BOOKS_REQUEST_URL);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        ProgressBar progressBar =(ProgressBar)findViewById(R.id.progressor_bar);
        progressBar.setVisibility(View.GONE);
        Log.i(LOG_TAG, " This is in the onLoadFinished");
        TextView empty_text = (TextView)findViewById(R.id.empty_textview);
        empty_text.setText(R.string.no_books_found);

        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        adapter.clear();
    }
}