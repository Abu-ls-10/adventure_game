package controls;


import javafx.scene.input.KeyCode;

public interface ControlScheme{

    boolean move(KeyCode key);
    boolean moveUp();
    boolean moveDown();
    boolean moveLeft();
    boolean moveRight();
}