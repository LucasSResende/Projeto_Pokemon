Feature: The PUT operation is responsible for updating an existing game:
  - Updates game name, publisherId and timePlayed map.
  - Requires a valid publisherId.
  - Returns 200 OK with the updated game payload.
  - Returns 404 NOT FOUND if the game id does not exist.
  - Returns 400 BAD REQUEST if publisherId does not exist or payload fields are invalid.

  Background:
    * url 'http://localhost:8081'
    * def game = read('classpath:karateTests/utils/game.json')
    And header Content-Type = 'application/json'

  Scenario: Successfully updating an existing game
    # 1. Create a game first
    Given path '/game'
    And request { "publisherId": "nintendo", "name": "Zelda Ocarina", "timePlayed": { "2023-01-01": 5 } }
    When method POST
    Then status 201
    * def createdId = response.id

    # 2. Update the game
    Given path '/game/' + createdId
    And request { "publisherId": "nintendo", "name": "Zelda Ocarina of Time", "timePlayed": { "2023-01-01": 10, "2023-01-02": 4 } }
    When method PUT
    Then status 200
    And match response.id == createdId
    And match response.name == "Zelda Ocarina of Time"
    And match response.publisherId == "nintendo"
    And match response.timePlayed["2023-01-01"] == 10
    And match response.timePlayed["2023-01-02"] == 4

  Scenario: Unsuccessfully updating a game with non-existing id
    Given path '/game/00000000-0000-0000-0000-000000000000'
    And request { "publisherId": "nintendo", "name": "Fake Game" }
    When method PUT
    Then status 404
    And match response.title contains "Game was not found."

  Scenario: Unsuccessfully updating a game with invalid publisherId
    # 1. Create a game first
    Given path '/game'
    And request { "publisherId": "nintendo", "name": "Metroid", "timePlayed": { "2023-01-01": 2 } }
    When method POST
    Then status 201
    * def createdId = response.id

    # 2. Update with invalid publisherId
    Given path '/game/' + createdId
    And request { "publisherId": "invalidPublisher", "name": "Metroid Prime" }
    When method PUT
    Then status 400
    And match response.detail contains "PublisherId was not found."
