package tests.selenium.tests.videoValidation.youtube;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilities.Driver;

import java.io.File;

import static utilities.Wait.waitFor;

public class EyesYouTubeTest {

    private static final String appName = EyesYouTubeTest.class.getSimpleName();
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
        eyes.setForceFullPageScreenshot(false);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setIsDisabled(false);
        eyes.setSaveNewTests(false);
        eyes.addProperty("username", userName);
        eyes.open(driver, appName, testInfo.getDisplayName(), new RectangleSize(1024, 800));
    }

    @AfterEach
    public void afterMethod(TestInfo testInfo) {
        System.out.println("AfterMethod: Test: " + testInfo.getDisplayName());
        boolean isPass = true;
        TestResults testResults = null;
        if (null != eyes) {
            testResults = eyes.close(false);
            TestResultsStatus testResultsStatus = testResults.getStatus();
            if (testResultsStatus.equals(TestResultsStatus.Failed) || testResultsStatus.equals(TestResultsStatus.Unresolved)) {
                isPass = false;
            }
        }
        if (null != driver) {
            driver.quit();
        }
        Assertions.assertTrue(isPass, "Visual differences found.\n" + testResults);
    }

    @Test
    public void visualValidateVideos() {
        driver.get("https://youtu.be/bWtiTkrOUw8?si=DCkYc8R8oWav5NZy&t=211");
        waitFor(2);
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
