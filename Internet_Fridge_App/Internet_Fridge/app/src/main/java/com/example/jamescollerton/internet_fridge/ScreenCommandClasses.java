package com.example.jamescollerton.internet_fridge;

/**
 * Created by JamesCollerton on 11/03/2016.
 */
public class ScreenCommandClasses {

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