/* =========================================================
 * ThreadedUndoableCommand.java
 *
 * Author:      kmchugh
 * Created:     17-Jan-2008, 03:12:55
 * 
 * Description
 * --------------------------------------------------------
 * A command that can be undone
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
 * A command that can be undone
 *
 * @param T     The return type for the command
 * @version     1.0 17-Jan-2008
 * @author      kmchugh
**/
public abstract class ThreadedUndoableCommand<A extends Arguments, T> extends Goliath.Commands.ThreadedCommand<A, T>
        implements Goliath.Interfaces.Commands.IUndoableCommand<A, T>
{
    private Goliath.Threading.Thread m_oThread;
    private ThreadJobUndoable m_oUndoThreadJob;

    public ThreadedUndoableCommand(int tnPriority)
            throws Goliath.Exceptions.InvalidParameterException
    {
        super(tnPriority);
    }

    public ThreadedUndoableCommand(boolean tlRegister, int tnPriority)
            throws Goliath.Exceptions.InvalidParameterException
    {
        super(tlRegister, tnPriority);
    }

    public ThreadedUndoableCommand(boolean tlRegister)
    {
        super(tlRegister);
    }

    public ThreadedUndoableCommand()
    {
        super();
    }

    public synchronized boolean undo()
    {
        m_oUndoThreadJob = new ThreadJobUndoable(this);
        m_oThread = new Goliath.Threading.Thread(m_oUndoThreadJob, this.toString());
        m_oThread.start();
        return this.getUndoResult();
    }
    
    /**
     * Gets a reference to the thread that the command is running under 
     * @return a reference to a thread object
     */
    public Goliath.Threading.Thread getUndoThread()
    {
        return m_oThread;
    }
    
    /**
     * Gets the result of when the command was undone the last time.
     * If the command has never been executed this will be false.
     * @return the result from when the command was last undone
     */
    public boolean getUndoResult()
    {
        if (m_oUndoThreadJob != null)
        {
            return m_oUndoThreadJob.getResult();
        }
        else
        {
            return false;
        }
    }
    
    protected abstract boolean doUndo();
    
    private class ThreadJobUndoable implements Runnable
    {
        private ThreadedUndoableCommand<A, T> m_oCommand;
        boolean m_oReturn;

        public ThreadJobUndoable(ThreadedUndoableCommand<A, T> toCommand) 
        {
            m_oCommand = toCommand;
        }
        
        public void run() 
        {
            m_oReturn = m_oCommand.doUndo();
        }
        
        public boolean getResult()
        {
            return m_oReturn;
        }
        
    }
}
