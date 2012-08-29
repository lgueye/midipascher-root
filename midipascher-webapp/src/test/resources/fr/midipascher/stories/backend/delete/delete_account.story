Delete account stories

Meta:
@refs 13
@progress pending

Narrative:
Given I provide a delete account request
When I send that request
It should either succeed or fail

Scenario: delete account should succeed
Given I provide  "admin@admin.com" uid and "secret" password
When I send a valid "delete account" request
Then the response code should be "200"

Scenario: delete account with wrong uid should fail
Given I provide  "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/xml" format
When I send a "delete account" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"
Examples:
|wrong_id|message|
|-1|Account [id = -1] was not found|
