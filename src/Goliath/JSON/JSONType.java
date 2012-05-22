/* ========================================================
 * JSONType.java
 *
 * Author:      admin
 * Created:     Sep 21, 2011, 12:25:26 PM
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
 * ===================================================== */
package Goliath.JSON;

import Goliath.DynamicEnum;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Sep 21, 2011
 * @author      admin
 **/
public class JSONType extends DynamicEnum
{
    
    private static JSONType m_oObject;
    public static JSONType OBJECT()
    {
        if (m_oObject == null)
        {
            m_oObject = createEnumeration(JSONType.class, "OBJECT");
        }
        return m_oObject;
    }
    
    private static JSONType m_oNull;
    public static JSONType NULL()
    {
        if (m_oNull == null)
        {
            m_oNull = createEnumeration(JSONType.class, "NULL");
        }
        return m_oNull;
    }
    
    private static JSONType m_oArray;
    public static JSONType ARRAY()
    {
        if (m_oArray == null)
        {
            m_oArray = createEnumeration(JSONType.class, "ARRAY");
        }
        return m_oArray;
    }
    
    private static JSONType m_oPrimitive;
    public static JSONType PRIMITIVE()
    {
        if (m_oPrimitive == null)
        {
            m_oPrimitive = createEnumeration(JSONType.class, "PRIMITIVE");
        }
        return m_oPrimitive;
    }

    /**
     * Creates a new instance of JSONType
     */
    protected JSONType(String tcValue)
    {
        super(tcValue);
    }
}
