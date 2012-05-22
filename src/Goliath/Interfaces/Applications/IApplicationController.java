/* ========================================================
 * IApplicationController.java
 *
 * Author:      kmchugh
 * Created:     Nov 2, 2010, 9:33:21 PM
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

package Goliath.Interfaces.Applications;

import Goliath.Constants.EventType;
import Goliath.Event;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.IEventDispatcher;



/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Nov 2, 2010
 * @author      kmchugh
**/
public interface IApplicationController<T extends EventType, E extends IApplicationController>
        extends IEventDispatcher<T, Event<E>>
{
    void requestAuthentication();

    boolean authenticate(String tcUserName, String tcPassword, IDelegate<E> toFailed, IDelegate<E> toSuccess);
    boolean unauthenticate();
    boolean getLicenceFromServer();

}
