Lock account stories

Meta:
@refs 14
@progress done

Narrative:
Given I provide a lock account request
When I send that request
It should either succeed or fail

Scenario: lock account should succeed
Given I authenticate with "admin@admin.com" uid and "secret" password
When I send a valid "lock account" request
Then the response code should be "200"

Scenario: lock account as account with wrong role should fail
Given I authenticate with "rmgr@rmgr.com" uid and "secret" password
And I accept "fr" language
And I accept "application/json" format
When I send a valid "lock account" request
Then the response code should be "403"
And the message should be "<message>"
Examples:
|message|
|Accès refusé|

Scenario: lock account with wrong id should fail
Given I authenticate with "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/xml" format
When I send a "lock account" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"
Examples:
|wrong_id|message|
|-1|Account [id = -1] was not found|
