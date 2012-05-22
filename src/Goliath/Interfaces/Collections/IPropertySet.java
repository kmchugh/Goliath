/* =========================================================
 * IPropertySet.java
 *
 * Author:      kmchugh
 * Created:     12-Dec-2007, 16:06:03
 * 
 * Description
 * --------------------------------------------------------
 * Interface for controlling a property bag
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.Collections;


/**
 * Interface for adding and removing properties from 
 * a property bag
 *
 * @see         Goliath.Collections.PropertyBag
 * @version     1.0 12-Dec-2007
 * @author      kmchugh
**/
public interface IPropertySet extends java.util.Map<String, java.lang.Object>,
        java.lang.Iterable<java.lang.Object >
{
     /**
     * Returns the value of a property
     *
     * @param <K> The type of the property
     * @param  tcName   the property specified
     * @return returns the property value or null if the property is not found
     */
    <K> K getProperty(String tcName);

    /**
     * Checks if the property set has the specified property
     * @param tcProperty the property to check for
     * @return true if contained
     */
    boolean hasProperty(String tcProperty);

    /**
     * Clears a property from the collection, this will remove the item from the internal list
     *
     * @param  tcName   the property specified
     * @return true if the collection is changed due to this call
     */
    boolean clearProperty(String tcName);
    
    
    /**
     * Returns the value of a property
     * @param <K> The type of the property
     * @param  tcName   the property specified
     * @param  tlThrowError  Throws an error if the property is not found
     * @return returns the property value
     * @throws Goliath.Exceptions.InvalidIndexException if the property was not found and tlThrowError is true
     */
    <K> K getProperty(String tcName, boolean tlThrowError) throws Goliath.Exceptions.InvalidIndexException;

    /**
     * Sets the property specified, returns true if this call resulted in a change
     * to the internal data structure
     * @param <K> The type of the property
     * @param tcProperty the property to get
     * @param toValue the new value of the property
     * @return true if the value was changed, false if the new value was inserted
     */
    <K> boolean setProperty(String tcProperty, K toValue);
    

    /**
     * Gets the list of keys that are in the property bag
     * @return the list of keys that exist in the property bag
     */
    Goliath.Collections.List<String> getPropertyKeys();
}
