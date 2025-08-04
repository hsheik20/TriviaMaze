package Model; // Assuming Game is part of the Model package, adjust if it's in a different package like View or Controller.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; // Although not directly used in the provided snippet, often needed for UI.

/**
 * The {@code Game} class represents the main window for the Trivia Maze Game.
 * It sets up the user interface, initializes game states, and launches the application.
 * This class extends {@link JFrame} and serves as the entry point of the game.
 *
 * Responsibilities include:
 * <ul>
 * <li>Initializing the main window and its core components.</li>
 * <li>Managing game state transitions through a {@link GameStateManager}.</li>
 * <li>Setting the system's look and feel for consistent UI design.</li>
 * </ul>
 *
 * @author Husein & Chan
 */
public class Game extends JFrame { // Explicitly extend JFrame
    /** Manages the various game states and transitions between them. */
    private GameStateManager myGameStateManager;
    /** Represents the top menu bar of the application. */
    private JMenuBar myMenuBar; // Changed to JMenuBar for Swing components
    /** The main content panel where UI components are placed. */
    private JPanel myContentPanel;

    /**
     * The default width of the game window.
     */
    private static final int DEFAULT_WIDTH = 1200;
    /**
     * The default height of the game window.
     */
    private static final int DEFAULT_HEIGHT = 800;
    /**
     * The title of the game window.
     */
    private static final String GAME_TITLE = "Trivia Maze Game";

    /**
     * Constructs a new {@code Game} instance.
     * Sets up the window properties and initializes game components.
     */
    public Game() {
        // Set window properties
        super(GAME_TITLE); // Call JFrame constructor with title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setLocationRelativeTo(null); // Center the window on screen

        // Initialize game state manager
        // Pass 'this' (the Game instance) to the GameStateManager if it needs to interact with the JFrame.
        myGameStateManager = new GameStateManager(this);
        initializeComponents();
    }

    /**
     * Initializes the main UI components of the game window, such as the menu bar
     * and the content panel.
     */
    private void initializeComponents() {
        // Initialize the menu bar (example, actual implementation would be in MenuBar class)
        myMenuBar = new JMenuBar(); // Use JMenuBar for Swing
        // Assuming MenuBar class will return a JMenuBar or be directly JMenuBar
        // For now, just create a basic one.
        // If you have a custom MenuBar class, it might look like:
        // myMenuBar = new MenuBar(); // if MenuBar extends JMenuBar

        // Example menu item
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(theEvent -> System.exit(0)); // Use lambda for action listener
        fileMenu.add(exitItem);
        myMenuBar.add(fileMenu);
        this.setJMenuBar(myMenuBar); // Set the menu bar for the JFrame

        // Initialize the main content panel
        myContentPanel = new JPanel();
        myContentPanel.setLayout(new BorderLayout()); // Example layout
        this.add(myContentPanel); // Add the content panel to the JFrame

        // The GameStateManager would then be responsible for adding specific game panels
        // to myContentPanel based on the current game state (e.g., main menu, game play, etc.).
    }

    /**
     * Returns the game state manager instance.
     *
     * @return The {@link GameStateManager} currently managing the game states.
     */
    public GameStateManager getGameStateManager() {
        return myGameStateManager;
    }

    /**
     * Returns the main content panel of the game window.
     *
     * @return The {@link JPanel} serving as the main content area.
     */
    public JPanel getContentPanel() {
        return myContentPanel;
    }

    /**
     * Main method that launches the Trivia Maze Game application.
     * Sets the system look and feel, then creates and displays the main game window.
     *
     * @param theArgs Command-line arguments (not used in this application).
     */
    public static void main(final String[] theArgs) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set native look and feel for the operating system
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (final ClassNotFoundException | InstantiationException |
                           IllegalAccessException | UnsupportedLookAndFeelException e) {
                // Print stack trace for debugging, but consider more user-friendly error handling
                // in a production application.
                e.printStackTrace();
            }
            // Create and display the game window
            new Game().setVisible(true);
        });
    }
}
