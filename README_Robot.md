# Applitools Example: Robot Framework

This is the example project for the [Robot Framework tutorial](https://applitools.com/tutorials/quickstart/web/robot-framework).
It shows how to start automating visual tests
with [Applitools Eyes](https://applitools.com/platform/eyes/)
and [Robot Framework](https://robotframework.org/).

It uses:

* [Robot Framework](https://robotframework.org/) as the core test framework
* [Python](https://www.python.org/) as the programming platform underneath Robot Framework
* [Selenium WebDriver](https://www.selenium.dev/) for browser automation underneath Robot Framework
* [Google Chrome](https://www.google.com/chrome/downloads/) as the local browser for testing
* [pip](https://packaging.python.org/en/latest/tutorials/installing-packages/) for dependency management
* [Applitools Eyes](https://applitools.com/platform/eyes/) for visual testing

It can also run tests with:

* [Applitools Ultrafast Grid](https://applitools.com/platform/ultrafast-grid/) for cross-browser execution

To run this example project, you'll need:

1. An [Applitools account](https://auth.applitools.com/users/register), which you can register for free
2. [Python 3](https://www.python.org/) version 3.6 or higher
3. A good editor with Robot Framework support like [Visual Studio Code](https://marketplace.visualstudio.com/items?itemName=robocorp.robotframework-lsp)
4. An up-to-date version of [Google Chrome](https://www.google.com/chrome/downloads/)
5. A corresponding version of [ChromeDriver](https://chromedriver.chromium.org/downloads)

To install dependencies, run:

```
python3 -m pip install -r src/test/robot/requirements.txt
```

The main test case spec is [`acme_bank.robot`](acme_bank.robot).
By default, the project will run tests with Ultrafast Grid.
You can control how Applitools runs by changing the `EyesLibrary` `runner` setting.

To execute tests, set the `APPLITOOLS_API_KEY` environment variable
to your [account's API key](https://applitools.com/tutorials/guides/getting-started/registering-an-account),
and then run:

### Run tests in Execution Cloud 
```
APPLITOOLS_LOG_DIR=reports/robot/applitools-logs robot --outputdir reports/robot src/test/robot/acme_bank.robot
```

### Run tests in Execution Cloud with Self Healing
```
APPLITOOLS_LOG_DIR=reports/robot-self-healing/applitools-logs robot --outputdir reports/robot-self-healing src/test/robot/acme_bank_self_healing.robot
```

**For full instructions on running this project, take our
[Robot Framework tutorial](https://applitools.com/tutorials/quickstart/web/robot-framework)!**
