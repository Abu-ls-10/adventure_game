package Visualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a composite element in the composite design pattern for visualization.
 * This class acts as a node containing the game-board image for the Game Environment AST.
 * It extends the VisualComponent abstract class, providing a common interface for leaf and composite objects.
 */
public class CompositeElement extends VisualComponent {

    /** The list of children components. */
    private List<VisualComponent> children = new ArrayList<>();
    private String nodeName;

    /**
     * Constructs a CompositeElement object.
     * This class acts as a node containing the game-board image for the Game Environment AST.
     *
     * @param nodeName A string representing the name of this node for reference
     */
    public CompositeElement(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Renders the composite element by rendering each child element.
     */
    @Override
    public void render() {
        for (VisualComponent child : children) {
            child.render(); // Render each child element
        }
    }

    /**
     * Adds a child component to the current composite element.
     *
     * @param child The child component to be added.
     */
    @Override
    public void addChild(VisualComponent child) {
        children.add(child);
    }

    /**
     * Removes a child component from the current composite element.
     *
     * @param child The child component to be removed.
     */
    @Override
    public void removeChild(VisualComponent child) {
        children.remove(child);
    }

    /**
     * Gets the list of children components of the composite element.
     *
     * @return A new list containing the children components.
     */
    @Override
    public List<VisualComponent> getChildren() {
        return new ArrayList<>(children);
    }
}