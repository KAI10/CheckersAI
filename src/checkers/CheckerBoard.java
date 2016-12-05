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
import java.awt.Font;
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
    JLabel statusJLabel, currentPlayerJLabel, msgJLabel, player1, player2, starter, remainingMovesToDraw;
    
    boolean mouseClickEnable;
    int firstClickedRow, firstClickedCol, mouseClicked;
    
    boolean jumpByHumanAgent;

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
                    cell[i*8 + j].setEnabled(false);
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
        
        String defaultLabelFont = statusJLabel.getFont().getFontName();
        statusJLabel.setFont(new Font(defaultLabelFont, Font.PLAIN, 13));
        
        starter = new JLabel("");
        player1 = new JLabel("", red, RIGHT);
        player2 = new JLabel("", black, RIGHT);
        
        remainingMovesToDraw = new JLabel();
        currentPlayerJLabel = new JLabel();
        msgJLabel = new JLabel();
        
        statusJPanel.add(statusJLabel);
        statusJPanel.add(starter);
        statusJPanel.add(player1);
        statusJPanel.add(player2);
        statusJPanel.add(remainingMovesToDraw);
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
    
    public void boardReset(){
        
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
            if(!validMove(cell, currentPlayer, row, col, firstClickedRow, firstClickedCol, true)){
                msgJLabel.setText("Invalid Move");
                mouseClicked = 0;
            }
            else{
                mouseClicked = 2;
                move(cell, firstClickedRow, firstClickedCol, row, col);
                msgJLabel.setText("");
                
                jumpByHumanAgent = Math.abs(firstClickedRow - row) == 2;
            }
            
        }
    }
    
    boolean validMove(JButton[]cell, ImageIcon currentPlayer, int row, int col, int fromRow, int fromCol, boolean inGUI){
        
        //check if destination position is empty
        if(cell[row*8+col].getIcon() != null) return false;
        
        //check if row/col move > 2
        int drow = Math.abs(fromRow - row), dcol = Math.abs(fromCol - col);
        if(drow != dcol || drow > 2 || dcol > 2) return false;
        
        //check if king movement done by normal checker
        /*
        if(inGUI && cell[fromRow*8+fromCol].getText().equals("K")){
            System.err.println(""+fromRow+" "+fromCol);
            System.err.println("K");
        }
        */
        
        if(!"K".equals(cell[fromRow*8+fromCol].getText()) && ((currentPlayer == red && row > fromRow) || 
                (currentPlayer == black && row < fromRow))) return false;
        
        //if 2, then check if an opponent checker is in between, if yes capture that checker 
        if(drow == 2){
            int midRow = (fromRow+row)/2, midCol = (fromCol+col)/2;
            ImageIcon midChecker = (ImageIcon) cell[midRow*8+midCol].getIcon();
            if(midChecker == null || midChecker == currentPlayer) return false;
            else return true;
        }
        return true;
    }
    
    public boolean move(JButton[]cell, int sr, int sc, int dr, int dc){
        cell[dr*8+dc].setIcon(cell[sr*8+sc].getIcon());
        cell[dr*8+dc].setText(cell[sr*8+sc].getText());
        cell[sr*8+sc].setIcon(null);
        cell[sr*8+sc].setText(null);
        
        if(Math.abs(sr-dr) == 2){
            int midRow = (sr+dr)/2, midCol = (sc+dc)/2;
            cell[midRow*8+midCol].setIcon(null);
            cell[midRow*8+midCol].setText(null);
        }
        
        boolean crowned = false;
        if(currentPlayer == red && dr == 0 && !"K".equals(cell[dr*8+dc].getText())){
            cell[dr*8+dc].setText("K");
            crowned = true;
        }
        if(currentPlayer == black && dr == 7 && !"K".equals(cell[dr*8+dc].getText())){
            cell[dr*8+dc].setText("K");
            crowned = true;
        }
        
        return crowned;
    }

}