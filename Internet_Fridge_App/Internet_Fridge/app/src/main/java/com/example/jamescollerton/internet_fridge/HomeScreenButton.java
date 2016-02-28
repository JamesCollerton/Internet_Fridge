package com.example.jamescollerton.internet_fridge;

import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JamesCollerton on 24/02/2016.
 */
public class HomeScreenButton {

    private String buttonObjectTag;
    private Map<String, Integer> screenDimensionsMap;

    /**
     *
     * Finds the tag of the button object so we know which button it represents (scan, deal, friends
     * or recipes). Then sets the action when the button is clicked.
     *
     * @param buttonObject The button object on the menu screen.
     *
     */
    public HomeScreenButton(Button buttonObject, HomeScreen parentHomeScreen) {

        buttonObjectTag = (String) buttonObject.getTag();

        setButtonMargins(buttonObject, parentHomeScreen);
        setButtonClickAction(buttonObject);

    }

    private void setButtonMargins(Button buttonObject, HomeScreen parentHomeScreen){

        screenDimensionsMap = new HashMap<>(parentHomeScreen.getScreenDimensionsMap());
        System.out.println("" + screenDimensionsMap.get("screenWidth") + ", " + screenDimensionsMap.get("screenHeight") );

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
