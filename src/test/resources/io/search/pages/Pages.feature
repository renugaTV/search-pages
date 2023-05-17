Feature: Pages

  Scenario: Happy Path Scenario of Search When clicking middle index of the list AND maximum limit is even number
    Given I want to display 10 pages
    When I click page Number 6 AND I have 15 total available results pages
    Then I should get page Numbers of [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

  Scenario: Happy Path Scenario of Search When clicking middle index of the list AND maximum limit is odd number
    Given I want to display 11 pages
    When I click page Number 7 AND I have 15 total available results pages
    Then I should get page Numbers of [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]

  Scenario: Happy Path Scenario of Search When clicking leftside index of the list
    Given I want to display 10 pages
    When I click page Number 2 AND I have 15 total available results pages
    Then I should get page Numbers of [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

  Scenario: Edge case Scenario of Search When clicking left most index of the list
    Given I want to display 10 pages
    When I click page Number 1 AND I have 15 total available results pages
    Then I should get page Numbers of [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

  Scenario: Happy Path Scenario of Search When clicking RightSide index of the list AND When more pages are available to load on Right side.
    Given I want to display 10 pages
    When I click page Number 9 AND I have 15 total available results pages
    Then I should get page Numbers of [4, 5, 6, 7, 8, 9, 10, 11, 12, 13]

  Scenario: Happy Path Scenario of Search When clicking RightSide index of the list AND When only few pages are available to load on Right side.
    Given I want to display 10 pages
    When I click page Number 14 AND I have 15 total available results pages
    Then I should get page Numbers of [6, 7, 8, 9, 10, 11, 12, 13, 14, 15]

  Scenario: Edge case Scenario of Search When clicking Last available Page
    Given I want to display 10 pages
    When I click page Number 15 AND I have 15 total available results pages
    Then I should get page Numbers of [6, 7, 8, 9, 10, 11, 12, 13, 14, 15]

  Scenario: Edge case Scenario of Search When total available page is less than max page to be displayed
    Given I want to display 10 pages
    When I click page Number 4 AND I have 5 total available results pages
    Then I should get page Numbers of [1, 2, 3, 4, 5]

  Scenario: Happy Path Scenario of Search Changing the maximum Limit of pages to be displayed
    Given I want to display 15 pages
    When I click page Number 4 AND I have 20 total available results pages
    Then I should get page Numbers of [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]

  Scenario: Edge case Scenario of Search Changing the maximum Limit of pages to be displayed and the maxLimit exceeds the total available page
    Given I want to display 25 pages
    When I click page Number 4 AND I have 20 total available results pages
    Then I should get page Numbers of [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]