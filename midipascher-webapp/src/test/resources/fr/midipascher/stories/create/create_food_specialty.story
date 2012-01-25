Create food specialties stories

Meta:
@refs 4
@progress wip

Narrative:
Given I provide a create food specialty request
When I send that request
It should either succeed or fail

Scenario: create food specialty should succeed
Given I authenticate with "admin" uid and "secret" password
When I send a valid "create food specialty" request
Then the response code should be "201"
And I should be able to read the new resource

Scenario: create food specialty as user with wrong role should fail
Scenario: create food specialty with wrong request type should fail
Scenario: create food specialty with wrong code should fail
Scenario: create food specialty with wrong label should fail
