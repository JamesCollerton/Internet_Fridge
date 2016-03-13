package com.example.jamescollerton.internet_fridge;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 *
 * Created by JamesCollerton on 12/03/2016.
 *
 * This class is used to control the error message pop up window. When an error
 * is thrown, a relevant string is passed to this, the window pops up with the
 * error message and on clicking OK the program exits.
 *
 */
public class ErrorDialog {

    /**
     *
     * The parent screen is used to pull the error strings from, and because the error dialog
     * builder needs an activity to build from. The errorMessage string is what is displayed
     * in the dialog.
     *
     */
    Object parentScreen;
    String errorMessage;

    /**
     *
     * Initialises the parentScreen and error message variables.
     *
     * @param parentScreen Whichever screen the dialog is called from.
     * @param errorMessage The error message to display to screen.
     */
    ErrorDialog(Object parentScreen, String errorMessage){

        this.parentScreen = parentScreen;
        this.errorMessage = errorMessage;
        setAlertDialog();

    }

    /**
     *
     * This builds the alert dialog. It builds from the parentScreen and then gets the message
     * and title from the string files. Next it builds up the dialog from those components. Finally
     * it creates and shows the builder.
     *
     */
    private void setAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) parentScreen);

        String errorMessageTitle = ((Activity) parentScreen).getResources().getString(R.string.errorMessageTitle);
        String errorMessageAccept = ((Activity) parentScreen).getResources().getString(R.string.errorMessageAccept);

        builder.setMessage(errorMessage)
                .setTitle(errorMessageTitle)
                .setPositiveButton(errorMessageAccept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        exitApplication();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     *
     * Wrapper for how we exit the application.
     *
     */
    private void exitApplication(){

        System.exit(1);

    }


}
