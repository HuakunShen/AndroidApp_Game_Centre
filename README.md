# Android App - Game Center

## Demo

![1565244795084](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\login_page)

![1565244828232](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\game_list)

![1565244898211](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\sliding_tiles)

![1565244862033](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\sliding_tiles_5x5)

![1565244944956](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\sudoku)

![1565244926061](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\sudoku_normal)

![1565244969236](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\picture_match)

![1565245018689](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\picture_match_6x6)

## Database

* DATABASE is written in SQLite

## UNIT TEST coverage

* For all games, the unit tests covers the board, the board manager, and the logical functions in the activity.

* Two tests are prepared for each game package: one specifically tests the board-manager (and tests the board through testing its manager), the other specifically tests the logical function in the activity, with an emphasis on the connection between the board-manager (the controller) and the activity (the view)

## Starting activity

* Used to display the buttons and views before the start of the game.

* When clicking new game it will intent to game activity.

## Movement controller

* This class will receive the information that is pass from the gesture grid view.(position, manager)

* It will process the information by calling the method of the manager class, which will change the board.

## Gesture Grid view

* The board manager will be pass from game activity class.

* There is an init method and when you touch the screen, the init method will be called automatically

## Game activity

* Used for displaying the gesture grid view

* When it observe the changes in board or boardmanager class, it will update the button and display them.

## Design patterns

* Model-View-Controller PATTERN:

  * The Boards are Models

  * The Boards’ managers are Controllers

  * The Game Activities are Views

  * They collaborates and makes our game design clear and easy to understand.

* Observer pattern:

  * Game activities are the observers
  * Either the board or its manager are being observed depending on the type of game.
  * Implementation of this design pattern assures that the users could see the most updated board.

* Iterator pattern

  * Iterator pattern is used to loop through the cells/tiles stored inside the board

## Scoreboard Design

* There are two types of  scoreboard:
  * one is generated based on score data of a specific user, 
  * the other is generated based on the type of the game

* Scoreboard by user

  * Scoreboard by user contains only data of the current user for all games he/she has played
  * Score Data are stored in a hashmap in the user object
  * gameName→Score
  * ![1565243956808](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\ScoreboardByUser)

* Scoreboard by game

  * Scoreboard by game contains only data of the specific game
  * ![1565244018338](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\ScoreboardByGame)
  * ![1565244052728](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\ScoreboardByGame2)

* Scoreboard display

  * Scoreboard is displayed using **ScoreboardActivity** on layout **activity_score_board**.

  * Title is first created, to regulate the number of columns the width of each column.

  * Data are taken from user object or Database depends on the type of scoreboard as a  **List<List<String>>**.

  * Each inner list represents a user, game and his highest score in this game.

  * Each inner list is put into a **TableRow**, and the **TableRow** is added to **TableLayout**
  * Scoreboard by User:
  * ![1565244113025](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\scoreboard_display)
  * Scoreboard By Game

![1565244131145](F:\Documents\CS\AndroidApp_Game_Centre\readme_img\scoreboard_display2)

[PPT Link](./README.pptx)

