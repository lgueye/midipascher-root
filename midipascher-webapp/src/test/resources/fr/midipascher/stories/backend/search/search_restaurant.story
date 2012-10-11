Search restaurant stories

Meta:
@refs 15
@progress wip

Narrative:
Given I provide a search restaurant request
When I send that request
It should either succeed or fail

Scenario: search restaurant by name should succeed
When I search for restaurants which name matches "awesome"
Then I should get the following restaurants:
| name                        | description                | mainOffer                 | address.streetAddress             | address.city  | address.postalCode | address.countryCode | specialties | companyId   |
| awesome name 20             | fflflflflflflflflflflfl    | s5dfsd86gdfg786gd6gdg6    | dfg4dfg87dfg7dfg5df87g            | vkvjbkfgj     | 75001              | FR                  | SDW,SLD     | 552 100 520 |
| awesome name 21             | awesome description 21     | vgmfmdkksksks             | dfgjdgdfjgidfjgdfogidfog          | fkfkfkkfkkfkf | 75002              | FR                  | BLG,SDW     | 552 100 521 |

Scenario: search restaurant by description should succeed
When I search for restaurants which description matches "awesome"
Then I should get the following restaurants:
| name                        | description                | mainOffer                 | address.streetAddress             | address.city  | address.postalCode | address.countryCode | specialties | companyId   |
| awesome name 21             | awesome description 21     | vgmfmdkksksks             | dfgjdgdfjgidfjgdfogidfog          | fkfkfkkfkkfkf | 75002              | FR                  | BLG,SDW     | 552 100 521 |
| dfglkdfjgdfgjdfiogjdiogjfio | awesome description 22     | awesome main offer 22     | dfgdfgdgdgdfgdfg                  | gdfgdfgdgdkf  | 75003              | FR                  | BLG,CHN     | 552 100 522 |


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
