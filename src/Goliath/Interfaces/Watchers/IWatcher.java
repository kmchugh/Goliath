/*
 * Interface for the watcher class.
 */

package Goliath.Interfaces.Watchers;

/**
 * @author home_stanbridge
 */
public interface IWatcher
{
    /**
     * Condition watch is to test
     * @return true if the condition for action is met otherwise false
     */
    boolean watcherCondition();
    /**
     * Action to be taken if the watcher condition returns true
     */
    void watcherAction();

}
