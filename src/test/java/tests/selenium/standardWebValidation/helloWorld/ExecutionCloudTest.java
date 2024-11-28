package tests.selenium.standardWebValidation.helloWorld;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecutionCloudTest {

    private static final String appName = ExecutionCloudTest.class.getSimpleName();
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = "0oxdZy9qSy2Sf5m898PHJS838QpVp2tOxwQWquPBOyF0110";
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

    private static WebDriver createExecutionCloudRemoteDriver() {
        WebDriver innerDriver;
        ChromeOptions chromeOptions = new ChromeOptions();
        DesiredCapabilities capabilities = new DesiredCapabilities(chromeOptions);
        capabilities.setCapability("applitools:apiKey", APPLITOOLS_API_KEY);
//        System.out.println("Using Tunnel");
//        capabilities.setCapability("applitools:tunnel", true);

        String executionCloudURL = Eyes.getExecutionCloudURL();
        System.out.println("executionCloudURL: " + executionCloudURL);
        try {
            innerDriver = new RemoteWebDriver(new URI(executionCloudURL).toURL(), capabilities);
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("Error creating a new RemoteWebDriver for url: " + executionCloudURL, e);
        }
        return innerDriver;
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver = createExecutionCloudRemoteDriver();

        eyes = new Eyes(visualGridRunner);
        Configuration config = new Configuration();

        config.setApiKey(APPLITOOLS_API_KEY);
        config.setBatch(batch);
        config.setIsDisabled(false);
        config.setSaveNewTests(false);
        config.setMatchLevel(MatchLevel.STRICT);
        config.addProperty("username", userName);
        config.setAccessibilityValidation(new AccessibilitySettings(AccessibilityLevel.AA, AccessibilityGuidelinesVersion.WCAG_2_1));

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(800, 600, BrowserType.CHROME_ONE_VERSION_BACK);
        config.addBrowser(800, 600, BrowserType.CHROME_TWO_VERSIONS_BACK);
        config.addBrowser(700, 500, BrowserType.EDGE_CHROMIUM);

        // Add mobile emulation devices in Portrait/Landscape mode
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);

        eyes.setConfiguration(config);
        eyes.setLogHandler(new StdoutLogHandler(true));

        eyes.open(driver, appName, testInfo.getDisplayName(), new RectangleSize(750, 750));
    }

    private static void displayVisualValidationResults(TestResults result) {
        boolean hasMismatches = false;
        System.out.println(result);
        System.out.println("\tTest Name: " + result.getName() + " :: " + result);
        System.out.println("\tTest status: " + result.getStatus());
        SessionAccessibilityStatus accessibilityStatus = result.getAccessibilityStatus();
        String accessibilityResults = (accessibilityStatus == null) ? "Accessibility not set" : "Accessibility validation: " + accessibilityStatus.getStatus().getName() + " at level: " + accessibilityStatus.getLevel().getName() + " with guideline version: " + accessibilityStatus.getVersion().getName();
        System.out.printf("\t\tName = '%s', " +
                          "%n\t\tBrowser = %s, OS = %s, Viewport = %dx%d, " +
                          "%n\t\t%s, " +
                          "%n\t\tmatched = %d, mismatched = %d, missing = %d, aborted = %s%n",
                          result.getName(),
                          result.getHostApp(),
                          result.getHostOS(),
                          result.getHostDisplaySize().getWidth(),
                          result.getHostDisplaySize().getHeight(),
                          accessibilityResults,
                          result.getMatches(),
                          result.getMismatches(),
                          result.getMissing(),
                          (result.isAborted() ? "aborted" : "no"));
//        StepInfo[] stepsInfo = result.getStepsInfo();
//        String diffImage = stepsInfo[0].getApiUrls().getDiffImage();
        System.out.println("Results available here: " + result.getUrl());
        hasMismatches = result.getMismatches() != 0 || result.isAborted();
        System.out.println("Visual validation failed? - " + hasMismatches);
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
//        eyes.checkWindow("home");
        eyes.check("home", Target.window().fully().layout());

        By linkText = By.linkText("?diff1");
        eyes.check("linkText", Target.region(linkText).matchLevel(MatchLevel.LAYOUT2));

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            driver.findElement(linkText).click();
            eyes.check("click-" + stepNumber, Target.window().fully()
                    .layout(By.xpath("//span[contains(@class,'random-number')]")));
        }

        if (isInject()) {
            System.out.println("Injecting a change");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").id=\"clkBtn1\"");
        } else {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"button\").id=\"clickButton\"");
        }
        driver.findElement(By.id("clickButton")).click();
        eyes.check("combo", Target.window()
                .fully()
                .layout(By.xpath("//p[contains(text(), 'Applitools')]"),
                        By.xpath("//span[contains(@class,'random-number')]")));
    }
}
