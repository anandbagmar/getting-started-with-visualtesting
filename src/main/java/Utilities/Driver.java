package Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

public class Driver {
    public static WebDriver createChromeDriver() {
        return getDriverPath("chrome");
    }

    public static WebDriver createFirefoxDriver() {
        return getDriverPath("firefox");
    }

    private static WebDriver getDriverPath(String browserType) {
        WebDriverManager webDriverManager = WebDriverManager.getInstance(browserType);
        webDriverManager.setup();

        String downloadedDriverPath = webDriverManager.getDownloadedDriverPath();
        String browserVersion = webDriverManager.getDownloadedDriverVersion();
        System.out.printf("Using Driver version: '%s' from: '%s'%n",
                          browserVersion,
                          downloadedDriverPath);
        System.setProperty("webdriver." + browserType.toLowerCase() + ".driver",
                           downloadedDriverPath);
        return webDriverManager.create();
    }
}
