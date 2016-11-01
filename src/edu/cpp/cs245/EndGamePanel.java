/***************************************************************
* file: EndGamePanel.java
* author: P. Chiang
* class: CS 245 - Programming Graphical User Interfaces
*
* assignment: Quarter Project v.1.1
* date last modified: 8/17/2016
*
* purpose: Shows the final score after the game finishes. Allows the 
* player to enter in their initials and submit to the high scores, then
* return to menu
****************************************************************/
package edu.cpp.cs245;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author plchiang
 */
public class EndGamePanel extends JPanel{
    private JButton endButton, submitButton;
    private JLabel scoreLabel, promptLabel;
    private JTextField playerInitials;
    private int score;
    private JLabel[] highScoreLabels;
    private File highScoresFile;
//    players, scores, and sortedScores are used to determine if the player has
//    made it to the high scores
    private ArrayList<String> players;
    private ArrayList<Integer> scores;
    private PriorityQueue<Integer> sortedScores;
    private Font labelFont, buttonFont;
    
//    constructor for endGamePanel has the score to start counting at as the parameter
    public EndGamePanel(int score) {
        super(null);
        this.score = score;
        
        highScoresFile = new File("src/resources/highscores.txt");
//        players are mapped to their scores by having the same index in the array
        players = new ArrayList<>();
        scores = new ArrayList<>();
//        sortedScores is used for finding the lowest score
        sortedScores = new PriorityQueue<>();
//        load previous high scores
        
        setBackground(Color.black);
        setBounds(0,0,600,400);
        
        labelFont = new Font("Centaur", 1, 24);
        buttonFont = new Font("Centaur", 0, 14);
        
//        user won/lost/skipped
        scoreLabel = new JLabel("Final score: " + score + " pts");
        scoreLabel.setBounds(210,10,250,30);
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(labelFont);
        add(scoreLabel);

//        label
        JLabel sectionLabel = new JLabel("<html>High Scores<hr></html>");
        sectionLabel.setBounds(225,60,400,30);
        sectionLabel.setForeground(Color.white);
        sectionLabel.setFont(labelFont);
        add(sectionLabel);
        
//        each high score
        highScoreLabels = new JLabel[5];
        for(int i = 0; i < 5; i++) {
            JLabel highScoreLabel = new JLabel("ABC.....00000");
            highScoreLabel.setBounds(235, 40*i + 100, 130, 15);
            highScoreLabel.setForeground(Color.white);
            highScoreLabel.setFont(new Font("Centaur", 1, 16));
            add(highScoreLabel);
            highScoreLabels[i] = highScoreLabel;
        }
        
        
//        prompt user for their initials
        promptLabel = new JLabel("Enter your initials: ");
        promptLabel.setBounds(170,290,200,50);
        promptLabel.setForeground(Color.white);
        promptLabel.setFont(labelFont);
        promptLabel.setToolTipText("Enter your initials");
        
//        text field to allow user to enter their initials
        playerInitials = new JTextField();
        playerInitials.setEditable(true);
        playerInitials.setBounds(370,300,30,25);
//        limit player initials to 3 chars and all uppercase
        playerInitials.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String  str, AttributeSet a) 
                    throws BadLocationException {
                if (str == null) return;

                if ((getLength() + str.length()) <= 3) {
//                    forces input to be 3 chars all uppercase
                    super.insertString(offset, str.toUpperCase(), a);
                }
            }
        });
        
//        submit the score to high scores
        submitButton = new JButton("Submit");
        submitButton.setBounds(405,300,73,25);
        submitButton.setFont(buttonFont);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                writeScoreToFile();
                submitButton.setEnabled(false);
                loadScores(false);
            }
        });
        submitButton.setToolTipText("Add your initials and score to the high scores");
        
//        button to take user back to menu page
        endButton = new JButton("End");
        endButton.setBounds(263,335,73,25);
        endButton.setFont(buttonFont);
        endButton.setToolTipText("Return to menu");
        add(endButton);
        setEndButtonActionListener();
        
        loadScores(true);
    }
    
//    Action listener for endButton
//    When the user clicks on it, the program shows the menu page
    private void setEndButtonActionListener() {
        EndGamePanel thisPanel = this;
        endButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent ae) {
                score = 0;
                submitButton.setEnabled(true);
                ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, thisPanel))
                        .doMenuReturnButtonAction();
                reinitialize();
            }
        });
    }
    
//    Loads high scores from a file
//    @param fillArrays true if this is the first time you have run the program
//    and you must fill up the players, scores, and sortedScores lists, false if
//    they have already been filled in
    private void loadScores(boolean fillArrays) {
//        reading from file
        try {
            Scanner fileIn = new Scanner(highScoresFile);
            int counter = 0;
            String nextLine, highScore = "";
            while(fileIn.hasNextLine()) {
                nextLine = fileIn.nextLine();
                String[] strArray = nextLine.split(" ");
//                player's name
                highScore += strArray[0];
//                player's score
                highScore += "........." + strArray[1];
//                setting text of each high score listing
                highScoreLabels[counter].setText(highScore);
                counter++;
                highScore = "";
                
                if(fillArrays == true) {
                    Integer playerScore = Integer.parseInt(strArray[1]);
    //                populating the players and scores arraylists
                    players.add(strArray[0]);
                    scores.add(playerScore);
    //                put scores in priority queue, the lowest scores at the front
                    sortedScores.add(playerScore);
                }
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("ERROR: Failed loading highscores.txt");
        }
        
        if(sortedScores.size() < 5 || score > sortedScores.peek()) {
            showPrompt();
        }
        else {
            hidePrompt();
        }
    }
    
    
//    Updates the score in endPanel with the most current score
    public void updateScore(int score) {
        this.score = score;
        scoreLabel.setText("Final score: " + score);
    }
    
//    loadScores should be called first. It uses a priority queue to 
//    determine which score is the lowest. If there are more than 5 high scores 
//    when including the current score, then remove the lowest until there are
//    at most 5. Then, write the modified high scores to the text file.
    private void writeScoreToFile() {
        try {
//            put current player's score
            players.add(playerInitials.getText());
            scores.add(score);
            sortedScores.add(score);
            
//            Only 5 players in high scores; remove the player with the lowest score
            while(sortedScores.size() > 5) {
//                find the lowest score
                Integer lowestScore = sortedScores.peek();
//                remove that player and score from each list
                sortedScores.remove(lowestScore);
                int index = scores.indexOf(lowestScore);
                players.remove(index);
                scores.remove(index);
            }
            
//            writing the changes to file
            FileWriter fileWriter = new FileWriter(highScoresFile, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
//            formatting the new file
            String whatToSave = "";
            while(sortedScores.size() > 0) {
//                each line is a different player/score, with highest scores on top
                Integer lowestScore = sortedScores.peek();
                sortedScores.remove(lowestScore);
                int index = scores.indexOf(lowestScore);
                String line = players.get(index) + " " + scores.get(index);
                players.remove(index);
                scores.remove(index);
//                since the front of the priority queue is actually the lowest score,
//                must add the lines in opposite order
                whatToSave = line + "\n" + whatToSave;
            }
//            printWriter overwrites the high scores file with new high scores
            printWriter.print(whatToSave);
            printWriter.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("ERROR: Failed loading highscores.txt");
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            System.out.println("ERROR: Failed writing to highscores.txt");
        }
//        reset panel for subsequent plays
        playerInitials.setText("");
        playerInitials.setEditable(false);
    }
    
//    Prompt the player to enter their initials if they made it to the high scores
    private void showPrompt() {
        add(promptLabel);
        add(playerInitials);
        add(submitButton);
    }
    
//    Hide the prompt for the player to enter in their initials if they didn't
//    make it to the high scores
    private void hidePrompt() {
        remove(promptLabel);
        remove(playerInitials);
        remove(submitButton);
    }
    
//    Resets components in the panel that have been modified for subsequent playthroughs
    public void reinitialize() {
        players.clear();
        scores.clear();
        sortedScores.clear();
        playerInitials.setText("");
        playerInitials.setEditable(true);
        hidePrompt();
        loadScores(true);
    }
}
