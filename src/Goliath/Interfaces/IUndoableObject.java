/* =========================================================
 * IUndoableObject.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 7:06:37 PM
 * 
 * Description
 * --------------------------------------------------------
 * Objects which control their own state, and can undo/redo
 * changes
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.Interfaces;

/**
 * Objects which control their own state, and can undo/redo
 * changes
 *
 * @version     1.0 Jan 9, 2008
 * @author      Ken McHugh
 **/
public interface IUndoableObject
{

    /**
     * Copies the current state of the object and inserts it to the internal
     * list mechanism
     */
    void copyState();

    /**
     * Reverts the internal state to the original state
     */
    void undoChanges();

    /**
     * mark a new original state point and remove all history
     */
    void acceptChanges();

    /**
     * undo a single state level
     */
    void undo();

    /**
     * redo a single state level
     */
    void redo();

    /**
     * Checks if it is possible to undo an action 
     * @return  true if it is possible to undo a level
     */
    boolean canUndo();

    /**
     * Checks if it is possible to redo an action 
     * @return  true if it is possible to redo a level
     */
    boolean canRedo();
}
