Search restaurant stories

Meta:
@refs 15
@progress wip

Narrative:
Given I provide a search restaurant request
When I send that request
It should either succeed or fail

Scenario: search restaurant by name should succeed
Given I search for restaurants which name matches "awesome"
Then I should get the following restaurants:
|name|description|mainOffer|address.streetAddress|address.city|address.postalCode|address.countryCode|specialties|companyId|
|awesome name 20|venez découvrir|Salade quinoa 6,20€|12 rue Lafayette|Paris|75009|FR|SDW|552 100 520|
|awesome name 21|awesome description 21|Salade quinoa 6,20€|12 rue Lafayette|Paris|75009|FR|SLD|552 100 521|


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
