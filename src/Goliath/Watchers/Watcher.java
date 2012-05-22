/*
 * The watcher class implements IWatcher so has a watch condition and watch action method.
 *
 */
package Goliath.Watchers;

import Goliath.Interfaces.Watchers.IWatcher;

/**
 *
 * @author home_stanbridge
 */
public abstract class Watcher extends Goliath.Object implements IWatcher
{

    public Watcher()
    {
        WatcherManager.getInstance().addWatcher(this);
    }

    /**
     * the condition to watch - this is currently empty as most implementations will be done via an anonymous class
     * @return false as a default
     */
    public abstract boolean watcherCondition();

    /**
     * The action to be taken if the watcher condition returns true. Most implementationsn will be done via an anonymous class
     */
    public abstract void watcherAction();
}
