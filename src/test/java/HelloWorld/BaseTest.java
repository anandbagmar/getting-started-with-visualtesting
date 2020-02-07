package HelloWorld;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
    protected static BatchInfo batch;
    protected Eyes eyes;
    protected WebDriver driver;

    protected static void checkResults(Eyes eyes) {
        Boolean throwtTestCompleteException = false;
        TestResults result = eyes.close(throwtTestCompleteException);
        System.out.println("Visual Testing results - " + result);
        String url = result.getUrl();
        if (result.isNew()) {
            System.out.println("New Baseline Created: URL=" + url);
        } else if (result.isPassed()) {
            System.out.println("All steps passed:     URL=" + url);
        } else {
            System.out.println("Test Failed:          URL=" + url);
        }
    }

    protected void sleep(int duractionInSec) {
        try {
            System.out.println(String.format("Sleep for %d sec", duractionInSec));
            Thread.sleep(duractionInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected WebElement waitForElementToBePresent(By elementId, AppiumDriver driver) {
        return (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(elementId));
    }

    protected WebElement waitForElementToBePresent(By elementId) {
        return (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(elementId));
    }
}
