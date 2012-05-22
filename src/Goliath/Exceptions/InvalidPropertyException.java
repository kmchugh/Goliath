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
public class InvalidPropertyException extends Goliath.Exceptions.Exception
{
    
     /**
    * Creates a new instance of ProcessNotComplete
    *
    * @param tcMessage   The error message
    */
    public InvalidPropertyException(String tcClassName, String tcProperty)
    {
        super("Class " + tcClassName + " does not contain property " + tcProperty);
    }

    /**
    * Creates a new instance of ProcessNotComplete
    *
    * @param tcMessage   The error message
    * @param toInnerException The inner exception
    */
    public InvalidPropertyException(String tcMessage, java.lang.Exception toInnerException)
    {
        super(tcMessage, toInnerException);
    }
}
