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
public class CriticalException  extends Goliath.Exceptions.UncheckedException
{
    
     /**
    * Creates a new instance of CriticalException
    * CriticalException should be thrown whenever something critical fails
    *
    * @param tcMessage   The error message
    */
    public CriticalException(String tcMessage)
    {
        super(tcMessage);
        Goliath.Applications.Application.getInstance().forceShutdown(-1);
    }

    public CriticalException(String tcMessage, Throwable toException)
    {
        super(tcMessage, toException);
        Goliath.Applications.Application.getInstance().forceShutdown(-1);
    }
    
    public CriticalException(Throwable toException)
    {
        super(toException);
        Goliath.Applications.Application.getInstance().forceShutdown(-1);
    }
}
