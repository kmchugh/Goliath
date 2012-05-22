/* =========================================================
 * ILogHandler.java
 *
 * Author:      kmchugh
 * Created:     27 November 2007, 15:14
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

import Goliath.Constants.LogType;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 27 November 2007
 * @author      kmchugh
 **/
public interface ILogHandler
{
    /**
    * Write an entry to the log
    *
    * @param  tcMessage Message being logged
    * @param toLogType The log type to log to the logger.
    */
    public void log(String tcMessage, LogType toLogType);
    
    /**
     * Add behaviour to the behaviour collection
     *
     * @param  toBehaviour Behaviour to be added
     * @param  tcKey Key to hash table entry for behaviour being added
     */
    public void addBehaviour(String tcKey, ILogBehaviour toBehaviour);
    
    /**
     * Remove behaviour from the behaviour collection
     *
     * @param  tcKey Key to hash table entry for behaviour being removed
     */
    public void removeBehaviour(String tcKey);
    
    /**
     * Remove all behaviours associated with this log handler, closing them first
     */
    public void removeBehaviours();

}
