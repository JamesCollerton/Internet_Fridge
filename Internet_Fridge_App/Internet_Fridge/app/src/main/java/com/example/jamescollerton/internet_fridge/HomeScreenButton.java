package com.example.jamescollerton.internet_fridge;

import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.app.Activity;

/**
 * Created by JamesCollerton on 24/02/2016.
 */
public class HomeScreenButton {

    private String buttonObjectTag;

    /**
     *
     * Finds the tag of the button object so we know which button it represents (scan, deal, friends
     * or recipes). Then sets the action when the button is clicked.
     *
     * @param buttonObject The button object on the menu screen.
     *
     */
    public HomeScreenButton(Button buttonObject) {

        buttonObjectTag = (String) buttonObject.getTag();

        setButtonMargins(buttonObject);
        setButtonClickAction(buttonObject);

    }

    private void setButtonMargins(Button buttonObject){

//        DisplayMetrics metrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int width1 = metrics.widthPixels;
//        int height1 = metrics.heightPixels;

//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//
//        Display display = getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        int height = display.getHeight();


    }

    /**
     *
     * This is used to set the action when the button is clicked.
     *
     * @param buttonObject The button object on the menu screen.
     *
     */
    private void setButtonClickAction(Button buttonObject){

        buttonObject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(buttonObjectTag);
            }
        });

    }

}
