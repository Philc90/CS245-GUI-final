/***************************************************************
* file: CreditPanel.java
* author: P. Chiang
* class: CS 245 - Programming Graphical User Interfaces
*
* assignment: Quarter Project v.1.1
* date last modified: 8/10/2016
*
* purpose: Shows the credits, including the names and BroncoIDs of the
* group members, and a button to go back to the menu.
****************************************************************/
package edu.cpp.cs245;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author plchiang
 */
public class CreditPanel extends JPanel{
    private JButton backButton;
    
//    default constructor
    public CreditPanel() {
//        absolute positioning
        super(null);
//        display title
        JLabel creditsLabel = new JLabel("CREDITS");
        creditsLabel.setFont(new java.awt.Font("Centaur", 1, 48));
        creditsLabel.setBounds(205, 60, 220, 50);
        add(creditsLabel);
//        display project group members
        JLabel person1 = new JLabel("Philip Chiang, 011125712"), 
                person2 = new JLabel("George Tong, 010451428");
        person1.setBounds(210, 150, 250, 50);
        person1.setFont(new java.awt.Font("Centaur", 0, 18));
        person2.setBounds(210, 210, 250, 50);
        person2.setFont(new java.awt.Font("Centaur", 0, 18));
        add(person1);
        add(person2);
//        button to return to menu
        backButton = new JButton("Back");
        backButton.setFont(new Font("Centaur", 0, 14));
        backButton.setBounds(10, 370, 73, 25);
        backButton.setToolTipText("Return to menu");
        add(backButton);
        setBackButtonActionListener();
    }
    
//    Action Listener for the "Back" button, goes back to the menu
    private void setBackButtonActionListener() {
        CreditPanel thisPanel = this;
        backButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent ae) {
                ((Wrapper)SwingUtilities.getAncestorOfClass(Wrapper.class, thisPanel))
                        .doMenuReturnButtonAction();
            }
        });
    }
}
