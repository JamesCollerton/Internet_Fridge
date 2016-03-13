package com.example.jamescollerton.internet_fridge;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

/**
 * Created by JamesCollerton on 12/03/2016.
 */
public class ErrorDialog {

    Object parentScreen;

    ErrorDialog(Object parentScreen){

        this.parentScreen = parentScreen;
        setAlertDialog();

    }

    public void setAlertDialog() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) parentScreen);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Hey")
                .setTitle("Hey");

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
