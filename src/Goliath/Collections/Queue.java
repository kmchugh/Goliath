/* =========================================================
 * Queue.java
 *
 * Author:      kmchugh
 * Created:     20 November 2007, 16:47
 *
 * Description
 * --------------------------------------------------------
 * Queue Collection class allows for a FIFO Collection
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Collections;

/**
 * Class implementing a FIFO list.
 * 
 *
 * @param T     The type that is stored in the queue
 * @see         java.util.concurrent.ConcurrentLinkedQueue
 * @version     1.0 20 November 2007
 * @author      kmchugh
 **/
public class Queue<T> extends java.util.concurrent.ConcurrentLinkedQueue<T>
{   
    /**
     * Creates a new instance of the Queue class
     */            
    public Queue()
    {
    }
}
