// View/MainMenuPanel.java
package View;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class MainMenuPanel extends JPanel {
    private Runnable onNewGame, onLoadGame, onInstructions, onAbout, onQuit;

    public MainMenuPanel(GameView view) {
        setLayout(new GridBagLayout());
        setBackground(new Color(92, 64, 51)); // brown background like the image

        // Title
        JLabel title = new JLabel("TRIVIA MAZE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        var box = new Box(BoxLayout.Y_AXIS);
        box.add(title);
        box.add(Box.createVerticalStrut(30));

        // Create buttons
        JButton newGame   = createMenuButton("NEW GAME");
        JButton load      = createMenuButton("LOAD GAME");
        JButton instruct  = createMenuButton("INSTRUCTIONS");
        JButton about     = createMenuButton("ABOUT");
        JButton quit      = createMenuButton("QUIT");

        // Add buttons with spacing
        for (var b : new JButton[]{newGame, load, instruct, about, quit}) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            box.add(b);
            box.add(Box.createVerticalStrut(15));
        }

        // Hook up actions to wiring
        newGame.addActionListener(e -> { if (onNewGame != null) onNewGame.run(); });
        load.addActionListener(e -> { if (onLoadGame != null) onLoadGame.run(); });
        instruct.addActionListener(e -> { if (onInstructions != null) onInstructions.run(); });
        about.addActionListener(e -> { if (onAbout != null) onAbout.run(); });
        quit.addActionListener(e -> { if (onQuit != null) onQuit.run(); });

        add(box);
    }

    /** Creates an orange arcade-style button */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 140, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 0), 4),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(260, 60));
        return button;
    }

    // Keep the original wiring methods
    public void onNewGame(Runnable r){ this.onNewGame = r; }
    public void onLoadGame(Runnable r){ this.onLoadGame = r; }
    public void onInstructions(Runnable r){ this.onInstructions = r; }
    public void onAbout(Runnable r){ this.onAbout = r; }
    public void onQuit(Runnable r){ this.onQuit = r; }
}
