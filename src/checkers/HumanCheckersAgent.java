/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ashik
 */
public class HumanCheckersAgent extends Agent{

    int []fr = {1, 1, -1, -1, 2, 2, -2, -2};
    int []fc = {1, -1, -1, 1, 2, -2, -2, 2};
    
    public HumanCheckersAgent(String name) {
        super(name);
    }

    @Override
    public boolean makeMove(Game cgame) {
        //System.out.println(name + "'s move.");
        
        checkerGame game = (checkerGame)cgame;
        int candMoves = 0;
        
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(game.board.cell[row*8 + col].getIcon() == null || 
                   game.board.cell[row*8 + col].getIcon() != game.board.currentPlayer) continue;
                
                for(int i=0; i<8; i++){
                    int nrow = row + fr[i], ncol = col + fc[i];
                    if(nrow < 0 || nrow > 7 || ncol < 0 || ncol > 7) continue;
                    
                    if(game.board.validMove(game.board.cell, game.board.currentPlayer, nrow, ncol, row, col, true)){
                        candMoves++;
                        
                        game.board.cell[row*8+col].setBackground(java.awt.Color.orange);
                        game.board.cell[nrow*8+ncol].setBackground(java.awt.Color.cyan);
                    }
                }
            }
        }
        
        if(candMoves == 0){
            game.winner = game.agent[1-role];
            return false;
        }
        
        
        game.board.mouseClickEnable = true;
        
        while(game.board.mouseClicked != 2) {
            try {
                //System.out.println(cgame.board.mouseClicked);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(HumanCheckersAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        game.board.mouseClicked = 0;
        game.board.mouseClickEnable = false;
        
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if((row+col)%2 == 1) game.board.cell[row*8+col].setBackground(java.awt.Color.white);
            }    
        }
        
        //System.out.println(name + "'s move complete.");
        return game.board.jumpByHumanAgent;
    }
    
}
