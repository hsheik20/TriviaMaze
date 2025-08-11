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
 *
 * Initializing the main window and its core components.
 * Managing game state transitions through a {@link GameStateManager}.
 * Setting the system's look and feel for consistent UI design.
 * Managing difficulty settings and providing UI for difficulty selection.
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
    /** The current difficulty settings for the game. */
    private DifficultySettings myCurrentDifficulty;
    /** Panel for difficulty selection UI. */
    private JPanel myDifficultySelectionPanel;

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

        // Initialize default difficulty
        myCurrentDifficulty = DifficultyPresets.normal();

        // Initialize game state manager
        // Pass 'this' (the Game instance) to the GameStateManager if it needs to interact with the JFrame.
        myGameStateManager = new GameStateManager(this);

        initializeComponents();

        // Start with difficulty selection state
        myGameStateManager.setState(new DifficultySelectionState());
    }

    /**
     * Initializes the main UI components of the game window, such as the menu bar
     * and the content panel.
     */
    private void initializeComponents() {
        // Initialize the menu bar
        myMenuBar = new JMenuBar();
        createMenuBar();
        this.setJMenuBar(myMenuBar);

        // Initialize the main content panel
        myContentPanel = new JPanel();
        myContentPanel.setLayout(new BorderLayout());
        this.add(myContentPanel);

        // Initialize difficulty selection panel
        createDifficultySelectionPanel();

        // Show difficulty selection by default
        showDifficultySelection();
    }

    /**
     * Creates the menu bar with difficulty and game options.
     */
    private void createMenuBar() {
        // Game menu
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(theEvent -> startNewGame());
        gameMenu.add(newGameItem);

        gameMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(theEvent -> System.exit(0));
        gameMenu.add(exitItem);

        myMenuBar.add(gameMenu);

        // Difficulty menu
        JMenu difficultyMenu = new JMenu("Difficulty");

        // Add preset difficulties
        JMenuItem easyItem = new JMenuItem("Easy");
        easyItem.addActionListener(theEvent -> setDifficulty(DifficultyPresets.easy()));
        difficultyMenu.add(easyItem);

        JMenuItem normalItem = new JMenuItem("Normal");
        normalItem.addActionListener(theEvent -> setDifficulty(DifficultyPresets.normal()));
        difficultyMenu.add(normalItem);

        JMenuItem hardItem = new JMenuItem("Hard");
        hardItem.addActionListener(theEvent -> setDifficulty(DifficultyPresets.hard()));
        difficultyMenu.add(hardItem);

        JMenuItem expertItem = new JMenuItem("Expert");
        expertItem.addActionListener(theEvent -> setDifficulty(DifficultyPresets.expert()));
        difficultyMenu.add(expertItem);

        difficultyMenu.addSeparator();

        JMenuItem customItem = new JMenuItem("Custom Settings...");
        customItem.addActionListener(theEvent -> showCustomDifficultyDialog());
        difficultyMenu.add(customItem);

        JMenuItem selectDifficultyItem = new JMenuItem("Select Difficulty...");
        selectDifficultyItem.addActionListener(theEvent -> showDifficultySelection());
        difficultyMenu.add(selectDifficultyItem);

        myMenuBar.add(difficultyMenu);

        // Settings menu
        JMenu settingsMenu = new JMenu("Settings");

        JMenuItem difficultyInfoItem = new JMenuItem("Current Difficulty Info");
        difficultyInfoItem.addActionListener(theEvent -> showDifficultyInfo());
        settingsMenu.add(difficultyInfoItem);

        myMenuBar.add(settingsMenu);
    }

    /**
     * Creates the difficulty selection panel with preset options.
     */
    private void createDifficultySelectionPanel() {
        myDifficultySelectionPanel = new JPanel(new BorderLayout());
        myDifficultySelectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Select Difficulty Level", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        myDifficultySelectionPanel.add(titleLabel, BorderLayout.NORTH);

        // Difficulty options panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create difficulty buttons
        JButton easyButton = createDifficultyButton("Easy", DifficultyPresets.easy(),
                "Small maze, no time limit, many hints");
        JButton normalButton = createDifficultyButton("Normal", DifficultyPresets.normal(),
                "Standard maze, moderate time limit");
        JButton hardButton = createDifficultyButton("Hard", DifficultyPresets.hard(),
                "Large maze, time pressure, fewer hints");
        JButton expertButton = createDifficultyButton("Expert", DifficultyPresets.expert(),
                "Very large maze, tight time limits, minimal assistance");

        optionsPanel.add(easyButton);
        optionsPanel.add(normalButton);
        optionsPanel.add(hardButton);
        optionsPanel.add(expertButton);

        myDifficultySelectionPanel.add(optionsPanel, BorderLayout.CENTER);

        // Current difficulty display
        JPanel infoPanel = new JPanel(new FlowLayout());
        JLabel currentLabel = new JLabel("Current: " + myCurrentDifficulty.toString());
        currentLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        infoPanel.add(currentLabel);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.setPreferredSize(new Dimension(120, 30));
        startGameButton.addActionListener(theEvent -> startGameWithCurrentDifficulty());
        infoPanel.add(startGameButton);

        myDifficultySelectionPanel.add(infoPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a difficulty selection button with description.
     *
     * @param theName The name of the difficulty level.
     * @param theSettings The difficulty settings.
     * @param theDescription Brief description of the difficulty.
     * @return A configured JButton for difficulty selection.
     */
    private JButton createDifficultyButton(final String theName,
                                           final DifficultySettings theSettings,
                                           final String theDescription) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(250, 120));

        // Name label
        JLabel nameLabel = new JLabel(theName, JLabel.CENTER);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button.add(nameLabel, BorderLayout.NORTH);

        // Description label
        JLabel descLabel = new JLabel("<html><center>" + theDescription + "</center></html>", JLabel.CENTER);
        descLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        button.add(descLabel, BorderLayout.CENTER);

        // Settings info
        String infoText = String.format("<html><center>%dx%d maze<br/>%d hints max</center></html>",
                theSettings.getMazeWidth(), theSettings.getMazeHeight(), theSettings.getMaxHints());
        JLabel infoLabel = new JLabel(infoText, JLabel.CENTER);
        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        button.add(infoLabel, BorderLayout.SOUTH);

        // Add action listener
        button.addActionListener(theEvent -> {
            setDifficulty(theSettings);
            updateDifficultyDisplay();
        });

        return button;
    }

    /**
     * Sets the current difficulty and updates the game state manager.
     *
     * @param theSettings The new difficulty settings to apply.
     */
    public void setDifficulty(final DifficultySettings theSettings) {
        myCurrentDifficulty = theSettings;
        myGameStateManager.setDifficultySettings(theSettings);

        // Show confirmation message
        JOptionPane.showMessageDialog(this,
                "Difficulty set to: " + theSettings.getDifficultyName() + "\n" +
                        theSettings.toString(),
                "Difficulty Changed",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows the difficulty selection panel.
     */
    public void showDifficultySelection() {
        myContentPanel.removeAll();
        myContentPanel.add(myDifficultySelectionPanel, BorderLayout.CENTER);
        updateDifficultyDisplay();
        myContentPanel.revalidate();
        myContentPanel.repaint();
    }

    /**
     * Updates the difficulty display in the selection panel.
     */
    private void updateDifficultyDisplay() {
        // Find and update the current difficulty label
        Component[] components = myDifficultySelectionPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Component[] subComponents = panel.getComponents();
                for (Component subComp : subComponents) {
                    if (subComp instanceof JLabel) {
                        JLabel label = (JLabel) subComp;
                        if (label.getText().startsWith("Current:")) {
                            label.setText("Current: " + myCurrentDifficulty.toString());
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Shows a dialog with detailed information about the current difficulty.
     */
    private void showDifficultyInfo() {
        String info = String.format(
                "Current Difficulty: %s\n\n" +
                        "Maze Size: %dx%d\n" +
                        "Wall Density: %.0f%%\n" +
                        "Door Density: %.0f%%\n" +
                        "Time Limit: %s\n" +
                        "Max Hints: %d\n" +
                        "Correct Answer Points: %d\n" +
                        "Wrong Answer Penalty: %d\n" +
                        "Hint Penalty: %d\n" +
                        "Skip Question Penalty: %d\n" +
                        "Allow Skipping: %s\n" +
                        "Question Difficulty Range: %d-%d",
                myCurrentDifficulty.getDifficultyName(),
                myCurrentDifficulty.getMazeWidth(),
                myCurrentDifficulty.getMazeHeight(),
                myCurrentDifficulty.getWallDensity() * 100,
                myCurrentDifficulty.getDoorDensity() * 100,
                myCurrentDifficulty.hasTimeLimit() ? myCurrentDifficulty.getTimeLimit() + " seconds" : "None",
                myCurrentDifficulty.getMaxHints(),
                myCurrentDifficulty.getCorrectAnswerPoints(),
                myCurrentDifficulty.getWrongAnswerPenalty(),
                myCurrentDifficulty.getHintPenalty(),
                myCurrentDifficulty.getSkipQuestionPenalty(),
                myCurrentDifficulty.isAllowSkipping() ? "Yes" : "No",
                myCurrentDifficulty.getQuestionDifficultyMin(),
                myCurrentDifficulty.getQuestionDifficultyMax()
        );

        JOptionPane.showMessageDialog(this, info, "Difficulty Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a dialog for creating custom difficulty settings.
     */
    private void showCustomDifficultyDialog() {
        // This could be expanded into a full custom difficulty editor
        String[] options = {"Easy Base", "Normal Base", "Hard Base", "Expert Base"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose a base difficulty to customize:",
                "Custom Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (choice >= 0) {
            DifficultySettings baseSettings;
            switch (choice) {
                case 0: baseSettings = DifficultyPresets.easy(); break;
                case 1: baseSettings = DifficultyPresets.normal(); break;
                case 2: baseSettings = DifficultyPresets.hard(); break;
                case 3: baseSettings = DifficultyPresets.expert(); break;
                default: baseSettings = DifficultyPresets.normal();
            }

            // For now, just use the base settings
            // In a full implementation, you'd show a detailed customization dialog
            setDifficulty(baseSettings);
            JOptionPane.showMessageDialog(this,
                    "Custom difficulty based on " + baseSettings.getDifficultyName() + " created.\n" +
                            "Full customization dialog would be implemented here.",
                    "Custom Difficulty",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Starts a new game with difficulty selection.
     */
    private void startNewGame() {
        showDifficultySelection();
    }

    /**
     * Starts the game with the current difficulty settings.
     */
    private void startGameWithCurrentDifficulty() {
        // Clear the content panel and show game panel
        myContentPanel.removeAll();

        // Create a simple game started panel (placeholder for actual game UI)
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(new JLabel("Game Started with " + myCurrentDifficulty.getDifficultyName() + " difficulty!",
                JLabel.CENTER), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Difficulty Selection");
        backButton.addActionListener(theEvent -> showDifficultySelection());
        gamePanel.add(backButton, BorderLayout.SOUTH);

        myContentPanel.add(gamePanel, BorderLayout.CENTER);
        myContentPanel.revalidate();
        myContentPanel.repaint();

        // Here you would typically transition to the actual game state
        // myGameStateManager.setState(new GamePlayState());
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
     * Gets the current difficulty settings.
     *
     * @return The current {@link DifficultySettings} instance.
     */
    public DifficultySettings getCurrentDifficulty() {
        return myCurrentDifficulty;
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