package Test;
import Model.MultipleChoiceQuestion;
import Model.Hint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MCQTest {
    private MultipleChoiceQuestion question;

    @BeforeEach
    void setUp() {
        List<String> options = Arrays.asList("Red", "Green", "Blue", "Yellow");
        question = new MultipleChoiceQuestion("What color is an Apple?", options, 0, new Hint("Think roses"));
    }

    @Test
    void testIsCorrectValidAnswer() {
        assertTrue(question.isCorrect("0"));
    }
    @Test
    void testIsCorrectInvalidAnswer() {
        assertFalse(question.isCorrect("abc")); // or any invalid string
    }

    @Test
    void testGetCorrectAnswer() {
        assertEquals("Red", question.getCorrectAnswer());
    }
    @Test
    void testGetOptions() {
        List<String> options = question.getOptions();
        assertEquals(4, options.size());
        assertEquals("Red", options.getFirst());
    }

}
