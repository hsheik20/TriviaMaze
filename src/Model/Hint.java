package Model;

/**
 * The {@code Hint} class represents a single hint that can assist the player
 * in answering a trivia question. Each hint has associated text and a flag
 * indicating whether it has already been used.
 *
 * Hints can be used once, and after that their state is marked as used.
 * This class may be expanded later to support different types or behaviors of hints.
 *
 * Intended to be associated with {@link Question} objects or managed via the trivia system.
 *
 * @author Husein & Chan
 */
class Hint {
    /** The text content of the hint. */
    private String hintText;
    /** Whether the hint has been used by the player. */
    private boolean used;
    /**
     * Constructs a new {@code Hint} with the specified hint text.
     * The hint is marked as unused by default.
     *
     * @param hintText the textual content of the hint
     */
    public Hint(String hintText) {
        this.hintText = hintText;
        this.used = false;
    }
    /**
     * Returns the text of the hint.
     *
     * @return the hint text
     */
    public String getHintText() {
        return hintText;
    }
    /**
     * Returns whether this hint has already been used.
     *
     * @return {@code true} if the hint has been used; {@code false} otherwise
     */
    public boolean isUsed() {
        return used;
    }
    /**
     * Marks this hint as used.
     * Once used, it cannot be used again.
     */
    public void use() {
        this.used = true;
    }
}
