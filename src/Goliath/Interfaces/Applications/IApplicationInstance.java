/* =========================================================
 * IApplicationInstance.java
 *
 * Author:      kmchugh
 * Created:     29-May-2008, 13:35:50
 * 
 * Description
 * --------------------------------------------------------
 * General Interface Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.Applications;

import Goliath.Interfaces.ISession;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 29-May-2008
 * @author      kmchugh
**/
public interface IApplicationInstance
{
    // Sets the session for the Application Instance
    void setSession(ISession toSession);
    
    /**
     * Gets the session from the application instance
     * @return the session associated with the application instance
     */
    ISession getSession();
    
    /**
     * Sets the application title
     * @param tcTitle the new title for the application
     */
    void setTitle(String tcTitle);
    
    /**
     * gets the current title for the application
     * @return tcTitle
     */
    String getTitle();
}
