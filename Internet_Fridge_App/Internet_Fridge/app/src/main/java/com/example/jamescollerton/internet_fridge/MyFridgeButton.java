package com.example.jamescollerton.internet_fridge;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JamesCollerton on 24/02/2016.
 *
 * This class is used to contain all of the information for the buttons on the homescreen. It controls
 * their actions when they are pressed, their positioning and their appearance.
 *
 */
public class MyFridgeButton {

    private String buttonObjectTag, buttonIconFileLocation;
    private Integer buttonObjectID;
    private Button buttonObject;
    private HomeScreen parentHomeScreen;
    private Map<String, Integer> screenDimensionsMap;
    private ScreenDimensionsList screenDimensionsList = new ScreenDimensionsList();
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();

    /**
     *
     * Finds the tag of the button object so we know which button it represents (scan, deal, friends
     * or recipes). Then sets the action when the button is clicked.
     *
     * @param buttonObject The button object on the menu screen.
     * @param parentHomeScreen The home screen object that the button was sourced from.
     *
     * TODO: Maybe come up with a way of doing this without a million if/else statements.
     *
     */
    public MyFridgeButton(Button buttonObject, HomeScreen parentHomeScreen) {

        this.buttonObject = buttonObject;
        this.parentHomeScreen = parentHomeScreen;

        buttonObjectTag = (String) buttonObject.getTag();
        buttonObjectID = buttonObject.getId();

        this.buttonObject.setTransformationMethod(null);

        setButtonFont();
        setButtonMargins();
        setButtonClickAction();
        setButtonIconFileLocation();
        resizeIcon();

    }

    /**
     *
     * This is used to set the button positions relative to the page size. It takes in the button
     * object (in order to change its margins) and also the homes. Interestingly, this can't be
     * a switch statement as the values need to be known at compile time for that to work.
     *
     */
    private void setButtonMargins(){

        screenDimensionsMap = new HashMap<>(parentHomeScreen.getScreenDimensionsMap());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) buttonObject.getLayoutParams();

        double buttonTopMargin = 0;
        double buttonSideMargins;

        if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonScanTag))){
            buttonTopMargin =  screenDimensionsList.homeScreenButtonScanTopPercentageMargin *
                               (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonDealsTag))){
            buttonTopMargin =  screenDimensionsList.homeScreenButtonDealsTopPercentageMargin *
                               (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonFriendsTag))){
            buttonTopMargin =  screenDimensionsList.homeScreenButtonFriendsTopPercentageMargin *
                               (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonRecipesTag))){
            buttonTopMargin =  screenDimensionsList.homeScreenButtonRecipesTopPercentageMargin *
                               (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonMyFridgeTag))){
            buttonTopMargin =  screenDimensionsList.homeScreenButtonMyFridgeTopPercentageMargin *
                               (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);
        }

        buttonSideMargins = screenDimensionsList.homeScreenButtonSidesPercentageMargin *
                            (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);

        params.setMargins((int) buttonSideMargins, (int) buttonTopMargin, (int) buttonSideMargins, 0);

        buttonObject.setLayoutParams(params);

    }

    /**
     *
     * This is used to set the action when the button is clicked.
     *
     */
    private void setButtonClickAction(){

        buttonObject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonScanTag))){

                }
                else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonDealsTag))){

                }
                else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonFriendsTag))){

                }
                else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonRecipesTag))){

                }
                else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonMyFridgeTag))){

                }
            }

        });

    }

    /**
     *
     * This is used to take the button text and set it to KeepCalm, the font
     * for the app.
     *
     */
    private void setButtonFont(){

        TextView tx = (TextView) parentHomeScreen.findViewById(buttonObjectID);
        Typeface custom_font = Typeface.createFromAsset(parentHomeScreen.getAssets(),
                                                        parentHomeScreen.getResources().getString(R.string.appDefaultFontLocation));
        tx.setTypeface(custom_font);

    }

    /**
     *
     * This function is used to set the button icon file location. It acts as a switch on the button
     * tag and then puts the icon location dependant on which button it is.
     *
     */
    private void setButtonIconFileLocation(){

        if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonScanTag))){
            buttonIconFileLocation = parentHomeScreen.getResources().getString(R.string.homeScreenButtonScanIconLocation);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonDealsTag))){
            buttonIconFileLocation = parentHomeScreen.getResources().getString(R.string.homeScreenButtonDealsIconLocation);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonFriendsTag))){
            buttonIconFileLocation = parentHomeScreen.getResources().getString(R.string.homeScreenButtonFriendsIconLocation);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonRecipesTag))){
            buttonIconFileLocation = parentHomeScreen.getResources().getString(R.string.homeScreenButtonRecipesIconLocation);
        }
        else if(buttonObjectTag.equals(parentHomeScreen.getResources().getString(R.string.homeScreenButtonMyFridgeTag))){
            buttonIconFileLocation = parentHomeScreen.getResources().getString(R.string.homeScreenButtonMyFridgeIconLocation);
        }

    }

    /**
     *
     * This is used to resize the icon on the buttons. It pulls the image from the assets/image
     * folder and then resizes it and puts it to screen. The try/ catch block is so that if it
     * can't find the image in the assets folder it exits gracefully.
     *
     * TODO: Have the paddingLeft and paddingRight variables automatically resize themselves.
     * TODO: Come up with a good way of exiting the application.
     *
     */
    private void resizeIcon(){

        try {

            Drawable img = Drawable.createFromStream(parentHomeScreen.getAssets().open(buttonIconFileLocation), null);
            img.setBounds(0, 0, 50, 50);
            buttonObject.setCompoundDrawables(img, null, null, null);

        } catch (IOException settingButtonIconIOException){
            System.exit(1);
        }

    }

}
