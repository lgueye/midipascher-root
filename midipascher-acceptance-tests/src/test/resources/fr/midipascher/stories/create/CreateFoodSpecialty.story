Create food specialties stories

Meta:
@refs 1

Narrative:
Given I provide a create food specialty request
When I send that request
It should either succeed or fail

Scenario: create food specialty should succeed
Scenario: create food specialty as user with wrong role should fail
Scenario: create food specialty with wrong request type should fail
Scenario: create food specialty with wrong code should fail
Scenario: create food specialty with wrong label should fail
