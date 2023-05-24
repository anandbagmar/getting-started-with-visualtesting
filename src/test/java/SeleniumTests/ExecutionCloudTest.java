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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class ExecutionCloudTest {

    private static final String className = ExecutionCloudTest.class.getSimpleName();
    private static VisualGridRunner visualGridRunner;
    private static BatchInfo batch;
    private Eyes eyes;
    private WebDriver driver;
    private static final String userName = System.getProperty("user.name");

    @BeforeAll
    public static void setUp() {
        visualGridRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(10));
        batch = new BatchInfo(userName + "-" + className);
        batch.setSequenceName(ExecutionCloudTest.class.getSimpleName());
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("self_healing");

        eyes = new Eyes(visualGridRunner);
        Configuration config = new Configuration();

        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);

        // Add mobile emulation devices in Portrait mode
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

    private static boolean isInject() {
        return null == System.getenv("INJECT") ? false : Boolean.parseBoolean(System.getenv("INJECT"));
    }

    @Test
    public void executionCloudTest() {
        double counter = 1;
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");
        if (isInject()) {
            System.out.println("Injecting a change");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.section" +
                                                                ".button-section\")" +
                                                                ".className=\"bytton-section\"");
        }

        for(int stepNumber = 0; stepNumber < counter; stepNumber++) {
            driver.findElement(By.xpath("//a[@href='?diff1']"))
                  .click();
            eyes.check("click-" + stepNumber, Target.window()
                                                    .fully()
                                                    .layout(By.xpath("//span[contains(@class,'random-number')]")));
        }
        By button = By.className("div.section.button-section");
//        By button = By.tagName("button");
        driver.findElement(button)
              .click();
        eyes.check("combo", Target.window()
                                  .fully()
                                  .layout(By.xpath("//p[contains(text(), 'Applitools')]"), By.xpath("//span[contains(@class,'random-number')]")));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        eyes.closeAsync();
        TestResultsSummary allTestResults = visualGridRunner.getAllTestResults(false);
        System.out.println(allTestResults);
    }
}
