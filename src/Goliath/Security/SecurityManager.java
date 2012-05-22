/* =========================================================
 * SecurityManager.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 12:54:04 AM
 *
 * Description
 * --------------------------------------------------------
 * This Manager is used to map between the Goliath internal Security and
 * the external security
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Security;

import Goliath.SecurityEntity;
import Goliath.Collections.HashTable;
import Goliath.Exceptions.PermissionDeniedException;
import Goliath.Interfaces.ISession;
import Goliath.Interfaces.Security.ISecurityManager;
import Goliath.Session;

/**
 *
 * @author kenmchugh
 */
public abstract class SecurityManager extends Goliath.Object
        implements ISecurityManager
{
    private HashTable<String, User> m_oUserEntityCache;
    private HashTable<String, Group> m_oGroupEntityCache;

    private HashTable<SecurityEntity, Object> m_oImplementationCache;
    private HashTable<Group, Object> m_oGroupCache;

    private User getCachedUserEntityByName(String tcName)
    {
        return m_oUserEntityCache != null ? m_oUserEntityCache.get(tcName) : null;
    }

    private void setCachedUserEntityByName(User toUser)
    {
        if (m_oUserEntityCache == null)
        {
            m_oUserEntityCache = new HashTable<String, User>();
        }
        m_oUserEntityCache.put(toUser.getName(), toUser);
    }

    @Override
    public final User getUser(String tcUserName)
    {
        // First check the cache
        User loReturn = getCachedUserEntityByName(tcUserName);
        if (loReturn == null)
        {
            loReturn = onGetUser(tcUserName);

            // Cache the user
            if (loReturn != null)
            {
                setCachedUserEntityByName(loReturn);
            }
        }
        return loReturn;
    }
    protected abstract User onGetUser(String tcName);
    
    @Override
    public final User createUser(String tcUserName, String tcEmail, String tcPassword, String tcDisplayName, String tcDescription)
    {
        return createUser(tcUserName, tcEmail, tcPassword, tcDisplayName, tcDescription, false);
    }

    @Override
    public final User createUser(String tcUserName, String tcEmail, String tcPassword, String tcDisplayName, String tcDescription, boolean tlUseSystem)
    {
        checkPermission(PermissionType.CREATEUSER());
        return onCreateUser(tcUserName, tcEmail, tcPassword, tcDisplayName, tcDescription, tlUseSystem);
    }

    @Override
    public final User createSystemUser(String tcUserName, String tcEmail, String tcPassword)
    {
        checkPermission(PermissionType.CREATESYSTEMUSER());
        User loReturn = onCreateSystemUser(tcUserName, tcEmail, tcPassword);
        return loReturn;
    }

    @Override
    public final User createAnonymousUser()
    {
        checkPermission(PermissionType.CREATEANONYMOUSUSER());
        return onCreateAnonymousUser();
    }

    @Override
    public final Group createGroup(String tcGUID)
    {
        checkPermission(PermissionType.CREATEGROUP());
        Group loReturn = onCreateGroup(tcGUID);
        return loReturn;
    }

    @Override
    public final void checkPermission(PermissionType toType)
            throws PermissionDeniedException
    {


    }

    public boolean isGroup(SecurityEntity toEntity)
    {
        return Goliath.DynamicCode.Java.isEqualOrAssignable(Group.class, toEntity.getClass());
    }

    protected abstract User onCreateSystemUser(String tcUserName, String tcEmail, String tcPassword);
    protected abstract User onCreateUser(String tcUserName, String tcEmail, String tcPassword, String tcDisplayName, String tcDescription, boolean tlUseSystem);
    protected abstract User onCreateAnonymousUser();
    protected abstract Group onCreateGroup(String tcGUID);

    @Override
    public final void setLocked(User toUser, boolean tlLocked)
    {
        checkPermission(tlLocked ? PermissionType.LOCKUSER() : PermissionType.UNLOCKUSER());
        boolean llLocked = getLocked(toUser);
        if (llLocked != tlLocked)
        {
            onSetLocked(toUser, tlLocked);
        }
    }

    protected abstract void onSetLocked(User toUser, boolean tlLocked);

    @Override
    public final boolean deleteUser(User toUser)
    {
        checkPermission(PermissionType.DELETEUSER());
        return onDeleteUser(toUser);
    }

    @Override
    public final boolean deleteGroup(Group toGroup)
    {
        checkPermission(PermissionType.DELETEGROUP());
        return onDeleteGroup(toGroup);
    }

    protected abstract boolean onDeleteUser(User toUser);
    protected abstract boolean onDeleteGroup(Group toGroup);

    @Override
    public final boolean changePassword(User toUser, String tcOldPassword, String tcNewPassword)
    {
        checkPermission(PermissionType.CHANGEPASSWORD());
        boolean llReturn = false;
        boolean llAuthenticated = toUser.isAuthenticated();
        // Unauthenticate the user in order to reauthenticate with the old password to ensure it is correct
        if (llAuthenticated)
        {
            toUser.unauthenticate();
        }

        if (toUser.authenticate(tcOldPassword))
        {
            llReturn = resetPassword(toUser, tcNewPassword);
        }
        // Unauthenticate with the old password, and authenticate with the new
        toUser.unauthenticate();
        if (llAuthenticated)
        {
            toUser.authenticate(tcNewPassword);
        }
        return llReturn;
    }
    
    @Override
    public final boolean changeOwnPassword(String tcOldPassword, String tcNewPassword)
    {
        ISession loSession = Session.getCurrentSession();
        User loUser = loSession.getUser();
        boolean llReturn = false;
        if (!getLocked(loUser) && loUser.authenticate(tcOldPassword))
        {
            boolean llAuthenticated = loUser.isAuthenticated();

            llReturn = resetOwnPassword(tcNewPassword);

            if (llReturn && llAuthenticated)
            {
                // Unauthenticate with the old password, and authenticate with the new
                loUser.unauthenticate();
                loUser.authenticate(tcNewPassword);
            }
        }
        return llReturn;
    }

    @Override
    public final boolean resetPassword(User toUser, String tcNewPassword)
    {
        checkPermission(PermissionType.RESETPASSWORD());
        return onResetPassword(toUser, tcNewPassword);
    }

    @Override
    public final boolean resetOwnPassword(String tcNewPassword)
    {
        ISession loSession = Session.getCurrentSession();
        User loUser = loSession.getUser();
        return onResetPassword(loUser, tcNewPassword);
    }

    protected abstract boolean onResetPassword(User toUser, String tcNewPassword);

    public String encryptPasswordString(User toUser, String tcPassword)
    {
        // Add salt to the password
        tcPassword = tcPassword + toUser.getGUID().substring(4);

        String lcString = Goliath.Utilities.encryptMD5(tcPassword);

        // Encode to base 64
        lcString = Goliath.Utilities.encodeBase64(lcString);

        return lcString;
    }
    
}
