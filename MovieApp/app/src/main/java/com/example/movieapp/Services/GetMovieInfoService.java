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
import com.example.movieapp.Activities.MovieViewActivity;
import com.example.movieapp.Dataclasses.Actor;
import com.example.movieapp.Dataclasses.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetMovieInfoService extends AsyncTask<String, Void, Void> {

    private RequestQueue requestQueue;
    private MovieViewActivity activity;
    private APIKey apiKey;

    public GetMovieInfoService(Context context, MovieViewActivity activity) {
        this.activity = activity;

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);

        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);

        apiKey = new APIKey();

    }

    @Override
    protected Void doInBackground(String... movieIDIn) {


        String movieID = movieIDIn[0];
        Log.d("Eservice", "Started service to get info about movie with id: " + movieID);

        requestQueue.start();
        ArrayList<Actor> actorList = new ArrayList<>();

        String url = "https://www.myapifilms.com/imdb/idIMDB?idIMDB="+movieID+"&token="+apiKey.getToken()+"&format=json&language=en-us&aka=0&limit=1&actors=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Eservice", "Responese received!");

                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray movies = data.getJSONArray("movies");
                            JSONObject movieObject = movies.getJSONObject(0);



                            String title = "";
                            String year = "NaN";
                            String runtime = "";
                            String urlPoster = "";
                            String id = "";
                            float rating = 0;
                            String directorName = "";
                            String plot = "";

                            if(movieObject.has("title")){
                                title = movieObject.get("title").toString();

                            }
                            if(movieObject.has("year")){
                                year = movieObject.get("year").toString();

                            }
                            if(movieObject.has("runtime")){
                                runtime = movieObject.get("runtime").toString();

                            }
                            if(movieObject.has("urlPoster")){
                                urlPoster = movieObject.get("urlPoster").toString();

                            }
                            if(movieObject.has("idIMDB")){
                                id = movieObject.get("idIMDB").toString();

                            }

                            if(movieObject.has("rating")){
                                rating = Float.parseFloat(movieObject.get("rating").toString())/2;
                            }

                            if(movieObject.has("plot")){
                                plot = movieObject.get("plot").toString();
                            }

                            JSONArray directors = movieObject.getJSONArray("directors");
                            JSONObject directorObject = directors.getJSONObject(0);
                            if(directorObject.has("name")){
                                directorName = directorObject.get("name").toString();

                            }




                            JSONArray actors = movieObject.getJSONArray("actors");

                            for (int i = 0; i < actors.length(); i++) {
                                String actorName = "";
                                String characterName= "";
                                JSONObject actor = actors.getJSONObject(i);

                                if(actor.has("actorName")){
                                    actorName = actor.get("actorName").toString();

                                }

                                if(actor.has("character")){
                                    characterName = actor.get("character").toString();
                                }



                                String imgUrl = "";
                                try {
                                    imgUrl = actor.get("urlPhoto").toString();
                                } catch (Exception e) {
                                    imgUrl = "";
                                }
                                actorList.add(new Actor(actorName, characterName, imgUrl));

                            }


                            activity.showMovieInfo(new Movie(title,year, directorName,runtime,actorList,urlPoster, rating, id, plot));

                        } catch (JSONException e) {

                            try{
                                JSONObject errorObject = response.getJSONObject("error");
                                String errorCode = errorObject.get("code").toString();
                                String errorMsg = errorObject.get("message").toString();

                                activity.showErrorMessage(errorCode+": "+errorMsg);

                            } catch(JSONException e2){


                                activity.showErrorMessage("Could not select that title");
                                Log.d("Eservice",e2.toString() );

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
    // Avslutar requesten
    public void stopRequestQueue(){
        requestQueue.stop();
    }
}
