/* =========================================================
 * InvalidIOException.java
 *
 * Author:      Peter
 * Created:     14th December 2007
 *
 * Description
 * --------------------------------------------------------
 * This exception should be thrown when an operation is attempted
 * on a file that throws an IO exception or Security Exception
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
 * @version     1.0 13 December 2007
 * @author      Peter
 **/
public class InvalidIOException  extends Goliath.Exceptions.UncheckedException
{
    
    /**
    * Creates a new instance of InvalidIOException
    * InvalidIOException should be thrown whenever a file IO error occurs
    *
    * @param tcMessage   The error message
    */
    public InvalidIOException(String tcMessage)
    {
        super(tcMessage);
    }

    /**
    * Creates a new instance of InvalidIOException
    *
    * @param toException   The inner exception
    */
    public InvalidIOException(Throwable toException)
    {
        super(toException);
    }
    
    /**
    * Creates a new instance of InvalidIOException
    *
     * @param toException   The inner exception
     * @param tlLogError if true the error will be logged
    */
    public InvalidIOException(Throwable toException, boolean tlLogError)
    {
        super(toException, tlLogError);
    }
    
    /**
     * Creates a new instance of InvalidIOException
     *
     * @param tcString    The message
     * @param toException the inner exception
     */
    public InvalidIOException(String tcString, java.lang.Exception toException)
    {
        super(tcString, toException);
    }
    
}
