package Strategies;

import Visualization.GameEnvironment;

import java.util.ArrayList;
import java.util.HashMap;


public class LowSightedBehaviour implements GameStrategy{

    public GameEnvironment gameEnv;

    /**
     * Constructs a LowSightedBehaviour strategy and with the specified GameEnvironment and color combination,
     * creates a new game board with the given color combination.
     *
     * @param gameEnv      The GameEnvironment instance.
     * @param combination  The color combination for the game.
     */
    public LowSightedBehaviour(GameEnvironment gameEnv, String combination){
        this.gameEnv = gameEnv;
        createGame(gameEnv, combination);
    }

    /**
     * Retrieves hexadecimal color values for each color in the combination.
     *
     * @param combination  The color combination for the game.
     * @return             An ArrayList of hexadecimal color values.
     * Precondition: Hexadecimal representation of color must exist. Onlt colors black, white, grey, yellow,
     *                  green and light gray can be passed in.
     */
    public ArrayList<String> getHex(String[] combination) {
        HashMap<String, String> hexs = new HashMap<>();
        ArrayList<String> hex = new ArrayList<>();

        hexs.put("BLACK", "#000000");
        hexs.put("WHITE", "#FFFFFF");
        hexs.put("GREY", "#838383");
        hexs.put("YELLOW", "#FFFF00");
        hexs.put("GREEN", "#00D600");
        hexs.put("LIGHTGREY", "#D3D3D3");
        for (String i: combination){
            hex.add(hexs.get(i.toUpperCase()));
        }

        return hex;
    }

    /**
     * Creates and updates the game environment based on the selected color combination.
     *
     * @param gameEnv      The GameEnvironment instance.
     * @param combination  The color combination for the game.
     */
    @Override
    public void createGame(GameEnvironment gameEnv, String combination) {

        String[] comb = combination.split(", ");
        ArrayList<String> hexNums = getHex(comb);

        gameEnv.updateEnvironment(hexNums.get(0), hexNums.get(1), hexNums.get(2), comb[3]);
    }
}
