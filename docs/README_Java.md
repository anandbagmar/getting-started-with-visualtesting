Back to main [README](../README.md)

# Running the Java tests

This repo contains Selenium-java tests with Applitools integrated for Visual Testing.
See this repo for Appium-java tests with Applitools - https://github.com/anandbagmar/getting-started-with-mobile-visualtesting

* Set APPLITOOLS_API_KEY as an environment variable, OR, replace the line:
> eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

with 
> eyes.setApiKey("<replace_me>");

## Samples included:

> HelloWorldTest -> basic selenium test
 
> EyesTest -> selenium test with Applitools integrated
 
> UFGTest -> selenium test with Applitools UltraFast Grid integrgated

## Running the tests

You can run the test directly from any IDE, OR, you can run the test from the command line using the command:

> ./gradlew clean test --tests <test_name>

ex: 
> ./gradlew clean test --tests HelloWorldTest

> ./gradlew clean test --tests EyesTest

> ./gradlew clean test --tests UFGTest

> INJECT=true ./gradlew clean test --tests ExecutionCloudTest

Running with different browsers:
> BROWSER=chrome ./gradlew clean test --tests <test_name>

> BROWSER=firefox ./gradlew clean test --tests <test_name>

> BROWSER=safari ./gradlew clean test --tests <test_name>

# Running the Cucumber Tests

> ./gradlew cukes



Back to main [README](../README.md)
