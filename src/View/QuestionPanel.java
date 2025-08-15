// View/QuestionPanel.java
package View;

import Model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class QuestionPanel extends JPanel {
    private final JTextArea prompt = new JTextArea();
    private final JLabel attempts = new JLabel();
    private final JTextField answer = new JTextField(22);
    private final JButton submit = new JButton("Answer");
    private final JButton hint = new JButton("Hint");
    private final JButton skip = new JButton("Skip");

    private Consumer<String> onSubmit;
    private Runnable onHint, onSkip;

    public QuestionPanel(GameView view) {
        setLayout(new BorderLayout(10,10));
        prompt.setLineWrap(true); prompt.setWrapStyleWord(true); prompt.setEditable(false);
        add(new JScrollPane(prompt), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(attempts);
        bottom.add(answer);
        bottom.add(submit);
        bottom.add(hint);
        bottom.add(skip);
        add(bottom, BorderLayout.SOUTH);

        submit.addActionListener(e -> { if (onSubmit != null) onSubmit.accept(answer.getText()); });
        hint.addActionListener(e -> { if (onHint != null) onHint.run(); });
        skip.addActionListener(e -> { if (onSkip != null) onSkip.run(); });
    }

    public void setQuestion(Question q, int attemptsLeft, boolean canHint, boolean canSkip) {
        prompt.setText(q.getPrompt());
        attempts.setText("Attempts left: " + (attemptsLeft==Integer.MAX_VALUE ? "∞" : attemptsLeft));
        hint.setEnabled(canHint);
        skip.setEnabled(canSkip);
        answer.setText("");
        answer.requestFocusInWindow();
    }

    public void showHint(String text, int hintsLeft) {
        JOptionPane.showMessageDialog(this, "Hint: " + text +
                "  (Hints remaining: " + (hintsLeft==Integer.MAX_VALUE ? "∞" : hintsLeft) + ")");
    }
    // View/QuestionPanel.java

    public void showWrongAndUpdate(final int attemptsLeft) {
        // quick feedback
        JOptionPane.showMessageDialog(
                this,
                " Wrong.\nAttempts left: " +
                        (attemptsLeft == Integer.MAX_VALUE ? "∞" : attemptsLeft),
                "Incorrect",
                JOptionPane.ERROR_MESSAGE
        );

        // refresh the attempts label and ready the input again
        attempts.setText("Attempts left: " +
                (attemptsLeft == Integer.MAX_VALUE ? "∞" : attemptsLeft));
        answer.setText("");
        answer.requestFocusInWindow();
    }



    public void onSubmit(Consumer<String> c){ this.onSubmit = c; }
    public void onHint(Runnable r){ this.onHint = r; }
    public void onSkip(Runnable r){ this.onSkip = r; }
}
