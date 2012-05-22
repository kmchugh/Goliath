/* ========================================================
 * Agent.java
 *
 * Author:      Ken McHugh
 * Created:
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 * The abstract class for Agents, which are pieces of code that act on the system and produce results, but timed to run
 * at set intervals.
 *
 * Agents may have logic that runs as a whole for the agent or against items defined to the agent (or both). Thus the
 * onRun method has a parameter free version that runs a single block of code for the whole agent and an onRun(Item) that
 * runs the code for the passed item. 
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * P.S.20110806    GJF - 40         Complete task modifications
 * ===================================================== */
package Goliath.Scheduling;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Date;
import Goliath.Interfaces.Scheduling.IAgent;

/**
 * Abstract class for Agents, which are pieces of code that act on the system and produce results, but timed to run.
 * Agents are run via the agent scheduler Scheduler.java
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0
 * @author      Ken McHugh
 **/
public abstract class Agent<T> extends Goliath.Object implements IAgent<T>
{

    private boolean m_lIsActive;
    private boolean m_lCancelled;
    private boolean m_lContinueAfterError;
    private boolean m_lExecuting;
    private Date m_dEndDate;
    private Date m_dStartDate;
    private Date m_dLastRun;
    private Date m_dNextRun;
    private long m_nInterval;
    private HashTable<T, List<Throwable>> m_oItemErrors;
    private List<Throwable> m_oErrors;

    /**
     * Agent constructor empty as agents can start when reflection is used
     */
    public Agent()
    {
        // No code in the constructor so the agents don't start up when reflection is used
    }

    /**
     * Load the agent - "constructor" code placed here and load executed by the Scheduler registration
     */
    @Override
    public void load()
    {
        m_lIsActive = Application.getInstance().getPropertyHandlerProperty("Application.Agents." + getClass().getSimpleName() + ".Active", true);
        m_lCancelled = false;
        setExecuting(false);


        setContinueAfterError(Application.getInstance().getPropertyHandlerProperty("Application.Agents." + getClass().getSimpleName() + ".ContinueAfterError", true));

        if (getInterval() == 0)
        {
            setInterval(m_nInterval = Application.getInstance().getPropertyHandlerProperty("Application.Agents." + getClass().getSimpleName() + ".Interval", 60000L));

        }
    }

    /**
     * return hash table of run errors for each item
     * @return - Hash table of any errors encountered during the agent run.
     */
    private HashTable<T, List<Throwable>> getItemErrorList()
    {
        if (m_oItemErrors == null)
        {
            m_oItemErrors = new HashTable<T, List<Throwable>>();
        }
        return m_oItemErrors;
    }

    /**
     * return list of errors with no association with individual items
     * @return
     */
    private List<Throwable> getErrorList()
    {
        if (m_oErrors == null)
        {
            m_oErrors = new List<Throwable>();
        }
        return m_oErrors;
    }

    /**
     * set the executing parameter
     * @param tlExecuting - true when the agent is executing otherwise false
     */
    public void setExecuting(boolean tlExecuting)
    {
        m_lExecuting = tlExecuting;
    }

    /**
     * Activate the agent - this simply allows this agent to execute its run method
     */
    @Override
    public void activate()
    {
        m_lIsActive = true;
    }

    /**
     * Deactivate the agent - this simply stops the agent from running until it is activated
     */
    @Override
    public void deactivate()
    {
        m_lIsActive = false;
    }

    /**
     * cancel the run - it will unregister this agent
     */
    @Override
    public void cancel()
    {
        m_lCancelled = true;
        Application.getInstance().getScheduler().unregister(this);
    }

    /**
     * retrieve whether the agent can continue after an error processing an agent item
     * @return true if the agent is to continue processing items when encountering an error with an agent item
     */
    @Override
    public boolean getContinueAfterError()
    {
        return m_lContinueAfterError;
    }

    /**
     * Set the continue after error.
     * @param tlContinue - set to true if the agent is to continue when encountering an error processing an agent item, otherwise false (the agent will stop)
     */
    @Override
    public void setContinueAfterError(boolean tlContinue)
    {
        m_lContinueAfterError = tlContinue;
    }

    /**
     * Returns the end date for the agent - the agent wont run after the end date
     * @return - the date the agent cannot run passed.
     */
    @Override
    public Date getEndDate()
    {
        // If end date hasn't been set but needed, set it to the maximum value!
        if (m_dEndDate == null)
        {
            m_dEndDate = new Date(Long.MAX_VALUE);
        }
        return m_dEndDate;
    }

    /**
     * Returns the start date for the agent - the agent won't run until the current date is on or passed the start date
     * @return - The date from which the agent is able to first run
     */
    @Override
    public Date getStartDate()
    {
        // todo - what happens if it is null - can't force the setting of these dates so default?
        if (m_dStartDate == null)
        {
            m_dStartDate = new Date();
        }

        return m_dStartDate;
    }

    /**
     * set the start date for the agent. The agent won't run until the current date is on or passed the start date
     * @param tdStartDate - the date from which the agent is able to first run
     */
    @Override
    public void setStartDate(Date tdStartDate)
    {
        m_dStartDate = tdStartDate;
    }

    /**
     * Set the end date for the agent - the agent won't run after this date
     * @param tdEndDate - the date for which the agent will end
     */
    @Override
    public void setEndDate(Date tdEndDate)
    {
        m_dEndDate = tdEndDate;
    }

    /**
     * Return the error hash table containing errors for each item
     * @return - the error hash table
     */
    @Override
    public HashTable<T, List<Throwable>> getItemErrors()
    {
        return getItemErrorList();
    }

    /**
     * return a list of errors for a given agent item
     * @param toItem - the agent item to retrieve errors
     * @return - list of errors for a given agent item
     */
    @Override
    public List<Throwable> getItemErrors(T toItem)
    {
        List<Throwable> loReturn = getItemErrorList().get(toItem);
        if (loReturn == null)
        {
            loReturn = new List<Throwable>(0);
        }
        return loReturn;
    }

    /**
     * Return list of errors not associated with items
     * @return - list of errors not associated with items
     */
    @Override
    public List<Throwable> getErrors()
    {
        return getErrorList();
    }

    /**
     * Returns the interval between runs of the agent
     * @return - the interval between runs of the agent
     */
    @Override
    public long getInterval()
    {
        return m_nInterval;
    }

    /**
     * Set the interval between runs of the agent
     * @param tnInterval - the interval between runs of the agent
     */
    @Override
    public void setInterval(long tnInterval)
    {
        m_nInterval = tnInterval;
    }

    /**
     * Returns the number of agent items
     * @return - the number of agent items
     */
    @Override
    public int getItemCount()
    {
        return getItems().size();
    }

    /**
     * Returns the list of items for this agent
     * @return - list of items for this agent
     */
    @Override
    public List<T> getItems()
    {
        List<T> loReturn = onGetItems();
        if (loReturn == null)
        {
            loReturn = new List<T>(0);
        }
        return loReturn;
    }

    /**
     * Custom override code to build the list of items for this agent
     * @return - list of items for this agent
     */
    protected abstract List<T> onGetItems();

    /**
     * The date the agent was list run
     * @return - the date the agent was last run
     */
    @Override
    public Date getLastRun()
    {
        return m_dLastRun;
    }

    /**
     * The date the agent is to next run
     * @return - the date the agent is to next run
     */
    @Override
    public Date getNextRun()
    {
        // if no next run date then set it to now plus the interval
        if (m_dNextRun == null)
        {
            Date ldStartDate;
            if (getStartDate() == null)
            {
                ldStartDate = new Date();
            }
            else
            {
                ldStartDate = getStartDate();
            }
            long lnNextRunLong = new Date(ldStartDate.getLong()).getLong() + getInterval();
            setNextRun(new Date(lnNextRunLong));
        }
        return m_dNextRun;
    }

    /**
     * Returns whether the agent has items
     * @return - true if the agent has items
     */
    @Override
    public boolean hasItems()
    {
        return getItemCount() > 0;
    }

    /**
     * Returns whether the agent is active (similar to pause/resume)
     * @return - true if the agent is active otherwise false
     */
    @Override
    public boolean isActive()
    {
        return m_lIsActive;
    }

    /**
     * Returns whether the agent is canceled
     * @return - true if the agent is canceled otherwise false.
     */
    @Override
    public boolean isCancelled()
    {
        return m_lCancelled;
    }

    /**
     * Returns whether or not the agent is executing
     * @return - true if the agent is currently executing (running) otherwise false
     */
    @Override
    public boolean isExecuting()
    {
        return m_lExecuting;
    }

    /**
     * Returns whether or not it is time to run the agent.
     * @return - true if the agent can be run otherwise false
     */
    @Override
    public boolean toRun()
    {
        // Agent can run if it is not already executing and if it fits within the date range and is active and not cancelled
        return !isExecuting() && toRunDate() && isActive() && !isCancelled();
    }

    /**
     * Returns whether or not the agent currently fits within the date range for running.
     * @return - true if the agent currently fits within the date range for running
     */
    private boolean toRunDate()
    {
        Date ldCurrentDate = new Date();
        // Can run if the next run is less than current date and if the current date is within the start and end dates.
        //todo - the start and end dates could be null - thus question on task to determine how to ensure that they are set
        return (getNextRun().getLong() <= ldCurrentDate.getLong()) && (ldCurrentDate.getLong() >= getStartDate().getLong()) && (ldCurrentDate.getLong() <= getEndDate().getLong());
    }

    /**
     * Run the agent
     */
    @Override
    public final void run()
    {
        if (toRun())
        {
            setExecuting(true);

            Date ldCurrent = new Date();

            setLastRun(ldCurrent);
            // Run code not related to items
            try
            {
                onRun();
            }
            catch (Throwable ex)
            {
                addError(ex);
                if (!getContinueAfterError())
                {
                    setExecuting(false);
                    nextRunCalculate();
                    return;
                }
            }
            // Retrieve each item if there are any and run code related to each item
            for (T loItem : getItems())
            {
                try
                {
                    // Process item if the agent is still active and not cancelled
                    if (!isActive() || isCancelled())
                    {
                        setExecuting(false);
                        break;
                    }

                    onRun(loItem);

                }
                catch (Throwable ex)
                {
                    addItemError(loItem, ex);
                    // if not continuing after error then break out of item processing
                    if (!getContinueAfterError())
                    {
                        setExecuting(false);
                        break;
                    }
                }
            }
            // calculate the next run date
            setExecuting(false);
            nextRunCalculate();

        }
    }

    /**
     * Calculate the next run date
     */
    private void nextRunCalculate()
    {
        // next date to be now plus interval
        setNextRun(new Date(getLastRun().getLong() + getInterval()));

        // but this could still be prior to now so loop until next run date is on or after now
        Date ldNextRun = new Date();
        while (getNextRun().getLong() <= ldNextRun.getLong())
        {
            setNextRun(new Date(getNextRun().getLong() + getInterval()));
        }
    }

    /**
     * Add item error to table
     * @param toItem - item being processed and in error
     * @param toException - the exception to be added to table
     */
    protected void addItemError(T toItem, Throwable toException)
    {
        HashTable<T, List<Throwable>> loList = getItemErrorList();
        if (!loList.containsKey(toItem))
        {
            loList.put(toItem, new List<Throwable>(1));
        }
        loList.get(toItem).add(toException);
    }

    /**
     * Add non-item error to list
     * @param toException  - the exception to be added to the list
     */
    protected void addError(Throwable toException)
    {
        List<Throwable> loList = getErrorList();
        loList.add(toException);
    }

    /**
     * set the last run date
     * @param toDate - Date last run
     */
    public void setLastRun(Date toDate)
    {
        m_dLastRun = toDate;
    }

    /**
     * Set the next run date
     * @param toDate - the next run date
     */
    public void setNextRun(Date toDate)
    {
        m_dNextRun = toDate;
    }

    /**
     * the non-item onrun to be overridden. This method should be empty if all the agent processing is on items
     */
    protected abstract void onRun();

    /**
     * item onrun to be overridden. This method should be empty if the only agent processing is non-item
     * @param toItem - item to run agent processing on
     */
    protected abstract void onRun(T toItem);
}
