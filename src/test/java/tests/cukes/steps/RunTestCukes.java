package tests.cukes.steps;

import com.applitools.eyes.TestResultsStatus;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import io.cucumber.java.*;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import utilities.Driver;
import utilities.EyesConfguration;
import utilities.SoftAssertionsLib;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;

public class RunTestCukes extends AbstractTestNGCucumberTests {

    public RunTestCukes() {
        System.out.println("RunTestCukes constructor");
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        System.out.println(String.format("RunTestCukes: ThreadId: %d: in overridden scenarios%n",
                Thread.currentThread().getId()));
        Object[][] scenarios = super.scenarios();
        System.out.println(scenarios);
        return scenarios;
    }

    @BeforeAll
    public static void beforeAll() {
        System.out.println("beforeAll");
        EyesConfguration.createRunnerAndBatch();
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("afterAll");
    }

    @Before
    public void beforeTestScenario(Scenario scenario) {
        System.out.printf("beforeTestScenario: Thread id: '%d', Test name: '%s'%n", Thread.currentThread().getId(), scenario.getName());
        if (!scenario.getSourceTagNames().contains("@noUI")) {
            WebDriver driver = Driver.createDriverFor("self_healing");
            EyesConfguration.createEyes(driver, scenario);
        }
        SoftAssertionsLib.createSoftAssertions();
    }

    @After
    public void afterTestScenario(Scenario scenario) {
        System.out.println("AfterMethod: Test: " + scenario.getName());
        Collection<String> scenarioTagNames = scenario.getSourceTagNames();

        Status scenarioStatus = scenario.getStatus();
        System.out.println("Scenario status: " + scenarioStatus);

        SoftAssertions softly = SoftAssertionsLib.getSoftAssertions();
        List<AssertionError> softAssertionErrors = softly.assertionErrorsCollected();
        System.out.println("Number of soft assertion errors: " + softAssertionErrors.size());

        boolean isVisualTestingSuccessful = getVisualValidationStatus(scenarioTagNames);

        closeDriver();

        boolean hasScenarioActuallyPassed = (!scenario.isFailed() && softAssertionErrors.isEmpty() && isVisualTestingSuccessful);

        if (scenarioTagNames.contains("@failingTest")) {
            // failing test - mark test as passed IF
            // soft assertions.size() = 0
            // no visual diff
            // no other issues i.e. scenario.getStatus() == Status.PASSED

            // Scenario has failed. Mark it as passed.
            if (!hasScenarioActuallyPassed) {
            }

        } else {
            // regular test
            // can fail for:
            // soft assertions.size() > 0
            // visual diff found
            // other issues found i.e. scenario.getStatus() != Status.PASSED
            softly.assertAll();
            Assertions.assertThat(hasScenarioActuallyPassed).as("Test failed").isTrue();
        }
    }

    private static void closeDriver() {
        WebDriver driver = Driver.getDriver();
        if (null != driver) {
            driver.quit();
        }
    }

    private static boolean getVisualValidationStatus(Collection<String> scenarioTagNames) {
        AtomicBoolean isPass = new AtomicBoolean(true);
        if (!scenarioTagNames.contains("noUI")) {
            Eyes eyes = EyesConfguration.getEyes();
            VisualGridRunner visualGridRunner = EyesConfguration.getRunner();
            if (null != eyes) {
                eyes.closeAsync();
                TestResultsSummary allTestResults = visualGridRunner.getAllTestResults(false);
                allTestResults.forEach(testResultContainer -> {
                    System.out.printf("Test: %s\n%s%n", testResultContainer.getTestResults().getName(), testResultContainer);
                    displayVisualValidationResults(testResultContainer.getTestResults());
                    TestResultsStatus testResultsStatus = testResultContainer.getTestResults().getStatus();
                    if (testResultsStatus.equals(TestResultsStatus.Failed) || testResultsStatus.equals(TestResultsStatus.Unresolved)) {
                        isPass.set(false);
                    }
                });
            }
        }
        return isPass.get();
    }
}
