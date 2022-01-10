# getting-started-with-visualtesting

* Set APPLITOOLS_API_KEY as an environment variable, OR, replace the line:
>> eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

with 
>> eyes.setApiKey("<replace_me>");

* If running Appium tests, you need to connect your device / start emulator
* Start appium on the local machine

## Samples included:

> Selenium_HelloWorld_BaseTest -> basic selenium test
 
> Selenium_HelloWorldTest -> selenium test with Applitools integrated
 
> Selenium_HelloWorld_UFGTest -> selenium test with Applitools UltraFast Grid integrated
 
> Appium_Native_HelloWorld_BaseTest -> basic appium test, *without* Applitools
 
> Appium_Native_HelloWorldTest -> appium test, *with* Applitools integrated

## Running the tests

You can run the test directly from any IDE, OR, you can run the test from the command line using the command:

> mvn clean test -Dtest=<test_name>