Feature: The POST operation is responsible for the user to create a game that has the following items:
  publisherId: Mandatory and of type String.
  name: Mandatory and of the String type, limited to 40 characters.
  timePlayed: optional and of the Map type, with the information -> date:"YYYY-MM-DD" (of the LocalDate type);
  hours (of the Integer type). As soon as the game is created, it must be returned with the status 201 CREATED and
  shows a payload containing the information created plus the id that is automatically generated.

  Background:
    * url 'http://localhost:8081'
    * def game = read('classpath:karateTests/utils/game.json')
    And header Content-Type = 'application/json'
    Given path '/game'

  Scenario Outline: Successfully when creating a game with a valid 'name' and 'publisherId', then return the status
  code 201 CREATED and the response body with fields: id, name and publisherId.
    * remove game.timePlayed
    * replace game
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
    And request game
    When method POST
    Then status 201
    And match response.id == '#present'
    And match response.publisherId == '<publisherId>'
    And match response.name == '<name>'
    And match response.timePlayed == '#notpresent'
    Examples:
      | publisherId | name          |
      | nintendo    | Mario         |
      | sega        | Sonic         |
      | capcom      | Resident Evil |
      | warner      | Mortal Kombat |

  @BaseCreation
  Scenario Outline: Successfully when creating a game with a valid 'publisherId', 'name' and 'timePlayed' fields, then
  return the status code 201 CREATED and the response body with fields: id, name, publisherId and timePlayed.
    * replace game
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
      | firstDate   | firstDate   |
      | secondDate  | secondDate  |
      | thirdDate   | thirdDate   |
      | fourthDate  | fourthDate  |
      | firstTime   | firstTime   |
      | secondTime  | secondTime  |
      | thirdTime   | thirdTime   |
      | fourthTime  | fourthTime  |
    And request game
    When method POST
    Then status 201
    And match response.id == '#present'
    And match response.publisherId == "<publisherId>"
    And match response.name == "<name>"
    And response.timePlayed == {"<firstDate>": <firstTime>, "<secondDate>": <secondTime>, "<thirdDate>": <thirdTime>, "<fourthDate>": <fourthTime>};

    Examples:
      | publisherId | name          | firstDate  | secondDate | thirdDate  | fourthDate | firstTime | secondTime | thirdTime | fourthTime |
      | nintendo    | Mario         | 2000-05-19 | 1999-08-02 | 1999-12-01 | 1999-05-01 | 12        | 5          | 12        | 12         |
      | sega        | Sonic         | 1987-04-22 | 2005-10-29 | 2000-03-01 | 1995-02-01 | 3         | 0          | 16        | 14         |
      | capcom      | Resident Evil | 2018-09-21 | 2022-02-12 | 2021-12-18 | 2009-10-18 | 16        | 18         | 13        | 4          |
      | warner      | Mortal Kombat | 2022-11-01 | 2022-08-30 | 2021-12-19 | 2018-12-19 | 18        | 12         | 11        | 14         |

  Scenario Outline: Unsuccessfully when creating a game without a valid 'publisherId', then return the status code 400
  BAD REQUEST and returned a publisher field exception message.
    * remove game.timePlayed
    * replace game
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
    And request game
    When method POST
    Then status 400
    And match response.detail contains "PublisherId was not found."
    Examples:
      | publisherId | name          |
      | ------      | Mario         |
      | ******      | Sonic         |
      | ljsdz       | Resident Evil |
      | 987654      | Mortal Kombat |

  Scenario: Unsuccessfully when creating a game null 'publisherId', then return the status code 400 BAD REQUEST and returned
  a publisherId field exception message (Group with errors).
    Given param publisherId = null
    * remove game.timePlayed
    And request game
    When method POST
    Then status 400
    And match response.detail contains "PublisherId was not found."

  Scenario Outline: Unsuccessfully when creating a game null 'name', then return the status code 400 BAD REQUEST and
  returned a name field exception message.
    * remove game.timePlayed
    * remove game.name
    * replace game
      | token       | value       |
      | publisherId | publisherId |
    And request game
    When method POST
    Then status 400
    And match response.detail contains "Name can not be empty"
    Examples:
      | publisherId |
      | nintendo    |
      | sega        |
      | capcom      |
      | warner      |

  Scenario Outline: Unsuccessfully when creating a game with invalid number of hours on 'timePlayed' , then return
  the status code 400 BAD REQUEST and a timePlayed field exception message.
    * replace game
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
      | firstDate   | firstDate   |
      | secondDate  | secondDate  |
      | thirdDate   | thirdDate   |
      | fourthDate  | fourthDate  |
      | firstTime   | firstTime   |
      | secondTime  | secondTime  |
      | thirdTime   | thirdTime   |
      | fourthTime  | fourthTime  |
    And request game
    When method POST
    Then status 400
    And match response.detail contains 'Hour can not exceeded 24'
    Examples:
      | publisherId | name          | firstDate  | secondDate | thirdDate  | fourthDate | firstTime | secondTime | thirdTime | fourthTime |
      | nintendo    | Mario         | 2000-05-19 | 1999-08-02 | 1999-12-01 | 1999-05-01 | 30        | 5          | 12        | 12         |
      | sega        | Sonic         | 1987-04-22 | 2005-10-29 | 2000-03-01 | 1995-02-01 | 3         | 0          | 26        | 14         |
      | capcom      | Resident Evil | 2018-09-21 | 2022-02-12 | 2021-12-18 | 2009-10-18 | 16        | 29         | 25        | 4          |
      | warner      | Mortal Kombat | 2022-11-01 | 2022-08-30 | 2021-12-19 | 2018-12-19 | 18        | 12         | 44        | 14         |

  Scenario Outline: Unsuccessfully when creating a game with size of name field bigger than 40, then return the status
  code 400 BAD REQUEST and a name field exception message.
    * remove game.timePlayed
    * replace game
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
    And request game
    When method POST
    Then status 400
    And match response.detail contains "Name can not exceeded 40 characters"
    Examples:
      | publisherId | name                                                     |
      | nintendo    | Mario Kart Company 1111111111111111111111                |
      | sega        | Sonic 222222222222222222222222222222222222222222222      |
      | capcom      | Resident Evil 33333333333333333333333333333              |
      | warner      | Mortal Kombat 444444444444444444444444444444444444444444 |