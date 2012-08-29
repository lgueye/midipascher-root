Update food specialties stories

Meta:
@refs 7
@progress wip

Narrative:
Given I provide a update food specialty request
When I send that request
It should either succeed or fail

Scenario: update food specialty should succeed
Given I provide "<uid>" uid and "<password>" password
When I send a valid "update food specialty" request
Then the response code should be "200"

Examples:
|uid|password|
|admin@admin.com|secret|

Scenario: update food specialty as account with wrong role should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a valid "update food specialty" request
Then the response code should be "403"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|message|
|rmgr@rmgr.com|secret|application/json|fr|Accès refusé|

Scenario: update food specialty with wrong id should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update food specialty" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_id|message|
|admin@admin.com|secret|application/json|en|-1|Food specialty [id = -1] was not found|

Scenario: update food specialty with wrong code should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update food specialty" request with wrong code "<wrong_code>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_code|message|
|admin@admin.com|secret|application/json|en||Code is required|
|admin@admin.com|secret|application/json|en|dlfjyyprpmz|Code max length is 10|
|admin@admin.com|secret|application/json|en|SDW|Code "SDW" already used|

Scenario: update food specialty with wrong label should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update food specialty" request with wrong label "<wrong_label>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_label|message|
|admin@admin.com|secret|application/json|en||Label is required|
|admin@admin.com|secret|application/json|en|aaaaaaaaaaBBBBBBBBBBuuuuuuuuuu55555555559999999999S|Label max length is 50|
