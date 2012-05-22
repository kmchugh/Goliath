/* ========================================================
 * JSONObject.java
 *
 * Author:      admin
 * Created:     Sep 21, 2011, 12:07:27 PM
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

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Constants.StringFormatType;
import Goliath.DynamicCode.Java;
import Goliath.Exceptions.InvalidParameterException;
import java.util.Iterator;

/**
 * A JSONObject is the encapsulation of a JSON type string
 *
 * @see         Related Class
 * @version     1.0 Sep 21, 2011
 * @author      admin
 **/
public class JSON extends Goliath.Object
{
    public class NullJSON extends JSON
    {
        protected NullJSON()
        {
            super("{}", JSONType.NULL());
        }

        @Override
        public List<String> getProperties()
        {
            return new List<String>(0);
        }
    }
    
    private JSONObject m_oJSONObject;
    private JSONArray m_oJSONArray;
    private JSONType m_oType;
    private Object m_oValue;
    
    /**
     * Creates a new JSON Object from a JSON Formatted string
     * @param tcJSONString the string containing the JSON formatted data
     * @throws InvalidParameterException if the string does not contain JSON
     */
    protected JSON(String tcJSONString, JSONType toType)
            throws InvalidParameterException
    {
        Goliath.Utilities.checkParameterNotNull("tcJSONString", tcJSONString);
        try
        {
            this.m_oJSONObject = new JSONObject(tcJSONString);
            this.m_oType = toType;
        }
        catch (JSONException ex)
        {
            throw new InvalidParameterException(ex.toString());
        }
    }

    /**
     * Creates a new JSON Object from a JSON Formatted string
     * @param tcJSONString the string containing the JSON formatted data
     * @throws InvalidParameterException if the string does not contain JSON
     */
    public JSON(String tcJSONString)
            throws InvalidParameterException
    {
        this(tcJSONString, JSONType.OBJECT());
    }
    
    /**
     * Creates a JSON Object from the value passed in
     * @param toValue the value to create the JSON Object from
     */
    private JSON(Object toValue)
    {
        // If this is a JSON Object then set the value
        if (isJSONObject(toValue))
        {
            if (isArray(toValue))
            {
                m_oJSONArray = (JSONArray)toValue;
                m_oType = JSONType.ARRAY();
            }
            else
            {
                m_oJSONObject = (JSONObject)toValue;
                m_oType = JSONType.OBJECT();
            }
        }
        else
        {
            if (toValue == null)
            {
                m_oType = JSONType.NULL();
            }
            else
            {
                m_oValue = toValue;
                m_oType = JSONType.PRIMITIVE();
            }
        }
    }
    
    /**
     * Helper function to check if the object passed is a JSON Array
     * @param toValue the value to check
     * @return true if this value is an array
     */
    private boolean isArray(Object toValue)
    {
        return toValue != null &&
                Java.isEqualOrAssignable(JSONArray.class, toValue.getClass());
    }
    
    /**
     * Helper function to check if the specified object is a json object
     * @param toValue the object to check
     * @return true if this is a json object
     */
    private boolean isJSONObject(Object toValue)
    {
        return toValue != null && (
                Java.isEqualOrAssignable(JSONObject.class, toValue.getClass()) ||
                Java.isEqualOrAssignable(JSONArray.class, toValue.getClass()));
    }
    
    /**
     * Gets the list of properties that are available for use on this object
     * @return the list of properties on this JSON Object
     */
    public List<String> getProperties()
    {
        List<String> loKeys = new List<String>(m_oJSONObject.length());
        Iterator loIter = m_oJSONObject.keys();
        while (loIter.hasNext())
        {
            loKeys.add(loIter.next().toString());
        }
        return loKeys;
        
    }
    
    /**
     * Gets the type of this object
     * @return the JSON type of this object
     */
    public JSONType getType()
    {
        return m_oType;
    }
    
    /**
     * Gets the value of the specified property as a JSONObject, this allows for chaining.
     * e.g. loObject.get("child1").get("anotherproperty");
     * If the property does not exist, this will return a null object
     * @param tcProperty the property to get the value for
     * @return the value of the property
     */
    public JSON get(String tcProperty)
    {
        try
        {
            Object loValue = m_oJSONObject.get(tcProperty);
            return loValue == null ? new NullJSON() : new JSON(loValue);
        }
        catch (JSONException ex)
        {
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return new NullJSON();
    }
    
    /**
     * Gets the value at a specified point in the array of this node
     * @param tnIndex the index of the array to get
     * @return the value of the object
     */
    public JSON get(int tnIndex)
    {
        try
        {
            Object loValue = m_oJSONArray != null ? m_oJSONArray.get(tnIndex) : null;
            return loValue == null ? new NullJSON() : new JSON(loValue);
        }
        catch (JSONException ex)
        {
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return new NullJSON();
    }
    
    /**
     * If this is a leaf node object, then the value returned will be the value being
     * held by this node.  Otherwise this will return null
     * @return the value of this node
     */
    public Object getValue()
    {
        return m_oValue;
    }
    
    /**
     * Gets the integer representation of this node
     * @return the numeric representation or 0 if there is a problem
     */
    public int getIntValue()
    {
        try
        {
            return m_oValue != null ? ((Integer)m_oValue).intValue() : 0;
        }
        catch (Throwable ex)
        {
            return 0;
        }
    }
    
    /**
     * Gets the float representation of this node
     * @return the numeric representation or 0 if there is a problem
     */
    public float getFloatValue()
    {
        try
        {
            return m_oValue != null ? ((Float)m_oValue).floatValue() : 0f;
        }
        catch (Throwable ex)
        {
            return 0f;
        }
    }
    
    /**
     * Gets the string value of this node
     * @return the string representation or "" if there is a problem
     */
    public String getStringValue()
    {
        return m_oValue != null ? m_oValue.toString() : "";
    }
    
    /**
     * Sets the property specified to the value specified
     * @param tcProperty the property to set
     * @param toValue the value to set
     * @return true if the value changed as a result of this call
     */
    public boolean setProperty(String tcProperty, Object toValue)
    {
        try
        {
            // TODO : Handle non primitive object types and non JSON using the getproperties
            if (m_oJSONObject != null)
            {
                m_oJSONObject.put(tcProperty, toValue);
            }
            else if (m_oJSONArray != null)
            {
                m_oJSONArray.put(toValue);
            }
            return true;
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return false;
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        if (m_oJSONObject != null)
        {
            return m_oJSONObject.toString();
        }
        else if (m_oJSONArray != null)
        {
            return m_oJSONArray.toString();
        }
        else if (m_oValue != null)
        {
            return m_oValue.toString();
        }
        else
        {
            return "null";
        }
    }
    
}
