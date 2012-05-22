/* =========================================================
 * Property.java
 *
 * Author:      kmchugh
 * Created:     12-Dec-2007, 15:44:02
 * 
 * Description
 * --------------------------------------------------------
 * This class is used to store attribute value pairs
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

import Goliath.Constants.StringFormatType;

/**
 * This class is used to store attribute value pairs
 *
 * @param T     The type of the value
 * @version     1.0 12-Dec-2007
 * @author      kmchugh
**/
public class Property<T> extends Goliath.Object implements Goliath.Interfaces.IProperty<T> 
{
     /**
     * Creates and returns a property with the specified values
     *
     * @param  tcName   the name of the property
     * @param  toValue  the value of the property
     * @return  a new property object
     */
    public static <K> Property<K> createProperty(String tcName, K toValue)
    {
        return new Property<K>(tcName, toValue);
    }
    
    /**
     * Gets the value from the property
     *
     * @param  toProperty the property to get the value from
     * @return  a new property object
     */
    public static <K> K getValue(Goliath.Interfaces.IProperty<K> toProperty)
    {
        return toProperty.getValue();
    }
            
    private final String m_cName;
    private T m_oValue;
    
    /**
     * Creates a new instance of Property
     *
     * @param  tcName   The name of the property
     */
    public Property(String tcName)
    {
        m_cName = tcName;
        m_oValue = null;
    }
    
    /**
     * Creates a new instance of Property
     *
     * @param  tcName   The name of the property
     * @param  toValue  The initial property value
     */
    public Property(String tcName, T toValue)
    {
        m_cName = tcName;
        m_oValue = toValue;
    }

    /**
     * Gets the name of this property
     * @return  the name of the property
     */
    @Override
    public String getName()
    {
        return m_cName;
    }

    /**
     * Gets the current property value
     *
     * @return  the current property value
     */
    @Override
    public T getValue()
    {
        return m_oValue;
    }

    /**
     * Sets the value of this property to the new value
     *
     * @param  toValue  The new value of the property
     * @return true if this action caused the value to change
     */
    @Override
    public boolean setValue(T toValue)
    {
        if (m_oValue == null || !m_oValue.equals(toValue))
        {
            m_oValue = toValue;
            return true;
        }
        return false;
    }

    /**
     * gets a string representation of the object
     *
     * @param  toFormat the format to return the string in
     * @return the string representation of the object
     */
    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return m_cName + " = " + Goliath.Utilities.isNull(m_oValue, "null").toString();
    }

    @Override
    public boolean equals(java.lang.Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Property<T> other = (Property<T>) obj;
        if ((this.m_cName == null) ? (other.m_cName != null) : !this.m_cName.equals(other.m_cName))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + (this.m_cName != null ? this.m_cName.hashCode() : 0);
        return hash;
    }

    

    
    
}
