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
import java.awt.Color;
import static java.awt.FlowLayout.RIGHT;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CheckerBoard extends JFrame implements ActionListener{
    
    JButton[] cell;
    ImageIcon red, black, currentPlayer;
    JPanel boardJPanel, statJPanel, allJPanel; 
    JLabel statusJLabel, currentPlayerJLabel, msgJLabel;
    
    boolean mouseClickEnable;
    int firstClickedRow, firstClickedCol, mouseClicked;

    public CheckerBoard() {

        mouseClickEnable = false;
        mouseClicked = 0;
        cell = new JButton[8 * 8];
        
        red = new ImageIcon("round_red.png");
        black = new ImageIcon("round_black.png");
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8;j++){
                if((i+j)%2 == 0){
                    cell[i*8 + j] = new JButton();
                    cell[i*8 + j].setBackground(Color.lightGray);
                }
                else{
                    cell[i*8 + j] = new JButton();
                    cell[i*8 + j].setBackground(Color.white);
                    cell[i*8+j].setForeground(Color.WHITE);
                   
                    cell[i*8+j].setHorizontalTextPosition(JButton.CENTER);
                    cell[i*8+j].setVerticalTextPosition(JButton.CENTER);
                    
                    if(i<3) cell[i*8 + j].setIcon(black);
                    else if(i>4) cell[i*8 + j].setIcon(red);
                                      
                    cell[i*8 + j].putClientProperty("row", i);
                    cell[i*8 + j].putClientProperty("column", j);
                    cell[i*8 + j].addActionListener((ActionListener) this);
                }
            }
        }
        
        
        boardJPanel = new JPanel(new GridLayout(8, 8));
        boardJPanel.setSize(750, 700);
        for (int i = 0; i < 64; i++) boardJPanel.add(cell[i]);
        
        //gameAreaJPanel = new JPanel
        
        JPanel statusJPanel = new JPanel(new GridLayout(10, 1));
        statusJPanel.setSize(300, 700);
        
        statusJLabel = new JLabel();
        
        JLabel player1 = new JLabel("Alice", red, RIGHT);
        JLabel player2 = new JLabel("Bob", black, RIGHT);
        
        currentPlayerJLabel = new JLabel();
        msgJLabel = new JLabel();
        
        statusJPanel.add(statusJLabel);
        statusJPanel.add(player1);
        statusJPanel.add(player2);
        statusJPanel.add(currentPlayerJLabel);
        statusJPanel.add(msgJLabel);
        
        allJPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        allJPanel.setSize(1050, 700);
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 8;
        c.gridheight = 8;
        
        c.weightx = 0.5;
        c.weighty = 0.5;
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.fill = GridBagConstraints.VERTICAL;
        
        allJPanel.add(boardJPanel, c);
        
        c.gridx = 8;
        c.gridy = 0;
        c.gridwidth = 8;
        c.gridheight = 8;
        
        c.weightx = 0.5;
        c.weighty = 0.5;
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.fill = GridBagConstraints.VERTICAL;
        
        allJPanel.add(statusJPanel, c);
        super.add(allJPanel);
        
        
        //System.out.println(cell[40].getIcon() == red);
    }
    
    public void move(int sr, int sc, int dr, int dc){
        cell[dr*8+dc].setIcon(cell[sr*8+sc].getIcon());
        cell[dr*8+dc].setText(cell[sr*8+sc].getText());
        cell[sr*8+sc].setIcon(null);
        
        if(currentPlayer == red && dr == 0) cell[dr*8+dc].setText("K");
        if(currentPlayer == black && dr == 7) cell[dr*8+dc].setText("K");
    }
    
    @Override
    public void actionPerformed(ActionEvent ae){
        
        if(mouseClickEnable == false) return;
        
        JButton button = (JButton) ae.getSource();
        
        Integer row = (Integer) button.getClientProperty("row");
        Integer col = (Integer) button.getClientProperty("column");
        
        if(mouseClicked == 0){
            if(currentPlayer != button.getIcon()){
                msgJLabel.setText("Invalid checker");
                return;
            }
            else{
                msgJLabel.setText("");
                mouseClicked = 1;
                firstClickedRow = row;
                firstClickedCol = col;
            }
            return;
        }
        
        if(mouseClicked == 1){
            //check if valid move
            if(!validMove(row, col)){
                msgJLabel.setText("Invalid Move");
                mouseClicked = 0;
            }
            else{
                mouseClicked = 2;
                move(firstClickedRow, firstClickedCol, row, col);
                msgJLabel.setText("");
            }
            
        }
    }
    
    boolean validMove(int row, int col){
        
        //check if destination position is empty
        if(cell[row*8+col].getIcon() != null) return false;
        
        //check if row/col move > 2
        int drow = Math.abs(firstClickedRow - row), dcol = Math.abs(firstClickedCol - col);
        if(drow != dcol || drow > 2 || dcol > 2) return false;
        
        //check if king movement done by normal checker
        if(!"K".equals(cell[firstClickedRow*8+firstClickedCol].getText()) && (currentPlayer == red && row > firstClickedRow || 
                currentPlayer == black && row < firstClickedRow)) return false;
        
        //if 2, then check if an opponent checker is in between, if yes capture that checker 
        if(drow == 2){
            int midRow = (firstClickedRow+row)/2, midCol = (firstClickedCol+col)/2;
            ImageIcon midChecker = (ImageIcon) cell[midRow*8+midCol].getIcon();
            if(midChecker == null || midChecker == currentPlayer) return false;
            else{
                cell[midRow*8+midCol].setIcon(null);
                return true;
            }
        }
        
        return true;
    }

}