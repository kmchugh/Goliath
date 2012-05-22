/* =========================================================
 * DateJSONHelper.java
 *
 * Author:      kenmchugh
 * Created:     Oct 7, 2009, 1:36:30 PM
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

package Goliath.JSON.Formatter;

import Goliath.Collections.List;

/**
 *
 * @author kenmchugh
 */
public class DateJSONHelper<T extends Goliath.Date> extends JSONFormatter<T>
{
    private List<String> g_oPropertyList;

    /*
    @Override
    public Class<T> supports()
    {
        return (Class<T>)Goliath.Date.class;
    }
     * 
     */

    @Override
    protected List<String> getPropertyList(T toObject)
    {
        if (g_oPropertyList == null)
        {
            g_oPropertyList = new List<String>();
            g_oPropertyList.add("Time");
        }
        return g_oPropertyList;
    }

    @Override
    protected java.lang.Object getPropertyValue(T toObject, String lcProperty)
    {
        if (lcProperty.equalsIgnoreCase("Time"))
        {
            return toObject.getLong();
        }
        return super.getPropertyValue(toObject, lcProperty);
    }
}