package HelloWorld;

import Utilities.*;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import io.appium.java_client.*;
import io.appium.java_client.remote.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.io.*;
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
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("fullReset", true);
        capabilities.setCapability("app", new File("./sampleApps/AndroidCalculator.apk").getAbsolutePath());
        capabilities.setCapability("appPackage", "com.android2.calculator3");
        capabilities.setCapability("appActivity", "com.android2.calculator3.Calculator");
        driver = new AppiumDriver<>(new URL(APPIUM_SERVER_URL), capabilities);
        System.out.println(String.format("Created AppiumDriver for - %s", APPIUM_SERVER_URL));

        handleUpgradePopup();

        eyes = new Eyes();
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setBatch(batch);
//        eyes.setIsDisabled(true);
        eyes.open(driver, className, testInfo.getDisplayName());
    }

    private void handleUpgradePopup() {
        DriverUtils.waitFor(1);
        MobileElement upgradeAppNotificationElement = (MobileElement) driver.findElementById("android:id/button1");
        if (null!= upgradeAppNotificationElement) {
            upgradeAppNotificationElement.click();
            DriverUtils.waitFor(1);
        }
        MobileElement gotItElement = (MobileElement) driver.findElementById("com.android2.calculator3:id/cling_dismiss");
        if (null!= gotItElement) {
            gotItElement.click();
            DriverUtils.waitFor(1);
        }
    }

    @Test
    public void appiumTest() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 2)).click();
        eyes.checkWindow("digit" + 2);
        driver.findElement(By.id("plus")).click();
        eyes.checkWindow("plus");
        driver.findElement(By.id("digit" + 3)).click();
        eyes.checkWindow("digit" + 3);
        driver.findElement(By.id("equal")).click();
        eyes.checkWindow("Calc works!");
    }

    @AfterEach
    void tearDown() {
        // Close the browser.
        ResultUtils.checkSeleniumResults(eyes);
        driver.quit();
    }
}