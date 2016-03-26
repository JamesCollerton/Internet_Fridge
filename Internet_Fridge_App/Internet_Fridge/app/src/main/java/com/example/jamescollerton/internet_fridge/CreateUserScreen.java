package com.example.jamescollerton.internet_fridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 *
 * CreateUserScreen
 *
 * This class is used to hold and control the functionality and appearance of the create user
 * screen.
 *
 */
public class CreateUserScreen extends AppCompatActivity {

    /**
     *
     * This class is used to create the user screen. It sets up the toolbar at the top of the
     * page.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

}
