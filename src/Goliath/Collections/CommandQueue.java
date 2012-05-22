/* =========================================================
 * CommandQueue.java
 *
 * Author:      kmchugh
 * Created:     20 November 2007, 17:12
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

package Goliath.Collections;

import Goliath.Constants.StringFormatType;
import Goliath.Interfaces.Commands.ICommand;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class Description.
 * A queue of commands ordered by command priority then fifo
 *
 * @see         java.util.Queue
 * @version     1.0 20 November 2007
 * @author      kmchugh
 **/
public class CommandQueue extends Goliath.Object implements java.util.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>>
{
    
    /**
     *  A hashmap of queues which contain ICommand objects
     */
    private java.util.concurrent.ConcurrentHashMap<Integer, Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>>> m_oLists = 
            new java.util.concurrent.ConcurrentHashMap<Integer, Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>>>();
    
    /** Creates a new instance of CommandQueue */
    public CommandQueue()
    {
    }
    
    /**
     * Gets or creates the queue with the correct priority
     *
     * @param  tnPriority the priority of the queue to get
     * @return  the queue with the correct priority
     */
    private synchronized Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> getQueue(int tnPriority)
    {
        if (m_oLists.containsKey(tnPriority))
        {
            // List already exists for this priority
            return m_oLists.get(tnPriority);
        }
        else
        {
            // List does not exist for this priority, so create it
            m_oLists.put(tnPriority, new Queue<ICommand<?, ?>>());
            return m_oLists.get(tnPriority);
        }
    }
    
     /**
     * Gets the queue that is the highest priority
     *
     * @return  the highest priority queue, or null if there are no queues
     */
    private synchronized Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> getHeadQueue()
    {
        if (m_oLists.size() != 0)                                
        {
            return m_oLists.elements().nextElement();
        }
        else
        {
            // There were no queues
            return null;
        }
    }
    
    /**
     * Removes a queue if it is empty
     *
     * @param  tnPriority the priority level of the queue to check
     */
    private synchronized void removeQueueIfEmpty(int tnPriority)
    {
        if (m_oLists.containsKey(tnPriority))
        {
            if (m_oLists.get(tnPriority).size() == 0)
            {
                m_oLists.remove(tnPriority);
            }
        }
    }
    
    /**
     * Adds an item to the list if possible
     *
     * @param  toCommand    The command to add to the queue
     * @return false if it was not possible to add an item to the list
     */
    public synchronized boolean offer(ICommand<?, ?> toCommand)
    {
        // Get the queue for this command priority and add the command
        return getQueue(toCommand.getPriority()).offer(toCommand);                
    }

    /**
     * Adds a command to the Queue
     *
     * @param  toCommand    The command to add to the queue
     * @return true if the collection changed as a result of this call
     */
    public synchronized boolean add(ICommand<?, ?> toCommand)
    {
        // Get the queue for this command priority and add the command
        return getQueue(toCommand.getPriority()).add(toCommand);
    }
    
    /**
     * Retrieves and removes an item from the head of a collection, throws an exception if the queue is empty
     *
     * @return  The ICommand item that is returned
     * @see Goliath.Interfaces.ICommand
     * @throws NullPointerException
     */
    public synchronized ICommand<?, ?> remove()
    {
         Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> loQueue = getHeadQueue();
         if (loQueue != null)
         {
             // Check if the queue is empty
             ICommand loCommand = loQueue.remove();
             removeQueueIfEmpty(loCommand.getPriority());
             return loCommand;
         }
         else
         {
             throw new NullPointerException();
         }
    }

    /**
     * Retrieves and removes and item from the head of the collection
     *
     * @return  the item at the head of the queue or null if there are no commands left
     */
    public synchronized ICommand poll()
    {
        Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> loQueue = getHeadQueue();
         if (loQueue != null)
         {
             // Check if the queue is empty
             ICommand loCommand = loQueue.poll();
             removeQueueIfEmpty(loCommand.getPriority());
             return loCommand;
         }
         return null;
    }

    /**
     * Retrieves a command from the head of the queue.  Does not remove from the queue.
     *
     * @return  the item at the head of the queue, or null if the queue is empty
     */
    public synchronized ICommand peek()
    {
        Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> loQueue = getHeadQueue();
        if (loQueue != null)
        {
            return loQueue.peek();
        }
        return null;
    }
    
    /**
     * Returns an array of ICommand objects
     *
     * @return  an array of ICommand objects
     */
    @Override
    public synchronized Goliath.Interfaces.Commands.ICommand<?, ?>[] toArray()
    {
        Goliath.Interfaces.Commands.ICommand<?, ?>[] laCommands = new Goliath.Interfaces.Commands.ICommand<?, ?>[size()];
        int i = 0;
        // Loop through all of the queues and all of the commands
        for (Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> loQueue : m_oLists.values())
        {
            for (Goliath.Interfaces.Commands.ICommand<?, ?> loCommand : loQueue)
            {
                laCommands[i] = loCommand;                
                i++;
            }
        }
        return laCommands;
    }

    /**
     * Calculates the number of ICommands currently in the queue
     *
     * @return the number if Commands in the queue
     */
    @Override
    public synchronized int size()
    {
        int lnCount = 0;
        // TODO: Optimise this method
        for (int i = 0; i < m_oLists.size(); i++)
        {
            lnCount = lnCount + m_oLists.get(i).size();
        }
        return lnCount;
    }

    /**
     * Clears all of the commands from the queue.  Any commands cleared will not be executed
     */
    @Override
    public synchronized void clear()
    {
        // Clear all of the commands first
        for (Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> loQueue : m_oLists.values())
        {
            loQueue.clear();
        }
        
        // Clear the queues
        m_oLists.clear();
        
    }

    /**
     * Currently throws a CloneNotSupportedException.
     *
     * @return throws an error
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }

    /**
     * retrieves but does not remove the head of a list, throws an exception if the list is empty
     *
     * @return the head of the list
     * @throws java.util.NoSuchElementException
     */
    public synchronized ICommand<?, ?> element()
    {
        ICommand<?, ?> loCommand = peek();
        if (loCommand == null)
        {
            throw new java.util.NoSuchElementException();
        }
        return loCommand;
    }

    
    @Override
    protected void finalize() throws Throwable
    {
        clear();
        super.finalize();
    }

    @Override
    public int hashCode()
    {
        int retValue;
        
        retValue = super.hashCode();
        return retValue;
    }

    public synchronized boolean isEmpty()
    {
        return m_oLists.isEmpty();
    }

    public synchronized Iterator<ICommand<?, ?>> iterator()
    {
        return new CommandQueueIterator(this);
    }

    
    public synchronized boolean remove(Object toObject)
    {
        for (Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> loQueue : m_oLists.values())
        {
            if (loQueue.contains(toObject))
            {
                return loQueue.remove(toObject);
            }
        }
        return false;                
    }

    public synchronized boolean contains(Object toObject)
    {
        for (Goliath.Collections.Queue<Goliath.Interfaces.Commands.ICommand<?, ?>> loQueue : m_oLists.values())
        {
            if (loQueue.contains(toObject))
            {
                return true;
            }
        }
        return false;  
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean retValue;
        
        retValue = super.equals(obj);
        return retValue;
    }

    public synchronized <T> T[] toArray(T[] taCommands)
    {
        throw new ArrayStoreException();
    }
    

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return getClass().getName() + "[" + size() + "]";
    }

    /**
     * Retains all of the items in this collection that are also contained in 
     * the specified collection
     *
     * @param  c The specified collection
     * @return  true if the collection is modified
     */
    public synchronized boolean retainAll(Collection<?> c)
    {
        boolean llReturn = false;
        for(ICommand<?, ?> loCommand : this)
        {
            if (!c.contains(loCommand))
            {
                llReturn = this.remove(loCommand) || llReturn;
            }
        }
        return llReturn;
    }

    /**
     * Removes all the elements from this collection that are also 
     * contained in the specified collection
     *
     * @param  c the specified collection
     * @return true if the collection is modified
     */
    public synchronized boolean removeAll(Collection<?> c)
    {
        boolean llReturn = false;
        for(java.lang.Object loItem : c)
        {
            llReturn = this.remove(loItem) || llReturn;
        }
        return llReturn;
    }

    /**
     * Checks if all the items in the specified collection are contained
     * in this collection
     *
     * @param  c the specified collection
     * @return  true if all the items are contained
     */
    public synchronized boolean containsAll(Collection<?> c)
    {
        for(java.lang.Object loItem : c)
        {
            if (!this.contains(loItem))
            {
                return false;                
            }
        }
        return true;
    }

    /**
     * Adds all of the items from the specified collection to this collection
     *
     * @param  c the specified collection
     * @return  true if this collection is changed
     */
    public synchronized boolean addAll(Collection<? extends ICommand<?, ?>> c)
    {
        boolean llReturn = false;
        for(Goliath.Interfaces.Commands.ICommand<?, ?> loCommand : c)
        {
            llReturn = this.add(loCommand) || llReturn;
        }
        return true;
    }
}
