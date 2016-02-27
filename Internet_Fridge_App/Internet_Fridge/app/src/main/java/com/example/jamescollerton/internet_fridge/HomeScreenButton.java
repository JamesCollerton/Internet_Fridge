package com.example.jamescollerton.internet_fridge;

import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;


/**
 * Created by JamesCollerton on 24/02/2016.
 */
public class HomeScreenButton {

    /**
     *
     * HomeScreenButton Constructor.
     *
     * @param buttonObject The button object on the menu screen.
     *
     */
    public HomeScreenButton(Button buttonObject) {

        buttonObject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Hello!");
            }
        });
        String buttonObjectID = (String) buttonObject.getTag();
        System.out.println(buttonObjectID);


    }

}
