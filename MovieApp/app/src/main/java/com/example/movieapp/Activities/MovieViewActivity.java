package com.example.movieapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.CustomAdapters.CustomAdapterActors;
import com.example.movieapp.Memory.SQLLiteProvider;
import com.example.movieapp.Services.ImageURLService;
import com.example.movieapp.Services.GetMovieInfoService;
import com.example.movieapp.Dataclasses.Movie;
import com.example.movieapp.R;


public class MovieViewActivity extends AppCompatActivity {

    private SQLLiteProvider memoryProvider;

    private ImageView poster;
    private TextView title;
    private TextView runtime;
    private TextView yearBox;
    private TextView plotBox;

    private RatingBar ratingBar;
    private ListView listView;
    private Button addToListButton;
    private Button findSimilarButton;
    private RelativeLayout loadingScreen;
    private RelativeLayout errorScreen;
    private TextView errorText;


    private GetMovieInfoService getMovieInfoService;
    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        memoryProvider = new SQLLiteProvider(getApplicationContext());

        poster = findViewById(R.id.poster);
        title = findViewById(R.id.movieTitleBox);
        runtime = findViewById(R.id.runtimeBox);
        listView = findViewById(R.id.listViewMovieList);
        yearBox = findViewById(R.id.yearBox);
        ratingBar = findViewById(R.id.ratingBar);
        loadingScreen = findViewById(R.id.loadingScreen);
        errorScreen = findViewById(R.id.errorScreen);
        errorText = findViewById(R.id.errorText);
        addToListButton = findViewById(R.id.addToListButton);
        findSimilarButton = findViewById(R.id.findSimilarButton);
        plotBox = findViewById(R.id.plotBox);

    }

    public void showErrorMessage(String errorMsg){

        loadingScreen.setVisibility(View.GONE);
        Toast toast = Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG);
        toast.show();
        finish();

    }

    public void showMovieInfo(Movie movie){

        currentMovie = movie;
        loadingScreen.setVisibility(View.GONE);
        addToListButton.setVisibility(View.VISIBLE);
        findSimilarButton.setVisibility(View.VISIBLE);
        title.setText(movie.getTitle());
        runtime.setText(movie.getRuntime());
        yearBox.setText(movie.getYear());
        ratingBar.setRating(movie.getRating());
        plotBox.setText(movie.getPlot());

        checkIfOnList();

        CustomAdapterActors customAdapterActors = new CustomAdapterActors(MovieViewActivity.this, movie.getActors());
        listView.setAdapter(customAdapterActors);

        SharedPreferences preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        if(preferences.getBoolean("showImages", true)){
            new ImageURLService((ImageView) findViewById(R.id.poster)).execute(movie.getUrlPoster());
        }


    }


    public void findSimilarClicked(View v){

             Intent intent = new Intent(MovieViewActivity.this, MovieListActivity.class);
             intent.putExtra("movieID", currentMovie.getId());
             intent.putExtra("purpose", "findSimilar");
             startActivity(intent);
    }


    public void onPause() {
        super.onPause();

       memoryProvider.commitToDatabase();


    }

    @Override
    public void onStop() {

        super.onStop();
        getMovieInfoService.stopRequestQueue();

    }


    private void checkIfOnList(){

        if(memoryProvider.isOnList(currentMovie.getId())){

             addToListButton.setBackgroundColor(Color.parseColor("#690e0e"));
             addToListButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
             addToListButton.setText("Remove from list");
        }
        else {

             addToListButton.setBackgroundColor(Color.parseColor("#E84545"));
             addToListButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
             addToListButton.setText("Add to list");

        }
    }


    @Override
    public void onResume() {

        super.onResume();
        memoryProvider.fetchFromDatabase();
        Log.d("Eservice", "MovieViewActivity started");
        if(getIntent().getStringExtra("purpose")!=null) {

            if (getIntent().getStringExtra("purpose").equals("showMovie")) {
                currentMovie = null;

                String id = getIntent().getStringExtra("movieID");
                addToListButton.setVisibility(View.GONE);
                findSimilarButton.setVisibility(View.GONE);

                getMovieInfoService = new GetMovieInfoService(this, this);
                getMovieInfoService.execute(id);
                getIntent().removeExtra("purpose");

            }
        }
    }

    public void addToListClicked(View v){

        if(currentMovie!= null) {

            if (memoryProvider.isOnList(currentMovie.getId())) {
                Toast.makeText(getApplicationContext(), "Movie removed from your list", Toast.LENGTH_SHORT).show();

                memoryProvider.remove(currentMovie);
                Log.d("Eservice", "Removing it");

            } else {

                memoryProvider.addMovie(currentMovie);
                Toast.makeText(getApplicationContext(), "Movie added to your list", Toast.LENGTH_SHORT).show();

                Log.d("Eservice", "Adding it");

            }
            checkIfOnList();
        }

    }

    public void homeClicked(View v){

        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void backClicked(View v){
        finish();

    }

}