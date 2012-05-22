/* ========================================================
 * MethodNotFoundException.java
 *
 * Author:      kenmchugh
 * Created:     Mar 21, 2011, 12:18:17 PM
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
 * @version     1.0 Mar 21, 2011
 * @author      kenmchugh
**/
public class MethodNotFoundException extends Goliath.Exceptions.UncheckedException
{
    /**
    * Creates a new instance of ClassNotFoundException
    * ClassNotFoundException is used throughout the Goliath.DynamicCode.Java class
    *
    * @param tcMessage     The message for the exception
    */
    public MethodNotFoundException(String tcMethod, Class[] taParams)
    {
        super("Method not found with signature " + tcMethod + "(" + (taParams == null ? "" : taParams.toString()) + ")", false);
    }
}

