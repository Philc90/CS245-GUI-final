/***************************************************************
* file: HangmanPanel.java
* author: P. Chiang, G. Tong
* class: CS 245 - Programming Graphical User Interfaces
*
* assignment: Quarter Project v.1.1
* date last modified: 8/10/2016
*
* purpose: shows the game screen with the title, current time and date,
* score, picture of hanged guy, revealed/hidden letters, and button for each 
* letter. Also replaces itself with the endGamePanel which shows the score and
* allows user to return to menu
****************************************************************/
package edu.cpp.cs245;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author plchiang gztong
 */
public class HangmanPanel extends JPanel{
    private String[] possibleWords = {"abstract", "cemetery", "nurse", "pharmacy", 
            "climbing"};
    private BufferedImage logo;
    private JPanel buttonPanel;
    private String chosenWord;
    private JButton skipButton, continueButton;
    private JLabel scoreLabel, titleLabel;
    private ArrayList<JLabel> revealedLetters;
    private HashMap<String, JButton> lettersToButtons;
    private int score, failedAttempts, lettersRevealed;
    
//    default constructor
    public HangmanPanel() {
        super(null);
        
        initialize();
    }
    
//    initialize components
//    called by default constructor and reinitialize
    private void initialize() {
//        See current date / time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JLabel dateAndTimeField = new JLabel();
        dateAndTimeField.setBounds(430, 5, 160, 16);
        dateAndTimeField.setFont(new Font("Centaur", 1, 16));
        dateAndTimeField.setToolTipText("Current system time");
        add(dateAndTimeField);
//        Title of game
        titleLabel = new JLabel("Hangman");
        titleLabel.setBounds(5, 0, 400, 45);
        titleLabel.setFont(new Font("Centaur", 1, 40));
        titleLabel.setToolTipText("Hangman");
        add(titleLabel);

        
//        initialize game counters
//      # of failed guesses (6 total to lose the game)
        failedAttempts = 0;
//        # of letters guessed correctly and shown on screen
        lettersRevealed = 0;
//        initial score, subtract by 10 for each wrong guess
        score = 100;
        
//        button to give up and end game
        skipButton = new JButton("Skip");
        skipButton.setFont(new Font("Centaur", 0, 16));
        skipButton.setBounds(5, 50, 73, 25);
        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                score = 0;
                scoreLabel.setText("Score: " + score);
                endHangman("You Skipped!");
            }
        });
        skipButton.setToolTipText("Skip the game. You will get 0 pts!");
        add(skipButton);
        
//        display current score
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Centaur", 0, 24));
        scoreLabel.setBounds(5, 80, 150, 50);
        scoreLabel.setToolTipText("Your current score");
        add(scoreLabel);
        
//        Timer updates dateAndTimeField to current time & date
        Timer time = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dateAndTimeField.setText(dateFormat.format(new Date()));
            }
        });
        time.setRepeats(true);
        time.start();
        
        setupHangman();
    }
    
//    paintComponent override to show the hangman picture
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            logo = ImageIO.read(new File("src/resources/hangman" + failedAttempts + ".png"));            
        } catch(IOException e) {
            e.printStackTrace();
        }
        g.drawImage(logo, 130, 50, this);
    }
    
//    sets up the hangman game area
    private void setupHangman()
    {
//        buttonPanel contains the letter buttons
        buttonPanel = new JPanel();
        buttonPanel.setMaximumSize(new java.awt.Dimension(580, 100));
        buttonPanel.setLayout(new java.awt.GridLayout(2, 13));
        
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        lettersToButtons maps each letter to corresponding button for ease of access
        lettersToButtons = new HashMap<>();
        String currentLetter;
//      For each letter create a new JButton
        for(int index = 0; index < alphabet.length(); index++) {
            currentLetter = ""+alphabet.charAt(index);
            JButton letterButton = new JButton();
            letterButton.setFont(new java.awt.Font("Centaur", 1, 11));
            letterButton.setText(currentLetter);
            letterButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
            letterButton.setMinimumSize(new java.awt.Dimension(0, 0));
            letterButton.setPreferredSize(new java.awt.Dimension(9, 20));
//            setting actionCommand allows action listeners to know which button
//          was clicked
            letterButton.setActionCommand(currentLetter);
            letterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
//                    System.out.println("clicked " + ae.getActionCommand());                
                    JButton pressedLetter = lettersToButtons.get(ae.getActionCommand());
//                     In case the letter recorded by ActionEvent doesn't correspond
//                  to a button in lettersToButtons; this should never happen
                    if(pressedLetter == null) {
                        System.out.println("In PlayPanel: ERROR tried to choose symbol "
                                    + ae.getActionCommand() + ", which doesn't exist");
                        return;
                    }
//                  if user made a bad guess: subtract points, update display
                    if(!hangmanMove(ae.getActionCommand())) {
                        failedAttempts++;
                        System.out.println("failed attempt "+failedAttempts);
//                        draw another part of the hanged guy
                        score-=10;
                        scoreLabel.setText("Score: " + score);
                    }
//                    End the game if hanged guy is drawn or user guesses the whole word
                    if(failedAttempts == 6 || lettersRevealed == chosenWord.length()) {
                        if(failedAttempts == 6) {
                            endHangman("No More Guesses!");
                        }
                        else {
                            endHangman("Correct Word!");
                        }
                        return;
                    }
                    pressedLetter.setEnabled(false);
//                    repaint at the end to ensure all changes displayed
                    repaint();
                }
            });
            lettersToButtons.put(currentLetter, letterButton);
            letterButton.setToolTipText("Click to make guess");
            buttonPanel.add(letterButton);
        }
        
//        choose a random word from the possible words
        chosenWord = possibleWords[(int)(Math.random()*possibleWords.length)];
//        System.out.println(chosenWord);
        JPanel revealedLettersPanel = new JPanel(new FlowLayout());
        revealedLettersPanel.setToolTipText("Letters you have guessed correctly so far");
//        revealedLetters keeps track of letters that have been revealed 
//      by showing the user's correct guesses in JTextFields
        revealedLetters = new ArrayList<>();
        for(int i = 0; i < chosenWord.length(); i++) {
//            Revealed letters initially all blank
            JLabel letterLabel = new JLabel("_");
            letterLabel.setFont(new Font("Centaur", 1, 36));
            revealedLettersPanel.add(letterLabel);
            revealedLetters.add(letterLabel);
        }
        
        buttonPanel.setBounds(30, 330, 540, 60);
        revealedLettersPanel.setBounds(30, 270, 540, 50);
        this.add(buttonPanel);
        this.add(revealedLettersPanel);
    }
    
//    Called every time a user makes a move (i.e. pushes a letter button)
//    @return true if the user makes a correct guess, false if not
    private boolean hangmanMove(String letterPushed) {
        boolean goodMove = false;
//        NOTE: since possibleWords are all lowercase, and the buttons are
//      all uppercase letters with corresponding actionCommand as the same, 
//      the next line is critical
        letterPushed = letterPushed.toLowerCase();
        for(int i = 0; i < chosenWord.length(); i++) {
//            If letterPushed is good reveal it
            if(letterPushed.equals(""+chosenWord.charAt(i))) {
//                System.out.println("checking " + chosenWord.charAt(i));
                revealedLetters.get(i).setText(""+chosenWord.charAt(i));
                goodMove = true;
                lettersRevealed++;
            }
        }
//        System.out.println("Good move? "+goodMove);
        return goodMove;
    }
    
//    Shows the player's result at game end, then move on to the color game
    private void endHangman(String result) {
        JPanel thisPanel = this;
//        Wait 2 seconds then show the color game
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, thisPanel))
                        .doEndHangmanAction();
            }
        });
//        Show result: skipped, guessed the right letter, or ran out of guesses
        titleLabel.setText(result);
        disableAllButtons();
        timer.setRepeats(false);
        timer.start();
        repaint();
    }
    
//    Disables all buttons after game ends
    private void disableAllButtons() {
        skipButton.setEnabled(false);
        for(Map.Entry<String, JButton> entry : lettersToButtons.entrySet()) {
//            System.out.println("disabling " + entry.getKey());
            entry.getValue().setEnabled(false);
        }
    }
    
//    Utilized by Wrapper to make it possible to replay game
    public void reinitialize() {
        removeAll();
        initialize();
        repaint();
    }
    
//    Returns current score
    public int getScore() {
        return score;
    }
}
