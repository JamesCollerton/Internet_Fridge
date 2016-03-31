package com.example.jamescollerton.internet_fridge;

/**
 *
 * StringConstantsList
 *
 * This is a list of all of the string constants used within the app.
 *
 */
public class StringConstantsList {

    // These are the messages displayed at the bottom of the screen when the home screen
    // floating action buttons are pressed.
    public final String homeScreenEmailSnackBarMessage = "Emailing list of out of date items.";
    public final String homeScreenRefreshSnackBarMessage = "Refreshing the fridge contents.";

    // This is the base API URL used for the server. All other URLs are created using this.
    public final String APIURL = "192.168.3.113:8080";

    // These are the base URLs (without parameters) for various actions.
    public final String createUserScreenRegisterUserURLPrefix = "https://" + APIURL + "/api/MyFridge/Email/registerUser/";

}
