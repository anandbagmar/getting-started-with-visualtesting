package HelloWorld;

import io.appium.java_client.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.net.*;

class Appium_Native_HelloWorld_BaseTest {

    @Test
    public void appiumBaseTest() {
        String testName = "Appium Native App test";
        AppiumDriver driver = setupNative();

        int p1 = 3;
        int p2 = 5;
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

    private AppiumDriver setupNative() {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("automationName", "uiautomator2");
        dc.setCapability("deviceName", "Google Nexus 5");
        dc.setCapability("platformName", "Android");
        dc.setCapability("appPackage", "com.google.android.calculator");
        dc.setCapability("appActivity", "com.android.calculator2.Calculator");
        dc.setCapability("browserName", "");
        AppiumDriver driver = null;
        try {
            driver = new AppiumDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }
}
