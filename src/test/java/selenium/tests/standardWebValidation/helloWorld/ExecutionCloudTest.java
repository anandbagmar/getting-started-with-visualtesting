package selenium.tests.standardWebValidation.helloWorld;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;
import org.junit.jupiter.api.*;

import java.io.File;

import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;

public class ExecutionCloudTest {

    private static final String appName = ExecutionCloudTest.class.getSimpleName();
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static VisualGridRunner visualGridRunner;
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

    private static boolean isInject() {
        return null == System.getenv("INJECT") ? false : Boolean.parseBoolean(System.getenv("INJECT"));
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("self_healing");

        eyes = new Eyes(visualGridRunner);
        Configuration config = new Configuration();

        config.setApiKey(APPLITOOLS_API_KEY);
        config.setBatch(batch);
        config.setIsDisabled(false);
        config.setSaveNewTests(false);
        config.setMatchLevel(MatchLevel.STRICT);
        config.addProperty("username", userName);

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);

        // Add mobile emulation devices in Portrait/Landscape mode
//        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);

        eyes.setConfiguration(config);
        eyes.setLogHandler(new StdoutLogHandler(true));

        eyes.open(driver, appName, testInfo.getDisplayName(), new RectangleSize(750, 750));
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
    void executionCloudIdTest() {
        double counter = 1;
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText).click();
            eyes.check("linkText", Target.region(linkText).matchLevel(MatchLevel.LAYOUT2));
            eyes.check("click-" + stepNumber, Target.window()
                    .fully()
                    .layout(By.xpath("//span[contains(@class,'randomnumber')]")));
        }

        if (isInject()) {
            System.out.println("Injecting a change");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").id=\"clkBtn1\"");
        } else {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").id=\"clickButton\"");
        }
        driver.findElement(By.id("clickButton")).click();
        eyes.check("After click", Target.window().layout());
        eyes.check("combo", Target.window()
                .fully()
                .layout(By.xpath("//p[contains(text(), 'Applitools')]"),
                        By.xpath("//span[contains(@class,'random-number')]")));
    }

    @Test
    void executionCloudInnerTextTest() {
        double counter = 1;
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText).click();
            eyes.check("linkText", Target.region(linkText).matchLevel(MatchLevel.LAYOUT2));
            eyes.check("click-" + stepNumber, Target.window()
                    .fully()
                    .layout(By.xpath("//span[contains(@class,'random-number')]")));
        }

        if (isInject()) {
            System.out.println("Injecting a change");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").innerText=\"2024\"");
            System.out.println("after change of 'Click me!' to '2024'");
        } else {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").innerText=\"2023\"");
            System.out.println("after change of 'Click me!' to '2023'");
        }
        driver.findElement(By.xpath("//button[contains(text(), '2023')]")).click();
        eyes.check("After click", Target.window().layout());
        eyes.check("combo", Target.window()
                .fully()
                .layout(By.xpath("//button[contains(text(), newInnerText)]"),
                        By.xpath("//p[contains(text(), 'Applitools')]"),
                        By.xpath("//span[contains(@class,'random-number')]")));
    }

    @FindBy(xpath = "//button[contains(text(),'Click me!')]")
    public WebElement btnClick;

    @Test
    void executionCloudFindByTest() {
        PageFactory.initElements(driver, this);
        double counter = 1;
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText).click();
            eyes.check("linkText", Target.region(linkText).matchLevel(MatchLevel.LAYOUT2));
            eyes.check("click-" + stepNumber, Target.window()
                    .fully()
                    .layout(By.xpath("//span[contains(@class,'random-number')]")));
        }

        if (isInject()) {
            System.out.println("Injecting a change");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").innerText=\"2024\"");
            System.out.println("after change of 'Click me!' to '2024'");
        } else {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").innerText=\"2023\"");
            System.out.println("after change of 'Click me!' to '2023'");
        }
        btnClick.click();
        eyes.check("After click", Target.window().layout());
        eyes.check("combo", Target.window()
                .fully()
                .layout(By.xpath("//button[contains(text(), newInnerText)]"),
                        By.xpath("//p[contains(text(), 'Applitools')]"),
                        By.xpath("//span[contains(@class,'random-number')]")));
    }
}
