package com.example.jamescollerton.internet_fridge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Map;

/**
 *
 * This class handles the appearance and functionality of the settings screen.
 *
 */
public class SettingsScreen extends AppCompatActivity {

    /**
     *
     * This creates the screen from where it left off, sets the layout and initialises the toolbar.
     * Next it initialises the OK and cancel buttons at the bottom of the screen.
     *
     * @param savedInstanceState Allows the screen to launch from where it left off.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        initialiseSettingsScreenButtons();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     *
     * Used to initialise the buttons at the bottom of the screen. It finds the buttons from the
     * xml, then sets the actions, fonts and icon locations and margins (which are null as we
     * don't want any icons or margins). Then it initialises the buttons for us.
     *
     */
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
