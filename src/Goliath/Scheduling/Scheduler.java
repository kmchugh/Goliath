/* ========================================================
 * Scheduler.java
 *
 * Author:      Ken McHugh
 * Created:
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * P.S.20110806    GJF - 40         Complete task modifications
 * ===================================================== */
package Goliath.Scheduling;

/**
 * Schedules agents that implement the IAgent interface.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0
 * @author      Ken McHugh
 **/
import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.LogType;
import Goliath.Interfaces.Scheduling.IAgent;
import Goliath.Interfaces.Scheduling.IScheduler;
import Goliath.Date;

public class Scheduler<T extends IAgent> extends Goliath.Object
        implements IScheduler<T>
{

    // Global reference to the timer task
    private TimerTask m_oTimerTask;
    // The time that the next timer task for the scheduler is scheduled - start it with a max value so that it will always be after the lowest schedule time
    private static long m_oScheduledTime = Long.MAX_VALUE;
    // Singleton Reference
    private static Scheduler m_oInstance;
    // table of agents being run
    private HashTable<Class<T>, List<IAgent>> m_oAgents;
    // The scheduler has started
    private boolean m_lIsStarted;
    // The scheduler is paused, but agents can still be added to it.
    private boolean m_lIsPaused;

    // Inner class extending javas TimerTsak
    private class TimerTask extends java.util.TimerTask
    {

        @Override
        public void run()
        {
            if (!m_lIsPaused && m_lIsStarted)
            {
                // process agents and run those that are to be run
                processAgents();
            }

            reSchedule();
            // the following is for testing uncomment for a basic test
            // schedule(500);
        }
    }
    /*
     * Singleton so do not allow to be instantiated externally
     */

    /**
     * Constructor
     */
    private Scheduler()
    {
        m_lIsStarted = false;
        m_lIsPaused = false;
        // this is for testing - uncomment when doing a basic run test
        // schedule(500);
    }

    /**
     * Singleton Scheduler
     * @return - the global scheduler
     */
    public static Scheduler getInstance()
    {
        if (m_oInstance == null)
        {
            m_oInstance = new Scheduler();
        }
        return m_oInstance;
    }

    /**
     * Timer task the scheduler
     * @param tnDelay - the delay before the scheduler is to run
     */
    private void schedule(long tnDelay)
    {
        m_oTimerTask = new TimerTask();
        setNextScheduledTime(tnDelay);
        Application.getInstance().scheduleTask(m_oTimerTask, tnDelay);
    }

    /**
     * Return a hash table of the agents registered to the scheduler
     * @return hash table of agents registered to the system
     */
    private HashTable<Class<T>, List<IAgent>> getAgents()
    {
        // create an empty hash table if it is null
        if (m_oAgents == null)
        {
            m_oAgents = new HashTable<Class<T>, List<IAgent>>();
        }
        return m_oAgents;
    }

    /**
     * Register the agents to the scheduler by loading those classes extending IAgent
     */
    @Override
    public void start()
    {

        Application.getInstance().log("Starting Scheduler " + getClass().getSimpleName(), LogType.TRACE());


        // Load all of the Agents and register them so we know when they are starting
        // List<Class<IAgent>> loList = Application.getInstance().getObjectCache().getClasses(IAgent.class);

        List<Class<T>> loList = new List(Application.getInstance().getObjectCache().getClasses(IAgent.class));
        for (Class<T> loClass : loList)
        {
            register(loClass);
        }

        // Clear the object cache
        Application.getInstance().getObjectCache().clearClass(IAgent.class);

        m_lIsStarted = true;
        // re schedule scheduler timer beacuse the new agent might require a different delay time.
        reSchedule();

        Application.getInstance().log("Started Scheduler " + getClass().getSimpleName(), LogType.TRACE());
    }

    /**
     * Indicates whether the agents have been loaded and registered to the scheduler
     * @return true if the scheduler has started and thus agents loaded and registered
     */
    @Override
    public boolean isStarted()
    {
        return m_lIsStarted;
    }

    /**
     * Stop the scheduler by unregistering each of the agents from the scheduler
     */
    @Override
    public void stop()
    {
        Application.getInstance().log("Stopping Scheduler " + getClass().getSimpleName(), LogType.TRACE());

        // TODO: Implement this once cancelScheduledTask is implemented on the Application
        //m_oTimer.cancel();
        m_lIsStarted = false;

        // unregister all the classes that have been loaded
        for (Class<T> loAgentClass : m_oAgents.keySet())
        {
            unregister(loAgentClass);
        }

        Application.getInstance().log("Stopped Scheduler " + getClass().getSimpleName(), LogType.TRACE());

    }

    /**
     * Pause the scheduler (but keeps agents registered and enables new registrations)
     */
    @Override
    public void pause()
    {
        m_lIsPaused = true;
    }

    /**
     * Resume a paused scheduler
     */
    @Override
    public void resume()
    {
        m_lIsPaused = false;
    }

    /**
     * Returns whether the scheduler is paused
     * @return true if the scheduler is paused otherwise false
     */
    public boolean isPaused()
    {
        return m_lIsPaused;
    }

    /**
     * Registers an agent class to the scheduler
     * @param toAgentClass - the class being registered to the scheduler
     */
    @Override
    public void register(Class<T> toAgentClass)
    {
        // If the agent is not already loaded, instantiate the agent class and add to the hash table
        if (!getAgents().containsKey(toAgentClass))
        {
            synchronized (getAgents())
            {
                IAgent loAgent = null;
                try
                {
                    loAgent = toAgentClass.newInstance();
                    loAgent.load();
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log("Could not create Agent  of type - " + toAgentClass.getName() + " - " + ex.getLocalizedMessage());
                }

                if (loAgent != null)
                {
                    List<IAgent> loAgentList = new List<IAgent>();
                    loAgentList.add(loAgent);
                    getAgents().put(toAgentClass, loAgentList);
                    reSchedule();

                }
            }
        }
    }

    /**
     * Registers an agent instance to the scheduler
     * @param toAgent - the agent instance being registered to the scheduler
     */
    @Override
    public void register(IAgent toAgent)
    {
        // if the class isn't already in the table, or if it is, but the agent isn't in the agent list for this class, then register this agent
        if (!getAgents().containsKey(toAgent.getClass()) || (getAgents().containsKey(toAgent.getClass()) && !getAgents().get(toAgent.getClass()).contains(toAgent)))
        {
            synchronized (getAgents())
            {
                // Add the class to the table if it doesn't exist
                if (!getAgents().containsKey(toAgent.getClass()))
                {
                    getAgents().put((Class<T>) toAgent.getClass(), new List<IAgent>());
                }
                // Add the agent to the table
                getAgents().get((Class<T>) toAgent.getClass()).add(toAgent);
                // re schedule scheduler timer beacuse the new agent might require a different delay time (do within sync so that other processes can't add new ones while scheduling.
                reSchedule();
            }
        }
    }

    /**
     * Unregister an agent object from the scheduler register
     * @param toAgent - the agent object to be unregistered
     */
    @Override
    public void unregister(IAgent toAgent)
    {
        unregister((Class<T>) toAgent.getClass());
        reSchedule();
    }

    /**
     * Unregister an agent class from the scheduler register
     * @param toAgentClass - the agent class to be unregistered
     */
    @Override
    public void unregister(Class<T> toAgentClass)
    {

        if (getAgents().containsKey(toAgentClass))
        {
            synchronized (getAgents())
            {
                List<IAgent> loAgents = getAgents().remove(toAgentClass);
            }
            reSchedule();
        }
    }

    /**
     * Set thread for agent to run (does not actually run the agent yet)
     * @param toAgent - the agent object to be updated
     */
    @Override
    public void updateAgent(IAgent toAgent)
    {
        Thread loThread = new Thread(toAgent, "Agent - " + toAgent.getClass().getName());
        loThread.start();
    }

    /**
     * Process each registered agent
     */
    private void processAgents()
    {
        for (Class<T> loAgentClass : getAgents().keySet())
        {
            runAgent(loAgentClass);
            Application.getInstance().log("Processed agent " + loAgentClass.getName() + "  " + getClass().getSimpleName(), LogType.TRACE());
        }

        Application.getInstance().log("Processed agents " + getClass().getSimpleName(), LogType.TRACE());
    }

    /**
     * Run the passed agent of the passed agent class if it is ready to run
     * @param toAgentClass - the class to run if it is ready to run
     */
    private void runAgent(Class<T> toAgentClass)
    {
        List<IAgent> loAgents = m_oAgents.get(toAgentClass);
        for (IAgent loAgent : loAgents)
        {
            // if the agent has reached it's schedule run time then run it by creating new thread for it to run in (updateAgent)
            if (loAgent.toRun())
            {
                updateAgent(loAgent);
                Application.getInstance().log("run agent " + loAgent.getClass().getName() + " from scheduler  " + getClass().getSimpleName(), LogType.TRACE());
            }
        }
    }

    /**
     *  Set the latest schedule time - i.e. when the next agent will run - this is now plus the delay time
     * @param toDelay - the delay for the next agent to run
     */
    private void setNextScheduledTime(long toDelay)
    {
        m_oScheduledTime = currentTime() + toDelay;
    }

    /**
     * Return the time the next agent will run
     * @return - the time the next agent will run
     */
    private long getNextScheduledTime()
    {
        return m_oScheduledTime;
    }

    /**
     * Determine when the next scheduled time is based on the next agent that should be run
     * @return - time the earliest agent needs to run
     */
    private long getNextScheduleTime()
    {
        // todo - ouch - this is not efficient - only one agent registered or unregistered at a time so simple at a time comparison should do for re scheduling
        if (getAgents().size() == 0)
        {
            return Long.MAX_VALUE;
        }

        // set the minumum time for next schedule to be the next scheduled time
        Date loMinDate = new Date();
        boolean llFirstDate = true;
        // Now check each agent and see if there is an agent to be scheduled before this time
        for (List<IAgent> loAgents : getAgents().values())
        {
            for (IAgent loAgent : loAgents)
            {
                if (llFirstDate)
                {
                    loMinDate = loAgent.getNextRun();
                    llFirstDate = false;
                }

                if (loAgent.getNextRun().getLong() < loMinDate.getLong())
                {
                    loMinDate = loAgent.getNextRun();
                }
            }
        }
        return loMinDate.getLong();
    }

    /**
     * Re schedule the timer task to delay to catch the next agent requiring processing
     */
    private void reSchedule()
    {
        // cancel the active timer - it is scheduled to be executed after the next agent requires
        if (m_oTimerTask != null)
        {
            m_oTimerTask.cancel();
        }
        // Only resset if there are agents to schedule
        if ((getAgents().size() != 0))
        {
            // schedule
            schedule(getNextScheduleTime() - currentTime() > 0 ? getNextScheduleTime() - currentTime() : 0);
        }
    }

    /**
     * get current time
     * @return - current time
     */
    private long currentTime()
    {
        return new Date().getLong();
    }
}
