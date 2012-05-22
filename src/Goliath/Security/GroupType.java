/* =========================================================
 * GroupType.java
 *
 * Author:      kenmchugh
 * Created:     Aug 20, 2010, 3:54:25 PM
 *
 * Description
 * --------------------------------------------------------
 * <Description>
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Security;

/**
 *
 * @author kenmchugh
 */
public class GroupType extends Goliath.DynamicEnum
{
    public GroupType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static PermissionType g_oEveryone;
    public static PermissionType EVERYONE()
    {
        if (g_oEveryone == null)
        {
            try
            {
                g_oEveryone = new PermissionType("2688b87e-2994-102c-9cb0-0c32d7a2a44e");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oEveryone;
    }

    private static PermissionType g_oAdmin;
    public static PermissionType ADMIN()
    {
        if (g_oAdmin == null)
        {
            try
            {
                g_oAdmin = new PermissionType("34a005d4-2994-102c-9cb0-0c32d7a2a44e");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oAdmin;
    }

    private static PermissionType g_oAnonymous;
    public static PermissionType ANONYMOUS()
    {
        if (g_oAnonymous == null)
        {
            try
            {
                g_oAnonymous = new PermissionType("3b30f381-2994-102c-9cb0-0c32d7a2a44e");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oAnonymous;
    }
}
