package AdventureModel;

import java.io.Serializable;

public class Position implements Serializable {
    /**
     * The x cooridinate
     */
    private int x;
    /**
     * The y cooridinate
     */
    private int y;

    /**
     * Creates a new Location object
     * @param x -- the x coordinate
     * @param y -- the y coordinate
     */
    public Position(int x, int y){
        this.x = x;
        this.y  = y;
    }


    /**
     * @return x -- returns this x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return y -- returns this y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets a new x cooridinate
     * @param x -- the new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets a new y cooridinate
     * @param y -- the new y cooridnate
     */
    public void setY(int y) {
        this.y = y;
    }

}