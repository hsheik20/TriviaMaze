package Model;

/**
 * This represents a fil in the blank trivia question. The player must type correct answer.
 */
public class FillInTheBlank extends Question {
    private final String myCorrectAnswer;

    /**
     * This constructs a new fill in the blank question
     * @param thePrompt the question prompt to display
     * @param theCorrectAnswer the correct answer
     * @param theHint the optional hint to show player
     */
    public FillInTheBlank(final String thePrompt, final  String theCorrectAnswer, final Hint theHint) {
        super(thePrompt, theHint);
        this.myCorrectAnswer = theCorrectAnswer.toLowerCase().trim();
    }

    /**
     * This checks if user answer is correct
     * @param theAnswer the user's typed answer
     * @return true if answer matches, false otherwise
     */
    @Override
    public boolean isCorrect(final String theAnswer) {
        return theAnswer != null && theAnswer.toLowerCase().trim().equals(myCorrectAnswer);
    }

    /**
     * This returns correct answer
     */
    @Override
    public String getCorrectAnswer() {
        return myCorrectAnswer;
    }

}
