# CheckersAI
This is a simple checkers game playing AI agent that follows the Minimax algorithm with alpha-beta pruning and depth limited search.
Welcome to the CheckersAI wiki!

# How to Play
Just run the _Checkers.jar_ file in _dist_ and then follow the on screen instructions.

# Rules of the Game
* The game is played on an 8x8 size board, initially each player has 12 checker pieces.
* ![Initial Setup](http://m.itsyourturn.com/help/kxsetup.gif)
* Basic movement is to move a checker one space diagonally forward. You can not move a checker backwards until it becomes a King.
* ![Basic Movement](http://m.itsyourturn.com/help/kxbasicmove.gif)
* When one of your checkers reaches the opposite side of the board, it is crowned and becomes a King. This is called crowning. A King can move backward as well as forward along the diagonals. It can only move a distance of one space.
* ![Crowning](http://m.itsyourturn.com/help/kxcrown.gif)
* If one of your opponent’s checkers is on a forward diagonal next to one of your checkers, and the next space beyond the opponent’s checker is empty, then your checker can jump the opponent’s checker and land in the space beyond. Your opponent’s checker is captured and removed from the board.
* ![jump_start](http://m.itsyourturn.com/help/kxjump1.gif)
![jump_end](http://m.itsyourturn.com/help/kxjump2.gif)
* Multiple jump with the same piece in a single move is not allowed.
* It is not mandatory to make a jump when it is available.
* If in 50 consecutive moves no piece on the board is captured, the match is declared a draw.
* If at any time a player has no moves, i.e. all the checkers of the player are captured/trapped(cannot move), then that player loses.

