package tests.cukes.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tests.cukes.businessLayer.AppBL;

public class TheAppSteps {

    @When("I login with invalid credentials - {string}, {string}")
    public void iLoginWithInvalidCredentials(String username, String password) {
        System.out.printf("iLoginWithInvalidCredentials - Username: '%s', Password:'%s' %n", username, password);
        new AppBL().provideInvalidDetailsForSignup(username, password);
    }


    @Then("I try to login again with invalid credentials - {string}, {string}")
    public void iTryToLoginAgainWithInvalidCredentials(String username, String password) {
        System.out.printf("iTryToLoginAgainWithInvalidCredentials - Username: '%s', Password:'%s'%n", username, password);
        new AppBL().loginAgain(username, password);
    }
}
