/* ========================================================
 * ApplicationController.java
 *
 * Author:      kmchugh
 * Created:     Nov 2, 2010, 8:47:38 PM
 *
 * Description
 * --------------------------------------------------------
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Applications;

import Goliath.Constants.EventType;
import Goliath.Event;
import Goliath.EventDispatcher;
import Goliath.Interfaces.Applications.IApplicationController;
import Goliath.Interfaces.IDelegate;


        
/**
 * The Application Controller controls the application rules
 * it should be the communication device to the Application window
 *
 * @version     1.0 Nov 2, 2010
 * @author      kmchugh
**/
public abstract class ApplicationController<T extends EventType, E extends ApplicationController> extends Goliath.Object
        implements IApplicationController<T, E>
{
    public static IApplicationController getInstance()
    {
        return Application.getInstance().getApplicationController();
    }

    private EventDispatcher<T, Event<E>> m_oEventDispatcher;

    /**
     * Creates a new instance of ApplicationController
     */
    public ApplicationController()
    {
    }

    

    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        if (m_oEventDispatcher == null)
        {
            return;
        }
        m_oEventDispatcher.suppressEvents(tlSuppress);
    }

    @Override
    public boolean removeEventListener(T toEvent, IDelegate toCallback)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.removeEventListener(toEvent, toCallback) : false;
    }

    @Override
    public boolean hasEventsFor(T toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.hasEventsFor(toEvent) : false;
    }

    @Override
    public final void fireEvent(T toEventType, Event<E> toEvent)
    {
        if (m_oEventDispatcher == null)
        {
            return;
        }
        m_oEventDispatcher.fireEvent(toEventType, toEvent);
    }

    @Override
    public boolean clearEventListeners(T toEvent)
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
            m_oEventDispatcher = new EventDispatcher<T, Event<E>>();
        }
        return m_oEventDispatcher.addEventListener(toEvent, toCallback);
    }
    /*
     * Gets a license from the server if a licence file does not exist
     * The licence server will place the .licence file onto the application server
     * that can then be read by the application settings class.
     * Returns true if the licence is successfully created - currently no code
     */
    /**
     * Places a license file for the current user/application into the application server. 
     * @return true if the license file is successfully created otherwise false
     */
    @Override
    public  boolean getLicenceFromServer()
    {
        return false;
    }

    @Override
    public abstract void requestAuthentication();

    @Override
    public abstract boolean authenticate(String tcUserName, String tcPassword, IDelegate<E> toFailed, IDelegate<E> toSuccess);

    @Override
    public abstract boolean unauthenticate();






}
