package Model;

/**
 * This represents an optional hint that can be shown to a player.
 */
public class Hint {
    /*
    The text of hint to display to user
     */
    private final String text;
    /*
    Flag to check if hint has already been used
     */
    public boolean isUsed = false;

    /**
     * This creates a new hint
     * @param text the text of the hint
     * @throws IllegalArgumentException if text is null or empty
     */
    public Hint(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Hint text can't be null or empty");
        }
        this.text = text;

    }

    /**
     * This marks hint as used and returns text
     */
    public String useHint() {
        isUsed = true;
        return text;
    }

    /**
     * This checks whether hint as already been used
     */
    public boolean isUsed() {
        return isUsed;
    }

}
