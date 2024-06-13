package tests.selenium.standardWebValidation.helloWorld;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import utilities.Driver;
import utilities.EyesResults;

import java.io.File;

public class FigmaTest {

    private static final String appName = FigmaTest.class.getSimpleName();
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static BatchInfo batch;
    double counter = 1;
    private WebDriver driver;
    private Eyes eyes;

    @BeforeAll
    public static void beforeSuite() {
        batch = new BatchInfo(userName + "-" + appName);
        batch.addProperty("REPOSITORY_NAME", new File(System.getProperty("user.dir")).getName());
        batch.addProperty("APP_NAME", appName);
    }

    @AfterAll
    public static void afterSuite() {
        if (null != batch) {
            batch.setCompleted(true);
        }
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("chrome");

        eyes = new Eyes();
        eyes.setApiKey(APPLITOOLS_API_KEY);
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setIsDisabled(false);
        eyes.setSaveNewTests(false);
        eyes.addProperty("username", userName);
    }

    @AfterEach
    public void afterMethod(TestInfo testInfo) {
        System.out.println("AfterMethod: Test: " + testInfo.getDisplayName());
        if (null != driver) {
            driver.quit();
        }
    }

    private void getResults(Eyes eyes) {
        TestResults testResults = null;
        if (null != eyes) {
            testResults = eyes.close(false);
            EyesResults.displayVisualValidationResults(testResults);
        }
    }

    @Test
    void testAgainstFigma() {
        driver.get("https://applitools.com/helloworld");
        eyes.setBaselineEnvName("Frame 1_1448");
        eyes.open(driver, "FigJam Basics", "Frame 1", new RectangleSize(1024, 800));
        eyes.checkWindow("home");
        getResults(eyes);

        driver.get("https://google.com");

        eyes.setBaselineEnvName("Frame 14176_1448");
        eyes.open(driver, "FigJam Basics", "Frame 14176", new RectangleSize(1024, 800));
        eyes.checkWindow("home");
        getResults(eyes);
    }

    @Test
    void testDifferentViewPortsFigma() {
        driver.get("https://applitools.com/helloworld");
        eyes.setBaselineEnvName("Frame 1_1448");
        eyes.open(driver, "FigJam Basics", "Frame 1", new RectangleSize(1024, 800));
        eyes.checkWindow("home");
        getResults(eyes);

        driver.get("https://google.com");

        eyes.setBaselineEnvName("Frame 14176_1448");
        eyes.open(driver, "FigJam Basics", "Frame 14176", new RectangleSize(1024, 800));
        eyes.checkWindow("home");
        getResults(eyes);
    }
}
