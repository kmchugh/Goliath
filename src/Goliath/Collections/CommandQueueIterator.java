/* =========================================================
 * CommandQueueIterator.java
 *
 * Author:      kmchugh
 * Created:     26 November 2007, 11:21
 *
 * Description
 * --------------------------------------------------------
 * Used for iterating a CommandQueue.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Collections;

/**
 * Iterates through a command queue, does not reflect changes to the 
 * command queue after the iterator has been created
 *
 * @see         CommandQueue
 * @version     1.0 26 November 2007
 * @author      kmchugh
 **/
public class CommandQueueIterator implements java.util.Iterator<Goliath.Interfaces.Commands.ICommand<?, ?>>
{
    private Goliath.Interfaces.Commands.ICommand<?, ?>[] m_aCommands;
    private int m_nPointer = 0;
    
    /**
     * Creates a new instance of the Iterator
     *
     * @param  toQueue The command queue to iterate over
     */
    public CommandQueueIterator(CommandQueue toQueue)
    {
        m_aCommands = toQueue.toArray();
        m_nPointer = 0;
    }

    /**
     * Gets the next item in the list
     *
     * @return  the next command in the list, or null if there are no items left
     */
    public Goliath.Interfaces.Commands.ICommand<?, ?> next()
    {
        if (hasNext())
        {
            Goliath.Interfaces.Commands.ICommand<?, ?> loCommand = m_aCommands[m_nPointer];
            m_nPointer++;                
            return loCommand;
        }
        return null;
    }

    public boolean hasNext()
    {
        return m_nPointer < m_aCommands.length ;
    }

    /**
     * Unsupported for the moment.
     *
     * @throws UnsupportedOperationException
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
    
}
