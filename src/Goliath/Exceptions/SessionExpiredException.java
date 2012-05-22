/* ========================================================
 * SessionExpiredException.java
 *
 * Author:      kenmchugh
 * Created:     Dec 29, 2010, 12:43:36 PM
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

package Goliath.Exceptions;

import Goliath.Session;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 29, 2010
 * @author      kenmchugh
**/
public class SessionExpiredException extends UncheckedException
{
    /**
     * Creates a new instance of SessionExpiredException
     */
    public SessionExpiredException()
    {
        super("Session " + Session.getCurrentSession().getSessionID() + " expired.");
    }
}
