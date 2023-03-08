package SeleniumTests;

import Utilities.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HelloWorldTest {

    int numOfSteps = 2;
    private WebDriver driver;

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("Starting test: " + testInfo.getDisplayName());
        driver = Driver.create();
    }

    @Test
    public void seleniumBaseTest() throws InterruptedException {
        driver.get("https://applitools.com/helloworld");

        for (int stepNumber = 0; stepNumber < numOfSteps; stepNumber++) {
            driver.findElement(By.linkText("?diff1"))
                    .click();
            Thread.sleep(1000);
        }

        driver.findElement(By.tagName("button"))
                .click();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
