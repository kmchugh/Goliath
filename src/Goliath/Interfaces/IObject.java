/* =========================================================
 * IObject.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This interface represents a base Object.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces;

/**
 * This interface represents a base Object
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public interface IObject
{
     /**
     * Returns the string representation of the object 
     *
     * @return  the string representation of the object
     */
    @Override
    String toString();
    
    /**
     * Returns the string representation of the object 
     *
     * @param toFormat  the format to use to represent the object
     * @return  the string representation of the object
     */
    String toString(Goliath.Constants.StringFormatType toFormat);
    
    // need to implement toXML
}
