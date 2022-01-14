package HelloWorld;

import Utilities.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

public class Selenium_HelloWorld_Test {

    int numOfSteps = 2;
    private WebDriver driver;

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("Starting test: " + testInfo.getDisplayName());
        driver = DriverUtils.createChromeDriver();
    }

    @Test
    public void seleniumBaseTest() throws InterruptedException {
        driver.get("https://applitools.com/helloworld");

        for (int stepNumber = 0; stepNumber < numOfSteps; stepNumber++) {
            driver.findElement(By.linkText("?diff1")).click();
            Thread.sleep(1000);
        }

        driver.findElement(By.tagName("button")).click();
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }
}
