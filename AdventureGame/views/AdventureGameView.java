package views;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import Strategies.NonSightedBehaviour;
import AdventureModel.Position;
import Visualization.*;
import controls.AWSDKeys;
import controls.ArrowKeys;
import controls.ControlScheme;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.AccessibleRole;
//import marytts.exceptions.MaryConfigurationException;

import java.io.File;
import java.util.*;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * VIDEO LINK: https://tinyurl.com/ansar136s-gameplay
 */
public class AdventureGameView {
    private static final int SQUARE_SIZE = 45;
    public AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered
    //Button gameStateButton, saveButton, loadButton, helpButton, controlButton; //buttons
    Button accessibilityButton, gameStateButton, helpButton, controlButton; //buttons
    Boolean helpToggle = false; //is help on display?

    GridPane gridPane = new GridPane(); //to hold images and buttons
    GridPane gameBoard = new GridPane();
    Label outputText = new Label(); //to hold room description and/or instructions
    ArrayList<AdventureObject> objectsInRoom; //to hold room items
    ArrayList<AdventureObject> objectsInInventory; //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input
    GameEnvironment gameEnv;
    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing
    Inventory inventory;

    Object[][] boardMatrix;
    Object[][] visualMatrix;
    public VBox gamePane;
    ControlScheme controlScheme;
    private VisualElement playerVisual; // to aid in player movement in terms of visuals
    private CompositeElement rootNode; // to keep track of objects in inventory, and on board
    public NonSightedBehaviour nSB = null;

    /**
     * Adventure Game View Constructor
     * Initializes attributes and sets up the UI.
     *
     * @param model The AdventureGame instance.
     * @param stage The JavaFX Stage for rendering.
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initializes the UI components and sets up the stage.
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("group89's Adventure Game");

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(950); // Only 1 column needed

        // Row constraints
        RowConstraints row1 = new RowConstraints(); // Row for the game state controls and instructions
        RowConstraints row2 = new RowConstraints(500); // Row for game display
        RowConstraints row3 = new RowConstraints(); // Row for inventory display

        row1.setVgrow(Priority.SOMETIMES);
        row3.setVgrow(Priority.SOMETIMES);

        gridPane.getColumnConstraints().addAll(column1);
        gridPane.getRowConstraints().addAll(row1, row2, row3);

        // Buttons
        accessibilityButton = new Button("Accessibility");
        accessibilityButton.setId("ChangeStrategy");
        customizeButton(accessibilityButton, 200, 50);
        makeButtonAccessible(accessibilityButton, "Accessibility Button", "This button has many Accessibility features.", "This button has many Accessibility features. Click it in order to make the game more accessible.");
        addAccessibilityEvent();

        gameStateButton = new Button("Game States");
        gameStateButton.setId("GameStates");
        customizeButton(gameStateButton, 150, 50);
        //makeButtonAccessible(gameStateButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addGameStateEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        controlButton = new Button("Arrow Keys");
        controlButton.setId("Control Scheme");
        customizeButton(controlButton, 150, 50);
        makeButtonAccessible(helpButton, "Switch control Button", "This button switches the games control scheme.",
                "Click to switch the control scheme.");
        addSwitchControlSchemeEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(accessibilityButton, helpButton, gameStateButton, controlButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        // Add all the widgets to the GridPane
        gridPane.add(topButtons, 0, 0, 1, 1);  // Add buttons

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

        accessibilityButton.requestFocus();

        updateText(""); //method displays an image and whatever text is supplied
        updateScene(); //update items shows inventory and objects in rooms
    }

    /**
     * Generate the element tree for the game environment.
     * This method constructs a CompositeElement tree representing the visual elements in the game environment,
     * including the player, inventory, and objects on the game board.
     *
     * @return The root CompositeElement of the generated element tree.
     */
    private CompositeElement generateElementTree() {
        CompositeElement root = new CompositeElement("root");
        CompositeElement playerNode = new CompositeElement("playerNode");
        CompositeElement inventoryNode = new CompositeElement("inventoryNode");
        CompositeElement boardNode = new CompositeElement("boardNode");

        ArrayList<VisualElement> leaves = generateLeaves();
        for (VisualElement leaf : leaves) {

            if (leaf.inInventory()) {
                inventoryNode.addChild(leaf);
            }
            else {
                boardNode.addChild(leaf);
            }
        }
        VisualElement playerElement = new VisualElement(this.model.getDirectoryName() +
                File.separator + "playerImages" + File.separator + "DEFAULT.png");
        playerNode.addChild(playerElement);
        playerNode.addChild(inventoryNode);

        root.addChild(playerNode);
        root.addChild(boardNode);

        return root;
    }

    /**
     * Generate the leaves of the element tree.
     * This method creates VisualElement instances for each object in the game, distinguishing between objects in
     * the inventory and objects on the game board. It then adds these VisualElements to an ArrayList.
     *
     * @return An ArrayList of VisualElement instances representing the objects in the game.
     */
    private ArrayList<VisualElement> generateLeaves() {

        ArrayList<VisualElement> res = new ArrayList<>();
        File[] objects = getObjectImages("default");  // List of object image files
        for (File file : objects) {
            // Extract object name from file
            int extensionIndex = file.getName().indexOf(".");
            String objName = file.getName().substring(0, extensionIndex).toUpperCase();
            Object[] inRoom = inRoom(objName);
            Object[] inInventory = inInventory(objName);

            AdventureObject obj;
            VisualElement objElement;
            if (!((boolean) inRoom[1] || (boolean) inInventory[1])) {
                continue;
            } else {
                if ((boolean) inRoom[1]) {
                    obj = (AdventureObject) inRoom[0];
                    objElement = new VisualElement(obj, file.getPath(), "BOARD");
                } else {
                    obj = (AdventureObject) inInventory[0];
                    objElement = new VisualElement(obj, file.getPath(), "INVENTORY");
                }
            }
            res.add(objElement);
        }
        return res;
    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    public static void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * Adds the event handler to all keyboard events on the grid.
     * If AWSD or arrow keys are clicked, the character must move on the grid visually.
     * @Precondition everything on game set up
     * @Postcondition The player's location on the game board is updated.
     */
    private void addGridHandlingEvent(){
        // CHANGE THE "gridpane. " if needed
        updateText("");
        stage.getScene().setOnKeyReleased(event -> {
                    // save location
                    int x  = model.player.getCurrentPosition().getX();
                    int y = model.player.getCurrentPosition().getY();
                    if (controlScheme.move(event.getCode())) { // character movement
                        int new_x = model.player.getCurrentPosition().getX();
                        int new_y = model.player.getCurrentPosition().getY();
                        if (visualMatrix[new_y][new_x] != null){
                            // this means the player intends to land on box or Object, NOT ALLOWED
                            model.player.setCurrentPosition(new Position(x, y)); // set back to old location
                            updateText("Cannot move to this direction!");
                        } else{
                            updatePlayer();
                            // Update the game grid in AdventureModel (back-end)
                            this.boardMatrix[y][x] = null;
                            this.boardMatrix[new_y][new_x] = 0;
                            this.model.updateMatrix(boardMatrix);

                            // Update the game grid in GameEnvironment (front-end)
                            this.visualMatrix[y][x] = null;
                            this.visualMatrix[new_y][new_x] = 0;

                            // Display updated message
                            updateText("You are currently on position " + (new_x+1) + ", " + (new_y+1));
                        }
                    } // if move returns false we do not do anything on the grid FOR NOW
                    else if (event.getCode() == KeyCode.T) {// to take object for inventory
                        int [] playerLocation = this.gameEnv.getPlayerLocation();

                        for(int i = 0; i < visualMatrix.length ; i ++){
                            for(int j = 0; j < boardMatrix[i].length; j++){
                                if(visualMatrix[i][j] instanceof VisualElement && ((VisualElement) visualMatrix[i][j]).onBoard()){
                                    if( Math.sqrt(Math.pow(playerLocation[0] - j, 2) + Math.pow(playerLocation[1] - i, 2)) <= 1){
                                        updateInventory((VisualElement) visualMatrix[i][j], i, j);
                                        visualMatrix[i][j] = null;
                                        return;
                                    }
                                }
                            }
                        }
                        // outside of for loop means no object is
                        if(!this.objectsInRoom.isEmpty()){
                            // that means object to far
                            updateText("Object too far away!");
                        } else{
                            // no object on board, all items collected
                            updateText("You have collected all items!");
                        }
                    }
                }
        );
    }

    /**
     * Adds object to inventory
     * @param objectElement -- the object to be added
     * @param x -- the x cooridnates of the object on game board
     * @param y -- the y corridnate of the object on game board
     * @Precondition -- objectElement is on gameboard
     * @Postcondition -- Object is now in inventory, not on game boad
     */
    private void updateInventory(VisualElement objectElement, int y, int x){
        // remove object on board, update visual element type to in inventory
        objectElement.setType("INVENTORY");

        AdventureObject object = this.objectsInRoom.get(0);
        for (AdventureObject obj : this.objectsInRoom) {
            if (obj.getName().equals(objectElement.getCaption().toUpperCase())) {
                object = obj;
                this.boardMatrix[y][x] = null;
                this.model.updateMatrix(boardMatrix);
                this.objectsInInventory.add(obj);
            }
        }
        this.objectsInRoom.remove(object);

        inventory.addItem(objectElement);
        updateText(objectElement.getCaption() + " has been taken.");

        if (this.objectsInRoom.isEmpty()) {
            victory();
        }

    }
    /**
     * Update the players location on the grid
     * @Precondition The gameboard, player are already initialized and x, y are the right cooridnates
     * @Postcondition The player's location on the game board is updated.
     */
    private void updatePlayer(){
        try{
            Thread.sleep(100);
        }catch(Exception e){
            System.out.println(e);
        }
        int x = model.player.getCurrentPosition().getX();
        int y = model.player.getCurrentPosition().getY();
        this.gameEnv.getGameBoard().getChildren().remove(this.playerVisual.getDisplay());
        gameEnv.setPlayerLocation(x, y);
    }


    /**
     * updateText
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     *
     * @param textToDisplay the text to display below the image.
     */
    public void updateText(String textToDisplay) {

        formatText(textToDisplay); //format the text to display
        outputText.setPrefWidth(500);
        outputText.setPrefHeight(500);
        outputText.setTextOverrun(OverrunStyle.CLIP);
        outputText.setWrapText(true);
        gamePane = new VBox(gameBoard, outputText);
        gamePane.setSpacing(10);
        gamePane.setAlignment(Pos.TOP_CENTER);
        gamePane.setStyle("-fx-background-color: #000000;");

        gridPane.add(gamePane, 0, 1);
//        stage.sizeToScene();
        if (nSB != null){
            nSB.speak(outputText.getText());
        }
    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String gameDesc = "Pick up all the objects to escape the room!";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) outputText.setText(gameDesc + "\n\nObjects in this room:\n" + objectString);
            else outputText.setText(gameDesc);
        } else {
            outputText.setText(textToDisplay);
        }
        outputText.setStyle("-fx-text-fill: white;");
        outputText.setFont(new Font("Arial", 16));
        outputText.setAlignment(Pos.CENTER);
    }

    /**
     * Updates the scene by initializing control schemes, updating the room and inventory objects,
     * and rendering the game environment.
     * This method sets up the control scheme for the player, updates the objects in the current room and in the inventory,
     * and initializes the game environment. It creates a visual representation of the game board and the player, and updates
     * the visual matrix. Additionally, it handles the addition of the inventory holder and the event for grid handling.
     *
     * Precondition: The 'model' must be initialized with a valid player and current room. The 'inventory' and 'SQUARE_SIZE'
     *               should also be properly defined.
     * Postcondition: The game environment is rendered with updated room objects, inventory, and player position. The game board
     *                and player visual are updated, and the visual matrix reflects the current state of the game environment.
     */
    public void updateScene() {
        controlScheme = new ArrowKeys(model.player); //default control scheme is arrow keys

        this.objectsInRoom = this.model.player.getCurrentRoom().objectsInRoom;
        this.objectsInInventory = this.model.player.inventory;

        addInventoryHolder(); // Display inventory holder

        this.boardMatrix = this.model.getBoardMatrix();
        CompositeElement rootElement = generateElementTree();

        this.gameEnv = new GameEnvironment(rootElement, inventory, boardMatrix, SQUARE_SIZE);
        this.gameEnv.renderEnvironment();
        this.gameBoard = gameEnv.getGameBoard(); // Create game board
        this.playerVisual = this.gameEnv.getPlayerVisual();
        this.visualMatrix = this.gameEnv.getVisualMatrix();

        //ensures the random location of player on grid is updated
        addGridHandlingEvent();
    }

    /**
     * Display the victory screen.
     * This method is called when the player has collected all objects and wins the game.
     * It removes the game board and displays a congratulatory message.
     */
    public void victory() {
        this.gamePane.getChildren().remove(this.gameBoard);
        this.outputText.setText("Congratulations! You have been added to the AdventureGame's Hall of Fame.");
        outputText.setStyle("-fx-text-fill: white;");
        outputText.setFont(new Font("Arial", 24));
        outputText.setAlignment(Pos.CENTER);
        //String victoryImgPath = this.model.getDirectoryName() + File.separator + "victory.png";
        //ImageView victoryImg = new ImageView(victoryImgPath);
        //this.gamePane.getChildren().add(outputText);
    }



    /**
     * Get object image files from game directory and return
     * @return a File[] array with object image files
     */
    private File[] getObjectImages(String colorScheme) {
        File objectsFolder = new File(this.model.getDirectoryName() + File.separator + "objectImages" + File.separator + colorScheme);
        return objectsFolder.listFiles();
    }

    /**
     * Check if an object is in the inventory and return the result.
     *
     * @param name The name of the object.
     * @return Object[] array with AdventureObject and boolean value true iff object in inventory
     */
    private Object[] inInventory(String name) {
        Object[] res = {null, false};
        for (AdventureObject obj : this.model.getPlayer().inventory) {
            if (obj.getName().equals(name)) {
                res[0] = obj;
                res[1] = true;
            }
        }
        return res;
    }

    /**
     * Check if an object in room and return
     * @param name object name
     * @return Object[] array with AdventureObject and boolean value true iff object in room
     */
    private Object[] inRoom(String name) {
        Object[] res = {null, false};
        for (AdventureObject obj : this.model.getPlayer().getCurrentRoom().objectsInRoom) {
            if (obj.getName().equals(name)) {
                res[0] = obj;
                res[1] = true;
            }
        }
        return res;
    }

    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        Label instructionsLabel = new Label(model.getInstructions());
        instructionsLabel.setStyle("-fx-text-fill: white;");
        instructionsLabel.setFont(new Font("Arial", 16));
        instructionsLabel.setAlignment(Pos.CENTER);
        instructionsLabel.setPrefWidth(700);
        instructionsLabel.setPrefHeight(500);
        instructionsLabel.setTextOverrun(OverrunStyle.CLIP);
        instructionsLabel.setWrapText(true);

        VBox instructionsPane = new VBox(instructionsLabel);
        instructionsPane.setPadding(new Insets(10));
        instructionsPane.setAlignment(Pos.TOP_CENTER);
        instructionsPane.setStyle("-fx-background-color: #000000;");


        if (helpToggle) {
            gridPane.getChildren().removeAll();
            updateText("");
            helpToggle = false;
        }
        else {
            gridPane.getChildren().removeAll();
            gridPane.add(instructionsPane, 0, 1);
            helpToggle = true;
            if (nSB != null){
                nSB.speak(model.getInstructions());
            }
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
        helpButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    stopArticulation(); //if speaking, stop
                    showInstructions();
                }
            }
        });
    }

    /**
     * This method handles the event related to the
     * Accessibility button.
     */
    public void addAccessibilityEvent() {
        accessibilityButton.setOnAction(e -> {
            gridPane.requestFocus();
            AccessibilityView accessibilityView = new AccessibilityView(this);
        });
        accessibilityButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    AccessibilityView accessibilityView = createAccessibilityView();
                }
            }
        });
    }



    /**
     * This method handles the event related to the
     * GameState button.
     */
    public void addGameStateEvent() {
        gameStateButton.setOnAction(e -> {
            gridPane.requestFocus();
            GameStateView gameStateView = new GameStateView(this);
        });
        gameStateButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    gridPane.requestFocus();
                    GameStateView gameStateView = createGameStateView();
                }
            }
        });
    }


    /**
     * This method handles the event when the control button is pressed
     *  The button has either the label 'Arrow Keys' or 'WASD Keys.'
     *  At the beginning, control Button is initialized, and switches the control scheme.
     */
    public void addSwitchControlSchemeEvent(){
        controlButton.setOnAction(e ->{
            if(controlButton.getText().equals("Arrow Keys")){
                controlButton.setText("WASD Keys");
                controlScheme = new AWSDKeys(model.player);
            } else{
                controlButton.setText("Arrow Keys");
                controlScheme = new ArrowKeys(model.player);
            }
        });
        controlButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if(controlButton.getText().equals("Arrow Keys")){
                        controlButton.setText("WASD Keys");
                        controlScheme = new AWSDKeys(model.player);
                    } else{
                        controlButton.setText("Arrow Keys");
                        controlScheme = new ArrowKeys(model.player);
                    }
                }
            }
        });
    }

    /**
     * Creates the UI for the inventory holder located at the bottom of the GUI
     */
    public void addInventoryHolder() {

        Label invLabel = new Label("INVENTORY");
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 14));

        this.inventory = new Inventory();

        VBox inventoryDisplay = new VBox(invLabel, inventory);
        gridPane.add(inventoryDisplay, 0, 2);

        GridPane.setMargin(inventoryDisplay, new Insets(0, 50, 0, 50));

    }

    /**
     * This method stops articulations 
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }

    // Getters and Setters
    /**
     * This method creates a new instance of AccessibilityView
     */
    private AccessibilityView createAccessibilityView() {
        return new AccessibilityView(this);
    }
    /**
     * This method creates a new instance of GameStateView.
     */
    private GameStateView createGameStateView(){
        return new GameStateView(this);
    }
}
