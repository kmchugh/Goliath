/* =========================================================
 * OperatingSystemName.java
 *
 * Author:      kenmchugh
 * Created:     Oct 29, 2010, 10:17:52 AM
 *
 * Description
 * --------------------------------------------------------
 * Represents a static enumeration of operating systems
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Constants;

/**
 * Represents a static enumeration of operating systems
 * @author kenmchugh
 */
public class OperatingSystemName extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of an OperatingSystemName Object
     *
     *
     * @param tcValue   The value of the OperatingSystemName
     * @param tnLevel The log level value of this log type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public OperatingSystemName(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static OperatingSystemName g_oWindows;
    private static OperatingSystemName g_oMac;
    private static OperatingSystemName g_oLinux;
    private static OperatingSystemName g_oSolaris;
    private static OperatingSystemName g_oUnknown;


    
    public static OperatingSystemName WINDOWS()
    {
        if (g_oWindows == null)
        {
            try
            {
                g_oWindows = new OperatingSystemName("WINDOWS");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oWindows;
    }

    public static OperatingSystemName MAC()
    {
        if (g_oMac == null)
        {
            try
            {
                g_oMac = new OperatingSystemName("MAC");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oMac;
    }

    public static OperatingSystemName LINUX()
    {
        if (g_oLinux == null)
        {
            try
            {
                g_oLinux = new OperatingSystemName("LINUX");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oLinux;
    }

    public static OperatingSystemName SOLARIS()
    {
        if (g_oSolaris == null)
        {
            try
            {
                g_oSolaris = new OperatingSystemName("SOLARIS");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oSolaris;
    }

    public static OperatingSystemName UNKNOWN()
    {
        if (g_oUnknown == null)
        {
            try
            {
                g_oUnknown = new OperatingSystemName("UNKNOWN");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oUnknown;
    }

    
}
