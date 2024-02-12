package controls;

import javafx.scene.input.KeyCode;
import AdventureModel.Player;
import AdventureModel.Position;

import java.util.concurrent.TimeUnit;

public class ArrowKeys implements ControlScheme{
    /**
     * The player of the gridGame.
     */
    private Player player;

    /**
     * Initializes the arrow keys control scheme
     * @param player -- the player that will be using this control scheme
     */
    public ArrowKeys(Player player){
        this.player = player;
    }

    /**
     * This method takes the key parameter to determine if the key is a valid move to move the character
     * @param key -- the keyboard command code
     * @return boolean -- if location of player updates, return true, otherwise false
     * @Precondtion: KeyCode key is not null
     * @Postcondtion: Updates whether the location is valid or not
     */
    public boolean move(KeyCode key){
        if (key == KeyCode.UP){
                return moveUp();
        } else if (key == KeyCode.DOWN) {
                return moveDown();

        } else if (key == KeyCode.LEFT) {
                return moveLeft();

        } else if (key == KeyCode.RIGHT) {
                return moveRight();
        }else{
                return false;
        }

    }

    /**
     * This method checks if moving up is possible
     * @return boolean -- if location of player updates, return true, otherwise false
     * @Precondtion: None
     * @Postcondtion: Updates whether the location is valid or not
     */
    public boolean moveUp(){
        Position new_loc = new Position(player.getCurrentPosition().getX(), player.getCurrentPosition().getY()-1);
        return player.updatePosition(new_loc);
    }
    /**
     * This method checks if moving down is possible
     * @return boolean -- if location of player updates, return true, otherwise false
     * @Precondtion: None
     * @Postcondtion: Updates whether the location is valid or not
     */
    public boolean moveDown(){
        Position new_loc = new Position(player.getCurrentPosition().getX(), player.getCurrentPosition().getY()+1);
        return player.updatePosition(new_loc);

    };
    /**
     * This method checks if moving right is possible
     * @return boolean -- if location of player updates, return true, otherwise false
     * @Precondtion: None
     * @Postcondtion: Updates whether the location is valid or not
     */
    public boolean moveRight(){
        Position new_loc = new Position(player.getCurrentPosition().getX()+1, player.getCurrentPosition().getY());
        return player.updatePosition(new_loc);

    };
    /**
     * This method checks if moving left is possible
     * @return boolean -- if location of player updates, return true, otherwise false
     * @Precondtion: None
     * @Postcondtion: Updates whether the location is valid or not
     */
    public boolean moveLeft(){
        Position new_loc = new Position(player.getCurrentPosition().getX()-1, player.getCurrentPosition().getY());
        return player.updatePosition(new_loc);

    };
}
