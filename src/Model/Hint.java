package Model;

/**
 * This represents an optional hint that can be shown to a player.
 */
public class Hint {
    /*
    The text of hint to display to user
     */
    private final String myText;
    /*
    Flag to check if hint has already been used
     */
    public boolean isUsed = false;

    /**
     * This creates a new hint
     * @param theText the text of the hint
     * @throws IllegalArgumentException if text is null or empty
     */
    public Hint(final String theText) {
        if (theText == null || theText.isEmpty()) {
            throw new IllegalArgumentException("Hint text can't be null or empty");
        }
        this.myText = theText;

    }

    /**
     * This marks hint as used and returns text
     */
    public String useHint() {
        isUsed = true;
        return myText;
    }

    /**
     * This checks whether hint as already been used
     */
    public boolean isUsed() {
        return isUsed;
    }

}
