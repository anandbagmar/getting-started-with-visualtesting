package SeleniumTests;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import utilities.Driver;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;

public class ExecutionCloudTest {

    private static final String className = ExecutionCloudTest.class.getSimpleName();
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static VisualGridRunner visualGridRunner;
    private static BatchInfo batch;
    private Eyes eyes;
    private WebDriver driver;

    @BeforeAll
    public static void setUp() {
        visualGridRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(10));
        batch = new BatchInfo(userName + "-" + className);
        batch.setSequenceName(ExecutionCloudTest.class.getSimpleName());
        batch.addProperty("Repo", new File(System.getProperty("user.dir")).getName());
    }

    @AfterAll
    public static void afterAll() {
        if (null != visualGridRunner) {
            System.out.println("Closing VisualGridRunner");
            visualGridRunner.close();
        }
        if (null != batch) {
            System.out.println("Mark batch completed");
            batch.setCompleted(true);
        }
    }

    private static boolean isInject() {
        return null == System.getenv("INJECT") ? false : Boolean.parseBoolean(System.getenv("INJECT"));
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("self_healing");

        eyes = new Eyes(visualGridRunner);
        Configuration config = new Configuration();

        config.setApiKey(APPLITOOLS_API_KEY);

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);

        // Add mobile emulation devices in Portrait/Landscape mode
//        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);

        config.setBatch(batch);
        config.setMatchLevel(MatchLevel.STRICT);
        config.addProperty("username", userName);
        config.setIsDisabled(false);
        eyes.setConfiguration(config);
        eyes.setLogHandler(new StdoutLogHandler(true));

        eyes.open(driver, className, testInfo.getDisplayName(), new RectangleSize(750, 750));
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("AfterEach: Test - " + testInfo.getDisplayName());
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
    void executionCloudTest() {
        double counter = 1;
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            driver.findElement(By.xpath("//a[@href='?diff1']"))
                    .click();
            eyes.check("click-" + stepNumber, Target.window()
                    .fully()
                    .layout(By.xpath("//span[contains(@class,'random-number')]")));
        }

        if (isInject()) {
            System.out.println("Injecting a change");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").id=\"clkBtn1\"");
        } else {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").id=\"clickButton\"");
        }
        driver.findElement(By.id("clickButton"))
                .click();
        eyes.check("combo", Target.window()
                .fully()
                .layout(By.xpath("//p[contains(text(), 'Applitools')]"), By.xpath("//span[contains(@class,'random-number')]")));
    }
}
