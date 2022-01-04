package com.example.movieapp.Dataclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {

    private String title;
    private String year;
    private String director;
    private String runtime;
    private String urlPoster;
    private float rating;
    private String id;
    private String plot;
    private ArrayList<Actor> actors;

    public Movie(){

    }



    public Movie(String title, String year, String director, String runtime, ArrayList<Actor> actors, String urlPoster, float rating, String id, String plot){

        this.title = title;
        this.year = year;
        this.director = director;
        this.runtime = runtime;
        this.actors = actors;
        this.urlPoster = urlPoster;
        this.rating = rating;
        this.id = id;
        this.plot = plot;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }


}
