/**
 * *************************************************************
 * file: ColorGamePanel.java
 * author: G. Tong
 * class: CS 245 - Programming Graphical User Interfaces
 *
 * assignment: Quarter Project v.1.1
 * date last modified: 8/15/2016
 *
 * purpose: Shows a randomly selected color name and gives the text is randomly colored.
 * There are also five colored buttons, if the button that has the same color as the text is pressed the user is given one-hundred points
 * The buttons will then move to different locations and the text will change as well, this will occur five times.
 * After the fifth time, the game will go to the end game panel
 * The date and time is also constantly displayed in this panel.
 ***************************************************************
 */
package edu.cpp.cs245;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.Event.*;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author GT
 */
public class ColorGamePanel extends JPanel {

    private JLabel colorLabel;
    private String[] colorNames = {"Red", "Yellow", "Green", "Blue", "Purple"};
    private Color[] fontColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
    private ArrayList<Integer> xCord;
    private ArrayList<Integer> yCord;
    private int numTries;
    private int score;
    

    public ColorGamePanel() {
        numTries = 0;
        score = 0;
        xCord = new ArrayList<Integer>();
        yCord = new ArrayList<Integer>();
        setLayout(null);
    }
    
    /**
     * Adds date and time to the top right hand corner of the panel.
     */
    public void addDateandTime()
    {
        //        See current date / time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JLabel dateAndTimeField = new JLabel();
        dateAndTimeField.setBounds(430, 5, 160, 16);
        dateAndTimeField.setFont(new Font("Centaur", 1, 16));
        dateAndTimeField.setToolTipText("Current system time");
        add(dateAndTimeField);
        //        Timer updates dateAndTimeField to current time & date
        Timer time = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dateAndTimeField.setText(dateFormat.format(new Date()));
            }
        });
        time.setRepeats(true);
        time.start();
    }

    /**
     * Adds all possible x and y coordinates to their respective ArrayLists.
     */
    public void addCords() {
        int xBase = 10;
        for (int i = 0; i < 6; i++) {
            xCord.add(xBase);
            xBase += 100;
        }

        int yBase = 20;
        for (int i = 0; i < 4; i++) {
            yCord.add(yBase);
            yBase += 100;
        }
    }

    /**
     * Removes all coordinates from the x and y coordinate ArrayLists.
     */
    public void removeAllCords() {
        xCord = new ArrayList<Integer>();
        yCord = new ArrayList<Integer>();
    }
    
    /**
     * Selects a random color name and a random color and displays it on the panel.
     */
    public void setUpColorName() {
        colorLabel = new JLabel(colorNames[(int) (Math.random() * 5)]);
        colorLabel.setForeground(fontColors[(int) (Math.random() * 5)]);
        Font colorFont = new Font("Centaur", 1, 30);
        colorLabel.setFont(colorFont);
        colorLabel.setBounds(240, 62, 150, 100);
        colorLabel.setToolTipText("Click on the square that has the color of the text");
        add(colorLabel);
    }
    
    /**
     * @param prevScore Score from the hangManGame
     * Adds the score from the Color Game to the Hangman Game
     */
    public void updateScore(int prevScore){
        score += prevScore;
    }
    
    /**
     * @return the current score
     * Gets the current score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * @param score the new score
     * Sets the score to the passed in value
     */
    public void setScore(int score)
    {
        this.score = score;
    }
    
    /**
     * Displays the score in the upper left hand corner
     */
    public void displayScore()
    {
        Integer currScore = score;
        JLabel scoreLabel = new JLabel("Score: " + currScore.toString());
        Font font = new Font("Centaur", 1, 14);
        scoreLabel.setFont(font);
        scoreLabel.setBounds(5, 5, 150, 16);
        scoreLabel.setToolTipText("Your current score");
        add(scoreLabel);
    }
    
    /**
     * Starts up the game and adds all necessary components such as buttons and labels.
     * Adds the Color name and the color that needs to be clicked to the panel.
     * Adds five colored buttons to random locations in the panel.
     * Clicking any button cause all the buttons to relocate themselves and will increase the score
     * if the correct button was clicked.
     */
    public void setUpGame() {
        addDateandTime();
        displayScore();
        setUpColorName();
        addCords();
        String[] buttonNames = {"redButton", "yellowButton", "greenButton", "blueButton", "purpleButton"};
        for (int i = 0; i < buttonNames.length; i++) {
            //Find file location for button picture
            String imgLoc = "src//resources//";
            imgLoc = imgLoc + buttonNames[i];
            String imgLocHover = imgLoc + "Hover.png";
            imgLoc += ".png";
            JButton btn = new JButton();
            ImageIcon img = new ImageIcon(imgLoc);
            ImageIcon img2 = new ImageIcon(imgLocHover);
            btn.setForeground(fontColors[i]);
            btn.setIcon(img);
            btn.setRolloverIcon(img2);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            add(btn);
            btn.setBounds(xCord.remove((int) (Math.random() * xCord.size())), yCord.get((int) (Math.random() * yCord.size())), 82, 82);
            btn.setVisible(true);  
            btn.setToolTipText("Click on the square that has the color of the text");
            repaint();
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == btn) {
                        if (btn.getForeground().equals(colorLabel.getForeground())) {
                            removeAll();
                            xCord.clear();
                            yCord.clear();
                            numTries++;
                            score += 100;                            
                            setUpGame();   
                        }
                        else
                        {
                            removeAll();
                            xCord.clear();
                            yCord.clear();
                            numTries++;
                            setUpGame();
                        }
                        
                        if(numTries >= 5)
                        {
                            numTries = 0;
                            endGame();
                        }
                    }
                }
            });
        }
    }
    
    /**
     * Ends the color game and transitions into the endgame panel
     */
    public void endGame()
    {
        removeAll();
        setVisible(false);
        ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, this))
                        .doEndColorGameAction();
    }
}
