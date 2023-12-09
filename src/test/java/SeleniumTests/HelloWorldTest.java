package SeleniumTests;

import Utilities.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Utilities.Wait.waitFor;

public class HelloWorldTest {

    int numOfSteps = 2;
    private WebDriver driver;

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("Starting test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("chrome");
    }

    @Test
    public void seleniumBaseTest() throws InterruptedException {
        driver.get("https://applitools.com/helloworld");

        String actualDefaultNumber = driver.findElement(By.xpath("//span[@class='primary']")).getText();
        System.out.println("actualDefaultNumber: " + actualDefaultNumber);
        String expectedDefaultNumber = "123456";
        Assertions.assertEquals(expectedDefaultNumber, actualDefaultNumber, "Default number is incorrect");
        String randomNumber = expectedDefaultNumber;
        for (int stepNumber = 0; stepNumber < numOfSteps; stepNumber++) {
            driver.findElement(By.linkText("?diff1"))
                    .click();
            String actualRandomNumber = driver.findElement(By.cssSelector("span.diff1.diff2.random-number")).getText();
            System.out.println("actualRandomNumber: " + actualRandomNumber);
            Assertions.assertNotEquals(randomNumber, actualRandomNumber, "Random number is same as earlier number");
            waitFor(1);
            randomNumber = actualRandomNumber;
        }

        driver.findElement(By.tagName("button"))
                .click();
        String actualSuccessfulMessage = driver.findElement(By.cssSelector("div.section.image-section")).findElement(By.cssSelector("p")).getText();
        System.out.println("actualSuccessfulMessage: " + actualSuccessfulMessage);
        String expectedSuccessfulMessage = "You successfully clicked the button!";
        Assertions.assertEquals(expectedSuccessfulMessage, actualSuccessfulMessage, "Successful message is incorrect");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
