/* =========================================================
 * Events.java
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
public class EventType extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of an EventTypes Object 
     *
     * @param tcValue The value for the event type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public EventType(String tcValue) 
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }
    
    private static EventType g_oChanged;
    /**
     * Used to indicate that the value of the target has changed
     * @return the changed event
     */
    public static EventType ONCHANGED()
    {
        if (g_oChanged == null)
        {
            try
            {
                g_oChanged = createEnumeration(EventType.class, "ONCHANGED");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oChanged;
    }

    private static EventType g_oStateChanged;
    /**
     * Used to indicate that the state of the target has changed
     * @return the state changed event
     */
    public static EventType ONSTATECHANGED()
    {
        if (g_oStateChanged == null)
        {
            try
            {
                g_oStateChanged = createEnumeration(EventType.class, "ONSTATECHANGED");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oStateChanged;
    }
}
