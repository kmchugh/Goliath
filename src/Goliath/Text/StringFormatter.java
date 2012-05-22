/* =========================================================
 * StringFormatter.java
 *
 * Author:      kmchugh
 * Created:     14-Dec-2007, 12:38:43
 * 
 * Description
 * --------------------------------------------------------
 * Used for changing formatting the string representation of an object
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Text;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.StringFormatType;
import Goliath.DynamicCode.Java;

/**
 * Used for changing formatting the string representation of an object
 *
 * @param T     The type of object to format
 * @version     1.0 14-Dec-2007
 * @author      kmchugh
**/
public abstract class StringFormatter<T> extends Goliath.Object 
    implements Goliath.Interfaces.IStringFormatter<T>
{
    private static HashTable<StringFormatType, HashTable<Class, StringFormatter>> g_oFormatters;
    private static boolean g_lLoaded;
    
    /**
     * Helper function to load all of the available formatters
     */
    private static void loadFormatters()
    {
        if (g_oFormatters == null)
        {
            g_oFormatters = new HashTable<StringFormatType, HashTable<Class, StringFormatter>>();
            
            List<Class<StringFormatter>> loFormatters = Application.getInstance().getObjectCache().getClasses(StringFormatter.class);
            if (loFormatters != null && loFormatters.size() > 0)
            {
                for (Class<StringFormatter> loClass : loFormatters)
                try
                {
                    StringFormatter loFormatter = loClass.newInstance();
                    List<Class> loGenerics = Java.getGenericClasses(loClass);
                    if (loGenerics.size() == 0)
                    {
                        int i=1;
                    }
                    Class loSupportedClass = loGenerics.size() == 0 ? java.lang.Object.class :
                            loGenerics.get(0);
                    for (Object loType : loFormatter.supportedFormats())
                    {
                        if (!g_oFormatters.containsKey((StringFormatType)loType))
                        {
                            g_oFormatters.put((StringFormatType)loType, new HashTable<Class, StringFormatter>());
                        }
                        
                        if (!g_oFormatters.get((StringFormatType)loType).containsKey(loSupportedClass))
                        {
                            g_oFormatters.get((StringFormatType)loType).put(loSupportedClass, loFormatter);
                        }
                    }
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
            g_lLoaded = true;
            Application.getInstance().getObjectCache().clearClass(StringFormatter.class);
        }
    }
    
    public static <K> StringFormatter<K> getFormatterFor(K toObject, StringFormatType toType)
    {
        if (g_oFormatters == null)
        {
            loadFormatters();
        }

        Class loFormatClass = toObject.getClass();

        while (!g_lLoaded)
        {
            try
            {
                Thread.sleep(50);
            }
            catch (Throwable ex)
            {}
        }
        
        if (!g_oFormatters.containsKey(toType) || !g_oFormatters.get(toType).containsKey(loFormatClass))
        {
            int lnDistance = 99999;
            Class loUsableClass = null;
            for (Class loClass : g_oFormatters.get(toType).keySet())
            {
                int lnNewDistance = Java.getPropinquity(loFormatClass, loClass);
                if (lnNewDistance < lnDistance && lnNewDistance > -1)
                {
                    loUsableClass = loClass;
                    lnDistance = lnNewDistance;
                    // If the distance is zero, the classes can not be closer, so break
                    if (lnNewDistance == 0)
                    {
                        break;
                    }
                }
            }
            if (loUsableClass != null)
            {
                g_oFormatters.get(toType).put(loFormatClass, g_oFormatters.get(toType).get(loUsableClass));
            }
        }

        return (g_oFormatters.containsKey(toType) && g_oFormatters.get(toType).containsKey(loFormatClass))
                ? g_oFormatters.get(toType).get(loFormatClass)
                : null;
    }
    
    
    
    /** Creates a new instance of StringFormatter */
    public StringFormatter()
    {
    }
    
    /**
     * Checks if the formatting for this type allows content, or inner formatting
     * @return true to allow inner content
     */
    public boolean allowContent()
    {
        return true;
    }
    
    /**
     * Checks if the format type is supported by this formatter
     * @param toType the type to check 
     * @return true if this type is supported
     */
    public boolean isSupported(StringFormatType toType)
    {
        List<StringFormatType> loTypes = supportedFormats();
        return loTypes != null && loTypes.contains(toType);
    }

    /**
     * Appends the object to the string builder using the format type specified
     * @param toBuilder the string builder to append the object to
     * @param toObject the object to append the string format of
     * @param toFormatType the format type to use to append this object
     */
    @Override
    public final void appendToStringBuilder(StringBuilder toBuilder, T toObject, StringFormatType toFormatType)
    {
        if (isSupported(toFormatType))
        {
            if (toObject == null)
            {
                toBuilder.append(formatNullObject());
            }
            else
            {
                if (Java.isPrimitive(toObject) || toObject.getClass().isArray() || !allowContent())
                {
                    try
                    {
                        appendPrimitiveString(toBuilder, toObject, toFormatType);
                    }
                    catch(Throwable ex)
                    {
                        // We were unable to write the content for some reason
                        Application.getInstance().log(ex);
                    }
                }
                else
                {
                    try
                    {
                        toBuilder.append(getStartTag(toObject));
                        List<String> loProperties = getPropertyList(toObject);
                        int lnCount = loProperties.size();
                        int lnCounter = 0;
                        for (String lcProperty : loProperties)
                        {
                            Object loValue = null;
                            try
                            {
                                loValue = getPropertyValue(toObject, lcProperty);
                            }
                            catch (Throwable ex)
                            {
                                Application.getInstance().log(ex);
                            }
                            
                            formatComplexProperty(toBuilder, lcProperty, loValue, loValue == null ? this : StringFormatter.getFormatterFor(loValue, toFormatType), toFormatType);

                            lnCounter++;
                            formatForPropertyCount(toBuilder, lnCounter, lnCount, toFormatType);
                        }
                        toBuilder.append(getEndTag(toObject));
                    }
                    catch(Throwable ex)
                    {
                        // We were unable to write the content for some reason
                        Application.getInstance().log(ex);
                    }
                }
            }
        }
    }
    
    /**
     * Converts a primitive to a string of the specified format type
     * @param toBuilder the string builder to append the string to
     * @param toObject the object to convert
     * @param toType the type of conversion
     */
    public abstract void appendPrimitiveString(StringBuilder toBuilder, T toObject, StringFormatType toType);
    
    /**
     * Converts a complex object to a string of the specified format type
     * @param toBuilder the string builder to append the string to
     * @param toObject the object to convert
     * @param toType the type of conversion
     */
    protected abstract void formatComplexProperty(StringBuilder toBuilder, String tcPropertyName, Object toValue, StringFormatter toFormatter, StringFormatType toType);
    
    /**
     * Allows inclusion of formatting after each property has been written
     * @param toBuilder the string builder to write to 
     * @param tnIndex the index of the current property
     * @param tnCount the count of the property set
     * @param toFormatType the format type
     */
    protected abstract void formatForPropertyCount(StringBuilder toBuilder, int tnIndex, int tnCount, StringFormatType toFormatType);
    
    

    /**
     * Gets the formats that this formatter can support
     * @return the supported formats
     */
    @Override
    public abstract List<StringFormatType> supportedFormats();

    /**
     * Converts the object specified to a string of the specified format type
     * @param toObject the object
     * @param toFormatType the format type
     * @return 
     */
    @Override
    public String toString(T toObject, StringFormatType toFormatType)
    {
        if (isSupported(toFormatType))
        {
            return toObject == null ?
                formatNullObject() :
                toString(toObject);
        }
        return "";
    }
    
    /**
     * Converts a null object to a string representation
     * @param toFormatType the format type
     * @return the null representation of the object
     */
    public abstract String formatNullObject();
    
    /**
     * Apply default formatting to the object specified
     * @param toObject the object
     * @return the string formatted using the default algorithm for the format type
     */
    public abstract String toString(T toObject);
    
    /**
     * Gets the list of properties for this object
     * @param toClass the class of the object
     * @return the list of properties
     */
    protected List<String> getPropertyList(T toObject)
    {
        return Java.getPropertyMethods(toObject.getClass());
    }

    /**
     * Gets the value of the specified property from the object
     * @param toObject the object containing the value
     * @param lcProperty the property to get the value of
     * @return the value of the property
     */
    protected java.lang.Object getPropertyValue(T toObject, String lcProperty)
    {
        return Java.getPropertyValue(toObject, lcProperty);
    }
    
    protected abstract String getStartTag(T toObject);

    protected abstract String getEndTag(T toObject);
    
    
}
