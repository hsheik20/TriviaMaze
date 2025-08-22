package Model;

/**
 * This represents a true/false trivia question.
 *
 * @author Husein & Chan
 */
public class TrueFalseQuestion extends Question {
    private final boolean myCorrectAnswer;

    /**
     * This constructs a new true/false question
     * @param prompt the question prompt to be displayed
     * @param correctAnswer the boolean value of correct answer
     * @param hint the optional hint to show player
     */
    public TrueFalseQuestion(String prompt, boolean correctAnswer, Hint hint) {
        super(prompt, hint);
        this.myCorrectAnswer = correctAnswer;
    }

    /**
     * This checks if user's answer is correct
     * @param theAnswer the user's answer, expected to be true or false
     * @return true if parsed value matches, false otherwise
     */
    @Override
    public boolean isCorrect(final String theAnswer) {
        if (theAnswer == null) return false;

        String answer = theAnswer.trim().toLowerCase();

        boolean parsedAnswer;
        if (answer.equals("t") || answer.equals("true")) {
            parsedAnswer = true;
        } else if (answer.equals("f") || answer.equals("false")) {
            parsedAnswer = false;
        } else {
            // Optional: Reject unrecognized input
            return false;
        }

        return parsedAnswer == myCorrectAnswer;
    }

    public String getCorrectAnswer() {
        return myCorrectAnswer ? "True" : "False";
    }

    /**
     * This returns correct answer as string
     */
    @Override
    public String cheatToken() {
        return myCorrectAnswer ? "T" : "F";
    }



}
