package tests.cukes.screens;

import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.Driver;
import utilities.EyesConfguration;

public class AppLaunchScreen {
    private static final By loginFormLinkText = By.linkText("Form Authentication");
    private final WebDriver driver;
    private final Eyes eyes;

    public AppLaunchScreen() {
        driver = Driver.getDriver();
        eyes = EyesConfguration.getEyes();
    }

    public LoginScreen selectLogin() {
        driver.get("https://the-internet.herokuapp.com");
        driver.findElement(loginFormLinkText).click();
        return new LoginScreen();
    }

}
