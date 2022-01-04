package com.example.movieapp.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;



public class ImageURLService extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    public ImageURLService(ImageView imageView) {

        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urlIn) {

        Bitmap bitmap = null;
        try{
            URL url = new URL(urlIn[0]);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        }catch(Exception e){
            Log.d("Eservice", e.toString());
        }
        return bitmap;

    }


    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }
}
