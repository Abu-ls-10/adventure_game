package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import states.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static states.SaveCommand.*;

public class GameStateView {

    // Save Command Fields
    private GameCommand saveCommand;
    private Label saveGameLabel = new Label(String.format("Enter name of file to save"));
    private Button saveGameButton = new Button("Save Game");
    private Button saveCloseWindowButton = new Button("Close Window");

    // Load Command Fields
    private GameCommand loadCommand;
    private Label selectGameLabel;
    private Button selectGameButton;
    private Button loadCloseWindowButton;

    private ListView<String> GameList;
    private String filename = null;

    // Restart Command Fields
    private GameCommand restartCommand;

    // Pause Command Fields
    private GameCommand pauseResumeCommand;


    // Other Fields
    private AdventureGameView adventureGameView;
    private Button pauseResumeButton, restartButton, saveButton, loadButton;
    private boolean isPaused = false;

    public GameStateView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        final Stage mainDialog = new Stage();
        mainDialog.initModality(Modality.APPLICATION_MODAL);
        mainDialog.initOwner(adventureGameView.stage);

        // Create buttons
        this.pauseResumeButton = new Button("Pause");
        //pauseButton.setGraphic();
        AdventureGameView.customizeButton(pauseResumeButton, 100, 40);
        AdventureGameView.makeButtonAccessible(pauseResumeButton,
                "Pause and Resume Button",
                "This button pauses and resumes the game.",
                "This button pauses the game. Click on it to pause the game. Click again to resume.");

        this.restartButton = new Button("Restart");
        //restartButton.setGraphic();
        AdventureGameView.customizeButton(restartButton, 100, 40);
        AdventureGameView.makeButtonAccessible(restartButton,
                "Restart Button",
                "This button restarts the game.",
                "This button restarts the game by starting a new game. Click on it to start a new game.");

        this.saveButton = new Button("Save");
        AdventureGameView.customizeButton(saveButton, 100, 40);
        AdventureGameView.makeButtonAccessible(saveButton,
                "Save Button",
                "This button saves the game.",
                "This button saves the game. Click it in order to save your current progress, so you can play more later.");

        this.loadButton = new Button("Load");
        AdventureGameView.customizeButton(loadButton, 100, 40);
        AdventureGameView.makeButtonAccessible(loadButton,
                "Load Button",
                "This button loads a game from a file.",
                "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");


        VBox mainVboxLeft = new VBox(pauseResumeButton, restartButton);
        mainVboxLeft.setPadding(new Insets(20, 20, 20, 20));
        mainVboxLeft.setStyle("-fx-background-color: #121212;");
        mainVboxLeft.setSpacing(20);

        VBox mainVboxRight = new VBox(saveButton, loadButton);
        mainVboxRight.setPadding(new Insets(20, 20, 20, 20));
        mainVboxRight.setStyle("-fx-background-color: #121212;");
        mainVboxRight.setSpacing(20);

        HBox mainHBoxRoot = new HBox(mainVboxLeft, mainVboxRight);
        mainHBoxRoot.setPadding(new Insets(50, 20, 50, 20));
        mainHBoxRoot.setStyle("-fx-background-color: #121212;");
        mainHBoxRoot.setSpacing(20);
        mainHBoxRoot.setAlignment(Pos.CENTER);

        Scene mainDialogScene = new Scene(mainHBoxRoot, 400, 300);
        mainDialog.setScene(mainDialogScene);
        mainDialog.show();


        pauseResumeCommandSetup();
        restartCommandSetup();
        saveCommandSetup();
        loadCommandSetup();
    }

    public void saveCommandSetup() {
        saveButton.setOnAction(e1 -> {
            saveCommand = new SaveCommand();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(adventureGameView.stage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.setPadding(new Insets(20, 20, 20, 20));
            dialogVbox.setStyle("-fx-background-color: #121212;");
            saveGameLabel.setId("SaveGame"); // DO NOT MODIFY ID
            saveFileErrorLabel.setId("SaveFileErrorLabel");
            saveFileNameTextField.setId("SaveFileNameTextField");
            saveGameLabel.setStyle("-fx-text-fill: #e8e6e3;");
            saveGameLabel.setFont(new Font(16));
            saveFileErrorLabel.setStyle("-fx-text-fill: #e8e6e3;");
            saveFileErrorLabel.setFont(new Font(16));
            saveFileNameTextField.setStyle("-fx-text-fill: #000000;");
            saveFileNameTextField.setFont(new Font(16));

            String gameName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser";
            saveFileNameTextField.setText(gameName);

            saveGameButton = new Button("Save board");
            saveGameButton.setId("SaveBoardButton"); // DO NOT MODIFY ID
            saveGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            saveGameButton.setPrefSize(200, 50);
            saveGameButton.setFont(new Font(16));
            AdventureGameView.makeButtonAccessible(saveGameButton, "save game", "This is a button to save the game", "Use this button to save the current game.");
            saveGameButton.setOnAction(e2 -> this.saveCommand.execute(adventureGameView));

            saveCloseWindowButton = new Button("Close Window");
            saveCloseWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
            saveCloseWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            saveCloseWindowButton.setPrefSize(200, 50);
            saveCloseWindowButton.setFont(new Font(16));
            saveCloseWindowButton.setOnAction(e3 -> dialog.close());
            AdventureGameView.makeButtonAccessible(saveCloseWindowButton, "close window", "This is a button to close the save game window", "Use this button to close the save game window.");

            VBox saveGameBox = new VBox(10, saveGameLabel, saveFileNameTextField, saveGameButton, saveFileErrorLabel, saveCloseWindowButton);
            saveGameBox.setAlignment(Pos.CENTER);

            dialogVbox.getChildren().add(saveGameBox);
            Scene dialogScene = new Scene(dialogVbox, 400, 400);
            dialog.setScene(dialogScene);
            dialog.show();
        });
    }

    public void loadCommandSetup() {
        loadButton.setOnAction(e1 -> {
            selectGameLabel = new Label(String.format(""));
            GameList = new ListView<>(); //to hold all the file names

            final Stage dialog = new Stage(); //dialogue box
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(adventureGameView.stage);

            VBox dialogVbox = new VBox(20);
            dialogVbox.setPadding(new Insets(20, 20, 20, 20));
            dialogVbox.setStyle("-fx-background-color: #121212;");
            selectGameLabel.setId("CurrentGame"); // DO NOT MODIFY ID
            GameList.setId("GameList");  // DO NOT MODIFY ID
            GameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            getFiles(GameList); //get files for file selector
            selectGameButton = new Button("Change Game");
            selectGameButton.setId("ChangeGame"); // DO NOT MODIFY ID
            AdventureGameView.makeButtonAccessible(selectGameButton, "select game", "This is the button to select a game", "Use this button to indicate a game file you would like to load.");

            loadCloseWindowButton = new Button("Close Window");
            loadCloseWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
            loadCloseWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            loadCloseWindowButton.setPrefSize(200, 50);
            loadCloseWindowButton.setFont(new Font(16));
            loadCloseWindowButton.setOnAction(e2 -> dialog.close());
            AdventureGameView.makeButtonAccessible(loadCloseWindowButton, "close window", "This is a button to close the load game window", "Use this button to close the load game window.");

            loadCommand = new LoadCommand(selectGameLabel, GameList);
            //on selection, do something
            selectGameButton.setOnAction(e3 -> loadCommand.execute(adventureGameView));

            VBox selectGameBox = new VBox(10, selectGameLabel, GameList, selectGameButton);

            // Default styles which can be modified
            GameList.setPrefHeight(100);
            selectGameLabel.setStyle("-fx-text-fill: #e8e6e3");
            selectGameLabel.setFont(new Font(16));
            selectGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            selectGameButton.setPrefSize(200, 50);
            selectGameButton.setFont(new Font(16));
            selectGameBox.setAlignment(Pos.CENTER);
            dialogVbox.getChildren().add(selectGameBox);
            Scene dialogScene = new Scene(dialogVbox, 400, 400);
            dialog.setScene(dialogScene);
            dialog.show();
        });
    }

    public void pauseResumeCommandSetup() {
        pauseResumeCommand = new PauseResumeCommand("PAUSE");
        pauseResumeButton.setOnAction(e -> {
            pauseResumeCommand.execute(adventureGameView);
            isPaused = !isPaused;
            if (isPaused) {
//                ((PauseResumeCommand) pauseResumeCommand).setState("RESUME");
//                pauseResumeButton.setText("Resume");
                pauseResumeButton.setDisable(true);
            }
            else {
//                ((PauseResumeCommand) pauseResumeCommand).setState("PAUSE");
//                pauseResumeButton.setText("Pause");

            }
        });
    }

    public void restartCommandSetup() {
        restartCommand = new RestartCommand();
        restartButton.setOnAction(e -> restartCommand.execute(adventureGameView));
    }

    /**
     * Get Files to display in the on screen ListView
     * Populate the listView attribute with .ser file names
     * Files will be located in the Games/Saved directory
     *
     * @param listView the ListView containing all the .ser files in the Games/Saved directory.
     */
    private void getFiles(ListView<String> listView) {
        File saved = new File("Games" + File.separator + "Saved");  // TODO: Uncomment if opening in <AdventureGame>
        // File saved = new File("AdventureGame" + File.separator + "Games" + File.separator + "Saved");  // TODO: Uncomment if opening in <group_89>

        if (!(saved.exists() && saved.isDirectory())) {
            saved.mkdir();
        }

        File[] files = saved.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".ser")) {
                    listView.getItems().add(file.getName());
                }
            }
        }
    }
}
