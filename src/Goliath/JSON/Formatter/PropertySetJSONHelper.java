/* =========================================================
 * PropertySetJSONHelper.java
 *
 * Author:      kenmchugh
 * Created:     Oct 7, 2009, 12:26:52 PM
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
import Goliath.Collections.PropertySet;

/**
 *
 * @author kenmchugh
 */
public class PropertySetJSONHelper<T extends PropertySet> extends JSONFormatter<T>
{

    @Override
    protected List<String> getPropertyList(T toObject)
    {
        return toObject.getPropertyKeys();
    }

    @Override
    protected Object getPropertyValue(T toObject, String lcProperty)
    {
        return toObject.getProperty(lcProperty);
    }

    /*
    @Override
    public Class<T> supports()
    {
        return (Class<T>)PropertySet.class;
    }
     * 
     */
}
