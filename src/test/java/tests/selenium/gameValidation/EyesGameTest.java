package tests.selenium.gameValidation;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import utilities.Driver;

import java.io.File;

import static utilities.Wait.waitFor;

public class EyesGameTest {

    private static final String appName = EyesGameTest.class.getSimpleName();
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
    void gameTest() {
        driver.get("https://openfairy.playco.games/");
        waitFor(20);
        eyes.checkWindow("1");
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
