/* =========================================================
 * ClassNotFoundException.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This error is used to notify when a class was requested but could not be found
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;

/**
 * This error is used to notify when a class was requested but could not be found
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class ClassNotFoundException extends Goliath.Exceptions.UncheckedException
{
    /**
    * Creates a new instance of ClassNotFoundException
    * ClassNotFoundException is used throughout the Goliath.DynamicCode.Java class
    *
    * @param tcMessage     The message for the exception
    */
    public ClassNotFoundException(String tcMessage)
    {
        super(tcMessage);
    }
}
