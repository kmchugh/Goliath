/* =========================================================
 * LogType.java
 *
 * Author:      kmchugh
 * Created:     10 December 2007, 22:39
 *
 * Description
 * --------------------------------------------------------
 * This class is used to define the error type, allows for
 * filtering of log levels
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Constants;

/**
 * This class is used to define the error type, allows for
 * filtering of log levels
 *
 * @version     1.0 10 December 2007
 * @author      kmchugh
 **/
public class LogType extends Goliath.DynamicEnum
{

    // TODO: Implement a compare on the level.
    private int m_nLevel = 0;
    /**
     * Creates a new instance of an LogType Object 
     * 
     * 
     * @param tcValue   The value of the LogType
     * @param tnLevel The log level value of this log type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public LogType(String tcValue, int tnLevel) 
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
        m_nLevel = tnLevel;
    }
    
    /**
     * Gets the value of this log level
     * @return the log level value
     */
    public int getLevel()
    {
        return m_nLevel;
    }
    
    private static LogType g_oFatal;
    private static LogType g_oError;
    private static LogType g_oWarning;
    private static LogType g_oEvent;
    private static LogType g_oTrace;
    private static LogType g_oDebug;
    
    /**
     * Static singleton for FATAL logging
     * @return The FATAL LogType
     */
    public static LogType FATAL()
    {
        if (g_oFatal == null)
        {
            try
            {
                g_oFatal = new LogType("FATAL", 0);
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oFatal;
    }
    
    /**
     *  Static singleton for ERROR logging
     * @return Returns the ERROR LogType
     */
    public static LogType ERROR()
    {
        if (g_oError == null)
        {
            try
            {
                g_oError = new LogType("ERROR", 10);
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oError;
    }
    
    /**
     * Static singleton for WARNING logging
     * @return The WARNING LogType
     */
    public static LogType WARNING()
    {
        if (g_oWarning == null)
        {
            try
            {
                g_oWarning = new LogType("WARNING", 20);
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oWarning;
    }
    
     /**
     *  Static singleton for EVENT logging
     * @return The EVENT LogType
     */
    public static LogType EVENT()
    {
        if (g_oEvent == null)
        {
            try
            {
                g_oEvent = new LogType("EVENT", 30);
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oEvent;
    }
    
    /**
     * Static singleton for TRACE logging
     * @return The TRACE LogType
     */
    public static LogType TRACE()
    {
        if (g_oTrace == null)
        {
            try
            {
                g_oTrace = new LogType("TRACE", 40);
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oTrace;
    }
    
     
    
    /**
     * Static singleton for DEBUG logging
     * @return The DEBUG LogType
     */
    public static LogType DEBUG()
    {
        if (g_oDebug == null)
        {
            try
            {
                g_oDebug = new LogType("DEBUG", 50);
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDebug;
    }
}
