package com.example.jamescollerton.internet_fridge;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by JamesCollerton on 28/02/2016.
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
