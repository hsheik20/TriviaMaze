// View/QuestionPanel.java
package View;

import Model.MultipleChoiceQuestion;
import Model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class QuestionPanel extends JPanel {
    private final JTextArea prompt = new JTextArea();
    private final JLabel attempts = new JLabel();
    private final JTextField answer = new JTextField(22);
    private final JButton submit = new JButton("Answer");
    private final JButton hint   = new JButton("Hint");
    private final JButton cheatBtn = new JButton("Cheat");
    private final JButton skip   = new JButton("Skip");

    // Inline area where we reveal the cheat
    private final JLabel cheatLbl = new JLabel("");

    private Consumer<String> onSubmit;
    private Runnable onHint, onSkip, onCheat;   // <-- declare onCheat

    public QuestionPanel(GameView view) {
        setLayout(new BorderLayout(10, 10));

        // Question text
        prompt.setLineWrap(true);
        prompt.setWrapStyleWord(true);
        prompt.setEditable(false);
        add(new JScrollPane(prompt), BorderLayout.CENTER);

        // Controls row (attempts + input + buttons)
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        controls.add(attempts);
        controls.add(answer);
        controls.add(submit);
        controls.add(hint);
        controls.add(cheatBtn);
        controls.add(skip);

        // Inline cheat label (above the controls)
        cheatLbl.setForeground(new Color(90, 90, 90));
        cheatLbl.setFont(cheatLbl.getFont().deriveFont(Font.ITALIC));

        JPanel cheatRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        cheatRow.add(cheatLbl);

        // SOUTH stack = cheatRow (top) + controls (bottom)
        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(cheatRow);
        south.add(controls);
        add(south, BorderLayout.SOUTH);

        // Actions
        submit.addActionListener(e -> { if (onSubmit != null) onSubmit.accept(answer.getText().trim()); });
        hint.addActionListener(e   -> { if (onHint   != null) onHint.run(); });
        cheatBtn.addActionListener(e-> { if (onCheat != null) onCheat.run(); });
        skip.addActionListener(e   -> { if (onSkip   != null) onSkip.run(); });
    }

    // inside QuestionPanel
    public void setQuestion(Question q, int attemptsLeft, boolean canHint, boolean canSkip) {
        // Build prompt text (add choices if MCQ)
        String text = q.getPrompt();
        if (q instanceof MultipleChoiceQuestion mcq) {
            StringBuilder sb = new StringBuilder(text).append("\n\n");
            var opts = mcq.getOptions(); // List<String>
            for (int i = 0; i < opts.size(); i++) {
                char letter = (char) ('A' + i);
                sb.append(letter).append(") ").append(opts.get(i)).append('\n');
            }
            text = sb.toString();
            answer.setToolTipText("Type A, B, C, or D");
        } else {
            answer.setToolTipText(null);
        }

        prompt.setText(text);
        attempts.setText("Attempts left: " + (attemptsLeft == Integer.MAX_VALUE ? "∞" : attemptsLeft));
        hint.setEnabled(canHint);
        skip.setEnabled(canSkip);
        answer.setText("");
        answer.requestFocusInWindow();
    }


    public void showHint(String text, int hintsLeft) {
        JOptionPane.showMessageDialog(
                this,
                "Hint: " + text + "  (Hints remaining: " + (hintsLeft==Integer.MAX_VALUE ? "∞" : hintsLeft) + ")"
        );
    }

    public void showWrongAndUpdate(final int attemptsLeft) {
        JOptionPane.showMessageDialog(
                this,
                "Wrong.\nAttempts left: " + (attemptsLeft == Integer.MAX_VALUE ? "∞" : attemptsLeft),
                "Incorrect",
                JOptionPane.ERROR_MESSAGE
        );
        attempts.setText("Attempts left: " + (attemptsLeft == Integer.MAX_VALUE ? "∞" : attemptsLeft));
        answer.setText("");
        answer.requestFocusInWindow();
    }

    // ----- NEW: display the correct answer inline -----
    public void showCheat(String correctAnswer) {
        cheatLbl.setText("Answer: " + correctAnswer);
        // Prefer a popup instead? Use:
        // JOptionPane.showMessageDialog(this, "Answer: " + correctAnswer, "Cheat", JOptionPane.WARNING_MESSAGE);
    }

    // callbacks
    public void onSubmit(Consumer<String> c){ this.onSubmit = c; }
    public void onHint(Runnable r){ this.onHint = r; }
    public void onSkip(Runnable r){ this.onSkip = r; }
    public void onCheat(Runnable r){ this.onCheat = r; }
}
