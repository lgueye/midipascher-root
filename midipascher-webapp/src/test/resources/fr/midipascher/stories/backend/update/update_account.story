Update account stories

Meta:
@refs 8
@progress done

Narrative:
Given I provide a update account request
When I send that request
It should either succeed or fail

Scenario: update account should succeed
Given I provide "<uid>" uid and "<password>" password
When I send a valid "update account" request
Then the response code should be "200"

Examples:
|uid|password|
|update@me.com|secret|

Scenario: update account with wrong id should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update account" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_id|message|
|update@me.com|secret|application/json|en|-89|Account [id = -89] was not found|

Scenario: update account with wrong password should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update account" request with wrong password "<wrong_password>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_password|message|
|update@me.com|secret|application/json|en||Password is required|
|update@me.com|secret|application/json|en|aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaax|Password max length is 200|

Scenario: update account with wrong first name
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update account" request with wrong first name "<wrong_first_name>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_first_name|message|
|update@me.com|secret|application/json|en||First name is required|
|update@me.com|secret|application/json|en|ccccccccccccccccccccccccccccccccccccccccccccccccccx|First name max length is 50|

Scenario: update account with wrong last name
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update account" request with wrong last name "<wrong_last_name>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|wrong_last_name|message|
|update@me.com|secret|application/json|en||Last name is required|
|update@me.com|secret|application/json|en|ccccccccccccccccccccccccccccccccccccccccccccccccccx|Last name max length is 50|
