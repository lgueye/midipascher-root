Update restaurant stories

Meta:
@refs 9
@progress done

Narrative:
Given I provide a update restaurant request
When I send that request
It should either succeed or fail

Scenario: update restaurant should succeed
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
When I send a valid "update restaurant" request
Then the response code should be "200"

Examples:
|uid|password|requestContentType|
|louis@rmgr.com|secret|application/xml|

Scenario: update restaurant with wrong id should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong id "<wrong_id>"
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_id|message|
|louis@rmgr.com|secret|application/xml|application/xml|en|-1|Restaurant [id = -1] was not found|

Scenario: update restaurant with wrong owner should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong account
Then the response code should be "404"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|message|
|louis@rmgr.com|secret|application/xml|application/xml|en|Account [id = -1] was not found|

Scenario: update restaurant with wrong name should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong name "<wrong_name>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_name|message|
|louis@rmgr.com|secret|application/xml|application/xml|en||Name is required|
|louis@rmgr.com|secret|application/xml|application/xml|en|ddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Name max length is 50|

Scenario: update restaurant with wrong description should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong description "<wrong_description>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_description|message|
|louis@rmgr.com|secret|application/xml|application/xml|en|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Description max length is 200|

Scenario: update restaurant with wrong phone number should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong phone number "<wrong_phone_number>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_phone_number|message|
|louis@rmgr.com|secret|application/xml|application/xml|en||Phone number is required|
|louis@rmgr.com|secret|application/xml|application/xml|en|zzzzzzzzzzzzzzzzzzzz1|Phone number max length is 20|

Scenario: update restaurant with wrong main offer should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong main offer "<wrong_main_offer>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_main_offer|message|
|louis@rmgr.com|secret|application/xml|application/xml|en|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Main offer max length is 200|

Scenario: update restaurant with wrong street address should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong street address "<wrong_street_address>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_street_address|message|
|louis@rmgr.com|secret|application/xml|application/xml|en||Street address is required|
|louis@rmgr.com|secret|application/xml|application/xml|en|ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss1|Street address max length is 100|

Scenario: update restaurant with wrong city should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong city "<wrong_city>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_city|message|
|louis@rmgr.com|secret|application/xml|application/xml|en||City is required|
|louis@rmgr.com|secret|application/xml|application/xml|en|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1|City max length is 50|

Scenario: update restaurant with wrong postal code should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong postal code "<wrong_postal_code>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_postal_code|message|
|louis@rmgr.com|secret|application/xml|application/xml|en||Postal code is required|
|louis@rmgr.com|secret|application/xml|application/xml|en|ssssssssss1|Postal code max length is 10|

Scenario: update restaurant with wrong country code should fail
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I accept "<responseLanguage>" language
And I accept "<responseContentType>" format
When I send a "update restaurant" request with wrong country code "<wrong_country_code>"
Then the response code should be "400"
And the message should be "<message>"

Examples:
|uid|password|requestContentType|responseContentType|responseLanguage|wrong_country_code|message|
|louis@rmgr.com|secret|application/xml|application/xml|en||Country code exact length is 2|
|louis@rmgr.com|secret|application/xml|application/xml|en|FRA|Country code exact length is 2|
