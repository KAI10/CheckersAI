/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ashik
 */
public class checkerGame extends Game{

    CheckerBoard board;
    int moveLimitToDraw, remainingMovesToDraw;
    
    public checkerGame(Agent a, Agent b) throws InterruptedException {
        super(a, b);
        
        agent[0].setRole(0);
        agent[1].setRole(1);
        
        name = " Checkers game\n";
        
        board = new CheckerBoard();
        board.setTitle("Checkers");
        
        board.player1.setText(a.name);
        board.player2.setText(b.name);
        
        board.setSize(1050, 700);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setVisible(true);
    
        moveLimitToDraw = 50;
        play();
    }

    private void play() throws InterruptedException {
        showStaus(name + " between " + agent[0].name + " and " + agent[1].name + ".");
        int turn = random.nextInt(2);

        board.starter.setText(agent[turn].name+ " made the first move.");
        
        //initialize(false);
        remainingMovesToDraw = moveLimitToDraw;
       
        while (!isFinished()) {
            updateMessage(agent[turn].name + "'s turn. ");
            System.out.println(agent[turn].name + "'s turn. ");
            
            if(turn == 0) board.currentPlayer = board.red;
            else board.currentPlayer = board.black;
            
            boolean jumped = agent[turn].makeMove(this);
            //showGameState();
            
            remainingMovesToDraw = (!jumped)? remainingMovesToDraw - 1: moveLimitToDraw;
            showMsg("Remaining moves to Draw: " + remainingMovesToDraw);
            
            turn = (turn + 1) % 2;
            
        }

       
        if (winner != null){
            updateMessage(winner.name + " wins!!!");
            JOptionPane.showMessageDialog(board, winner.name + " wins!!!");
        }
        else{
            updateMessage("Game drawn!!");
            JOptionPane.showMessageDialog(board, "Match drawn!!!");
        }
        
        Thread.sleep(500);
        board.setVisible(false);
        board.setEnabled(false);
    }
    
    @Override
    boolean isFinished() {
        if(winner != null || remainingMovesToDraw == 0) return true;
        
        int redCount = 0, blackCount = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board.cell[i*8+j].getIcon() == board.red) redCount++;
                else if(board.cell[i*8+j].getIcon() == board.black) blackCount++;
            }
        }
        
        if(redCount == 0){
            winner = agent[1];
            return true;
        }
        if(blackCount == 0){
            winner = agent[0];
            return true;
        }
       
        return false;
    }

    @Override
    void initialize(boolean fromFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void showGameState(JButton []cell) {
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(cell[i*8 + j].getIcon() == board.currentPlayer) System.out.print("B ");
                else if(cell[i*8+j].getIcon()  == null) System.out.print("0 ");
                else System.out.print("H ");
            }
            System.out.println();
        }
        
        System.out.println();
    }

    @Override
    void updateMessage(String msg) {
        board.currentPlayerJLabel.setText(msg);
    }
    
    void showStaus(String msg) {
        board.statusJLabel.setText(msg);
    }
    
    void showMsg(String msg){
        board.remainingMovesToDraw.setText(msg);
    }

    @Override
    void showGameState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void exit() {
        board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
    }
}
