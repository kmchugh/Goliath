/*
 * This Singleton class is the watch manager, it watches any of the watchers added to its list of watchers
 *
 */

package Goliath.Watchers;


import Goliath.Applications.Application;
import Goliath.Applications.ApplicationState;
import Goliath.Arguments.Arguments;
import Goliath.Interfaces.Watchers.IWatcher;
import Goliath.Threading.ThreadJob;
import Goliath.Collections.List;
import Goliath.SingletonHandler;
import Goliath.Utilities;


/**
 *
 * @author home_stanbridge
 * A new thread is created where the watch manager runs, watching for the watches added to its list of watchers
 */
public final class WatcherManager extends Goliath.Object
{

    private List<IWatcher> m_oWatcherClasses;

    public WatcherManager()
    {
               run();
    }
/**
 * If the singleton watch manager does not exist creates an instance of the watch manager and runs it
 * @return the singleton instance of the watch manager
 */
    public static WatcherManager getInstance()
    {
       return SingletonHandler.getInstance(WatcherManager.class);
    }
/**
 *  Runs the watch manager which then sets up the watch in a new thread
 * @return - true if completed successfully
 */
    public boolean run()
    {

        Goliath.Threading.ThreadJob loThreadJob = new ThreadJob(null)
        {
            @Override
            /**
             * Loop through all watch objects and execute their watch method
             */

            protected void onRun(Arguments toCommandArgs)
            {
                try
                {
                    ApplicationState[] loValidStates = {ApplicationState.LOADING(), ApplicationState.RUNNING()};
                    while(Utilities.isOneOf(Application.getInstance().getState(), loValidStates))
                    {
                        if (m_oWatcherClasses != null && m_oWatcherClasses.size() > 0)
                        {
                            List<IWatcher> loWatchers = new List(m_oWatcherClasses);
                            for (IWatcher loWatchClass : loWatchers)
                            {
                                try
                                {
                                    if (loWatchClass.watcherCondition())
                                    {
                                        loWatchClass.watcherAction();
                                    }
                                }
                                catch (Throwable ex)
                                {
                                    /* This keeps the watchers running even if there is an error in the code
                                     for the watch condition or action */
                                    Application.getInstance().log(ex);
                                }
                            }
                        }
                        Goliath.Threading.Thread.sleep(500);
                    }
                }
                catch (Throwable toException)
                {
                    Application.getInstance().log(toException);
                }
            }
        };
        Goliath.Threading.Thread loThread = new Goliath.Threading.Thread(loThreadJob);
        loThread.setName("WatchManager");
        loThread.start();
        return true;
    }

    /**
     * Adds a new watcher to the watch manager
     * @param toWatcher - additional watcher being added to the watch manager
     */
    public void addWatcher(IWatcher toWatcher)
    {
        if (m_oWatcherClasses == null)
        {
            m_oWatcherClasses = new List<IWatcher>();
        }
        m_oWatcherClasses.add(toWatcher);
    }

    /**
     * Removes the specified watcher
     * @param toWatcher the watcher to remove
     */
    public void removeWatcher(IWatcher toWatcher)
    {
        if (m_oWatcherClasses != null)
        {
            m_oWatcherClasses.remove(toWatcher);
        }
    }
    

    /**
     * removes all the watchers
     */
    public void clearWatchers()
    {
        m_oWatcherClasses = null;
    }
}
