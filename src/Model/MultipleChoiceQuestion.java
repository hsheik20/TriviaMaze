package Model;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents a multiple choice trivia question. It shows a list of answer options,
 * and checks the player's choice by index
 */
public class MultipleChoiceQuestion extends Question {
    private final List<String> options;
    private final int correctIndex;

    /**
     * This constructs a new multiple choice question
     * @param prompt the text of question
     * @param options the list of answer choices
     * @param correctIndex the index of correct answer
     * @param hint the optional hint to display to player
     * @throws IllegalArgumentException if index out of bounds
     */
    public MultipleChoiceQuestion(String prompt, List<String> options, int correctIndex, Hint hint) {
        super(prompt, hint);
        this.options = options;
        this.correctIndex = correctIndex;
    }

    /**
     * This returns view of all the answer options
     */
    public List<String> getOptions() {
        return options;
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
            if (ansIndex < 0 || ansIndex > options.size()) {
                throw new IllegalArgumentException("Invalid index option for answer");
            }
            return ansIndex == correctIndex;
        } catch (NumberFormatException e) {
    return false;
        }
    }

    /**
     *This returns the correct answer
     */
    @Override
    public String getCorrectAnswer() {
        return options.get(correctIndex);
    }

}
