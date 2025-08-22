package Test;
import Model.MultipleChoiceQuestion;
import Model.Hint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the {@link MultipleChoiceQuestion} model.
 * It verifies the correct functionality of multiple-choice questions,
 * including answer validation, correct answer retrieval, and option management.
 *
 * @author Husein
 */
public class MCQTest {
    private MultipleChoiceQuestion question;

    /**
     * Sets up the test environment before each test method.
     * Initializes a new {@link MultipleChoiceQuestion} with a predefined
     * list of options and a correct answer index.
     */
    @BeforeEach
    void setUp() {
        List<String> options = Arrays.asList("Red", "Green", "Blue", "Yellow");
        question = new MultipleChoiceQuestion("What color is an Apple?", options, 0, new Hint("Think roses"));
    }

    /**
     * Tests the {@code isCorrect} method with a valid and correct answer.
     * The method should return {@code true} when the correct index is provided.
     */
    @Test
    void testIsCorrectValidAnswer() {
        assertTrue(question.isCorrect("0"));
    }

    /**
     * Tests the {@code isCorrect} method with an invalid answer.
     * The method should return {@code false} when the input is not a valid
     * index string (e.g., "abc").
     */
    @Test
    void testIsCorrectInvalidAnswer() {
        assertFalse(question.isCorrect("abc")); // or any invalid string
    }

    /**
     * Tests the {@code getCorrectAnswer} method to ensure it returns the
     * correct answer string from the options list based on the correct index.
     */
    @Test
    void testGetCorrectAnswer() {
        assertEquals("Red", question.getCorrectAnswer());
    }

    /**
     * Tests the {@code getOptions} method to ensure it returns the
     * complete and correct list of choices provided during initialization.
     */
    @Test
    void testGetOptions() {
        List<String> options = question.getOptions();
        assertEquals(4, options.size());
        assertEquals("Red", options.getFirst());
    }
}