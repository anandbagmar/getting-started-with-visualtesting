package HelloWorld;

import io.appium.java_client.*;
import io.appium.java_client.remote.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.net.*;

class Appium_Native_Calc_Test {

    private AppiumDriver<WebElement> driver;
    private final String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub";

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
    }

    @Test
    public void appiumBaseTest() {
        int p1 = 3;
        int p2 = 5;
        driver.findElement(By.id("digit_" + p1)).click();
        driver.findElement(By.id("op_add")).click();
        driver.findElement(By.id("digit_" + p2)).click();
        driver.findElement(By.id("eq")).click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
