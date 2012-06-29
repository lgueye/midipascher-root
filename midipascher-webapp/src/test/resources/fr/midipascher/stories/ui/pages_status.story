Page status stories

Meta:
@refs 15
@progress wip

Narrative:
Given a valid http request to a ui page
When I send that request
It should either succeed

Scenario: sending a request to the index page should succeed
When I send a "<http_method>" request at uri "<uri>"
Then the response code should be "200"

Examples:
|http_method|uri|
|GET|/index|
|GET|/restaurants/new|
|GET|/restaurants/12/edit|
|GET|/restaurants/12/delete|
|GET|/authenticate|
|GET|/login|
|GET|/search|
|GET|/search/new|
|GET|/account/louis.gueye@gmail.com/edit|
|GET|/account/update|
|GET|/favorites|
|GET|/favorites/add|
|GET|/favorites/45/edit|

