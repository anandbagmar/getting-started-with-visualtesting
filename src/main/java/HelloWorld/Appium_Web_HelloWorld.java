package HelloWorld;

import Utilities.DriverUtils;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class Appium_Web_HelloWorld extends BaseTest {

    public static void main(String[] args) throws MalformedURLException {
        DriverUtils.getPathForChromeDriverFromMachine();
        Appium_Web_HelloWorld appiumWebHelloWorld = new Appium_Web_HelloWorld();
        System.out.println("Start time: " + new Date().toString());
        String batchName = appiumWebHelloWorld.getClass().getSimpleName();
        String appName = Appium_Web_HelloWorld.class.getSimpleName();
        String testName = "Test ";

        // Initialize the eyes SDK and set your private API key.
        Eyes eyes = new Eyes();
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setBatch(new BatchInfo(batchName));
        eyes.setLogHandler(new StdoutLogHandler(true));

        runTest(appName, testName + 1, 1, eyes);
        runTest(appName, testName + 2, 3, eyes);
        System.out.println("End time: " + new Date().toString());
    }

    private static void runTest(String appName, String testName, int numOfSteps, Eyes eyes) throws MalformedURLException {
        AppiumDriver driver = setupMobileWeb();
        eyes.open(driver, appName, testName);
        runAppiumWebTest(eyes, appName, testName, numOfSteps, driver);
    }

    protected static void runAppiumWebTest(Eyes eyes, String appName, String testName, int numOfSteps, WebDriver driver) {
        try {
            driver.get("https://applitools.com/helloworld");
            eyes.checkWindow("Hello!");

            for (int stepNumber = 0; stepNumber < numOfSteps; stepNumber++) {
                driver.findElement(By.linkText("?diff1")).click();
                eyes.checkWindow("diff1-" + stepNumber);

                Thread.sleep(1000);
            }

            driver.findElement(By.tagName("button")).click();
            eyes.checkWindow("Click");
            checkResults(eyes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();

            // If the test was aborted before eyes.close was called, ends the test as aborted.
            TestResults testResults = eyes.abortIfNotClosed();
            System.out.printf("Visual Testing results (finally) - " + testResults);
        }
    }


    private static AppiumDriver setupMobileWeb() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities(new ChromeOptions());
        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9");
        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        dc.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");

//        dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);
//        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
//        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "Google Pixel 2");
//        dc.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
        dc.setCapability("chromedriverExecutable", System.getenv("webdriverChromeDriver"));
        dc.setCapability("recreateChromeDriverSessions", true);
//        dc.setCapability("autoWebview","true");
//        dc.setCapability("platformVersion", "5.0.1");

        // Open browser.
        AppiumDriver driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }
}