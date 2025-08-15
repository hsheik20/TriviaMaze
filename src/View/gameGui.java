//
//package View;
//
//import Model.*;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//
///**
// * The {@code GameGUI} class provides the Swing-based user interface for the Trivia Maze Game.
// * This class serves as the View in the MVC pattern, observing the Game model and updating
// * the display accordingly.
// *
// * Responsibilities include:
// * - Displaying the game interface and maze
// * - Handling user input and forwarding to the model
// * - Updating the display based on model changes
// * - Managing difficulty selection and game flow
// *
// */
//
//public class GameGUI extends JFrame implements PropertyChangeListener {
//    /** The Game model instance this GUI observes. */
//    private Game myGame;
//    /** Main content panel for switching between different views. */
//    private JPanel myContentPanel;
//    /** Panel for difficulty selection interface. */
//    private JPanel myDifficultyPanel;
//    /** Panel for the main game interface. */
//    private JPanel myGamePanel;
//    /** Panel for displaying game information and controls. */
//    private JPanel myInfoPanel;
//    /** Label displaying current score. */
//    private JLabel myScoreLabel;
//    /** Label displaying hints remaining. */
//    private JLabel myHintsLabel;
//    /** Label displaying current difficulty. */
//    private JLabel myDifficultyLabel;
//    /** Label displaying player position. */
//    private JLabel myPositionLabel;
//    /** Text area for displaying game messages. */
//    private JTextArea myMessageArea;
//    /** Button for using hints. */
//    private JButton myHintButton;
//    /** Buttons for movement directions. */
//    private JButton myNorthButton, mySouthButton, myEastButton, myWestButton;
//
//    /** Default window dimensions. */
//    private static final int DEFAULT_WIDTH = 1000;
//    private static final int DEFAULT_HEIGHT = 700;
//    private static final String WINDOW_TITLE = "Trivia Maze Game";
//
//    /**
//     * Constructs a new GameGUI.
//     *
//     * @param theGame The Game model to observe and control.
//     */
//    public GameGUI(final Game theGame) {
//        super(WINDOW_TITLE);
//        myGame = theGame;
//        myGame.addListener(this); // Observer pattern
//
//        initializeWindow();
//        initializeComponents();
//        showDifficultySelection();
//    }
//
//    /**
//     * Initializes the main window properties.
//     */
//    private void initializeWindow() {
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//        setLocationRelativeTo(null);
//        setResizable(true);
//    }
//
//    /**
//     * Initializes all GUI components.
//     */
//    private void initializeComponents() {
//        // Create main content panel with CardLayout for switching views
//        myContentPanel = new JPanel(new CardLayout());
//        add(myContentPanel);
//
//        createMenuBar();
//        createDifficultySelectionPanel();
//        createGamePanel();
//
//        // Add panels to card layout
//        myContentPanel.add(myDifficultyPanel, "DIFFICULTY");
//        myContentPanel.add(myGamePanel, "GAME");
//    }
//
//    /**
//     * Creates the menu bar with game options.
//     */
//    private void createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        // Game Menu
//        JMenu gameMenu = new JMenu("Game");
//
//        JMenuItem newGameItem = new JMenuItem("New Game");
//        newGameItem.addActionListener(e -> showDifficultySelection());
//        gameMenu.add(newGameItem);
//
//        gameMenu.addSeparator();
//
//        JMenuItem exitItem = new JMenuItem("Exit");
//        exitItem.addActionListener(e -> System.exit(0));
//        gameMenu.add(exitItem);
//
//        // Difficulty Menu
//        JMenu difficultyMenu = new JMenu("Difficulty");
//
//        JMenuItem easyItem = new JMenuItem("Easy");
//        easyItem.addActionListener(e -> setDifficultyAndStart(DifficultyPresets.easy()));
//        difficultyMenu.add(easyItem);
//
//        JMenuItem normalItem = new JMenuItem("Normal");
//        normalItem.addActionListener(e -> setDifficultyAndStart(DifficultyPresets.normal()));
//        difficultyMenu.add(normalItem);
//
//        JMenuItem hardItem = new JMenuItem("Hard");
//        hardItem.addActionListener(e -> setDifficultyAndStart(DifficultyPresets.hard()));
//        difficultyMenu.add(hardItem);
//
//        // Info Menu
//        JMenu infoMenu = new JMenu("Info");
//
//        JMenuItem difficultyInfoItem = new JMenuItem("Current Difficulty");
//        difficultyInfoItem.addActionListener(e -> showDifficultyInfo());
//        infoMenu.add(difficultyInfoItem);
//
//        menuBar.add(gameMenu);
//        menuBar.add(difficultyMenu);
//        menuBar.add(infoMenu);
//
//        setJMenuBar(menuBar);
//    }
//
//    /**
//     * Creates the difficulty selection panel.
//     */
//    private void createDifficultySelectionPanel() {
//        myDifficultyPanel = new JPanel(new BorderLayout());
//        myDifficultyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Title
//        JLabel titleLabel = new JLabel("Select Difficulty Level", JLabel.CENTER);
//        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
//        myDifficultyPanel.add(titleLabel, BorderLayout.NORTH);
//
//        // Difficulty options
//        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
//        optionsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
//
//        // Create difficulty buttons
//        JButton easyButton = createDifficultyButton("Easy", DifficultyPresets.easy(),
//                "Small maze, no time limit, many hints");
//        JButton normalButton = createDifficultyButton("Normal", DifficultyPresets.normal(),
//                "Standard maze, moderate time limit");
//        JButton hardButton = createDifficultyButton("Hard", DifficultyPresets.hard(),
//                "Large maze, time pressure, fewer hints");
//        JButton expertButton = createDifficultyButton("Expert", DifficultyPresets.expert(),
//                "Very large maze, tight time limits, minimal assistance");
//
//        optionsPanel.add(easyButton);
//        optionsPanel.add(normalButton);
//        optionsPanel.add(hardButton);
//        optionsPanel.add(expertButton);
//
//        myDifficultyPanel.add(optionsPanel, BorderLayout.CENTER);
//
//        // Current difficulty info
//        JPanel infoPanel = new JPanel(new FlowLayout());
//        JLabel currentLabel = new JLabel("Current: " + myGame.getDifficultySettings().toString());
//        currentLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
//        infoPanel.add(currentLabel);
//
//        myDifficultyPanel.add(infoPanel, BorderLayout.SOUTH);
//    }
//
//    /**
//     * Creates a difficulty selection button.
//     *
//     * @param theName The difficulty name.
//     * @param theSettings The difficulty settings.
//     * @param theDescription Brief description.
//     * @return Configured difficulty button.
//     */
//    private JButton createDifficultyButton(final String theName,
//                                           final DifficultySettings theSettings,
//                                           final String theDescription) {
//        JButton button = new JButton();
//        button.setLayout(new BorderLayout());
//        button.setPreferredSize(new Dimension(220, 120));
//
//        // Name
//        JLabel nameLabel = new JLabel(theName, JLabel.CENTER);
//        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
//        button.add(nameLabel, BorderLayout.NORTH);
//
//        // Description
//        JLabel descLabel = new JLabel("<html><center>" + theDescription + "</center></html>");
//        descLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
//        button.add(descLabel, BorderLayout.CENTER);
//
//        // Settings info
//        String infoText = String.format("<html><center>%dx%d maze<br/>%d hints max</center></html>",
//                theSettings.getMazeWidth(), theSettings.getMazeHeight(), theSettings.getMaxHints());
//        JLabel infoLabel = new JLabel(infoText, JLabel.CENTER);
//        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
//        button.add(infoLabel, BorderLayout.SOUTH);
//
//        button.addActionListener(e -> setDifficultyAndStart(theSettings));
//        return button;
//    }
//
//    /**
//     * Creates the main game panel with controls and information display.
//     */
//    private void createGamePanel() {
//        myGamePanel = new JPanel(new BorderLayout());
//
//        // Info panel (top)
//        createInfoPanel();
//        myGamePanel.add(myInfoPanel, BorderLayout.NORTH);
//
//        // Game area (center) - placeholder for maze display
//        JPanel gameArea = new JPanel(new BorderLayout());
//        gameArea.setBorder(BorderFactory.createTitledBorder("Maze"));
//
//        JLabel mazeLabel = new JLabel("Maze visualization would go here", JLabel.CENTER);
//        mazeLabel.setPreferredSize(new Dimension(400, 300));
//        mazeLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        gameArea.add(mazeLabel, BorderLayout.CENTER);
//
//        myGamePanel.add(gameArea, BorderLayout.CENTER);
//
//        // Controls panel (right)
//        createControlsPanel();
//        myGamePanel.add(createControlsPanel(), BorderLayout.EAST);
//
//        // Message area (bottom)
//        createMessageArea();
//        myGamePanel.add(new JScrollPane(myMessageArea), BorderLayout.SOUTH);
//    }
//
//    /**
//     * Creates the information panel displaying game stats.
//     */
//    private void createInfoPanel() {
//        myInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        myInfoPanel.setBorder(BorderFactory.createEtchedBorder());
//
//        myScoreLabel = new JLabel("Score: 0");
//        myHintsLabel = new JLabel("Hints: " + myGame.getHintsRemaining());
//        myDifficultyLabel = new JLabel("Difficulty: " + myGame.getDifficultySettings().getDifficultyName());
//        myPositionLabel = new JLabel("Position: (0,0)");
//
//        myInfoPanel.add(myScoreLabel);
//        myInfoPanel.add(Box.createHorizontalStrut(20));
//        myInfoPanel.add(myHintsLabel);
//        myInfoPanel.add(Box.createHorizontalStrut(20));
//        myInfoPanel.add(myDifficultyLabel);
//        myInfoPanel.add(Box.createHorizontalStrut(20));
//        myInfoPanel.add(myPositionLabel);
//    }
//
//    /**
//     * Creates the controls panel with movement and action buttons.
//     *
//     * @return The controls panel.
//     */
//    private JPanel createControlsPanel() {
//        JPanel controlsPanel = new JPanel(new BorderLayout());
//        controlsPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
//        controlsPanel.setPreferredSize(new Dimension(200, 0));
//
//        // Movement buttons
//        JPanel movementPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        myNorthButton = new JButton("North");
//        mySouthButton = new JButton("South");
//        myEastButton = new JButton("East");
//        myWestButton = new JButton("West");
//
//        // Add action listeners for movement
//        myNorthButton.addActionListener(e -> myGame.attemptMove(Direction.NORTH));
//        mySouthButton.addActionListener(e -> myGame.attemptMove(Direction.SOUTH));
//        myEastButton.addActionListener(e -> myGame.attemptMove(Direction.EAST));
//        myWestButton.addActionListener(e -> myGame.attemptMove(Direction.WEST));
//
//        // Layout movement buttons in cross pattern
//        gbc.gridx = 1; gbc.gridy = 0;
//        movementPanel.add(myNorthButton, gbc);
//        gbc.gridx = 0; gbc.gridy = 1;
//        movementPanel.add(myWestButton, gbc);
//        gbc.gridx = 2; gbc.gridy = 1;
//        movementPanel.add(myEastButton, gbc);
//        gbc.gridx = 1; gbc.gridy = 2;
//        movementPanel.add(mySouthButton, gbc);
//
//        controlsPanel.add(movementPanel, BorderLayout.NORTH);
//
//        // Action buttons
//        JPanel actionPanel = new JPanel(new GridLayout(0, 1, 5, 5));
//
//        myHintButton = new JButton("Use Hint");
//        myHintButton.addActionListener(e -> useHint());
//        actionPanel.add(myHintButton);
//
//        JButton backButton = new JButton("Back to Menu");
//        backButton.addActionListener(e -> showDifficultySelection());
//        actionPanel.add(backButton);
//
//        controlsPanel.add(actionPanel, BorderLayout.CENTER);
//        return controlsPanel;
//    }
//
//    /**
//     * Creates the message area for displaying game events.
//     */
//    private void createMessageArea() {
//        myMessageArea = new JTextArea(4, 0);
//        myMessageArea.setEditable(false);
//        myMessageArea.setBorder(BorderFactory.createTitledBorder("Messages"));
//        myMessageArea.setText("Welcome to Trivia Maze! Select your difficulty and start playing.");
//    }
//
//    /**
//     * Sets difficulty and starts the game.
//     *
//     * @param theDifficulty The difficulty to set.
//     */
//    private void setDifficultyAndStart(final DifficultySettings theDifficulty) {
//        myGame.setDifficultySettings(theDifficulty);
//        showGamePanel();
//        addMessage("Game started with " + theDifficulty.getDifficultyName() + " difficulty!");
//    }
//
//    /**
//     * Shows the difficulty selection panel.
//     */
//    private void showDifficultySelection() {
//        CardLayout cl = (CardLayout) myContentPanel.getLayout();
//        cl.show(myContentPanel, "DIFFICULTY");
//    }
//
//    /**
//     * Shows the main game panel.
//     */
//    private void showGamePanel() {
//        updateGameInfo();
//        CardLayout cl = (CardLayout) myContentPanel.getLayout();
//        cl.show(myContentPanel, "GAME");
//    }
//
//    /**
//     * Shows detailed difficulty information dialog.
//     */
//    private void showDifficultyInfo() {
//        JOptionPane.showMessageDialog(this,
//                myGame.getDifficultySummary(),
//                "Difficulty Information",
//                JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    /**
//     * Handles hint usage.
//     */
//    private void useHint() {
//        if (myGame.useHint()) {
//            addMessage("Hint used! Hints remaining: " + myGame.getHintsRemaining());
//        } else {
//            addMessage("No hints available!");
//        }
//    }
//
//    /**
//     * Updates the game information display.
//     */
//    private void updateGameInfo() {
//        if (myGame.getPlayer() != null) {
//            myScoreLabel.setText("Score: " + myGame.getPlayer().getScore());
//            myPositionLabel.setText("Position: (" + myGame.getPlayer().getX() + "," + myGame.getPlayer().getY() + ")");
//        }
//        myHintsLabel.setText("Hints: " + myGame.getHintsRemaining());
//        myDifficultyLabel.setText("Difficulty: " + myGame.getDifficultySettings().getDifficultyName());
//        myHintButton.setEnabled(myGame.areHintsAvailable());
//    }
//
//    /**
//     * Adds a message to the message area.
//     *
//     * @param theMessage The message to add.
//     */
//    private void addMessage(final String theMessage) {
//        myMessageArea.append("\n" + theMessage);
//        myMessageArea.setCaretPosition(myMessageArea.getDocument().getLength());
//    }
//
//    /**
//     * Handles property change events from the Game model.
//     *
//     * @param evt The property change event.
//     */
//    @Override
//    public void propertyChange(final PropertyChangeEvent evt) {
//        SwingUtilities.invokeLater(() -> {
//            switch (evt.getPropertyName()) {
//                case "scoreChanged":
//                    updateGameInfo();
//                    break;
//                case "playerMoved":
//                    updateGameInfo();
//                    addMessage("Moved to new room!");
//                    break;
//                case "doorBlocked":
//                    addMessage("Wrong answer! Door blocked.");
//                    break;
//                case "hintUsed":
//                    updateGameInfo();
//                    break;
//                case "questionSkipped":
//                    addMessage("Question skipped.");
//                    break;
//                case "difficultySettings":
//                    updateGameInfo();
//                    addMessage("Difficulty changed to: " + myGame.getDifficultySettings().getDifficultyName());
//                    break;
//                case "askQuestion":
//                    handleQuestionRequest((QuestionRequest) evt.getNewValue());
//                    break;
//            }
//        });
//    }
//
//    /**
//     * Handles question request from the game model.
//     *
//     * @param theRequest The question request.
//     */
//    private void handleQuestionRequest(final QuestionRequest theRequest) {
//        // This would show a question dialog
//        // For now, simulate answering correctly
//        int result = JOptionPane.showConfirmDialog(this,
//                "Question: " + theRequest.getQuestion().getPrompt() + "\n\nAnswer correctly?",
//                "Trivia Question",
//                JOptionPane.YES_NO_OPTION);
//
//        myGame.handleAnswer(theRequest.getDoor(), result == JOptionPane.YES_OPTION);
//    }
//
//    /**
//     * Main method to launch the GUI application.
//     *
//     * @param theArgs Command line arguments.
//     */
//    public static void main(final String[] theArgs) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            // Create mock game components for demonstration
//            // In real implementation, these would be properly initialized
//            Maze maze = new Maze(); // Assuming default constructor exists
//            Player player = new Player(); // Assuming default constructor exists
//            GameStateManager gsm = new GameStateManager(null); // This would need proper initialization
//
//            Game game = new Game(maze, player, gsm);
//            new GameGUI(game).setVisible(true);
//        });
//    }
//}
