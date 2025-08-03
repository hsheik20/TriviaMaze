package Model;

/**
 * The {@code Trivia} class is responsible for generating and managing
 * trivia questions within the game. It acts as a bridge between the game
 * logic and the {@link QuestionFactory}, which creates questions.
 *
 * This class maintains the current active {@link Question} that the player
 * is attempting to answer.
 *
 * Typically used during gameplay to provide a random question when the player
 * attempts to move through the maze.
 *
 * @author Husein & Chan
 */
class Trivia {
    /** Factory used to create random trivia questions. */
    private QuestionFactory questionFactory;
    /** The current question being presented to the player. */
    private Question currentQuestion;
    /**
     * Constructs a new {@code Trivia} instance and initializes
     * the {@link QuestionFactory}.
     */
    public Trivia() {
        this.questionFactory = new QuestionFactory();
    }
    /**
     * Generates a new random trivia question using the factory
     * and stores it as the current question.
     *
     * @return the newly generated {@link Question}
     */
    public Question generateQuestion() {
        currentQuestion = questionFactory.createRandomQuestion();
        return currentQuestion;
    }
    /**
     * Returns the current active question.
     *
     * @return the current {@link Question}
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }
}
