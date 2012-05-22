/* =========================================================
 * PermissionTypes.java
 *
 * Author:      kenmchugh
 * Created:     Aug 20, 2010, 3:53:48 PM
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
public class PermissionType extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of a StringFormatType Object
     *
     * @param tcValue The value for the string format type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public PermissionType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static PermissionType g_oCreateUser;
    public static PermissionType CREATEUSER()
    {
        if (g_oCreateUser == null)
        {
            try
            {
                g_oCreateUser = new PermissionType("CREATEUSER");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oCreateUser;
    }

    private static PermissionType g_oCreateSystemUser;
    public static PermissionType CREATESYSTEMUSER()
    {
        if (g_oCreateSystemUser == null)
        {
            try
            {
                g_oCreateSystemUser = new PermissionType("CREATESYSTEMUSER");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oCreateSystemUser;
    }

    private static PermissionType g_oCreateAnonymousUser;
    public static PermissionType CREATEANONYMOUSUSER()
    {
        if (g_oCreateAnonymousUser == null)
        {
            try
            {
                g_oCreateAnonymousUser = new PermissionType("CREATEANONYMOUSUSER");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oCreateAnonymousUser;
    }

    private static PermissionType g_oCreateGroup;
    public static PermissionType CREATEGROUP()
    {
        if (g_oCreateGroup == null)
        {
            try
            {
                g_oCreateGroup = new PermissionType("CREATEGROUP");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oCreateGroup;
    }

    private static PermissionType g_oLockUser;
    public static PermissionType LOCKUSER()
    {
        if (g_oLockUser == null)
        {
            try
            {
                g_oLockUser = new PermissionType("LOCKUSER");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oLockUser;
    }

    private static PermissionType g_oUnlockUser;
    public static PermissionType UNLOCKUSER()
    {
        if (g_oUnlockUser == null)
        {
            try
            {
                g_oUnlockUser = new PermissionType("UNLOCKUSER");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oUnlockUser;
    }

    private static PermissionType g_oDeleteUser;
    public static PermissionType DELETEUSER()
    {
        if (g_oDeleteUser == null)
        {
            try
            {
                g_oDeleteUser = new PermissionType("DELETEUSER");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDeleteUser;
    }

    private static PermissionType g_oDeleteGroup;
    public static PermissionType DELETEGROUP()
    {
        if (g_oDeleteGroup == null)
        {
            try
            {
                g_oDeleteGroup = new PermissionType("DELETEGROUP");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDeleteGroup;
    }

    private static PermissionType g_oChangePassword;
    public static PermissionType CHANGEPASSWORD()
    {
        if (g_oChangePassword == null)
        {
            try
            {
                g_oChangePassword = new PermissionType("CHANGEPASSWORD");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oChangePassword;
    }

    private static PermissionType g_oResetPassword;
    public static PermissionType RESETPASSWORD()
    {
        if (g_oResetPassword == null)
        {
            try
            {
                g_oResetPassword = new PermissionType("RESETPASSWORD");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oResetPassword;
    }
}
