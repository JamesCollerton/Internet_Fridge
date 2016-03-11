package com.example.jamescollerton.internet_fridge;

/**
 * Created by JamesCollerton on 11/03/2016.
 */
public class HomeScreenCommandClasses {

    public class UserFridgeScreenCommand {

        private HomeScreen parentScreen;

        UserFridgeScreenCommand(HomeScreen parentScreen){

            this.parentScreen = parentScreen;

        }

        public void openScreen(){

            parentScreen.launchUserFridgeScreen();

        }

    }

    public class ScanScreenCommand {

    }

    public class DealsScreenCommand {

    }

    public class FriendsScreenCommand {

    }

    public class RecipesScreenCommand {

    }

}