/* =========================================================
 * IProperty.java
 *
 * Author:      kmchugh
 * Created:     12-Dec-2007, 15:45:22
 * 
 * Description
 * --------------------------------------------------------
 * Interface is used to access name value pairs
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces;

/**
 * Interface is used to access name value pairs
 *
 * @param T     The type of the property value
 * @see         Goliath.Property
 * @version     1.0 12-Dec-2007
 * @author      kmchugh
**/
public interface IProperty<T>
{
     /**
     * returns the value of the property
     *
     * @return  the value of the property
     */       
    T getValue();
    
    /**
     * sets the value of the property
     *
     * @param  toValue  the new value to place in the property
     * @return true if the property was actually changed due to this call
     */
    boolean setValue(T toValue);
    
    /**
     * gets the name of the property
     *
     * @return  the name of the property
     */
    String getName();
}
