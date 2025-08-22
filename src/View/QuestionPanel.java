package View;

import Model.MultipleChoiceQuestion;
import Model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A Swing panel that displays a trivia question and provides controls for
 * answering, getting a hint, skipping, or cheating.
 * This panel is responsible for presenting the question to the user and
 * capturing their input, which is then handled by a controller via a set of
 * public callback setters.
 *
 * @author Husein & Chan
 */
public class QuestionPanel extends JPanel {

    /** Multiline text area for displaying the question prompt. */
    private final JTextArea myPromptArea = new JTextArea();

    /** Label to show the number of remaining attempts for the current question. */
    private final JLabel myAttemptsLabel = new JLabel();

    /** A single-line text field where the user can type their answer. */
    private final JTextField myAnswerField = new JTextField(22);

    /** Button to submit the user's answer. */
    private final JButton mySubmitButton = new JButton("Answer");

    /** Button to request a hint, enabled only if hints are available. */
    private final JButton myHintButton = new JButton("Hint");

    /** Button to reveal the correct answer (a cheat). */
    private final JButton myCheatButton = new JButton("Cheat");

    /** Button to skip the current question, enabled only if skipping is allowed. */
    private final JButton mySkipButton = new JButton("Skip");

    /** Label to display the correct answer when the user cheats. */
    private final JLabel myCheatLabel = new JLabel("");

    /**
     * A callback invoked with the user's answer string when the "Answer" button is clicked.
     */
    private Consumer<String> myOnSubmit;

    /**
     * A callback invoked when the user requests a hint.
     */
    private Runnable myOnHint;

    /**
     * A callback invoked when the user requests to skip the question.
     */
    private Runnable myOnSkip;

    /**
     * A callback invoked when the user requests to see the correct answer (cheat).
     */
    private Runnable myOnCheat;

    /**
     * Constructs a {@code QuestionPanel}.
     * It sets up the layout, initializes all UI components (labels, text areas, buttons),
     * and wires the button actions to their corresponding internal callbacks.
     *
     * @param theView The parent {@link GameView} associated with this panel.
     */
    public QuestionPanel(final GameView theView) {
        setLayout(new BorderLayout(10, 10));

        // Prompt (CENTER)
        myPromptArea.setLineWrap(true);
        myPromptArea.setWrapStyleWord(true);
        myPromptArea.setEditable(false);
        add(new JScrollPane(myPromptArea), BorderLayout.CENTER);

        // Inline cheat label (above controls)
        myCheatLabel.setForeground(new Color(90, 90, 90));
        myCheatLabel.setFont(myCheatLabel.getFont().deriveFont(Font.ITALIC));
        final JPanel theCheatRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        theCheatRow.add(myCheatLabel);

        // Controls row (attempts + input + buttons)
        final JPanel theControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        theControls.add(myAttemptsLabel);
        theControls.add(myAnswerField);
        theControls.add(mySubmitButton);
        theControls.add(myHintButton);
        theControls.add(myCheatButton);
        theControls.add(mySkipButton);

        final JPanel theSouth = new JPanel();
        theSouth.setLayout(new BoxLayout(theSouth, BoxLayout.Y_AXIS));
        theSouth.add(theCheatRow);
        theSouth.add(theControls);
        add(theSouth, BorderLayout.SOUTH);

        // Wire actions to buttons
        mySubmitButton.addActionListener(e -> {
            if (myOnSubmit != null) myOnSubmit.accept(myAnswerField.getText().trim());
        });
        myHintButton.addActionListener(e -> runIfNotNull(myOnHint));
        myCheatButton.addActionListener(e -> runIfNotNull(myOnCheat));
        mySkipButton.addActionListener(e -> runIfNotNull(myOnSkip));
    }

    /**
     * Populates the panel with a new question and updates the UI elements
     * according to the current game state (attempts, hint/skip availability).
     *
     * @param theQuestion     The {@link Question} to be displayed.
     * @param theAttemptsLeft The number of remaining attempts for this question.
     * @param theCanHint      {@code true} if the hint button should be enabled.
     * @param theCanSkip      {@code true} if the skip button should be enabled.
     */
    public void setQuestion(final Question theQuestion,
                            final int theAttemptsLeft,
                            final boolean theCanHint,
                            final boolean theCanSkip) {

        String theText = theQuestion.getPrompt();
        if (theQuestion instanceof final MultipleChoiceQuestion theMcq) {
            final StringBuilder theSb = new StringBuilder(theText).append("\n\n");
            final var theOptions = theMcq.getOptions();
            for (int i = 0; i < theOptions.size(); i++) {
                final char theLetter = (char) ('A' + i);
                theSb.append(theLetter).append(") ").append(theOptions.get(i)).append('\n');
            }
            theText = theSb.toString();
            myAnswerField.setToolTipText("Type A, B, C, or D");
        } else {
            myAnswerField.setToolTipText(null);
        }

        myPromptArea.setText(theText);
        myAttemptsLabel.setText("Attempts left: " + (theAttemptsLeft == Integer.MAX_VALUE ? "∞" : theAttemptsLeft));
        myHintButton.setEnabled(theCanHint);
        mySkipButton.setEnabled(theCanSkip);
        myAnswerField.setText("");
        myAnswerField.requestFocusInWindow();
    }

    /**
     * Displays a hint message to the user in a pop-up dialog.
     *
     * @param theText      The hint text to display.
     * @param theHintsLeft The number of hints remaining.
     */
    public void showHint(final String theText, final int theHintsLeft) {
        JOptionPane.showMessageDialog(
                this,
                "Hint: " + theText + "  (Hints remaining: " + (theHintsLeft == Integer.MAX_VALUE ? "∞" : theHintsLeft) + ")"
        );
    }

    /**
     * Notifies the user that their answer was incorrect via a pop-up dialog,
     * and updates the attempts counter on the panel.
     *
     * @param theAttemptsLeft The number of remaining attempts.
     */
    public void showWrongAndUpdate(final int theAttemptsLeft) {
        JOptionPane.showMessageDialog(
                this,
                "Wrong.\nAttempts left: " + (theAttemptsLeft == Integer.MAX_VALUE ? "∞" : theAttemptsLeft),
                "Incorrect",
                JOptionPane.ERROR_MESSAGE
        );
        myAttemptsLabel.setText("Attempts left: " + (theAttemptsLeft == Integer.MAX_VALUE ? "∞" : theAttemptsLeft));
        myAnswerField.setText("");
        myAnswerField.requestFocusInWindow();
    }

    /**
     * Displays the correct answer on the panel's cheat label.
     *
     * @param theCorrectAnswer The correct answer string to reveal.
     */
    public void showCheat(final String theCorrectAnswer) {
        myCheatLabel.setText("Answer: " + theCorrectAnswer);
    }

    // ======================
    // == Wiring Setters   ==
    // ======================

    /**
     * Sets the handler for the "Answer" button.
     *
     * @param theHandler A consumer that receives the user's answer string.
     */
    public void setOnSubmit(final Consumer<String> theHandler) { this.myOnSubmit = theHandler; }

    /**
     * Sets the handler for the "Hint" button.
     *
     * @param theHandler The action to run when a hint is requested.
     */
    public void setOnHint(final Runnable theHandler) { this.myOnHint = theHandler; }

    /**
     * Sets the handler for the "Skip" button.
     *
     * @param theHandler The action to run when a skip is requested.
     */
    public void setOnSkip(final Runnable theHandler) { this.myOnSkip = theHandler; }

    /**
     * Sets the handler for the "Cheat" button.
     *
     * @param theHandler The action to run when a cheat is requested.
     */
    public void setOnCheat(final Runnable theHandler) { this.myOnCheat = theHandler; }

    /**
     * A helper method to safely run a {@link Runnable} only if it is not null.
     *
     * @param theAction The action to execute.
     */
    private void runIfNotNull(final Runnable theAction) { if (theAction != null) theAction.run(); }
}