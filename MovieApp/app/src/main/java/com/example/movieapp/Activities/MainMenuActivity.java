package com.example.movieapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.movieapp.R;


public class MainMenuActivity extends AppCompatActivity {

    private EditText searchField;
    private Switch imageSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchField = findViewById(R.id.searchField);
        imageSwitch = findViewById(R.id.imageSwitch);
        checkImagesPreference();
    }


    protected void onResume() {
        super.onResume();
        checkImagesPreference();
    }



    public void searchButtonClicked(View v){

        String movieTitle = searchField.getText().toString().replace(' ', '+');
        Log.d("Eservice", movieTitle);


        if(movieTitle.length()!=0){


            Intent intent = new Intent(MainMenuActivity.this, MovieListActivity.class);
            intent.putExtra("searchTitle", movieTitle);
            intent.putExtra("purpose", "search");
            startActivity(intent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "A movie title is required!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    public void myListClicked(View v){


        Intent intent = new Intent(MainMenuActivity.this, MovieListActivity.class);
        intent.putExtra("purpose", "showMyList");
        startActivity(intent);

    }



    public void imagesSwitched(View v){
        checkImagesPreference();
    }



    private void checkImagesPreference(){


        if(imageSwitch.isChecked()){

            SharedPreferences mySharedPreferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean("showImages", true);
            editor.commit();

        } else {
            SharedPreferences mySharedPreferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean("showImages", false);
            editor.commit();

        }
    }
}