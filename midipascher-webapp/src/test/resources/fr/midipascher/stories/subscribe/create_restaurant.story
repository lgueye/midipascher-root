Create restaurant stories

Meta:
@refs 6
@progress done

Narrative:
Given I provide a create restaurant request
When I send that request
It should either succeed or fail

Scenario: create restaurant should succeed
Given I provide  "louis" uid and "secret" password
When I send a valid "create restaurant" request
Then the response code should be "201"
And I should be able to read the new resource

Scenario: create restaurant with wrong owner should fail
Given I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong account
Then the response code should be "404"
And the message should be "<message>"
Examples:
|message|
|Account [id = -1] was not found|

Scenario: create restaurant with wrong name should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong name "<wrong_name>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_name|message|
||Name is required|
|ddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Name max length is 50|

Scenario: create restaurant with wrong description should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong description "<wrong_description>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_description|message|
|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Description max length is 200|

Scenario: create restaurant with wrong phone number should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong phone number "<wrong_phone_number>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_phone_number|message|
||Phone number is required|
|zzzzzzzzzzzzzzzzzzzz1|Phone number max length is 20|

Scenario: create restaurant with wrong main offer should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong main offer "<wrong_main_offer>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_main_offer|message|
|dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd1|Main offer max length is 200|

Scenario: create restaurant with wrong specialties should fail

Scenario: create restaurant with wrong street address should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong street address "<wrong_street_address>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_street_address|message|
||Street address is required|
|ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss1|Street address max length is 100|

Scenario: create restaurant with wrong city should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong city "<wrong_city>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_city|message|
||City is required|
|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1|City max length is 50|

Scenario: create restaurant with wrong postal code should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong postal code "<wrong_postal_code>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_postal_code|message|
||Postal code is required|
|ssssssssss1|Postal code max length is 10|

Scenario: create restaurant with wrong country code should fail
Given I provide  "louis" uid and "secret" password
And I accept "en" language
And I accept "application/json" format
When I send a "create restaurant" request with wrong country code "<wrong_country_code>"
Then the response code should be "400"
And the message should be "<message>"
Examples:
|wrong_country_code|message|
||Country code exact length is 2|
|FRA|Country code exact length is 2|
