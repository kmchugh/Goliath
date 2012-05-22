/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Security;

import Goliath.Constants.StringFormatType;
import Goliath.Interfaces.Security.IResourcePermission;

/**
 *
 * @author kenmchugh
 */
public class ResourcePermission extends Permission
        implements IResourcePermission
{
    private String m_cResourceName;
    private String m_cResourceGUID;
    private Goliath.Security.ResourceType m_oResourceType;
    private long m_nResourceID = 0;

    public ResourcePermission(String tcResourceName, String tcResourceGUID, Goliath.Security.ResourceType toType, AccessType toAccessType)
    {
        super(tcResourceName, toAccessType);

        m_cResourceName = tcResourceName;
        m_cResourceGUID = tcResourceGUID;
        m_oResourceType = toType;
    }
    
    public ResourcePermission(String tcResourceName, String tcResourceGUID, Goliath.Security.ResourceType toType, Boolean tlRead, Boolean tlWrite, Boolean tlExecute, Boolean tlCreate, Boolean tlDelete)
    {
        super(tcResourceName, tlRead, tlWrite, tlExecute, tlCreate, tlDelete);
        m_cResourceName = tcResourceName;
        m_cResourceGUID = tcResourceGUID;
        m_oResourceType = toType;
    }

    public ResourcePermission(String tcResourceName, long tnResourceID, Goliath.Security.ResourceType toType, Boolean tlRead, Boolean tlWrite, Boolean tlExecute, Boolean tlCreate, Boolean tlDelete)
    {
        super(tcResourceName, tlRead, tlWrite, tlExecute, tlCreate, tlDelete);
        m_cResourceName = tcResourceName;
        m_nResourceID = tnResourceID;
        m_oResourceType = toType;
    }

    public ResourcePermission(String tcResourceName, long tnResourceID, Goliath.Security.ResourceType toType, AccessType toAccessType)
    {
        super(tcResourceName, toAccessType);
        m_nResourceID = tnResourceID;
        m_oResourceType = toType;
    }
    
    @Override
    public long getResourceID()
    {
        return m_nResourceID;
    }

    @Override
    public String getResourceName()
    {
        return m_cResourceName;
    }

    @Override
    public String getResourceGUID()
    {
        return m_cResourceGUID;
    }

    @Override
    public Goliath.Security.ResourceType getResourceType()
    {
        return m_oResourceType;
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return getResourceName() + "[" + (Goliath.Utilities.isNullOrEmpty(m_cResourceName) ? m_nResourceID : m_cResourceGUID) + "]";
    }


}
