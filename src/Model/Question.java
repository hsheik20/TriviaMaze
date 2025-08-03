package Model;

/**
 * This represents a trivia question with question prompt and optional hint.
 */
public abstract class Question {
    protected String prompt;
    protected Hint hint;

    /**
     * Constructs a new question
     * @param prompt the question prompt that will be displayed
     * @param hint the hint to help player
     */
    public Question(String prompt, Hint hint) {
        this.prompt = prompt;
        this.hint = hint;
    }

    /**
     * This returns question prompt
     */
    public String getPrompt() {
        return prompt;
    }
    /**
     * This returns hint associated with question
     */
    public Hint getHint() {
        return hint;
    }

    /**
     * This checks if answer given for question is correct
     * @param answer the player is answer
     * @return true if answer correct, false otherwise
     */
    public abstract boolean isCorrect(String answer);

    /**
     * This returns correct answer for question
     */
    public abstract String getCorrectAnswer();
}

