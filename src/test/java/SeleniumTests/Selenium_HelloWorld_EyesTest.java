package SeleniumTests;

import Utilities.Driver;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Selenium_HelloWorld_EyesTest {

    private static final String appName = Selenium_HelloWorld_EyesTest.class.getSimpleName();
    private static BatchInfo batch;
    double counter = 3;
    private WebDriver driver;
    private Eyes eyes;
    private static final String userName = System.getProperty("user.name");

    @BeforeAll
    public static void beforeAll() {
        batch = new BatchInfo(userName + "-" + appName);
    }

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        driver = Driver.createChromeDriver();

        eyes = new Eyes();
        eyes.setBatch(batch);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setForceFullPageScreenshot(false);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT);
        eyes.setIsDisabled(false);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.addProperty("username",
                         userName);
        eyes.open(driver,
                  appName,
                  testInfo.getDisplayName(),
                  new RectangleSize(800,
                                    800));
    }

    @Test
    public void seleniumEyesTest() {
        driver.get("https://applitools.com/helloworld");
        eyes.checkWindow("home");

        for (int stepNumber = 0; stepNumber < counter; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText)
                  .click();
            eyes.checkWindow("click-" + stepNumber);
            eyes.check("click",
                       Target.region(linkText)
                             .matchLevel(MatchLevel.CONTENT));
        }
        driver.findElement(By.tagName("button"))
              .click();
        eyes.checkWindow("After click");
    }

    @AfterEach
    public void afterMethod() {
        driver.close();
        TestResults result = eyes.close(false);
        System.out.println(result);
    }
}
