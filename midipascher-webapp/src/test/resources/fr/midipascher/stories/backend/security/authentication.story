Security story

Meta:
@refs 2
@progress wip

Narrative:
In order to provide security capabilities to my service
As a client
I want to clearly get notified when a security event happens

Scenario: requesting a protected resource with wrong uid should return 401
Given I authenticate with "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
When I request a protected resource
Then the response code should be "401"
And the response message should be "<message>"

Examples:
|uid|password|responseLanguage|message|
||whatever|en|bad credentials provided|
|unknown-account|whatever|en|bad credentials provided|
||whatever|fr|informations de connexion erronées|
|unknown-account|whatever|fr|informations de connexion erronées|

Scenario: requesting a protected resource with correct uid and wrong password should return 401
Given I authenticate with "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
When I request a protected resource
Then the response code should be "401"
And the response message should be "<message>"

Examples:
|uid|password|responseLanguage|message|
|bob||en|bad credentials provided|
|bob|unknown-password|en|bad credentials provided|
|bob||fr|informations de connexion erronées|
|bob|unknown-password|fr|informations de connexion erronées|
