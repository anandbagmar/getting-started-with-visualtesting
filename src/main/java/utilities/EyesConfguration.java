package utilities;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.io.File;

import static utilities.Driver.APPLITOOLS_API_KEY;

public class EyesConfguration {
    private static ThreadLocal<com.applitools.eyes.selenium.Eyes> eyesThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<VisualGridRunner> visualGridRunnerThreadLocal = new ThreadLocal<>();
//    private static VisualGridRunner visualGridRunner;
    private static BatchInfo batch;
    private static final String appName = "Cucumber-JVM Tests";
    private static final String userName = System.getProperty("user.name");
    private static final boolean IS_EYES_ENABLED = Boolean.parseBoolean(System.getenv("IS_EYES_ENABLED")) || true;

    public static void createRunnerAndBatch() {
        if (null == batch) {
            batch = new BatchInfo(userName + "-" + appName);
            batch.setNotifyOnCompletion(false);
            batch.addProperty("REPOSITORY_NAME", new File(System.getProperty("user.dir")).getName());
            batch.addProperty("APP_NAME", appName);
        }
    }

    public static com.applitools.eyes.selenium.Eyes createEyes(WebDriver driver, Scenario scenario) {
        System.out.println("createEyes for: " + scenario.getName());
        if (null == visualGridRunnerThreadLocal.get()) {
            VisualGridRunner visualGridRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(10));
            visualGridRunner.setDontCloseBatches(true);
            visualGridRunnerThreadLocal.set(visualGridRunner);
        }
        com.applitools.eyes.selenium.Eyes eyes = new com.applitools.eyes.selenium.Eyes(visualGridRunnerThreadLocal.get());
        Configuration config = new Configuration();

        config.setApiKey(APPLITOOLS_API_KEY);
        config.setBatch(batch);
        config.setIsDisabled(!IS_EYES_ENABLED);
        config.setSaveNewTests(false);
        config.setMatchLevel(MatchLevel.STRICT);
        config.addProperty("username", userName);

        // Add browsers with different viewports
        config.addBrowser(1440, 1200, BrowserType.CHROME);
        config.addBrowser(1200, 1200, BrowserType.FIREFOX);

        // Add mobile emulation devices in Portrait/Landscape mode
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);

        eyes.setConfiguration(config);
        eyes.setLogHandler(new StdoutLogHandler(true));

        eyes.open(driver, appName, scenario.getName(), new RectangleSize(1200, 900));
        eyesThreadLocal.set(eyes);
        return getEyes();
    }

    public static com.applitools.eyes.selenium.Eyes getEyes() {
        return eyesThreadLocal.get();
    }

    public static VisualGridRunner getRunner() {
        return visualGridRunnerThreadLocal.get();
    }
}
