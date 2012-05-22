/* ========================================================
 * ClientType.java
 *
 * Author:      kmchugh
 * Created:     Aug 25, 2010, 3:16:58 PM
 *
 * Description
 * --------------------------------------------------------
 * Base ClientType, A client is something that connects to
 * this application
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath;

import Goliath.Applications.Application;
import Goliath.Collections.List;


        
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
public abstract class ClientType extends Goliath.Object
{

    private static List<ClientType> g_oClientTypes;

    public static ClientType createFromString(String tcIdentifier)
    {
        if (g_oClientTypes == null)
        {
            List<Class<ClientType>> loTypes = Application.getInstance().getObjectCache().getClasses(ClientType.class);
            g_oClientTypes = new List<ClientType>(loTypes.size());
            for (Class<ClientType> loTypeClass : loTypes)
            {
                try
                {
                    g_oClientTypes.add(loTypeClass.newInstance());
                }
                catch (Throwable ex)
                {
                }
            }
        }
        

        for (ClientType loType : g_oClientTypes)
        {
            if (loType.isSupported(tcIdentifier))
            {
                try
                {
                    return loType.getClass().newInstance();
                }
                catch(Throwable ex)
                {
                    
                }
            }
        }

        // If we got here, we were unable to create a client type, so use the default
        return Application.getInstance().getApplicationSettings().createUnknownClientType(tcIdentifier);
    }


    private String m_cIdentifier;

    /**
     * Creates a new instance of ClientType
     */
    public ClientType()
    {
    }

    /**
     * Returns true if the identifier passed in is supported by this type
     * @param tcIdentifier the identifier to check
     * @return true if supported, otherwise false
     */
    public abstract boolean isSupported(String tcIdentifier);

    /**
     * Attempts to populate the properties based on the object passed in
     * @param toProperties the object to use to populate the properties of this client type
     */
    public abstract void populateFromObject(java.lang.Object toProperties);

    public String getIdentifier()
    {
        return m_cIdentifier;
    }

    public void setIdentifier(String tcIdentifier)
    {
        m_cIdentifier = tcIdentifier;
    }
}
