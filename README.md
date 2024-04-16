This repo contains Selenium-java tests with Applitools integrated for Visual Testing.
See this repo for Appium-java tests with Applitools - https://github.com/anandbagmar/getting-started-with-mobile-visualtesting

# getting-started-with-visualtesting

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

# Running the PDF test

## Java

> ./gradlew clean test --tests PDFJavaTest

## JavaScript

> npm install

> npm run pdftest

# Running the Cucumber Tests

> ./gradlew cukes

# Machine setup instructions
Follow the below steps to get your machine setup ready. These steps are for Selenium-Java based Test Automation. If you are using any other combination, please contact anand.bagmar@essenceoftesting.com
- Install JDK 11 or higher
- Clone this git repo (https://github.com/anandbagmar/getting-started-with-visualtesting) on your laptop
- Open the cloned project in your IDE as a Maven project. This will automatically download all the dependencies
- Once all dependencies are downloaded, run the following command from command prompt / terminal window:
  --> ./gradlew clean test --tests HelloWorldTest

# Machine Connectivity Check

Run the following commands on your laptops to ensure connectivity to the Applitools server.
The response status code for each of these methods should be 2xx / 3xx.

## Instructions for Windows OS:

- Run the following commands in PowerShell window and note the response status code:
- curl -Method GET https://eyes.applitools.com
- curl -Method GET https://eyesapi.applitools.com

If you get an error in the console / terminal window with message such as FORBIDDEN / ACCESS DENIED / PROXY ERROR / etc., then try the same commands by providing the proxy details:

NOTE: Based on your network configuration, the -ProxyCredential parameter may need to be specified

- curl -Method GET -Proxy -ProxyCredential https://eyes.applitools.com
- curl -Method GET -Proxy -ProxyCredential https://eyesapi.applitools.com

## Instructions for Linux / OSX OS:
Run the following commands in PowerShell window and note the response status code:

- curl -I https://eyes.applitools.com
- curl -I https://eyesapi.applitools.com

If you get an error in the console / terminal window with message such as FORBIDDEN / ACCESS DENIED / PROXY ERROR / etc., then try the same commands by providing the proxy details:

NOTE: Based on your network configuration, the --U parameter may need to be specified
- curl -I -p -U https://eyes.applitools.com
- curl -I -p -U https://eyesapi.applitools.com

If you are still getting an error response, then you will need to get the following URLs whitelisted on your network:
- https://render-wus.applitools.com
- https://eyesapi.applitools.com
- https://eyes.applitools.com
- https://eyespublicwusi0.blob.core.windows.net 
