Create account scenarii

Meta:
@refs 5
@progress done

Narrative:
Given I provide a create account request
When I send that request
It should either succeed or fail

Scenario: create account should succeed
When I send a valid "create account" request
Then the response code should be "201"
And I should be able to read the new resource

Scenario: create account with wrong uid should fail
Given I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create account" request with wrong uid "<wrong_uid>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|wrong_uid|message|responseContentType|responseLanguage|
||Email is required|application/xml|en|
|mail.mail|Valid email format is required|application/xml|en|
|mailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmail@mail.mail|Email max length is 100|application/xml|en|
|exists@mail.com|Email "exists@mail.com" already used|application/xml|en|

Scenario: create account with wrong password should fail
Given I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create account" request with wrong password "<wrong_password>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|wrong_password|message|responseContentType|responseLanguage|
||Password is required|application/xml|en|
|aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaax|Password max length is 200|application/xml|en|

Scenario: create account with wrong first name
Given I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create account" request with wrong first name "<wrong_first_name>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|wrong_first_name|message|responseContentType|responseLanguage|
||First name is required|application/xml|en|
|ccccccccccccccccccccccccccccccccccccccccccccccccccx|First name max length is 50|application/xml|en|

Scenario: create account with wrong last name
Given I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create account" request with wrong last name "<wrong_last_name>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|wrong_last_name|message|responseContentType|responseLanguage|
||Last name is required|application/xml|en|
|ccccccccccccccccccccccccccccccccccccccccccccccccccx|Last name max length is 50|application/xml|en|
