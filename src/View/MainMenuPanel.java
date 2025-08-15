// View/MainMenuPanel.java
package View;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class MainMenuPanel extends JPanel {
    private Runnable onNewGame, onLoadGame, onInstructions, onAbout, onQuit;

    public MainMenuPanel(GameView view) {
        setLayout(new GridBagLayout());
        var box = new Box(BoxLayout.Y_AXIS);

        JLabel title = new JLabel("TRIVIA  MAZE", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 36f));
        title.setAlignmentX(CENTER_ALIGNMENT);
        box.add(title);
        box.add(Box.createVerticalStrut(20));

        JButton newGame = new JButton("New Game");
        JButton load    = new JButton("Load Game");
        JButton instruct= new JButton("Instructions");
        JButton about   = new JButton("About");
        JButton quit    = new JButton("Quit");

        for (var b : new JButton[]{newGame, load, instruct, about, quit}) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(240, 40));
            box.add(b);
            box.add(Box.createVerticalStrut(10));
        }

        newGame.addActionListener(e -> { if (onNewGame != null) onNewGame.run(); });
        load.addActionListener(e -> { if (onLoadGame != null) onLoadGame.run(); });
        instruct.addActionListener(e -> { if (onInstructions != null) onInstructions.run(); });
        about.addActionListener(e -> { if (onAbout != null) onAbout.run(); });
        quit.addActionListener(e -> { if (onQuit != null) onQuit.run(); });

        add(box);
    }

    public void onNewGame(Runnable r){ this.onNewGame = r; }
    public void onLoadGame(Runnable r){ this.onLoadGame = r; }
    public void onInstructions(Runnable r){ this.onInstructions = r; }
    public void onAbout(Runnable r){ this.onAbout = r; }
    public void onQuit(Runnable r){ this.onQuit = r; }
}
