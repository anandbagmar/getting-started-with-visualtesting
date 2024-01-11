package selenium.tests.gameValidation;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import selenium.tests.standardWebValidation.helloWorld.ExecutionCloudTest;
import utilities.Driver;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;
import static utilities.Wait.waitFor;

public class ClassicRunnerGameTest {

    private static final String appName = ClassicRunnerGameTest.class.getSimpleName();
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static EyesRunner visualGridRunner;
    private static BatchInfo batch;
    private Eyes eyes;
    private WebDriver driver;

    @BeforeAll
    public static void beforeSuite() {
        System.out.println("BeforeSuite");
        visualGridRunner = new ClassicRunner();
        visualGridRunner.setDontCloseBatches(true);
        batch = new BatchInfo(userName + "-" + appName);
        batch.setNotifyOnCompletion(false);
        batch.setSequenceName(ExecutionCloudTest.class.getSimpleName());
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
        config.setAccessibilityValidation(new AccessibilitySettings(AccessibilityLevel.AAA, AccessibilityGuidelinesVersion.WCAG_2_1));
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
    void gameTest() {
        driver.get("https://openfairy.playco.games/");
        waitFor(15);
        eyes.check("1", Target.window().fully());
        WebElement canvasElement = driver.findElement(By.id("pixi-canvas")); // Replace with the actual ID or other locator

        Dimension size = driver.manage().window().getSize();
        System.out.println("size: " + size);

        // Create an instance of the Actions class
        Actions actions = new Actions(driver);

        actions.moveToLocation(200, 950).click().perform();
        waitFor(10);
        eyes.checkWindow("2");
        actions.moveToLocation(250, 900).click().perform();
        waitFor(10);
        eyes.checkWindow("3");
        actions.moveToLocation(250, 950).click().perform();
        waitFor(10);
        eyes.checkWindow("4");
        actions.moveToLocation(600, 400).click().perform();
        waitFor(15);
        eyes.checkWindow("5");;
        driver.findElement(By.xpath("//input")).sendKeys("abc");
        waitFor(2);
        eyes.checkWindow("6");
        actions.moveToLocation(600, 900).click().perform();
        waitFor(15);
        eyes.checkWindow("7");
        actions.moveToLocation(400, 800).click().perform();
        waitFor(10);
        eyes.checkWindow("8");
    }
}
