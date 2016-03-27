package com.example.jamescollerton.internet_fridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

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
        setParentScreen();

        initialiseText();
        initialiseTextViews();
        initialiseButtons();

    }


    private void setScreenDimensions(){

        Intent intent = getIntent();
        screenDimensionsMap = (HashMap<String, Integer>)intent.getSerializableExtra(dictionaryKeysList.screenDimensionsMapIntentKey);

    }

    private void setParentScreen(){

        Intent intent = getIntent();

    }

    /**
     *
     * This is used to initialise the positioning of the text on screen.
     *
     */
    private void initialiseText(){



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

    public void launchHomeScreen(){

        Intent homeScreenIntent = new Intent(this, HomeScreen.class);
        startActivity(homeScreenIntent);

    }

}
