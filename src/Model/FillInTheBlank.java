package Model;

/**
 * Represents a fill-in-the-blank trivia question.
 * The player must type the exact correct answer to solve it. This class extends
 * the abstract {@link Question} class, providing specific implementations for
 * checking the correctness of an answer and retrieving the correct answer.
 *
 * @author Husein
 */
public class FillInTheBlank extends Question {

    /** The correct answer to the question, stored in lowercase for case-insensitive comparison. */
    private final String myCorrectAnswer;

    /**
     * Constructs a new fill-in-the-blank question.
     *
     * @param thePrompt        The question prompt to display to the player.
     * @param theCorrectAnswer The correct answer string. It will be stored in lowercase
     * and trimmed of leading/trailing whitespace.
     * @param theHint          An optional {@link Hint} to show the player. Can be null.
     */
    public FillInTheBlank(final String thePrompt, final String theCorrectAnswer, final Hint theHint) {
        super(thePrompt, theHint);
        this.myCorrectAnswer = theCorrectAnswer.toLowerCase().trim();
    }

    /**
     * Checks if the user's typed answer is correct.
     * The comparison is case-insensitive and ignores leading/trailing whitespace.
     *
     * @param theAnswer The user's typed answer.
     * @return {@code true} if the answer is correct; {@code false} otherwise.
     */
    @Override
    public boolean isCorrect(final String theAnswer) {
        return theAnswer != null && theAnswer.toLowerCase().trim().equals(myCorrectAnswer);
    }

    /**
     * Returns the correct answer for this question.
     *
     * @return The correct answer string.
     */
    @Override
    public String getCorrectAnswer() {
        return myCorrectAnswer;
    }
}