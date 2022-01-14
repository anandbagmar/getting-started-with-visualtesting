package HelloWorld;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import io.appium.java_client.*;
import io.appium.java_client.remote.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.net.*;

class Appium_Native_Calc_EyesTest {

    private static BatchInfo batch;
    private static final String className = Appium_Native_Calc_EyesTest.class.getSimpleName();
    private AppiumDriver<WebElement> driver;
    private Eyes eyes;
    private final String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub";

    @BeforeAll
    public static void beforeAll() {
        batch = new BatchInfo(className);
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws MalformedURLException {
        System.out.println("Test - " + testInfo.getDisplayName());
        System.out.println(String.format("Create AppiumDriver for - %s", APPIUM_SERVER_URL));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability("appPackage", "com.google.android.calculator");
        capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
        driver = new AppiumDriver<>(new URL(APPIUM_SERVER_URL), capabilities);
        System.out.println(String.format("Created AppiumDriver for - %s", APPIUM_SERVER_URL));

        eyes = new Eyes();
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setBatch(batch);
//        eyes.setIsDisabled(true);
        eyes.open(driver, className, testInfo.getDisplayName());
    }


    @Test
    public void appiumTest() {
        eyes.checkWindow("Hello!");
        driver.findElement(By.id("digit_" + 2)).click();
        eyes.checkWindow("digit_" + 2);
        driver.findElement(By.id("op_add")).click();
        eyes.checkWindow("op_add");
        driver.findElement(By.id("digit_" + 3)).click();
        eyes.checkWindow("digit_" + 3);
        driver.findElement(By.id("eq")).click();
        eyes.checkWindow("Calc works!");
    }

    @AfterEach
    void tearDown() {
        // Close the browser.
        BaseTest.checkResults(eyes);
        driver.quit();
    }
}