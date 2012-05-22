/* =========================================================
 * ObjectNotCreatedException.java
 *
 * Author:      kmchugh
 * Created:     13 November 2007, 13:03
 *
 * Description
 * --------------------------------------------------------
 * This exception is used to notify a user that an object
 * was not created, used normally during dynamic code.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Exceptions;

/**
 * This exception is used to notify a user that an object
 * was not created, used normally during dynamic code.
 *
 * @version     1.0 13 November 2007
 * @author      kmchugh
 **/
public class ObjectNotCreatedException  extends Goliath.Exceptions.UncheckedException
{
    
     /**
    * Creates a new instance of ObjectNotCreatedException
    * CriticalException should be thrown whenever an object creation method failed
    *
    * @param tcMessage   The error message
    */
    public ObjectNotCreatedException(String tcMessage)
    {
        super(tcMessage);
    }
    
    public ObjectNotCreatedException(Throwable toException)
    {
        super(toException);
    }

    /**
    * Creates a new instance of ObjectNotCreatedException
    *
    * @param tcMessage   The error message
    * @param toInnerException The inner exception
    */
    public ObjectNotCreatedException(String tcMessage, Throwable toInnerException)
    {
        super(tcMessage, toInnerException);
    }
    
    /**
     * Creates a new instance of object not created exception, defaults the message to include the
     * class name
     * @param toClass the class that could not be created
     */
    public ObjectNotCreatedException(Class toClass)
    {
        super("An object of type " + toClass.getName() + " could not be created");
    }

    
    
    
    
}
