package pl.put.poznan.checker.logic;

/**
 * Visitable interface implemented as part of the Visitor design pattern.
 * The interface includes a single method - accept(), which takes Visitor object
 * as an argument. Visitable interface is meant to allow a Visitor class object
 * to "visit" the instances of classes implementing this interface.
 */
public interface Visitable {
    /**
     * Method accepts a visitor. In concrete implementations the method
     * is meant to send the visitor to other objects that should be visited.
     * @param visitor - Visitor object that can be accepted by class instance
     */
    void accept(Visitor visitor);
}
