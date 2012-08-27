Content Negotiation story

Meta:
@refs 1
@progress wip

Narrative:
In order to provide content negotiation capabilities to my service
As a client
I want to specify a content type and get that content

Scenario: response content negotiation should succeed
Given I receive "<responseContentType>" data
When I send a search request
Then the response code should be "200"

Examples:
|responseContentType|
|application/xml|
|application/json|

Scenario: response content negotiation should fail
Given I receive "<responseContentType>" data
When I send a search request
Then the response code should be "406"

Examples:
|responseContentType|
|application/octet-stream|

Scenario: request content negotiation should succeed
Given I send "<requestContentType>" data
When I send a create request
Then the response code should be "201"
And I should get my newly created resource

Examples:
|requestContentType|
|application/json|
