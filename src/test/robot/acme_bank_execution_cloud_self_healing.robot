*** Settings ***
Library     OperatingSystem
Library     SeleniumLibrary
Library     EyesLibrary       runner=web_ufg    config=applitools_execution_cloud.yaml
Variables    applitools_execution_cloud.yaml
Test Setup        Setup
Test Teardown     Teardown

*** Keywords ***
Setup
    ${selfHealingOptions} =     Create Dictionary    findElementsSupport=${True}
    ${remote_url}=              Get Execution Cloud URL
    Log to console              appName=${web_ufg.app_name}
    Log to console              Remote url is ${remote_url}
    Log to console              testName=${TEST NAME}
    Open Browser                https://demo.applitools.com  chrome  remote_url=${remote_url}
    ...     options=set_capability('applitools:useSelfHealing', ${True});set_capability('browserVersion', 'latest');set_capability('selfHealingOptions', ${selfHealingOptions})
    &{ARGS}=                    Create Dictionary  appName=${web_ufg.app_name}    testName=${TEST NAME}
#    Execute JavaScript          applitools:startTest    ARGUMENTS    ${ARGS}
    Eyes Open


Teardown
    &{ARGS}=  IF  "${TEST STATUS}" == "PASS"  Create Dictionary  status=Passed  ELSE  Create Dictionary  status=Failed
#    Execute JavaScript  applitools:endTest      ARGUMENTS    ${ARGS}
    Eyes Close Async
    Close All Browsers

*** Test Cases ***
Log into bank account
    Eyes Check Window    Login Page     Fully

    # Simulate Self-Healing Event
#    Execute Javascript    document.getElementById('log-in').id = 'access';

    Input Text        id:username    applibot
    Input Text        id:password    I<3VisualTests
    Eyes Check              Target Region By Selector    id:username    With Name   username
    Eyes Check              Target Region By Selector    id:password    With Name   password
    Eyes Check Window    Credentials entered    Fully    Match Level  LAYOUT
    Click Element     id:log-in
    Eyes Check Window    Main Page    Fully    Match Level  LAYOUT
