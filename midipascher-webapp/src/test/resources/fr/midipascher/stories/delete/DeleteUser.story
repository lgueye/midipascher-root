Delete account stories

Meta:
@refs 1

Narrative:
Given I provide a delete account request
When I send that request
It should either succeed or fail

Scenario: delete account should succeed
Scenario: delete account as account with wrong role should fail
Scenario: delete account with wrong uid should fail
