Delete account stories

Meta:
@refs 13
@progress done

Narrative:
Given I provide a delete account request
When I send that request
It should either succeed or fail

Scenario: delete account should succeed
Given I provide "<uid>" uid and "<password>" password
When I send a valid "delete account" request
Then the response code should be "200"

Examples:
|uid|password|
|admin@admin.com|secret|

Scenario: delete account with wrong uid should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "delete account" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_id|message|
|admin@admin.com|secret|application/xml|en|-1|Account [id = -1] was not found|
