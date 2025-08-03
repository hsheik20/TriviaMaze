package Model;
/**
 * The {@code Player} class represents a player in the Trivia Maze game.
 * It maintains the player's position in the maze, score, and the number
 * of questions answered.
 *
 * The player can move through the maze if the move is valid, update their score,
 * and track progress through the game.
 *
 * @author Husein & Chan
 */
public class Player {
    /** X-coordinate of the player's position in the maze. */
    private int x;
    /** Y-coordinate of the player's position in the maze. */
    private int y;
    /** The player's current score. */
    private int score;
    /** Number of questions the player has answered. */
    private int questionsAnswered;
    /**
     * Constructs a new {@code Player} instance with the default starting position and zero score.
     */
    public Player() {
        resetPosition();
    }
    /**
     * Resets the player's position to the starting point (0,0),
     * and resets the score and number of answered questions.
     */
    public void resetPosition() {
        x = 0;
        y = 0;
        score = 0;
        questionsAnswered = 0;
    }
    /**
     * Attempts to move the player to a new position in the maze.
     * The move is only performed if the target position is valid.
     *
     * @param newX the new x-coordinate
     * @param newY the new y-coordinate
     * @param maze the maze object that validates the move
     * @return {@code true} if the move is valid and executed, {@code false} otherwise
     */
    public boolean move(int newX, int newY, Maze maze) {
        if (maze.isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            return true;
        }
        return false;
    }
    /**
     * @return the current x-coordinate of the player
     */
    public int getX() {
        return x;
    }
    /**
     * @return the current y-coordinate of the player
     */
    public int getY() {
        return y;
    }
    /**
     * @return the player's current score
     */
    public int getScore() {
        return score;
    }
    /**
     * @return the number of questions the player has answered
     */
    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    /**
     * Sets the player's x-coordinate.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Sets the player's y-coordinate.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * Adds points to the player's score.
     *
     * @param points the number of points to add
     */
    public void addScore(int points) {
        this.score += points;
    }
    /**
     * Increments the count of answered questions by one.
     */
    public void incrementQuestionsAnswered() {
        this.questionsAnswered++;
    }
}
