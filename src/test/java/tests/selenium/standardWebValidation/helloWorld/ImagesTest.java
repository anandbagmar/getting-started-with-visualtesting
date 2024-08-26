package tests.selenium.standardWebValidation.helloWorld;

import com.applitools.eyes.*;
import com.applitools.eyes.images.ImageRunner;
import com.applitools.eyes.images.Target;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import utilities.Driver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;

public class ImagesTest {

    private static final String appName = "Applitools-Images";
    private static final String testName = "Applitools-logo";
    private static final String userName = System.getProperty("user.name");
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private final RectangleSize viewportSize = new RectangleSize(1280, 1024);
    private WebDriver driver;
    private TestInfo testInfo;

    @BeforeAll
    public static void beforeSuite() {
    }

    @AfterAll
    public static void afterSuite() {
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("chrome");
        this.testInfo = testInfo;
    }

    private VisualGridRunner initVisualGridRunner() {
        VisualGridRunner visualGridRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(10));
        visualGridRunner.setDontCloseBatches(true);
        return visualGridRunner;
    }

    private BatchInfo initBatchInfo() {
        BatchInfo batch = new BatchInfo(userName + "-" + appName);
        batch.setNotifyOnCompletion(false);
        batch.setSequenceName(ExecutionCloudTest.class.getSimpleName());
        batch.addProperty("REPOSITORY_NAME", new File(System.getProperty("user.dir")).getName());
        batch.addProperty("APP_NAME", appName);
        return batch;
    }

    private void closeBatch(BatchInfo batch) {
        if (null != batch) {
            batch.setCompleted(true);
        }
    }

    private Eyes initialiseEyes(VisualGridRunner visualGridRunner, BatchInfo batch) {
        Eyes eyes = new Eyes(visualGridRunner);
        Configuration config = new Configuration();
        config.setHostOS(System.getProperty("os.name"));
        config.setAppName(appName);
        config.setBaselineEnvName(testName + "-baseline");

        config.setApiKey(APPLITOOLS_API_KEY);
        config.setBatch(batch);
        config.setIsDisabled(false);
        config.setForceFullPageScreenshot(true);
        config.setStitchMode(StitchMode.CSS);
        config.setSaveNewTests(false);
        config.setMatchLevel(MatchLevel.STRICT);
        config.addProperty("username", userName);
        config.setAccessibilityValidation(new AccessibilitySettings(AccessibilityLevel.AA, AccessibilityGuidelinesVersion.WCAG_2_1));

        // Add browsers with different viewports
        config.addBrowser(1280, 1024, BrowserType.CHROME);
        config.addBrowser(1920, 1200, BrowserType.CHROME);
        config.addBrowser(1440, 1024, BrowserType.FIREFOX);
        config.addBrowser(1320, 1200, BrowserType.CHROME);
        config.addBrowser(1024, 1024, BrowserType.FIREFOX);

        // Add mobile emulation devices in Portrait/Landscape mode
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);

        eyes.setConfiguration(config);
        eyes.setLogHandler(new StdoutLogHandler(true));

        return eyes;

    }

    @AfterEach
    public void afterMethod(TestInfo testInfo) {
        System.out.println("AfterMethod: Test: " + testInfo.getDisplayName());
        if (null != driver) {
            driver.quit();
        }
    }

    private void getResults(VisualGridRunner visualGridRunner, Eyes eyes) {
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
    void testAgainstFigma() {
        String baselineEnvName = "swift-desktop";
        uploadImageAndSetAsBaseline();
        compareWithUploadedImage();
    }

    private void compareWithUploadedImage() {
        driver.get("https://applitools.com/wp-content/uploads/2022/08/Applitools-Logo-Set-V2_Applitools_1c_Green_Applitools_fullcolor_tagline-1.svg");
        VisualGridRunner visualGridRunner = initVisualGridRunner();
        BatchInfo batchInfo = initBatchInfo();
        Eyes eyesSelenium = initialiseEyes(visualGridRunner, batchInfo);
        eyesSelenium.open(driver, appName, testName, viewportSize);
        eyesSelenium.checkWindow("home");
        getResults(visualGridRunner, eyesSelenium);
        closeBatch(batchInfo);
        closeVisualGridRunner(visualGridRunner);
    }

    private static void closeVisualGridRunner(VisualGridRunner visualGridRunner) {
        visualGridRunner.close();
    }

    private void uploadImageAndSetAsBaseline() {
        EyesRunner runner = new ImageRunner();
        com.applitools.eyes.images.Eyes eyesImages = new com.applitools.eyes.images.Eyes(runner);
        eyesImages.setBaselineEnvName(testName + "-baseline");
        com.applitools.eyes.config.Configuration config = eyesImages.getConfiguration();
        config.setHostOS(System.getProperty("os.name"));
        config.setHostApp(appName);
        config.setBaselineEnvName(testName + "-baseline");
        config.setSaveNewTests(true);
        eyesImages.setConfiguration(config);

        try {
            eyesImages.open(appName, testName, viewportSize);
            BufferedImage img = ImageIO.read(new File("src/test/resources/applitools.png"));
            eyesImages.check("img", Target.image(img));
            TestResults testResults = eyesImages.close(false);
            System.out.println("TestResults: " + testResults);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            eyesImages.abortIfNotClosed();
        }
    }

}
