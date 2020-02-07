package Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.Arrays;

public class DriverUtils {
    public static String getPathForChromeDriverFromMachine() {
        WebDriverManager.chromedriver().setup();
        String chromeDriverPath = WebDriverManager.chromedriver().getBinaryPath();
        System.out.println("ChromeDriver path: " + chromeDriverPath);
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        return chromeDriverPath;
    }

    public static String getPathForChromeDriverFromConnectedDevice() {
        int[] versionNamesArr = getChromeVersionsFor();
        if (versionNamesArr.length > 0) {
            int highestChromeVersion = Arrays.stream(versionNamesArr).max().getAsInt();
            String message = "ChromeDriver for Chrome version " + highestChromeVersion
                    + "on device: ";
            System.out.println(message);
            WebDriverManager.chromedriver().version(String.valueOf(highestChromeVersion)).setup();
            String chromeDriverPath = WebDriverManager.chromedriver().getBinaryPath();
            System.out.println("ChromeDriver path: " + chromeDriverPath);
            return chromeDriverPath;
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

    public static String getPathForFirefoxDriverFromMachine() {
        WebDriverManager.firefoxdriver().setup();
        String firefoxDriverPath = WebDriverManager.firefoxdriver().getBinaryPath();
        System.out.println("FirefoxDriver path: " + firefoxDriverPath);
        System.setProperty("webdriver.firefox.driver", firefoxDriverPath);
        return firefoxDriverPath;
    }
}
