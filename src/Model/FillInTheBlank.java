package Model;

/**
 * This represents a fil in the blank trivia question. The player must type correct answer.
 */
public class FillInTheBlank extends Question {
    private final String correctAnswer;

    /**
     * This constructs a new fill in the blank question
     * @param prompt the question prompt to display
     * @param correctAnswer the correct answer
     * @param hint the optional hint to show player
     */
    public FillInTheBlank(String prompt, String correctAnswer, Hint hint) {
        super(prompt, hint);
        this.correctAnswer = correctAnswer.toLowerCase().trim();
    }

    /**
     * This checks if user answer is correct
     * @param answer the user's typed answer
     * @return true if answer matches, false otherwise
     */
    @Override
    public boolean isCorrect(String answer) {
        return answer != null && answer.toLowerCase().trim().equals(correctAnswer);
    }

    /**
     * This returns correct answer
     */
    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

}
