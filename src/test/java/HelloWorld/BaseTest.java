package HelloWorld;

import com.applitools.eyes.*;

public class BaseTest {

    protected static void checkResults(com.applitools.eyes.selenium.Eyes eyes) {
        Boolean throwtTestCompleteException = false;
        TestResults result = eyes.close(throwtTestCompleteException);
        printResults(result);
    }

    protected static void checkResults(com.applitools.eyes.appium.Eyes eyes) {
        Boolean throwtTestCompleteException = false;
        TestResults result = eyes.close(throwtTestCompleteException);
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

    protected void sleep(int duractionInSec) {
        try {
            System.out.println(String.format("Sleep for %d sec", duractionInSec));
            Thread.sleep(duractionInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
