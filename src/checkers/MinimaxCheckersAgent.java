/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author ashik
 */
public class MinimaxCheckersAgent extends Agent{

    int cutOffDepth;
    final int MAX_WIN = 10000, MAX_LOSE = -10000;
    
    int []fr = {1, 1, -1, -1, 2, 2, -2, -2};
    int []fc = {1, -1, -1, 1, 2, -2, -2, 2};
    
    public MinimaxCheckersAgent(String name, int depthLimit) {
        super(name);
        cutOffDepth = depthLimit;
    }

    @Override
    public boolean makeMove(Game game) {
        
        checkerGame cgame = (checkerGame)game;
        
        JButton []cell = new JButton[64];
        for(int i=0; i<64; i++) cell[i] = new JButton(cgame.board.cell[i].getText(), cgame.board.cell[i].getIcon());
        
        action A = MAX_VALUE(cgame, cell, Integer.MIN_VALUE,Integer.MAX_VALUE, 0);
        
        System.out.println(A.toString());
        
        if(A.cantMove == true){
            cgame.winner = cgame.agent[1-role];
            return false;
        }
        
        /*********************** FOR SHOW *************************************/
        cgame.board.cell[A.fromRow*8+A.fromCol].setBackground(Color.pink);
        cgame.board.cell[A.toRow*8+A.toCol].setBackground(Color.cyan);
        /**********************************************************************/
        
        /*********************** FOR SHOW *************************************/
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(MinimaxCheckersAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        /**********************************************************************/
        
        //make move in gui
        cgame.board.move(cgame.board.cell, A.fromRow, A.fromCol, A.toRow, A.toCol);
        
        /*********************** FOR SHOW *************************************/
        try {
            Thread.sleep(700);
        } catch (InterruptedException ex) {
            Logger.getLogger(MinimaxCheckersAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cgame.board.cell[A.fromRow*8+A.fromCol].setBackground(Color.white);
        cgame.board.cell[A.toRow*8+A.toCol].setBackground(Color.white);
        /**********************************************************************/
        
        return Math.abs(A.fromRow - A.toRow) == 2;
    }

    private action MAX_VALUE(checkerGame game, JButton[] cell, int alpha, int beta, int curDepth) {
        int candMoves = 0;
        action cur = new action(Integer.MIN_VALUE);
        
        //depth limit
        if(curDepth >= cutOffDepth){
            cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
            return cur;
        }
        
        /*
        //terminal test win_for_max, i.e max wins (opponent has no moves) in current state
        boolean max_already_won = false;
        
        if(moveAbleCheckers(game, cell, opponentPlayer) == 0){
            System.err.println("MAX MOVE: MAX WIN");
            cur.moveUtilVal = MAX_WIN;
            if(curDepth > 0)return cur; //beacause though win, need to give a move
            else max_already_won = true;
        }
        */

        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(cell[row*8 + col].getIcon() == null || cell[row*8 + col].getIcon() != game.board.currentPlayer) continue;
                
                for(int i=0; i<8; i++){
                    int nrow = row + fr[i], ncol = col + fc[i];
                    if(!validCell(nrow, ncol)) continue;
                    
                    if(game.board.validMove(cell, game.board.currentPlayer, nrow, ncol, row, col, false)){
                        candMoves++;
                        
                        //save necessary info for backtracking
                        int midRow = 0, midCol = 0;
                        ImageIcon saveIcon = null;
                        String saveText = null;
                        
                        if(i > 3){ //save the checker that is being captured
                            midRow = (row+nrow)/2;
                            midCol = (col+ncol)/2;
                            saveIcon = (ImageIcon) (cell[midRow*8+midCol].getIcon());
                            saveText = cell[midRow*8+midCol].getText();
                        }
                        
                        //make the move
                        boolean crowned = game.board.move(cell, row, col, nrow, ncol);
                        
                        action mn = MIN_VALUE(game, cell, alpha, beta, curDepth +1);
                        
                        //rollback to previous state
                        game.board.move(cell, nrow, ncol, row, col);
                        if(crowned) cell[row*8+col].setText(null);
                        if(i>3){
                            cell[midRow*8+midCol].setIcon(saveIcon);
                            cell[midRow*8+midCol].setText(saveText);
                        }
                        
                        
                        if(mn.moveUtilVal > cur.moveUtilVal){
                            //if(curDepth == 0) System.err.println("0: Setting cur");
                            //System.err.println("Setting cur");
                            cur.moveUtilVal = mn.moveUtilVal;
                            if(curDepth == 0){
                                cur.fromRow = row;
                                cur.fromCol = col;
                                cur.toRow = nrow;
                                cur.toCol = ncol;
                            }
                        }
                        
                        if(cur.moveUtilVal >= beta) return cur;
                        alpha = Integer.max(cur.moveUtilVal, alpha);
                    }
                }
            }
        }
        
        if(candMoves == 0){ // no move for MAX, max lose, handled in utility_for_MAX
            //terminal state, lose_for_max
            //cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
            if(curDepth == 0) cur.cantMove = true;
            //System.err.println("MAX MOVE: MAX: LOSE");
            cur.moveUtilVal = MAX_LOSE;
        }
        
        return cur;
    }

    private action MIN_VALUE(checkerGame game, JButton[] cell, int alpha, int beta, int curDepth) {
        int candMoves = 0;
        action cur = new action(Integer.MAX_VALUE);
        
        ImageIcon currentPlayer = null;
        if(game.board.currentPlayer == game.board.red) currentPlayer = game.board.black;
        else currentPlayer = game.board.red;
        
        //depth limit
        if(curDepth >= cutOffDepth){
            cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
            return cur;
        }
        
        //terminal test for MAX_LOSE/MIN_WIN
        if(moveAbleCheckers(game, cell, game.board.currentPlayer) == 0){
            cur.moveUtilVal = MAX_LOSE;
            //System.err.println("MIN MOVE: MAX LOSE");
            return cur;
        }
        
        
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(cell[row*8 + col].getIcon() == null || cell[row*8 + col].getIcon() != currentPlayer) continue;
                
                for(int i=0; i<8; i++){
                    int nrow = row + fr[i], ncol = col + fc[i];
                    if(!validCell(nrow, ncol)) continue;
                    
                    if(game.board.validMove(cell, currentPlayer, nrow, ncol, row, col, false)){
                        candMoves++;
                        
                        //save necessary info for backtracking
                        int midRow = 0, midCol = 0;
                        ImageIcon saveIcon = null;
                        String saveText = null;
                        
                        if(i > 3){ //save the checker that is being captured
                            midRow = (row+nrow)/2;
                            midCol = (col+ncol)/2;
                            saveIcon = (ImageIcon) (cell[midRow*8+midCol].getIcon());
                            saveText = cell[midRow*8+midCol].getText();
                        }
                        
                        //make the move
                        boolean crowned = game.board.move(cell, row, col, nrow, ncol);
                        
                        action mx = MAX_VALUE(game, cell, alpha, beta, curDepth + 1);
                        
                        //rollback to previous state
                        game.board.move(cell, nrow, ncol, row, col);
                        if(crowned) cell[row*8+col].setText(null);
                        if(i>3){
                            cell[midRow*8+midCol].setIcon(saveIcon);
                            cell[midRow*8+midCol].setText(saveText);
                        }
                        
                        if(mx.moveUtilVal < cur.moveUtilVal){
                            cur.moveUtilVal = mx.moveUtilVal;
                        }
                        
                        if(cur.moveUtilVal <= alpha) return cur;
                        beta = Integer.min(cur.moveUtilVal, beta);
                    }
                }
            }
        }
        
        if(candMoves == 0){ // no move available for opponent, so MAX_WIN (MIN_LOSE), handled in utility_for_MAX
            //terminal state 
            //cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
            cur.moveUtilVal = MAX_WIN;
            //System.err.println("MIN MOVE: MAX WIN");
        }
        
        return cur;
    }
    
    
    private int utility_for_MAX(checkerGame game, JButton []cell, ImageIcon currentPlayer){
        
        ImageIcon opponentPlayer = null;
        if(currentPlayer == game.board.red) opponentPlayer = game.board.black;
        else opponentPlayer = game.board.black;
        
        int myPostionVal = 0, opPositionVal = 0;
        int myPawns = 0, opPawns = 0;
        int myKings = 0, opKings = 0;
        int myAttackers = 0, opAttackers = 0;
        int myCantMove = 0, opCantMove = 0;
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8;j++){
                //player MAX
                if(cell[i*8+j].getIcon() == currentPlayer){
                    myPostionVal += cellValue(i, j);
                    myPawns++;
                    if("K".equals(cell[i*8+j].getText())) myKings++;
                    
                    boolean CantMove = true;
                    for(int k=0; k<8; k++){
                        int ni = i+fr[i], nj = j+fr[j];
                        if(!validCell(ni, nj)) continue;
                        if(game.board.validMove(cell, currentPlayer, ni, nj, i, j, false)){
                            CantMove = false;
                            if(k>3) myAttackers++;
                        }
                    }
                    if(CantMove) myCantMove++;
                }
                
                //opponent player
                else if(cell[i*8+j].getIcon() != null){
                    opPositionVal += cellValue(i, j);
                    opPawns++;
                    if("K".equals(cell[i*8+j].getText())) opKings++;
                    
                    boolean CantMove = true;
                    for(int k=0; k<8; k++){
                        int ni = i+fr[i], nj = j+fr[j];
                        if(!validCell(ni, nj)) continue;
                        if(game.board.validMove(cell, opponentPlayer, ni, nj, i, j, false)){
                            CantMove = false;
                            if(k>3) opAttackers++;
                        }
                    }
                    if(CantMove) opCantMove++;
                }
            }
        }
        
        //System.out.printf("%d %d %d\n", opPawns, opKings, opCantMove);
        if(opPawns + opKings - opCantMove == 0) return MAX_WIN; // opponent has no move
        if(myPawns + myKings - myCantMove == 0) return  MAX_LOSE; // MAX has no move
        
        int myCheckers = myPawns + myKings, opCheckers = opPawns + opKings; 
        
        //System.err.println("HERE");
        int heuristicVal =  (myPawns + 2*myKings + myPostionVal)
             - (myCantMove)
             - (opPawns + 2*opKings + opAttackers + opPositionVal)
             + (opCantMove);
        
        //if myCheckers is considerably greater than opponentCheckers, increase attacking tendency (propotional to checker count difference)
        if( myCheckers - opCheckers > 4) return heuristicVal + (myCheckers - opCheckers) * myAttackers;
        return heuristicVal;
    }
    
    
    int moveAbleCheckers(checkerGame game, JButton[] cell, ImageIcon Player){
        int count = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(cell[i*8+j].getIcon() != Player) continue;
                for(int k=0; k<8; k++){
                    int ni = i+fr[k], nj = j+fr[k];
                    if(ni <0 || ni > 7 || nj < 0 || nj > 7) continue;
                    if(game.board.validMove(cell, Player, ni, nj, i, j, false)) count++;
                }
            }
        }
        return count;
    }
    
    int cellValue(int row, int col){
        if(row == 0 || row == 7 || col == 0 || col == 7) return 4;
        if(row == 1 || row == 6 || col == 1 || col == 6) return 3;
        if(row == 2 || row == 5 || col == 2 || col == 5) return 2;
        return 1;
    }
    
    boolean validCell(int row, int col){
        return !(row < 0 || row > 7 || col < 0 || col > 7);
    }

    class action{
        int fromRow, fromCol, toRow, toCol, moveUtilVal;
        boolean cantMove;
        public action(int moveUtilVal) {
            fromRow = -1;
            fromCol = -1;
            toRow = -1;
            toCol = -1;
            cantMove = false;
            this.moveUtilVal = moveUtilVal;
        }

        @Override
        public String toString() {
            return "action{" + "fromRow=" + fromRow + ", fromCol=" + fromCol + ", toRow=" + toRow + 
                    ", toCol=" + toCol + ", moveUtilVal=" + moveUtilVal + ", cantMove=" + cantMove + '}';
        }

        
    }

}
