package HelloWorld;

import com.applitools.eyes.*;

public class BaseTest {

    protected static void checkResults(com.applitools.eyes.selenium.Eyes eyes) {
        Boolean throwTestCompleteException = false;
        TestResults result = eyes.close(throwTestCompleteException);
        printResults(result);
    }

    protected static void checkResults(com.applitools.eyes.appium.Eyes eyes) {
        Boolean throwTestCompleteException = false;
        TestResults result = eyes.close(throwTestCompleteException);
        printResults(result);
    }

    static void printResults(TestResults result) {
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

    protected void sleep(int durationInSec) {
        try {
            System.out.println(String.format("Sleep for %d sec", durationInSec));
            Thread.sleep(durationInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
