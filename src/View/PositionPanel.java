// View/PositionPanel.java
package View;

import javax.swing.*;
import java.awt.*;

/**
 * This represents a position panel detailing the users current coordinate grid position in the game
 * and the distance to reaching the exit and winning game.
 *
 */
public class PositionPanel extends JPanel {


    /** Label displays the current (x,y) position. */
    private final JLabel myCurrentLabel  = new JLabel("Current: (0,0)", SwingConstants.CENTER);
    /** Label displays the distance to the exit */
    private final JLabel myDistanceLabel = new JLabel("To exit: —", SwingConstants.CENTER);


    /**
     * Constructs a PositionPanel with current position, distance to exit, and attempts.
     */
    public PositionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Position"));
        setBackground(Color.LIGHT_GRAY);

        final JPanel theCenter = new JPanel(new GridLayout(3, 1));
        theCenter.setBackground(Color.LIGHT_GRAY);
        theCenter.add(myCurrentLabel);
        theCenter.add(myDistanceLabel);


        add(theCenter, BorderLayout.CENTER);
    }

    /**
     * This updates the displayed (x,y) position.
     *
     * @param theX the current x-coordinate
     * @param theY the current y-coordinate
     */
    public void setPosition(final int theX, final int theY) {
        myCurrentLabel.setText("Current: (" + theX + "," + theY + ")");
    }

    /**
     * This updates the distance displayed
     *
     * @param theText the distance readout to display
     */
    public void setDistanceText(final String theText) {
        myDistanceLabel.setText("To exit: " + theText);
    }


}
