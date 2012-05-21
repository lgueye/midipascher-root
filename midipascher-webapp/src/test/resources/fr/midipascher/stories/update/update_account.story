Update account stories

Meta:
@refs 8
@progress done

Narrative:
Given I provide a update account request
When I send that request
It should either succeed or fail

Scenario: update account should succeed
Given I provide  "update@me.com" uid and "secret" password
When I send a valid "update account" request
Then the response code should be "200"

Scenario: update account with wrong id should fail
Given I provide  "update@me.com" uid and "secret" password
Given I accept "en" language
And I accept "application/json" format
When I send a "update account" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"
Examples:
|wrong_id|message|
|-89|Account [id = -89] was not found|
	
Scenario: update account with wrong password should fail
Given I provide  "update@me.com" uid and "secret" password
Given I accept "en" language
And I accept "application/json" format
When I send a "update account" request with wrong password "<wrong_password>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_password|message|
||Password is required|
|aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaax|Password max length is 200|

Scenario: update account with wrong first name
Given I provide  "update@me.com" uid and "secret" password
Given I accept "en" language
And I accept "application/json" format
When I send a "update account" request with wrong first name "<wrong_first_name>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_first_name|message|
||First name is required|
|ccccccccccccccccccccccccccccccccccccccccccccccccccx|First name max length is 50|

Scenario: update account with wrong last name
Given I provide  "update@me.com" uid and "secret" password
Given I accept "en" language
And I accept "application/json" format
When I send a "update account" request with wrong last name "<wrong_last_name>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_last_name|message|
||Last name is required|
|ccccccccccccccccccccccccccccccccccccccccccccccccccx|Last name max length is 50|
