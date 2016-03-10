package com.example.jamescollerton.internet_fridge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Map;

public class SettingsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        initialiseSettingsScreenButtons();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialiseSettingsScreenButtons(){

        Button OKButton = (Button) findViewById(R.id.settingsScreenOKButtonID);
        String testAction = "testAction";
        String testFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String testFileLocation = null;
        Map<String, Integer> screenDimensionsMap = null;
        Map<String, Integer> buttonMargins = null;

        SettingsScreenButton test = new SettingsScreenButton(OKButton, testAction, testFontLocation, testFileLocation, screenDimensionsMap, buttonMargins, this);


//        Button CancelButton = (Button) findViewById(R.id.settingsScreenCancelButtonID);
//
//        MyFridgeButton settingsScreenButtonOK = new MyFridgeButton(OKButton, null);
//        MyFridgeButton settingsScreenButtonCancel = new MyFridgeButton(CancelButton, null);

    }

}
