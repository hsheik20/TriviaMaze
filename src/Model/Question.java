package Model;

/**
 * This abstract class represents a trivia question with a question prompt and an optional hint.
 * Subclasses will implement the logic for checking correctness and providing the correct answer.
 */
public abstract class Question {
    /** The question prompt that will be displayed to the player. */
    protected final String myPrompt;
    /** The hint associated with the question to help the player, can be null. */
    protected final Hint myHint;

    /**
     * Constructs a new question with the specified prompt and hint.
     *
     * @param thePrompt The question prompt that will be displayed. Cannot be null or empty.
     * @param theHint The hint to help the player. Can be null if no hint is available.
     * @throws IllegalArgumentException if thePrompt is null or empty.
     */
    public Question(final String thePrompt, final Hint theHint) {
        if (thePrompt == null || thePrompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Question prompt cannot be null or empty.");
        }
        this.myPrompt = thePrompt;
        this.myHint = theHint;
    }

    /**
     * Returns the question prompt.
     *
     * @return The question prompt.
     */
    public String getPrompt() {
        return myPrompt;
    }

    /**
     * Returns the hint associated with this question.
     *
     * @return The hint object, or null if no hint is provided.
     */
    public Hint getHint() {
        return myHint;
    }

    /**
     * Checks if the given answer for this question is correct.
     * This method must be implemented by concrete subclasses.
     *
     * @param theAnswer The player's answer to be checked.
     * @return True if the answer is correct, false otherwise.
     */
    public abstract boolean isCorrect(final String theAnswer);

    /**
     * Returns the correct answer for this question.
     * This method must be implemented by concrete subclasses.
     *
     * @return The correct answer as a String.
     */
    public abstract String getCorrectAnswer();
}
