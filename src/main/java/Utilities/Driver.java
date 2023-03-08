package Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class Driver {
    public static WebDriver create() {
        String browser = (null == System.getenv("BROWSER")) ? "chrome" : System.getenv("BROWSER");
        System.out.println("Running test with browser - " + browser);
        WebDriver driver = null;
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = Driver.createChromeDriver();
                break;
            case "firefox":
                driver = Driver.createFirefoxDriver();
                break;
            case "safari":
                driver = Driver.createSafariDriver();
                break;
            default:
                throw new RuntimeException(browser + " is not yet supported");
        }
        return driver;
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }

    private static WebDriver createSafariDriver() {
        SafariOptions options = new SafariOptions();
        WebDriverManager.safaridriver().setup();
        return new SafariDriver(options);
    }
}
