/* ========================================================
 * DateXMLFormatter.java
 *
 * Author:      kmchugh
 * Created:     Aug 5, 2010, 10:59:34 PM
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

package Goliath.XML;

import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.XMLFormatType;
import Goliath.Date;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 5, 2010
 * @author      kmchugh
**/
public class DateXMLFormatter extends Goliath.XML.XMLFormatter<Date>
{
    @Override
    public Class supports()
    {
        return Date.class;
    }

    @Override
    protected boolean allowContent(Date toObject, XMLFormatType toFormatType)
    {
        return false;
    }

    @Override
    protected List<String> getAttributeList(Date toObject)
    {
        return new Goliath.Collections.List<String>(new String[]{"long"});
    }

    @Override
    protected Date onCreateObject(Class<Date> toClass, PropertySet toAttributes, Object toObject)
    {
        long lnTime = 0;
        try
        {
            lnTime = Long.parseLong(toAttributes.<String>getProperty("long"));
        }
        catch (Throwable ex)
        {}
        return new Date(lnTime);
    }


    @Override
    protected boolean finishedOnCreate()
    {
        return true;
    }
}
