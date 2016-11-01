/***************************************************************
* file: HighScorePanel.java
* author: G. Tong, P Chiang
* class: CS 245 - Programming Graphical User Interfaces
*
* assignment: Quarter Project v.1.1
* date last modified: 8/16/2016
*
* purpose: displays the high scores list with initials for each person
****************************************************************/
package edu.cpp.cs245;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author gztong
 */
public class HighScorePanel extends javax.swing.JPanel {
    /**
     * Creates new form HighScorePanel
     */
    public HighScorePanel() {
        initComponents();
        loadScores();
        backBttn.setToolTipText("Return to menu");
        setBackButtonActionListener();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * Adds in the highscores and a button to return to the main menu.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backBttn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        HighScore1 = new javax.swing.JLabel();
        HighScore2 = new javax.swing.JLabel();
        HighScore3 = new javax.swing.JLabel();
        HighScore4 = new javax.swing.JLabel();
        HighScore5 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(600, 400));
        setPreferredSize(new java.awt.Dimension(600, 400));
        setLayout(null);

        backBttn.setFont(new java.awt.Font("Centaur", 0, 14)); // NOI18N
        backBttn.setText("Back");
        add(backBttn);
        backBttn.setBounds(10, 370, 73, 25);

        jLabel1.setFont(new java.awt.Font("Centaur", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HIGH SCORES");
        add(jLabel1);
        jLabel1.setBounds(130, 40, 340, 80);

        jPanel1.setLayout(new java.awt.GridLayout(5, 1));

        HighScore1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HighScore1.setText("ABC.....00000");
        jPanel1.add(HighScore1);

        HighScore2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HighScore2.setText("ABC.....00000");
        jPanel1.add(HighScore2);

        HighScore3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HighScore3.setText("ABC.....00000");
        jPanel1.add(HighScore3);

        HighScore4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HighScore4.setText("ABC.....00000");
        jPanel1.add(HighScore4);

        HighScore5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HighScore5.setText("ABC.....00000");
        jPanel1.add(HighScore5);

        add(jPanel1);
        jPanel1.setBounds(150, 120, 310, 240);
    }// </editor-fold>//GEN-END:initComponents
    
    private void backBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBttnActionPerformed

    }//GEN-LAST:event_backBttnActionPerformed
    
    /**
     * Hides the highscores and returns to the main panel
     */
    private void setBackButtonActionListener() {
        HighScorePanel thisPanel = this;
        backBttn.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, thisPanel))
                        .doMenuReturnButtonAction();
            }
        });
    }
    
//    Loads high scores from a file
    private void loadScores() {
        JLabel[] highScoreLabels = {HighScore1, HighScore2, HighScore3, 
                HighScore4, HighScore5};
//        reading from file
        try {
            Scanner fileReader = new Scanner(new File("src/resources/highscores.txt"));
            int counter = 0;
            String nextLine, highScore = "";
            while(fileReader.hasNextLine()) {
                nextLine = fileReader.nextLine();
                String[] strArray = nextLine.split(" ");
//                player's name
                highScore += strArray[0];
//                player's score
                highScore += "........." + strArray[1];
                highScoreLabels[counter].setText(highScore);
                counter++;
                highScore = "";
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("ERROR: Failed loading highscores.txt");
        }
    }
    
//    Forces the panel to update to the most current high scores
    public void update() {
        loadScores();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HighScore1;
    private javax.swing.JLabel HighScore2;
    private javax.swing.JLabel HighScore3;
    private javax.swing.JLabel HighScore4;
    private javax.swing.JLabel HighScore5;
    private javax.swing.JButton backBttn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
