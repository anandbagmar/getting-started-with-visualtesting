This repo contains Selenium-java tests with Applitools integrated for Visual Testing.
See this repo for Appium-java tests with Applitools - https://github.com/anandbagmar/getting-started-with-mobile-visualtesting

# getting-started-with-visualtesting

* Set APPLITOOLS_API_KEY as an environment variable, OR, replace the line:
> eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

with 
> eyes.setApiKey("<replace_me>");

## Samples included:

> Selenium_HelloWorld_Test -> basic selenium test
 
> Selenium_HelloWorld_EyesTest -> selenium test with Applitools integrated
 
> Selenium_HelloWorld_UFGTest -> selenium test with Applitools UltraFast Grid integrated

## Running the tests

You can run the test directly from any IDE, OR, you can run the test from the command line using the command:

> mvn clean test -Dtest=<test_name>