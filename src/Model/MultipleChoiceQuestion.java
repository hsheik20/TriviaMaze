package Model;
import java.util.List;

/**
 * This represents a multiple choice trivia question. It shows a list of answer options,
 * and checks the player's choice by index
 */
public class MultipleChoiceQuestion extends Question {
    private final List<String> myOptions;
    private final int myCorrectIndex;

    /**
     * This constructs a new multiple choice question
     * @param thePrompt the text of question
     * @param theOptions the list of answer choices
     * @param theCorrectIndex the index of correct answer
     * @param theHint optional hint to display to player
     * @throws IllegalArgumentException if index out of bounds
     */
    public MultipleChoiceQuestion(final String thePrompt, final List<String> theOptions, final int theCorrectIndex, final Hint theHint) {
        super(thePrompt, theHint);
        this.myOptions = theOptions;
        this.myCorrectIndex = theCorrectIndex;
    }

    /**
     * This returns view of all the answer options
     */
    public List<String> getOptions() {
        return myOptions;
    }

    /**
     * This parses player's answer as integer index and checks if it is correct index
     * @param answer the user's answer as string
     * @return true if parsed index matches correct index, false otherwise
     */
    @Override
    public boolean isCorrect(String answer) {
        try{
            int ansIndex = Integer.parseInt(answer);
            if (ansIndex < 0 || ansIndex > myOptions.size()) {
                throw new IllegalArgumentException("Invalid index option for answer");
            }
            return ansIndex == myCorrectIndex;
        } catch (NumberFormatException e) {
    return false;
        }
    }

    /**
     *This returns the correct answer
     */
    @Override
    public String getCorrectAnswer() {
        return myOptions.get(myCorrectIndex);
    }

}
