package HelloWorld;

import Utilities.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Selenium_HelloWorld_Base extends BaseTest {

    private void verifyHelloWorld(int numOfSteps) {
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

    public static void main(String[] args) {
        new Selenium_HelloWorld_Base().verifyHelloWorld(2);
    }
}
