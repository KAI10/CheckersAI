/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

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
        
        Agent Alice = new HumanCheckersAgent("Alice");
        Agent Bob = new MinimaxCheckersAgent("Bob");
        
        Game game = new checkerGame(Alice, Bob);
        //game.play();
    }

}
