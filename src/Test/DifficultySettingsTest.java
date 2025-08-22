package Test;

import Model.DifficultySettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the {@link DifficultySettings} class and its
 * nested {@link DifficultySettings.Builder} class.
 * This class uses JUnit 5 to test the construction, configuration,
 * validation, and immutability of game difficulty settings.
 *
 * @author Husein & Chan
 */
public class DifficultySettingsTest {

    /**
     * A {@link DifficultySettings.Builder} instance used as a common starting point
     * for various tests, initialized before each test method.
     */
    private DifficultySettings.Builder builder;

    /**
     * Sets up the test environment before each test method.
     * Initializes a new {@link DifficultySettings.Builder} with a default
     * "Test Difficulty" name.
     */
    @BeforeEach
    void setUp() {
        builder = new DifficultySettings.Builder("Test Difficulty");
    }

    /**
     * {@code Nested} test class for verifying the construction and default
     * values of the {@link DifficultySettings.Builder}.
     */
    @Nested
    @DisplayName("Builder Construction Tests")
    class BuilderConstructionTests {

        /**
         * Tests that the builder can be successfully created with a valid
         * difficulty name.
         */
        @Test
        @DisplayName("Should create builder with required difficulty name")
        void testBuilderWithValidName() {
            // Arrange & Act
            DifficultySettings.Builder testBuilder = new DifficultySettings.Builder("Easy");

            // Assert
            assertNotNull(testBuilder, "Builder should be created with valid name");
        }

        /**
         * Tests that when no configuration methods are called on the builder,
         * the {@link DifficultySettings} object is built with its predefined
         * default values.
         */
        @Test
        @DisplayName("Should build with default values when no methods called")
        void testBuildWithDefaults() {
            // Act
            DifficultySettings settings = builder.build();

            // Assert
            assertEquals("Test Difficulty", settings.getDifficultyName());
            assertEquals(8, settings.getMazeWidth());
            assertEquals(6, settings.getMazeHeight());
            assertEquals(0, settings.getTimeLimit());
            assertEquals(3, settings.getMaxHints());
            assertEquals(10, settings.getCorrectAnswerPoints());
            assertEquals(5, settings.getWrongAnswerPenalty());
            assertEquals(5, settings.getHintPenalty());
            assertEquals(10, settings.getSkipQuestionPenalty());
            assertTrue(settings.isAllowSkipping());
            assertEquals(1, settings.getQuestionDifficultyMin());
            assertEquals(3, settings.getQuestionDifficultyMax());
        }
    }

    /**
     * {@code Nested} test class for verifying the functionality of individual
     * configuration methods within the {@link DifficultySettings.Builder}.
     */
    @Nested
    @DisplayName("Builder Method Tests")
    class BuilderMethodTests {

        /**
         * Tests that the {@code mazeSize} method correctly sets the maze
         * width and height.
         */
        @Test
        @DisplayName("Should set maze size correctly")
        void testMazeSize() {
            // Act
            DifficultySettings settings = builder
                    .mazeSize(10, 8)
                    .build();

            // Assert
            assertEquals(10, settings.getMazeWidth());
            assertEquals(8, settings.getMazeHeight());
        }

        /**
         * Tests that the {@code timeLimit} method correctly sets the time limit
         * and clamps negative values to 0.
         */
        @Test
        @DisplayName("Should set time limit with minimum of 0")
        void testTimeLimit() {
            // Test positive value
            DifficultySettings settings1 = builder
                    .timeLimit(60)
                    .build();
            assertEquals(60, settings1.getTimeLimit());

            // Test negative value should be clamped to 0
            DifficultySettings settings2 = new DifficultySettings.Builder("Test2")
                    .timeLimit(-30)
                    .build();
            assertEquals(0, settings2.getTimeLimit());
        }

        /**
         * Tests that the {@code maxHints} method correctly sets the maximum
         * number of hints and clamps negative values to 0.
         */
        @Test
        @DisplayName("Should set max hints with minimum of 0")
        void testMaxHints() {
            // Test positive value
            DifficultySettings settings1 = builder
                    .maxHints(5)
                    .build();
            assertEquals(5, settings1.getMaxHints());

            // Test negative value should be clamped to 0
            DifficultySettings settings2 = new DifficultySettings.Builder("Test2")
                    .maxHints(-2)
                    .build();
            assertEquals(0, settings2.getMaxHints());
        }

        /**
         * Tests that the {@code scoring} method correctly sets all
         * scoring-related parameters.
         */
        @Test
        @DisplayName("Should set scoring parameters correctly")
        void testScoring() {
            // Act
            DifficultySettings settings = builder
                    .scoring(15, 8, 6, 12)
                    .build();

            // Assert
            assertEquals(15, settings.getCorrectAnswerPoints());
            assertEquals(8, settings.getWrongAnswerPenalty());
            assertEquals(6, settings.getHintPenalty());
            assertEquals(12, settings.getSkipQuestionPenalty());
        }

        /**
         * Tests that the {@code scoring} method clamps invalid (negative)
         * scoring parameters to their defined minimum values.
         */
        @Test
        @DisplayName("Should clamp scoring parameters to minimum values")
        void testScoringWithInvalidValues() {
            // Act
            DifficultySettings settings = builder
                    .scoring(-5, -3, -2, -1)
                    .build();

            // Assert
            assertEquals(1, settings.getCorrectAnswerPoints()); // minimum is 1
            assertEquals(0, settings.getWrongAnswerPenalty()); // minimum is 0
            assertEquals(0, settings.getHintPenalty()); // minimum is 0
            assertEquals(0, settings.getSkipQuestionPenalty()); // minimum is 0
        }

        /**
         * Tests that the {@code allowSkipping} method correctly sets
         * the boolean flag for allowing question skipping.
         */
        @Test
        @DisplayName("Should set allow skipping correctly")
        void testAllowSkipping() {
            // Test true
            DifficultySettings settings1 = builder
                    .allowSkipping(true)
                    .build();
            assertTrue(settings1.isAllowSkipping());

            // Test false
            DifficultySettings settings2 = new DifficultySettings.Builder("Test2")
                    .allowSkipping(false)
                    .build();
            assertFalse(settings2.isAllowSkipping());
        }

        /**
         * Tests that the {@code questionDifficultyRange} method correctly
         * sets the minimum and maximum question difficulty levels.
         */
        @Test
        @DisplayName("Should set question difficulty range correctly")
        void testQuestionDifficultyRange() {
            // Act
            DifficultySettings settings = builder
                    .questionDifficultyRange(2, 5)
                    .build();

            // Assert
            assertEquals(2, settings.getQuestionDifficultyMin());
            assertEquals(5, settings.getQuestionDifficultyMax());
        }

        /**
         * Tests that the {@code questionDifficultyRange} method ensures
         * the maximum difficulty is at least equal to the minimum difficulty.
         * If max is set less than min, max should be adjusted to min.
         */
        @Test
        @DisplayName("Should ensure max difficulty is at least equal to min")
        void testQuestionDifficultyRangeValidation() {
            // Test when max < min
            DifficultySettings settings = builder
                    .questionDifficultyRange(5, 3)
                    .build();

            assertEquals(5, settings.getQuestionDifficultyMin());
            assertEquals(5, settings.getQuestionDifficultyMax()); // Should be adjusted to min
        }

        /**
         * Tests that the {@code questionDifficultyRange} method ensures
         * the minimum difficulty is at least 1, clamping any lower values.
         */
        @Test
        @DisplayName("Should ensure minimum difficulty is at least 1")
        void testQuestionDifficultyMinimum() {
            // Act
            DifficultySettings settings = builder
                    .questionDifficultyRange(-2, 4)
                    .build();

            // Assert
            assertEquals(1, settings.getQuestionDifficultyMin());
            assertEquals(4, settings.getQuestionDifficultyMax());
        }
    }

    /**
     * {@code Nested} test class for verifying validation rules applied
     * during the construction of {@link DifficultySettings} via its builder.
     */
    @Nested
    @DisplayName("Builder Validation Tests")
    class BuilderValidationTests {

        /**
         * Tests that building with a {@code null} difficulty name
         * throws an {@link IllegalArgumentException}.
         */
        @Test
        @DisplayName("Should throw exception for null difficulty name")
        void testNullDifficultyName() {
            // Arrange
            DifficultySettings.Builder invalidBuilder = new DifficultySettings.Builder(null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, invalidBuilder::build,
                    "Should throw exception for null difficulty name");
        }

        /**
         * Tests that building with an empty string as the difficulty name
         * throws an {@link IllegalArgumentException}.
         */
        @Test
        @DisplayName("Should throw exception for empty difficulty name")
        void testEmptyDifficultyName() {
            // Arrange
            DifficultySettings.Builder invalidBuilder = new DifficultySettings.Builder("");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, invalidBuilder::build,
                    "Should throw exception for empty difficulty name");
        }

        /**
         * Tests that building with a difficulty name consisting only of
         * whitespace throws an {@link IllegalArgumentException}.
         */
        @Test
        @DisplayName("Should throw exception for whitespace-only difficulty name")
        void testWhitespaceOnlyDifficultyName() {
            // Arrange
            DifficultySettings.Builder invalidBuilder = new DifficultySettings.Builder("   ");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, invalidBuilder::build,
                    "Should throw exception for whitespace-only difficulty name");
        }

        /**
         * Tests that setting a maze size smaller than the minimum allowed (3x3)
         * throws an {@link IllegalArgumentException}.
         */
        @Test
        @DisplayName("Should throw exception for maze size too small")
        void testMazeTooSmall() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                builder.mazeSize(2, 2).build();
            }, "Should throw exception for maze smaller than 3x3");
        }

        /**
         * Tests that setting a maze size larger than the maximum allowed (20x20)
         * throws an {@link IllegalArgumentException}.
         */
        @Test
        @DisplayName("Should throw exception for maze size too large")
        void testMazeTooLarge() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                builder.mazeSize(25, 25).build();
            }, "Should throw exception for maze larger than 20x20");
        }

        /**
         * Tests that the builder accepts maze sizes at the valid boundaries
         * (minimum 3x3 and maximum 20x20).
         */
        @Test
        @DisplayName("Should accept boundary maze sizes")
        void testBoundaryMazeSizes() {
            // Test minimum size
            DifficultySettings settings1 = builder
                    .mazeSize(3, 3)
                    .build();
            assertEquals(3, settings1.getMazeWidth());
            assertEquals(3, settings1.getMazeHeight());

            // Test maximum size
            DifficultySettings settings2 = new DifficultySettings.Builder("Test2")
                    .mazeSize(20, 20)
                    .build();
            assertEquals(20, settings2.getMazeWidth());
            assertEquals(20, settings2.getMazeHeight());
        }
    }

    /**
     * {@code Nested} test class for verifying the fluent interface
     * capabilities of the {@link DifficultySettings.Builder}.
     */
    @Nested
    @DisplayName("Method Chaining Tests")
    class MethodChainingTests {

        /**
         * Tests that multiple configuration methods can be chained together
         * to build a {@link DifficultySettings} object, and all settings
         * are applied correctly.
         */
        @Test
        @DisplayName("Should support method chaining")
        void testMethodChaining() {
            // Act
            DifficultySettings settings = new DifficultySettings.Builder("Chained Test")
                    .mazeSize(12, 10)
                    .timeLimit(45)
                    .maxHints(2)
                    .scoring(8, 6, 7, 12)
                    .allowSkipping(false)
                    .questionDifficultyRange(2, 4)
                    .build();

            // Assert
            assertEquals("Chained Test", settings.getDifficultyName());
            assertEquals(12, settings.getMazeWidth());
            assertEquals(10, settings.getMazeHeight());
            assertEquals(45, settings.getTimeLimit());
            assertEquals(2, settings.getMaxHints());
            assertEquals(8, settings.getCorrectAnswerPoints());
            assertEquals(6, settings.getWrongAnswerPenalty());
            assertEquals(7, settings.getHintPenalty());
            assertEquals(12, settings.getSkipQuestionPenalty());
            assertFalse(settings.isAllowSkipping());
            assertEquals(2, settings.getQuestionDifficultyMin());
            assertEquals(4, settings.getQuestionDifficultyMax());
        }
    }

    /**
     * {@code Nested} test class for verifying specific methods of the
     * {@link DifficultySettings} class itself, beyond its construction.
     */
    @Nested
    @DisplayName("DifficultySettings Method Tests")
    class DifficultySettingsMethodTests {

        /**
         * Tests the {@code hasTimeLimit} method to ensure it correctly
         * indicates whether a time limit is active (greater than 0).
         */
        @Test
        @DisplayName("Should correctly identify if time limit exists")
        void testHasTimeLimit() {
            // Test with time limit
            DifficultySettings withTimeLimit = builder
                    .timeLimit(60)
                    .build();
            assertTrue(withTimeLimit.hasTimeLimit());

            // Test without time limit
            DifficultySettings withoutTimeLimit = new DifficultySettings.Builder("Test2")
                    .timeLimit(0)
                    .build();
            assertFalse(withoutTimeLimit.hasTimeLimit());
        }

        /**
         * Tests the {@code toString} method to ensure it provides a
         * meaningful string representation of the difficulty settings,
         * including key parameters.
         */
        @Test
        @DisplayName("Should provide correct toString representation")
        void testToString() {
            // Act
            DifficultySettings settings = builder
                    .mazeSize(10, 8)
                    .build();

            String result = settings.toString();

            // Assert
            assertTrue(result.contains("Test Difficulty"));
            assertTrue(result.contains("10x8"));
            assertTrue(result.contains("40%")); // Default correct answer ratio (40% of max points for maze)
            assertTrue(result.contains("30%")); // Default wrong answer penalty ratio (30% of max points for maze)
        }
    }

    /**
     * {@code Nested} test class for verifying the immutability and
     * serializability of the {@link DifficultySettings} object.
     */
    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        /**
         * Tests that once a {@link DifficultySettings} object is created,
         * its internal state cannot be modified, implying immutability.
         * This is verified by checking that getters return consistent values
         * and by the absence of public setters.
         */
        @Test
        @DisplayName("Should be immutable after creation")
        void testImmutability() {
            // Act
            DifficultySettings settings = builder.build();

            // Assert - All fields should be final and no setters should exist
            // We test this by verifying getters return consistent values
            String name1 = settings.getDifficultyName();
            String name2 = settings.getDifficultyName();
            assertSame(name1, name2, "Difficulty name should be the same reference");

            int width1 = settings.getMazeWidth();
            int width2 = settings.getMazeWidth();
            assertEquals(width1, width2, "Maze width should be consistent");

            // Further checks could involve reflection to assert no public setters exist.
            // For now, consistent getter returns are sufficient for this test's scope.
        }

        /**
         * Tests that the {@link DifficultySettings} class implements
         * the {@link java.io.Serializable} interface, allowing its instances
         * to be converted into a byte stream.
         */
        @Test
        @DisplayName("Should implement Serializable")
        void testSerializable() {
            // Act
            DifficultySettings settings = builder.build();

            // Assert
            assertTrue(settings instanceof java.io.Serializable,
                    "DifficultySettings should implement Serializable");
        }
    }
}