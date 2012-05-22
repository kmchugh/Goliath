/* ========================================================
 * CommandEventType.java
 *
 * Author:      kmchugh
 * Created:     Aug 4, 2010, 6:16:40 PM
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

package Goliath.Commands;

import Goliath.Constants.EventType;
        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 4, 2010
 * @author      kmchugh
**/
public class CommandEventType extends EventType
{
    public CommandEventType(String tcValue)
    {
        super(tcValue);
    }

    private static CommandEventType g_oProgressChanged;
    public static CommandEventType ONPROGRESSCHANGED()
    {
        if (g_oProgressChanged == null)
        {
            g_oProgressChanged = new CommandEventType("ONPROGRESSCHANGED");
        }
        return g_oProgressChanged;
    }

    private static CommandEventType g_oCancelled;
    public static CommandEventType ONCANCELLED()
    {
        if (g_oCancelled == null)
        {
            g_oCancelled = new CommandEventType("ONCANCELLED");
        }
        return g_oCancelled;
    }

    private static CommandEventType g_oComplete;
    public static CommandEventType ONCOMPLETE()
    {
        if (g_oComplete == null)
        {
            g_oComplete = new CommandEventType("ONCOMPLETE");
        }
        return g_oComplete;
    }

    private static CommandEventType g_oStarted;
    public static CommandEventType ONSTARTED()
    {
        if (g_oStarted == null)
        {
            g_oStarted = new CommandEventType("ONSTARTED");
        }
        return g_oStarted;
    }

    private static CommandEventType g_oReset;
    public static CommandEventType ONRESET()
    {
        if (g_oReset == null)
        {
            g_oReset = new CommandEventType("ONRESET");
        }
        return g_oReset;
    }

    private static CommandEventType g_oSessionChanged;
    public static CommandEventType ONSESSIONCHANGED()
    {
        if (g_oSessionChanged == null)
        {
            g_oSessionChanged = new CommandEventType("ONSESSIONCHANGED");
        }
        return g_oSessionChanged;
    }
}
