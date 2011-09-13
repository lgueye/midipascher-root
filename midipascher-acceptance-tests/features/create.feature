Feature: Create entity

	Scenario Outline: I should be able to create a restaurant
		Given I am a valid service user
		And I am allowed to update resources
		And I send "<request_content_type>"
		And I receive "<response_content_type>"
		When I send a POST request on restaurant with valid body
		Then the response status code should be "201"
		And I should be able to successfully read location 

	Examples:
    	|	request_content_type	|	response_content_type	|
    	|	application/json		|	application/xml			|
    	|	application/xml			|	application/json		|

	Scenario Outline: I should be able to create a food specialty
		Given I am a valid service user
		And I am allowed to update resources
		And I send "<request_content_type>"
		And I receive "<response_content_type>"
		When I send a POST request on food specialty with valid body
		Then the response status code should be "201"
		And I should be able to successfully read location 

	Examples:
    	|	request_content_type	|	response_content_type	|
    	|	application/json		|	application/xml			|
    	|	application/xml			|	application/json		|
