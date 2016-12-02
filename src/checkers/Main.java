/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javax.swing.JOptionPane;

/**
 *
 * @author ashik
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String[] buttons = { "Human vs Computer", "Computer vs Computer" };
        
        int mode = -1;
        mode = JOptionPane.showOptionDialog(null, "Mode ?", "Checkers", JOptionPane.YES_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[1]);
        
        System.out.println(mode);
        
        Agent Bob = new MinimaxCheckersAgent("Bob");
        Agent Alice;

        if(mode == 0) Alice = new HumanCheckersAgent("Alice");
        else Alice = new MinimaxCheckersAgent("Alice");
        
        Game game = new checkerGame(Alice, Bob);
        //game.play();
    }

}
