/* =========================================================
 * CommandManager.java
 *
 * Author:      kmchugh
 * Created:     13-Oct-2008, 16:31:36
 * 
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Commands;

import Goliath.Applications.Application;
import Goliath.Arguments.SingleParameterArguments;
import Goliath.Collections.CommandQueue;
import Goliath.Collections.HashTable;
import Goliath.Exceptions.ProcessNotComplete;
import Goliath.Interfaces.Commands.ICommand;
import Goliath.Interfaces.Commands.ICommandManager;
import Goliath.Interfaces.ISession;
import Goliath.Session;
import Goliath.Threading.ThreadJob;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 13-Oct-2008
 * @author      kmchugh
**/
public class CommandManager extends Goliath.Object
        implements ICommandManager
{
    private int m_nThreadDelay;
    private boolean m_lActive;
    private int m_nCommandProcessCount;
    private ICommand<?, ?> m_oCurrent;
    private Goliath.Collections.CommandQueue m_oCommandQueue;
    private Goliath.Collections.HashTable<ICommand<?, ?>, java.lang.Object> m_oCommandResults;
    private ISession m_oSession;
    private boolean m_lPaused;
    private int m_nMaxThreads;


    /**
     * Creates a new instance of command manater
     * @param tnThreadDelay the number of milliseconds to delay before starting processing another group of commands
     * @param tnCommandProcessCount the number of commands to process at once
     * @param tnMaxThreads the maximum number of threads this manager will create
     */
    public CommandManager(int tnThreadDelay, int tnCommandProcessCount, int tnMaxThreads, ISession toSession)
    {
        m_nCommandProcessCount = tnCommandProcessCount;
        m_nThreadDelay = tnThreadDelay;
        m_nMaxThreads = tnMaxThreads;
        m_oSession = toSession;
    }
    
    /** Creates a new instance of CommandManager
     */
    public CommandManager()
    {
        this(Goliath.Applications.Application.getInstance().getPropertyHandlerProperty("Application.Settings.CommandManager.ThreadDelay", 100),
                Goliath.Applications.Application.getInstance().getPropertyHandlerProperty("Application.Settings.CommandManager.CommandProcessCount", 10),
                // A session should not really need more than 3 threads, one for UI, one for background process, and one redundant
                Goliath.Applications.Application.getInstance().getPropertyHandlerProperty("Application.Settings.CommandManager.MaxThread", Math.min(Goliath.Environment.getAvailableProcessors(), 3)),
                null);
    }


    
    /** Creates a new instance of CommandManager
     * @param toSession The session to attach this command manager to
     */
    public CommandManager(ISession toSession)
    {
        this();
        m_oSession = toSession;        
    }
    
    @Override
    public void pause()
    {
        m_lPaused = true;
    }
    
    @Override
    public void resume()
    {
        m_lPaused = false;
    }

    @Override
    public boolean contains(ICommand toCommand)
    {
        return ((m_oCommandQueue != null) ? m_oCommandQueue.contains(toCommand) : false) ||
                (m_oCurrent != null && m_oCurrent.equals(toCommand)) ||
                (m_oCommandResults != null && m_oCommandResults.containsKey(toCommand));
    }
    
    /**
     * Gets the pause between completing current commands and checking for new commands
     * @return the amount of time in milliseconds
     */
    @Override
    public int getThreadDelay()
    {
        return m_nThreadDelay;
    }
    
    
    /**
     * Sets the pause between completing current commands and checking for new commands
     * @param tnThreadDelay the new time in milliseconds
     */
    @Override
    public void setThreadDelay(int tnThreadDelay)
    {
        m_nThreadDelay = tnThreadDelay;
    }
    
    /**
     * Gets the number of commands that will be processed before relinquishing control
     * If there are less commands to process then control will be returned after processing them
     * @return the number of commands
     */
    @Override
    public int getCommandProcessCount()
    {
        return m_nCommandProcessCount;
    }
    
    
    /**
     * Gets the number of commands that will be processed before relinquishing control
     * If there are less commands to process then control will be returned after processing them
     * @param tnCommandProcessCount the number of commands 
     */
    @Override
    public void setCommandProcessCount(int tnCommandProcessCount)
    {
        m_nCommandProcessCount = tnCommandProcessCount;
    }
    

    /**
     * Activates the command manager, starts processing commands
     */
    @Override
    public synchronized void activate()
    {
        if (m_lActive)
        {
            // Already active
            return;
        }

        m_lActive = true;

        // Starts the session running in it's own thread to handle it's own commands
        Goliath.Threading.ThreadJob loThreadJob = new ThreadJob<SingleParameterArguments<ISession>>(new SingleParameterArguments(m_oSession))
        {
            @Override
            protected void onRun(SingleParameterArguments<ISession> toCommandArgs)
            {
                try
                {
                    // TODO: Optimise this method
                    ISession loSession = toCommandArgs.getParameter();
                    if (loSession == null)
                    {
                        loSession = Session.getCurrentSession();
                        m_oSession = loSession;
                    }
                    while (m_lActive && loSession != null && !loSession.isExpired())
                    {
                        if (!m_lPaused)
                        {
                            // Process the commands in the manager
                            process();
                        }
                        Goliath.Threading.Thread.sleep(getThreadDelay());
                    }
                }
                catch (Exception toException)
                {
                    // TODO: See what needs to be done here
                    Application.getInstance().log(toException);
                }
            }
        };

        for (int i =0; i< m_nMaxThreads; i++)
        {
            String lcSessionID = m_oSession == null ? "Unknown Session" : m_oSession.getSessionID();
            Goliath.Threading.Thread loThread = new Goliath.Threading.Thread(loThreadJob, lcSessionID);
            loThread.start();
        }
    }

    /**
     * Checks if this command manager has any active commands
     * @return true if there are commands in the queue
     */
    public synchronized boolean hasCommands()
    {
        return m_oCommandQueue != null && m_oCommandQueue.size() > 0;
    }

    /**
     * Stops the command manager from processing commands
     */    
    @Override
    public void deactivate()
    {
        m_lActive = false;
    }

    /**
     * Gets the state of the command manager
     * @return true if the command manager is processing commands
     */
    @Override
    public boolean isActive()
    {
        return m_lActive;
    }

    
    @Override
    public synchronized void addCommand(Goliath.Interfaces.Commands.ICommand<?, ?> toCommand)
    {
        if (m_oCommandQueue == null)
        {
            m_oCommandQueue = new CommandQueue();
        }
        
        if (!m_oCommandQueue.contains(toCommand))
        {
            m_oCommandQueue.add(toCommand);
        }
    }
    
    private void process()
    {
        // Get the first command off the command queue and process it
        if (m_oCommandQueue != null && m_oCommandQueue.size() > 0)
        {
            // TODO: Optimise this method
            for (int i= this.getCommandProcessCount(); i>0 && m_oCommandQueue.size() > 0; i--)
            {
                ICommand<?, ?> loCommand;
                synchronized (m_oCommandQueue)
                {
                    loCommand = m_oCommandQueue.poll();
                    m_oCurrent = loCommand;
                }
                if (loCommand != null)
                {
                    try
                    {
                        loCommand.execute();
                    }
                    catch (Throwable ex)
                    {
                        Application.getInstance().log(ex);
                    }

                    // TODO: Implement this with asynchronous commands as well
                    addCompletedCommand(loCommand);
                    if (m_oCurrent == loCommand)
                    {
                        m_oCurrent = null;
                    }
                }
            }
        }
    }
    
    @Override
    public void addCompletedCommand(ICommand<?, ?> toCommand)
    {
        if (toCommand.isComplete() && toCommand.getStoreResult())
        {
            try
            {
                getCommandResults().put(toCommand, Goliath.Utilities.isNull(toCommand.getResult(), ""));
            }
            catch (ProcessNotComplete toException)
            {
                
            }
        }
    }
    
    @Override
    public synchronized HashTable<String, Float> getCommandStatus()
    {
        HashTable<String, Float> loReturn = new HashTable<String, Float>();
        if (m_oCommandQueue != null)
        {
            for(ICommand<?, ?> loCommand : this.m_oCommandQueue.toArray())
            {
                loReturn.put(loCommand.getID(), loCommand.getProgress());
            }

            // Also get the currently processing command
            if (m_oCurrent != null)
            {
                loReturn.put(m_oCurrent.getID(), m_oCurrent.getProgress());
            }
        }

        return loReturn;
    }
    
    @Override
    public synchronized Goliath.Collections.HashTable<ICommand<?, ?>, java.lang.Object> getCommandResults()
    {
        if (m_oCommandResults == null)
        {
            m_oCommandResults = new Goliath.Collections.HashTable<ICommand<?, ?>, java.lang.Object>();
        }
        return m_oCommandResults;
    }
    
    @Override
    public synchronized java.lang.Object popCommandResult(ICommand<?, ?> toCommand)
    {
        java.lang.Object loReturn = null;
        if (toCommand.isComplete())
        {
            if (getCommandResults().containsKey(toCommand))
            {
                getCommandResults().remove(toCommand);
                try
                {
                    loReturn = toCommand.getResult();
                }
                catch(ProcessNotComplete ex)
                {
                    // Do nothing because we have alread checked if the command is complete
                }
            }
        }
        return loReturn;
    }
    
    
}
