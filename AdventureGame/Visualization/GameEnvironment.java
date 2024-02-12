package Visualization;

import AdventureModel.AdventureObject;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents the game environment, handling rendering, placing/removing objects on the gameboard.
 */
public class GameEnvironment {
    private int width;
    private int height;
    private Inventory inventory;
    private CompositeElement rootElement;
    private Object[][] visualMatrix;
    private Object[][] boardMatrix;
    private GridPane gameBoard;
    private int[] playerLocation = new int[2];
    private int squareSize;
    private VisualElement player;
    private String[] colors = {"#D3D3D3", "#A3A3A3", "#000000"};  // Equivalent to [bgColor, wallColor, lineColor]
    public ArrayList<VisualElement> leafNodes = new ArrayList<>();


    /**
     * Constructs a GameEnvironment object.
     *
     * @param rootElement The root element for all visuals.
     * @param inventory The inventory of the game.
     * @param boardMatrix
     * @param squareSize The size of each square on the game board.
     * Precondition: rootElement, inventory are not null; width > 0; height > 0; squareSize > 0
     * Postcondition: A GameEnvironment object is created with the specified parameters.
     */
    public GameEnvironment(CompositeElement rootElement, Inventory inventory,
                           Object[][] boardMatrix, int squareSize) {
        // This class will handle rendering, placing/removing of objects on the gameboard.
        // This class represents an AST of the game environment, acting as a root to two children, the gameboard and the player
        this.rootElement = rootElement; // Root element for all visuals
        this.width = boardMatrix[0].length;
        this.height = boardMatrix.length;
        this.boardMatrix = boardMatrix;
        this.visualMatrix = new Object[this.height][this.width];
        this.inventory = inventory;
        this.squareSize = squareSize;
        this.player = (VisualElement) rootElement.getChildren().get(0).getChildren().get(0);
    }

    /**
     * Renders the entire game environment.
     * @Precondition None
     * @Postcondition The game environment is visually rendered.
     */
    public void renderEnvironment() {
        rootElement.render(); // Render the entire environment
        this.gameBoard = createGameBoard(this.boardMatrix);
        renderInventory();
    }

    /**
     * Updates the visual representation of the game environment.
     *
     * @param bgColor The background color.
     * @param wallColor The color for walls.
     * @param lineColor The color for grid lines.
     * @param colorScheme The color scheme to be applied
     * @Precondition All color parameters are not null.
     * @Post-condition The visual representation is updated based on the specified colors.
     */
    public void updateEnvironment(String bgColor,
                                  String wallColor,
                                  String lineColor, String colorScheme) {

        updateInventory(colorScheme);
        updateElements(colorScheme);
        this.colors = new String[]{bgColor, wallColor, lineColor};
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                Object ele = this.visualMatrix[row][col];
                Rectangle rectBG = new Rectangle(this.squareSize, this.squareSize);
                rectBG.setFill(Color.web(bgColor));
                rectBG.setStroke(Color.web(lineColor));
                this.gameBoard.add(rectBG, col, row);

                if (ele != null) {
                    if (ele instanceof VisualElement component) {
                        this.gameBoard.add(component.getDisplay(), col, row);

                    }
                    else if (ele.equals(-1)) {
                        Rectangle rectWall = new Rectangle(this.squareSize, this.squareSize);
                        rectWall.setFill(Color.web(wallColor));
                        rectWall.setStroke(Color.web(lineColor));
                        this.gameBoard.add(rectWall, col, row);
                    }
                    else if (ele.equals(0)) {
                        this.gameBoard.add(this.player.getDisplay(), col, row);

                    }
                }
            }
        }
        this.gameBoard.setStyle("-fx-grid-lines-color: " + lineColor.toLowerCase() + ";");
    }
    /**
     * Updates the image paths of the visual elements in the leafNodes collection based on a given color scheme.
     * @param colorScheme The color scheme to be applied to the visual elements. The method expects a string that corresponds
     *                    to the name of the directory containing the color scheme images.
     * Precondition: 'leafNodes' should be initialized and contain VisualElement objects. 'colorScheme' should be a valid string
     *               corresponding to a directory name within the image resources.
     * Postcondition: The image paths of the player and other visual elements in 'leafNodes' are updated to reflect the new color scheme.
     *                Elements are re-rendered with the updated images, and any element in the inventory is updated accordingly.
     */
    private void updateElements(String colorScheme) {
        for (VisualElement leaf : this.leafNodes) {
            if (leaf.isPlayer()) {
                int i = this.player.getImagePath().indexOf("playerImages") + 11;
                String imgPath = this.player.getImagePath().substring(0, i+1) + File.separator + colorScheme + ".png";
                this.player.setImagePath(imgPath);
                this.player.render();
            }
            else {
                // Change colour scheme by removing old image directory between index j - i
                String imgPath = newObjImgPath(colorScheme, leaf);
                leaf.setImagePath(imgPath);
                leaf.render();

                if (leaf.inInventory()) {
                    this.inventory.addItem(leaf);
                }
            }
        }
    }

    /**
     * Generates a new image path for a given visual element based on the specified color scheme.
     * @param colorScheme The color scheme to be applied. This should be a string corresponding to the name of a directory
     *                    within the image resources that contains images for the specified color scheme.
     * @param leaf The VisualElement object for which the image path is to be updated. This object should have properties
     *             like imagePath and caption properly initialized.
     * @return The updated image path string that incorporates the specified color scheme.
     * Precondition: The 'leaf' object should have valid 'imagePath' and 'caption' properties. The 'colorScheme' should be a valid string
     *               that corresponds to a directory name within the image resources.
     * Postcondition: Returns a string representing the new image path, but does not change the state of the 'leaf' object itself.
     */
    private String newObjImgPath(String colorScheme, VisualElement leaf) {
        int i = leaf.getImagePath().indexOf("objectImages") + 11 + File.separator.length();
        int j = leaf.getImagePath().indexOf(leaf.getCaption().toLowerCase());
        if (j == -1) {
            j = leaf.getImagePath().indexOf(leaf.getCaption());
        }
        return leaf.getImagePath().substring(0,i+1) + colorScheme +
                File.separator + leaf.getImagePath().substring(j);
    }
    /**
     * Updates the color scheme of the inventory's visual elements.
     * @param colorScheme The color scheme to be applied to the inventory. Accepts "default" or "scheme2" to set the background color to black,
     *                    and any other string will set the background color to white.
     * Precondition: The inventory and its rows should be initialized and contain Node objects. The colorScheme should be a valid string.
     * Postcondition: The background color of each inventory item is updated according to the specified color scheme. If an item in the inventory
     *                has a second child in its StackPane, that child is removed.
     */
    private void updateInventory(String colorScheme) {
        for (Node obj : this.inventory.getInventoryRow().getChildren()) {
            if (obj instanceof StackPane placeholder) {
                Rectangle rectBG = (Rectangle) placeholder.getChildren().get(0);
                if (colorScheme.equals("default") || colorScheme.equals("scheme2")) {
                    rectBG.setFill(Color.BLACK);
                }
                else {
                    rectBG.setFill(Color.WHITE);
                }
                if (placeholder.getChildren().size() == 2) {
                    placeholder.getChildren().remove(1);
                }
            }
        }
    }


    /**
     * Renders the inventory of the game.
     * @Precondition None
     * @Post-condition The inventory is visually rendered.
     */
    private void renderInventory() {
        for (VisualElement leaf : leafNodes) {
            if (leaf.inInventory()) {
                this.inventory.addItem(leaf);
            }
        }
    }

    /**
     * Creates the initial game board with random walls.
     *
     * @return The created game board as a GridPane.
     * @Precondition None
     * @Post-condition A new game board is created and returned.
     */
    public GridPane createGameBoard(Object[][] boardMatrix) {
        GridPane adventureBoard = new GridPane();
        adventureBoard.setAlignment(Pos.CENTER);
        adventureBoard.setGridLinesVisible(true);

        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                Rectangle square = new Rectangle(this.squareSize, this.squareSize);
                square.setFill(Color.LIGHTGRAY);
                square.setStroke(Color.BLACK);
                Object curr = boardMatrix[row][col];

                if (curr == null) {
                    adventureBoard.add(square, col, row);  // Add square to the grid
                }
                else if (curr.equals(-1)) {
                    square.setFill(Color.GRAY);
                    this.visualMatrix[row][col] = -1;  // Add -1 to indicate a block on the grid
                    adventureBoard.add(square, col, row);  // Add square to the grid
                }
                else if (curr.equals(0)) {
                    adventureBoard.add(square, col, row);  // Add square to the grid
                    this.playerLocation[0] = col;
                    this.playerLocation[1] = row;
                    adventureBoard.add(this.player.getDisplay(), col, row);
                }
                else if (curr instanceof AdventureObject obj) {
                    adventureBoard.add(square, col, row);  // Add square to the grid
                    displayPos(this.rootElement, col, row, obj, adventureBoard);
                }
            }
        }

        adventureBoard.setStyle("-fx-grid-lines-color: gray; -fx-background-color: #000000;");

        return adventureBoard;
    }

    /**
     *
     * @param root
     * @param x
     * @param y
     */
    private void displayPos(VisualComponent root, int x, int y, AdventureObject obj, GridPane gridPane) {
        if (root instanceof VisualElement leaf) {
            if (leaf.getCaption().toUpperCase().equals(obj.getName())){
                positionObject(leaf, x, y, gridPane);
            }
            if (!this.leafNodes.contains(leaf)) {
                this.leafNodes.add(leaf);
            }
        }
        else {
            for (VisualComponent subtree : root.getChildren()) {
                displayPos(subtree, x, y, obj, gridPane);
            }
        }
    }

    /**
     * Positions the image of a visual element on the game board.
     *
     * @param component The visual element to be positioned.
     * @return True if the image is successfully positioned, false otherwise.
     * @Precondition component is not null.
     * @Post-condition The image of the visual element is positioned on the game board.
     */
    private void positionObject(VisualElement component, int x, int y, GridPane gridPane) {
        if (component.onBoard()) {
            this.visualMatrix[y][x] = component;
            gridPane.add(component.getDisplay(), x, y);
        }
        else {
            this.inventory.addItem(component);
        }
    }
    /**
     * Retrieves the current player.
     *
     * @return A Visual Element representing the Player
     */
    public VisualElement getPlayerVisual() {
        return this.player;
    }

    /**
     * Retrieves the current location of the player on the game board.
     *
     * @return An array containing the x and y coordinates of the player.
     */
    public int[] getPlayerLocation() {
        return new int[] {this.playerLocation[0], this.playerLocation[1]};
    }

    /**
     * Sets the location of the player on the game board.
     *
     * @param x The x-coordinate for the new player location.
     * @param y The y-coordinate for the new player location.
     * @Precondition x, y are valid coordinates on the game board.
     * @Post-condition The player's location on the game board is updated.
     */
    public void setPlayerLocation(int x, int y) {
        int oldX = this.playerLocation[0];
        int oldY = this.playerLocation[1];

        Rectangle square = new Rectangle(this.squareSize, this.squareSize);
        square.setFill(Color.web(this.colors[0]));
        square.setStroke(Color.web(this.colors[2]));
        this.gameBoard.add(square, oldX, oldY);
        this.visualMatrix[oldY][oldX] = null;

        this.playerLocation[0] = x;
        this.playerLocation[1] = y;
        this.gameBoard.add(this.player.getDisplay(), x, y);
        this.visualMatrix[y][x] = 0;
        this.gameBoard.setGridLinesVisible(true);
    }

    /**
     * Retrieves the matrix representing the game board.
     *
     * @return The matrix representing the game board.
     */
    public Object[][] getVisualMatrix() {
        return visualMatrix;
    }

    /**
     * Retrieves the game board as a GridPane.
     *
     * @return The game board as a GridPane.
     */
    public GridPane getGameBoard() {
        return this.gameBoard;
    }

    /**
     * Retrieves the width of the game board.
     *
     * @return The width of the game board.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Retrieves the height of the game board.
     *
     * @return The height of the game board.
     */
    public int getHeight() {
        return this.height;
    }
}
