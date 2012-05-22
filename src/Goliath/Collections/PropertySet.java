/* =========================================================
 * PropertySet.java
 *
 * Author:      kmchugh
 * Created:     12-Dec-2007, 16:03:53
 * 
 * Description
 * --------------------------------------------------------
 * Contains a set of properties
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.Collections;

import Goliath.Exceptions.InvalidIndexException;
import java.util.Iterator;
import java.util.Map;

/**
 * Contains a set of properties, properties can
 * be added and removed.  If a property already exists
 * in the collection then its value is replaces with the
 * property being added.  Property names are case insensitive
 *
 * @see         
 * @version     1.0 12-Dec-2007
 * @author      kmchugh
 **/
public class PropertySet extends Goliath.Collections.HashTable<String, java.lang.Object>
        implements Goliath.Interfaces.Collections.IPropertySet
{

    public class NullValue
    {

        private NullValue g_oNullValue;

        private NullValue()
        {
        }

        public NullValue getInstance()
        {
            if (g_oNullValue == null)
            {
                g_oNullValue = new NullValue();
            }
            return g_oNullValue;
        }
    }
    private NullValue m_oNullValue = new NullValue();

    // TODO: This class needs to be cleaned and refactored, the set and clear should also return the old values rather than a boolean value
    /**
     * Creates a new instance of the hashtable class
     * A property bag is case sensitive by default
     */
    public PropertySet()
    {
        super();
    }

    
    /**
     * Creates a new instance of the hashtable class
     * A property bag is case sensitive by default
     *
     * @param  tnInitialCapacity    the size of the initial table
     */
    public PropertySet(int tnInitialCapacity)
    {
        super(tnInitialCapacity);
    }
    
    /**
     * Creates a new instance of the hashtable class
     * A property bag is case sensitive by default
     *
     * @param  tnInitialCapacity    the size of the initial table
     * @param  tnLoadFactor         the size to increment the table when full
     */
    public PropertySet(int tnInitialCapacity, float tnLoadFactor)
    {
        super(tnInitialCapacity, tnLoadFactor);


    }

    /**
     * Creates a property set from a hashtable where the first generic type is a string
     * @param toHashTable the hash table to create a property set from
     */
    public PropertySet(Map<String, ? extends java.lang.Object> toMap)
    {
        super(toMap.size());
        for (String lcKey : toMap.keySet())
        {
            this.setProperty(lcKey, toMap.get(lcKey));
        }
    }

    /**
     * Adds a new property or changes the value of an existing one 
     *
     * @param <T> The type of the property
     * @param  tcProperty the name of the property to add or change
     * @param toValue the value of the property
     * @return  true if a value was changed or a property was added
     */

    @Override
    public final <K> boolean setProperty(String tcProperty, K toValue)
    {
        tcProperty = tcProperty.toLowerCase();
        return this.put(tcProperty, toValue) == null;
    }
    
    /**
     * Merges toProperties with this property set.  Any common properties will be
     * left at the original value if tlOverwrite if false, if tlOverwrite is true
     * then the common property values will be taken from toProperties
     * @param toProperties the Property set to merge
     * @param tlOverwrite if true then overwrite existing properties with values from toProperties
     * @return true if any properties were changed or added as a result of this call
     */
    public final boolean merge(PropertySet toProperties, boolean tlOverwrite)
    {
        boolean llReturn = false;
        for (String lcProperty : toProperties.getPropertyKeys())
        {
            llReturn = this.setProperty(lcProperty, this.hasProperty(lcProperty) && !tlOverwrite ? this.getProperty(lcProperty) : toProperties.getProperty(lcProperty))  || llReturn;
        }
        return llReturn;
    }

    /**
     * Clears a property from the list
     *
     * @param  tcName   the property specified
     * @return true if the list is changed due to this call
     */
    @Override
    public final boolean clearProperty(String tcName)
    {
        tcName = tcName.toLowerCase();
        return this.remove(tcName) != null;
    }
    
    /**
     * Returns the value of a property, returns null if the property does not exist
     *
     * @param <T> The type of the property
     * @param  tcName   the property specified
     * @return returns the property value or null if the property is not found
     */
    @Override
    public final <K> K getProperty(String tcName)
    {
        return (K)this.getProperty(tcName, false);
    }
    
    /**
     * Returns the value of a property
     *
     * @param <T> The type of the property
     * @param  tcName   the property specified
     * @param  tlThrowError  Throws an error if the property is not found
     * @return returns the property value
     * @throws Goliath.Exceptions.InvalidIndexException if the property was not found and tlThrowError is true
     */
    @Override
    public final <K> K getProperty(String tcName, boolean tlThrowError) throws InvalidIndexException
    {
        tcName = tcName.toLowerCase();
        if (!this.containsKey(tcName) && tlThrowError)
        {
            // if the property was not found and an error was asked for, throw the error
            throw new Goliath.Exceptions.InvalidIndexException("Index does not exist");
        }

        Object loReturn = this.get(tcName);
        return (K)(loReturn == m_oNullValue ? null : loReturn);
    }

    @Override
    public synchronized Object put(String tcProperty, Object toValue)
    {
        return super.put(tcProperty.toLowerCase(), toValue != null ? toValue : m_oNullValue);
    }
    
    

    /**
     * Checks if the property set has the specified property
     * @param tcProperty the property to check for
     * @return true if contained
     */
    @Override
    public final boolean hasProperty(String tcProperty)
    {
        return this.containsKey(tcProperty);
    }

    @Override
    public final boolean containsKey(Object tcProperty)
    {
        return super.containsKey(((String) tcProperty).toLowerCase());
    }

    /**
     * Gets the list of keys that are in the property bag, this will return a 
     * new list so modifications to the list will not have an effect on the property set
     * @return the list of keys that exist in the property bag
     */
    @Override
    public final Goliath.Collections.List<String> getPropertyKeys()
    {
        return new Goliath.Collections.List<String>(this.keySet());
    }

    /**
     * Gets the iterator for the property bag
     * @return
     */
    @Override
    public final Iterator<java.lang.Object> iterator()
    {

        return this.iterator();

    }
}
