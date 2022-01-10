package HelloWorld;

import Utilities.*;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.fluent.*;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.model.*;
import com.applitools.eyes.visualgrid.services.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

public class Selenium_HelloWorld_UFGTest {

    private static VisualGridRunner visualGridRunner;
    private static BatchInfo batch;
    private Eyes eyes;
    private WebDriver driver;

    @BeforeAll
    public static void setUp() {
        visualGridRunner = new VisualGridRunner();
        batch = new BatchInfo("helloworld-ufg");
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
        driver = DriverUtils.createChromeDriver();

        // Initialize the eyes SDK and set your private API key.
        eyes = new Eyes(visualGridRunner);
        Configuration config = eyes.getConfiguration();

        // You can get your api key from the Applitools dashboard
        config.setApiKey("APPLITOOLS_API_KEY");

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);
        config.addBrowser(1600, 1200, BrowserType.IE_11);
        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(800, 600, BrowserType.SAFARI);

        // Add mobile emulation devices in Portrait mode
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Galaxy_Note_2, ScreenOrientation.PORTRAIT);

        config.setBatch(batch);

        // Set the configuration object to eyes
        eyes.setConfiguration(config);
        eyes.setLogHandler(new StdoutLogHandler(true));

//        eyes.setIsDisabled(true);

        // Set the API key from the env variable. Please read the "Important Note"
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
        eyes.open(driver, "Hello world", testInfo.getDisplayName(), new RectangleSize(800, 800));
    }

    @Test
    public void seleniumTest() {
        double counter = 3;
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
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        TestResultsSummary allTestResults = visualGridRunner.getAllTestResults(false);
        System.out.println(allTestResults);
    }
}
