package HelloWorld;

import Utilities.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

public class Selenium_HelloWorld_Base extends BaseTest {

    private void verifyHelloWorld(int numOfSteps) {
        DriverUtils.getPathForChromeDriverFromMachine();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://applitools.com/helloworld");

            for (int stepNumber = 0; stepNumber < numOfSteps; stepNumber++) {
                driver.findElement(By.linkText("?diff1")).click();
            }

            // Click the "Click me!" button.
            driver.findElement(By.tagName("button")).click();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
            // Close the browser.
            driver.close();
        }
    }

    public static void main(String[] args) {
        new Selenium_HelloWorld_Base().verifyHelloWorld(2);
    }
}
