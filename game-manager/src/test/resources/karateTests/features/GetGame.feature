Feature: The GET operation is the way that the user will have to list the registered games, and the way to list:
  By all games and only by publisherId and will show all user-created fields in the POST operation for that game.
  getAllGames: lists all registered games, regardless of publisherId.
  GetGamesByPublisherId: Lists only the games of the identified publisherId. (Test def read 8th line)

  Background:
    * url 'http://localhost:8081'
    * def game = read('classpath:karateTests/utils/game.json')
    * def postGame = callonce read('classpath:karateTests/features/PostGame.feature@BaseCreation')
    And header Content-Type = 'application/json'
    Given path '/game'

  Scenario: Successfully listing all games, return the 200 OK code and response body with the fields created by the
  user in the POST operation.
    When method GET
    Then status 200
    And match each response[*].id == '#present'
    And match each response[*].publisherId == '#present'
    And match each response[*].name == '#present'
    And def listSize = karate.sizeOf(response)
    And assert listSize > 0

  Scenario Outline: Successfully when listing a game with a valid 'publisherId', then return the status code 200 OK
  and the response body with fields: id, name and publisherId.
    Given param publisherId = '<publisherId>'
    * remove game.timePlayed
    * replace game
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
    And request game
    When method GET
    Then status 200
    And match each response[*].id == '#present'
    And match each response[*].publisherId == '<publisherId>'
    And match each response[*].name == '#present'
    And def listSize = karate.sizeOf(response)
    And assert listSize > 0
    Examples:
      | publisherId | name          |
      | nintendo    | Mario         |
      | sega        | Sonic         |
      | capcom      | Resident Evil |
      | warner      | Mortal Kombat |

  Scenario Outline: Successfully when listing a game with an invalid 'publisherId', then return the status code 200 OK
  and the response body with fields: id, name and publisherId.
    Given param publisherId = '<publisherId>'
    * remove game.timePlayed
    * replace game
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |

    And request game
    When method GET
    Then status 200
    And match response == []
    Examples:
      | publisherId | name          |
      | -----       | Mario         |
      | ****        | Sonic         |
      |             | Resident Evil |
      | 987654      | Mortal Kombat |