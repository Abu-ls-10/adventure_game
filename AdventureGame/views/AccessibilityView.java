package views;

import Strategies.LowSightedBehaviour;
import Strategies.NonSightedBehaviour;
import Strategies.SightedBehaviour;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.Objects;


public class AccessibilityView {
    private AdventureGameView adventureGameView;
    private Label combinationLabel, audioLabel;
    private final Button combinationButtonOne;
    private final Button combinationButtonTwo;
    private Button combinationButtonThree;
    private Button deafultCombinationButton;

    private Button closeWindowButton, audioSettings, audioOff;

    private Boolean soundState = false;


    /**
     * AccessibilityView Constructor
     * __________________________
     * Initializes attributes
     * Constructs an AccessibilityView associated with the AdventureGameView.
     * Initializes the pop-up settings box and sets the UI
     *
     * @param adventureGameView The AdventureGameView instance.
     */
    public AccessibilityView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");


        combinationLabel = new Label();
        combinationLabel.setStyle("-fx-text-fill: white;");
        combinationLabel.setFont(new Font("Arial", 16));
        combinationLabel.setAlignment(Pos.CENTER);
        combinationLabel.setText("Color Palettes: (Background, Object, Wall, Player, Line)");

        combinationButtonOne = new Button("White, Black, Yellow, Red, and Grey color scheme");
        combinationButtonOne.setId("White, Yellow, Grey, scheme1, placeholder");
        combinationButtonOne.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        combinationButtonOne.setPrefSize(400, 50);
        combinationButtonOne.setFont(new Font(16));
        combinationButtonOne.setOnAction(e -> this.setStrategy("LS", combinationButtonOne.getId()));
        AdventureGameView.makeButtonAccessible(combinationButtonOne,
                "Color Scheme One",
                "This is a button to change the game color scheme",
                "Use this button to change the color scheme to White, Black, Yellow, Red and Grey.");
        setOnEnterEvent(combinationButtonOne);

        combinationButtonTwo = new Button("Black, White, Green, Red, and Yellow color scheme");
        combinationButtonTwo.setId("Black, Green, Yellow, scheme2, Placeholder");
        combinationButtonTwo.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        combinationButtonTwo.setPrefSize(400, 50);
        combinationButtonTwo.setFont(new Font(16));
        combinationButtonTwo.setOnAction(e -> this.setStrategy("LS", combinationButtonTwo.getId()));
        AdventureGameView.makeButtonAccessible(combinationButtonTwo,
                "Color Scheme Two",
                "This is a button to change the game color scheme",
                "Use this button to change the color scheme to Black, White, Green, Red, and Yellow.");
        setOnEnterEvent(combinationButtonTwo);

        combinationButtonThree = new Button("Black and White color scheme");
        combinationButtonThree.setId("White, Black, Grey, uncolored, Placeholder");
        combinationButtonThree.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        combinationButtonThree.setPrefSize(400, 50);
        combinationButtonThree.setFont(new Font(16));
        combinationButtonThree.setOnAction(e -> this.setStrategy("LS", combinationButtonThree.getId()));
        AdventureGameView.makeButtonAccessible(combinationButtonThree,
                "Black and White color Scheme",
                "This is a button to switch color scheme to black and white",
                "Use this button to change the game colors to be in black and white.");
        setOnEnterEvent(combinationButtonThree);

        deafultCombinationButton =  new Button("Default colors");
        deafultCombinationButton.setId("LightGrey, Grey, Black, default, Placeholder");
        deafultCombinationButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        deafultCombinationButton.setPrefSize(400, 50);
        deafultCombinationButton.setFont(new Font(16));
        deafultCombinationButton.setOnAction(e -> this.setStrategy("LS", deafultCombinationButton.getId()));
        AdventureGameView.makeButtonAccessible(deafultCombinationButton,
                "Default colors",
                "This is a button to revert colors to default",
                "Use this button to revert the colors to the default state which was at the beginning of the game.");
        setOnEnterEvent(deafultCombinationButton);

        audioLabel = new Label("Turn Audio On/Off");
        audioLabel.setStyle("-fx-text-fill: white;");
        audioLabel.setFont(new Font("Arial", 16));
        audioLabel.setAlignment(Pos.CENTER);


        audioSettings = new Button("Audio On");
        audioSettings.setId("Audio ON");
        audioSettings.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        audioSettings.setPrefSize(150, 50);
        audioSettings.setFont(new Font(16));
        audioSettings.setOnAction(e -> this.setStrategy("NS", audioSettings.getText()));
        addAudioControlEvent(audioSettings);
        AdventureGameView.makeButtonAccessible(audioSettings,
                "Audio Setting",
                "This is a button to turn On the audio",
                "Use this button to enable audio settings.");

        audioOff = new Button("Audio Off");
        audioOff.setId("Audio Off");
        audioOff.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        audioOff.setPrefSize(150, 50);
        audioOff.setFont(new Font(16));
        audioOff.setOnAction(e -> this.setStrategy("NS", audioOff.getText()));
        audioOff.setOnAction(e-> updateAudioSettings(audioOff));
        AdventureGameView.makeButtonAccessible(audioOff,
                "Audio Setting",
                "This is a button to turn off the audio",
                "Use this button to disable audio settings.");
        audioOff.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    updateAudioSettings(audioOff);
                }
            }
        });


        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        addClosewindowEvent(dialog);
        AdventureGameView.makeButtonAccessible(closeWindowButton,
                "close window",
                "This is a button to close the save game window",
                "Use this button to close the save game window.");

        VBox saveGameBox = new VBox(10, combinationLabel, combinationButtonOne, combinationButtonTwo,
                combinationButtonThree, deafultCombinationButton,
                audioLabel, audioSettings, audioOff, closeWindowButton);
        saveGameBox.setAlignment(Pos.CENTER);

        dialogVbox.getChildren().add(saveGameBox);
        Scene dialogScene = new Scene(dialogVbox, 600, 400);
        dialog.setScene(dialogScene);
        dialog.show();

        if (adventureGameView.nSB != null){
            audioOff.setDisable(false);
            audioSettings.setDisable(true);
        }
        else {
            audioOff.setDisable(true);
            audioSettings.setDisable(false);
        }
    }


    /**
     * This method creates an event handler which handles the case where any
     * of the combination buttons are selected using the enter key.
     */
    private void setOnEnterEvent(Button button) {
        button.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    setStrategy("LS", button.getId());
                }
            }
        });
    }

    /**
     * This method creates an event handler which handles the case where any
     * of the combination buttons are selected using the enter key.
     */
    public void setStrategy(String strategy, String combination){
        if (strategy.equals("LS")){
            LowSightedBehaviour lSB = new LowSightedBehaviour(adventureGameView.gameEnv, combination);
        }
        if (strategy.equals("NS")){
            NonSightedBehaviour nSB = new NonSightedBehaviour(adventureGameView.gameEnv);
        }
        if (strategy.equals("S")){
            SightedBehaviour sB = new SightedBehaviour(adventureGameView.gameEnv);
        }
    }

    /**
     * addAudioControlEvent
     * Adds an event handler for updating audio settings to a button. when inside the AccessibilityView.
     * @param button The button for which the event is added.
     */
    public void addAudioControlEvent(Button button){
        audioSettings.setOnAction(e -> updateAudioSettings(button));
        audioSettings.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    updateAudioSettings(button);
                }
            }
        });

    }

    /**
     * Updates the audio settings based on the button state.
     * If audio is turned on the audioSettings button is disabled and if audio is off then audioSettings button
     * is enabled. the vice versa is true for the audioOff button.
     *
     * @param button The button representing audio settings.
     */
    public void updateAudioSettings(Button button){
        if(Objects.equals(button.getText(), "Audio On")){
            adventureGameView.nSB = new NonSightedBehaviour(adventureGameView.gameEnv);
            adventureGameView.nSB.speak("Sound has been turned on");
            audioSettings.setDisable(true);
            audioOff.setDisable(false);
            audioOff.setFocusTraversable(true);

        }
        else{
            if (adventureGameView.nSB != null){adventureGameView.nSB.speak("Sound has been turned off");
            }
            adventureGameView.nSB = null;
            audioSettings.setDisable(false);
            audioOff.setDisable(true);

        }

    }

    /**
     * Adds an event handler for closing the window when inside the AccessibilityView.
     *
     * @param dialog The stage representing the window to be closed.
     */
    public void addClosewindowEvent(Stage dialog){
        closeWindowButton.setOnAction(e -> dialog.close());
        closeWindowButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    dialog.close();
                }
            }
        });
    }
}

