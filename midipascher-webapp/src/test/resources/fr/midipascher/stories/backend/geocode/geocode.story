Geocode stories

Meta:
@refs 17
@progress wip

Narrative:
Given I provide a create/update restaurant request
When I send that request
It should geocode address or fail

Scenario: geocode on restaurant update should succeed
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I reset the coordinates
When I send a valid "create restaurant" request
Then the response code should be "201"
And I should be able to read the new resource
And the address should be geocoded

Scenario: geocode on restaurant update should succeed
Given I provide "<uid>" uid and "<password>" password
And I send "<requestContentType>" format
And I reset the coordinates
When I send a valid "update restaurant" request
Then the response code should be "200"
And I should be able to read the resource
And the address should be geocoded
