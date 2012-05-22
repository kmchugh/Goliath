/* =========================================================
 * ProcessNotComplete.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:13:40
 * 
 * Description
 * --------------------------------------------------------
 * Used for signalling a call to a process that was too early
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;

/**
 * Used for signalling a call to a process that was too early
 *
 * @version     1.0 28-Jan-2008
 * @author      kmchugh
**/
public class ProcessNotComplete extends Goliath.Exceptions.Exception
{
    
     /**
    * Creates a new instance of ProcessNotComplete
    *
    * @param tcMessage   The error message
    */
    public ProcessNotComplete(String tcMessage)
    {
        super(tcMessage);
    }

    /**
    * Creates a new instance of ProcessNotComplete
    *
    * @param tcMessage   The error message
    * @param toInnerException The inner exception
    */
    public ProcessNotComplete(String tcMessage, java.lang.Exception toInnerException)
    {
        super(tcMessage, toInnerException);
    }
}
