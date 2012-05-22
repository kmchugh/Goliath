/* =========================================================
 * SessionEventType.java
 *
 * Author:      kmchugh
 * Created:     02-Jun-2008, 08:45:41
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
 * =======================================================*/

package Goliath.Constants;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 02-Jun-2008
 * @author      kmchugh
**/
public class SessionEventType extends EventType
{
    /**
     * Creates a new instance of an EventTypes Object
     *
     * @param tcValue The value for the event type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public SessionEventType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static SessionEventType g_oExpired;
    public static SessionEventType ONEXPIRED()
    {
        if (g_oExpired == null)
        {
            try
            {
                g_oExpired = new SessionEventType("ONEXPIRED");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oExpired;
    }

    private static SessionEventType g_oAuthenticated;
    public static SessionEventType ONAUTHENTICATED()
    {
        if (g_oAuthenticated == null)
        {
            try
            {
                g_oAuthenticated = new SessionEventType("ONAUTHENTICATED");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oAuthenticated;
    }

    private static SessionEventType g_oUnauthenticated;
    public static SessionEventType ONUNAUTHENTICATED()
    {
        if (g_oUnauthenticated == null)
        {
            try
            {
                g_oUnauthenticated = new SessionEventType("ONUNAUTHENTICATED");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oUnauthenticated;
    }
}
