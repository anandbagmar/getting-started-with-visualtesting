package selenium.tests;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import utilities.Driver;
import org.junit.jupiter.api.*;
import java.io.File;

public class EyesTest {

    private static final String appName = EyesTest.class.getSimpleName();
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
        eyes.open(driver, appName, testInfo.getDisplayName(), new RectangleSize(800, 800));
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
    void seleniumEyesTest() {
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");
        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText)
                    .click();
            eyes.check("linkText", Target.region(linkText)
                    .matchLevel(MatchLevel.LAYOUT2));
            eyes.check("click-" + stepNumber, Target.window()
                    .fully()
                    .layout(By.xpath("//span[contains(@class,'random-number')]")));

        }
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\".section.button-section\").id=\"clickButton\" ");
        driver.findElement(By.id("clickButton"))
                .click();
        eyes.checkWindow("After click");
        eyes.check("combo", Target.window()
                .fully()
                .layout(By.xpath("//p[contains(text(), 'Applitools')]"), By.xpath("//span[contains(@class,'random-number')]")));
    }
}
