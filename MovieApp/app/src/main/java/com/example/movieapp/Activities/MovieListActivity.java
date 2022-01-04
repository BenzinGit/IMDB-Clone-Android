package com.example.movieapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.movieapp.CustomAdapters.CustomAdapterMovies;
import com.example.movieapp.Dataclasses.Movie;
import com.example.movieapp.Memory.SQLLiteProvider;
import com.example.movieapp.R;
import com.example.movieapp.Services.FindSimilarService;
import com.example.movieapp.Services.SearchMoviesService;

import java.util.ArrayList;



public class MovieListActivity extends AppCompatActivity {

    private ListView listView;
    private RelativeLayout loadingScreen;
    private RelativeLayout errorScreen;
    private TextView errorText;
    private SQLLiteProvider memoryProvider;


    private SearchMoviesService searchMoviesService;
    private FindSimilarService findSimilarService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        listView = findViewById(R.id.listViewMovieList);
        loadingScreen = findViewById(R.id.loadingScreen);
        errorScreen = findViewById(R.id.errorScreen);
        errorText = findViewById(R.id.errorText);
        memoryProvider = new SQLLiteProvider(getApplicationContext());



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Movie movie = (Movie) parent.getAdapter().getItem(position);
                Log.d("Eservice", movie.getTitle() +" clicked on. Changing activity");


                Intent intent = new Intent(MovieListActivity.this, MovieViewActivity.class);
                intent.putExtra("movieID", movie.getId());
                intent.putExtra("purpose","showMovie");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {

        super.onStop();


        if(searchMoviesService != null){
            searchMoviesService.stopRequestQueue();
        }


        if(findSimilarService != null){
           findSimilarService.stopRequestQueue();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getIntent().getStringExtra("purpose")!= null ) {


            if (getIntent().getStringExtra("purpose").equals("search")) {


                String movieSearchTitle = getIntent().getStringExtra("searchTitle");
                searchMoviesService = new SearchMoviesService(this, this);
                searchMoviesService.execute(movieSearchTitle);


                getIntent().removeExtra("purpose");


            } else if (getIntent().getStringExtra("purpose").equals("findSimilar")) {


                String movieID = getIntent().getStringExtra("movieID");
                findSimilarService = new FindSimilarService(this, this);
                findSimilarService.execute(movieID);

                getIntent().removeExtra("purpose");


            } else if(getIntent().getStringExtra("purpose").equals("showMyList")){

                memoryProvider.fetchFromDatabase();
                showMoviesFound(memoryProvider.getAllMovies());

            }
        }
    }


    public void showMoviesFound(ArrayList<Movie> movieList){

        loadingScreen.setVisibility(View.GONE);
        CustomAdapterMovies customAdapter = new CustomAdapterMovies(MovieListActivity.this, movieList);
        listView.setAdapter(customAdapter);
    }


    public void showErrorMessage(String errorMsg){
        loadingScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.VISIBLE);
        errorText.setText(errorMsg);
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