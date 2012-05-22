/* =========================================================
 * RuleArgs.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 7:29:55 PM
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

package Goliath.Arguments;

import Goliath.Collections.HashTable;
import Goliath.Interfaces.Arguments.IArguments;

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
public class Arguments extends Goliath.Object
        implements IArguments
{
    private String m_cMessage;
    private HashTable<String, java.lang.Object> m_oParameters;

    public Arguments()
    {
    }
    
    public String getMessage()
    {
        return m_cMessage;
    }
    
    public void setMessage(String tcMessage)
    {
        m_cMessage = tcMessage;
    }

    @Override
    public java.lang.Object getParameter(String tcParameter)
    {
        if (m_oParameters != null && m_oParameters.containsKey(tcParameter))
        {
            return m_oParameters.get(tcParameter);
        }
        return null;
    }

    @Override
    public void setParameter(String tcKey, java.lang.Object toParameter)
    {
        if (toParameter == null)
        {
            removeParameter(tcKey);
            return;
        }

        if (m_oParameters == null)
        {
            m_oParameters = new HashTable<String, java.lang.Object>();
        }
        m_oParameters.put(tcKey, toParameter);
    }

    @Override
    public void removeParameter(String tcKey)
    {
        if (m_oParameters != null && m_oParameters.containsKey(tcKey))
        {
            m_oParameters.remove(tcKey);

            if (m_oParameters.size() == 0)
            {
                m_oParameters = null;
            }
        }
        
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Arguments other = (Arguments) obj;
        if ((this.m_cMessage == null) ? (other.m_cMessage != null) : !this.m_cMessage.equals(other.m_cMessage)) {
            return false;
        }

        if (this.m_oParameters == null && other.m_oParameters != null || other.m_oParameters == null && this.m_oParameters != null)
        {
            return false;
        }

        for (java.lang.Object loKey : this.m_oParameters.keySet())
        {
            if (!other.m_oParameters.containsKey(loKey))
            {
                return false;

            }

            if (other.m_oParameters.get(loKey) != this.m_oParameters.get(loKey))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.m_cMessage != null ? this.m_cMessage.hashCode() : 0);
        hash = 73 * hash + (this.m_oParameters != null ? this.m_oParameters.hashCode() : 0);
        return hash;
    }



}
