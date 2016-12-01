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

    public HumanCheckersAgent(String name) {
        super(name);
    }

    @Override
    public void makeMove(Game game) {
        System.out.println(name + "'s move.");
        checkerGame cgame = (checkerGame) game;
        cgame.board.mouseClickEnable = true;
        
        while(cgame.board.mouseClicked != 2) {
            try {
                //System.out.println(cgame.board.mouseClicked);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(HumanCheckersAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        cgame.board.mouseClicked = 0;
        cgame.board.mouseClickEnable = false;
        
        System.out.println(name + "'s move complete.");
    }
    
}
