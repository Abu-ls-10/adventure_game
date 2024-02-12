package states;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import views.AdventureGameView;

/**
 * Represents a command to load a saved game.
 */
public class LoadCommand implements GameCommand {

    /**
     * Label used to display a prompt for selecting a game during the load operation.
     */
    Label selectGameLabel = new Label(String.format(""));

    /**
     * Represents the state of the LoadCommand.
     */
    String state;

    /**
     * ListView containing the list of available saved games.
     */
    ListView<String> gameList;

    /**
     * Constructs a LoadCommand with the default state "LOAD" and initializes the selectGameLabel and gameList.
     *
     * @param selectGameLabel The label used to display a prompt for selecting a game.
     * @param gameList        The ListView containing the list of available saved games.
     */
    public LoadCommand(Label selectGameLabel, ListView<String> gameList) {
        this.state = "LOAD";
        this.selectGameLabel = selectGameLabel;
        this.gameList = gameList;
    }

    /**
     * Executes the load command, invoking the loadGame method of the CommandReceiver.
     *
     * @param aGV The AdventureGameView instance on which the load operation is performed.
     */
    @Override
    public void execute(AdventureGameView aGV) {
        CommandReceiver.loadGame(selectGameLabel, gameList, aGV);
    }
}
