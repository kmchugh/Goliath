/* =========================================================
 * UnsupportedOperationException.java
 *
 * Author:      kmchugh
 * Created:     13 November 2007, 13:03
 *
 * Description
 * --------------------------------------------------------
 * This Exception is to be used when an opperation is attempted that is not
 * currently supported or has not been defined.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Exceptions;

/**
 * This Exception is to be used when an opperation is attempted that is not
 * currently supported or has not been defined.
 *
 * @version     1.0 13 November 2007
 * @author      kmchugh
 **/
public class UnsupportedOperationException  extends Goliath.Exceptions.UncheckedException
{
    
     /**
    * Creates a new instance of UnsupportedOperationException
    * CriticalException should be thrown whenever an operation is not allowed
    * or has not been defined
    *
    * @param tcMessage   The error message
    */
    public UnsupportedOperationException(String tcMessage)
    {
        super(tcMessage);
    }
    
}
