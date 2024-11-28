@theapp
Feature: Scenarios for "The App"

  Scenario: Verify error message on invalid login
    Given I login with invalid credentials - "znsio1", "invalid password"
    Then I try to login again with invalid credentials - "znsio2", "another invalid password"

  Scenario: Verify more error messages on invalid login
    Given I login with invalid credentials - "znsio3", "invalid password"
    Then I try to login again with invalid credentials - "znsio4", "another invalid password"
