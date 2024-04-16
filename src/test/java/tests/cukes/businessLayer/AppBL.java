package tests.cukes.businessLayer;

import tests.cukes.screens.AppLaunchScreen;
import tests.cukes.screens.LoginScreen;

public class AppBL {

    public AppBL provideInvalidDetailsForSignup(String username, String password) {
        new AppLaunchScreen().selectLogin();
        return loginAgain(username, password);
    }

    public AppBL loginAgain(String username, String password) {
        String webErrorMessage = "Your username is invalid!";

        LoginScreen loginScreen = new LoginScreen().enterLoginDetails(username, password).login();
        String actualErrorMessage = loginScreen.getInvalidLoginError();
        System.out.println("actualErrorMessage: " + actualErrorMessage);

        loginScreen.dismissAlert();

        return new AppBL();
    }
}
