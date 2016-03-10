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

        Map<String, Integer> screenDimensionsMap = null;

        Button OKButton = (Button) findViewById(R.id.settingsScreenOKButtonID);
        Button cancelButton = (Button) findViewById(R.id.settingsScreenCancelButtonID);

        String OKButtonAction = "testAction";
        String cancelButtonAction = "testAction";

        String OKButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String cancelButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);

        String OKButtonIconFileLocation = null;
        String cancelButtonIconFileLocation = null;

        Map<String, Double> OKButtonMargins = null;
        Map<String, Double> cancelButtonMargins = null;

        MyFridgeButton settingsScreenButtonOK = new MyFridgeButton(OKButton, OKButtonAction, OKButtonFontLocation, OKButtonIconFileLocation, screenDimensionsMap, OKButtonMargins, this);
        MyFridgeButton settingsScreenButtonCancel = new MyFridgeButton(cancelButton, cancelButtonAction, cancelButtonFontLocation, cancelButtonIconFileLocation, screenDimensionsMap, cancelButtonMargins, this);

    }

}
