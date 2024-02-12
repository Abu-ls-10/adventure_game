package Visualization;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents an inventory system in a graphical user interface, extending the ScrollPane class.
 * This class is responsible for managing and displaying a collection of inventory items. It uses an HBox to align
 * these items in a horizontal row, allowing horizontal scrolling when the number of items exceeds the display area.
 * The inventory has customizable style properties, including background and border colors.
 *
 * The class includes an ArrayList to store the objects in the inventory and an integer to keep track of the inventory size.
 * The constructor initializes the inventory row, sets it as the content of the ScrollPane, and configures the scrollbar
 * policies and fit properties. It also sets the default style for the inventory display.
 *
 */
public class Inventory extends ScrollPane {

    private int inventorySize;
    private ArrayList<String> objects = new ArrayList<>();
    private HBox inventoryRow;
    /**
     * Constructs an Inventory instance with a horizontal row for displaying items.
     * Initializes the inventory row, sets scrollbar policies, fits the content to the ScrollPane size,
     * and applies default styling.
     */
    public Inventory() {
        inventoryRow = createInventoryRow();

        setContent(inventoryRow);
        setHbarPolicy(ScrollBarPolicy.ALWAYS); // Enable horizontal scrollbar if needed

        setFitToHeight(true);
        setFitToWidth(true);
        setStyle("-fx-background-color: black; -fx-border-color: green;");
    }

    /**
     * Creates and returns a horizontal box (HBox) representing a row in the inventory.
     *
     * @return An HBox object representing a row in the inventory, populated with 10 inventory slots.
     * Precondition: None. This method does not require any existing state or parameters to function correctly.
     * Postcondition: Returns an HBox with 10 child StackPanes, each containing an InventorySlot object. The HBox has a black background,
     *                centered alignment, and padding set. This HBox can be used as a row in the inventory UI.
     */
    public HBox createInventoryRow() {
        HBox inventoryRow = new HBox(10);
        inventoryRow.setAlignment(Pos.CENTER);
        inventoryRow.setPadding(new Insets(10));
        inventoryRow.setStyle("-fx-background-color: black;");

        for (int i = 0; i < 10; i++) {
            InventorySlot itemSlot = new InventorySlot();
            StackPane placeholder = new StackPane(itemSlot);
            inventoryRow.getChildren().add(placeholder);
        }

        return inventoryRow;
    }

    /**
     * A custom graphical component representing a slot in the inventory.
     * This class extends Rectangle and is used to create an inventory slot. Each slot can be selected or deselected,
     * and responds to mouse clicks, changing its appearance based on its selection state.
     */
    private class InventorySlot extends Rectangle {
        private boolean selected;
        /**
         * Constructor for InventorySlot.
         * Initializes the inventory slot with a predefined size and color. It also sets up a mouse click event handler
         * to handle selection and deselection of the slot, displaying appropriate messages in the console.
         */
        public InventorySlot() {
            super(50, 50); // Slightly bigger inventory slots
            setFill(Color.BLACK);

            setOnMouseClicked(event -> {
                if (event.isShiftDown()) {
                    System.out.println("Shift-clicked item slot");
                } else {
                    selectObject();
                    System.out.println("Clicked item slot");
                }
                // Add logic to handle the selection and display of the green border if needed

            });
        }
        /**
         * Toggles the selection state of this inventory slot.
         * If the slot is currently selected, it is deselected and its border color changes to gray. If it is not selected,
         * it becomes selected and its border color changes to light gray.
         */

        private void selectObject() {
            if (selected) {
                setStroke(Color.GRAY);
                selected = false;
            }
            else {
                setStroke(Color.LIGHTGRAY);
                selected = true;
            }
        }

        /**
         * Checks if the inventory slot is selected.
         *
         * @return true if the slot is selected, false otherwise.
         */
        private boolean isSelected() {
            return this.selected;
        }
    }

    /**
     * Retrieves the HBox representing the row of the inventory.
     *
     * @return The HBox containing the inventory row.
     */
    public HBox getInventoryRow() {
        return this.inventoryRow;
    }

    /**
     * Adds a visual element to the first available slot in the inventory row.
     * This method iterates through each slot in the inventory row. When it finds an empty slot (a StackPane with only one child),
     * it adds the visual representation of the item to this slot and then exits the method.
     *
     * @param item The VisualElement to be added to the inventory.
     */
    public void addItem(VisualElement item) {
        for (Node obj : this.inventoryRow.getChildren()) {
            if (obj instanceof StackPane placeholder) {
                if (placeholder.getChildren().size() == 1) {
                    placeholder.getChildren().add(item.getDisplay());
                    return;
                }
            }
        }
    }

    /**
     * Removes a visual element from the inventory row.
     * This method iterates through each slot in the inventory row. If it finds the given item in a slot (identified as a StackPane containing the item),
     * it removes the item from the slot and replaces it with a new InventorySlot, effectively emptying the slot.
     *
     * @param item The VisualElement to be removed from the inventory.
     */
    public void removeItem(VisualElement item) {
        for (Node obj : this.inventoryRow.getChildren()) {
            if (obj instanceof StackPane placeholder) {
                if (placeholder.getChildren().get(0).equals(item.getDisplay())) {
                    placeholder.getChildren().removeAll();
                    placeholder.getChildren().add(new InventorySlot());
                    return;
                }
            }
        }
    }
}