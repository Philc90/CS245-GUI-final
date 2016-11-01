/***************************************************************
* file: MenuPanel.java
* author: P. Chiang
* class: CS 245 - Programming Graphical User Interfaces
*
* assignment: Quarter Project v.1.1
* date last modified: 8/10/2016
*
* purpose: Shows the menu at the start of the program after the intro.
* The menu has a logo, and buttons to go to Play, High Scores, and Credits.
****************************************************************/
package edu.cpp.cs245;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author plchiang
 */
public class MenuPanel extends JPanel {
    BufferedImage logo = null;
    private JButton playButton, highScoresButton, creditsButton;
    
//    default constructor
    public MenuPanel() {
//        absolute positioning
        super(null);
        setBackground(Color.BLACK);
//        font for use with subsequent buttons
        Font buttonFont = new Font("Centaur", 1, 20);
//        For subsequent buttons, they are displayed in MenuPanel but their 
//      ActionListeners are actually set in Wrapper
//        Play game
        playButton = new JButton("Play");
        playButton.setBounds(390,115,200,50);
        playButton.setFont(buttonFont);
        playButton.setToolTipText("Play the game");
        add(playButton);
//        View high scores
        highScoresButton = new JButton("High Scores");
        highScoresButton.setBounds(390,175,200,50);
        highScoresButton.setFont(buttonFont);
        highScoresButton.setToolTipText("View high scores");
        add(highScoresButton);
//        view credits
        creditsButton = new JButton("Credits");
        creditsButton.setBounds(390,235,200,50);
        creditsButton.setFont(buttonFont);
        creditsButton.setToolTipText("View credits");
        add(creditsButton);
        
        setupButtonActionListeners();
    }
    
//    overridde paintComponent to show the group logo
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            logo = ImageIO.read(new File("src/resources/logo.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        g.drawImage(logo, 60, 70, this);
    }
    
//    Set actionlisteners for each button, called by constructo
    private void setupButtonActionListeners() {
        setPlayButtonActionListener();
        setHighScoresButtonActionListener();
        setCreditsButtonActionListener();
    }
    
//    Action Listener for the "Play" button, starts the hangman game
    public void setPlayButtonActionListener() {
        MenuPanel thisPanel = this;
        playButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, thisPanel))
                        .doPlayButtonAction();
            }
        });
    }
    
//    Action Listener for the "High Scores" button, shows high score list
    public void setHighScoresButtonActionListener() {
        MenuPanel thisPanel = this;
        highScoresButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, thisPanel))
                        .doHighScoresButtonAction();
            }
        });
    }
    
//    Action Listener for the "Credits" button, shows the credits
    public void setCreditsButtonActionListener() {
        MenuPanel thisPanel = this;
        creditsButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, thisPanel))
                        .doCreditsButtonAction();
            }
        });
    }
}
