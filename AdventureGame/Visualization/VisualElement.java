package Visualization;

import AdventureModel.AdventureObject;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;

/**
 * Represents a visual element in the composite design pattern for visualization.
 * This class extends the VisualComponent abstract class and encapsulates the visual representation of objects
 * on the game board, including images, labels, and other display properties as leaf nodes.
 */
public class VisualElement extends VisualComponent {

    /** The type of the visual element. */
    private String type;

    /** The display container for the visual element. */
    private VBox display;

    /** The caption or name of the visual element. */
    private String caption;

    /** The description of the visual element. */
    private String description;

    /** The file path to the image associated with the visual element. */
    private String imagePath;

    /**
     * Constructs a VisualElement object based on an AdventureObject.
     *
     * @param obj The AdventureObject associated with the visual element.
     * @param imagePath The file path to the image associated with the visual element.
     * @param type The type of the visual element.
     */
    public VisualElement(AdventureObject obj, String imagePath, String type) {
        this.imagePath = imagePath;
        this.caption = obj.getName();
        this.description =  obj.getDescription();
        this.type = type.toUpperCase();
    }

    /**
     * Constructs a VisualElement object representing the player.
     *
     * @param imagePath The file path to the image associated with the player.
     */
    public VisualElement(String imagePath) {
        this.type = "PLAYER";
        this.imagePath = imagePath;
        this.caption = "PLAYER";
    }

    /**
     * Generates an ImageView for the visual element based on the provided image path.
     *
     * @param imagePath The file path to the image associated with the visual element.
     * @return The generated ImageView.
     */
    private ImageView generateImage(String imagePath) {
        if (imagePath.startsWith("AdventureGame")) {
            imagePath = imagePath.substring(13 + File.separator.length());
        }
        ImageView objImageView = new ImageView(new Image(imagePath));

        if (isPlayer()) {
            objImageView.setFitWidth(45);
            objImageView.setFitHeight(45);
        }
        else {
            objImageView.setFitWidth(40);
            objImageView.setFitHeight(40);
        }
        return objImageView;
    }

    /**
     * Generates a Label for the visual element.
     *
     * @return The generated Label.
     */
    private Label generateLabel(){
        Label objLabel = new Label(this.caption);
        objLabel.setStyle("-fx-text-fill: black;");
        objLabel.setFont(new Font("Arial", 12));
        objLabel.setPrefWidth(50);
        objLabel.setAlignment(Pos.CENTER);
        return objLabel;
    }

    /**
     * Renders the visual element by creating a VBox with the generated image.
     */
    @Override
    public void render() {
        this.display = new VBox(generateImage(this.imagePath));
        this.display.setAlignment(Pos.CENTER);
    }

    /**
     * Gets the display container for the visual element.
     *
     * @return The display container (VBox) for the visual element.
     */
    public VBox getDisplay() {
        return this.display;
    }

    /**
     * Gets the file path to the image associated with the visual element.
     *
     * @return The file path to the image.
     */
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * Sets the file path to the image associated with the visual element.
     *
     * @param newPath The new file path to the image.
     */
    public void setImagePath(String newPath) {
        this.imagePath = newPath;
    }

    /**
     * set the type for this object
     * @param type -- the type of object is either "BOARD", "INVENTORY", OR "PLAYER"
     */
    public void setType(String type){this.type = type;}

    /**
     * Gets the type of the visual element.
     *
     * @return The type of the visual element.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets the caption or name of the visual element.
     *
     * @return The caption or name of the visual element.
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Gets the description of the visual element.
     *
     * @return The description of the visual element.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Checks if the visual element belongs in the inventory.
     *
     * @return True if the visual element is in the inventory, false otherwise.
     */
    public boolean inInventory() {
        return this.type.equals("INVENTORY");
    }

    /**
     * Checks if the visual element belongs on the game board.
     *
     * @return True if the visual element is on the game board, false otherwise.
     */
    public boolean onBoard() {
        return this.type.equals("BOARD");
    }

    /**
     * Checks if the visual element represents the player.
     *
     * @return True if the visual element represents the player, false otherwise.
     */
    public boolean isPlayer() {
        return this.type.equals("PLAYER");
    }
}