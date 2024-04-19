*** Settings ***
# Use Selenium WebDriver and Applitools Eyes.
Library     OperatingSystem
Library     SeleniumLibrary
Library     EyesLibrary       runner=web_ufg    config=applitools.yaml

# Declare setup and teardown routines.
Test Setup        Setup
Test Teardown     Teardown


*** Keywords ***
# For setup, load the demo site's login page and open Eyes to start visual testing.
Setup
    ${remote_url}=  Get Execution Cloud URL
    Log           Remote url is ${remote_url}
    Open Browser  https://demo.applitools.com  chrome  remote_url=${remote_url}
    Eyes Open

# For teardown, close Eyes and the browser.
Teardown
    Eyes Close Async
    Close All Browsers

*** Test Cases ***
# This test covers login for the Applitools demo site, which is a dummy banking app.
# The interactions use typical Selenium WebDriver calls,
# but the verifications use one-line snapshot calls with Applitools Eyes.
# If the page ever changes, then Applitools will detect the changes and highlight them in the Eyes Test Manager.
# Traditional assertions that scrape the page for text values are not needed here.
Log into bank account

    # Verify the full login page loaded correctly.
    Eyes Check Window    Login Page     Fully

    # Get the current id for username input box
    ${user_id_element}      Execute Javascript    return window.document.querySelector("#username").getAttribute("id")
    Log                     ${user_id_element}      INFO

    # Update id for username input box to 'user'
    ${user_id_element1}      Execute Javascript    window.document.querySelector("#username").id='user'
    Log                     user_id_element1 ${user_id_element1}    INFO

    # See if username input box id has been updated to 'user'
    ${updated_element}      Execute Javascript    return window.document.querySelector("#user").getAttribute("id")
    Log                     updated_user_id_element: ${updated_element}     INFO

    # Perform login.

    # Works with original id
    # Input Text              id:username    applibot

    # Self-healing does not work with updated id
    Input Text              id:username    applibot
    Input Text              id:password    I<3VisualTests
    Eyes Check Window       Before Login    Fully    Match Level  STRICT
    Click Element           id:log-in

    # Verify the full main page loaded correctly.
    # This snapshot uses LAYOUT match level to avoid differences in closing time text.
    Eyes Check Window    Main Page    Fully    Match Level  LAYOUT
