// View/PausePanel.java
package View;

import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {
    private Runnable onResume, onMainMenu, onQuit;

    public PausePanel(final GameView view) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245,245,245));

        JButton resume   = new JButton("Resume");
        JButton mainMenu = new JButton("Main Menu");
        JButton quit     = new JButton("Quit");

        // Wire the buttons to the callbacks
        resume.addActionListener(e -> { if (onResume   != null) onResume.run(); });
        mainMenu.addActionListener(e -> { if (onMainMenu != null) onMainMenu.run(); });
        quit.addActionListener(e -> { if (onQuit     != null) onQuit.run(); });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        add(resume,   gbc);
        add(mainMenu, gbc);
        add(quit,     gbc);
    }

    // Callbacks the controller sets
    public void onResume(Runnable r)   { this.onResume = r; }
    public void onMainMenu(Runnable r) { this.onMainMenu = r; }
    public void onQuit(Runnable r)     { this.onQuit = r; }
}
