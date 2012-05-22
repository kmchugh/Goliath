/* =========================================================
 * CacheType.java
 *
 * Author:      kmchugh
 * Created:     17-Jun-2008, 16:00:04
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
 * @version     1.0 17-Jun-2008
 * @author      kmchugh
**/
public class CacheType extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of a StringFormatType Object 
     *
     * @param tcValue The value for the string format type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public CacheType(String tcValue) 
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }
    
    private static CacheType g_oNone;
    /**
     *  Static singleton for DEFAULT formatting
     * @return The DEFAULT string format type
     */
    public static CacheType NONE()
    {
        if (g_oNone == null)
        {
            try
            {
                g_oNone = new CacheType("NONE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oNone;
    }
    
    private static CacheType g_oApplication;
    /**
     *  Static singleton for DEFAULT formatting
     * @return The DEFAULT string format type
     */
    public static CacheType APPLICATION()
    {
        if (g_oApplication == null)
        {
            try
            {
                g_oApplication = new CacheType("APPLICATION");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oApplication;
    }
    
    private static CacheType g_oSession;
    /**
     *  Static singleton for DEFAULT formatting
     * @return The DEFAULT string format type
     */
    public static CacheType SESSION()
    {
        if (g_oSession == null)
        {
            try
            {
                g_oSession = new CacheType("SESSION");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oSession;
    }
    
}

