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
    //Integer alpha, beta;
    //checkerGame game;
    
    int []fr = {1, 1, -1, -1, 2, 2, -2, -2};
    int []fc = {1, -1, -1, 1, 2, -2, -2, 2};
    
    public MinimaxCheckersAgent(String name, int depthLimit) {
        super(name);
        cutOffDepth = depthLimit;
    }

    @Override
    public void makeMove(Game game) {
        
        checkerGame cgame = (checkerGame)game;
        
        JButton []cell = new JButton[64];
        for(int i=0; i<64; i++) cell[i] = new JButton(cgame.board.cell[i].getText(), cgame.board.cell[i].getIcon());
        
        action A = MAX_VALUE(cgame, cell, Integer.MIN_VALUE,Integer.MAX_VALUE, 0);
        
        if(A.fromRow == -1){
            cgame.winner = cgame.agent[1-role];
            return;
        }
        
        /*********************** FOR SHOW *************************************/
        //show move in console
        System.err.println(""+A.fromRow+" "+A.fromCol);
        System.err.println(""+A.toRow+" "+A.toCol);
        
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
        
    }

    private action MAX_VALUE(checkerGame game, JButton[] cell, int alpha, int beta, int curDepth) {
        int candMoves = 0;
        action cur = new action(Integer.MIN_VALUE);
        
        //depth limit
        if(curDepth >= cutOffDepth){
            cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
            return cur;
        }
        
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(cell[row*8 + col].getIcon() == null || cell[row*8 + col].getIcon() != game.board.currentPlayer) continue;
                
                for(int i=0; i<8; i++){
                    int nrow = row + fr[i], ncol = col + fc[i];
                    if(nrow < 0 || nrow > 7 || ncol < 0 || ncol > 7) continue;
                    
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
                            cur.moveUtilVal = mn.moveUtilVal;
                            cur.fromRow = row;
                            cur.fromCol = col;
                            cur.toRow = nrow;
                            cur.toCol = ncol;
                        }
                        
                        if(cur.moveUtilVal >= beta) return cur;
                        alpha = Integer.max(cur.moveUtilVal, alpha);
                    }
                }
            }
        }
        
        if(candMoves == 0){
            //terminal state
            cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
        }
        
        return cur;
    }

    private action MIN_VALUE(checkerGame game, JButton[] cell, int alpha, int beta, int curDepth) {
        int candMoves = 0;
        action cur = new action(Integer.MAX_VALUE);
        
        //depth limit
        if(curDepth >= cutOffDepth){
            cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
            return cur;
        }
        
        ImageIcon currentPlayer = null;
        if(game.board.currentPlayer == game.board.red) currentPlayer = game.board.black;
        else currentPlayer = game.board.red;
        
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(cell[row*8 + col].getIcon() == null || cell[row*8 + col].getIcon() != currentPlayer) continue;
                
                for(int i=0; i<8; i++){
                    int nrow = row + fr[i], ncol = col + fc[i];
                    if(nrow < 0 || nrow > 7 || ncol < 0 || ncol > 7) continue;
                    
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
                            cur.fromRow = row;
                            cur.fromCol = col;
                            cur.toRow = nrow;
                            cur.toCol = ncol;
                        }
                        
                        if(cur.moveUtilVal <= alpha) return cur;
                        beta = Integer.min(cur.moveUtilVal, beta);
                    }
                }
            }
        }
        
        if(candMoves == 0){
            //terminal state
            cur.moveUtilVal = utility_for_MAX(game, cell, game.board.currentPlayer);
        }
        
        return cur;
    }
    
    
    private int utility_for_MAX(checkerGame game, JButton []cell, ImageIcon currentPlayer){
        
        ImageIcon opponentPlayer = null;
        if(currentPlayer == game.board.red) opponentPlayer = game.board.black;
        else opponentPlayer = game.board.black;
        
        int add = 0, sub = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(cell[i*8+j].getIcon() == currentPlayer){
                    if("K".equals(cell[i*8+j].getText())) add += 2; /// 2 points for kings
                    else add++; /// 1 point for normal checker
                    
                    for(int k=4; k<8; k++){
                        int ni = i+fr[i], nj = j+fc[j];
                        if(ni < 0 || ni > 7 || nj < 0 || nj > 7) continue;
                        if(game.board.validMove(cell, currentPlayer, ni, nj, i, j, false)){
                            add++; /// 1 additional point for attacker checker 
                        }
                    }
                    
                    if(i == 0 || j == 0 || i == 7 || j == 7) add++; /// 1 point for safe checkers
                }
                else if(cell[i*8+j].getIcon() != null){
                    if("K".equals(cell[i*8+j].getText())) sub += 2; /// -2 points for opponent kings
                    else sub++;  /// -1 point for normal opponent
                    
                    for(int k=4; k<8; k++){
                        int ni = i+fr[i], nj = j+fc[j];
                        if(ni < 0 || ni > 7 || nj < 0 || nj > 7) continue;
                        if(game.board.validMove(cell, opponentPlayer, ni, nj, i, j, false)){
                            sub++; /// -1 additional point for attacker opponent checker 
                        }
                    }
                    
                    if(i == 0 || j == 0 || i == 7 || j == 7) sub++; /// -1 point for safe opponent checkers
                }
                
            }
        }
        return add - 3*sub;
    }

    class action{
        int fromRow, fromCol, toRow, toCol, moveUtilVal;

        public action(int moveUtilVal) {
            fromRow = -1;
            fromCol = -1;
            toRow = -1;
            toCol = -1;
            this.moveUtilVal = moveUtilVal;
        }
    }

}
