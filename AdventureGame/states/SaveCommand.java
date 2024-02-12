package states;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import views.AdventureGameView;

/**
 * Represents a command to save the game state.
 */
public class SaveCommand implements GameCommand {

    /**
     * Message indicating a successful save operation.
     */
    static String saveFileSuccess = "Saved Adventure Game!!";

    /**
     * Message indicating an error when attempting to save a file that already exists.
     */
    static String saveFileExistsError = "Error: File already exists";

    /**
     * Message indicating an error when attempting to save a file without the .ser extension.
     */
    static String saveFileNotSerError = "Error: File must end with .ser";

    /**
     * Label used to display error messages related to saving files.
     */
    public static Label saveFileErrorLabel = new Label("");

    /**
     * Text field used to input the desired save file name.
     */
    public static TextField saveFileNameTextField = new TextField("");

    /**
     * Represents the state of the SaveCommand.
     */
    String state;

    /**
     * Constructs a SaveCommand with the default state "SAVE".
     */
    public SaveCommand() {
        this.state = "SAVE";
    }

    /**
     * Executes the save command, invoking the saveGame method of the CommandReceiver.
     *
     * @param aGV The AdventureGameView instance on which the save operation is performed.
     */
    @Override
    public void execute(AdventureGameView aGV) {
        CommandReceiver.saveGame(aGV);
    }
}
