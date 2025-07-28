package Model;

class Hint {
    private String hintText;
    private boolean used;

    public Hint(String hintText) {
        this.hintText = hintText;
        this.used = false;
    }

    public String getHintText() { return hintText; }
    public boolean isUsed() { return used; }
    public void use() { this.used = true; }
}
