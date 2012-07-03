Delete restaurant stories

Meta:
@refs 12
@progress done

Narrative:
Given I provide a delete restaurant request
When I send that request
It should either succeed or fail

Scenario: delete restaurant should succeed
Given I provide  "louis@rmgr.com" uid and "secret" password
When I send a valid "delete restaurant" request
Then the response code should be "200"

Scenario: delete restaurant as restaurant with wrong account should fail
Given I provide  "louis@rmgr.com" uid and "secret" password
Given I accept "en" language
And I accept "application/json" format
When I send a "delete restaurant" request with wrong account
Then the response code should be "404"
And the message should be "<message>"
Examples:
|message|
|Account [id = -1] was not found|

Scenario: delete restaurant with wrong id should fail
Given I provide  "louis@rmgr.com" uid and "secret" password
And I accept "en" language
And I accept "application/xml" format
When I send a "delete restaurant" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"
Examples:
|wrong_id|message|
|-1|Restaurant [id = -1] was not found|
