/* =========================================================
 * IStartup.java
 *
 * Author:      Ken McHugh
 * Created:     Feb 19, 2008, 7:20:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * Used to mark a class as a startup class.  Classes with this
 * interface will be loaded and executed when an application starts up
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces;

/**
 * Used to mark a class as a startup class.  Classes with this
 * interface will be loaded and executed when an application starts up,
 * providing the class has a public default constructor defined and is not an abstract class
 *
 * @see         Related Class
 * @version     1.0 Feb 19, 2008
 * @author      Ken McHugh
**/
public interface IStartup        
{
    boolean run();
    int getSequence();
    String getName();
    boolean isFirstRun();
}
