/* =========================================================
 * AccessTypes.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 2:13:02 AM
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

package Goliath.Security;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jan 9, 2008
 * @author      Ken McHugh
**/
public class AccessType extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of an AccessType Object 
     *
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public AccessType(String tcValue) 
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }
    
    private static AccessType g_oRead;
    /**
     *  Static singleton for READ access 
     * formatting
     */
    public static AccessType READ()
    {
        if (g_oRead == null)
        {
            try
            {
                g_oRead = new AccessType("READ");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oRead;
    }
    
    private static AccessType g_oWrite;
    /**
     *  Static singleton for Write access 
     * formatting
     * @return 
     */
    public static AccessType WRITE()
    {
        if (g_oWrite == null)
        {
            try
            {
                g_oWrite = new AccessType("WRITE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oWrite;
    }

    private static AccessType g_oCreate;
    /**
     *  Static singleton for Create access
     * formatting
     * @return
     */
    public static AccessType CREATE()
    {
        if (g_oCreate == null)
        {
            try
            {
                g_oCreate = new AccessType("CREATE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oCreate;
    }

    private static AccessType g_oDelete;
    /**
     *  Static singleton for Delete access
     * formatting
     * @return
     */
    public static AccessType DELETE()
    {
        if (g_oDelete == null)
        {
            try
            {
                g_oDelete = new AccessType("DELETE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDelete;
    }

    private static AccessType g_oExecute;
    /**
     *  Static singleton for Write access
     * formatting
     */
    public static AccessType EXECUTE()
    {
        if (g_oExecute == null)
        {
            try
            {
                g_oExecute = new AccessType("EXECUTE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oExecute;
    }
}
