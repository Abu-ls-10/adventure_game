package states;

import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import views.AdventureGameView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static states.SaveCommand.*;

/**
 * Class containing methods to execute various game commands.
 */
public class CommandReceiver {

    /**
     * List of nodes to store the current state of the game pane.
     */
    static ArrayList<Node> nodes;

    /**
     * Pauses the game.
     *
     * @param adventureGameView The AdventureGameView instance.
     */
    public static void pauseGame(AdventureGameView adventureGameView) {
        System.out.println("Game paused");
        // Logic for pausing the game

        nodes = new ArrayList<>(adventureGameView.gamePane.getChildren());
        adventureGameView.gamePane.getChildren().clear();

        Label paused = new Label("GAME PAUSED");
        paused.setFont(new Font("Arial", 24));
        paused.setStyle("-fx-text-fill: white;");
        paused.setAlignment(Pos.CENTER);

        Button resumeGame = new Button("Resume");
        AdventureGameView.customizeButton(resumeGame, 100, 50);
        AdventureGameView.makeButtonAccessible(resumeGame,
                "Resume",
                "Click to resume",
                "This button resumes the game. Click to resume");

        VBox pauseDisplay = new VBox(paused, resumeGame);
        pauseDisplay.setPadding(new Insets(20, 20, 20, 20));
        pauseDisplay.setAlignment(Pos.CENTER);

        adventureGameView.gamePane.getChildren().add(pauseDisplay);

        resumeGame.setOnAction(e -> {
            adventureGameView.gamePane.getChildren().clear();
            adventureGameView.gamePane.getChildren().addAll(nodes);
        });

    }

    /**
     * Resumes the game.
     *
     * @param adventureGameView The AdventureGameView instance.
     */
    public static void resumeGame(AdventureGameView adventureGameView) {
        System.out.println("Game resumed");
        // Logic for resuming the game

        adventureGameView.gamePane.getChildren().clear();
        adventureGameView.gamePane.getChildren().addAll(nodes);
    }

    /**
     * Loads a saved game.
     *
     * @param selectGameLabel The label used for displaying prompts.
     * @param gameList        The ListView containing the list of available saved games.
     * @param adventureGameView The AdventureGameView instance.
     */
    public static void loadGame(Label selectGameLabel, ListView<String> gameList, AdventureGameView adventureGameView) {
        // Logic for loading the game
        System.out.println("Game loaded");

        try {
            selectGame(selectGameLabel, gameList, adventureGameView);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the game.
     * Save the game to a serialized (binary) file.
     * Get the name of the file from saveFileNameTextField.
     * Files will be saved to the Games/Saved directory.
     * If the file already exists, set the saveFileErrorLabel to the text in saveFileExistsError.
     * If the file doesn't end in .ser, set the saveFileErrorLabel to the text in saveFileNotSerError.
     * Otherwise, load the file and set the saveFileErrorLabel to the text in saveFileSuccess.
     *
     * @param adventureGameView The AdventureGameView instance.
     */
    public static void saveGame(AdventureGameView adventureGameView) {
        // Logic for saving the game
        System.out.println("Game saved");

        String savedPath = "Games" + File.separator + "Saved" + File.separator + saveFileNameTextField.getText(); // TODO: Uncomment if opening in <AdventureGame>
        // String savedPath = "AdventureGame" + File.separator + "Games" + File.separator + "Saved" + File.separator + saveFileNameTextField.getText(); // TODO: Uncomment this line if opening in <group_89>

        File savedGame = new File(savedPath);
        if (!savedGame.getName().endsWith(".ser")) {
            saveFileErrorLabel.setText(saveFileNotSerError);
        } else if (savedGame.exists()) {
            saveFileErrorLabel.setText(saveFileExistsError);
        } else {
            adventureGameView.model.saveModel(savedGame);
            saveFileErrorLabel.setText(saveFileSuccess);
        }
    }

    /**
     * Restarts the game.
     *
     * @param adventureGameView The AdventureGameView instance.
     */
    public static void restartGame(AdventureGameView adventureGameView) {
        // Logic for restarting the game
        System.out.println("Game restarted");
        // adventureGameView.model = new AdventureGame("AdventureGame", "VisualGame"); // TODO: Uncomment if opening in <group_89>
        adventureGameView.model = new AdventureGame("VisualGame"); // TODO: Uncomment if opening in <AdventureGame>
        adventureGameView.updateScene();
        adventureGameView.updateText("New Game has started!");
    }

    /**
     * Selects a game for loading.
     *
     * @param selectGameLabel The label used for displaying errors or successes.
     * @param gameList        The ListView containing the list of available saved games.
     * @param adventureGameView The AdventureGameView instance.
     * @throws IOException if an I/O error occurs.
     */
    private static void selectGame(Label selectGameLabel, ListView<String> gameList, AdventureGameView adventureGameView) throws IOException {
        //saved games will be in the Games/Saved folder!
        String selectedGame = gameList.getSelectionModel().getSelectedItem();
        File gameFile = new File("Games" + File.separator + "Saved" + File.separator + selectedGame);  // TODO: Uncomment if opening in <AdventureGame>
        // File gameFile = new File("AdventureGame" + File.separator + "Games" + File.separator + "Saved" + File.separator + selectedGame);  // TODO: Uncomment if opening in <group_89>

        try {
            adventureGameView.stopArticulation();
            selectGameLabel.setText(selectedGame);
            adventureGameView.model = load(gameFile.getPath());
            adventureGameView.updateText("");
            adventureGameView.updateScene();

        } catch (NullPointerException | ClassNotFoundException e) {
            adventureGameView.stopArticulation();
            adventureGameView.model = new AdventureGame("VisualGame");
        }
    }

    /**
     * Loads the game from a file.
     *
     * @param gameFile The file to load.
     * @return The loaded AdventureGame instance.
     * @throws IOException if an I/O error occurs.
     * @throws ClassNotFoundException if the class of the serialized object cannot be found.
     */
    private static AdventureGame load(String gameFile) throws IOException, ClassNotFoundException {
        // Reading the object from a file
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(gameFile);
            in = new ObjectInputStream(file);
            return (AdventureGame) in.readObject();
        } finally {
            if (in != null) {
                in.close();
                file.close();
            }
        }
    }
}
