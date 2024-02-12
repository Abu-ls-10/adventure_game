package AdventureModel;

import java.io.Serializable;

public class Wall implements Serializable {

    Position position;

    public Wall(int x, int y) {
        this.position = new Position(x, y);
    }

    public int getX() {
        return this.position.getX();
    }

    public void setX(int newX) {
        this.position.setX(newX);
    }

    public int getY() {
        return this.position.getY();
    }

    public void setY(int newY) {
        this.position.setY(newY);
    }
}
