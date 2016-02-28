package com.example.jamescollerton.internet_fridge;

import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.*;

import android.util.DisplayMetrics;

public class HomeScreen extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Map<String, Integer> screenDimensionsMap = new HashMap<String, Integer>();
    private Map<String, HomeScreenButton> homeScreenButtonMap = new HashMap<String, HomeScreenButton>();
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();

    /**
     *
     * Loads up its latest instance (presumably from when its been sleeping).
     * Then sets the content view and the toolbar from the XML. Sets all of the
     * setting buttons on the toolbar and the floating action buttons at the bottom
     * of the page. Then initialises all of the buttons on the page.
     *
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setScreenDimensions();
        setSupportActionBar(toolbar);
        floatingActionButtonFunctions();
        initialiseHomeScreenButtons();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


//  ----------------------------------------------------------------------------------------------
//  Default functions inserted by the template. They have been left in for the time being.

    /**
     *
     * This initialises all of the floating action buttons at the bottom of the page.
     * Eventually this will include a synchronise button and an email button.
     *
     */
    public void floatingActionButtonFunctions() {

        FloatingActionButton emailFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.homeScreenEmailFloatingActionButton);
        FloatingActionButton refreshFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.homeScreenRefreshFloatingActionButton);

        HomeScreenFloatingActionButton homeScreenEmailFloatingActionButton =
                new HomeScreenFloatingActionButton(emailFloatingActionButton);
        HomeScreenFloatingActionButton homeScreenRefreshFloatingActionButton =
                new HomeScreenFloatingActionButton(refreshFloatingActionButton);

    }

    /**
     *
     * This creates the option menu in the toolbar which can be dropped down to change
     * settings and to access other options.
     *
     * The inflater part inflates the menu; this adds items to the action bar if it is present.
     *
     * @return true
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    /**
     *
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * The bottom part is a noinspection SimplifiableIfStatement
     *
     * @return selectedItem (An item that has been selected from the options menu.)
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//  ------------------------------------------------------------------------------------------------

//  ------------------------------------------------------------------------------------------------
//  Defined functions.

    /**
     *
     * This is used to set the screen dimensions array. It puts them into a dictionary which
     * can be accessed publicly.
     *
     */
    private void setScreenDimensions(){

        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screenMetrics);
        int screenWidth = screenMetrics.widthPixels;
        int screenHeight = screenMetrics.heightPixels;
        screenDimensionsMap.put(dictionaryKeysList.screenDimensionsMapScreenWidth, screenWidth);
        screenDimensionsMap.put(dictionaryKeysList.screenDimensionsMapScreenHeight, screenHeight);

    }

    /**
     *
     * This is used to initialise each of the four buttons on the home screen: Scan, Recipes,
     * Deals and Friends.
     *
     */
    private void initialiseHomeScreenButtons(){

        Button scanButton = (Button) findViewById(R.id.homeScreenButtonScanID);
        Button dealsButton = (Button) findViewById(R.id.homeScreenButtonDealsID);
        Button friendsButton = (Button) findViewById(R.id.homeScreenButtonFriendsID);
        Button recipesButton = (Button) findViewById(R.id.homeScreenButtonRecipesID);
        Button myFridgeButton = (Button) findViewById(R.id.homeScreenButtonMyFridgeID);

        HomeScreenButton homeScreenButtonScan = new HomeScreenButton(scanButton, this);
        HomeScreenButton homeScreenButtonDeals = new HomeScreenButton(dealsButton, this);
        HomeScreenButton homeScreenButtonFriends = new HomeScreenButton(friendsButton, this);
        HomeScreenButton homeScreenButtonRecipes = new HomeScreenButton(recipesButton, this);
        HomeScreenButton homeScreenButtonMyFridge = new HomeScreenButton(myFridgeButton, this);

        homeScreenButtonMap.put(dictionaryKeysList.homeScreenButtonMapScan, homeScreenButtonScan);
        homeScreenButtonMap.put(dictionaryKeysList.homeScreenButtonMapDeals, homeScreenButtonDeals);
        homeScreenButtonMap.put(dictionaryKeysList.homeScreenButtonMapFriends, homeScreenButtonFriends);
        homeScreenButtonMap.put(dictionaryKeysList.homeScreenButtonMapRecipes, homeScreenButtonRecipes);
        homeScreenButtonMap.put(dictionaryKeysList.homeScreenButtonMapMyFridge, homeScreenButtonMyFridge);

    }

    /**
     *
     * This is used to return the map of screen dimensions. At the moment it is used by the buttons
     * on the screen in order to set their position relative to the screen size.
     *
     * @return screenDimensionsMap (The map of screen dimensions.)
     */
    public Map<String, Integer> getScreenDimensionsMap(){

        return (screenDimensionsMap);

    }

//  ------------------------------------------------------------------------------------------------

//  ------------------------------------------------------------------------------------------------
//  Google API Automatically Generated Functions.

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,       // TODO: choose an action type.
                "HomeScreen Page",      // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jamescollerton.internet_fridge/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,       // TODO: choose an action type.
                "HomeScreen Page",      // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jamescollerton.internet_fridge/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

//    ----------------------------------------------------------------------------------------------
