package HelloWorld;

import Utilities.*;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.fluent.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

public class Selenium_HelloWorld_EyesTest {

    private static final String appName = Selenium_HelloWorld_EyesTest.class.getSimpleName();
    private static BatchInfo batch;
    double counter = 3;
    private WebDriver driver;
    private Eyes eyes;

    @BeforeAll
    public static void beforeAll() {
        batch = new BatchInfo(appName);
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        driver = DriverUtils.createChromeDriver();

        eyes = new Eyes();
        eyes.setBatch(batch);
//        eyes.setLogHandler(new StdoutLogHandler(false));
        eyes.setForceFullPageScreenshot(false);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT);
//        eyes.setIsDisabled(true);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.open(driver, appName, testInfo.getDisplayName(), new RectangleSize(800, 800));
    }

    @Test
    public void seleniumEyesTest() {
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText).click();
            eyes.checkWindow("click-" + stepNumber);
            eyes.check("click", Target.region(linkText).matchLevel(MatchLevel.CONTENT));
        }
        driver.findElement(By.tagName("button")).click();
        eyes.checkWindow("After click");
    }

    @AfterEach
    public void afterMethod() {
        driver.close();
        ResultUtils.checkSeleniumResults(eyes);
    }
}
