/***************************************************************
* file: ProjectFrame.java
* author: G. Tong
* class: CS 245 - Programming Graphical User Interfaces
*
* assignment: Quarter Project v.1.1
* date last modified: 8/10/2016
*
* purpose: The JFrame where the project will be contained
****************************************************************/
package edu.cpp.cs245;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author gztong
 */
public class ProjectMain {
    
    /**
     * The main executable for the program
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    /**
     * Creates and shows the game. The title screen will show for 3 seconds and then it will 
     * close and move to the main menu.
     */
    private static void createAndShowGUI() {
//        frame to hold program
        JFrame f = new JFrame("Quarter Project");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        IntroPanel intro = new IntroPanel();
        f.add(intro);
        f.setResizable(false);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
//        Pressing F1 shows info from the creators of the program
        f.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("F1"), "Credits");
        f.getRootPane().getActionMap().put("Credits", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(f, "Philip Chiang, 011125712\n"
                        + "George Tong, 010451428\n"
                        + "Quarter Project\n"
                        + "Summer 2016");
            }
        });
//        Pressing Escape ends the program
        f.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "End");
        f.getRootPane().getActionMap().put("End", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
//        timer shows intro page for 3 seconds, then show menu page
        Timer time = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                intro.setVisible(false);
                f.remove(intro);
                f.add(new Wrapper());
            }
        });
        time.setRepeats(false);
        time.start();
    }
}

