Feature: Can Take Course

  Scenario: I Have not Preq
    Given I have No Req
    When I Asking can take the course
    Then Violation should not be 0


  Scenario: I Have Preq
    Given I have Req
    When I Asking can take the course
    Then Violation should be 0