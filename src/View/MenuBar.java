// View/GameMenuBar.java
package View;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    // callbacks (wired by controller)
    private Runnable onNewGame, onPauseToggle, onSaveGame, onQuitGame;

    // keep refs so you can update labels/enabled state
    private final JMenuItem miNew   = new JMenuItem("New Game");
    private final JMenuItem miPause = new JMenuItem("Pause Game");
    private final JMenuItem miSave  = new JMenuItem("Save Game");
    private final JMenuItem miQuit  = new JMenuItem("Quit Game");

    public MenuBar() {
        JMenu game = new JMenu("Game");

        // click handlers only (no accelerators)
        miNew.addActionListener(e -> { if (onNewGame     != null) onNewGame.run(); });
        miPause.addActionListener(e -> { if (onPauseToggle!= null) onPauseToggle.run(); });
        miSave.addActionListener(e -> { if (onSaveGame    != null) onSaveGame.run(); });
        miQuit.addActionListener(e -> { if (onQuitGame    != null) onQuitGame.run(); });

        game.add(miNew);
        game.add(miPause);
        game.add(miSave);
        game.addSeparator();
        game.add(miQuit);

        add(game);
    }

    /* ---------- wiring API ---------- */
    public void onNewGame(Runnable r)      { this.onNewGame = r; }
    public void onPauseToggle(Runnable r)  { this.onPauseToggle = r; }
    public void onSaveGame(Runnable r)     { this.onSaveGame = r; }
    public void onQuitGame(Runnable r)     { this.onQuitGame = r; }

    /* ---------- optional helpers ---------- */
    /** Update Pause/Resume label based on current state. */
    public void setPaused(boolean paused) {
        miPause.setText(paused ? "Resume Game" : "Pause Game");
    }

    /** Enable/disable Save (e.g., before a game starts). */
    public void setSaveEnabled(boolean enabled) {
        miSave.setEnabled(enabled);
    }
}
