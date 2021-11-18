# getting-started-with-visualtesting

* Set APPLITOOLS_API_KEY as an environment variable, OR, replace the line:
>> eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

with 
>> eyes.setApiKey("<replace_me>");

* If running Appium tests, you need to connect your device / start emulator
* Start appium on the local machine

## Samples included:

>> Selenium_HelloWorld_Base -> basic selenium test
> 
>> Selenium_HelloWorld -> selenium test with Applitools integrated
> 
>> Appium_Native_HelloWorld_Base -> basic appium test, *without* Applitools
> 
>> Appium_Native_HelloWorld -> appium test, *with* Applitools integrated
