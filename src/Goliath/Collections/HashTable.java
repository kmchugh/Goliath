/* =========================================================
 * HashMap.java
 *
 * Author:      kmchugh
 * Created:     12-Dec-2007, 16:39:51
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
 * =======================================================*/

package Goliath.Collections;

import java.util.Map;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @param <K>     The type to use as the key for the hash table
 * @param <V>     The type that is stored in the hash table
 * @version     1.0 12-Dec-2007
 * @author      kmchugh
**/
public class HashTable<K, V> extends java.util.Hashtable<K, V>
{
    
     /**
     * Creates a new instance of the hashtable class
     */
    public HashTable()
    {
        super(0);
    }

    /**
     * Creates a new instance of the hashtable class
     *
     * @param  tnInitialCapacity    the size of the initial table
     */
    public HashTable(int tnInitialCapacity)
    {
        super(tnInitialCapacity);
    }

    /**
     * Creates a new instance of the hashtable class
     *
     * @param  tnInitialCapacity    the size of the initial table
     * @param  tnLoadFactor         the size to increment the table when full
     */
    public HashTable(int tnInitialCapacity, float tnLoadFactor)
    {
        super(tnInitialCapacity, tnLoadFactor);
    }
    
    /**
     * Copy constructor
     * @param toHashTable the map to copy
     */
    public HashTable(Map<K, V> toMap)
    {
        super(toMap);
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
}
