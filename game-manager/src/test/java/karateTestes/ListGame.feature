Feature: The GET operation is the way that the user will have to list the registered games, and the way to list:
  By all games and only by publisherId and will show all user-created fields in the POST operation for that game.
  getAllGames: lists all registered games, regardless of publisherId.
  GetGamesByPublisherId: Lists only the games of the identified publisherId.

  Scenario: Successfully listing all games, return the 200 OK code and response body with the fields created by the
  user in the POST operation.
    When the method operation is GET
    Then status code returned is 200
    And id != null
    And publisherId != null
    And name != null
    And the response.size > 0


  Scenario: Successfully when listing a game with a valid 'publisherId', then return the status code 200 OK
  and the response body with fields: id, name and publisherId.
    When the method operation is GET
    Then status code returned is 200
    And id != null
    And publisherId != null
    And name != null
    And the response.size > 0


  Scenario: Successfully when listing a game with a invalid 'publisherId', then return the status code 200 OK
  and the response body with fields: id, name and publisherId.
    When the method operation is GET
    Then status code returned is 200
    And id != null
    And publisherId != null
    And name != null
    And the response == null