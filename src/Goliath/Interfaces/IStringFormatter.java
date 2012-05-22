/* =========================================================
 * IStringFormatter.java
 *
 * Author:      kmchugh
 * Created:     14-Dec-2007, 13:10:44
 * 
 * Description
 * --------------------------------------------------------
 * General Interface Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces;

import Goliath.Collections.List;
import Goliath.Constants.StringFormatType;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @param T     The type of object being formatted
 * @see         Related Class
 * @version     1.0 14-Dec-2007
 * @author      kmchugh
**/
public interface IStringFormatter<T>
{
    /**
     * Returns a string based on the object 
     *
     * @param  toObject the object to get a string from
     * @return  a string representation of the object
     */
    String toString(T toObject, StringFormatType toFormatType);
    
    /**
     * Appends the object to string builder specified
     * @param toObject the object to append
     * @param toBuilder the string builder to append to
     * @param toFormatType the format type
     */
    void appendToStringBuilder(StringBuilder toBuilder, T toObject, StringFormatType toFormatType);
    
    /**
     * Gets the list of formats that are supported by this formatter
     * @return the list of format types
     */
    List<? extends StringFormatType> supportedFormats();

    
}
