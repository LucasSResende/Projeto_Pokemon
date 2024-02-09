Feature: The POST operation is responsible for the user to create a game that has the following items:
  publisherId: Mandatory and of type String.
  name: Mandatory and of the String type, limited to 40 characters.
  timePlayed: optional and of the Map type, with the information -> date:"YYYY-MM-DD" (of the LocalDate type);
  hours (of the Integer type). As soon as the game is created, it must be returned with the status 201 CREATED and
  shows a payload containing the information created plus the id that is automatically generated.

  Scenario Outline: Successfully when creating a game with a valid name and 'publisherId', then return the status code 201 CREATED
  and the response body with fields: id, name and publisherId.
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
      | publisherId | name           |
      | Nintendo    | Mario          |
      | Sega        | Sonic          |
      | Capcom      | Residente Evil |
      | Warner Bros | Mortal Kombat  |

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
      | publisherId | name           | firstDate  | secondDate | thirdDate  | firstTime | secondTime | thirdTime | fourthTime |
      | Nintendo    | Mario          | 2022-10-05 | 2023-01-02 | 1999-12-01 | 12        | 5          | 12        | 12         |
      | Sega        | Sonic          | 2021-04-12 | 2022-04-05 | 2000-03-01 | 3         | 6          | 14        | 14         |
      | Capcom      | Residente Evil | 2023-12-01 | 2021-10-06 | 2020-11-11 | 5         | 1          | 24        | 24         |
      | Warner Bros | Mortal Kombat  | 2023-12-01 | 2021-10-06 | 2020-11-11 | 5         | 1          | 24        | 24         |

  Scenario Outline: Unsuccessfully when creating a game without a valid 'publisherId', then return the status code 400 BAD
  REQUEST and returned a publisher field exception message.
    * replace
      | token      | value      |
      | name       | name       |
      | firstDate  | firstDate  |
      | secondDate | secondDate |
      | thirdDate  | thirdDate  |
      | firstTime  | firstTime  |
      | secondTime | secondTime |
      | thirdTime  | thirdTime  |
      | fourthTime | fourthTime |

    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "The publisherId can't be empty"

    Examples:
      | name           | firstDate  | secondDate | thirdDate  | thirdDate  | firstTime | secondTime | thirdTime | fourthTime |
      | Mario          | 2022-10-05 | 2023-01-02 | 1999-12-01 | 1999-12-01 | 12        | 5          | 12        | 12         |
      | Sonic          | 2021-04-12 | 2022-04-05 | 2000-03-01 | 2000-03-01 | 3         | 6          | 14        | 14         |
      | Residente Evil | 2023-12-01 | 2021-10-06 | 2020-11-11 | 999-12-01  | 5         | 1          | 24        | 12         |
      | Mortal Kombat  | 2023-12-01 | 2021-10-06 | 2020-11-11 | 000-03-01  | 5         | 1          | 24        | 14         |

  Scenario Outline: Unsuccessfully when creating a game without a valid 'name', then return the status code 400 BAD REQUEST
  and returned a name field exception message.
    * replace
      | token       | value       |
      | publisherId | publisherId |
      | firstDate   | firstDate   |
      | secondDate  | secondDate  |
      | thirdDate   | thirdDate   |
      | firstTime   | firstTime   |
      | secondTime  | secondTime  |
      | thirdTime   | thirdTime   |
      | fourthTime  | fourthTime  |

    When the method operation is POST
    Then status code returned is 400
    And match exceptionMessage contains "The name can't be empty"

    Examples:
      | publisherId | firstDate  | secondDate | thirdDate  | firstTime | secondTime | thirdTime | fourthTime |
      | Nintendo    | 2022-10-05 | 2023-01-02 | 1999-12-01 | 12        | 5          | 12        | 12         |
      | Sega        | 2021-04-12 | 2022-04-05 | 2000-03-01 | 3         | 6          | 14        | 14         |
      | microsoft   | 2023-12-01 | 2021-10-06 | 2020-11-11 | 5         | 1          | 24        | 24         |

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
      | publisherId | name     | firstDate  | secondDate | thirdDate  | firstTime | secondTime | thirdTime | fourthTime |
      | Nintendo    | Mario    | 2022-10-05 | 2023-01-02 | 1999-12-01 | 30        | 5          | 12        | 12         |
      | Sega        | Sonic    | 2021-04-12 | 2022-04-05 | 2000-03-01 | 3         | 0          | 26        | 14         |
      | microsoft   | DarkSide | 2023-12-01 | 2021-10-06 | 2020-11-11 | 5         | 28         | 24        | 24         |

  Scenario Outline: Unsuccessfully when creating a game with a number of characters bigger than 40, then return
  the status code 400 BAD REQUEST and a name field exception message.
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
    And match exceptionMessage contains "Exceded number of charaters"
    Examples:
      | publisherId | name                                        | firstDate  | secondDate | thirdDate  | firstTime | secondTime | thirdTime | fourthTime |
      | Nintendo    | Mario Kart Company 111111111111111111111111 | 2022-10-05 | 2023-01-02 | 1999-12-01 | 18        | 5          | 12        | 12         |
      | Sega        | Sonic 2222222222222222222222222222222222222 | 2021-04-12 | 2022-04-05 | 2000-03-01 | 3         | 0          | 15        | 14         |
      | microsoft   | DarkSide 3333333333333333333333333333333333 | 2023-12-01 | 2021-10-06 | 2020-11-11 | 5         | 20         | 24        | 24         |