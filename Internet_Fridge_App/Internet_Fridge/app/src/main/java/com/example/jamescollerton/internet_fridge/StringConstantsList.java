package com.example.jamescollerton.internet_fridge;

/**
 *
 * StringConstantsList
 *
 * This is a list of all of the string constants used within the app.
 *
 */
public class StringConstantsList {

    public final String homeScreenEmailSnackBarMessage = "Emailing list of out of date items.";
    public final String homeScreenRefreshSnackBarMessage = "Refreshing the fridge contents.";

    public final String APIURL = "192.168.3.35:8080";

    public final String createUserScreenRegisterUserURLPrefix = "https://" + APIURL + "/api/MyFridge/Email/registerUser/";

}
