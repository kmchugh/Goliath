/* ========================================================
 * FileXMLFormatter.java
 *
 * Author:      kenmchugh
 * Created:     May 3, 2011, 5:44:10 PM
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

import Goliath.Collections.PropertySet;
import Goliath.Constants.XMLFormatType;
import java.io.File;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 May 3, 2011
 * @author      kenmchugh
**/
public class FileXMLFormatter extends Goliath.XML.XMLFormatter<File>
{
    @Override
    public Class supports()
    {
        return File.class;
    }

    @Override
    protected boolean allowContent(File toObject, XMLFormatType toFormatType)
    {
        return false;
    }

    @Override
    protected Goliath.Collections.List<String> getAttributeList(File toObject)
    {
        return new Goliath.Collections.List<String>(new String[]{"path"});
    }

    @Override
    protected File onCreateObject(Class<File> toClass, PropertySet toAttributes, Object toObject)
    {
        String lcPath = toAttributes.getProperty("path");
        return new File(lcPath);
    }

    @Override
    protected boolean finishedOnCreate()
    {
        return true;
    }
}
