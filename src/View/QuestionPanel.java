package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel for displaying trivia questions and handling user responses.
 * Supports multiple choice questions with hints and timer functionality.
 *
 * @author Generated for Trivia Maze
 */
public class QuestionPanel extends JPanel {

    private static final Font QUESTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private static final Font ANSWER_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private static final Font INFO_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 12);
    private static final Color BACKGROUND_COLOR = new Color(248, 248, 255);
    private static final Color QUESTION_COLOR = new Color(25, 25, 112);
    private static final Color CORRECT_COLOR = new Color(34, 139, 34);
    private static final Color INCORRECT_COLOR = new Color(220, 20, 60);

    private Question myCurrentQuestion;
    private Door myCurrentDoor;
    private Game myGame;
    private ActionListener myAnswerHandler;

    // UI Components
    private JLabel myQuestionLabel;
    private JTextArea myQuestionTextArea;
    private ButtonGroup myAnswerButtonGroup;
    private JRadioButton[] myAnswerButtons;
    private JButton mySubmitButton;
    private JButton myHintButton;
    private JButton mySkipButton;
    private JLabel myTimerLabel;
    private JLabel myHintLabel;
    private JProgressBar myTimerProgressBar;

    // Timer functionality
    private Timer myQuestionTimer;
    private int myTimeRemaining;
    private int myTotalTime;

    // State tracking
    private boolean myAnswerSubmitted;
    private boolean myHintUsed;

    /**
     * Constructs the question panel.
     *
     * @param game The game instance
     */
    public QuestionPanel(Game game) {
        myGame = game;
        myAnswerButtons = new JRadioButton[4]; // Assuming max 4 multiple choice options
        myAnswerSubmitted = false;
        myHintUsed = false;

        initializePanel();
        createComponents();
        layoutComponents();
        setupTimer();
    }

    /**
     * Initializes the panel's basic properties.
     */
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(500, 400));
    }

    /**
     * Creates all UI components.
     */
    private void createComponents() {
        // Question display
        myQuestionLabel = new JLabel("Trivia Question");
        myQuestionLabel.setFont(QUESTION_FONT);
        myQuestionLabel.setForeground(QUESTION_COLOR);
        myQuestionLabel.setHorizontalAlignment(JLabel.CENTER);

        myQuestionTextArea = new JTextArea();
        myQuestionTextArea.setFont(ANSWER_FONT);
        myQuestionTextArea.setBackground(BACKGROUND_COLOR);
        myQuestionTextArea.setEditable(false);
        myQuestionTextArea.setWrapStyleWord(true);
        myQuestionTextArea.setLineWrap(true);
        myQuestionTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Answer options
        myAnswerButtonGroup = new ButtonGroup();
        for (int i = 0; i < myAnswerButtons.length; i++) {
            myAnswerButtons[i] = new JRadioButton();
            myAnswerButtons[i].setFont(ANSWER_FONT);
            myAnswerButtons[i].setBackground(BACKGROUND_COLOR);
            myAnswerButtons[i].setVisible(false);
            myAnswerButtonGroup.add(myAnswerButtons[i]);
        }

        // Control buttons
        mySubmitButton = createStyledButton("Submit Answer");
        myHintButton = createStyledButton("Use Hint");
        mySkipButton = createStyledButton("Skip Question");

        // Timer components
        myTimerLabel = new JLabel("Time: 30s");
        myTimerLabel.setFont(INFO_FONT);
        myTimerLabel.setHorizontalAlignment(JLabel.CENTER);

        myTimerProgressBar = new JProgressBar(0, 100);
        myTimerProgressBar.setStringPainted(true);
        myTimerProgressBar.setString("Time Remaining");
        myTimerProgressBar.setForeground(new Color(70, 130, 180));

        // Hint display
        myHintLabel = new JLabel();
        myHintLabel.setFont(INFO_FONT);
        myHintLabel.setForeground(Color.BLUE);
        myHintLabel.setHorizontalAlignment(JLabel.CENTER);
        myHintLabel.setVisible(false);
    }

    /**
     * Creates a styled button with consistent appearance.
     *
     * @param text The button text
     * @return A styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(ANSWER_FONT);
        button.setPreferredSize(new Dimension(120, 35));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(button.getBackground().brighter());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(70, 130, 180));
                }
            }
        });

        return button;
    }

    /**
     * Lays out all components in the panel.
     */
    private void layoutComponents() {
        // Top panel - Question and timer
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(myQuestionLabel, BorderLayout.NORTH);

        JScrollPane questionScrollPane = new JScrollPane(myQuestionTextArea);
        questionScrollPane.setPreferredSize(new Dimension(450, 80));
        questionScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        topPanel.add(questionScrollPane, BorderLayout.CENTER);

        // Timer panel
        JPanel timerPanel = new JPanel(new BorderLayout(5, 5));
        timerPanel.setBackground(BACKGROUND_COLOR);
        timerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        timerPanel.add(myTimerLabel, BorderLayout.NORTH);
        timerPanel.add(myTimerProgressBar, BorderLayout.CENTER);
        topPanel.add(timerPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Center panel - Answer options
        JPanel answerPanel = new JPanel(new GridBagLayout());
        answerPanel.setBackground(BACKGROUND_COLOR);
        answerPanel.setBorder(BorderFactory.createTitledBorder("Answer Options"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        for (int i = 0; i < myAnswerButtons.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            answerPanel.add(myAnswerButtons[i], gbc);
        }

        add(answerPanel, BorderLayout.CENTER);

        // Bottom panel - Controls and hint
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(mySubmitButton);
        buttonPanel.add(myHintButton);
        buttonPanel.add(mySkipButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(myHintLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Setup button actions
        setupButtonActions();
    }

    /**
     * Sets up button action listeners.
     */
    private void setupButtonActions() {
        mySubmitButton.addActionListener(e -> handleSubmitAnswer());
        myHintButton.addActionListener(e -> handleUseHint());
        mySkipButton.addActionListener(e -> handleSkipQuestion());
    }

    /**
     * Sets up the question timer.
     */
    private void setupTimer() {
        myQuestionTimer = new Timer(1000, e -> updateTimer());
    }

    /**
     * Displays a new question.
     *
     * @param question The question to display
     * @param door The door associated with this question
     */
    public void displayQuestion(Question question, Door door) {
        myCurrentQuestion = question;
        myCurrentDoor = door;
        myAnswerSubmitted = false;
        myHintUsed = false;

        if (question == null) {
            clearDisplay();
            return;
        }

        // Set question text
        myQuestionTextArea.setText(question.getPrompt());

        // Set up answer options
        setupAnswerOptions(question);

        // Reset UI state
        resetUIState();

        // Start timer if enabled
        if (myGame.getDifficultySettings().hasTimeLimit()) {
            startTimer();
        } else {
            hideTimer();
        }

        // Update button states
        updateButtonStates();

        setVisible(true);
    }

    /**
     * Sets up the answer options for the current question.
     *
     * @param question The question with answer options
     */
    private void setupAnswerOptions(Question question) {
        // Hide all answer buttons first
        for (JRadioButton button : myAnswerButtons) {
            button.setVisible(false);
            button.setSelected(false);
        }

        // Get answer options (this would depend on your Question class implementation)
        // For demonstration, assuming we have methods to get options
        List<String> options = getQuestionOptions(question);

        // Display available options
        for (int i = 0; i < Math.min(options.size(), myAnswerButtons.length); i++) {
            myAnswerButtons[i].setText(options.get(i));
            myAnswerButtons[i].setVisible(true);
            myAnswerButtons[i].setEnabled(true);
            myAnswerButtons[i].setForeground(Color.BLACK);
        }
    }

    /**
     * Gets the answer options for a question.
     * This is a placeholder method that would need to be implemented based on your Question class.
     *
     * @param question The question
     * @return List of answer options
     */
    private List<String> getQuestionOptions(Question question) {
        // This is a placeholder - implement based on your Question class structure
        // For now, returning sample options
        List<String> options = new ArrayList<>();
        options.add("Option A");
        options.add("Option B");
        options.add("Option C");
        options.add("Option D");
        return options;
    }

    /**
     * Resets the UI state for a new question.
     */
    private void resetUIState() {
        myAnswerButtonGroup.clearSelection();
        myHintLabel.setVisible(false);
        myHintLabel.setText("");

        for (JRadioButton button : myAnswerButtons) {
            button.setForeground(Color.BLACK);
            button.setEnabled(true);
        }
    }

    /**
     * Updates button states based on game state.
     */
    private void updateButtonStates() {
        mySubmitButton.setEnabled(!myAnswerSubmitted);
        myHintButton.setEnabled(!myHintUsed && myGame.areHintsAvailable());
        mySkipButton.setEnabled(!myAnswerSubmitted && myGame.isSkippingAllowed());
    }

    /**
     * Starts the question timer.
     */
    private void startTimer() {
        myTotalTime = myGame.getDifficultySettings().getQuestionTimeLimit();
        myTimeRemaining = myTotalTime;
        myTimerProgressBar.setValue(100);
        myTimerLabel.setText("Time: " + myTimeRemaining + "s");
        myTimerProgressBar.setVisible(true);
        myTimerLabel.setVisible(true);
        myQuestionTimer.start();
    }

    /**
     * Hides the timer components.
     */
    private void hideTimer() {
        myTimerProgressBar.setVisible(false);
        myTimerLabel.setVisible(false);
        if (myQuestionTimer.isRunning()) {
            myQuestionTimer.stop();
        }
    }

    /**
     * Updates the timer display and handles timeout.
     */
    private void updateTimer() {
        myTimeRemaining--;
        myTimerLabel.setText("Time: " + myTimeRemaining + "s");

        int progress = (int) ((double) myTimeRemaining / myTotalTime * 100);
        myTimerProgressBar.setValue(progress);

        // Change color as time runs out
        if (myTimeRemaining <= 5) {
            myTimerProgressBar.setForeground(Color.RED);
            myTimerLabel.setForeground(Color.RED);
        } else if (myTimeRemaining <= 10) {
            myTimerProgressBar.setForeground(Color.ORANGE);
            myTimerLabel.setForeground(Color.ORANGE);
        }

        if (myTimeRemaining <= 0) {
            handleTimeout();
        }
    }

    /**
     * Handles timeout when timer reaches zero.
     */
    private void handleTimeout() {
        myQuestionTimer.stop();
        myAnswerSubmitted = true;
        updateButtonStates();

        // Disable all answer options
        for (JRadioButton button : myAnswerButtons) {
            button.setEnabled(false);
        }

        // Notify that time is up (treat as wrong answer)
        if (myAnswerHandler != null) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "TIMEOUT");
            myAnswerHandler.actionPerformed(event);
        }

        showTimeoutMessage();
    }

    /**
     * Shows a timeout message.
     */
    private void showTimeoutMessage() {
        JOptionPane.showMessageDialog(this,
                "Time's up! The question will be marked as incorrect.",
                "Time Expired",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Handles submit answer button click.
     */
    private void handleSubmitAnswer() {
        if (myAnswerSubmitted) return;

        // Check if an answer is selected
        if (myAnswerButtonGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select an answer before submitting.",
                    "No Answer Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Stop timer
        if (myQuestionTimer.isRunning()) {
            myQuestionTimer.stop();
        }

        // Get selected answer
        String selectedAnswer = getSelectedAnswer();
        boolean isCorrect = checkAnswer(selectedAnswer);

        myAnswerSubmitted = true;
        updateButtonStates();

        // Highlight correct/incorrect answers
        highlightAnswers(isCorrect);

        // Notify answer handler
        if (myAnswerHandler != null) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                    isCorrect ? "CORRECT" : "INCORRECT");
            myAnswerHandler.actionPerformed(event);
        }

        // Show result message
        showAnswerResult(isCorrect);
    }

    /**
     * Gets the currently selected answer text.
     *
     * @return The selected answer text, or null if none selected
     */
    private String getSelectedAnswer() {
        for (JRadioButton button : myAnswerButtons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    /**
     * Checks if the selected answer is correct.
     *
     * @param selectedAnswer The selected answer
     * @return True if correct, false otherwise
     */
    private boolean checkAnswer(String selectedAnswer) {
        if (myCurrentQuestion == null || selectedAnswer == null) {
            return false;
        }

        // This would depend on your Question class implementation
        // For demonstration, assuming first option is always correct
        return selectedAnswer.equals("Option A");
    }

    /**
     * Highlights the correct and incorrect answers.
     *
     * @param userWasCorrect Whether the user's answer was correct
     */
    private void highlightAnswers(boolean userWasCorrect) {
        for (JRadioButton button : myAnswerButtons) {
            button.setEnabled(false);

            if (button.getText().equals("Option A")) { // Assuming Option A is correct
                button.setForeground(CORRECT_COLOR);
                if (userWasCorrect && button.isSelected()) {
                    button.setBackground(CORRECT_COLOR.brighter());
                }
            } else if (button.isSelected() && !userWasCorrect) {
                button.setForeground(INCORRECT_COLOR);
                button.setBackground(INCORRECT_COLOR.brighter());
            }
        }
    }

    /**
     * Shows the answer result message.
     *
     * @param correct Whether the answer was correct
     */
    private void showAnswerResult(boolean correct) {
        String message = correct ? "Correct! Well done!" : "Incorrect. The correct answer is highlighted.";
        String title = correct ? "Correct Answer" : "Incorrect Answer";
        int messageType = correct ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    /**
     * Handles hint button click.
     */
    private void handleUseHint() {
        if (myHintUsed || !myGame.areHintsAvailable()) return;

        if (myGame.useHint()) {
            myHintUsed = true;
            String hint = getQuestionHint();
            myHintLabel.setText("Hint: " + hint);
            myHintLabel.setVisible(true);
            updateButtonStates();
        }
    }

    /**
     * Gets a hint for the current question.
     *
     * @return The hint text
     */
    private String getQuestionHint() {
        // This would depend on your Question class implementation
        // For demonstration, returning a generic hint
        return "Consider the first option carefully.";
    }

    /**
     * Handles skip question button click.
     */
    private void handleSkipQuestion() {
        if (myAnswerSubmitted || !myGame.isSkippingAllowed()) return;

        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to skip this question? This may block the door.",
                "Skip Question",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            myAnswerSubmitted = true;

            // Stop timer
            if (myQuestionTimer.isRunning()) {
                myQuestionTimer.stop();
            }

            updateButtonStates();

            // Notify skip handler
            if (myAnswerHandler != null) {
                ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "SKIP");
                myAnswerHandler.actionPerformed(event);
            }
        }
    }

    /**
     * Clears the question display.
     */
    public void clearDisplay() {
        myCurrentQuestion = null;
        myCurrentDoor = null;
        myQuestionTextArea.setText("");

        for (JRadioButton button : myAnswerButtons) {
            button.setVisible(false);
            button.setSelected(false);
        }

        myHintLabel.setVisible(false);
        hideTimer();
        setVisible(false);
    }

    /**
     * Sets the answer handler for processing user responses.
     *
     * @param handler The action listener to handle answers
     */
    public void setAnswerHandler(ActionListener handler) {
        myAnswerHandler = handler;
    }

    /**
     * Gets the current question being displayed.
     *
     * @return The current question, or null if none
     */
    public Question getCurrentQuestion() {
        return myCurrentQuestion;
    }

    /**
     * Gets the door associated with the current question.
     *
     * @return The current door, or null if none
     */
    public Door getCurrentDoor() {
        return myCurrentDoor;
    }

    /**
     * Checks if an answer has been submitted for the current question.
     *
     * @return True if answer submitted, false otherwise
     */
    public boolean isAnswerSubmitted() {
        return myAnswerSubmitted;
    }

    /**
     * Enables or disables the timer functionality.
     *
     * @param enabled Whether timer should be enabled
     */
    public void setTimerEnabled(boolean enabled) {
        if (!enabled && myQuestionTimer.isRunning()) {
            myQuestionTimer.stop();
            hideTimer();
        }
    }

    /**
     * Updates the display with current game state.
     */
    public void updateDisplay() {
        updateButtonStates();
        repaint();
    }
}