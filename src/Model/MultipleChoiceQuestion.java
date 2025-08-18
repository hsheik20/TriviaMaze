package Model;

import java.util.List;

/**
 * Multiple-choice question that validates answers strictly as letters A/B/C/D.
 * The correct answer index is 0-based (0 -> A, 1 -> B, 2 -> C, 3 -> D).
 */
public class MultipleChoiceQuestion extends Question {
    private final List<String> myOptions;   // immutable copy
    private final int myCorrectIndex;       // 0-based

    public MultipleChoiceQuestion(final String thePrompt,
                                  final List<String> theOptions,
                                  final int theCorrectIndex,
                                  final Hint theHint) {
        super(thePrompt, theHint);
        if (theOptions == null || theOptions.isEmpty()) {
            throw new IllegalArgumentException("Options cannot be null or empty.");
        }
        if (theCorrectIndex < 0 || theCorrectIndex >= theOptions.size()) {
            throw new IllegalArgumentException("Correct index out of range.");
        }
        this.myOptions = List.copyOf(theOptions);
        this.myCorrectIndex = theCorrectIndex;
    }

    /** Returns an immutable view of the options. */
    public List<String> getOptions() {
        return myOptions;
    }

    /** Accept only letters A/B/C/D (case-insensitive). */
    @Override
    public boolean isCorrect(final String answer) {
        if (answer == null) return false;
        final String a = answer.trim();
        if (a.length() != 1) return false;

        final char ch = Character.toUpperCase(a.charAt(0));
        final int idx = ch - 'A';            // A->0, B->1, C->2, D->3
        return idx >= 0 && idx < myOptions.size() && idx == myCorrectIndex;
    }

    /** Returns the correct option TEXT (kept for compatibility with existing UI). */
    @Override
    public String getCorrectAnswer() {
        return myOptions.get(myCorrectIndex);
    }

    /** Convenience: returns the correct letter, e.g., 'C'. */
    public char getCorrectLetter() {
        return (char) ('A' + myCorrectIndex);
    }

    // Model/MultipleChoiceQuestion.java
    @Override
    public String cheatToken() {
        return String.valueOf(getCorrectLetter());   // e.g., "C"
    }


}
