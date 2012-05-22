/* =========================================================
 * WriteAccessException.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 10, 2008, 9:10:12 PM
 * 
 * Description
 * --------------------------------------------------------
 * Used for exceptions when a write was attempted on a read only
 * resource
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;

/**
 * Used for exceptions when a write was attempted on a read only
 * resource
 *
 * @version     1.0 Jan 10, 2008
 * @author      Ken McHugh
**/
public class WriteAccessException extends Goliath.Exceptions.Exception
{
    /**
    * Creates a new instance of InvalidOperationException
    *
    * @param tcMessage   The error message
    */
    public WriteAccessException(String tcMessage)
    {
        super(tcMessage);
    }
    
    /**
    * Creates a new instance of InvalidOperationException
    *
    * @param tcMessage   The error message
    * @param toException The inner exception
    */
    public WriteAccessException(String tcMessage, java.lang.Exception toException)
    {
        super(tcMessage, toException);
    }
}
