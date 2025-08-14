package Model;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code Player} class represents a player in the Trivia Maze game.
 * It maintains the player's logical position (coordinates), score, and the number
 * of questions answered.
 *
 * The player can attempt to move through the maze, update their score,
 * and track progress through the game. The actual maze traversal logic
 * is handled by the {@link Maze} class.
 *
 * @author Husein & Chan
 */
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** X-coordinate (row) of the player's position in the maze. */
    private int myX;
    /** Y-coordinate (column) of the player's position in the maze. */
    private int myY;
    /** The player's current score. */
    private int myScore;
    /** Number of questions the player has answered. */
    private int myQuestionsAnswered;

    /**
     * Constructs a new {@code Player} instance with the default starting position (0,0)
     * and zero score.
     */
    public Player() {
        resetPosition();
    }

    /**
     * Resets the player's logical position to the starting point (0,0),
     * and resets the score and number of answered questions.
     * Note: This method only updates the player's internal coordinates and score.
     * The maze's internal current position must be reset separately using {@link Maze#reset()}.
     */
    public void resetPosition() {
        myX = 0;
        myY = 0;
        myScore = 0;
        myQuestionsAnswered = 0;
    }

    /**
     * Attempts to move the player in a specified direction within the maze.
     * This method delegates the actual movement and validation to the provided {@link Maze} object.
     * If the move is successful, the player's internal coordinates (myX, myY) are updated
     * to reflect the new room's coordinates in the maze.
     *
     * @param theDirection The compass direction (NORTH, EAST, SOUTH, WEST) to attempt to move.
     * @param theMaze The maze object that validates and performs the move.
     * @return {@code true} if the move was valid and executed by the maze, {@code false} otherwise.
     * @throws IllegalArgumentException if theMaze is null.
     */
    public boolean move(final Direction theDirection, final Maze theMaze) {
        if (theMaze == null) {
            throw new IllegalArgumentException("The maze object cannot be null for player movement.");
        }

        final boolean moveSuccessful = theMaze.canMove(theDirection);
        if (moveSuccessful) {
            // Update player's internal coordinates to match the maze's current room
            myX = theMaze.getCurrentRoom().getRow();
            myY = theMaze.getCurrentRoom().getCol();
        }
        return moveSuccessful;
    }

    /**
     * @return the current x-coordinate (row) of the player.
     */
    public int getX() {
        return myX;
    }

    /**
     * @return the current y-coordinate (column) of the player.
     */
    public int getY() {
        return myY;
    }

    /**
     * @return the player's current score.
     */
    public int getScore() {
        return myScore;
    }

    /**
     * @return the number of questions the player has answered.
     */
    public int getQuestionsAnswered() {
        return myQuestionsAnswered;
    }

    /**
     * Sets the player's x-coordinate (row).
     *
     * @param theX The new x-coordinate.
     */
    public void setX(final int theX) {
        this.myX = theX;
    }

    /**
     * Sets the player's y-coordinate (column).
     *
     * @param theY The new y-coordinate.
     */
    public void setY(final int theY) {
        this.myY = theY;
    }

    /**
     * Adds points to the player's score.
     *
     * @param thePoints The number of points to add.
     * @throws IllegalArgumentException if thePoints is negative.
     */
    public void addScore(final int thePoints) {
        if (thePoints < 0) {
            throw new IllegalArgumentException("Points to add cannot be negative.");
        }
        this.myScore += thePoints;
    }

    /**
     * Increments the count of answered questions by one.
     */
    public void incrementQuestionsAnswered() {
        this.myQuestionsAnswered++;
    }
}