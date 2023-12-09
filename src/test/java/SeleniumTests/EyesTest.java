package SeleniumTests;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import utilities.Driver;

public class EyesTest {

    private static final String appName = EyesTest.class.getSimpleName();
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static BatchInfo batch;
    double counter = 1;
    private WebDriver driver;
    private Eyes eyes;

    @BeforeAll
    public static void beforeAll() {
        batch = new BatchInfo(userName + "-" + appName);
    }

    @AfterAll
    public static void afterAll() {
        if (null != batch) {
            batch.setCompleted(true);
        }
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("chrome");

        eyes = new Eyes();
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setForceFullPageScreenshot(false);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);
        eyes.setIsDisabled(false);
        eyes.setApiKey(APPLITOOLS_API_KEY);
        eyes.addProperty("username", userName);
        eyes.open(driver, appName, testInfo.getDisplayName(), new RectangleSize(800, 800));
    }

    @AfterEach
    public void afterMethod(TestInfo testInfo) {
        System.out.println("AfterEach: Test - " + testInfo.getDisplayName());
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
    void seleniumEyesTest() {
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");
        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText)
                    .click();
            eyes.checkWindow("click-" + stepNumber);
            eyes.check("click", Target.region(linkText)
                    .matchLevel(MatchLevel.IGNORE_COLORS));
        }
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\".section.button-section\").id=\"clickButton\" ");
        driver.findElement(By.id("clickButton"))
                .click();
        eyes.checkWindow("After click");
    }
}
