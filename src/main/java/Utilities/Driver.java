package Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class Driver {
    public static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        return new ChromeDriver(options);
//        return getDriverPath("chrome", options);
    }

    public static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        return getDriverPath("firefox", options);
    }

    private static WebDriver getDriverPath(String browserType, AbstractDriverOptions options) {
        WebDriverManager webDriverManager = WebDriverManager.getInstance(browserType);
        webDriverManager.setup();

        String downloadedDriverPath = webDriverManager.getDownloadedDriverPath();
        String browserVersion = webDriverManager.getDownloadedDriverVersion();
        System.out.printf("Using Driver version: '%s' from: '%s'%n",
                          browserVersion,
                          downloadedDriverPath);
        webDriverManager.capabilities(options);
        return webDriverManager.create();
    }
}
