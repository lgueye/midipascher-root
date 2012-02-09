Create user stories

Meta:
@refs 5
@progress wip


Narrative:
Given I provide a create user request
When I send that request
It should either succeed or fail

Scenario: create user should succeed
When I send a valid "create user" request
Then the response code should be "201"
And I should be able to read the new resource

Scenario: create user with wrong uid should fail
Given existing users:
|firstName|lastName|uid|password|
|firstName|lastName|EXISTS|password|
And I accept "en" language
And I accept "application/json" format
When I send a "create user" request with wrong uid "<wrong_uid>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_uid|message|
||Email is required|
|@mail.mail|Valid email format is required|
|mailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmail@mail.mail|Email max length is 100|
|EXISTS|Email "EXISTS" already used|

Scenario: create user with wrong password should fail
Given I accept "en" language
And I accept "application/json" format
When I send a "create user" request with wrong password "<wrong_password>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_password|message|
||Password is required|
|aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaax|Password max length is 200|

Scenario: create user with wrong first name
Given I accept "en" language
And I accept "application/json" format
When I send a "create user" request with wrong first name "<wrong_first_name>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_first_name|message|
||First name is required|
|ccccccccccccccccccccccccccccccccccccccccccccccccccx|First name max length is 50|

Scenario: create user with wrong last name
Given I accept "en" language
And I accept "application/json" format
When I send a "create user" request with wrong last name "<wrong_last_name>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_last_name|message|
||Last name is required|
|ccccccccccccccccccccccccccccccccccccccccccccccccccx|Last name max length is 50|
