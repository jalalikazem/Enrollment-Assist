Feature: Can Make a Course?


  Scenario: Add A Course Successful
    Given title "riazi" and CourseNumber "1234567" and credits 2 and graduateLevel "Undergraduate"
    When I Create a new Course
    Then Course Should be created with 0 exception

  Scenario: Add A Course with invalid courseNumber
    Given title "riazi" and CourseNumber "12343223567" and credits 2 and graduateLevel "Undergraduate"
    When I Create a new Course
    Then Course Should be created with 1 exception

  Scenario: Add A Course with invalid tilte
    Given title "" and CourseNumber "1234567" and credits 2 and graduateLevel "Undergraduate"
    When I Create a new Course
    Then Course Should be created with 1 exception

  Scenario: Add A Course with invalid graduateLevel
    Given title "riazi" and CourseNumber "1234567" and credits 2 and graduateLevel "Underffgraduate"
    When I Create a new Course
    Then Course Should be created with 1 exception