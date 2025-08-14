package Test;
import Model.TrueFalseQuestion;
import Model.Hint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TFQTest {
    private TrueFalseQuestion tfQuestion;

    @BeforeEach
    void setUp() {
        tfQuestion = new TrueFalseQuestion("Is 1 + 1 = 2?", true, new Hint("Add 2 fingers together"));
    }
    @Test
    void testIsCorrectTrue() {
        assertTrue(tfQuestion.isCorrect("true"));
        assertTrue(tfQuestion.isCorrect("TRuE"));
    }
    @Test
    void testIsCorrectFalse() {
        assertFalse(tfQuestion.isCorrect("false"));
    }
    @Test
    void isCorrectInvalidOption() {
        assertFalse(tfQuestion.isCorrect("yes"));
    }
    @Test
    void testGetCorrectAnswer() {
        assertEquals("true", tfQuestion.getCorrectAnswer());

    }
}
