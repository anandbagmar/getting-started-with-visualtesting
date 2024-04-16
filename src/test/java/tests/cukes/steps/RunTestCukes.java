package tests.cukes.steps;

import com.applitools.eyes.TestResultsStatus;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import io.cucumber.java.*;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import utilities.Driver;
import utilities.EyesConfguration;

import java.util.concurrent.atomic.AtomicBoolean;

import static utilities.EyesResults.displayVisualValidationResults;

public class RunTestCukes extends AbstractTestNGCucumberTests {

    public RunTestCukes() {
        long threadId = Thread.currentThread().getId();
        System.out.println("RunTestCukes: Constructor: ThreadId: " + threadId);
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
        System.out.println(String.format("RunTestCukes: ThreadId: %d: in overridden beforeTestScenario%n",
                Thread.currentThread().getId()));
        WebDriver driver = Driver.createDriverFor("self_healing");
        EyesConfguration.createEyes(driver, scenario);
    }

    @After
    public void afterTestScenario(Scenario scenario) {
        System.out.println("AfterMethod: Test: " + scenario.getName());
        Eyes eyes = EyesConfguration.getEyes();
        AtomicBoolean isPass = new AtomicBoolean(true);
        if (null != eyes) {
            eyes.closeAsync();
            TestResultsSummary allTestResults = EyesConfguration.getRunner().getAllTestResults(false);
            allTestResults.forEach(testResultContainer -> {
                System.out.printf("Test: %s\n%s%n", testResultContainer.getTestResults().getName(), testResultContainer);
                displayVisualValidationResults(testResultContainer.getTestResults());
                TestResultsStatus testResultsStatus = testResultContainer.getTestResults().getStatus();
                if (testResultsStatus.equals(TestResultsStatus.Failed) || testResultsStatus.equals(TestResultsStatus.Unresolved)) {
                    isPass.set(false);
                }
            });
        }
        WebDriver driver = Driver.getDriver();
        if (null != driver) {
            driver.quit();
        }
        Assertions.assertTrue(isPass.get(), "Visual differences found.");
    }
}
