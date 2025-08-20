package View;

import Model.MultipleChoiceQuestion;
import Model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * This represents the question panel which displays trivia questions, answer input field to submit answer
 * and actions to display a hint, display the answer using cheat, and skipping a question.
 *
 * Setter methods used to wire controller call backs
 */
public class QuestionPanel extends JPanel {

    /** Multiline area displaying prompt for question */
    private final JTextArea myPromptArea = new JTextArea();
    /** Label showing remaining attempts. */
    private final JLabel myAttemptsLabel = new JLabel();
    /** Single-line answer field to answer question. */
    private final JTextField myAnswerField = new JTextField(22);
    /** Button to submit current answer. */
    private final JButton mySubmitButton = new JButton("Answer");
    /** Button to request a hint (if allowed). */
    private final JButton myHintButton = new JButton("Hint");
    /** Button to reveal the answer (cheat). */
    private final JButton myCheatButton = new JButton("Cheat");
    /** Button to skip the question (if allowed). */
    private final JButton mySkipButton = new JButton("Skip");
    /** Label to display cheat. */
    private final JLabel myCheatLabel = new JLabel("");

    /** Callback invoked on submit with the user's answer string. */
    private Consumer<String> myOnSubmit;
    /** Callback invoked when hint user requests a hint. */
    private Runnable myOnHint;
    /** Callback invoked when hint user requests to skip question */
    private Runnable myOnSkip;
    /** Callback invoked when hint user requests to use a cheat */
    private Runnable myOnCheat;

    /**
     * Constructs a QuestionPanel with prompt area in CENTER and controls in SOUTH.
     *
     * @param theView the GameView associated with this panel (not used directly; kept for symmetry)
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

        // Actions
        mySubmitButton.addActionListener(e -> {
            if (myOnSubmit != null) myOnSubmit.accept(myAnswerField.getText().trim());
        });
        myHintButton.addActionListener(e -> runIfNotNull(myOnHint));
        myCheatButton.addActionListener(e -> runIfNotNull(myOnCheat));
        mySkipButton.addActionListener(e -> runIfNotNull(myOnSkip));
    }

    /**
     * This populates the panel with the given question and UI state.
     *
     * @param theQuestion      the question to display
     * @param theAttemptsLeft  remaining attempts
     * @param theCanHint       whether the Hint button should be enabled
     * @param theCanSkip       whether the Skip button should be enabled
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
     * This shows a hint message dialog and indicates remaining hints.
     *
     * @param theText      the hint text
     * @param theHintsLeft remaining hints
     */
    public void showHint(final String theText, final int theHintsLeft) {
        JOptionPane.showMessageDialog(
                this,
                "Hint: " + theText + "  (Hints remaining: " + (theHintsLeft == Integer.MAX_VALUE ? "∞" : theHintsLeft) + ")"
        );
    }

    /**
     * This shows an "Incorrect" dialog, updates attempts readout, and resets the answer field.
     *
     * @param theAttemptsLeft remaining attempts
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
     * This displays correct answer
     *
     * @param theCorrectAnswer the correct answer text to reveal
     */
    public void showCheat(final String theCorrectAnswer) {
        myCheatLabel.setText("Answer: " + theCorrectAnswer);
    }

    /** This assigns the submit handler (receives answer string). */
    public void setOnSubmit(final Consumer<String> theHandler) { myOnSubmit = theHandler; }

    /** This assigns the hint handler. */
    public void setOnHint(final Runnable theHandler) { myOnHint = theHandler; }

    /** This assigns the skip handler. */
    public void setOnSkip(final Runnable theHandler) { myOnSkip = theHandler; }

    /** This assigns the cheat handler. */
    public void setOnCheat(final Runnable theHandler) { myOnCheat = theHandler; }

    /** This runs a task if non-null. */
    private void runIfNotNull(final Runnable theAction) { if (theAction != null) theAction.run(); }
}
