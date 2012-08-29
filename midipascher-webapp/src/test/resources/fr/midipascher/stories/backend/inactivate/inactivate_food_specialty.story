Inactivate food specialties stories

Meta:
@refs 10
@progress wip

Narrative:
Given I provide an inactivate food specialty request
When I send that request
It should either succeed or fail

Scenario: inactivate food specialty should succeed
Given I provide "<uid>" uid and "<password>" password
When I send a valid "inactivate food specialty" request
Then the response code should be "200"

Examples:
|uid|password|
|admin@admin.com|secret|

Scenario: inactivate food specialty as account with wrong role should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a valid "inactivate food specialty" request
Then the response code should be "403"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|message|
|rmgr@rmgr.com|secret|application/json|fr|Accès refusé|

Scenario: inactivate food specialty with wrong id should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "inactivate food specialty" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_id|message|
|admin@admin.com|secret|application/xml|en|-1|Food specialty [id = -1] was not found|
