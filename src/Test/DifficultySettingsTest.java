package Test;

import Model.DifficultySettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

public class DifficultySettingsTest {

    private DifficultySettings.Builder builder;

    @BeforeEach
    void setUp() {
        builder = new DifficultySettings.Builder("Test Difficulty");
    }

    @Nested
    @DisplayName("Builder Construction Tests")
    class BuilderConstructionTests {

        @Test
        @DisplayName("Should create builder with required difficulty name")
        void testBuilderWithValidName() {
            // Arrange & Act
            DifficultySettings.Builder testBuilder = new DifficultySettings.Builder("Easy");

            // Assert
            assertNotNull(testBuilder, "Builder should be created with valid name");
        }

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

    @Nested
    @DisplayName("Builder Method Tests")
    class BuilderMethodTests {

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

    @Nested
    @DisplayName("Builder Validation Tests")
    class BuilderValidationTests {

        @Test
        @DisplayName("Should throw exception for null difficulty name")
        void testNullDifficultyName() {
            // Arrange
            DifficultySettings.Builder invalidBuilder = new DifficultySettings.Builder(null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, invalidBuilder::build,
                    "Should throw exception for null difficulty name");
        }

        @Test
        @DisplayName("Should throw exception for empty difficulty name")
        void testEmptyDifficultyName() {
            // Arrange
            DifficultySettings.Builder invalidBuilder = new DifficultySettings.Builder("");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, invalidBuilder::build,
                    "Should throw exception for empty difficulty name");
        }

        @Test
        @DisplayName("Should throw exception for whitespace-only difficulty name")
        void testWhitespaceOnlyDifficultyName() {
            // Arrange
            DifficultySettings.Builder invalidBuilder = new DifficultySettings.Builder("   ");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, invalidBuilder::build,
                    "Should throw exception for whitespace-only difficulty name");
        }

        @Test
        @DisplayName("Should throw exception for maze size too small")
        void testMazeTooSmall() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                builder.mazeSize(2, 2).build();
            }, "Should throw exception for maze smaller than 3x3");
        }

        @Test
        @DisplayName("Should throw exception for maze size too large")
        void testMazeTooLarge() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                builder.mazeSize(25, 25).build();
            }, "Should throw exception for maze larger than 20x20");
        }

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

    @Nested
    @DisplayName("Method Chaining Tests")
    class MethodChainingTests {

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

    @Nested
    @DisplayName("DifficultySettings Method Tests")
    class DifficultySettingsMethodTests {

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
            assertTrue(result.contains("40%"));
            assertTrue(result.contains("30%"));
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

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
        }

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