package View;
import javax.swing.*;
import java.awt.*;

public class DirectionPanel extends JPanel {
    public DirectionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Available"));
        setBackground(Color.LIGHT_GRAY);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        String[] directions = {"NORTH", "SOUTH", "EAST", "WEST"};
        for (String dir : directions) {
            JButton btn = new JButton(dir);
            btn.setEnabled(false); // Start disabled by default
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }
}