Authentication stories

Meta:
@refs 1

Narrative:
Given I provide credentials
When I send authentication-required requests
It should either succeed or fail

Scenario: create food specialty with wrong uid should fail
Given I authenticate with <uid> uid and whatever password
And I send <request-contenttype>
And I provide a valid create food specialty request body 
When I send a create food specialty request
Then I should get an unsuccessfull response
And the response code should be 401

Examples:
|uid|request-contenttype|
||application/json|
|unknown-user|application/json|
||application/xml|
|unknown-user|application/xml|


Scenario: create food specialty with wrong password should fail
Scenario: create food specialty with disabled uid should fail
Scenario: create food specialty with expired uid should fail
Scenario: search restaurant with no uid should succeed
