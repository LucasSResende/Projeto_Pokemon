Feature: The DELETE operation is responsible for removing an existing game:
  - Deletes game by its UUID.
  - Returns 204 NO CONTENT on success.
  - Returns 404 NOT FOUND if the game id does not exist.

  Background:
    * url 'http://localhost:8081'
    And header Content-Type = 'application/json'

  Scenario: Successfully deleting an existing game
    # 1. Create a game first
    Given path '/game'
    And request { "publisherId": "nintendo", "name": "Game to Delete", "timePlayed": { "2023-01-01": 1 } }
    When method POST
    Then status 201
    * def createdId = response.id

    # 2. Delete the game
    Given path '/game/' + createdId
    When method DELETE
    Then status 204

    # 3. Confirm game is gone
    Given path '/game/' + createdId
    When method GET
    Then status 404

  Scenario: Unsuccessfully deleting a non-existing game
    Given path '/game/00000000-0000-0000-0000-000000000000'
    When method DELETE
    Then status 404
    And match response.title contains "Game was not found."
