package selenium.tests.videoValidation.youtube;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.tests.standardWebValidation.helloWorld.ExecutionCloudTest;
import utilities.Driver;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;
import static utilities.Wait.waitFor;

public class ClassicRunnerYouTubeTest {

    private static final String appName = ClassicRunnerYouTubeTest.class.getSimpleName();
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
    public void visualValidateVideos() {
        driver.get("https://youtu.be/bWtiTkrOUw8?si=DCkYc8R8oWav5NZy&t=211");
        waitFor(3);
        String videoLocation = "document.querySelector('video')";

        for (WebElement video : driver.findElements(By.tagName("video"))) {
            // Pause video and get details
            ((JavascriptExecutor) driver).executeScript(videoLocation + ".pause();");
            double videoLength = (double) ((JavascriptExecutor) driver).executeScript("return " + videoLocation + ".duration;");
            System.out.println("Video length: " + videoLength);

            double duration= 0.15;
            String state = "First Frame";
            System.out.println("Set to " + duration*100 + "% duration " + state);
            ((JavascriptExecutor) driver).executeScript(videoLocation + ".currentTime = " + videoLength * duration + ";");
            waitFor(2);
//            eyes.checkWindow(state + "-full window");
            eyes.check(state + "-viewport", Target.window().fully(false));
            eyes.checkRegion(By.cssSelector("video"), state + "-css");

            state = "Middle Frame";
            System.out.println("Set to " + state);
            ((JavascriptExecutor) driver).executeScript(videoLocation + ".currentTime = " + videoLength / 2 + ";");
            waitFor(2);
//            eyes.checkWindow(state + "-full window");
            eyes.check(state + "-viewport", Target.window().fully(false));
            eyes.checkRegion(By.cssSelector("video"), state + "-css");

            state = "Last Frame";
            duration= 0.95;
            System.out.println("Set to " + duration*100 + "% duration " + state);
            ((JavascriptExecutor) driver).executeScript(videoLocation + ".currentTime = " + videoLength * duration + ";");
            waitFor(2);
//            eyes.checkWindow(state + "-full window");
            eyes.check(state + "-viewport", Target.window().fully(false));
            eyes.checkRegion(By.cssSelector("video"), state + "-css");
        }
    }
}
