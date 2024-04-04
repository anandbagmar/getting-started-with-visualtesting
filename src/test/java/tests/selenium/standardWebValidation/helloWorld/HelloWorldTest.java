package tests.selenium.tests.standardWebValidation.helloWorld;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import utilities.Driver;

import static utilities.Wait.waitFor;

class HelloWorldTest {

    int numOfSteps = 2;
    private WebDriver driver;

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("chrome");
    }

    @Test
    void seleniumBaseTest() throws InterruptedException {
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
    public void afterMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver.quit();
    }
}
