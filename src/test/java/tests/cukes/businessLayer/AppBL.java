package tests.cukes.businessLayer;

import tests.cukes.screens.AppLaunchScreen;
import tests.cukes.screens.LoginScreen;
import utilities.SoftAssertionsLib;

public class AppBL {

    public AppBL provideInvalidDetailsForSignup(String username, String password) {
        new AppLaunchScreen().selectLogin();
        return loginAgain(username, password);
    }

    public AppBL loginAgain(String username, String password) {
        String expectedErrorMessage = "Your username is invalid!\n" +
                "×";

        LoginScreen loginScreen = new LoginScreen().enterLoginDetails(username, password).login();
        String actualErrorMessage = loginScreen.getInvalidLoginError();
        System.out.println("expectedErrorMessage: " + expectedErrorMessage);
        System.out.println("actualErrorMessage: " + actualErrorMessage);

        SoftAssertionsLib.getSoftAssertions().assertThat(expectedErrorMessage).as("Invalid login error message is incorrect").isEqualTo(actualErrorMessage);
        loginScreen.dismissAlert();

        return new AppBL();
    }
}
