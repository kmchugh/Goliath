/* =========================================================
 * SecurityEntity.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 12:54:04 AM
 *
 * Description
 * --------------------------------------------------------
 * A Security Entity is the base entity used for security and permissions
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Interfaces.Security.IPermission;
import Goliath.Interfaces.Security.ISecurityManager;
import Goliath.Security.AccessType;
import Goliath.Security.Group;
import Goliath.Security.Permission;
import Goliath.Security.PermissionType;

/**
 *
 * @author kenmchugh
 */
public abstract class SecurityEntity extends Goliath.Object
{
    private ISecurityManager m_oManager;
    private String m_cID;

    private List<Group> m_oFullMemberList;
    private List<IPermission> m_oFullPermissionList;


    /**
     * Creates a new instance of the security entity and forces it to use the specified SecurityManager
     * @param toManager the Security manager
     */
    public SecurityEntity(ISecurityManager toManager, String tcID)
    {
        m_oManager = toManager;
        m_cID = tcID;
    }

    /**
     * Gets the security manager for this entity
     * @return the security manager
     */
    protected final ISecurityManager getSecurityManager()
    {
        if (m_oManager == null)
        {
            m_oManager = Application.getInstance().getSecurityManager();
        }
        return m_oManager;
    }

    public final String getLookupID()
    {
        return m_cID;
    }


    /**
     * Gets the GUID for this Security Entity
     * @return the GUID
     */
    public final String getGUID()
    {
        return getSecurityManager().getGUID(this);
    }
    
    /**
     * Gets the ID for this security entity
     * @return the ID
     */
    public final long getID()
    {
        return getSecurityManager().getID(this);
    }

    /**
     * Gets the name of this security entity
     * @return the name
     */
    public final String getName()
    {
        return getSecurityManager().getName(this);
    }
    
    /**
     * Sets the new name of the security entity
     * @param tcName the new name
     */
    public final void setName(String tcName)
    {
        getSecurityManager().setName(this, tcName);
    }
    
    /**
     * Gets the list of groups that this entity is a direct member of
     * @return the list of groups, or an empty list if this entity is not a member of any groups
     */
    public final List<Group> getDirectMemberOfList()
    {
        return getSecurityManager().getMembershipList();
    }
    
    /**
     * Gets the full list of groups that this entity is a member of
     * @return the list of groups, or an empty list if this entity is not a member of any groups
     */
    public final List<Group> getMemberOfList()
    {
        if (m_oFullMemberList == null)
        {
            m_oFullMemberList = new List<Group>();
            for(Group loGroup : getDirectMemberOfList())
            {
                if (!m_oFullMemberList.contains(loGroup))
                {
                    m_oFullMemberList.add(loGroup);
                    for (Group loSubGroup : loGroup.getMemberOfList())
                    {
                        if (!m_oFullMemberList.contains(loSubGroup))
                        {
                            m_oFullMemberList.add(loSubGroup);
                        }
                    }
                }
            }
        }
        return m_oFullMemberList;
    }

    /**
     * Gets the list of permissions that are directly attached to this entity
     * @return the list of permissions for this entity
     */
    protected final List<IPermission> getPermissionList()
    {
        return getSecurityManager().getPermissionList();
    }

    /**
     * Gets the full list of permissions that are currently applicable to this entity
     * @return the full list of permissions
     */
    protected final List<IPermission> getFullPermissionList()
    {
        if (m_oFullPermissionList == null)
        {
            m_oFullPermissionList = new List<IPermission>();
            for(Group loGroup : getMemberOfList())
            {
                for (IPermission loPermission : loGroup.getPermissionList())
                {
                    int lnIndex = m_oFullPermissionList.indexOf(loPermission);
                    if (lnIndex >=0)
                    {
                        m_oFullPermissionList.get(lnIndex).mergePermission(loPermission);
                    }
                    else
                    {
                        m_oFullPermissionList.add(loPermission);
                    }
                }
            }
        }
        return m_oFullPermissionList;
    }

    /**
     * Clears the membership list for this entity, this is used as part of the caching mechanism
     */
    protected final void clearMembershipList()
    {
        m_oFullMemberList = null;
    }

    /**
     * Clears the full permission list for this entity, this is used as part of the caching mechanism
     */
    protected final void clearFullPermissionList()
    {
        m_oFullPermissionList = null;
    }

    /**
     * Adds this entity as a member of the specifed entity
     * @param toGroup the group to add this entity as a member of
     * @return true if this was added successfully, false if it was not
     */
    public boolean addMembership(Group toGroup)
    {
        // Because we have added this entity as a member of another, we know that the permissions and members may have changed
        if (getSecurityManager().addAsMember(this, toGroup))
        {
            clearMembershipList();
            clearFullPermissionList();
            return true;
        }
        return false;
    }

    /**
     * Checks if this entity is a member of the specified group
     * @param toGroup the group to check membership of
     * @return true if this entity is a member of the specified group
     */
    public final boolean isMemberOf(Group toGroup)
    {
        return getMemberOfList().contains(toGroup);
    }

    /**
     * Checks if this entity has the current permission
     * @param toPermission the permission to check
     * @param toAccessType the access type to check for
     * @return true if allowed, otherwise false
     */
    public final boolean hasPermission(IPermission toPermission, AccessType toAccessType)
    {
        /* In general this is not a good way of coding things, however we want to protect the system user to ensure
         * developers can not create a similar type of user, because of that, we have stated this method is final
         * and have hard coded the system user
         */
        if (Goliath.DynamicCode.Java.isEqualOrAssignable(Goliath.SystemUser.class, this.getClass()))
        {
            return true;
        }

        return true;
        /*

        int lnIndex = getFullPermissionList().indexOf(toPermission);
        // If the permission has not been specified, then we are going to deny based on the rule of restrictive security
        if (lnIndex >=0)
        {
            IPermission loPermission = getFullPermissionList().get(lnIndex);
            Boolean llResult = loPermission.checkAccess(toAccessType);
            return llResult == null ? false : llResult;
        }
        return false;
         *
         */
    }

    /**
     * Checks if this entity has the current permission
     * @param toPermission the permission to check for
     * @param toAccess the access type to check for
     * @return true if allowed, false otherwise
     */
    public final boolean hasPermission(PermissionType toPermission, AccessType toAccess)
    {
        Permission loPermission = new Permission(toPermission.getValue(), toAccess);
        return hasPermission(loPermission, toAccess);
    }

    /**
     * Adds the specified permission to the current entity
     * @param toPermission the permission to add
     * @return true if the permission was added, false otherwise
     */
    public boolean addPermission(IPermission toPermission)
    {
        return getSecurityManager().addPermission(this, toPermission);
    }

    /**
     * Gets the expiry date for this entity
     * @return
     */
    public Date getExpiry()
    {
        return getSecurityManager().getExpiry(this);
    }

    /**
     * Sets the expiry for this entity
     * @param toExpires the new expiry date
     */
    public void setExpires(Date toExpires)
    {
        getSecurityManager().setExpiry(this, toExpires);
    }

    /**
     * Checks if this entity is expired
     * @return true if expired, false otherwise
     */
    public boolean isExpired()
    {
        return getSecurityManager().isExpired(this);
    }

    @Override
    public boolean equals(java.lang.Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SecurityEntity other = (SecurityEntity) obj;
        if ((this.m_cID == null) ? (other.m_cID != null) : !this.m_cID.equals(other.m_cID))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + (this.m_cID != null ? this.m_cID.hashCode() : 0);
        return hash;
    }

    

}
