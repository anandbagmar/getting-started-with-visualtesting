package selenium.tests.standardWebValidation.mindvalley;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import utilities.Driver;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;
import static utilities.Wait.waitFor;

public class UFGMindValleyTest {

    private static final String appName = UFGMindValleyTest.class.getSimpleName();
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static EyesRunner visualGridRunner;
    private static BatchInfo batch;
    private Eyes eyes;
    private WebDriver driver;

    @BeforeAll
    public static void beforeSuite() {
        System.out.println("BeforeSuite");
        visualGridRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(10));
        visualGridRunner.setDontCloseBatches(true);
        batch = new BatchInfo(userName + "-" + appName);
        batch.setNotifyOnCompletion(false);
        batch.setSequenceName(UFGMindValleyTest.class.getSimpleName());
        batch.addProperty("REPOSITORY_NAME", new File(System.getProperty("user.dir")).getName());
        batch.addProperty("APP_NAME", appName);
    }

    @AfterAll
    public static void afterSuite() {
        System.out.println("AfterSuite");
        if (null != visualGridRunner) {
            System.out.println("Closing VisualGridRunner");
            visualGridRunner.close();
        }
        if (null != batch) {
            System.out.println("Mark batch completed");
            batch.setCompleted(true);
        }
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("BeforeTest: Test: " + testInfo.getDisplayName());
        driver = Driver.create();

        eyes = new Eyes(visualGridRunner);
        Configuration config = new Configuration();

        config.setApiKey(APPLITOOLS_API_KEY);
        config.setBatch(batch);
        config.setIsDisabled(false);
        config.setSaveNewTests(false);
        config.setMatchLevel(MatchLevel.STRICT);
        config.addProperty("username", userName);

        // Add browsers with different viewports
        config.addBrowser(1200, 1080, BrowserType.CHROME);
        config.addBrowser(1080, 1080, BrowserType.FIREFOX);
//        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM);
//        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM_ONE_VERSION_BACK);
//        config.addBrowser(800, 600, BrowserType.SAFARI);
//        config.addBrowser(800, 600, BrowserType.SAFARI_ONE_VERSION_BACK);

        // Add mobile emulation devices in Portrait/Landscape mode
//        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPhone_11, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Galaxy_Note_2, ScreenOrientation.PORTRAIT);

        eyes.setConfiguration(config);
        eyes.setLogHandler(new StdoutLogHandler(true));

        eyes.open(driver, appName, testInfo.getDisplayName(), new RectangleSize(1400, 1080));
    }

    @AfterEach
    void afterMethod(TestInfo testInfo) {
        System.out.println("AfterMethod: Test: " + testInfo.getDisplayName());
        AtomicBoolean isPass = new AtomicBoolean(true);
        if (null != eyes) {
            eyes.closeAsync();
            TestResultsSummary allTestResults = visualGridRunner.getAllTestResults(false);
            allTestResults.forEach(testResultContainer -> {
                System.out.printf("Test: %s\n%s%n", testResultContainer.getTestResults().getName(), testResultContainer);
                displayVisualValidationResults(testResultContainer.getTestResults());
                TestResultsStatus testResultsStatus = testResultContainer.getTestResults().getStatus();
                if (testResultsStatus.equals(TestResultsStatus.Failed) || testResultsStatus.equals(TestResultsStatus.Unresolved)) {
                    isPass.set(false);
                }
            });
        }
        if (null != driver) {
            driver.quit();
        }
        Assertions.assertTrue(isPass.get(), "Visual differences found.");
    }

    @Test
    void userAlreadyExistsTest() {
        driver.get("https://login.mindvalley.com/login");
        waitFor(2);
        eyes.check("Login", Target.window().fully(false));

        driver.findElement(By.xpath("//a[contains(text(), 'Create an account')]")).click();
        waitFor(2);
        eyes.checkWindow("Find your life mystery score");

        driver.findElement(By.id("skipbtn")).click();
        waitFor(2);
        eyes.checkWindow("Create account or login");

        driver.findElement(By.id("create_account-tab")).click();
        eyes.checkWindow("Create account");

        driver.findElement(By.id("create_account-first-name")).sendKeys("Applitools one");
        driver.findElement(By.id("create_account-last-name")).sendKeys("Demo");
        driver.findElement(By.id("create_account-email")).sendKeys(System.getenv("MINDVALLEY_EMAIL"));
        driver.findElement(By.id("create_account-password")).sendKeys(System.getenv("MINDVALLEY_PASSWORD"));
        eyes.checkWindow("New user details filled");

        driver.findElement(By.id("btn-create_account")).click();
        waitFor(2);
        eyes.checkWindow("User already exists");
    }
}
