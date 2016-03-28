package com.example.jamescollerton.internet_fridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * SettingsScreen
 *
 * This class handles the appearance and functionality of the settings screen.
 *
 */
public class SettingsScreen extends AppCompatActivity {

    /**
     *
     * This is used to store the screen dimensions.
     *
     */
    private HashMap<String, Integer> screenDimensionsMap;
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();
    private ScreenDimensionsList screenDimensionsList = new ScreenDimensionsList();

    /**
     *
     * This creates the screen from where it left off, sets the layout and initialises the toolbar.
     * Next it finds the screen dimensions and initialises the OK and cancel buttons at the
     * bottom of the screen.
     *
     * @param savedInstanceState Allows the screen to launch from where it left off.
     *
     * TODO: Make the text bars rearrange according to the screen size.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Sets up the top toolbar.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setScreenDimensions();
        initialiseSettingsScreenButtons();
        initialiseEditTextFields();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     *
     * This is used to find the dimensions of the screen from the intent of the parent activity.
     * They should be inserted as an extra.
     *
     */
    private void setScreenDimensions(){

        Intent intent = getIntent();
        screenDimensionsMap = (HashMap<String, Integer>)intent.getSerializableExtra(dictionaryKeysList.screenDimensionsMapIntentKey);

    }

    /**
     *
     * Used to initialise the buttons at the bottom of the screen. It finds the buttons from the
     * xml, then sets the actions, fonts and icon locations and margins (which are null as we
     * don't want any icons or margins). Then it initialises the buttons for us.
     *
     */
    private void initialiseSettingsScreenButtons(){

        Map<String, Integer> screenDimensionsMap = null;

        Button OKButton = (Button) findViewById(R.id.settingsScreenOKButtonID);
        Button cancelButton = (Button) findViewById(R.id.settingsScreenCancelButtonID);

        String OKButtonAction = "testAction";
        String cancelButtonAction = "testAction";

        String OKButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);
        String cancelButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);

        String OKButtonIconFileLocation = null;
        String cancelButtonIconFileLocation = null;

        Map<String, Double> OKButtonMargins = null;
        Map<String, Double> cancelButtonMargins = null;

        MyFridgeButton settingsScreenButtonOK = new MyFridgeButton(OKButton, OKButtonAction, OKButtonFontLocation, OKButtonIconFileLocation, screenDimensionsMap, OKButtonMargins, this);
        MyFridgeButton settingsScreenButtonCancel = new MyFridgeButton(cancelButton, cancelButtonAction, cancelButtonFontLocation, cancelButtonIconFileLocation, screenDimensionsMap, cancelButtonMargins, this);

    }

    /**
     *
     * This finds all of the text fields from the xml file, then puts all of them except for the top
     * one into a map. The top one is then set a certain percentage of the way down the page, and
     * the remaining ones are spaced around it accordingly.
     *
     */
    private void initialiseEditTextFields(){

        EditText emailEditText = (EditText) findViewById(R.id.settingsScreenEmailTextFieldID);
        EditText usernameEditText = (EditText) findViewById(R.id.settingsScreenUsernameTextFieldID);
        EditText passwordEditText = (EditText) findViewById(R.id.settingsScreenPasswordTextFieldID);

        Map<String, EditText> editTextFields = new HashMap<>();
        editTextFields.put(dictionaryKeysList.settingsScreenUsernameEditTextFieldKey, usernameEditText);
        editTextFields.put(dictionaryKeysList.settingsScreenPasswordEditTextFieldKey, passwordEditText);

        setEditTextFieldMargins(emailEditText, editTextFields);

    }

    /**
     *
     * The top field is set a certain percentage down the page using the screen height and percentages
     * from screenDimensionsList. The sides of the text field are also spaced out in a similar way. The
     * next thing that happens is for all of the remaining text fields, they are looped through and
     * they are spaced below the top one by adding on the height of a text line each time.
     *
     * @param topEditTextField Top edit text field, this is set a percentage down the page.
     * @param remainingEditTextFields List of all of the other text fields so they can be positioned relative.
     *
     */
    private void setEditTextFieldMargins(EditText topEditTextField, Map<String, EditText> remainingEditTextFields){

        CoordinatorLayout.LayoutParams topEditTextFieldParams = (CoordinatorLayout.LayoutParams) topEditTextField.getLayoutParams();

        double topEditTextFieldTopMargin = screenDimensionsList.settingsScreenEditTextEmailTopPercentageMargin *
                (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenHeight);

        double topEditTextFieldSideMargin = screenDimensionsList.settingsScreenEditTextEmailSidePercentageMargin *
                (double) screenDimensionsMap.get(dictionaryKeysList.screenDimensionsMapScreenWidth);

        topEditTextFieldParams.setMargins((int) topEditTextFieldSideMargin, (int) topEditTextFieldTopMargin, (int) topEditTextFieldSideMargin, 0);

        topEditTextField.setLayoutParams(topEditTextFieldParams);

        int i = 0;

        for (Map.Entry<String, EditText> editTextField: remainingEditTextFields.entrySet()) {

            CoordinatorLayout.LayoutParams remainingEditTextFieldParams = (CoordinatorLayout.LayoutParams) editTextField.getValue().getLayoutParams();

            double editTextFieldTopMargin = topEditTextFieldTopMargin + (++i * screenDimensionsList.settingsScreenEditTextLineSize);

            remainingEditTextFieldParams.setMargins((int) topEditTextFieldSideMargin, (int) editTextFieldTopMargin, (int) topEditTextFieldSideMargin, 0);

            editTextField.getValue().setLayoutParams(remainingEditTextFieldParams);

        }

    }

}
