package com.example.android.booklisting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, List<Book> object){
        super(context,0,object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Book book = getItem(position);
        TextView rating = (TextView)listItemView.findViewById(R.id.rating);
        rating.setText(book.getRating());

        ImageView tumbline =(ImageView) listItemView.findViewById(R.id.thumbline);
//        URL url = null;
//        try {
//            String image_url =makeImageUrl(book.getImage_url());
//            url = new URL(image_url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        Bitmap bmp = null;
//        try {
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        tumbline.setImageBitmap(bmp);
        URL url = null;
        try {
            String image_url =makeImageUrl(book.getImage_url());
            url = new URL(image_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Picasso.get().load(String.valueOf(url)).into(tumbline);


        TextView title =(TextView)listItemView.findViewById(R.id.book_title);
        title.setText(book.getTitle());

        TextView author =(TextView)listItemView.findViewById(R.id.author_name);
        author.setText(book.getAuthor());

        TextView date =(TextView)listItemView.findViewById(R.id.publish_date);
        date.setText(book.getPublished_date());

        GradientDrawable magnitudeCircle = (GradientDrawable)rating.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(book.getRating());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        return listItemView;
    }

    private String makeImageUrl(String image_url) {

        String url ="";
        String arr[] = image_url.split(":");
        url+=arr[0]+"s:"+arr[1];
        return url;
    }

    private int getMagnitudeColor(String rating) {

        if(rating==null)return ContextCompat.getColor(getContext(), R.color.rating4to5);
        double color = Double.parseDouble(rating);

        if(color<1)return ContextCompat.getColor(getContext(), R.color.ratingLess1);
        else if (color>1 && color<2)return ContextCompat.getColor(getContext(), R.color.rating1to2);
        else if(color>2 && color<3)return ContextCompat.getColor(getContext(), R.color.rating2to3);
        else if(color>3 && color<4)return ContextCompat.getColor(getContext(), R.color.rating3to4);
        else return ContextCompat.getColor(getContext(), R.color.rating4to5);

    }
}
