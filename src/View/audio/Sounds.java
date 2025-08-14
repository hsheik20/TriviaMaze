package View.audio;

public enum Sounds {

    MENU("/Resources/blast.wav"),
    CORRECT("/Resources/correct.wav"),
    INCORRECT("/Resources/Incorrect.wav"),
    WIN("/Resources/Won.wav"),
    LOSE("/Resources/GameOver.wav");

    private final String path;

    Sounds(String path) {
        this.path = path;
    }

    public void play() {
        SoundFX.play(path);
    }

    public String getPath() {
        return path;
    }
}


