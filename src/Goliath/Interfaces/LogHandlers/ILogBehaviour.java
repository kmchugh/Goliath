/* =========================================================
 * ILogIOBehaviour.java
 *
 * Author:      kmchugh
 * Created:     27 November 2007, 15:13
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

package Goliath.Interfaces.LogHandlers;
import java.util.Date;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 27 November 2007
 * @author      Peter
 **/
public interface ILogBehaviour
{
    /**
    * Log a fatal message
    * @param  tdDate - Date and time of log
    * @param  tcMessage - Message being logged
    */
    public void logFatal(Date tdDate, String tcMessage);
    
    /**
    * Log an error message
    * @param  tdDate - Date and time of log
    * @param  tcMessage - Message being logged
    */
    public void logError(Date tdDate, String tcMessage);
    
    /**
    * Log a warning message
    * @param  tdDate - Date and time of log
    * @param  tcMessage - Message being logged
    */
    public void logWarning(Date tdDate, String tcMessage);
    
    /**
    * Log an event message
    * @param  tdDate - Date and time of log
    * @param  tcMessage - Message being logged
    */
    public void logEvent(Date tdDate, String tcMessage);
    
    /**
    * Log a trace message
    * @param  tdDate - Date and time of log
    * @param  tcMessage - Message being logged
    */
    public void logTrace(Date tdDate, String tcMessage);
    
    /**
    * Log a debug message
    * @param  tdDate - Date and time of log
    * @param  tcMessage - Message being logged
    */
    public void logDebug(Date tdDate, String tcMessage);
}
