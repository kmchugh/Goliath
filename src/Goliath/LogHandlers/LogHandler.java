/* =========================================================
 * ConnectionString.java
 *
 * Author:      pstanbridge
 * Created:     12-Dec-2007, 15:29:24
 * 
 * Description
 * --------------------------------------------------------
 * This class handles the processing of log requests and
 * passes appopriate behaviours to the log behaviour interface
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.LogHandlers;

import Goliath.Applications.Application;
import Goliath.Constants.LogType;
import java.util.Date;

import Goliath.Interfaces.LogHandlers.*;

/**
 * This class handles the processing of log requests and
 * passes appopriate behaviours to the log behaviour interfaerface
 * For example:
 * <pre>
 *     
 * </pre>
 *
 *          
 * @version     1.0 Nov 13, 2007
 * @author      Peter Stanbridge
 **/
public abstract class LogHandler implements ILogHandler {
    
    // Storage for active behaviours
    private Goliath.Collections.HashTable<String, ILogBehaviour> m_aBehaviours = new Goliath.Collections.HashTable<String, ILogBehaviour>();


    public LogHandler() {

    }
    
    /**
     * Checks if it is possible to log this error due to log levels
     * @param toLogType The log type to check against
     * @return 
     */
    private boolean canLog(LogType toLogType)
    {
        // If there are no behaviours then you can not log
        return m_aBehaviours.size() > 0 && Application.getInstance().getLogLevel().getLevel() >= toLogType.getLevel();
    }
    
    /**
     * Write an error log entry to log
     *
     * @param  tcMessage Message being logged
     */
    @Override
    public void log(String tcMessage, LogType toLogType)
    {
        // TODO: Log using a state pattern to increase throughput
        // TODO: Possibly need to implement a behaviour by log type rather than global
        try
        {
            // Check if you can log this type of error
            if (!canLog(toLogType))
            {
                return;
            }
            
            synchronized(this)
            {
                if (toLogType == LogType.FATAL())
                {
                    logFatal(tcMessage);
                }
                else if (toLogType == LogType.ERROR())
                {
                    logError(tcMessage);
                }
                else if (toLogType == LogType.WARNING())
                {
                    logWarning(tcMessage);
                }
                else if (toLogType == LogType.EVENT())
                {
                    logEvent(tcMessage);
                }
                else if (toLogType == LogType.TRACE())
                {
                    logTrace(tcMessage);
                }
                else if (toLogType == LogType.DEBUG())
                {
                    logDebug(tcMessage);
                }
            }
        }
        catch (Throwable ignore)
        {}
    }
    
    
    /**
     * Write a fatal log entry to log
     *
     * @param  tcMessage Message being logged
     */
    private void logFatal(String tcMessage)
    {
        for(ILogBehaviour m_aBehaviour : m_aBehaviours.values()) 
        {
            m_aBehaviour.logFatal(new Date(), tcMessage);
        }
    }

    /**
     * Write an error log entry to log
     *
     * @param  tcMessage Message being logged
     */
    private void logError(String tcMessage)
    {
        for(ILogBehaviour m_aBehaviour : m_aBehaviours.values()) 
        {
            m_aBehaviour.logError(new Date(), tcMessage);
        }
    }
    
    /**
     * Write a warning log entry to log
     *
     * @param  tcMessage Message being logged
     */
    private void logWarning(String tcMessage)
    {
        for(ILogBehaviour m_aBehaviour : m_aBehaviours.values()) 
        {
            m_aBehaviour.logWarning(new Date(), tcMessage);
        }
    }
    
   
    /**
     * Write an event log entry to log
     *
     * @param  tcMessage Message being logged
     */
    private void logEvent(String tcMessage)
    {
        for(ILogBehaviour m_aBehaviour : m_aBehaviours.values()) 
        {
            m_aBehaviour.logEvent(new Date(), tcMessage);
        }
    }

    /**
     * Write a trace log entry to log
     *
     * @param  tcMessage Message being logged
     */
    private void logTrace(String tcMessage)
    {
        for(ILogBehaviour m_aBehaviour : m_aBehaviours.values()) 
        {
            m_aBehaviour.logTrace(new Date(), tcMessage);
        }
    }

    /**
     * Write an error log entry to log
     *
     * @param  tcMessage Message being logged
     */
    private void logDebug(String tcMessage)
    {
        for(ILogBehaviour m_aBehaviour : m_aBehaviours.values()) 
        {
            m_aBehaviour.logDebug(new Date(), tcMessage);
        }
    }
    
       /**
     * Add behaviour to the behaviour collection
     *
     * @param  toBehaviour Behaviour to be added
     * @param  tcKey Key to hash table entry for behaviour being added
     */
    @Override
    public void addBehaviour(String tcKey, ILogBehaviour toBehaviour) {
        if (tcKey != null & toBehaviour != null) {
        m_aBehaviours.put(tcKey, toBehaviour);
        }
    }
    
      /**
     * Remove behaviour from the behaviour collection
     *
     * @param  tcKey Key to hash table entry for behaviour being removed
     */
    @Override
    public void removeBehaviour(String tcKey) {
        if (tcKey != null) {
           m_aBehaviours.remove(tcKey); 
        }
    }
    
    /**
     * Remove all behaviours associated with this log handler, closing them first
     */
    @Override
    public void removeBehaviours() {
      m_aBehaviours.clear();
    }    
      

    

}
