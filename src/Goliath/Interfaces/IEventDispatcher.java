/* ========================================================
 * IEventDispatcher.java
 *
 * Author:      kmchugh
 * Created:     Jul 31, 2010, 5:03:56 PM
 *
 * Description
 * --------------------------------------------------------
 * Indicate that an object is able to manage and fire its
 * own events
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Interfaces;

import Goliath.Constants.EventType;
import Goliath.Event;




/**
 * Implements an event Dispatcher.
 * The following code is all that is needed to give an object the ability
 * to cleanly fire and manage events, while lazy loading the event dispatcher
 * <pre>
 *      
    private EventDispatcher<T, E> m_oEventDispatcher;

    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        if (m_oEventDispatcher != null)
        {
            m_oEventDispatcher.suppressEvents(tlSuppress);
        }
    }

    @Override
    public boolean removeEventListener(EventType toEvent, IDelegate toCallback)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.removeEventListener(toEvent, toCallback) : false;
    }

    @Override
    public boolean hasEventsFor(EventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.hasEventsFor(toEvent) : false;
    }

    @Override
    public final void fireEvent(T toEventType, E toEvent)
    {
        if (m_oEventDispatcher != null)
        {
            m_oEventDispatcher.fireEvent(toEventType, toEvent);
        }
    }

    @Override
    public boolean clearEventListeners(EventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners(toEvent) : false;
    }

    @Override
    public boolean clearEventListeners()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners() : false;
    }

    @Override
    public boolean areEventsSuppressed()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.areEventsSuppressed() : false;
    }

    @Override
    public boolean addEventListener(T toEvent, IDelegate toCallback)
    {
        if (m_oEventDispatcher == null)
        {
            m_oEventDispatcher = new EventDispatcher<T, E>();
        }
        return m_oEventDispatcher.addEventListener(toEvent, toCallback);
    }

 * </pre>
 *
 * @see         Related Class
 * @version     1.0 $(date)
 * @author      kmchugh
**/
public interface IEventDispatcher<T extends EventType, E extends Event>
{
    /**
     * Checks if this dispatcher is currently suppressing events, it it is, then
     * it will simply not notify delegates of events
     * @return
     */
    boolean areEventsSuppressed();

    /**
     * Sets if this dispatcher is suppressing events or not
     * @param tlSuppress
     */
    void suppressEvents(boolean tlSuppress);


    /**
     * Forces the specified event to fire
     * @param toEventType The event type to fire
     * @param toEvent the object to pass as the event
     */
    void fireEvent(T toEventType, E toEvent);


    /**
     * Checks if there are any events registered for the specified event
     * @param toEvent The event to check for
     * @return true if there are events, otherwise false
     */
    boolean hasEventsFor(T toEvent);

    /**
     * Adds an event listener for the specified event
     * @param toEvent the event to add the listener for
     * @param toCallback the method that is called back for the event
     * @return true if an listener was added, false if the listener already existed
     */
    boolean addEventListener(T toEvent, IDelegate toCallback);

    /**
     * Removes the specified listener from the event
     * @param toEvent the event
     * @param toCallback the listener to remove
     * @return true if it was removed, false if it never existed
     */
    boolean removeEventListener(T toEvent, IDelegate toCallback);

    /**
     * Clears all the event listeners on this control
     * @return true if listeners were cleared, false if there were no listeners to clear
     */
    boolean clearEventListeners();

    /**
     * Clears all the event listeners for the specified event
     * @param toEvent the event to clear listeners for
     * @return true if listeners were cleared, false if there were no listeners to clear
     */
    boolean clearEventListeners(T toEvent);
}
