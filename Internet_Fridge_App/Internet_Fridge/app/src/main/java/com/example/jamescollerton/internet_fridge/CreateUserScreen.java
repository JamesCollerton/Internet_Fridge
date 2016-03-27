package com.example.jamescollerton.internet_fridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * CreateUserScreen
 *
 * This class is used to hold and control the functionality and appearance of the create user
 * screen.
 *
 */
public class CreateUserScreen extends AppCompatActivity {

    private HashMap<String, Integer> screenDimensionsMap;
    private DictionaryKeysList dictionaryKeysList = new DictionaryKeysList();
    private ScreenCommandClasses screenCommandClasses = new ScreenCommandClasses();
    private ScreenDimensionsList screenDimensionsList = new ScreenDimensionsList();

    /**
     *
     * This class is used to create the user screen. It sets up the toolbar at the top of the
     * page, then it sets the screen dimensions and moves all of the text around the page so they
     * are properly proportioned.
     *
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setScreenDimensions();

        initialiseText();
        initialiseTextViews();
        initialiseButtons();

    }

    /**
     *
     * This is used to set the dimensions of the screen according to the Activity input. It takes
     * them from the extras set on the intent when the intent was started.
     *
     */
    private void setScreenDimensions(){

        Intent intent = getIntent();
        screenDimensionsMap = (HashMap<String, Integer>)intent.getSerializableExtra(dictionaryKeysList.screenDimensionsMapIntentKey);

    }

    /**
     *
     * This is used to initialise the positioning of the text on screen.
     *
     */
    private void initialiseText(){

        EditText emailEditText = (EditText) findViewById(R.id.settingsScreenEmailTextFieldID);
        EditText usernameEditText = (EditText) findViewById(R.id.settingsScreenUsernameTextFieldID);
        EditText passwordEditText = (EditText) findViewById(R.id.settingsScreenPasswordTextFieldID);

        Map<String, EditText> editTextFields = new HashMap<>();
        editTextFields.put(dictionaryKeysList.settingsScreenUsernameEditTextFieldKey, usernameEditText);
        editTextFields.put(dictionaryKeysList.settingsScreenPasswordEditTextFieldKey, passwordEditText);

        setEditTextFieldMargins(emailEditText, editTextFields);

    }

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

    /**
     *
     * This is used to initialise the positioning of the textviews on screen.
     *
     */
    private void initialiseTextViews(){


    }

    /**
     *
     * This is used to initialise the buttons on screen.
     *
     */
    private void initialiseButtons(){

        Button signUpButton = (Button) findViewById(R.id.createUserScreenSignUpButton);

        ScreenCommandClasses.HomeScreenCommand signUpButtonAction = screenCommandClasses.new HomeScreenCommand(this);

        String signUpButtonFontLocation = this.getResources().getString(R.string.appDefaultFontLocation);

        String signUpButtonIconFileLocation = null;

        Map<String, Double> signUpButtonMargins =  new HashMap<String, Double>();

        signUpButtonMargins.put(dictionaryKeysList.buttonBottomMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpBottomPercentageMargin);
        signUpButtonMargins.put(dictionaryKeysList.buttonTopMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpTopPercentageMargin);
        signUpButtonMargins.put(dictionaryKeysList.buttonLeftMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpLeftPercentageMargin);
        signUpButtonMargins.put(dictionaryKeysList.buttonRightMarginPercentage, screenDimensionsList.createUserScreenButtonSignUpRightPercentageMargin);

        MyFridgeButton createUserScreenButtonSignUp = new MyFridgeButton(signUpButton, signUpButtonAction, signUpButtonFontLocation, signUpButtonIconFileLocation, screenDimensionsMap, signUpButtonMargins, this);

    }

    /**
     *
     * This is used to launch the home screen when the user presses the sign me up button and
     * the user's details are registered and sent off to the server to be processed.
     *
     */
    public void launchHomeScreen(){

        Intent homeScreenIntent = new Intent(this, HomeScreen.class);
        startActivity(homeScreenIntent);

    }

}
