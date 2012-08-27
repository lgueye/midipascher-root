Create food specialties stories

Meta:
@refs 4
@progress wip

Narrative:
Given I provide a create food specialty request
When I send that request
It should either succeed or fail

Scenario: create food specialty should succeed
Given I authenticate with "<uid>" uid and "<password>" password
When I send a valid "create food specialty" request
Then the response code should be "201"
And I should be able to read the new resource
Examples:
|uid|password|
|admin@admin.com|secret|

Scenario: create food specialty as account with wrong role should fail
Given I authenticate with "<uid>" uid and "<password>" password
And I accept "<responseContentType>" format
And I accept "<responseLanguage>" language
When I send a valid "create food specialty" request
Then the response code should be "403"
And the message should be "<message>"
Examples:
|uid|password||message|responseContentType|responseLanguage|
|rmgr@rmgr.com|secret||Accès refusé|application/xml|fr

Scenario: create food specialty with wrong code should fail
Given I authenticate with "<uid>" uid and "<password>" password
And I accept "en" language
And I accept "application/json" format
When I send a "create food specialty" request with wrong code "<wrong_code>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|wrong_code|message|
|admin@admin.com|secret||Code is required|
|admin@admin.com|secret|dlfjyyprpmz|Code max length is 10|
|admin@admin.com|secret|SDW|Code "SDW" already used|

Scenario: create food specialty with wrong label should fail
Given I authenticate with "<uid>" uid and "<password>" password
And I accept "en" language
And I accept "application/json" format
When I send a "create food specialty" request with wrong label "<wrong_label>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|wrong_label|message|
|admin@admin.com|secret||Label is required|
|admin@admin.com|secret|aaaaaaaaaaBBBBBBBBBBuuuuuuuuuu55555555559999999999S|Label max length is 50|
