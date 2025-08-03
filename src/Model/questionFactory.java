package Model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * This represents a factory for loading trivia questions from SQLite Database.
 * This supports a variety of question types: True/false, multiple choice, and fill in the blank
 */

public class questionFactory {
    /**
     * path pointing to SQLite file containing all trivia questions
     */
    private static final String DB_PATH = "jdbc:sqlite:lib/trivia.db";

    /**
     * This loads a random question of a specifc type from DB
     * @param type the type of question: TF, MC, FB
     * @return an instance loaded with question prompt, correct answer, a hint, or null if no question of type exists
     */
    public static Question createQuestion(String type) {
        String query = "SELECT * FROM questions WHERE type = ? ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_PATH);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String prompt = rs.getString("question");
                String correct = rs.getString("correct_answer");

                // Optional: You can retrieve a hint string and convert it to a Hint object if needed
                Hint hint = new Hint("This is a placeholder hint!"); // Update when hints are in DB

                switch (type) {
                    case "TF":
                        boolean tfAnswer = Boolean.parseBoolean(correct);
                        return new TrueFalseQuestion(prompt, tfAnswer, hint);

                    case "MC":
                        List<String> options = new ArrayList<>();
                        options.add(rs.getString("option_a"));
                        options.add(rs.getString("option_b"));
                        options.add(rs.getString("option_c"));
                        options.add(rs.getString("option_d"));
                        int correctIndex = Integer.parseInt(correct);  // Assuming correct_answer stores index (e.g. "2")
                        return new MultipleChoiceQuestion(prompt, options, correctIndex, hint);

                    case "FB":
                        return new FillInTheBlank(prompt, correct, hint);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // fallback if no question or error occurs
    }

    /**
     * Test to check that the DB connection was working and it did contain all questions
     */
    public static void testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM questions;");
            if (rs.next()) {
                System.out.println("Question count: " + rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        questionFactory.testConnection();
    }
}
