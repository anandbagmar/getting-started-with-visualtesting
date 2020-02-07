package HelloWorld;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

class Appium_Native_HelloWorld_Base extends BaseTest {

    public static void main(String[] args) throws MalformedURLException {
        String testName = "Appium Native App test";
        runTest(testName + 1, 2, 0);
//        runTest(eyes, appName, testName+2, 3, 9);
    }

    private static void runTest(String testName, int p1, int p2) throws MalformedURLException {
        AppiumDriver driver = setupNative();
        basicTest(testName, p1, p2, driver);
    }

    protected static void basicTest(String testName, int p1, int p2, AppiumDriver driver) {
        try {
            System.out.println("Running test - " + testName);
//            How to switch WebDriver context to WEBVIEW

//            Set contextNames = driver.getContextHandles();
//            driver.context(contextNames.toArray()[0].toString());
//            driver.context("WEBVIEW")

//            eyes.setLogHandler(new StdoutLogHandler(true));

            driver.findElement(By.id("digit_" + p1)).click();
            driver.findElement(By.id("op_add")).click();
            driver.findElement(By.id("digit_" + p2)).click();
            driver.findElement(By.id("op_add")).click();
            driver.findElement(By.id("digit_" + 2)).click();
            driver.findElement(By.id("op_add")).click();
            driver.findElement(By.id("digit_" + 9)).click();
            driver.findElement(By.id("eq")).click();
        } finally {
            // Close the browser.
            driver.quit();
        }
    }

    private static AppiumDriver setupNative() throws MalformedURLException {
        // Set the desired capabilities.
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("automationName", "uiautomator2");
        dc.setCapability("deviceName", "Google Nexus 5");
        dc.setCapability("platformName", "Android");
        dc.setCapability("appPackage", "com.android.calculator2");
        dc.setCapability("appActivity", "com.android.calculator2.Calculator");
        dc.setCapability("browserName", "");

        // Open browser.
        AppiumDriver driver = new AppiumDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }
}