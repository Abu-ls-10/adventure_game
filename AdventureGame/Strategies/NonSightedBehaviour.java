package Strategies;

import Visualization.GameEnvironment;

// For free tts
import com.sun.speech.freetts.*;


public class NonSightedBehaviour implements GameStrategy{

    public GameEnvironment gameEnv;

    /**
     * Constructs a NonSightedBehaviour with the specified GameEnvironment and Audio capabilities.
     *
     * @param gameEnv      The GameEnvironment instance.
     */
    public NonSightedBehaviour(GameEnvironment gameEnv){
        this.gameEnv = gameEnv;
        createGame(gameEnv, "");
    }

    /**
     * Creates and updates the game environment based on the NonSightedBehaviour Strategy.
     *
     * @param gameEnv      The GameEnvironment instance.
     * @param combination  The color combination for the game.
     */
    @Override
    public void createGame(GameEnvironment gameEnv, String combination) {
        System.out.println("Audio ON");
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    }

    /**
     * Uses text-to-speech library freetts to speak the provided text.
     *
     * @param text The text to be spoken.
     */
    public void speak(String text) {
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice("kevin16");
        voice.allocate();
        voice.speak(text);

    }
}