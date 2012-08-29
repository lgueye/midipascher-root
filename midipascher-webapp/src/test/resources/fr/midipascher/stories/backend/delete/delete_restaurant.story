Delete restaurant stories

Meta:
@refs 12
@progress done

Narrative:
Given I provide a delete restaurant request
When I send that request
It should either succeed or fail

Scenario: delete restaurant should succeed
Given I provide "<uid>" uid and "<password>" password
When I send a valid "delete restaurant" request
Then the response code should be "200"

Examples:
|uid|password|
|louis@rmgr.com|secret|

Scenario: delete restaurant as restaurant with wrong account should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "delete restaurant" request with wrong account
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|message|
|louis@rmgr.com|secret|application/xml|en|Account [id = -1] was not found|

Scenario: delete restaurant with wrong id should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "delete restaurant" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_id|message|
|louis@rmgr.com|secret|application/xml|en|-1|Restaurant [id = -1] was not found|
