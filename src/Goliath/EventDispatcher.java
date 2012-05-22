/* ========================================================
 * EventDispatcher.java
 *
 * Author:      kmchugh
 * Created:     Jul 31, 2010, 5:01:28 PM
 *
 * Description
 * --------------------------------------------------------
 * Helper class to handle management and firing of events
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.EventType;
import Goliath.Constants.LogType;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.IEventDispatcher;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 $(date)
 * @author      kmchugh
**/
public class EventDispatcher<T extends EventType, E extends Event> extends Goliath.Object
        implements IEventDispatcher<T, E>
{
    private boolean m_lSuppressEvents;
    private HashTable<T, List<IDelegate>> m_oEventListeners;

    /**
     * Creates a new instance of EventHandler
     */
    public EventDispatcher()
    {
    }

    /**
     * Checks if this dispatcher is currently suppressing events, it it is, then
     * it will simply not notify delegates of events
     * @return
     */
    @Override
    public boolean areEventsSuppressed()
    {
        return m_lSuppressEvents;
    }

    /**
     * Sets if this dispatcher is suppressing events or not
     * @param tlSuppress
     */
    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        m_lSuppressEvents = tlSuppress;
    }


    /**
     * Forces the specified event to fire
     * @param toEventType The event type to fire
     * @param toEvent the object to pass as the event
     */
    @Override
    public final void fireEvent(T toEventType, E toEvent)
    {
        if (hasEventsFor(toEventType))
        {
            List<IDelegate> loDelegates = new List(m_oEventListeners.get(toEventType));
            for (IDelegate loDelegate : loDelegates)
            {
                try
                {
                    loDelegate.invoke(toEvent);
                }
                catch(Throwable ex)
                {
                    Application.getInstance().log("Event handler failed for " + toEventType.toString() + " on class " + this.getClass().getSimpleName(), LogType.WARNING());
                    Application.getInstance().log(ex);
                }
            }
        }
        
        if (toEvent.canBubble())
        {
            toEvent.bubble(toEventType);
        }
    }


    /**
     * Checks if there are any events registered for the specified event
     * @param toEvent The event to check for
     * @return true if there are events, otherwise false
     */
    @Override
    public boolean hasEventsFor(T toEvent)
    {
        return !(m_oEventListeners == null || !m_oEventListeners.containsKey(toEvent));
    }


    /**
     * Adds an event listener for the specified event
     * @param toEvent the event to add the listener for
     * @param toCallback the method that is called back for the event
     * @return true if an listener was added, false if the listener already existed
     */
    @Override
    public boolean addEventListener(T toEvent, IDelegate toCallback)
    {
        if (toEvent == null || toCallback == null)
        {
            return false;
        }

        boolean llReturn = false;
        if (m_oEventListeners == null)
        {
            m_oEventListeners = new HashTable<T, List<IDelegate>>();
        }

        if (!m_oEventListeners.containsKey(toEvent))
        {
            m_oEventListeners.put(toEvent, new List<IDelegate>());
        }

        if (!m_oEventListeners.get(toEvent).contains(toCallback))
        {
            llReturn = m_oEventListeners.get(toEvent).add(toCallback);
        }
        return llReturn;
    }

    /**
     * Removes the specified listener from the event
     * @param toEvent the event
     * @param toCallback the listener to remove
     * @return true if it was removed, false if it never existed
     */
    @Override
    public boolean removeEventListener(T toEvent, IDelegate toCallback)
    {
        if (m_oEventListeners == null || !m_oEventListeners.containsKey(toEvent) || !m_oEventListeners.get(toEvent).contains(toCallback))
        {
            return false;
        }

        boolean llReturn = m_oEventListeners.get(toEvent).remove(toCallback);

        if (m_oEventListeners.get(toEvent).size() == 0)
        {
            clearEventListeners(toEvent);
        }

        return llReturn;
    }

    /**
     * Clears all the event listeners on this control
     * @return true if listeners were cleared, false if there were no listeners to clear
     */
    @Override
    public boolean clearEventListeners()
    {
        if (m_oEventListeners == null)
        {
            return false;
        }
        // TODO: Trace through to see if we leak memory if we don't clean up the delegates
        m_oEventListeners = null;

        return true;
    }

    /**
     * Clears all the event listeners for the specified event
     * @param toEvent the event to clear listeners for
     * @return true if listeners were cleared, false if there were no listeners to clear
     */
    @Override
    public boolean clearEventListeners(T toEvent)
    {
        if (m_oEventListeners == null || !m_oEventListeners.containsKey(toEvent))
        {
            return false;
        }

        boolean llReturn = m_oEventListeners.remove(toEvent) != null;

        if (m_oEventListeners.size() == 0)
        {
            m_oEventListeners = null;
        }

        return llReturn;
    }
}
