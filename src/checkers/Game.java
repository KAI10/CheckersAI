/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.Random;

/**
 * Extend this abstract class for your game implementation
 *
 * @author Azad
 *
 */
public abstract class Game {

    Agent agent[]; // Array containing all the agents, here we only consider two player games.
    String name = "A Generic Game"; //A name for the Game object, it will be changed by the actual game class extending it

    Random random = new Random();
    Agent winner = null; // The winning agent will be saved here after the game compeltes, if null the game is drawn.

    public Game(Agent a, Agent b) {
        // TODO Auto-generated constructor stub
        agent = new Agent[2];
        agent[0] = a;
        agent[1] = b;

    }

    /**
     * The actual game loop, each player takes turn. The first player is
     * selected randomly
     * @return 
     */
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String str = "";
        return str;
    }

    /**
     *
     * @return Returns true if the game has finished. Also must update the
     * winner member variable.
     */
    abstract boolean isFinished();

    /**
     * Initializes the game as needed. If the game starts with different initial
     * configurations, it can be read from file.
     *
     * @param fromFile if true loads the initial state from file.
     */
    abstract void initialize(boolean fromFile);

    /**
     * Prints the game state in console, or show it in the GUI
     */
    abstract void showGameState();

    /**
     * Shows game messages in console, or in the GUI
     */
    abstract void updateMessage(String msg);
    
    abstract void exit();
}
