/* =========================================================
 * ThreadedCommand.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 16, 2008, 3:29:13 AM
 * 
 * Description
 * --------------------------------------------------------
 * Command object that when executed will run in a new thread
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Commands;

import Goliath.Arguments.Arguments;

/**
 * Command object that when executed will run in a new thread
 *
 * @param T     The return type for the execute command
 * @version     1.0 Jan 16, 2008
 * @author      Ken McHugh
**/
public abstract class ThreadedCommand<A extends Arguments, T> extends Command<A, T>
{
    private Goliath.Threading.Thread m_oThread;
    private ThreadJob m_oThreadJob;

    /**
     * Creates a new ThreadedCommand and sets the priority 
     *
     * @param  tnPriority   The priority to create this thread with
     * @throws Goliath.Exceptions.InvalidParameterException if tnPriority is less than 0
     */
    public ThreadedCommand(int tnPriority) 
            throws Goliath.Exceptions.InvalidParameterException
    {
        super(tnPriority);
    }

     /**
     * This creates an instance of the ThreadCommand object.
     *
     * @param  tlRegister   True if you want to register the command immediately
     * @param  tnPriority   The priority of the command
     * @throws Goliath.Exceptions.InvalidParameterException if tnPriority is less than 0
     */
    public ThreadedCommand(boolean tlRegister, int tnPriority) 
            throws Goliath.Exceptions.InvalidParameterException
    {
        super(tlRegister, tnPriority);
    }
    
    /**
     * This creates an instance of the ThreadCommand object.
     *
     * @param  tlRegister   True if you want to register the command immediately
     */
    public ThreadedCommand(boolean tlRegister) 
    {
        super(tlRegister);
    }

    public ThreadedCommand() 
    {
    }

    /**
     * Executes the command.  
     *
     * @param  taArgs   a list of parameters
     * @return  returns  true always.
     */
    @Override
    public boolean execute(A taArgs) 
    {
        synchronized (this)
        {
            m_oThreadJob = new ThreadJob(this, taArgs);
            m_oThread = new Goliath.Threading.Thread(m_oThreadJob, this.toString());
            m_oThread.start();
            return true;
        }
    }
    

    /**
     * Gets a reference to the thread that the command is running under 
     * @return a reference to a thread object
     */
    public Goliath.Threading.Thread getThread()
    {
        return m_oThread;
    }
    
    /**
     * Gets the result of when the command was executed the last time.
     * If the command has never been executed this will be null.
     * @return the result from when the command was last run
     */
    @Override
    public T getResult() throws Goliath.Exceptions.ProcessNotComplete
    {
        // Ensure the command is complete first
        if (!isComplete())
        {
            throw new Goliath.Exceptions.ProcessNotComplete("The command has not been completed yet");
        }
        if (m_oThreadJob != null)
        {
            return m_oThreadJob.getResult();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Helper class for running a thread
     * TODO: Replace this with Goliath.Threading when it is complete
     */
    private class ThreadJob implements Runnable
    {
        private ThreadedCommand<A, T> m_oCommand;
        private A m_aArgs;
        T m_oReturn;

        public ThreadJob(ThreadedCommand<A, T> toCommand, A taArgs) 
        {
            m_oCommand = toCommand;
            m_aArgs = taArgs;
        }
        
        public void run() 
        {
            try
            {
                m_oCommand.resetCommand();
                m_oCommand.execute(m_aArgs);
            }
            catch (Throwable t)
            {
                m_oCommand.addError(new Goliath.Exceptions.Exception(t.getLocalizedMessage()));
            }
            finally
            {
                m_oCommand.setProgress(100);
            }
        }
        
        public T getResult()
        {
            return m_oReturn;
        }
        
    }
   
}
