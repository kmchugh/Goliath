/* =========================================================
 * DefaultLotIOBehaviour.java
 *
 * Author:      Peter
 * Created:     14-Dec-2007, 15:27:19
 * 
 * Description
 * --------------------------------------------------------
 * General Class Description.
 * This is the strategy abstract class for logging behaviours
 * 
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.LogHandlers;

import Goliath.Interfaces.ISession;
import Goliath.Interfaces.LogHandlers.*;
import Goliath.Session;
import java.util.Date;

/**
 * Default log IO behaviour abstract class.
 * This is the strategy abstract class for logging behaviours
 * 
 * @version     1.0 14-Dec-2007
 * @author      Peter
 **/
public abstract class DefaultLogIOBehaviour implements ILogBehaviour
{

    /**
     * Write an warning message to log
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public abstract void logWarning(Date tdDate, String tcMessage);
    
     /**
     * Write an error message to log
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public abstract void logError(Date tdDate, String tcMessage);
    
      /**
     * Write an event message to log
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public abstract void logEvent(Date tdDate, String tcMessage);

}

