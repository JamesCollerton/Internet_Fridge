package com.example.jamescollerton.internet_fridge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SettingsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
//        initialiseSettingsScreenButtons();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialiseSettingsScreenButtons(){

//        Button OKButton = (Button) findViewById(R.id.settingsScreenOKButtonID);
//        Button CancelButton = (Button) findViewById(R.id.settingsScreenCancelButtonID);
//
//        MyFridgeButton settingsScreenButtonOK = new MyFridgeButton(OKButton, null);
//        MyFridgeButton settingsScreenButtonCancel = new MyFridgeButton(CancelButton, null);

    }

}
