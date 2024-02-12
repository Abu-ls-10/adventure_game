package AdventureModel;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.*;

/**
 * Class AdventureGame.  Handles all the necessary tasks to run the Adventure game.
 */
public class AdventureGame implements Serializable {
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 10;
    private final String directoryName; //An attribute to store the Introductory text of the game.
    private String helpText; //A variable to store the Help text of the game. This text is displayed when the user types "HELP" command.
    private final HashMap<Integer, Room> rooms; //A list of all the rooms in the game.
    public Player player; //The Player of the game.
    private ArrayList<Wall> walls = new ArrayList<>();
    private Object[][] boardMatrix = new Object[GRID_HEIGHT][GRID_WIDTH];

    /**
     * Adventure Game Constructor
     * __________________________
     * Initializes attributes
     *
     * @param name the name of the adventure
     */
    public AdventureGame(String name){
        this.rooms = new HashMap<>();
        this.directoryName = "Games" + File.separator + name; //all games files are in the Games directory!
        try {
            setUpGame();
        } catch (IOException e) {
            throw new RuntimeException("An Error Occurred: " + e.getMessage());
        }
    }

    public AdventureGame(String prefix, String name) {
        this.rooms = new HashMap<>();
        this.directoryName = prefix + File.separator + "Games" + File.separator + name; //all games files are in the Games directory!
        try {
            setUpGame();
        } catch (IOException e) {
            throw new RuntimeException("An Error Occurred: " + e.getMessage());
        }
    }

    /**
     * Save the current state of the game to a file
     * 
     * @param file pointer to file to write to
     */
    public void saveModel(File file) {
        try {
            FileOutputStream outfile = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(outfile);
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setUpGame
     * __________________________
     *
     * @throws IOException in the case of a file I/O error
     */
    public void setUpGame() throws IOException {

        String directoryName = this.directoryName;
        AdventureLoader loader = new AdventureLoader(this, directoryName);
        loader.loadGame();

        // set up the player's current location
        this.player = new Player(this.rooms.get(1));
        initializeMatrix();
    }

    public void initializeMatrix() {
        // Set up the random number generator
        Random random = new Random();

        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {

                // Add a random wall with 20% probability
                if (random.nextDouble() < 0.2) {
                    Wall wall = new Wall(row, col);
                    this.walls.add(wall);
                    this.boardMatrix[row][col] = -1;  // Add -1 to indicate a wall on the grid
                }
            }
        }

        positionMatrix(this.player, 0);

        for (int room : this.rooms.keySet()) {
            Room curr = this.rooms.get(room);
            for (AdventureObject obj : curr.objectsInRoom) {
                positionMatrix(obj, obj);
            }
        }

    }

    public boolean positionMatrix(Object obj, Object val) {
        Random random = new Random();
        int randX = random.nextInt(GRID_WIDTH);
        int randY = random.nextInt(GRID_HEIGHT);

        if (this.boardMatrix[randY][randX] == null) {
            if (obj instanceof Player) {
                this.player.setCurrentPosition(new Position(randX, randY));
            }
            else if (obj instanceof AdventureObject advObj) {
                advObj.setPosition(randX, randY);
            }
            this.boardMatrix[randY][randX] = val;
            return true;
        }
        else {
            return positionMatrix(obj, val);
        }
    }

    public void updateMatrix(Object[][] newMatrix) {
        //this.walls.removeAll();
        this.boardMatrix = newMatrix;
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {

                Object curr = boardMatrix[row][col];

                if (curr == null) {
                    continue;
                }
                else if (curr.equals(0)) {
                    this.player.setCurrentPosition(new Position(col, row));
                }
//                else if (curr.equals(-1)) {
//                    Wall wall = new Wall(row, col);
//                    this.walls.add(wall);
//                }
            }
        }
    }



    /**
     * getDirectoryName
     * __________________________
     * Getter method for directory 
     * @return directoryName
     */
    public String getDirectoryName() {
        return this.directoryName;
    }

    /**
     * getInstructions
     * __________________________
     * Getter method for instructions 
     * @return helpText
     */
    public String getInstructions() {
        return helpText;
    }

    /**
     * getPlayer
     * __________________________
     * Getter method for Player 
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * getRooms
     * __________________________
     * Getter method for rooms 
     * @return map of key value pairs (integer to room)
     */
    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    /**
     * setHelpText
     * __________________________
     * Setter method for helpText
     * @param help which is text to set
     */
    public void setHelpText(String help) {
        this.helpText = help;
    }

    public Object[][] getBoardMatrix() {
        return this.boardMatrix;
    }


}
