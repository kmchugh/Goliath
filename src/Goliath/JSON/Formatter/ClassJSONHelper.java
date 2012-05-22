/* ========================================================
 * ClassJSONHelper.java
 *
 * Author:      admin
 * Created:     Aug 12, 2011, 5:39:43 PM
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
package Goliath.JSON.Formatter;

import Goliath.Constants.StringFormatType;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 12, 2011
 * @author      admin
 **/
public class ClassJSONHelper extends JSONFormatter<Class>
{
    
    @Override
    public boolean allowContent()
    {
        return false;
    }

    /**
     * Creates a new instance of ClassJSONHelper
     */
    public ClassJSONHelper()
    {
    }
    
    @Override
    public void appendPrimitiveString(StringBuilder toBuilder, Class toObject, StringFormatType toType)
    {
        Goliath.Utilities.appendToStringBuilder(toBuilder, 
                    "\"" + toObject.getName() + "\"");
    }
}
