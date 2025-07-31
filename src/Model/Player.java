package Model;


public class Player {
    private int x, y;
    private int score;
    private int questionsAnswered;

    public Player() {
        resetPosition();
    }

    public void resetPosition() {
        x = 0;
        y = 0;
        score = 0;
        questionsAnswered = 0;
    }

    public boolean move(int newX, int newY, Maze maze) {
        if (maze.isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            return true;
        }
        return false;
    }

    // Getters and setters
    public int getX() {
        return x;
    }
    public int getY() {
        return y
    }
    public int getScore() {
        return score;
    }
    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void addScore(int points){
        this.score += points;
    }
    public void incrementQuestionsAnswered() {
        this.questionsAnswered++;
    }
}
