Create food specialties stories

Meta:
@refs 4
@progress done

Narrative:
Given I provide a create food specialty request
When I send that request
It should either succeed or fail

Scenario: create food specialty should succeed
Given I authenticate with "admin@admin.com" uid and "secret" password
When I send a valid "create food specialty" request
Then the response code should be "201"
And I should be able to read the new resource

Scenario: create food specialty as account with wrong role should fail
Given I authenticate with "rmgr@rmgr.com" uid and "secret" password
And I accept "fr" language
And I accept "application/json" format
When I send a valid "create food specialty" request
Then the response code should be "403"
And the message should be "<message>"
Examples:
|message|
|Accès refusé|

Scenario: create food specialty with wrong code should fail
Given I authenticate with "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create food specialty" request with wrong code "<wrong_code>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_code|message|
||Code is required|
|dlfjyyprpmz|Code max length is 10|
|SDW|Code "SDW" already used|

Scenario: create food specialty with wrong label should fail
Given I authenticate with "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create food specialty" request with wrong label "<wrong_label>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_label|message|
||Label is required|
|aaaaaaaaaaBBBBBBBBBBuuuuuuuuuu55555555559999999999S|Label max length is 50|