package HelloWorld;

import Utilities.*;
import com.applitools.eyes.*;
import com.applitools.eyes.appium.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

public class Selenium_HelloWorldTest {

    @Test
    public void seleniumTest() {
        WebDriver driver = DriverUtils.createChromeDriver();

        // Initialize the eyes SDK and set your private 378355API key.
        Eyes eyes = new Eyes();
        BatchInfo batch = new BatchInfo("helloworld");
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(false));
        eyes.setForceFullPageScreenshot(false);
        eyes.setStitchMode(StitchMode.CSS);

        // Set the API key from the env variable. Please read the "Important Note"
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        try {
            // Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
            double counter = 3;
            eyes.open(driver, "Hello world", "Hello world test - " + counter, new RectangleSize(800, 800));

            driver.get("https://applitools.com/helloworld");
            eyes.checkWindow("home");

            for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
                By linkText = By.linkText("?diff1");
                driver.findElement(linkText).click();
                eyes.checkWindow("click-" + stepNumber);
                eyes.check("click", Target.region(linkText).matchLevel(MatchLevel.CONTENT));
            }
            // Click the "Click me!" button.
            driver.findElement(By.tagName("button")).click();
            eyes.checkWindow("After click");
            // End the test.
            BaseTest.checkResults(eyes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser.
            driver.close();

            // If the test was aborted before eyes.close was called, ends the test as aborted.
            TestResults testResults = eyes.abortIfNotClosed();
            BaseTest.printResults(testResults);
        }
    }
}
