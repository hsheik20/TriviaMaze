package Model;

import java.sql.*;
import java.util.*;

/**
 * Factory to preload and serve trivia questions of various types from SQLite.
 * Supports: Multiple Choice (MC), True/False (TF), and Fill in the Blank (FB).
 *
 * @author Husein & Chan
 */
public class questionFactory {

    private final String dbPath;
    private final Random random = new Random();
    private final Map<String, Queue<Question>> questionsByType = new HashMap<>();

    public questionFactory(final String theDbPath) {
        this.dbPath = theDbPath;
        preloadQuestions();
    }

    private void preloadQuestions() {
        String query = "SELECT * FROM questions";

        try (Connection conn = DriverManager.getConnection(dbPath);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("type").trim().toUpperCase();
                String prompt = rs.getString("question");
                String correct = rs.getString("correct_answer");
                int id = rs.getInt("id");

                List<String> hints = getHintsForQuestion(id);
                Hint hint = hints.isEmpty() ? null : new Hint(hints.get(0));

                Question question = switch (type) {
                    case "TF" -> new TrueFalseQuestion(prompt, Boolean.parseBoolean(correct), hint);
                    case "MC" -> {
                        List<String> options = List.of(
                                rs.getString("option_a"),
                                rs.getString("option_b"),
                                rs.getString("option_c"),
                                rs.getString("option_d")
                        );
                        int correctIndex = correct.trim().toUpperCase().charAt(0) - 'A';
                        yield new MultipleChoiceQuestion(prompt, options, correctIndex, hint);
                    }
                    case "FB" -> new FillInTheBlank(prompt, correct, hint);
                    default -> null;
                };

                if (question != null) {
                    questionsByType
                            .computeIfAbsent(type, k -> new LinkedList<>())
                            .add(question);
                }
            }

            // Shuffle each queue
            for (Map.Entry<String, Queue<Question>> entry : questionsByType.entrySet()) {
                List<Question> shuffled = new ArrayList<>(entry.getValue());
                Collections.shuffle(shuffled, random);
                entry.setValue(new LinkedList<>(shuffled));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error preloading questions: " + e.getMessage());
        }
    }

    /**
     * Returns the next available question (of any type), ensuring no repeats.
     */
    public Question getNextAvailableQuestion() {
        List<String> availableTypes = questionsByType.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        if (availableTypes.isEmpty()) return null;

        String randomType = availableTypes.get(random.nextInt(availableTypes.size()));
        return questionsByType.get(randomType).poll();
    }

    /**
     * Loads all hints for a question ID.
     * @param questionId ID in the database
     * @return list of hint strings
     */
    private List<String> getHintsForQuestion(int questionId) {
        List<String> hints = new ArrayList<>();
        String sql = "SELECT hint_text FROM hints WHERE question_id = ?";

        try (Connection conn = DriverManager.getConnection(dbPath);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hints.add(rs.getString("hint_text"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error loading hints: " + e.getMessage());
        }

        return hints;
    }
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lib/trivia(1).db")) {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table';"
            );
            System.out.println("Tables found:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
