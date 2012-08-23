Page status stories

Meta:
@refs 16
@progress wip

Narrative:
Given a valid login request
When I send that request
It should either succeed or fail

Scenario: sending a login request should succeed
When I submit a valid "login" form with "laurent.thibaut@gmail.com" as "uid" 
Then I should get redirected to the "index" page
And the response should not contain a "/authenticate" link
And the response should contain a "/accounts/laurent.thibaut@gmail.com/edit" link

Scenario: sending a login request should fail
Given I accept "en" language
When I submit an invalid "login" form with "laurent.thibaut" as "uid"
Then I should get forwarded to the "login" page
And the response should contain a "Valid email format is required" text
