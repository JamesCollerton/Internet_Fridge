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
 */
public class HomeScreenFloatingActionButton {

    /**
     *
     * Variables for the tag, the actual object itself, the homeScreen that it was passed from and
     * then the string constants variable. This is used to access the messages to be put on the snack
     * bar when the action is taking place.
     *
     */
    String floatingActionButtonObjectTag;
    FloatingActionButton floatingActionButtonObject;
    HomeScreen parentHomeScreen;
    StringConstantsList stringConstants = new StringConstantsList();

    /**
     *
     * Constructor for the floating action button. Controls the action and the message that displays
     * when the button is pressed.
     *
     * @param floatingActionButtonObject The actual floating button object as made on the homescreen.
     * @param parentHomeScreen The parent screen the button came from so we can access its properties.
     *
     */
    public HomeScreenFloatingActionButton(FloatingActionButton floatingActionButtonObject, HomeScreen parentHomeScreen){

        this.floatingActionButtonObject = floatingActionButtonObject;
        this.parentHomeScreen = parentHomeScreen;
        this.floatingActionButtonObjectTag = (String) floatingActionButtonObject.getTag();

        setFloatingActionButtonSnackBar();

    }

    /**
     *
     * This is used to control the action and the message that are displayed when the buttons are
     * pressed on the phone. It loops through trying to find the matching tag, then sets the
     * message from string constants and sets it as the final value (apparently it has to be a final
     * type). Then on click it displays the snack bar and does the action.
     *
     */
    private void setFloatingActionButtonSnackBar(){

        String snackBarActionMessage = "";
        final String snackBarActionMessageFinal;

        if(floatingActionButtonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenFloatingActionButtonEmailTag))){
            snackBarActionMessage = stringConstants.homeScreenEmailSnackBarMessage;
        }
        else if(floatingActionButtonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenFloatingActionButtonRefreshTag))){
            snackBarActionMessage = stringConstants.homeScreenRefreshSnackBarMessage;
        }

        snackBarActionMessageFinal = snackBarActionMessage;

        floatingActionButtonObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, snackBarActionMessageFinal, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
