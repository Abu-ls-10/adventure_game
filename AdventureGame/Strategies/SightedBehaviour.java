package Strategies;

import Visualization.GameEnvironment;


public class SightedBehaviour implements GameStrategy{

    public GameEnvironment gameEnv;

    /**
     * Constructs a SightedBehaviour strategy and with the specified GameEnvironment and color combination,
     * creates a new game board with the given color combination.
     *
     * @param gameEnv      The GameEnvironment instance.
     */
    public SightedBehaviour(GameEnvironment gameEnv){
        this.gameEnv = gameEnv;
        createGame(gameEnv, "");
    }

    /**
     * Creates and updates the game environment based on the selected color combination.
     *
     * @param gameEnv      The GameEnvironment instance.
     * @param combination  The color combination for the game.
     */
    @Override
    public void createGame(GameEnvironment gameEnv, String combination) {
        System.out.println(1);
        // gameEnv.updateEnvironment("GREY", "Lightgrey", "blue", "black", "white");
    }
}