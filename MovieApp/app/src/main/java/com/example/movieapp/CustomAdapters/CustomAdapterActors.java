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

import com.example.movieapp.Dataclasses.Actor;
import com.example.movieapp.Services.ImageURLService;
import com.example.movieapp.R;

import java.util.ArrayList;



public class CustomAdapterActors extends BaseAdapter {

    private Context context;
    private ArrayList<Actor> actors;

    public CustomAdapterActors(Context context, ArrayList<Actor> actors){
        this.actors = actors;
        this.context = context;
    }

    @Override
    public int getCount() {
        return actors.size();
    }

    @Override
    public Object getItem(int i) {
        return actors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_actor, viewGroup, false);
        }

        Actor actor = (Actor) getItem(i);

        TextView nameBox = (TextView)view.findViewById(R.id.actorNameBox);
        TextView roleBox = (TextView)view.findViewById(R.id.actorRoleBox);
        ImageView imageBox = view.findViewById(R.id.actorImage);

        nameBox.setText(actor.getActorName());
        roleBox.setText(actor.getCharacterName());

        SharedPreferences preferences = context.getSharedPreferences("preferences",0);
        Boolean showImages = preferences.getBoolean("showImages", true);

        if(showImages){
            new ImageURLService((ImageView) imageBox).execute(actor.getImgUrl());

        }

        return view;
    }
}
