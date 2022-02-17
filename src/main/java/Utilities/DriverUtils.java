package Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;

import java.io.IOException;
import java.util.Arrays;

public class DriverUtils {
    public static WebDriver createChromeDriver() {
        return getDriverPath("chrome");
    }

    public static WebDriver createChromeDriverForConnectedDevice() {
        int[] versionNamesArr = getChromeVersionsFor();
        if (versionNamesArr.length > 0) {
            int highestChromeVersion = Arrays.stream(versionNamesArr).max().getAsInt();
            String message = "ChromeDriver for Chrome version " + highestChromeVersion
                    + "on device: ";
            System.out.println(message);
            WebDriverManager webDriverManager = WebDriverManager.getInstance("chrome").browserVersion(String.valueOf(highestChromeVersion));
            webDriverManager.setup();
            String chromeDriverPath = webDriverManager.getDownloadedDriverPath();
            System.out.println("ChromeDriver path: " + chromeDriverPath);
            return webDriverManager.create();
        } else {
            return null;
        }
    }

    private static int[] getChromeVersionsFor() {
        CommandPrompt cmd = new CommandPrompt();
        String resultStdOut = null;
        try {
            resultStdOut = cmd.runCommandThruProcess("adb shell dumpsys package com.android.chrome | grep versionName");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting chrome version from device - " + e.getMessage());
        }
        int[] versionNamesArr = {};
        if (resultStdOut.contains("versionName=")) {
            String[] foundVersions = resultStdOut.split("\n");
            for (String foundVersion : foundVersions) {
                String version = foundVersion.split("=")[1].split("\\.")[0];
                String format = String.format("Found Chrome version - '%s'", version);
                System.out.println(format);
                versionNamesArr = ArrayUtils.add(versionNamesArr, Integer.parseInt(version));
            }
        } else {
            System.out.println(String.format("Chrome not found on device"));
        }
        return versionNamesArr;
    }

    public static WebDriver createFirefoxDriver() {
        return getDriverPath("firefox");
    }

    private static WebDriver getDriverPath(String browserType) {
        WebDriverManager webDriverManager = WebDriverManager.getInstance(browserType);
        webDriverManager.setup();

        String downloadedDriverPath = webDriverManager.getDownloadedDriverPath();
        String browserVersion = webDriverManager.getDownloadedDriverVersion();
        System.out.printf("Using Driver version: '%s' from: '%s'%n", browserVersion, downloadedDriverPath);
        System.setProperty("webdriver." + browserType.toLowerCase() + ".driver", downloadedDriverPath);
        return webDriverManager.create();
    }

    public static void waitFor(int durationInSec) {
        try {
            System.out.println(String.format("Sleep for %d sec", durationInSec));
            Thread.sleep(durationInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
