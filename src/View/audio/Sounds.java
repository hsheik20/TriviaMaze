package View.audio;

public enum Sounds {

    GAME("/Resources/game.wav"),
    MENU("/Resources/blast.wav"),
    CORRECT("/Resources/correct.wav"),
    INCORRECT("/Resources/Incorrect.wav"),
    WIN("/Resources/Won.wav"),
    LOSE("/Resources/GameOver.wav");

    private final String path;

    Sounds(String path) {
        this.path = path;
    }

    public void play() { SoundFX.play(path); }
    public void loop() { SoundFX.loop(path); }          // new
    public String getPath() { return path; }

    public static void stopLoop() { SoundFX.stopLoop(); } // convenience
    public static boolean isLooping() { return SoundFX.isLooping(); }
}


