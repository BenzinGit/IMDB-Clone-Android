package com.example.movieapp.Services;

import android.content.Context;
import android.os.AsyncTask;
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



public class FindSimilarService extends AsyncTask<String, Void, Void> {

    private RequestQueue requestQueue;
    private MovieListActivity activity;
    private APIKey apiKey;

    public FindSimilarService(Context context, MovieListActivity activity) {
        this.activity = activity;

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);

        apiKey = new APIKey();
    }


    @Override
    protected Void doInBackground(String... movieIdIn) {


        requestQueue.start();
        ArrayList<Movie> movieList = new ArrayList<>();

        String token = "4f1d431f-2358-45cd-9592-fdb4fda17fa5";
        String url = "https://www.myapifilms.com/imdb/idIMDB?idIMDB="+movieIdIn[0]+"&token="+apiKey.getToken()+"&format=json&language=en-us&similarMovies=1&filter=3";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Eservice", "responese recived!");

                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray movies = data.getJSONArray("movies");
                            JSONObject movieObject = movies.getJSONObject(0);
                            JSONArray similarMovies = movieObject.getJSONArray("similarMovies");

                            for (int i = 0; i<similarMovies.length(); i++){
                                String name = "";
                                String id = "";
                                JSONObject similarMovieObject = similarMovies.getJSONObject(i);

                                if(similarMovieObject.has("name") ){
                                    name = similarMovieObject.get("name").toString();
                                }

                                if(similarMovieObject.has("id") ){
                                    id = similarMovieObject.get("id").toString();
                                }

                                movieList.add(new Movie(name, null, null, null, null, null, 0, id, null));
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

                                activity.showErrorMessage("Unexpected error: " +e2.toString());

                            }
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
