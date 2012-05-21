Inactivate authorities stories

Meta:
@refs 11
@progress done

Narrative:
Given I provide an inactivate authority request
When I send that request
It should either succeed or fail

Scenario: inactivate authority should succeed
Given I authenticate with "admin@admin.com" uid and "secret" password
When I send a valid "inactivate authority" request
Then the response code should be "200"

Scenario: inactivate authority as account with wrong role should fail
Given I authenticate with "rmgr@rmgr.com" uid and "secret" password
And I accept "fr" language
And I accept "application/json" format
When I send a valid "inactivate authority" request
Then the response code should be "403"
And the message should be "<message>"
Examples:
|message|
|Accès refusé|

Scenario: inactivate authority with wrong id should fail
Given I authenticate with "admin@admin.com" uid and "secret" password
And I accept "en" language
And I accept "application/xml" format
When I send a "inactivate authority" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"
Examples:
|wrong_id|message|
|-1|Authority [id = -1] was not found|
