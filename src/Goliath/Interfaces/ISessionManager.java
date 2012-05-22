/* =========================================================
 * ISessionManager.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This interface represents a Session Manager.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.Interfaces;

/**
 * This interface represents a Session Manager
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public interface ISessionManager
{/**
     * deactivates and unload the session manager
     */
    void deactivate();
    
    /**
     * Activates and loads the session manager
     */
    void activate();
    
    /**
     * Checks if this session manager is currently active
     * @return true if the session manager is active
     */
    boolean isActive();
    
    /**
     * Creates a new session
     *
     * @return  a new session
     */
    Goliath.Interfaces.ISession createSession();
    
    /**
     * Creates a new session using the provided session id
     *
     * @param  tcSessionID the session id to use to create the session
     * @return  a new session
     */
    Goliath.Interfaces.ISession createSession(String tcSessionID);
    
    /**
     * Gets an existing session based on the session id
     *
     * @param  tcSessionID  the session id to get a reference to
     * @return  a session with a matching id, or null if there were no matches
     */
    Goliath.Interfaces.ISession getSession(String tcSessionID);

    /**
     * Checks if the session specified exists
     * @param tcSessionID the session to check for
     * @return true if the session exists, false otherwise
     */
    boolean hasSession(String tcSessionID);
    
    
     /**
     * Clears an existing session based on the session id
     *
     * @param  tcSessionID  the session id to get a reference to
     */
     void clearSession(String tcSessionID);
}
