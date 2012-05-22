/* ========================================================
 * Event.java
 *
 * Author:      kmchugh
 * Created:     Aug 3, 2010, 9:19:09 PM
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
 * ===================================================== */

package Goliath;

import Goliath.Constants.EventType;



        
/**
 * Base event object, any events to be fired throught he event dispatchers should
 * inherit from this class
 *
 * @see         Related Class
 * @version     1.0 Aug 3, 2010
 * @author      kmchugh
**/
public class Event<T> extends Goliath.Object
{
    private boolean m_lCanBubble;
    private T m_oTarget;

    /**
     * Creates a new instance of Event
     */
    public Event(T toTarget)
    {
        m_oTarget = toTarget;
        m_lCanBubble = true;
    }

    /**
     * Gets the target of this event
     * @return the object that is the target of the event
     */
    public final T getTarget()
    {
        return m_oTarget;
    }
    
    /**
     * Cancels event bubbling, multiple calls to this are a no op
     */
    public final void cancelBubble()
    {
        m_lCanBubble = false;
    }
    
    /**
     * resumes the event bubbling, multiple calls to this are a no op
     */
    public final void resumeBubble()
    {
        m_lCanBubble = true;
    }
    
    /**
     * Checks if this event is allowed to bubble up to its parents
     */
    public final boolean canBubble()
    {
        return m_lCanBubble;
    }
    
    /**
     * Should be overridden in subclasses to bubble the event to the parent of the
     * target
     */
    public <K extends EventType> void bubble(K toType)
    {
        // By default this does nothing as we do not know how to bubble an event
        // from this level
    }
}
