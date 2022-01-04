package com.example.movieapp.Memory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.movieapp.Dataclasses.Movie;

import java.util.ArrayList;


public class SQLLiteProvider {

    private int pos = 0;

    private SQLiteDatabase database;
    private String[] columns = { DBHelper.FIELD1, DBHelper.FIELD2, DBHelper.FIELD3, DBHelper.FIELD4 };

    private ArrayList<Movie> movieList = new ArrayList<Movie>();



    public SQLLiteProvider(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        fetchFromDatabase();
    }

    public ArrayList<Movie> getAllMovies(){
        return movieList;

    }

    public void addMovie(Movie movie){
        movieList.add(movie);
    }


    public void remove(Movie movie){

        for (int i=0; i<movieList.size(); i++){
            if(movie.getId().equals(movieList.get(i).getId())){
                movieList.remove(i);
            }
        }
    }



    public void fetchFromDatabase() {
        movieList.clear();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, columns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            movieList.add(movie);
            cursor.moveToNext();
        }

        for (int i =0; i < movieList.size(); i++){
           Log.d("database", movieList.get(i).getTitle());
        }
        cursor.close();

    }

    public boolean isOnList(String id){

        for (int i =0; i < movieList.size(); i++){
            if (movieList.get(i).getId().equals(id)){
                return true;
            }
        }
        return false;


    }

    public void commitToDatabase(){

        clearDatabase();

        for (int i = 0; i< movieList.size(); i++ ){
            addMovieToDatabase(movieList.get(i));
        }

    }


    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getString(0));
        movie.setTitle(cursor.getString(1));
        movie.setYear(cursor.getString(2));
        movie.setUrlPoster(cursor.getString(3));

        return movie;
    }


    private void clearDatabase(){
        database.execSQL("delete from "+ DBHelper.TABLE_NAME);
    }

    private void addMovieToDatabase(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.FIELD1, movie.getId() );
        values.put(DBHelper.FIELD2, movie.getTitle() );
        values.put(DBHelper.FIELD3, movie.getYear() );
        values.put(DBHelper.FIELD4, movie.getUrlPoster() );

        long insertId = database.insert(DBHelper.TABLE_NAME, null, values);
        Log.d("Eservice", "writing in DB: " + insertId);
    }


}
