Search restaurant stories

Meta:
@refs 1

Narrative:
Given I provide a search restaurant request
When I send that request
It should either succeed or fail

Scenario: search restaurant without credentials should succeed
Scenario: search restaurant with wrong request type should fail
Scenario: search restaurant by name should succeed
Scenario: search restaurant by description should succeed
Scenario: search restaurant by phone number should succeed
Scenario: search restaurant by account should succeed
Scenario: search restaurant by main offer should succeed
Scenario: search restaurant by specialties should succeed
Scenario: search restaurant by street adress should succeed
Scenario: search restaurant by city should succeed
Scenario: search restaurant by postal code should succeed
Scenario: search restaurant by country code should succeed
Scenario: search restaurant by halal property should succeed
Scenario: search restaurant by kosher property should succeed
Scenario: search restaurant by vegetarian property should succeed
Scenario: search restaurant without size should fallback to defaultSize
Scenario: search restaurant without from should fallback to defaultFrom
Scenario: search restaurant without sort should fallback to defaultSort
