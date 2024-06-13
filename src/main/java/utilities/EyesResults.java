package utilities;

import com.applitools.eyes.SessionAccessibilityStatus;
import com.applitools.eyes.TestResults;

public class EyesResults {
    private EyesResults() {
    }

    public static void displayVisualValidationResults(TestResults result) {
        boolean hasMismatches = false;
        System.out.println(result);
        System.out.println("\tTest Name: " + result.getName() + " :: " + result);
        System.out.println("\tTest status: " + result.getStatus());
        SessionAccessibilityStatus accessibilityStatus = result.getAccessibilityStatus();
        String accessibilityResults = (accessibilityStatus==null) ? "Accessibility not set" : "Accessibility validation: " + accessibilityStatus.getStatus().getName() + " at level: " + accessibilityStatus.getLevel().getName() + " with guideline version: " + accessibilityStatus.getVersion().getName();
        System.out.printf("\t\tName = '%s', " +
                          "%n\t\tBrowser = %s, OS = %s, Viewport = %dx%d, " +
                          "%n\t\t%s, " +
                          "%n\t\tmatched = %d, mismatched = %d, missing = %d, aborted = %s%n",
                          result.getName(),
                          result.getHostApp(),
                          result.getHostOS(),
                          result.getHostDisplaySize().getWidth(),
                          result.getHostDisplaySize().getHeight(),
                          accessibilityResults,
                          result.getMatches(),
                          result.getMismatches(),
                          result.getMissing(),
                          (result.isAborted() ? "aborted" : "no"));
        System.out.println("Results available here: " + result.getUrl());
        hasMismatches = result.getMismatches() != 0 || result.isAborted();
        System.out.println("Visual validation failed? - " + hasMismatches);
    }
}
