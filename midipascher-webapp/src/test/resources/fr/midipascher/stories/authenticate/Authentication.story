Authentication stories

Meta:
@refs 1

Narrative:
Given I provide credentials
When I send authentication-required requests
It should either succeed or fail

Scenario: requesting protected resource with wrong uid should fail
Given I authenticate with <uid> uid and whatever password
And I send <requestContentType>
And I accept <language> language
And I provide a valid create food specialty request body 
When I send a create food specialty request
Then I should get an unsuccessfull response
And the response code should be 401
And the error message should be <message>

Examples:
|uid|requestContentType|language|message|
||application/json|en|bad credentials provided|
|unknown-user|application/json|en|bad credentials provided|
||application/xml|en|bad credentials provided|
|unknown-user|application/xml|en|bad credentials provided|
||application/json|fr|informations de connexion erronées|
|unknown-user|application/json|fr|informations de connexion erronées|
||application/xml|fr|informations de connexion erronées|
|unknown-user|application/xml|fr|informations de connexion erronées|

Scenario: requesting protected resource with wrong password should fail
Given I authenticate with whatever uid and <password> password
And I send <requestContentType>
And I accept <language> language
And I provide a valid create food specialty request body 
When I send a create food specialty request
Then I should get an unsuccessfull response
And the response code should be 401
And the error message should be <message>

Examples:
|password|requestContentType|language|message|
||application/json|en|bad credentials provided|
|unknown-password|application/json|en|bad credentials provided|
||application/xml|en|bad credentials provided|
|unknown-password|application/xml|en|bad credentials provided|
||application/json|fr|informations de connexion erronées|
|unknown-password|application/json|fr|informations de connexion erronées|
||application/xml|fr|informations de connexion erronées|
|unknown-password|application/xml|fr|informations de connexion erronées|

Scenario: requesting protected resource with disabled uid should fail
Scenario: requesting protected resource with expired uid should fail
Scenario: requesting protected resource with insufficient privileges should fail
Scenario: search restaurant with no uid should succeed
