package HelloWorld;

import io.appium.java_client.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.net.*;

class Appium_Native_HelloWorld_Base extends BaseTest {

    public static void main(String[] args) throws MalformedURLException {
        String testName = "Appium Native App test";
        basicTest(testName + 1, 2, 0, setupNative());
    }

    protected static void basicTest(String testName, int p1, int p2, AppiumDriver driver) {
        try {
            System.out.println("Running test - " + testName);
            driver.findElement(By.id("digit_" + p1)).click();
            driver.findElement(By.id("op_add")).click();
            driver.findElement(By.id("digit_" + p2)).click();
            driver.findElement(By.id("eq")).click();
        } finally {
            // Close the browser.
            driver.quit();
        }
    }

    private static AppiumDriver setupNative() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("automationName", "uiautomator2");
        dc.setCapability("deviceName", "Google Nexus 5");
        dc.setCapability("platformName", "Android");
        dc.setCapability("appPackage", "com.google.android.calculator");
        dc.setCapability("appActivity", "com.android.calculator2.Calculator");
        dc.setCapability("browserName", "");
        AppiumDriver driver = new AppiumDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), dc);
        return driver;
    }
}
