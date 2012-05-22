/* ========================================================
 * Utilities.java
 *
 * Author:      admin
 * Created:     Aug 9, 2011, 2:01:37 AM
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

import Goliath.Constants.StringFormatType;
import Goliath.Text.StringFormatter;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 9, 2011
 * @author      admin
 **/
public class Utilities extends Goliath.Object
{

    /**
     * Creates a new instance of Utilities
     */
    private Utilities()
    {
    }
    
    public static String toJSON(java.lang.Object toObject)
    {
        StringBuilder loBuilder = new StringBuilder();
        StringFormatter loFormatter = StringFormatter.getFormatterFor(toObject, StringFormatType.JSON());
        loFormatter.appendToStringBuilder(loBuilder, toObject, StringFormatType.JSON());
        
        return loBuilder.toString();
    }
}
