package View;

import javax.swing.*;
import java.awt.*;

public class PositionPanel extends JPanel {
    private final JLabel currentLbl  = new JLabel("Current: (0,0)", SwingConstants.CENTER);
    private final JLabel distanceLbl = new JLabel("To exit: —",     SwingConstants.CENTER);
    private final JLabel attemptsLbl = new JLabel("Attempts (door): —", SwingConstants.CENTER);

    public PositionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Position"));
        setBackground(Color.LIGHT_GRAY);

        JPanel center = new JPanel(new GridLayout(3, 1));
        center.setBackground(Color.LIGHT_GRAY);
        center.add(currentLbl);
        center.add(distanceLbl);
        center.add(attemptsLbl);

        add(center, BorderLayout.CENTER);
    }

    public void setPosition(int x, int y) {
        currentLbl.setText("Current: (" + x + "," + y + ")");
    }

    /** Pass a number as text, or "No path". */
    public void setDistanceText(String text) {
        distanceLbl.setText("To exit: " + text);
    }

    public void setAttempts(Integer attemptsLeftOrNull) {
        final String text = (attemptsLeftOrNull == null)
                ? "—"
                : (attemptsLeftOrNull == Integer.MAX_VALUE ? "∞" : attemptsLeftOrNull.toString());
        attemptsLbl.setText("Attempts (door): " + text);
    }
}
