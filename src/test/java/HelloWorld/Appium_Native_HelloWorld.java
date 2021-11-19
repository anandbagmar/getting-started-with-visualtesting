package HelloWorld;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import io.appium.java_client.*;
import io.appium.java_client.remote.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.net.*;
import java.util.*;

class Appium_Native_HelloWorld extends BaseTest {

    public static void main(String[] args) throws MalformedURLException {
        String batchName = "appium_native_test-Anand-" + new Date().toString();
        String appName = "Calculator";
        String testName = "Test ";

        // Initialize the eyes SDK and set your private API key.
        Eyes eyes = new Eyes();
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setBatch(new BatchInfo(batchName));

        runNativeTest(eyes, appName, testName + 1, 2, 3, createAppiumDriver());
    }

    protected static void runNativeTest(Eyes eyes, String appName, String testName, int p1, int p2, AppiumDriver driver) {
        try {
            eyes.setLogHandler(new StdoutLogHandler(true));

            eyes.setForceFullPageScreenshot(false);
            // Start the test.
            eyes.open(driver, appName, testName);

            eyes.checkWindow("Hello!");

            driver.findElement(By.id("digit_" + p1)).click();
            eyes.checkWindow("digit_" + p1);
            driver.findElement(By.id("op_add")).click();
            eyes.checkWindow("op_add");
            driver.findElement(By.id("digit_" + p2)).click();
            eyes.checkWindow("digit_" + p2);
            driver.findElement(By.id("eq")).click();
            eyes.checkWindow("Calc works!");
            checkResults(eyes);

        } finally {

            // Close the browser.
            driver.quit();

            // If the test was aborted before eyes.close was called, ends the test as aborted.
            eyes.abort();

        }
    }

    private static AppiumDriver<WebElement> createAppiumDriver() {
        String appiumPort = "4723";
        Integer systemPort = 8201;
        String deviceId = "";
        String deviceName = "Android";
        AppiumDriver<WebElement> driver = null;
        String APPIUM_SERVER_URL = "http://localhost:port/wd/hub";

        System.out.println(String.format("Create AppiumDriver for - %s:%s, appiumPort - %s", deviceId, systemPort, appiumPort));

        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.APP, "");
            capabilities.setCapability("appPackage", "com.google.android.calculator");
            capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");

            capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
            capabilities.setCapability("autoGrantPermissions", true);
            driver = new AppiumDriver<>(new URL(APPIUM_SERVER_URL.replace("port", appiumPort)), capabilities);
            System.out.println(String.format("Created AppiumDriver for - %s:%s, appiumPort - %s", deviceId, systemPort, appiumPort));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in creating Appium Driver");
        }
        return driver;
    }
}