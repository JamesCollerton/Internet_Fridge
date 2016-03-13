package com.example.jamescollerton.internet_fridge;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private String buttonIconFileLocation;
    private Integer buttonObjectID;
    private Button buttonObject;
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();
    private AppCompatActivity parentScreen;

    /**
     *
     * Finds the tag of the button object so we know which button it represents (scan, deal, friends
     * or recipes). Then sets the action when the button is clicked.
     *
     * @param buttonObject The button object on the menu screen.
     *
     */
    public MyFridgeButton(Button buttonObject, Object onClickAction, String fontLocation,
                          String iconFileLocation, Map<String, Integer> screenDimensionsMap,
                          Map<String, Double> buttonMargins, AppCompatActivity parentScreen) {

        this.parentScreen = parentScreen;
        this.buttonObject = buttonObject;
        buttonObjectID = buttonObject.getId();

        this.buttonObject.setTransformationMethod(null);

        if(fontLocation != null){ setButtonFont(fontLocation); };
        if(screenDimensionsMap != null && buttonMargins != null){ setButtonMargins(screenDimensionsMap, buttonMargins); };
        if(onClickAction != null){ setButtonClickAction(onClickAction); }
        if(iconFileLocation != null){
            setButtonIconFileLocation(iconFileLocation);
            resizeIcon();
        }

    }

    /**
     *
     * This is used to set the button positions relative to the page size. It takes in the button
     * object (in order to change its margins) and also the homes. Interestingly, this can't be
     * a switch statement as the values need to be known at compile time for that to work.
     *
     */
    private void setButtonMargins(Map<String, Integer> screenDimensionsMap, Map<String, Double> buttonMargins){

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) buttonObject.getLayoutParams();

        double buttonTopMargin, buttonBottomMargin, buttonLeftMargin, buttonRightMargin;

        buttonTopMargin = buttonMargins.get(dictionaryKeysList.buttonTopMarginPercentage) *
                          (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);

        buttonBottomMargin = buttonMargins.get(dictionaryKeysList.buttonBottomMarginPercentage) *
                             (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);

        buttonLeftMargin = buttonMargins.get(dictionaryKeysList.buttonLeftMarginPercentage) *
                           (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenWidth);

        buttonRightMargin = buttonMargins.get(dictionaryKeysList.buttonRightMarginPercentage) *
                            (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenWidth);

        params.setMargins((int) buttonLeftMargin, (int) buttonTopMargin, (int) buttonRightMargin, (int) buttonBottomMargin);

        buttonObject.setLayoutParams(params);

    }

    /**
     *
     * This is used to set the action when the button is clicked.
     *
     * TODO: Write an exit function class for these exceptions.
     *
     */
    private void setButtonClickAction(final Object onClickAction){

        buttonObject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Method openScreenMethod = null;

                try {
                    openScreenMethod = onClickAction.getClass().getMethod("openScreen");
                } catch (SecurityException e) {
                    String errorString = parentScreen.getResources().getString(R.string.errorMessageOpenScreenSecurityException);
                    ErrorDialog errorDialog = new ErrorDialog(parentScreen, errorString);
                } catch (NoSuchMethodException e) {
                    String errorString = parentScreen.getResources().getString(R.string.errorMessageOpenScreenNoSuchMethodException);
                    ErrorDialog errorDialog = new ErrorDialog(parentScreen, errorString);
                }

                try {
                    openScreenMethod.invoke(onClickAction);
                } catch (IllegalArgumentException e) {
                    String errorString = parentScreen.getResources().getString(R.string.errorMessageOpenScreenIllegalArgumentException);
                    ErrorDialog errorDialog = new ErrorDialog(parentScreen, errorString);
                } catch (IllegalAccessException e) {
                    String errorString = parentScreen.getResources().getString(R.string.errorMessageOpenScreenIllegalAccessException);
                    ErrorDialog errorDialog = new ErrorDialog(parentScreen, errorString);
                } catch (InvocationTargetException e) {
                    String errorString = parentScreen.getResources().getString(R.string.errorMessageOpenScreenInvocationTargetException);
                    ErrorDialog errorDialog = new ErrorDialog(parentScreen, errorString);
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
    private void setButtonFont(String fontLocation){

        TextView tx = (TextView) parentScreen.findViewById(buttonObjectID);
        Typeface custom_font = Typeface.createFromAsset(parentScreen.getAssets(), fontLocation);
        tx.setTypeface(custom_font);

    }

    /**
     *
     * This function is used to set the button icon file location. It acts as a switch on the button
     * tag and then puts the icon location dependant on which button it is.
     *
     */
    private void setButtonIconFileLocation(String iconFileLocation){

        buttonIconFileLocation = iconFileLocation;

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

            Drawable img = Drawable.createFromStream(parentScreen.getAssets().open(buttonIconFileLocation), null);
            img.setBounds(0, 0, 50, 50);
            buttonObject.setCompoundDrawables(img, null, null, null);

        } catch (IOException settingButtonIconIOException){
            String errorString = parentScreen.getResources().getString(R.string.errorMessageOpenScreenIconIOException);
            ErrorDialog errorDialog = new ErrorDialog(parentScreen, errorString);
        }

    }

}
