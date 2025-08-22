package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A Swing panel that serves as the main menu screen for the game.
 * This panel displays a title and a set of buttons for starting a new game,
 * loading a saved game, viewing instructions, reading about the game, and quitting.
 * It follows a listener-based design, where external components (the controller)
 * can "wire" actions to its buttons via setter methods.
 *
 * @author Husein
 */
public class MainMenuPanel extends JPanel {

    /**
     * A {@link Runnable} to be executed when the "New Game" button is clicked.
     * This action is typically set by the game controller.
     */
    private Runnable onNewGame;

    /**
     * A {@link Runnable} to be executed when the "Load Game" button is clicked.
     */
    private Runnable onLoadGame;

    /**
     * A {@link Runnable} to be executed when the "Instructions" button is clicked.
     */
    private Runnable onInstructions;

    /**
     * A {@link Runnable} to be executed when the "About" button is clicked.
     */
    private Runnable onAbout;

    /**
     * A {@link Runnable} to be executed when the "Quit" button is clicked.
     */
    private Runnable onQuit;

    /**
     * Constructs a {@code MainMenuPanel}.
     * It sets the panel's layout and background color and then initializes
     * and adds the main menu components (title and buttons).
     *
     * @param view The parent {@link GameView} frame. This parameter is used
     * to pass a reference to the main view, though it is not
     * directly used in the panel's current logic.
     */
    public MainMenuPanel(GameView view) {
        setLayout(new GridBagLayout());
        setBackground(new Color(92, 64, 51)); // brown background

        Box menuBox = createMainMenuBox();
        add(menuBox);
    }

    // =====================
    // == Helper Methods  ==
    // =====================

    /**
     * Creates and returns a vertical {@link Box} containing the title label and
     * all main menu buttons. This method encapsulates the full layout of the menu.
     *
     * @return A {@link Box} containing the menu components.
     */
    private Box createMainMenuBox() {
        Box box = new Box(BoxLayout.Y_AXIS);

        JLabel title = createTitleLabel();
        box.add(title);
        box.add(Box.createVerticalStrut(30));

        JButton newGameBtn  = createAndWireButton("NEW GAME", () -> runIfNotNull(onNewGame));
        JButton loadBtn     = createAndWireButton("LOAD GAME", () -> runIfNotNull(onLoadGame));
        JButton instructBtn = createAndWireButton("INSTRUCTIONS", () -> runIfNotNull(onInstructions));
        JButton aboutBtn    = createAndWireButton("ABOUT", () -> runIfNotNull(onAbout));
        JButton quitBtn     = createAndWireButton("QUIT", () -> runIfNotNull(onQuit));

        for (JButton b : new JButton[]{newGameBtn, loadBtn, instructBtn, aboutBtn, quitBtn}) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            box.add(b);
            box.add(Box.createVerticalStrut(15));
        }

        return box;
    }

    /**
     * Creates and returns the title label for the main menu.
     *
     * @return The "TRIVIA MAZE" title as a {@link JLabel}.
     */
    private JLabel createTitleLabel() {
        JLabel title = new JLabel("TRIVIA MAZE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        return title;
    }

    /**
     * A helper method to create a button, set its style, and attach an action listener.
     *
     * @param text   The text to display on the button.
     * @param action The {@link Runnable} action to execute when the button is clicked.
     * @return A styled and wired {@link JButton}.
     */
    private JButton createAndWireButton(String text, Runnable action) {
        JButton button = createMenuButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    /**
     * Creates a button with a consistent style used throughout the main menu.
     * This includes font, colors, borders, and a hover effect.
     *
     * @param text The text for the button.
     * @return A styled {@link JButton}.
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 140, 0));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(260, 60));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 0), 4),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        addHoverEffect(button, new Color(255, 140, 0), new Color(255, 170, 60));
        return button;
    }

    /**
     * Adds a simple hover effect to a button, changing its background and
     * foreground colors when the mouse enters or exits its bounds.
     *
     * @param button The {@link JButton} to apply the effect to.
     * @param normal The color to use when the button is not hovered.
     * @param hover  The color to use when the button is hovered.
     */
    private void addHoverEffect(JButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hover);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(normal);
                button.setForeground(Color.WHITE);
            }
        });
    }

    /**
     * A safe helper method to run a {@link Runnable} only if it is not null.
     *
     * @param r The {@link Runnable} to run.
     */
    private void runIfNotNull(Runnable r) {
        if (r != null) r.run();
    }

    // ======================
    // == Wiring Setters   ==
    // ======================

    /**
     * Sets the action to be performed when the "New Game" button is clicked.
     * This is the public API for the controller to wire its logic to the view.
     *
     * @param r The {@link Runnable} action for the "New Game" button.
     */
    public void onNewGame(Runnable r) {
        this.onNewGame = r;
    }

    /**
     * Sets the action for the "Load Game" button.
     *
     * @param r The {@link Runnable} action.
     */
    public void onLoadGame(Runnable r) {
        this.onLoadGame = r;
    }

    /**
     * Sets the action for the "Instructions" button.
     *
     * @param r The {@link Runnable} action.
     */
    public void onInstructions(Runnable r) {
        this.onInstructions = r;
    }

    /**
     * Sets the action for the "About" button.
     *
     * @param r The {@link Runnable} action.
     */
    public void onAbout(Runnable r) {
        this.onAbout = r;
    }

    /**
     * Sets the action for the "Quit" button.
     *
     * @param r The {@link Runnable} action.
     */
    public void onQuit(Runnable r) {
        this.onQuit = r;
    }
}