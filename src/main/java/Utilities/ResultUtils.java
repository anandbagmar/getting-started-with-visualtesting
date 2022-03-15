package Utilities;


public class ResultUtils {

    public static void checkSeleniumResults(com.applitools.eyes.selenium.Eyes eyes) {
        Boolean throwTestCompleteException = false;
        com.applitools.eyes.TestResults result = eyes.close(throwTestCompleteException);
        printResults(result);
    }

    public static void checkAppiumResults(com.applitools.eyes.appium.Eyes eyes) {
        Boolean throwTestCompleteException = false;
        com.applitools.eyes.TestResults result = eyes.close(throwTestCompleteException);
        printResults(result);
    }

    static void printResults(com.applitools.eyes.TestResults result) {
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
}
