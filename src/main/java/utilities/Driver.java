package utilities;

import com.applitools.eyes.selenium.Eyes;
import exceptions.TestExecutionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class Driver {
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");

    private Driver() {
    }

    public static WebDriver create() {
        String browser = (null == System.getenv("BROWSER")) ? "chrome" : System.getenv("BROWSER");
        return createDriverFor(browser);
    }

    public static WebDriver createDriverFor(String browser) {
        System.out.println("Running test with browser - " + browser);
        switch (browser.toLowerCase()) {
            case "chrome":
                return Driver.createChromeDriver();
            case "firefox":
                return Driver.createFirefoxDriver();
            case "edge":
                return Driver.createEdgeDriver();
            case "safari":
                return Driver.createSafariDriver();
            case "self_healing":
                return createExecutionCloudRemoteDriver();
            default:
                throw new RuntimeException(browser + " is not yet supported");
        }
    }

    private static WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        return new EdgeDriver(options);
    }

    private static WebDriver createExecutionCloudRemoteDriver() {
        WebDriver innerDriver;
        ChromeOptions chromeOptions = new ChromeOptions();
        DesiredCapabilities capabilities = new DesiredCapabilities(chromeOptions);
        capabilities.setCapability("applitools:apiKey", APPLITOOLS_API_KEY);

        String executionCloudURL = Eyes.getExecutionCloudURL();
        try {
            innerDriver = new RemoteWebDriver(new URI(executionCloudURL).toURL(), capabilities);
        } catch (MalformedURLException | URISyntaxException e) {
            throw new TestExecutionException("Error creating a new RemoteWebDriver for url: " + executionCloudURL, e);
        }
        return innerDriver;
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        return new FirefoxDriver(options);
    }

    private static WebDriver createSafariDriver() {
        SafariOptions options = new SafariOptions();
        return new SafariDriver(options);
    }
}
