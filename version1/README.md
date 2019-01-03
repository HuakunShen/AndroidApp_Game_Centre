# README

## Setup

1. Clone the project, with git bash.

2. Open Android Studio, open existing project, select GameCenter project from Phase 1 folder.

3. Build and run the app

## Meeting Note

* notes are recorded in a folder called note in the group folder.

   ## SaveFiles

* login.ser : store login info

* login_tem.ser : store temporary login info

* user_info.ser : store user info

* user_info_temp.ser : store temporary user info

* score_save_file.ser : store highest score for each game and each user

* save_file.ser : save file of SlidingTiles

* save_file_tmp.ser : temporary save file of SlidingTiles

Not implemented yet:

* save_nick_names.ser : store user nick names

* save_avatars.ser : store user avatars(icons)



   ## Features

* Sign in or sign up

  - Use username: admin and password: admin to sign in (admin, admin is an built-in account)
  - Or click sign up, and set username and password (repeat password must match)

* Basic Menu: On the user interface immediately after logging in, user can slide from the left most
        ​    edge to the right side to open menu. (the left most edge is important)

* Change Password: There is an option in setting for the user to change password.

* Score Board: There is an option in setting for the user to check out the highest score for each game.

* Games: user can choose a game to be played, we only have one game available for now, which is SlidingTiles.

Features in SlidingTiles:

* Autosave: autosave game state every 5 sec.

* Undo: user can undo previous 3 steps by default, but they can change that before the game starts,
    the max undo can be set is 20.

* Game Complexity: user can choose three different modes of the game. (including 3x3, 4x4, 5x5)

* Load Saved Game: user can load their latest state of the game and continue playing from where they left.

* Save Game: user can save the game just played if the app was not shut down.

* Import Image: user can import image and use it as a background in SlidingTiles game.
  - If using a simulator with no image stored, user can drag a image into the simulator,
    and this image will be found in the Downloads folder.

* User can select that if he wants to play game with imported image as background or with numbers as background.



   ## Coming in Phase 2

* Can set nickname when register.

* Can change nickname and avatar(icon).

* Complete Interfaces AutoSave and Undo.