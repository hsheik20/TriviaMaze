// ==========================================

// DifficultyPresets.java
package Model;

/**
 * The {@code DifficultyPresets} class provides predefined difficulty configurations
 * for the Trivia Maze Game. Uses the Builder pattern to create various difficulty levels.
 *
 * @author Chan
 */
public final class DifficultyPresets {

    /** Private constructor to prevent instantiation of utility class. */
    private DifficultyPresets() {
        // Utility class - no instantiation
    }

    /**
     * Creates an easy difficulty setting.
     * Features: Small maze, no time limit, many hints, low penalties.
     *
     * @return DifficultySettings configured for easy gameplay.
     */
    public static DifficultySettings easy() {
        return new DifficultySettings.Builder("Easy")
                .mazeSize(3, 3)
                .timeLimit(0) // no time limit
                .maxHints(5)
                .scoring(15, 3, 3, 5)
                .allowSkipping(true)
                .maxAttemptsPerDoor(3)
                .questionDifficultyRange(1, 2)
                .build();
    }

    /**
     * Creates a normal difficulty setting.
     * Features: Standard maze, moderate time limit, balanced scoring.
     *
     * @return DifficultySettings configured for normal gameplay.
     */
    public static DifficultySettings normal() {
        return new DifficultySettings.Builder("Normal")
                .mazeSize(4, 4)
                .timeLimit(60) // 1 minute per question
                .maxHints(3)
                .scoring(10, 5, 5, 10)
                .allowSkipping(true)
                .maxAttemptsPerDoor(2)
                .questionDifficultyRange(1, 3)
                .build();
    }

    /**
     * Creates a hard difficulty setting.
     * Features: Large maze, time pressure, fewer hints, higher penalties.
     *
     * @return DifficultySettings configured for hard gameplay.
     */
    public static DifficultySettings hard() {
        return new DifficultySettings.Builder("Hard")
                .mazeSize(5, 5)
                .timeLimit(45) // 45 seconds per question
                .maxHints(2)
                .scoring(8, 7, 8, 15)
                .allowSkipping(true)
                .maxAttemptsPerDoor(1)
                .questionDifficultyRange(2, 4)
                .build();
    }


    /**
     * Creates a base custom difficulty setting.
     * Returns normal settings that can be further customized.
     *
     * @return DifficultySettings configured as a starting point for customization.
     */
    public static DifficultySettings custom() {
        return new DifficultySettings.Builder("Custom")
                .build(); // Returns normal settings by default
    }

    /**
     * Gets all predefined difficulty presets.
     *
     * @return Array of all available difficulty presets.
     */
    public static DifficultySettings[] getAllPresets() {
        return new DifficultySettings[] {
                easy(), normal(), hard()
        };
    }
}
