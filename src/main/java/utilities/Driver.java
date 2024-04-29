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
    static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    public static boolean USE_TUNNEL = false;

    private Driver() {
    }

    public static WebDriver create() {
        String browser = (null == System.getenv("BROWSER")) ? "chrome" : System.getenv("BROWSER");
        return createDriverFor(browser);
    }

    public static WebDriver createDriverFor(String browser) {
        System.out.println("Running test with browser - " + browser);
        WebDriver driver = null;
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = Driver.createChromeDriver();
                break;
            case "firefox":
                driver = Driver.createFirefoxDriver();
                break;
            case "edge":
                driver = Driver.createEdgeDriver();
                break;
            case "safari":
                driver = Driver.createSafariDriver();
                break;
            case "self_healing":
                driver = createExecutionCloudRemoteDriver();
                break;
            default:
                throw new RuntimeException(browser + " is not yet supported");
        }
        // Add the created driver instance to the ThreadLocal variable
        driverThreadLocal.set(driver);
        return getDriver();
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
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
        if(USE_TUNNEL) {
            System.out.println("Using Tunnel");
            capabilities.setCapability("applitools:tunnel", true);
        }

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
