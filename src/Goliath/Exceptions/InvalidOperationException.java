/* =========================================================
 * InvalidOperationException.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This error is used to notify when an invalid operation is attempted.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;


/**
 * This error is used to notify when an invalid operation was attempted
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class InvalidOperationException extends Goliath.Exceptions.UncheckedException
{
    /**
    * Creates a new instance of InvalidOperationException
    * InvalidOperationException should be thrown whenever an operation fails
    *
    * @param tcMessage   The error message
    */
    public InvalidOperationException(String tcMessage)
    {
        super(tcMessage);
    }

    /**
     * Creates a new instance of InvalidOperationException
     * InvalidOperationException should be thrown whenever an operation fails
     *
     * @param tcMessage   The error message
     * @param tlLog if true this message will be logged on creation
     */
    public InvalidOperationException(String tcMessage, boolean tlLog)
    {
        super(tcMessage, tlLog);
    }
    
    /**
    * Creates a new instance of InvalidOperationException
    * InvalidOperationException should be thrown whenever an operation fails
    *
    * @param tcMessage   The error message
    * @param toException The inner exception
    */
    public InvalidOperationException(String tcMessage, Throwable toException)
    {
        super(tcMessage, toException);
    }
    
    /**
    * Creates a new instance of InvalidOperationException
    * InvalidOperationException should be thrown whenever an operation fails
    */
    public InvalidOperationException()
    {
        super(Goliath.Constants.Strings.AN_INVALID_OPERATION_WAS_ATTEMPTED);
    }
    
    
   
}
