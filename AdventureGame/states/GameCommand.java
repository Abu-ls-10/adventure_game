package states;

import views.AdventureGameView;

/**
 * Interface representing a game command.
 */
public interface GameCommand {

    /**
     * Executes the game command.
     *
     * @param aGV The AdventureGameView instance on which the command is executed.
     */
    void execute(AdventureGameView aGV);
}
