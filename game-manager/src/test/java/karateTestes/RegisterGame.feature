Feature: The POST operation is responsible for the user to create a game that has the following items:
  publisherId: Required and of type String.
  name: Required and of the String type, limited to 40 characters.
  timePlayed: optional and of the Map type, with the information -> date:"YYYY-MM-DD" (of the LocalDate type);
  hours (of the Integer type). As soon as the game is created, it must be returned with the status 201 CREATED and
  shows a payload containing the information created plus the id that is automatically generated.

  Scenario: Successfully when creating a game with a registered 'publisherId', then return the status code 201 CREATED
  and the response body with fields: id, name and publisherId.
    When the method operation is POST
    Then status code returned is 201
    And id != null
    And publisherId != null
    And name == "<ValidName>"
    And timePlayed == null

  Scenario: Unsuccessfully when creating a game without a valid 'publisherId', then return the status code 400 BAD
  REQUEST and returned a publisher field exception message.
    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "message"

  Scenario: Unsuccessfully when creating a game without a valid 'name', then return the status code 400 BAD REQUEST
  and returned a name field exception message.
    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "message"

  Scenario: Successfully when creating a game with a valid 'publisherId', 'name' and 'timePlayed' fields, then
  return the status code 201 CREATED and the response body with fields: id, name, publisherId and timePlayed fields.
    When the method operation is POST
    Then status code returned is 201
    And id != null
    And publisherId != null
    And name == "<validName>";
    And 0 <= timePlayed.hour <= 24

  Scenario: Unsuccessfully when creating a game with a negative number of hour on 'timePlayed' , then return
  the status code 400 BAD REQUEST and a timePlayed field exception message.
    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "message"