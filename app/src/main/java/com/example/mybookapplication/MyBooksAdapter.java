package com.example.mybookapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyBooksAdapter extends ArrayAdapter<MyBooks> {

    private static final String LOG_TAG = MyBooksAdapter.class.getSimpleName();

    public MyBooksAdapter(@NonNull Context context, ArrayList<MyBooks> myBooks) {
        super(context, 0, myBooks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.line_item, parent, false);
        }

        MyBooks myBooks = getItem(position);

        ImageView myBookImageView = (ImageView) listItemView.findViewById(R.id.book_image);
        Picasso.with(listItemView.getContext()).load(myBooks.myBookImage).into(myBookImageView);

        TextView myBookTitleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        myBookTitleTextView.setText(myBooks.myBookTitle);

        TextView myBookAuthorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        myBookAuthorTextView.setText(myBooks.myBookAuthor);

        TextView myBookDateTextView = (TextView) listItemView.findViewById(R.id.book_publish_date);
        myBookDateTextView.setText(myBooks.myBookDate);

       return listItemView;
    }
}
