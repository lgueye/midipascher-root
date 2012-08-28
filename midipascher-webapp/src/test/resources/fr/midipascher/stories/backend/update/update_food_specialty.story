Update food specialties stories

Meta:
@refs 7
@progress done

Narrative:
Given I provide a update food specialty request
When I send that request
It should either succeed or fail

Scenario: update food specialty should succeed
Given I provide "admin@admin.com" uid and "secret" password
When I send a valid "update food specialty" request
Then the response code should be "200"

Scenario: update food specialty as account with wrong role should fail
Given I provide "rmgr@rmgr.com" uid and "secret" password
And I accept "fr" language
And I accept "application/json" format
When I send a valid "update food specialty" request
Then the response code should be "403"
And the message should be "<message>"
Examples:
|message|
|Accès refusé|

Scenario: update food specialty with wrong id should fail
Given I provide "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/xml" format
When I send a "update food specialty" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"
Examples:
|wrong_id|message|
|-1|Food specialty [id = -1] was not found|

Scenario: update food specialty with wrong code should fail
Given I provide "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "update food specialty" request with wrong code "<wrong_code>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_code|message|
||Code is required|
|dlfjyyprpmz|Code max length is 10|
|SDW|Code "SDW" already used|

Scenario: update food specialty with wrong label should fail
Given I provide "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "update food specialty" request with wrong label "<wrong_label>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_label|message|
||Label is required|
|aaaaaaaaaaBBBBBBBBBBuuuuuuuuuu55555555559999999999S|Label max length is 50|
