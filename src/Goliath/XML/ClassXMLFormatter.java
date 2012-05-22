/* ========================================================
 * ClassXMLFormatter.java
 *
 * Author:      kenmchugh
 * Created:     Apr 25, 2011, 5:32:06 PM
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


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Apr 25, 2011
 * @author      kenmchugh
**/
public class ClassXMLFormatter extends Goliath.XML.XMLFormatter<Class>
{
    @Override
    public Class supports()
    {
        return Class.class;
    }

    @Override
    protected boolean allowContent(Class toObject, XMLFormatType toFormatType)
    {
        return false;
    }

    @Override
    protected List<String> getAttributeList(Class toObject)
    {
        return new Goliath.Collections.List<String>(new String[]{"Name"});
    }

    @Override
    protected Class onCreateObject(Class<Class> toClass, PropertySet toAttributes, Object toObject)
    {
        Class loClass = null;
        try
        {
            loClass = Class.forName(toAttributes.<String>getProperty("Name"));
        }
        catch (Throwable ex)
        {}
        return loClass;
    }


    @Override
    protected boolean finishedOnCreate()
    {
        return true;
    }
}