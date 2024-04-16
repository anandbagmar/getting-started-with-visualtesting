package tests.cukes.screens;

import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Driver;
import utilities.EyesConfguration;

import java.time.Duration;

import static utilities.Wait.waitFor;

public class LoginScreen {
    private static final By userNameId = By.id("username");
    private static final By passwordId = By.id("password");
    private static final By loginButtonXpath = By.xpath("//button/i[contains(text(),\"Login\")]");
    private static final By errorMessageId = By.id("flash");
    private final WebDriver driver;
    private final Eyes eyes;

    public LoginScreen() {
        driver = Driver.getDriver();
        eyes = EyesConfguration.getEyes();
    }

    public LoginScreen enterLoginDetails(String username, String password) {
        waitFor(2);
        driver.findElement(userNameId).sendKeys(username);
        driver.findElement(passwordId).sendKeys(password);
        return this;
    }

    public LoginScreen login() {
        driver.findElement(loginButtonXpath).click();
        waitFor(2);
        eyes.checkWindow("Clicked on Login");
        return this;
    }

    public String getInvalidLoginError() {
        WebElement alertText = waitForClickabilityOf(errorMessageId);
        eyes.checkWindow("Invalid Login alert");
        return alertText.getText().trim();
    }

    public LoginScreen dismissAlert() {
        waitFor(2);
        eyes.checkWindow("Invalid Login alert dismissed");
        return this;
    }

    public WebElement waitForClickabilityOf(By elementId) {
        int numberOfSecondsToWait = 10;
        return (new WebDriverWait(driver, Duration.ofSeconds(numberOfSecondsToWait)).until(ExpectedConditions.elementToBeClickable(elementId)));
    }

}
