/* ========================================================
 * ClientInformation.java
 *
 * Author:      kmchugh
 * Created:     Aug 25, 2010, 2:16:56 PM
 *
 * Description
 * --------------------------------------------------------
 * Holds all the information related to a client of a session
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Applications;

import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Date;

        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 25, 2010
 * @author      kmchugh
**/
public class ClientInformation extends Goliath.Object
{
    private PropertySet m_oProperties;
    private Date m_oCreated;

    /**
     * Creates a new instance of ClientInformation
     */
    public ClientInformation()
    {
        m_oCreated = new Date();
    }

    public <T> T getProperty(String tcName)
    {
        return m_oProperties != null ? (T)m_oProperties.getProperty(tcName) : null;
    }

    public <T> void setProperty(String tcName, T toValue)
    {
        if (m_oProperties == null)
        {
            m_oProperties = new PropertySet();
        }
        m_oProperties.setProperty(tcName, toValue);
    }

    public Date getCreatedDate()
    {
        return m_oCreated;
    }

    public List<String> getPropertyKeySet()
    {
        return m_oProperties == null ? new List<String>(0) : new List<String>(m_oProperties.keySet());
    }

}
