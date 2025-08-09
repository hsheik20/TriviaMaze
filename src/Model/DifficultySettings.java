// DifficultySettings.java
package Model;

import java.io.Serializable;

/**
 * The {@code DifficultySettings} class represents immutable game difficulty configuration.
 * Uses the Builder pattern to create flexible, customizable difficulty settings.
 *
 * @author Chan
 */
public class DifficultySettings implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;

    // Game settings fields
    /** The display name of this difficulty level. */
    private final String myDifficultyName;
    /** The width of the maze in cells. */
    private final int myMazeWidth;
    /** The height of the maze in cells. */
    private final int myMazeHeight;
    /** The density of walls in the maze (0.0 to 1.0). */
    private final double myWallDensity;
    /** The density of doors in the maze (0.0 to 1.0). */
    private final double myDoorDensity;
    /** Time limit per question in seconds, 0 = no limit. */
    private final int myTimeLimit;
    /** Maximum number of hints allowed per game. */
    private final int myMaxHints;
    /** Points awarded for correct answers. */
    private final int myCorrectAnswerPoints;
    /** Points deducted for wrong answers. */
    private final int myWrongAnswerPenalty;
    /** Points deducted for using hints. */
    private final int myHintPenalty;
    /** Points deducted for skipping questions. */
    private final int mySkipQuestionPenalty;
    /** Whether players are allowed to skip questions. */
    private final boolean myAllowSkipping;
    /** Minimum difficulty level for questions. */
    private final int myQuestionDifficultyMin;
    /** Maximum difficulty level for questions. */
    private final int myQuestionDifficultyMax;

    /**
     * Private constructor - only Builder can create instances.
     *
     * @param theBuilder The builder instance containing configuration values.
     */
    private DifficultySettings(final Builder theBuilder) {
        myDifficultyName = theBuilder.myDifficultyName;
        myMazeWidth = theBuilder.myMazeWidth;
        myMazeHeight = theBuilder.myMazeHeight;
        myWallDensity = theBuilder.myWallDensity;
        myDoorDensity = theBuilder.myDoorDensity;
        myTimeLimit = theBuilder.myTimeLimit;
        myMaxHints = theBuilder.myMaxHints;
        myCorrectAnswerPoints = theBuilder.myCorrectAnswerPoints;
        myWrongAnswerPenalty = theBuilder.myWrongAnswerPenalty;
        myHintPenalty = theBuilder.myHintPenalty;
        mySkipQuestionPenalty = theBuilder.mySkipQuestionPenalty;
        myAllowSkipping = theBuilder.myAllowSkipping;
        myQuestionDifficultyMin = theBuilder.myQuestionDifficultyMin;
        myQuestionDifficultyMax = theBuilder.myQuestionDifficultyMax;
    }

    // Getters following the naming convention
    /**
     * Gets the difficulty name.
     * @return The difficulty name.
     */
    public String getDifficultyName() { return myDifficultyName; }

    /**
     * Gets the maze width.
     * @return The maze width in cells.
     */
    public int getMazeWidth() { return myMazeWidth; }

    /**
     * Gets the maze height.
     * @return The maze height in cells.
     */
    public int getMazeHeight() { return myMazeHeight; }

    /**
     * Gets the wall density.
     * @return The wall density (0.0 to 1.0).
     */
    public double getWallDensity() { return myWallDensity; }

    /**
     * Gets the door density.
     * @return The door density (0.0 to 1.0).
     */
    public double getDoorDensity() { return myDoorDensity; }

    /**
     * Gets the time limit per question.
     * @return The time limit in seconds, 0 if no limit.
     */
    public int getTimeLimit() { return myTimeLimit; }

    /**
     * Gets the maximum number of hints.
     * @return The maximum hints allowed.
     */
    public int getMaxHints() { return myMaxHints; }

    /**
     * Gets points for correct answers.
     * @return The points awarded for correct answers.
     */
    public int getCorrectAnswerPoints() { return myCorrectAnswerPoints; }

    /**
     * Gets penalty for wrong answers.
     * @return The points deducted for wrong answers.
     */
    public int getWrongAnswerPenalty() { return myWrongAnswerPenalty; }

    /**
     * Gets penalty for using hints.
     * @return The points deducted for hints.
     */
    public int getHintPenalty() { return myHintPenalty; }

    /**
     * Gets penalty for skipping questions.
     * @return The points deducted for skipping.
     */
    public int getSkipQuestionPenalty() { return mySkipQuestionPenalty; }

    /**
     * Checks if skipping is allowed.
     * @return True if skipping is allowed.
     */
    public boolean isAllowSkipping() { return myAllowSkipping; }

    /**
     * Gets minimum question difficulty.
     * @return The minimum question difficulty level.
     */
    public int getQuestionDifficultyMin() { return myQuestionDifficultyMin; }

    /**
     * Gets maximum question difficulty.
     * @return The maximum question difficulty level.
     */
    public int getQuestionDifficultyMax() { return myQuestionDifficultyMax; }

    /**
     * Checks if there is a time limit.
     * @return True if time limit is enabled.
     */
    public boolean hasTimeLimit() { return myTimeLimit > 0; }

    @Override
    public String toString() {
        return String.format("Difficulty: %s (Maze: %dx%d, Walls: %.0f%%, Doors: %.0f%%)",
                myDifficultyName, myMazeWidth, myMazeHeight,
                myWallDensity * 100, myDoorDensity * 100);
    }

    /**
     * Builder class for creating DifficultySettings instances.
     * Implements the Builder pattern for flexible object construction.
     */
    public static class Builder {
        // Required parameters
        /** The name of the difficulty level. */
        private String myDifficultyName;

        // Optional parameters with default values
        /** Default maze width. */
        private int myMazeWidth = 8;
        /** Default maze height. */
        private int myMazeHeight = 6;
        /** Default wall density. */
        private double myWallDensity = 0.3;
        /** Default door density. */
        private double myDoorDensity = 0.3;
        /** Default time limit (no limit). */
        private int myTimeLimit = 0;
        /** Default maximum hints. */
        private int myMaxHints = 3;
        /** Default points for correct answers. */
        private int myCorrectAnswerPoints = 10;
        /** Default penalty for wrong answers. */
        private int myWrongAnswerPenalty = 5;
        /** Default penalty for hints. */
        private int myHintPenalty = 5;
        /** Default penalty for skipping. */
        private int mySkipQuestionPenalty = 10;
        /** Default skipping allowance. */
        private boolean myAllowSkipping = true;
        /** Default minimum question difficulty. */
        private int myQuestionDifficultyMin = 1;
        /** Default maximum question difficulty. */
        private int myQuestionDifficultyMax = 3;

        /**
         * Constructs a new Builder with the required difficulty name.
         *
         * @param theDifficultyName The name of the difficulty level.
         */
        public Builder(final String theDifficultyName) {
            myDifficultyName = theDifficultyName;
        }

        /**
         * Sets the maze dimensions.
         *
         * @param theWidth The maze width.
         * @param theHeight The maze height.
         * @return This builder instance for method chaining.
         */
        public Builder mazeSize(final int theWidth, final int theHeight) {
            myMazeWidth = theWidth;
            myMazeHeight = theHeight;
            return this;
        }

        /**
         * Sets the wall density.
         *
         * @param theDensity The wall density (0.0 to 1.0).
         * @return This builder instance for method chaining.
         */
        public Builder wallDensity(final double theDensity) {
            myWallDensity = Math.max(0.0, Math.min(1.0, theDensity));
            return this;
        }

        /**
         * Sets the door density.
         *
         * @param theDensity The door density (0.0 to 1.0).
         * @return This builder instance for method chaining.
         */
        public Builder doorDensity(final double theDensity) {
            myDoorDensity = Math.max(0.0, Math.min(1.0, theDensity));
            return this;
        }

        /**
         * Sets the time limit per question.
         *
         * @param theSeconds The time limit in seconds (0 = no limit).
         * @return This builder instance for method chaining.
         */
        public Builder timeLimit(final int theSeconds) {
            myTimeLimit = Math.max(0, theSeconds);
            return this;
        }

        /**
         * Sets the maximum number of hints.
         *
         * @param theHints The maximum hints allowed.
         * @return This builder instance for method chaining.
         */
        public Builder maxHints(final int theHints) {
            myMaxHints = Math.max(0, theHints);
            return this;
        }

        /**
         * Sets the scoring parameters.
         *
         * @param theCorrectPoints Points for correct answers.
         * @param theWrongPenalty Penalty for wrong answers.
         * @param theHintPenalty Penalty for using hints.
         * @param theSkipPenalty Penalty for skipping questions.
         * @return This builder instance for method chaining.
         */
        public Builder scoring(final int theCorrectPoints, final int theWrongPenalty,
                               final int theHintPenalty, final int theSkipPenalty) {
            myCorrectAnswerPoints = Math.max(1, theCorrectPoints);
            myWrongAnswerPenalty = Math.max(0, theWrongPenalty);
            myHintPenalty = Math.max(0, theHintPenalty);
            mySkipQuestionPenalty = Math.max(0, theSkipPenalty);
            return this;
        }

        /**
         * Sets whether skipping questions is allowed.
         *
         * @param theAllow True if skipping should be allowed.
         * @return This builder instance for method chaining.
         */
        public Builder allowSkipping(final boolean theAllow) {
            myAllowSkipping = theAllow;
            return this;
        }

        /**
         * Sets the question difficulty range.
         *
         * @param theMin Minimum difficulty level.
         * @param theMax Maximum difficulty level.
         * @return This builder instance for method chaining.
         */
        public Builder questionDifficultyRange(final int theMin, final int theMax) {
            myQuestionDifficultyMin = Math.max(1, theMin);
            myQuestionDifficultyMax = Math.max(theMin, theMax);
            return this;
        }

        /**
         * Builds and returns a new DifficultySettings instance.
         *
         * @return A new DifficultySettings with the configured parameters.
         * @throws IllegalArgumentException If required parameters are invalid.
         */
        public DifficultySettings build() {
            // Validation
            if (myDifficultyName == null || myDifficultyName.trim().isEmpty()) {
                throw new IllegalArgumentException("Difficulty name cannot be null or empty");
            }
            if (myMazeWidth < 3 || myMazeHeight < 3) {
                throw new IllegalArgumentException("Maze must be at least 3x3");
            }
            if (myMazeWidth > 20 || myMazeHeight > 20) {
                throw new IllegalArgumentException("Maze cannot be larger than 20x20");
            }

            return new DifficultySettings(this);
        }
    }
}
