package SeleniumTests;

import Utilities.Driver;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.atomic.AtomicBoolean;

public class UFGTest {

    private static final String className = UFGTest.class.getSimpleName();
    private static VisualGridRunner visualGridRunner;
    private static BatchInfo batch;
    private Eyes eyes;
    private WebDriver driver;
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");

    @BeforeAll
    public static void setUp() {
        visualGridRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(10));
        batch = new BatchInfo(userName + "-" + className);
        batch.setSequenceName(UFGTest.class.getSimpleName());
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

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
        driver = Driver.create();

        eyes = new Eyes(visualGridRunner);
        Configuration config = new Configuration();

        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);
//        config.addBrowser(700, 500, BrowserType.FIREFOX_ONE_VERSION_BACK);
//        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM);
//        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM_ONE_VERSION_BACK);
//        config.addBrowser(800, 600, BrowserType.SAFARI);
//        config.addBrowser(800, 600, BrowserType.SAFARI_ONE_VERSION_BACK);

        // Add mobile emulation devices in Portrait/Landscape mode
//        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPhone_11, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Galaxy_Note_2, ScreenOrientation.PORTRAIT);

        config.setBatch(batch);
        config.setMatchLevel(MatchLevel.STRICT);
        config.addProperty("username", userName);
        config.setIsDisabled(false);
        config.setApiKey(APPLITOOLS_API_KEY);
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
    void seleniumUFGTest() {
        double counter = 1;
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText)
                    .click();
            eyes.check("click-" + stepNumber, Target.window()
                    .fully()
                    .layout(By.xpath("//span[contains(@class,'random-number')]")));
        }
        By button = By.tagName("button");
        driver.findElement(button)
                .click();
        eyes.checkWindow("After click");
        eyes.check("combo", Target.window()
                .fully()
                .layout(By.xpath("//p[contains(text(), 'Applitools')]"), By.xpath("//span[contains(@class,'random-number')]")));
    }
}
