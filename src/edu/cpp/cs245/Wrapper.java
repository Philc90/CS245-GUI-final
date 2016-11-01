/***************************************************************
* file: Wrapper.java
* author: P. Chiang
* class: CS 245 - Programming Graphical User Interfaces
*
* assignment: Quarter Project v.1.1
* date last modified: 8/10/2016
*
* purpose: Wrapper is a JPanel with CardLayout which allows for switching between
* different JPanels. Menu, High Scores, Credits, and Play are all JPanels that sit on
* top of Wrapper.
****************************************************************/

package edu.cpp.cs245;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author plchiang
 */
public class Wrapper extends JPanel{
    private CardLayout cl;
    private MenuPanel menu;
    private HighScorePanel highScore;
    private CreditPanel credits;
    private HangmanPanel play;
    private EndGamePanel end;
    private ColorGamePanel colorGame;
    private SudokuGame sudokuGame;
    
//    Default constructor
    public Wrapper() {
        super();
//        CardLayout allows for switching to display another panel
        cl = new CardLayout();
        setLayout(cl);
//        add components
        menu = new MenuPanel();
        add(menu, "Menu");
        highScore = new HighScorePanel();
        add(highScore, "High Scores");
        credits = new CreditPanel();
        add(credits, "Credits");
        play = new HangmanPanel();
        add(play, "Play");
        colorGame = new ColorGamePanel();
        add(colorGame, "Color Game");
        sudokuGame = new SudokuGame();
        add(sudokuGame, "Sudoku Game");
        end = new EndGamePanel(0);
        add(end, "End");
//        begin by showing the menu
        cl.show(this, "Menu");
    }
//    Makes this panel and all child panels 600x400
//    Needed since ProjectFrame doesn't specify its own size
    public Dimension getPreferredSize() {
        return new Dimension(600, 400);
    }
    
//    Called upon clicking the play button in the menu panel
//    shows the Hangman game
    public void doPlayButtonAction() {
        play.reinitialize();
        cl.show(this, "Play");
    }
    
//    Called upon clicking on the high scores button in the menu panel
//    shows the High Scores panel
    public void doHighScoresButtonAction() {
        highScore.update();
        cl.show(this, "High Scores");
    }
    
//    Called upon clicking on the Credits button in the menu panel
//    Shows the Credits panel
    public void doCreditsButtonAction() {
        cl.show(this, "Credits");
    }
    
//    Called upon clicking on the End button in EndGamePanel
//    Shows the Menu page
    public void doMenuReturnButtonAction() {
        cl.show(this, "Menu");
    }
    
//    Called at the end of the hangman game
//    sends its current score to the color game, then shows the color game
    public void doEndHangmanAction() {
//        call this to pass the score from the hangman game to the color game
        colorGame.updateScore(play.getScore());
        colorGame.setUpGame();
        cl.show(this, "Color Game");
    }
    
//    Called at the end of the color game
//    Shows the end panel
    public void doEndColorGameAction() {
//        send current score to the end panel, then reinitialize the color game
        end.updateScore(colorGame.getScore());        
        sudokuGame.setScore(colorGame.getScore() + sudokuGame.getScore());
        colorGame.setScore(0);
        sudokuGame.startGame();
        cl.show(this, "Sudoku Game");
    }
    
    public void doEndSudokuGameAction()
    {
        end.updateScore(sudokuGame.getScore());
        end.reinitialize();
        sudokuGame.setScore(540);
        cl.show(this, "End");
    }
}
