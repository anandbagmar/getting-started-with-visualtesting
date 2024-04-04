package tests.selenium.gameValidation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import utilities.Driver;

import static utilities.Wait.waitFor;

class GameTest {

    int numOfSteps = 2;
    private WebDriver driver;

    @BeforeEach
    public void beforeMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver = Driver.createDriverFor("chrome");
    }

    @Test
    void gameTest() {
        driver.get("https://openfairy.playco.games/");
        waitFor(15);
        WebElement canvasElement = driver.findElement(By.id("pixi-canvas")); // Replace with the actual ID or other locator

        Dimension size = driver.manage().window().getSize();
        System.out.println("size: " + size);

        // Create an instance of the Actions class
        Actions actions = new Actions(driver);

        actions.moveToLocation(200, 950).click().perform();
        waitFor(10);
        actions.moveToLocation(250, 900).click().perform();
        waitFor(10);
        actions.moveToLocation(250, 950).click().perform();
        waitFor(10);
        actions.moveToLocation(600, 400).click().perform();
        waitFor(15);
        driver.findElement(By.xpath("//input")).sendKeys("abc");
        waitFor(2);
        actions.moveToLocation(600, 900).click().perform();
        waitFor(15);
        actions.moveToLocation(400, 800).click().perform();
        waitFor(10);
    }

    @AfterEach
    public void afterMethod(TestInfo testInfo) {
        System.out.println("BeforeMethod: Test: " + testInfo.getDisplayName());
        driver.quit();
    }
}
