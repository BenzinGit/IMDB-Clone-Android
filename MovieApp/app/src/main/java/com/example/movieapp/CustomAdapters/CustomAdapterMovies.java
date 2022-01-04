package com.example.movieapp.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.Services.ImageURLService;
import com.example.movieapp.Dataclasses.Movie;
import com.example.movieapp.R;

import java.util.ArrayList;


public class CustomAdapterMovies extends BaseAdapter {

    private Context context;
    private ArrayList<Movie> movies;

    public CustomAdapterMovies(Context context, ArrayList<Movie> movies){
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_movie, viewGroup, false);
        }

        Movie tempMovie = (Movie) getItem(i);

        TextView titleBox = (TextView)view.findViewById(R.id.movieTitleInListBox);
        TextView yearBox = (TextView)view.findViewById(R.id.movieYearInListBox);
        ImageView imageBox = view.findViewById(R.id.moviePosterInList);

        titleBox.setText(tempMovie.getTitle());
        yearBox.setText(tempMovie.getYear());


        SharedPreferences preferences = context.getSharedPreferences("preferences",0);

        Boolean showImages = preferences.getBoolean("showImages", true);

        if(showImages){
            new ImageURLService((ImageView) imageBox).execute(tempMovie.getUrlPoster());

        }



        return view;
    }



}
