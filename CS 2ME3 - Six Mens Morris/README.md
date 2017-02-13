# Six Men's Morris

The requirement of this project was to implement a modified version of "Nine Men's Morris" called "Six Men's Morris".  The main difference is the board is smaller than the original.

The rules of the game are simple:
1.	Each actor is assigned a coloured disc (red or blue).
2.	Each actor takes turns placing discs on the board (up to 6 discs placed per player, must be placed on an empty slot).
	Note: The areas where a player may place a disc are marked by small black dots on the board.
3.	If 3 discs of the same colour are in a "mill" (3-in-a-row), the actor of the corresponding colour gets to remove 1 disc of the opponent's discs currently on the board.
4.	Once both actors have placed their 6 coloured discs on the board, the actors take turns moving their currently placed discs to adjacent locations on the board.
	Note: There are lines on the board connecting dots to show pathways.
5.	A winner is determined when the opposing actor is either unable to move any of their pieces or if they do not have enough discs to create a mill (ie, less than 3 discs).

# Requirements
The library "GSON" by Google (https://github.com/google/gson) is required to compile the source.  This library is solely used for serializing game state into JSON format.