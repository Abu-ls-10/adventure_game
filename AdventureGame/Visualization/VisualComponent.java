package Visualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract component in the composite design pattern for visualization.
 * This class provides a common interface for leaf and composite objects.
 */
public abstract class VisualComponent {

    /** The list of children components. */
    private List<VisualComponent> children = new ArrayList<>();

    /**
     * Renders the visual component.
     * @throws UnsupportedOperationException If not overridden by concrete subclasses.
     */
    public void render() {
        throw new UnsupportedOperationException("Abstract method");
    }

    /**
     * Adds a child component to the current composite.
     * @param child The child component to be added.
     * @throws UnsupportedOperationException If not overridden by concrete subclasses.
     */
    public void addChild(VisualComponent child) {
        throw new UnsupportedOperationException("Abstract method");
    }

    /**
     * Removes a child component from the current composite.
     * @param child The child component to be removed.
     * @throws UnsupportedOperationException If not overridden by concrete subclasses.
     */
    public void removeChild(VisualComponent child) {
        throw new UnsupportedOperationException("Abstract method");
    }

    /**
     * Gets the list of children components.
     * @return A new list containing the children components.
     */
    public List<VisualComponent> getChildren() {
        return new ArrayList<>(this.children);
    }
}