package com.example.jamescollerton.internet_fridge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.*;

import android.util.DisplayMetrics;

/**
 *
 * This is the initial HomeScreen class that is ran on startup. It is responsible for holding all
 * of the buttons that are contained on the screen, the menu bar and keeping the screen dimensions.
 *
 */
public class HomeScreen extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Map<String, Integer> screenDimensionsMap = new HashMap<String, Integer>();
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();
    private ScreenDimensionsList screenDimensionsList = new ScreenDimensionsList();
    private ScreenCommandClasses screenCommandClasses = new ScreenCommandClasses();

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
        initialiseHomeScreenFloatingActionButtons();
        initialiseHomeScreenButtons();

//        FOR TESTING THE API CONNECTION.
//        MyFridgeAPIConnection test = new MyFridgeAPIConnection();
//        test.execute("http://192.168.1.174:8080/api/MyFridge");

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
    public void initialiseHomeScreenFloatingActionButtons() {

        FloatingActionButton emailFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.homeScreenEmailFloatingActionButton);
        FloatingActionButton refreshFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.homeScreenRefreshFloatingActionButton);

        HomeScreenFloatingActionButton homeScreenEmailFloatingActionButton =
                new HomeScreenFloatingActionButton(emailFloatingActionButton, this);
        HomeScreenFloatingActionButton homeScreenRefreshFloatingActionButton =
                new HomeScreenFloatingActionButton(refreshFloatingActionButton, this);

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
            launchSettingsScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * This function is used for launching the settings screen for the app.
     *
     */
    public void launchSettingsScreen(){

        Intent settingsIntent = new Intent(this, SettingsScreen.class);
        startActivity(settingsIntent);

    }

    /**
     *
     * This is used to
     *
     */
    public void launchUserFridgeScreen(){

        Intent userFridgeIntent = new Intent(this, UserFridgeScreen.class);
        startActivity(userFridgeIntent);

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

        Map<String, Integer> homeScreenDimensionsMap = this.screenDimensionsMap;

        Button scanButton = (Button) findViewById(R.id.homeScreenButtonScanID);
        Button dealsButton = (Button) findViewById(R.id.homeScreenButtonDealsID);
        Button myFridgeButton = (Button) findViewById(R.id.homeScreenButtonMyFridgeID);
        Button friendsButton = (Button) findViewById(R.id.homeScreenButtonFriendsID);
        Button recipesButton = (Button) findViewById(R.id.homeScreenButtonRecipesID);

        ScreenCommandClasses.UserFridgeScreenCommand scanButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand dealsButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand myFridgeButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand friendsButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);
        ScreenCommandClasses.UserFridgeScreenCommand recipesButtonAction = screenCommandClasses.new UserFridgeScreenCommand(this);

        String scanButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String dealsButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String myFridgeButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String friendsButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String recipesButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);

        String scanButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonScanIconLocation);
        String dealsButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonDealsIconLocation);
        String myFridgeButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonMyFridgeIconLocation);
        String friendsButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonFriendsIconLocation);
        String recipesButtonIconFileLocation = this.getResources().getString(R.string.homeScreenButtonRecipesIconLocation);

        Map<String, Double> scanButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> dealsButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> myFridgeButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> friendsButtonMargins =  new HashMap<String, Double>();
        Map<String, Double> recipesButtonMargins =  new HashMap<String, Double>();

        scanButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonScanBottomPercentageMargin);
        scanButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonScanTopPercentageMargin);
        scanButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonScanLeftPercentageMargin);
        scanButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonScanRightPercentageMargin);

        dealsButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonDealsBottomPercentageMargin);
        dealsButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonDealsTopPercentageMargin);
        dealsButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonDealsLeftPercentageMargin);
        dealsButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonDealsRightPercentageMargin);

        myFridgeButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeBottomPercentageMargin);
        myFridgeButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeTopPercentageMargin);
        myFridgeButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeLeftPercentageMargin);
        myFridgeButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonMyFridgeRightPercentageMargin);

        friendsButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonFriendsBottomPercentageMargin);
        friendsButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonFriendsTopPercentageMargin);
        friendsButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonFriendsLeftPercentageMargin);
        friendsButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonFriendsRightPercentageMargin);

        recipesButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.homeScreenButtonRecipesBottomPercentageMargin);
        recipesButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.homeScreenButtonRecipesTopPercentageMargin);
        recipesButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.homeScreenButtonRecipesLeftPercentageMargin);
        recipesButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.homeScreenButtonRecipesRightPercentageMargin);

        MyFridgeButton homeScreenButtonScan = new MyFridgeButton(scanButton, scanButtonAction, scanButtonFontLocation, scanButtonIconFileLocation, homeScreenDimensionsMap, scanButtonMargins, this);
        MyFridgeButton homeScreenButtonRecipes = new MyFridgeButton(recipesButton, recipesButtonAction, recipesButtonFontLocation, recipesButtonIconFileLocation, homeScreenDimensionsMap, recipesButtonMargins, this);
        MyFridgeButton homeScreenButtonDeals = new MyFridgeButton(dealsButton, dealsButtonAction, dealsButtonFontLocation, dealsButtonIconFileLocation, homeScreenDimensionsMap, dealsButtonMargins, this);
        MyFridgeButton homeScreenButtonMyFridge = new MyFridgeButton(myFridgeButton, myFridgeButtonAction, myFridgeButtonFontLocation, myFridgeButtonIconFileLocation, homeScreenDimensionsMap, myFridgeButtonMargins, this);
        MyFridgeButton homeScreenButtonFriends = new MyFridgeButton(friendsButton, friendsButtonAction, friendsButtonFontLocation, friendsButtonIconFileLocation, homeScreenDimensionsMap, friendsButtonMargins, this);

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
