/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.awt.event.WindowEvent;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author ashik
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        String[] options = {"Human vs Computer", "Computer vs Computer"};
        String[] response = {"OK"};
        String title = "Checkers";

        while (true) {
            
            String[] depths = {"Select Depth Limit of AI Agent", "1", "2", "3", "4", "5", "6", "7", "8"};
            final JComboBox<String> combo = new JComboBox<>(depths);
            
            int mode = JOptionPane.showOptionDialog(null, combo, title,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);

            System.out.println(mode);

            Object depth = combo.getSelectedItem();
            Integer depthLimitInteger = 4;
            if (depth != null) {
                depthLimitInteger = Integer.parseInt((String) depth);
                System.out.println("depthLimit: " + depthLimitInteger);
            }

            Agent Bob = new MinimaxCheckersAgent("Bob" + Integer.toString(depthLimitInteger), depthLimitInteger);
            Agent Alice;

            if (mode == 0) {
                Alice = new HumanCheckersAgent("Alice");
            } else {
                depths = Arrays.copyOfRange(depths, 1, 9);
                depthLimitInteger = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Depth Limit of Second AI Agent?",
                        title, JOptionPane.QUESTION_MESSAGE, null, depths, depths[0]));
                Alice = new MinimaxCheckersAgent("Alice" + Integer.toString(depthLimitInteger), depthLimitInteger);
            }

            Game game = new checkerGame(Alice, Bob);

            int selectedOption = JOptionPane.showConfirmDialog(null, "Play Again?", "Choose", JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.NO_OPTION) {
                game.exit();
            }
        }
        //game.play();
    }

}
