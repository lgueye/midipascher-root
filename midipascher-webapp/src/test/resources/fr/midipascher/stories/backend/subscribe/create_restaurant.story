Create restaurant stories

Meta:
@refs 6
@progress done

Narrative:
Given I provide a create restaurant request
When I send that request
It should either succeed or fail

Scenario: create restaurant should succeed
Given I provide "<uid>" uid and "<password>" password
When I send a valid "create restaurant" request
Then the response code should be "201"
And I should be able to read the new resource

Examples:
|uid|password|
|louis@rmgr.com|secret|

Scenario: create restaurant with wrong owner should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong account
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password||message|responseContentType|responseLanguage|
|rmgr@rmgr.com|secret|Account [id = -1] was not found|application/json|en|

Scenario: create restaurant with wrong name should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong name "<wrong_name>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|wrong_name|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret||Name is required|application/json|en|
|louis@rmgr.com|secret|ddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Name max length is 50|application/json|en|

Scenario: create restaurant with wrong description should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong description "<wrong_description>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|wrong_description|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Description max length is 200|application/json|en|

Scenario: create restaurant with wrong phone number should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong phone number "<wrong_phone_number>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|wrong_phone_number|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret||Phone number is required|application/json|en|
|louis@rmgr.com|secret|zzzzzzzzzzzzzzzzzzzz1|Phone number max length is 20|application/json|en|

Scenario: create restaurant with wrong main offer should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong main offer "<wrong_main_offer>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|wrong_main_offer|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Main offer max length is 200|application/json|en|

Scenario: create restaurant with wrong street address should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong street address "<wrong_street_address>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|uid|password|wrong_street_address|message|responseContentType|responseLanguage|
|louis@rmgr.com||Street address is required|application/json|en|
|louis@rmgr.com|ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss1|Street address max length is 100|application/json|en|

Scenario: create restaurant with wrong city should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong city "<wrong_city>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|uid|password|wrong_city|message|responseContentType|responseLanguage|
|louis@rmgr.com||City is required|application/json|en|
|louis@rmgr.com|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1|City max length is 50|application/json|en|

Scenario: create restaurant with wrong postal code should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong postal code "<wrong_postal_code>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|uid|password|wrong_postal_code|message|responseContentType|responseLanguage|
|louis@rmgr.com||Postal code is required|application/json|en|
|louis@rmgr.com|ssssssssss1|Postal code max length is 10|application/json|en|

Scenario: create restaurant with wrong country code should fail
Given I provide "<uid>" uid and "<password>" password
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong country code "<wrong_country_code>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|uid|password|wrong_country_code|message|responseContentType|responseLanguage|
|louis@rmgr.com||Country code exact length is 2|application/json|en|
|louis@rmgr.com|FRA|Country code exact length is 2|application/json|en|
