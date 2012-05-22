/* =========================================================
 * DynamicEnum.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 10:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This class is to be used like enumerations, but allows
 * for a third party to extend on the fly.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

import Goliath.Applications.Application;
import Goliath.Applications.ApplicationState;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.LogType;
import Goliath.Constants.StringFormatType;
import Goliath.DynamicCode.Java;
import Goliath.Interfaces.Collections.IList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class acts as an Enumeration to make it easier on the
 * developers.  It is also extendable at runtime and design time
 * For example:
 * <pre>
 *      return toString(Goliath.Constants.StringFormatType.DEFAULT());
 * </pre>
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class DynamicEnum extends Goliath.Object
{
    private String m_cValue;
    private static HashTable<java.lang.Class<? extends DynamicEnum>, HashTable<String, DynamicEnum>> g_oEnums = 
            new HashTable<java.lang.Class<? extends DynamicEnum>, HashTable<String, DynamicEnum>>();
    
    /**
     * Gets the class that this dynamic enumeration is part of.  This is the class that should
     * be used to load the dynamic enumerations
     * @param <K> the class type, must extend DynamicEnum
     * @param toClass the Dynamic Enum class
     */
    private static <K extends DynamicEnum> Class<K> getEnumerationClass(Class<K> toClass)
    {
        Class loClass = toClass.getClass().getEnclosingClass();
        if (loClass == null)
        {
            loClass = toClass;
            while (loClass != null && loClass.getSuperclass() != DynamicEnum.class)
            {
                loClass = loClass.getSuperclass();
            }
        }
        return loClass;
    }
    
    /**
     * Adds a dynamic enum to the global collection
     * @param <K>
     * @param toEnum
     */
    private static <K extends DynamicEnum> void addDynamicEnum(K toEnum)
    {
        Class<K> loClass = getEnumerationClass((Class<K>)toEnum.getClass());
        if (!g_oEnums.containsKey(loClass))
        {
            g_oEnums.put(loClass, new HashTable<String, DynamicEnum>());
            // First time, so also load and add any other enums from the class
            loadDynamicEnums(loClass);                   
        }
        g_oEnums.get(loClass).put(toEnum.getValue().toLowerCase(), toEnum);
    }
    
    /**
     * Loads all of the dynamic enums from the specified class
     * @param <K>
     * @param toClass
     */
    private static <K extends DynamicEnum> void loadDynamicEnums(Class<K> toClass)
    {
        Class<K> loClass = getEnumerationClass(toClass);
        // Get all the static methods that take no parameter and return the class specified
        for (Method loMethod : loClass.getDeclaredMethods())
        {
            if (Modifier.isPublic(loMethod.getModifiers()) && Modifier.isStatic(loMethod.getModifiers()) && loMethod.getReturnType().equals(loClass) && loMethod.getParameterTypes().length == 0)
            {
                try
                {
                    java.lang.Object[] loArg = null;
                    addDynamicEnum((K)loMethod.invoke(null, loArg));
                }
                catch(Exception ex)
                {
                    Application.getInstance().log(ex);
                }
            }
        }
        
        
        try
        {
            // Get all of the classes that inherit from this class that have public constructors which take no parameters
            if (Application.getInstance() != null &&
                    Application.getInstance().getState().equals(ApplicationState.RUNNING()))
            {
                // TODO: This can be optimised by checking if the class is already loaded as an enum
                IList<Class<K>> loClasses = Java.getClasses(loClass, (java.lang.Class[])null);
                for (Class<K> loSubClass : loClasses)
                {
                    if (!Java.isAbstract(loSubClass))
                    {
                        try
                        {
                            addDynamicEnum(loSubClass.newInstance());
                        }
                        catch (InstantiationException ex)
                        {
                        }
                        catch(Exception ex)
                        {
                            Application.getInstance().log(ex);
                        }
                    }
                }
            }
        }
        catch (Throwable ex)
        {
        }
    }
    
    /**
     * Gets all the enumerations of a specified type
     * @param <K>
     * @param toClass
     * @return
     */
    public static <K extends DynamicEnum> List<K> getEnumerations(java.lang.Class<K> toClass)
    {
        Class<K> loClass = getEnumerationClass(toClass);
        if (g_oEnums == null || !g_oEnums.containsKey(loClass))
        {
            loadDynamicEnums(loClass);
        }
        return (List<K>)new List(g_oEnums.get(loClass).values());
    }
    
    
    /**
     * Gets a specific enumeration from a specified class
     * @param <K>
     * @param toClass
     * @param tcValue
     * @return
     */ 
    public static <K extends DynamicEnum> K getEnumeration(java.lang.Class<K> toClass, String tcValue)
    {
        Class<K> loClass = getEnumerationClass(toClass);
        if (!g_oEnums.containsKey(loClass))
        {
            loadDynamicEnums(loClass);
        }

        // Prepare the key
        tcValue = tcValue.replaceAll(";.+$", "").trim().toLowerCase();
        
        if (g_oEnums.containsKey(loClass))
        {
            if (g_oEnums.get(loClass).containsKey(tcValue))
            {
                return (K)g_oEnums.get(loClass).get(tcValue);
            }
        }
        Application.getInstance().log("Enumeration Value " + tcValue + " does not exist in the enumeration type " + loClass.getName(), LogType.WARNING());
        return null;
    }

    /**
     * Gets a specific enumeration from a specified class, if none is found then the default is returned
     * @param <K>
     * @param toClass
     * @param tcValue
     * @return
     */
    public static <K extends DynamicEnum> K getEnumeration(java.lang.Class<K> toClass, String tcValue, K toDefault)
    {
        return Goliath.Utilities.isNull(getEnumeration(toClass, tcValue), toDefault);
    }



    // TODO: All dynamic enumeration classes should be created using the createEnumeration method (write a method to confirm by checking stack trace in constructor)

    /**
     *  Creates the enumeration.  this should be used to create all dynamic enumeration static members
     * @param <K>
     * @param toClass the class to create the enumeration from
     * @param tcValue the value of the enum
     * @return the new enumeration
     */
    protected synchronized static <K extends DynamicEnum> K createEnumeration(java.lang.Class<K> toClass, String tcValue)
    {
        return createEnumeration(toClass, tcValue, (java.lang.Object[])null);
    }


    /**
     * Creates the enumeration.  this should be used to create all dynamic enumeration static members
     * @param <K>
     * @param toClass the class to create the enumeration from
     * @param tcValue the value of the enum
     * @param taParams any additional parameters
     * @return the new enumeration
     */
    protected synchronized static <K extends DynamicEnum> K createEnumeration(java.lang.Class<K> toClass, String tcValue, java.lang.Object... taParams)
    {
        K loReturn = null;
        // First check if the enum already exists
        if (g_oEnums != null && g_oEnums.containsKey(toClass) && g_oEnums.get(toClass).containsKey(tcValue))
        {
            loReturn = (K)g_oEnums.get(toClass).get(tcValue);
        }
        else
        {
            java.lang.Object[] laParams = new java.lang.Object[taParams != null ? taParams.length + 1 : 1];
            laParams[0] = tcValue;
            if (taParams != null)
            {
                for (int i=0, lnLength = taParams.length; i<lnLength; i++)
                {
                    laParams[i+1] = taParams[i];
                }
            }
            // We manually create this rather than calling the create object from the java class, as that can cause stack overflow
            int lnLength = (laParams != null) ? laParams.length : 0;
            Class[] laClasses = new Class[lnLength];
            for (int i=0; i<lnLength; i++)
            {
                laClasses[i] = laParams[i].getClass();
            }

            // Get the constructor
            try
            {
                Constructor<K> loConstructor = Java.getConstructor(toClass, laClasses);
                if (loConstructor != null && !Modifier.isAbstract(toClass.getModifiers()))
                {
                    boolean llIsAccessible = loConstructor.isAccessible();
                    if (!llIsAccessible)
                    {
                        loConstructor.setAccessible(true);
                    }

                    loReturn = loConstructor.newInstance(laParams);

                    if (!llIsAccessible)
                    {
                        loConstructor.setAccessible(false);
                    }
                }
                else
                {
                    throw new Goliath.Exceptions.ObjectNotCreatedException("No constructor found on " + toClass.getSimpleName() + " with argument types " + laClasses.toString());
                }
            }
            catch(Throwable ex)
            {
                throw new Goliath.Exceptions.ObjectNotCreatedException(ex);
            }
            if (loReturn != null)
            {
                if (g_oEnums != null && g_oEnums.containsKey(toClass) && g_oEnums.get(toClass).containsKey(tcValue))
                {
                    return (K)g_oEnums.get(toClass).get(tcValue);
                }
            }
        }
        return loReturn;
    }
            
    /** 
     * Creates a new instance of DynamicEnum 
     * DynamicEnum is used to create enumeration values that can be
     * altered during run time
     *
     * @param tcValue   The value of the enumeration
     * @throws Goliath.Exceptions.InvalidParameterException    if tcValue is not a valid value
     */
    protected DynamicEnum(String tcValue)
    {
        Goliath.Utilities.checkParameterNotNull("tcValue", tcValue);
        m_cValue = tcValue;
        addDynamicEnum(this);
    }
    
    /** 
     * Creates a new instance of DynamicEnum 
     * DynamicEnum is used to create enumeration values that can be
     * altered during run time, this will use the class name as the enumeration value
     *
     * @throws Goliath.Exceptions.InvalidParameterException    if tcValue is not a valid value
     */
    protected DynamicEnum()
    {
        m_cValue = getClass().getName();
        addDynamicEnum(this);
    }
    
    /**
     * Returns the value assigned to the enum
     *
     * @return      the string value
     */
    public String getValue()
    {
        return m_cValue;
    }

    @Override
    protected String formatString(StringFormatType toFormat) {
        return m_cValue;
    }

    
    @Override
    public boolean equals(java.lang.Object toObject)
    {
        return (toObject != null && getClass() == toObject.getClass()
                && (this.m_cValue.equals(((DynamicEnum)toObject).m_cValue)));
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + (this.m_cValue != null ? this.m_cValue.hashCode() : 0);
        return hash;
    }
    
}
