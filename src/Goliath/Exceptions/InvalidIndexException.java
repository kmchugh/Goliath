/* =========================================================
 * InvalidIndexException.java
 *
 * Author:      kmchugh
 * Created:     20 November 2007, 17:38
 *
 * Description
 * --------------------------------------------------------
 * This exception should be thrown when an operation is attempted
 * on an indexed item in a collection where the index is out of range
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Exceptions;

/**
 * This exception should be thrown when an operation is attempted
 * on an indexed item in a collection where the index is out of range
 *
 * @version     1.0 20 November 2007
 * @author      kmchugh
 **/
public class InvalidIndexException  extends Goliath.Exceptions.UncheckedException
{
    
     /**
    * Creates a new instance of InvalidIndexException
    * InvalidIndexException should be thrown whenever retrieval of an
    * object from a collection is attempted where the index is out of range
    *
    * @param tcMessage   The error message
    */
    public InvalidIndexException(String tcMessage)
    {
        super(tcMessage);
    }
}
