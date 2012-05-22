/* =========================================================
 * IDelegate.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 10:35:12 PM
 * 
 * Description
 * --------------------------------------------------------
 * Proxy for a method.  Used to pass methods as objects
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces;

/**
 * Used to pass methods as objects (Callbacks)
 *
 * @param T     The type that is returned when the delegate is invoked
 * @version     1.0 Jan 9, 2008
 * @author      Ken McHugh
**/
public interface IDelegate<T>
{
     /**
     * Executes the delegate with the arguments specified
     *
     * @param  taArgs   The arguments for the command
      * @return the value from the delegate
      * @throws java.lang.Throwable 
     */
    T invoke(java.lang.Object... taArgs)
            throws Throwable;
}
