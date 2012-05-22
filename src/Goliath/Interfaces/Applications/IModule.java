/* =========================================================
 * IModule.java
 *
 * Author:      kmchugh
 * Created:     09-Apr-2008, 20:38:14
 * 
 * Description
 * --------------------------------------------------------
 * General Interface Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.Applications;

import Goliath.Interfaces.IStartup;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 09-Apr-2008
 * @author      kmchugh
**/
public interface IModule extends IStartup
{
    String getID();
    String getDescription();
    String getImageSource();
    String getDefaultContext();
}
