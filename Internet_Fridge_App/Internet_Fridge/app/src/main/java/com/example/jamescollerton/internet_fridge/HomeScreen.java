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

public class HomeScreen extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     *
     * @param savedInstanceState
     *
     * Loads up its latest instance (presumably from when its been sleeping).
     * Then sets the content view and the toolbar from the XML. Sets all of the
     * setting buttons on the toolbar and the floating action buttons at the bottom
     * of the page. Then initialises all of the buttons on the page.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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
     * This initialises all of the floating action buttons at the bottom of the page.
     * Eventually this will include a synchronise button and an email button.
     *
     * @param none
     * @return void
     *
     */
    public void floatingActionButtonFunctions() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    /**
     * This creates the option menu in the toolbar which can be dropped down to change
     * settings and to access other options.
     *
     * The inflater part inflates the menu; this adds items to the action bar if it is present.
     *
     * @param none
     * @return true
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * The bottom part is a noinspection SimplifiableIfStatement
     *
     * @param none
     * @return true/ a selected item
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

    /**
     *
     * This is used to initialise each of the four buttons on the home screen: Scan, Recipes,
     * Deals and Friends.
     *
     */
    public void initialiseHomeScreenButtons(){

        Button scanButton = (Button) findViewById(R.id.homeScreenButtonScanID);
        Button dealsButton = (Button) findViewById(R.id.homeScreenButtonDealsID);
        Button friendsButton = (Button) findViewById(R.id.homeScreenButtonFriendsID);
        Button recipesButton = (Button) findViewById(R.id.homeScreenButtonRecipesID);

        HomeScreenButton homeScreenButtonScan = new HomeScreenButton(scanButton);
        HomeScreenButton homeScreenButtonDeals = new HomeScreenButton(dealsButton);
        HomeScreenButton homeScreenButtonFriends = new HomeScreenButton(friendsButton);
        HomeScreenButton homeScreenButtonRecipes = new HomeScreenButton(recipesButton);

    }

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
