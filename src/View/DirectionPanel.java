// View/DirectionPanel.java
package View;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Shows which directions are available. Purely visual; not clickable.
 * Call setAvailable(north, south, east, west) to update colors.
 */
public class DirectionPanel extends JPanel {

    private static final Color AVAILABLE_COLOR   = new Color(180, 255, 180); // light green
    private static final Color UNAVAILABLE_COLOR = new Color(220, 220, 220); // light gray

    private final Map<String, JLabel> myLabels = new HashMap<>();

    public DirectionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Available"));
        setBackground(Color.LIGHT_GRAY);

        final JPanel theGrid = new JPanel(new GridLayout(4, 1, 5, 5));
        theGrid.setOpaque(false);

        for (final String theDir : new String[]{"NORTH", "SOUTH", "EAST", "WEST"}) {
            final JLabel theLabel = new JLabel(theDir, SwingConstants.CENTER);
            theLabel.setOpaque(true);                          // key: labels honor background
            theLabel.setBackground(UNAVAILABLE_COLOR);
            theLabel.setFont(theLabel.getFont().deriveFont(Font.BOLD, 13f));
            myLabels.put(theDir, theLabel);
            theGrid.add(theLabel);
        }

        add(theGrid, BorderLayout.CENTER);
    }

    /** Updates availability colors. */
    public void setAvailable(final boolean theNorth,
                             final boolean theSouth,
                             final boolean theEast,
                             final boolean theWest) {
        set("NORTH", theNorth);
        set("SOUTH", theSouth);
        set("EAST",  theEast);
        set("WEST",  theWest);
        revalidate();
        repaint();
    }

    private void set(final String theDir, final boolean theAvailable) {
        final JLabel theLabel = myLabels.get(theDir);
        if (theLabel != null) {
            theLabel.setBackground(theAvailable ? AVAILABLE_COLOR : UNAVAILABLE_COLOR);
        }
    }
}
