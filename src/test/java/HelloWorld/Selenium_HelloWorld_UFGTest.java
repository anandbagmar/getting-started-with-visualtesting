package HelloWorld;

import Utilities.*;
import com.applitools.eyes.*;
import com.applitools.eyes.appium.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.*;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.model.*;
import com.applitools.eyes.visualgrid.services.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

public class Selenium_HelloWorld_UFGTest {

    private static final String className = Selenium_HelloWorld_UFGTest.class.getSimpleName();
    private static VisualGridRunner visualGridRunner;
    private static BatchInfo batch;
    private Eyes eyes;
    private WebDriver driver;
    private static final String userName = System.getProperty("user.name");

    @BeforeAll
    public static void setUp() {
        visualGridRunner = new VisualGridRunner(10);
        batch = new BatchInfo(userName + "-" + className);
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
        driver = DriverUtils.createChromeDriver();

        eyes = new Eyes(visualGridRunner);
        Configuration config = eyes.getConfiguration();

        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

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
        eyes.addProperty("username", userName);
        eyes.setConfiguration(config);
//        eyes.setLogHandler(new StdoutLogHandler(true));
//        eyes.setIsDisabled(true);

        eyes.open(driver, className, testInfo.getDisplayName(), new RectangleSize(800, 800));
    }

    @Test
    public void seleniumUFGTest() {
        double counter = 3;
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText).click();
            eyes.checkWindow("click-" + stepNumber);
            eyes.check("click", Target.region(linkText).matchLevel(MatchLevel.CONTENT));
        }
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
