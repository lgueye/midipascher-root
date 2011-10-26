Authentication stories

Meta:
@refs 1

Narrative:
Given I provide credentials
When I send authentication-required requests
It should either succeed or fail

Scenario: create food specialty with wrong uid should fail
Scenario: create food specialty with wrong password should fail
Scenario: create food specialty with disabled uid should fail
Scenario: create food specialty with expired uid should fail
Scenario: search restaurant with no uid should succeed
