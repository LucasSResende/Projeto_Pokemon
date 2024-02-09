Feature: The POST operation is responsible for the user to create a game that has the following items:
  publisherId: Mandatory and of type String.
  name: Mandatory and of the String type, limited to 40 characters.
  timePlayed: optional and of the Map type, with the information -> date:"YYYY-MM-DD" (of the LocalDate type);
  hours (of the Integer type). As soon as the game is created, it must be returned with the status 201 CREATED and
  shows a payload containing the information created plus the id that is automatically generated.

  Scenario Outline: Successfully when creating a game with a valid 'name' and 'publisherId', then return the status
  code 201 CREATED and the response body with fields: id, name and publisherId.
    * replace
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |

    When the method operation is POST
    Then status code returned is 201
    And id != null
    And publisherId == "<publisherId>"
    And name == "<name>"

    Examples:
      | publisherId | name          |
      | Nintendo    | Mario         |
      | Sega        | Sonic         |
      | Capcom      | Resident Evil |
      | Warner Bros | Mortal Kombat |

  Scenario Outline: Successfully when creating a game with a valid 'publisherId', 'name' and 'timePlayed' fields, then
  return the status code 201 CREATED and the response body with fields: id, name, publisherId and timePlayed fields.
    * replace
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
      | firstDate   | firstDate   |
      | secondDate  | secondDate  |
      | thirdDate   | thirdDate   |
      | firstTime   | firstTime   |
      | secondTime  | secondTime  |
      | thirdTime   | thirdTime   |
      | fourthTime  | fourthTime  |

    When the method operation is POST
    Then status code returned is 201
    And id != null
    And publisherId == "<publisherId>";
    And name == "<name>";
    And timePlayed == {"<firstDate>": <firstTime>, "<secondDate>": <secondTime>, "<thirdDate>": <thirdTime>}, "<fourthDate>": <fourthTime>};

    Examples:
      | publisherId | name          | firstDate  | secondDate | thirdDate  | fourthDate | firstTime | secondTime | thirdTime | fourthTime |
      | Nintendo    | Mario         | 2023-10-15 | 2022-02-25 | 2021-12-11 | 2021-12-11 | 18        | 23         | 2         | 12         |
      | Sega        | Sonic         | 2023-10-02 | 2022-02-28 | 2021-12-13 | 2021-12-13 | 22        | 16         | 4         | 8          |
      | Capcom      | Resident Evil | 2023-11-21 | 2022-03-28 | 2021-12-18 | 2021-12-18 | 16        | 15         | 2         | 4          |
      | Warner Bros | Mortal Kombat | 2023-12-01 | 2022-08-30 | 2021-12-19 | 2021-12-19 | 18        | 12         | 2         | 14         |

  Scenario Outline: Unsuccessfully when creating a game without a valid 'publisherId', then return the status code 400
  BAD REQUEST and returned a publisher field exception message.
    * replace
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "The publisherId can't be empty"
    Examples:
      | publisherId | name          |
      | -----       | Mario         |
      | ****        | Sonic         |
      |             | Resident Evil |
      | 987654      | Mortal Kombat |

  Scenario: Unsuccessfully when creating a game null 'publisherId', then return the status code 400 BAD REQUEST and returned
  a publisherId field exception message.
    Given param publisherId = null
    * remove game.timePlayed
    And request game
    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "The publisherId can't be null"

  Scenario: Unsuccessfully when creating a game null 'name', then return the status code 400 BAD REQUEST and returned
  a name field exception message.
    Given param name = null
    * remove game.timePlayed
    And request game
    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "The name can't be null"

  Scenario Outline: Unsuccessfully when creating a game with invalid number of hours on 'timePlayed' , then return
  the status code 400 BAD REQUEST and a timePlayed field exception message.
    * replace
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |
      | firstDate   | firstDate   |
      | secondDate  | secondDate  |
      | thirdDate   | thirdDate   |
      | firstTime   | firstTime   |
      | secondTime  | secondTime  |
      | thirdTime   | thirdTime   |
      | fourthTime  | fourthTime  |
    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "Hours can not exceded 24"
    Examples:
      | publisherId | name          | firstDate  | secondDate | thirdDate  | fourthDate | firstTime | secondTime | thirdTime | fourthTime |
      | Nintendo    | Mario         | 2022-10-05 | 2023-01-02 | 1999-12-01 | 1999-12-01 | 30        | 5          | 12        | 12         |
      | Sega        | Sonic         | 2021-04-12 | 2022-04-05 | 2000-03-01 | 1999-12-01 | 3         | 0          | 26        | 14         |
      | Capcom      | Resident Evil | 2023-11-21 | 2022-03-28 | 2021-12-18 | 2021-12-18 | 16        | 29         | 25        | 4          |
      | Warner Bros | Mortal Kombat | 2023-12-01 | 2022-08-30 | 2021-12-19 | 2021-12-19 | 18        | 12         | 44        | 14         |

  Scenario Outline: Unsuccessfully when creating a game with size of name field bigger than 40, then return the status
  code 400 BAD REQUEST and a name field exception message.
    * replace
      | token       | value       |
      | publisherId | publisherId |
      | name        | name        |

    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "Exceded number of charaters"
    Examples:
      | publisherId | name                                                     |
      | Nintendo    | Mario Kart Company 1111111111111111111111                |
      | Sega        | Sonic 222222222222222222222222222222222222222222222      |
      | Capcom      | Resident Evil 33333333333333333333333333333              |
      | Warner Bros | Mortal Kombat 444444444444444444444444444444444444444444 |