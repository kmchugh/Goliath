/* ========================================================
 * ObjectAlreadyExistsException.java
 *
 * Author:      kenmchugh
 * Created:     Dec 31, 2010, 4:08:49 PM
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


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 31, 2010
 * @author      kenmchugh
**/
public class ObjectAlreadyExistsException extends Goliath.Exceptions.UncheckedException
{

     /**
    * Creates a new instance of ObjectNotCreatedException
    * CriticalException should be thrown whenever an object creation method failed
    *
    * @param tcMessage   The error message
    */
    public ObjectAlreadyExistsException(String tcMessage)
    {
        super(tcMessage);
    }

    public ObjectAlreadyExistsException(Throwable toException)
    {
        super(toException);
    }

    /**
    * Creates a new instance of ObjectNotCreatedException
    *
    * @param tcMessage   The error message
    * @param toInnerException The inner exception
    */
    public ObjectAlreadyExistsException(String tcMessage, Throwable toInnerException)
    {
        super(tcMessage, toInnerException);
    }
}
