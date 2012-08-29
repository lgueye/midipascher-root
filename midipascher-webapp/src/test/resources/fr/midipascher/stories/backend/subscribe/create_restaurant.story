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
And I send "<requestContentType>" format
When I send a valid "create restaurant" request
Then the response code should be "201"
And I should be able to read the new resource

Examples:
|uid|password|requestContentType|
|louis@rmgr.com|secret|application/xml|

Scenario: create restaurant with wrong owner should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong account
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml|Account [id = -1] was not found|application/xml|en|

Scenario: create restaurant with wrong name should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong name "<wrong_name>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_name|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml||Name is required|application/xml|en|
|louis@rmgr.com|secret|application/xml|ddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Name max length is 50|application/xml|en|

Scenario: create restaurant with wrong description should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong description "<wrong_description>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_description|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Description max length is 200|application/xml|en|

Scenario: create restaurant with wrong phone number should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong phone number "<wrong_phone_number>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_phone_number|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml||Phone number is required|application/xml|en|
|louis@rmgr.com|secret|application/xml|zzzzzzzzzzzzzzzzzzzz1|Phone number max length is 20|application/xml|en|

Scenario: create restaurant with wrong main offer should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong main offer "<wrong_main_offer>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_main_offer|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Main offer max length is 200|application/xml|en|

Scenario: create restaurant with wrong street address should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong street address "<wrong_street_address>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_street_address|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml||Street address is required|application/xml|en|
|louis@rmgr.com|secret|application/xml|ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss1|Street address max length is 100|application/xml|en|

Scenario: create restaurant with wrong city should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong city "<wrong_city>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_city|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml||City is required|application/xml|en|
|louis@rmgr.com|secret|application/xml|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1|City max length is 50|application/xml|en|

Scenario: create restaurant with wrong postal code should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong postal code "<wrong_postal_code>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_postal_code|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml||Postal code is required|application/xml|en|
|louis@rmgr.com|secret|application/xml|ssssssssss1|Postal code max length is 10|application/xml|en|

Scenario: create restaurant with wrong country code should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "create restaurant" request with wrong country code "<wrong_country_code>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|wrong_country_code|message|responseContentType|responseLanguage|
|louis@rmgr.com|secret|application/xml||Country code exact length is 2|application/xml|en|
|louis@rmgr.com|secret|application/xml|FRA|Country code exact length is 2|application/xml|en|
