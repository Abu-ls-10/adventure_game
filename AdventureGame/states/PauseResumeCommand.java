package states;

import views.AdventureGameView;

/**
 * Represents a command to pause or resume the game.
 */
public class PauseResumeCommand implements GameCommand {

    /**
     * Represents the state of the PauseResumeCommand.
     */
    String state;

    /**
     * Constructs a PauseResumeCommand with the specified initial state.
     *
     * @param state The initial state of the PauseResumeCommand.
     */
    public PauseResumeCommand(String state) {
        this.state = state;
    }

    /**
     * Executes the pause or resume command based on the current state,
     * invoking either the pauseGame or resumeGame method of the CommandReceiver.
     *
     * @param aGV The AdventureGameView instance on which the pause or resume operation is performed.
     */
    @Override
    public void execute(AdventureGameView aGV) {
        if (this.state.equals("PAUSE")) {
            CommandReceiver.pauseGame(aGV);
        } else {
            CommandReceiver.resumeGame(aGV);
        }
    }

    /**
     * Sets the new state for the PauseResumeCommand.
     *
     * @param newState The new state to set.
     */
    public void setState(String newState) {
        this.state = newState;
    }
}
