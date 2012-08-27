Security story

Meta:
@refs 3
@progress done

Narrative:
In order to provide security capabilities to my service
As a client
I want to clearly get notified when a security event happens

Scenario: requesting a protected resource with correct uid, correct password and insufficient rights should return 403
Given I authenticate with "<uid>" uid and "<password>" password
And I receive "<responseContentType>" data
And I accept "<responseLanguage>" language
When I request a protected resource that require ADMIN rights
Then the response code should be "403"
And the response message should be "<message>"

Examples:
|uid|password|responseContentType|responseLanguage|message|
|louis@rmgr.com|secret|application/xml|en|Access denied|
|louis@rmgr.com|secret|application/xml|fr|Accès refusé|
|louis@rmgr.com|secret|application/json|en|Access denied|
|louis@rmgr.com|secret|application/json|fr|Accès refusé|

Scenario: requesting a protected resource with correct uid, correct password sufficient authority should return 201
Given I authenticate with "<uid>" uid and "<password>" password
When I request a protected resource that require ADMIN rights
Then the response code should be "201"

Examples:
|uid|password|
|admin@admin.com|secret|
