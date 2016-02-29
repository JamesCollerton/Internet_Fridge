package com.example.jamescollerton.internet_fridge;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by JamesCollerton on 28/02/2016.
 *
 * This class controls the floating action buttons at the bottom of the screen on the app. It
 * controls their actions and their appearance.
 *
 * I think that most of this functionality will be contained on the server.
 *
 */
public class HomeScreenFloatingActionButton {

    FloatingActionButton floatingActionButtonObject;

    public HomeScreenFloatingActionButton(FloatingActionButton floatingActionButtonObject){

        this.floatingActionButtonObject = floatingActionButtonObject;

        floatingActionButtonObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
