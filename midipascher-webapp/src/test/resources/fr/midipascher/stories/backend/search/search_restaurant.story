Search restaurant stories

Meta:
@refs 15
@progress wip

Narrative:
Given I provide a search restaurant request
When I send that request
It should either succeed or fail

Scenario: search restaurant by name should succeed
Given persisted restaurants:
|name|description|mainOffer|address.streetAddress|address.city|address.postalCode|address.countryCode|specialties|companyId|
|awesome name 20|fflflflflflflflflflflfl|s5dfsd86gdfg786gd6gdg6|dfg4dfg87dfg7dfg5df87g|vkvjbkfgj|75001|FR|SLD,SDW|552 100 520|
|awesome name 21|awesome description 21|vgmfmdkksksks|dfgjdgdfjgidfjgdfogidfog|fkfkfkkfkkfkf|75002|FR|SDW,BLG|552 100 521|
|dfglkdfjgdfgjdfiogjdiogjfio|awesome description 22|awesome main offer 22|dfgdfgdgdgdfgdfg|gdfgdfgdgdkf|75003|FR|BLG,CHN|552 100 522|
|fflfkkgkgkhkhklhjl|jkdfhkgdkfghdfghdfgudfuhgk|awesome main offer 23|12 rue awesome|gdfgdfgdgdkf|75004|FR|CHN,JPN|552 100 523|
|dkgfhlfhi|dfkghdkfghdgkudfl|sfdguhdfgudhudiguidfg|24 rue awesome|puteaux|92800|FR|JPN,SLD|552 100 524|
|mzaokszdfjkgjdkf|wpdfjfgidgidfh|sdfghighdfigudhughgkf|25 rue awesome|puteaux|92800|FR|SLD,|552 100 525|
When I search for restaurants which name matches "awesome"
Then I should get the following restaurants:
|name|description|mainOffer|address.streetAddress|address.city|address.postalCode|address.countryCode|specialties|companyId|
|awesome name 20|fflflflflflflflflflflfl|s5dfsd86gdfg786gd6gdg6|dfg4dfg87dfg7dfg5df87g|vkvjbkfgj|75001|FR|SLD,SDW|552 100 520|
|awesome name 21|awesome description 21|vgmfmdkksksks|dfgjdgdfjgidfjgdfogidfog|fkfkfkkfkkfkf|75002|FR|SDW,BLG|552 100 521|


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
