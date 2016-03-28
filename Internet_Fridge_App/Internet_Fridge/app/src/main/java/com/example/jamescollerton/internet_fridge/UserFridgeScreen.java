package com.example.jamescollerton.internet_fridge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 *
 * UserFridgeScreen
 *
 * This class deals with the user's fridge screen. It holds all of the data and functionality
 * for the screen.
 *
 */
public class UserFridgeScreen extends AppCompatActivity {

    /**
     *
     * The screen is created from the last saved instance and the toolbar initiated.
     *
     * @param savedInstanceState Allows the screen to launch from where it left off.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Sets up the top toolbar.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
