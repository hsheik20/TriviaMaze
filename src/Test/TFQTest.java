package Test;
import Model.TrueFalseQuestion;
import Model.Hint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the {@link TrueFalseQuestion} model.
 * It verifies the correct functionality of true/false questions,
 * including answer validation and correct answer retrieval.
 *
 * @author Husein
 */
public class TFQTest {
    private TrueFalseQuestion tfQuestion;

    /**
     * Sets up the test environment before each test method.
     * Initializes a new {@link TrueFalseQuestion} with a predefined
     * prompt, a correct answer of "true," and a hint.
     */
    @BeforeEach
    void setUp() {
        tfQuestion = new TrueFalseQuestion("Is 1 + 1 = 2?", true, new Hint("Add 2 fingers together"));
    }

    /**
     * Tests the {@code isCorrect} method with the correct answer,
     * including case-insensitivity. The method should return {@code true}.
     */
    @Test
    void testIsCorrectTrue() {
        assertTrue(tfQuestion.isCorrect("true"));
        assertTrue(tfQuestion.isCorrect("TRuE"));
    }

    /**
     * Tests the {@code isCorrect} method with an incorrect answer.
     * The method should return {@code false}.
     */
    @Test
    void testIsCorrectFalse() {
        assertFalse(tfQuestion.isCorrect("false"));
    }

    /**
     * Tests the {@code isCorrect} method with an invalid answer that is
     * neither "true" nor "false". The method should return {@code false}.
     */
    @Test
    void isCorrectInvalidOption() {
        assertFalse(tfQuestion.isCorrect("yes"));
    }

    /**
     * Tests the {@code getCorrectAnswer} method to ensure it returns the
     * correct answer as a standardized string ("true" or "false").
     */
    @Test
    void testGetCorrectAnswer() {
        assertEquals("true", tfQuestion.getCorrectAnswer());
    }
}