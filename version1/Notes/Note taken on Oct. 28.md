Note  taken on Oct. 28

Discussion:

* final decision on how to storing data:

  * store data to local by file (as done in A2)

* find problem while writing undo:

  * we are doing undo by storing the game states to a stack, and poping the top state for each undo operation. But there is a bug in storing the dates to file. Thus, we decide to process undo by storing each new position of the tile after each move. Then, we can undo the game by making a reverse move.
