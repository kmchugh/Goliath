/* =========================================================
 * Object.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 10:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This is the base class for Goliath Objects.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

/**
 * This class is the base class for most Goliath Objects.
 * It contains methods for object formatting
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class Object implements Goliath.Interfaces.IObject
{
    
    /** Creates a new instance of Object */
    protected Object()
    {
    }
    
    /**
     * Gets a string representation of the object
     *  To override the representation the formatString method must be overridden
     *
     * @return      The string representation of the object
     */
    @Override
    public final String toString()
    {
        return toString(Goliath.Constants.StringFormatType.DEFAULT());
    }
    
    /**
     * Gets a string representation of the object
     *  To override the representation the formatString method must be overridden
     *
     * @param toFormat  the format type to apply to the string representation
     * @return      The string representation of the object
     */
    @Override
    public final String toString(Goliath.Constants.StringFormatType toFormat)
    {
        return formatString(toFormat);
    }
    
    /**
     * Creates a string representation of the object
     *
     * @param toFormat  the format type to apply to the string representation
     * @return      The string representation of the object
     */
    protected String formatString(Goliath.Constants.StringFormatType toFormat)
    {
        return super.toString();       
    }
    
    // TODO: formatString should call format which takes a dynamic enum that points to a specific formatter factory.
    
}
