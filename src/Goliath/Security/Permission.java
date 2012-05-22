/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Security;

import Goliath.Interfaces.Security.IPermission;

/**
 *
 * @author kenmchugh
 */
public class Permission extends Goliath.Object
        implements Goliath.Interfaces.Security.IPermission
{
    private Boolean m_lExecute;
    private Boolean m_lRead;
    private Boolean m_lWrite;
    private Boolean m_lCreate;
    private Boolean m_lDelete;
    private String m_cName;
    private String m_cDescription;

    public Permission(String tcName, AccessType toAccessType)
    {
        m_cName = tcName;
        if (toAccessType == AccessType.READ())
        {
            m_lRead = true;
        }
        else if (toAccessType == AccessType.WRITE())
        {
            m_lWrite = true;
        }
        else if (toAccessType == AccessType.EXECUTE())
        {
            m_lExecute = true;
        }
        else if (toAccessType == AccessType.CREATE())
        {
            m_lCreate = true;
        }
        else if (toAccessType == AccessType.DELETE())
        {
            m_lDelete = true;
        }
    }

    public Permission(String tcName, Boolean tlRead, Boolean tlWrite, Boolean tlExecute, Boolean tlCreate, Boolean tlDelete)
    {
        m_cName = tcName;
        m_lRead = tlRead;
        m_lWrite = tlWrite;
        m_lCreate = tlCreate;
        m_lDelete = tlDelete;
        m_lExecute = tlExecute;
    }

    public Permission(String tcName, String tcDescription, Boolean tlRead, Boolean tlWrite, Boolean tlExecute, Boolean tlCreate, Boolean tlDelete)
    {
        this(tcName, tlRead, tlWrite, tlExecute, tlCreate, tlDelete);
        m_cDescription = tcDescription;
    }

    @Override
    public Boolean canExecute()
    {
        return m_lExecute;
    }

    @Override
    public Boolean canRead()
    {
        return m_lRead;
    }

    @Override
    public Boolean canWrite()
    {
        return m_lWrite;
    }

    @Override
    public Boolean canCreate()
    {
        return m_lCreate;
    }

    @Override
    public Boolean canDelete()
    {
        return m_lDelete;
    }

    @Override
    public String getDescription()
    {
        return m_cDescription;
    }

    @Override
    public String getName()
    {
        return m_cName;
    }

    /**
     * Checks if this permission allows the specified access type
     * @param toAccess the access type to check
     * @return true if allowed, false if not allowed, null if not specified
     */
    @Override
    public Boolean checkAccess(AccessType toAccess)
    {
        if(toAccess == AccessType.EXECUTE())
        {
            return canExecute();
        }
        else if(toAccess == AccessType.READ())
        {
            return canRead();
        }
        else if (toAccess == AccessType.WRITE())
        {
            return canWrite();
        }
        return false;
    }

    /**
     * Merges two permissions
     * RWE - R-- = RWE
     * -WE - R-- = RWE
     * R~W~E - R-- = R~W~E
     * ~R~W~E - R-- = ~R~W~E
     * ~R~W~E - ~R-- = ~R~W~E
     *
     * @param toPermission The permission to merge
     */
    @Override
    public void mergePermission(IPermission toPermission)
    {
        m_lRead = canRead() == null ? toPermission.canRead() : mergeValue(toPermission.canRead(), m_lRead);
        m_lWrite = canWrite() == null ? toPermission.canWrite() : mergeValue(toPermission.canWrite(), m_lWrite);
        m_lExecute = canExecute() == null ? toPermission.canExecute() : mergeValue(toPermission.canExecute(), m_lExecute);
        m_lCreate = canCreate() == null ? toPermission.canCreate() : mergeValue(toPermission.canCreate(), m_lCreate);
        m_lDelete = canDelete() == null ? toPermission.canDelete() : mergeValue(toPermission.canDelete(), m_lDelete);
    }

    /**
     * Merges the two values:
     * Null and true = true
     * Null and false = false
     * Null and null = null
     * @param tlValue1
     * @param tlValue2
     * @return
     */
    private Boolean mergeValue(Boolean tlValue1, Boolean tlValue2)
    {
        // If either value is null, return the other value
        if (tlValue1 == null || tlValue2 == null)
        {
            return tlValue1 == null ? tlValue2 : tlValue1;
        }

        // If either value is false, then false is returned
        return !tlValue1 || !tlValue2;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Permission other = (Permission) obj;
        if ((this.m_cName == null) ? (other.m_cName != null) : !this.m_cName.equals(other.m_cName))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + (this.m_cName != null ? this.m_cName.hashCode() : 0);
        return hash;
    }

    
}
