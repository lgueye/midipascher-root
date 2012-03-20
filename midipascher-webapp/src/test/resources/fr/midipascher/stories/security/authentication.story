Security story

Meta:
@refs 2
@progress done

Narrative:
In order to provide security capabilities to my service
As a client
I want to clearly get notified when a security event happens

Scenario: requesting a protected resource with wrong uid should return 401
Given I authenticate with <uid> uid and whatever password
And I accept <responseLanguage> language
When I request a protected resource
Then the response code should be 401
And the response message should be <message>

Examples:
|uid|responseLanguage|message|
||en|bad credentials provided|
|unknown-account|en|bad credentials provided|
||fr|informations de connexion erronées|
|unknown-account|fr|informations de connexion erronées|

Scenario: requesting a protected resource with correct uid and wrong password should return 401
Given I authenticate with bob uid and <password> password
And I accept <responseLanguage> language
When I request a protected resource
Then the response code should be 401
And the response message should be <message>

Examples:
|password|responseLanguage|message|
||en|bad credentials provided|
|unknown-password|en|bad credentials provided|
||fr|informations de connexion erronées|
|unknown-password|fr|informations de connexion erronées|
