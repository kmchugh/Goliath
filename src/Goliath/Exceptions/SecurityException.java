/* =========================================================
 * CriticalException.java
 *
 * Author:      kmchugh
 * Created:     13 November 2007, 13:00
 *
 * Description
 * --------------------------------------------------------
 * This error should be thrown when anything really bad happens.
 * This error will shut down the session and possibly the application.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Exceptions;

/**
 * When this error is thrown it will cause the application to shut down
 * to shut down.
 *
 * @version     1.0 13 November 2007
 * @author      kmchugh
 **/
public class SecurityException  extends Goliath.Exceptions.UncheckedException
{
    
     /**
    * Creates a new instance of CriticalException
    * CriticalException should be thrown whenever something critical fails
    *
    * @param tcMessage   The error message
    */
    public SecurityException(String tcMessage)
    {
        super(tcMessage);
    }

    public SecurityException(String tcMessage, boolean tlLog)
    {
        super(tcMessage, tlLog);
    }
    
    public SecurityException(java.lang.Exception toException)
    {
        super(toException);
    }
}
