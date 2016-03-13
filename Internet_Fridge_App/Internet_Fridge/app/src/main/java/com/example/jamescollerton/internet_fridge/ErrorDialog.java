package com.example.jamescollerton.internet_fridge;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by JamesCollerton on 12/03/2016.
 */
public class ErrorDialog {

    Object parentScreen;
    String errorMessage;

    ErrorDialog(Object parentScreen, String errorMessage){

        this.parentScreen = parentScreen;
        this.errorMessage = errorMessage;
        setAlertDialog();

    }

    private void setAlertDialog() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) parentScreen);

        String errorMessageTitle = ((Activity) parentScreen).getResources().getString(R.string.errorMessageTitle);
        String errorMessageAccept = ((Activity) parentScreen).getResources().getString(R.string.errorMessageAccept);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(errorMessage)
                .setTitle(errorMessageTitle)
                .setPositiveButton(errorMessageAccept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        exitApplication();
                    }
                });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void exitApplication(){

        System.exit(1);

    }


}
