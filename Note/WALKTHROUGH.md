# WALKTHROUGH

## Unit Test Coverage

## Important Classes

## Design Patterns

## Scoreboard

### Design

* There are two types of  scoreboard, one is created by user, the other is created by game
* Scoreboard by user contains only data of current user and all games he/she has played

For example: Scoreboard for User ```admin```

| Game          | Username | Highest Score |
| ------------- | -------- | ------------- |
| Sudoku        | admin    | 90            |
| Picture Match | admin    | 100           |

* Scoreboard by game contains only data of the specific game

For example: Scoreboard for game ```Sliding Tiles```

| Rank | Game          | User  | Score |
| ---- | ------------- | ----- | ----- |
| 1    | Sliding Tiles | admin | 100   |
| 2    | Sliding Tiles | Jason | 90    |

 

### Data

* Score data are stored in user object and SQLDatabase

* For Scoreboard by user, we access user object and get the list of data (Game Name, Username, Score)

  * Score data are stored in a hashmap, where ```Game Name``` is the key and ```Score```is the value.
  * The hashmap in user object only stores score data for the user (no other user's score)
  * Thus accessing the score data is very fast

* For Scoreboard by game, we access SQL Database to get a list of data (Rank, Game, User, Score) with a SQL query (data sorted by score in Descending Order )

  ### SQL Database Table: dataTable

  | username | game   | score | file                  |
  | -------- | ------ | ----- | --------------------- |
  | admin    | Sudoku | 100   | admin_sudoku_data.ser |

* In this way, we don't have to load every user object to access the data.

### Display

* Scoreboard is displayed using ```ScoreboardActivity``` on layout ```activity_score_board```.

* Title is first created, to regulate the number of columns the width of each column.

* Data are taken from user object or Database depends on the type of scoreboard as a ```List<List<String>>```.

* Each inner list represents a user, game and his highest score in this game.

  Each inner list is put into a ```TableRow```, and the ```TableRow```is added to ```TableLayout```



