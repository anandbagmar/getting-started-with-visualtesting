package HelloWorld;

import Utilities.*;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

public class Selenium_HelloWorld extends BaseTest {

    public void verifyHelloWorld(int i) {
        DriverUtils.getPathForChromeDriverFromMachine();
        WebDriver driver = new ChromeDriver();

        // Initialize the eyes SDK and set your private 378355API key.
        Eyes eyes = new Eyes();
        BatchInfo batch = new BatchInfo("helloworld");
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setForceFullPageScreenshot(false);
        eyes.setStitchMode(StitchMode.CSS);

        // Set the API key from the env variable. Please read the "Important Note"
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        try {
            // Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
            eyes.open(driver, "Hello world", "Hello world test - " + i, new RectangleSize(800, 800));

            driver.get("https://applitools.com/helloworld");
            eyes.checkWindow("home");

            for (int stepNumber = 0; stepNumber < i; stepNumber++) {
                driver.findElement(By.linkText("?diff1")).click();
                eyes.checkWindow("click-" + stepNumber);
            }
            // Click the "Click me!" button.
            driver.findElement(By.tagName("button")).click();
            eyes.checkWindow("After click");
            // End the test.
            TestResults testResults = eyes.close(false);
            System.out.println("Visual Test results: " + testResults);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
            // Close the browser.
            driver.close();

            // If the test was aborted before eyes.close was called, ends the test as aborted.
            TestResults testResults = eyes.abortIfNotClosed();
            System.out.println("Visual Test results (finally): " + testResults);
        }
    }

    public static void main(String[] args) {
        new Selenium_HelloWorld().verifyHelloWorld(1);
    }
}
