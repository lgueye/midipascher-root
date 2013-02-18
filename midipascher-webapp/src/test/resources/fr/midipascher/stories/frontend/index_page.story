Index page stories

Meta:
@refs 18
@progress wip

Narrative:
Given I am a customer
When I navigate to the index page
It should succeed
And various page elements should be present

Scenario: various page elements should be present
Given I navigate to the "index" page
Then my outcome should be the "index" page
Then I should be able to click the "authenticate" link
And I should be able to click the "favorite-searches" link
And I should be able to input the "street-address"
And I should be able to input the "postal-code"
And I should be able to click the "find-by-address" button
And I should be able to click the "find-by-current-location" button
And I should be able to click the "legal" link
And I should be able to click the "confidentiality" link
And I should be able to click the "about" link

Scenario: clicking the "authenticate" link should succeed
Given I navigate to the "index" page
When I click on the "authenticate" link
Then my outcome should be the "authenticate" page

Scenario: clicking the "favorite-searches" link should succeed
Given I navigate to the "index" page
When I click on the "favorite-searches" link
Then my outcome should be the "favorite-searches" page

Scenario: clicking the "legal" link should succeed
Given I navigate to the "index" page
When I click on the "legal" link
Then my outcome should be the "legal" page

Scenario: clicking the "confidentiality" link should succeed
Given I navigate to the "index" page
When I click on the "confidentiality" link
Then my outcome should be the "confidentiality" page

Scenario: clicking the "about" link should succeed
Given I navigate to the "index" page
When I click on the "about" link
Then my outcome should be the "about" page

Scenario: clicking the "find-by-address" button with empty fields should lead to the search page with no result
Given I navigate to the "index" page
And I input the "street-address" with "_blank"
And I input the "postal-code" with "_blank"
When I click on the "find-by-address" button
Then my outcome should be the "index" page
And my outcome should be the "index" page

Scenario: clicking the "find-by-address" button with empty fields should lead to the search page
Given I navigate to the "index" page
And I input the "street-address" with "5 Rue Anatole France"
And I input the "postal-code" with "92800"
When I click on the "find-by-address" button
Then my outcome should be the "search" page
