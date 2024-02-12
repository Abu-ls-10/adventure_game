package states;

import views.AdventureGameView;

/**
 * Represents a command to restart the game.
 */
public class RestartCommand implements GameCommand {

    /**
     * Represents the state of the RestartCommand.
     */
    String state;

    /**
     * Constructs a RestartCommand with the default state "RESTART".
     */
    public RestartCommand() {
        this.state = "RESTART";
    }

    /**
     * Executes the restart command, invoking the restartGame method of the CommandReceiver.
     *
     * @param aGV The AdventureGameView instance on which the restart operation is performed.
     */
    @Override
    public void execute(AdventureGameView aGV) {
        CommandReceiver.restartGame(aGV);
    }
}
