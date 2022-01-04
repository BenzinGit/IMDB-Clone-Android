package com.example.movieapp.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movieapp.Activities.MovieListActivity;
import com.example.movieapp.Dataclasses.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchMoviesService extends AsyncTask<String, Void, Void> {


    private RequestQueue requestQueue;
    private MovieListActivity activity;
    private APIKey apiKey;

   public SearchMoviesService(Context context, MovieListActivity activity) {
        this.activity = activity;

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);

       Network network = new BasicNetwork(new HurlStack());
       requestQueue = new RequestQueue(cache, network);
       apiKey = new APIKey();

    }



    @Override
    protected Void doInBackground(String... movieTitleToSearch) {

       Log.d("Eservice", "Starting searching for movies with title: "+movieTitleToSearch[0]);

        requestQueue.start();

        ArrayList<Movie> movieList = new ArrayList<>();

        String url = "https://www.myapifilms.com/imdb/idIMDB?title="+movieTitleToSearch[0]+"&filter=3&token="+apiKey.getToken()+"&format=json&language=en-us&limit=6";
        Log.d("Eservice", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Eservice", "Respone received");

                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray movies = data.getJSONArray("movies");


                            for (int i = 0; i < movies.length(); i++){
                                JSONObject movieObject = movies.getJSONObject(i);

                                String title = "";
                                String year = "NaN";
                                String urlPoster = "";
                                String idIMDB = "";

                                if(movieObject.has("title")){
                                    title = movieObject.get("title").toString();
                                }

                                if(movieObject.has("year")){
                                    year = movieObject.get("year").toString();
                                }

                                if(movieObject.has("urlPoster")){
                                    urlPoster = movieObject.get("urlPoster").toString();

                                }
                                if(movieObject.has("idIMDB")){
                                    idIMDB = movieObject.get("idIMDB").toString();
                                }


                                movieList.add(new Movie(title, year, null, null, null, urlPoster, 0, idIMDB, null));
                            }

                            activity.showMoviesFound(movieList);

                        } catch (JSONException e) {
                            e.printStackTrace();


                            try{
                                JSONObject errorObject = response.getJSONObject("error");
                                String errorCode = errorObject.get("code").toString();
                                String errorMsg = errorObject.get("message").toString();


                                activity.showErrorMessage(errorCode+": "+errorMsg);


                            } catch(JSONException e2){

                                activity.showErrorMessage("Unexptected error: "+e2.toString());

                            }


                            Log.d("Eservice", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Eservice", error.toString());
                        activity.showErrorMessage("Could not find a connection to API or internet.");

                    }
                });

        requestQueue.add(jsonObjectRequest);


        return null;



    }

    public void stopRequestQueue(){
        requestQueue.stop();
    }


}
