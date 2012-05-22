/* =========================================================
 * Environment.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 10:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This class provided easy access to environment variables.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

import Goliath.Constants.OperatingSystemName;
import java.nio.charset.Charset;

/**
 * This class provides easy access to environment variables
 * For example:
 * <pre>
 *      String lcString = "this is a new line" + Goliath.Environment.NEWLINE;
 * </pre>
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class Environment
{
    private static OperatingSystemName g_oOS;

    /**
     * returns the new line characters for this environment.
     * @return The newline character
     */
    public static String NEWLINE()
    {
        return System.getProperty("line.separator");
    }
    
    /**
     * returns the file separator characters for this environment.
     * @return The file separator character
     */
    public static String FILESEPARATOR()
    {
        return System.getProperty("file.separator");
    }

    public static Charset ENCODING()
    {
        return Charset.defaultCharset();
    }

    public static OperatingSystemName OS()
    {
        if (g_oOS == null)
        {
            String lcSystemName = System.getProperty("os.name").toLowerCase();

            if (lcSystemName.contains("windows"))
            {
                g_oOS = OperatingSystemName.WINDOWS();
            }
            else if (lcSystemName.contains("mac"))
            {
                g_oOS = OperatingSystemName.MAC();
            }
            else if (lcSystemName.contains("linux"))
            {
                g_oOS = OperatingSystemName.LINUX();
            }
            else if (lcSystemName.contains("solaris"))
            {
                g_oOS = OperatingSystemName.SOLARIS();
            }
            else
            {
                g_oOS = OperatingSystemName.UNKNOWN();
            }
        }
        return g_oOS;
    }
    
    public static int getAvailableProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
    }
    
    public static long getAvailableMemory()
    {
        return Runtime.getRuntime().totalMemory();
    }
    
    public static long getFreeMemory()
    {
        return Runtime.getRuntime().freeMemory();
    }

    /** Not Creatable */
    private Environment()
    {
    }
    
}
