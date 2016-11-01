/**
 * *************************************************************
 * file: SudokuGame.java
 * author: G. Tong
 * class: CS 245 - Programming Graphical User Interfaces
 *
 * assignment: Quarter Project v.1.2
 * date last modified: 8/23/2016
 *
 * purpose: Displays a Sudoku game  with some spaces already filled in.
 * The user must fill the board with number so that each row and column has no repeating numbers.
 * The valid numbers are from 1-9 and any other input will give the user a warning.
 * The user starts with 540 points and 10 points are deducted for each incorrect square.
 * Quitting without attempting the game will give a 540 point deduction
 * Incorrect squares can only be deducted once.
 * The game will end upon completion or quitting.
 ***************************************************************
 */
package edu.cpp.cs245;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.NumberFormatter;
import javax.swing.text.PlainDocument;

/**
 *
 * @author GT
 */
public class SudokuGame extends JPanel {
    
    private ArrayList<JTextField> sudokuList;
    private ArrayList<Boolean> sudokuTracker;
    private Integer answerGrid[][];
    private Integer score;
    private boolean attempted;

    /**
     * Default constructor for SudokuGame. The default layout is null and the
     * points start at 540.
     */
    public SudokuGame() {
        resetGrid();
        sudokuList = new ArrayList<JTextField>();
        sudokuTracker = new ArrayList<Boolean>();
        setLayout(null);
        setSize(600, 400);
        score = 540;
        attempted = false;
    }

    /**
     * Resets the answerGrid to its original state. The correct solutions are
     * not in this grid.
     */
    public void resetGrid() {
        answerGrid = new Integer[][]{
            {8, 0, 0, 4, 0, 6, 0, 0, 7},
            {0, 0, 0, 0, 0, 0, 4, 0, 0},
            {0, 1, 0, 0, 0, 0, 6, 5, 0},
            {5, 0, 9, 0, 3, 0, 7, 8, 0},
            {0, 0, 0, 0, 7, 0, 0, 0, 0},
            {0, 4, 8, 0, 2, 0, 1, 0, 3},
            {0, 5, 2, 0, 0, 0, 0, 9, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0},
            {3, 0, 0, 9, 0, 2, 0, 0, 5}
        };
    }

    /**
     * Adds components needed for the sudokuGame. Adds the title, the score, the
     * date, the time, and the game board. Only numbers 1-9 are allowed in the
     * sudokuBoard and any violations will cause a warning to appear. The
     * solution can be checked with the checkSolution button.
     */
    public void startGame() {
        //        See current date / time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JLabel dateAndTimeField = new JLabel();
        dateAndTimeField.setBounds(430, 5, 160, 16);
        dateAndTimeField.setFont(new Font("Centaur", 1, 16));
        dateAndTimeField.setToolTipText("Current system time");
        add(dateAndTimeField);

//        Timer updates dateAndTimeField to current time & date
        javax.swing.Timer time = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dateAndTimeField.setText(dateFormat.format(new Date()));
            }
        });
        time.setRepeats(true);
        time.start();

//        Title of game
        JLabel titleLabel = new JLabel("Sudoku");
        titleLabel.setBounds(5, 0, 400, 45);
        titleLabel.setFont(new Font("Centaur", 1, 40));
        titleLabel.setToolTipText("Sudoku");
        add(titleLabel);

        //Create sudoku game
        JPanel sudoku = new JPanel();
        sudoku.setMaximumSize(new Dimension(400, 280));
        sudoku.setLayout(new GridLayout(9, 9));
        sudoku.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(sudoku);
        sudoku.setBounds(150, 40, 350, 350);
        for (int i = 0; i < 81; i++) {
            //allows only 1-9 in text field
            NumberFormat format = NumberFormat.getInstance();
            NumberFormatter formatter = new NumberFormatter(format);
            formatter.setValueClass(Integer.class);
            formatter.setMinimum(1);
            formatter.setMaximum(9);
            formatter.setAllowsInvalid(true);
            formatter.setCommitsOnValidEdit(true);
            JFormattedTextField input = new JFormattedTextField(formatter);
            input.setHorizontalAlignment(JTextField.CENTER);
            input.setToolTipText("Enter a number between 1-9");
            sudokuList.add(input);
            sudokuTracker.add(true);
            sudoku.add(input);
            //keyListener for if the user inputs an invalid number
            input.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (input.getText().length() > 1) {
                        JOptionPane.showMessageDialog(input, "Please enter a digit from 1-9", "Warning", JOptionPane.ERROR_MESSAGE);
                        if(input.isEditable())
                        {
                            input.setText("");
                        }
                    } else if (key == KeyEvent.VK_F1) {
                        JOptionPane.showMessageDialog(null, "Philip Chiang, 011125712\n"
                                + "George Tong, 010451428\n"
                                + "Quarter Project\n"
                                + "Summer 2016");
                    } else if (key == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    } else if (key != 8 && key != KeyEvent.VK_DELETE && key != KeyEvent.VK_ENTER) {
                        if (key < 49 || (key > 57 && key < 97) || key > 105) {
                            JOptionPane.showMessageDialog(input, "Please enter a digit from 1-9", "Warning", JOptionPane.ERROR_MESSAGE);
                            if(input.isEditable())
                            {
                                input.setText("");
                            }
                        }
                    }
                    
                }
            });
        }
        setUpSudoku();
        updateAnswerGrid();
        JButton submitButton = new JButton("Submit");
        JButton quitButton = new JButton("Quit");
        JLabel scoreLabel = new JLabel();
        scoreLabel.setText("Score:" + score.toString());
        scoreLabel.setHorizontalTextPosition(JLabel.CENTER);
        scoreLabel.setToolTipText("Your current score");
        submitButton.setHorizontalAlignment(JButton.CENTER);
        quitButton.setHorizontalAlignment(JButton.CENTER);
        submitButton.setToolTipText("Click to check solution");
        quitButton.setToolTipText("End the game with the points you have earned so far");
        
        submitButton.setBounds(30, 350, 80, 30);
        quitButton.setBounds(520, 350, 60, 30);
        scoreLabel.setBounds(10, 50, 80, 80);
        add(submitButton);
        add(quitButton);
        add(scoreLabel);
        
        sudoku.setVisible(true);
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean done = checkSolution();
                attempted = true;
                scoreLabel.setText("Score:" + score.toString());
                if (done) {
                    JOptionPane.showMessageDialog(null, "Congratulations you solved the puzzle!");
                    endGame();
                } else {
                    JOptionPane.showMessageDialog(null, "Some tiles are incorrect");
                }
            }
        });
        
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endGame();
            }
        });
        
    }
    
    /**
     * Sets up the pre-given numbers in the Sudoku board.
     */
    public void setUpSudoku() {
        int index = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (answerGrid[r][c] != 0) {
                    sudokuList.get(index).setText(answerGrid[r][c].toString());
                    sudokuList.get(index).setEditable(false);
                }
                index++;
            }
        }
    }

    /**
     * @return whether or not the puzzle has been solved Checks to see if the
     * correct solution has been inputed. Points are deducted for empty spaces
     * or incorrect solutions. Points can only be deducted once per square.
     */
    public boolean checkSolution() {
        int index = 0;
        boolean solved = true;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (sudokuList.get(index).getText().equals("")) {
                    if (sudokuTracker.get(index) == true) {
                        score -= 10;
                        sudokuTracker.set(index, false);
                        solved = false;
                    } else {
                        solved = false;
                    }
                } else {
                    int input = Integer.valueOf(sudokuList.get(index).getText());
                    if (answerGrid[r][c] != input) {
                        if (sudokuTracker.get(index) == true) {
                            score -= 10;
                            sudokuTracker.set(index, false);
                            solved = false;
                        } else {
                            solved = false;
                        }
                    }
                }
                
                index++;
            }
        }
        return solved;
    }

    /**
     * Updates the answerGrid to hold the actual solutions.
     */
    public void updateAnswerGrid() {
        answerGrid = new Integer[][]{
            {8, 3, 5, 4, 1, 6, 9, 2, 7},
            {2, 9, 6, 8, 5, 7, 4, 3, 1},
            {4, 1, 7, 2, 9, 3, 6, 5, 8},
            {5, 6, 9, 1, 3, 4, 7, 8, 2},
            {1, 2, 3, 6, 7, 8, 5, 4, 9},
            {7, 4, 8, 5, 2, 9, 1, 6, 3},
            {6, 5, 2, 7, 8, 1, 3, 9, 4},
            {9, 8, 1, 3, 4, 5, 2, 7, 6},
            {3, 7, 4, 9, 6, 2, 8, 1, 5}
        };
    }

    /**
     * Ends the game and removes all elements. Transitions into the end game
     * panel.
     */
    public void endGame() {
        if (!attempted) {
            score -= 540;
            JOptionPane.showMessageDialog(null, "No attempts were made, 540 points were deducted.");
        }
        attempted = false;
        removeAll();
        resetGrid();
        sudokuList = new ArrayList<JTextField>();
        sudokuTracker = new ArrayList<Boolean>();
        setVisible(false);
        ((Wrapper) SwingUtilities.getAncestorOfClass(Wrapper.class, this))
                .doEndSudokuGameAction();
    }

    /**
     *
     * @param newScore the updated score. Setter method for the private variable
     * score in the sudokuGame
     */
    public void setScore(int newScore) {
        score = newScore;
        
    }

    /**
     *
     * @return the score the player received The getter for the private variable
     * score.
     */
    public int getScore() {
        return score;
    }
}
