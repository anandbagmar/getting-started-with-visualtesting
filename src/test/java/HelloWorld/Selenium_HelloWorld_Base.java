package HelloWorld;

import Utilities.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

public class Selenium_HelloWorld_Base {

    @Test
    public void seleniumBaseTest() {
        int numOfSteps = 2;
        DriverUtils.getPathForChromeDriverFromMachine();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://applitools.com/helloworld");

            for (int stepNumber = 0; stepNumber < numOfSteps; stepNumber++) {
                driver.findElement(By.linkText("?diff1")).click();
                Thread.sleep(1000);
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
}
