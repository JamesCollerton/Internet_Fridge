package com.example.jamescollerton.internet_fridge;

/**
 * Created by JamesCollerton on 11/03/2016.
 *
 * This class is used in order to pass the openScreen arguments for the different screens to the
 * button functions. A ScreenCommandClass instance is initiated and then one of the sub classes
 * is also initiated. The openScreen method is then read in in the button function and executed.
 *
 */
public class ScreenCommandClasses {

    /**
     *
     * Used for opening the User's fridge screen. Initiated with a copy of the parent screen, the
     * user fridge screen is then launched from that when the method is called.
     *
     */
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

        private HomeScreen parentScreen;

        ScanScreenCommand(HomeScreen parentScreen){

            this.parentScreen = parentScreen;

        }

        public void openScreen(){



        }

    }

    public class DealsScreenCommand {

        private HomeScreen parentScreen;

        DealsScreenCommand(HomeScreen parentScreen){

            this.parentScreen = parentScreen;

        }

        public void openScreen(){



        }

    }

    public class FriendsScreenCommand {

        private HomeScreen parentScreen;

        FriendsScreenCommand(HomeScreen parentScreen){

            this.parentScreen = parentScreen;

        }

        public void openScreen(){



        }

    }

    public class RecipesScreenCommand {

        private HomeScreen parentScreen;

        RecipesScreenCommand(HomeScreen parentScreen){

            this.parentScreen = parentScreen;

        }

        public void openScreen(){



        }

    }

}