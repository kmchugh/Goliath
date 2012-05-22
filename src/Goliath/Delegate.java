/* =========================================================
 * Delegate.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 10:17:13 PM
 * 
 * Description
 * --------------------------------------------------------
 * Used similar to c# delegates
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.DynamicCode.Java;
import Goliath.DynamicCode.Java.MethodDefinition;
import Goliath.Exceptions.InvalidParameterException;
import Goliath.Interfaces.IDelegate;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Used similar to c# delegates, allows treating methods
 * as first class objects.  This does use reflection so
 * shouldn't be used in tight loops.
 * Based on 
 * http://www.onjava.com/pub/a/onjava/2003/05/21/delegates.html
 *
 * @param <T>     The return type for the delegate
 * @version     1.0 Jan 9, 2008
 * @author      Ken McHugh
**/
public class Delegate<T> extends Goliath.Object
{
    
    /**
     * This class is returned by the build method and is the actual proxy to the method
     * @param <T> The return type for the delegate
     */
    protected static class DelegateProxy<T> extends Goliath.Object
            implements Goliath.Interfaces.IDelegate, java.lang.reflect.InvocationHandler
    {
        private final java.lang.reflect.Method m_oMethod;
        private final java.lang.Object m_oTarget;
        private PropertySet m_oPropertySet;

        public void setPropertySet(PropertySet toProperties)
        {
            m_oPropertySet = toProperties;
        }
        
        /**
        * Creates a DelegateProxy for calling the specified method when invoked
        * @param toClass The class to call the method from
        * @param toTarget The target object to call the method on
        * @param tcMethodName the name of the method to call
        * @param toReturnType the type to be returned (Should match T)
        * @param toTemplate the template with the arguments
        * @throws Goliath.Exceptions.InvalidParameterException when the method is not found
        */
        protected DelegateProxy(java.lang.Class toClass, java.lang.Object toTarget, String tcMethodName, Delegate toTemplate)
                throws Goliath.Exceptions.InvalidParameterException
        {
            this(toTarget, Goliath.DynamicCode.Java.getMethod(toClass, tcMethodName, toTemplate.getParameterTypes()));
        }

        /**
        * Creates a DelegateProxy for calling the specified method when invoked
        * @param toClass The class to call the method from
        * @param toTarget The target object to call the method on
        * @param tcMethodName the name of the method to call
        * @param toReturnType the type to be returned (Should match T)
        * @param toTemplate the template with the arguments
        * @throws Goliath.Exceptions.InvalidParameterException when the method is not found
        */
        protected DelegateProxy(java.lang.Object toTarget, Method toMethod)
                throws Goliath.Exceptions.InvalidParameterException
        {
            m_oTarget = toTarget;
            m_oMethod = toMethod;
            m_oPropertySet = null;

            // Allow the method to be invokable by any classes, bypassing normal access checks
            m_oMethod.setAccessible(true);
        }

        @Override
        public T invoke(java.lang.Object toProxy, Method tcMethod, java.lang.Object... taArgs) 
                throws Throwable 
        {
            java.lang.Object[] loObject = null;
            if (taArgs.length == 1 && m_oPropertySet == null)
            {
                loObject = (java.lang.Object[])taArgs[0];
            }
            else if (taArgs.length == 1 && m_oPropertySet != null)
            {
                loObject = new java.lang.Object[2];
                loObject[0] = taArgs[0];
                loObject[1] = m_oPropertySet;
            }
            return invoke(loObject);
        }
        
        @Override
        public T invoke(java.lang.Object... taArgs)
                throws Goliath.Exceptions.InvalidOperationException
        {
            try
            {
                java.lang.Object loReturn = null;
                // TODO: May need to do something different for static methods.  Need to test that

                if (m_oPropertySet != null)
                {
                    List<java.lang.Object> loList = new List<java.lang.Object>(taArgs);
                    loList.add(m_oPropertySet);
                    loReturn = m_oMethod.invoke(m_oTarget, loList.toArray());
                }
                else
                {
                    loReturn = m_oMethod.invoke(m_oTarget, taArgs);
                }
                
                return (T)loReturn;
            }
            catch(InvocationTargetException ex)
            {
                Application.getInstance().log(ex.getCause());

                StringBuilder loBuilder = new StringBuilder("Call to invoke failed for method ");
                loBuilder.append(m_oMethod.toGenericString());

                throw new Goliath.Exceptions.InvalidOperationException(loBuilder.toString(), ex);
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
                StringBuilder loBuilder = new StringBuilder("Call to invoke failed for method ");
                loBuilder.append(m_oMethod.toGenericString());
                loBuilder.append("\npassed parameters [");

                // TODO : Find out if this is displaying the correct generic type

                for (int i=0; i<taArgs.length; i++)
                {
                    Class loClass = taArgs[i] == null ? null : taArgs[i].getClass();
                    loBuilder.append(loClass == null ? "null" : loClass.getName());
                    if (loClass.getTypeParameters().length > 0)
                    {
                        loBuilder.append("<");
                        loBuilder.append(loClass.getGenericSuperclass().toString());
                        loBuilder.append(">");
                    }
                    loBuilder.append("\n");
                }
                loBuilder.append("]");

                throw new Goliath.Exceptions.InvalidOperationException(loBuilder.toString(), ex);
            }
        }
    }
    
    
    private final Class m_oReturn;
    private final Class[] m_aParameters;
    
    /**
     * Creates a new instance of Delegate 
     *
     * @param  taParams the list of parameter types
     * @param  toReturn the return value type
     */
    public Delegate(Class[] taParams, Class toReturn)
    {
        m_oReturn = toReturn;
        m_aParameters = taParams;
    }
    
     /**
     * Creates a new instance of Delegate 
     *
     * @param  taParams the list of parameter types
     */
    public Delegate(Class[] taParams)
    {
        this(taParams, null);
    }
    
    /**
     * Creates a new instance of Delegate 
     */
    public Delegate()
    {
        this(null, null);
    }
    
    /**
     * Gets the type that will be returned when the delegate is invoked
     * @return the class to return
     */
    public Class getReturnType()
    {
        return m_oReturn;
    }
    
    /**
     * Gets the parameters that are to be passed to invoke
     * @return an array of parameter types that should be passed to invoke
     */
    public Class[] getParameterTypes()
    {
        return m_aParameters;
    }
    
    /**
     * Builds a delegate proxy for executing the method
     * @param toClass   the class to run the method from
     * @param toTarget  the target that the method will be run from
     * @param tcMethodName the name of the method to run
     * @return a new IDelegate for the method
     * @throws Goliath.Exceptions.ObjectNotCreatedException if the delegate could not be created
     */
    public Goliath.Interfaces.IDelegate<T> build(java.lang.Class toClass, java.lang.Object toTarget, String tcMethodName)
            throws Goliath.Exceptions.ObjectNotCreatedException
    {
        try
        {
            DelegateProxy loDelegate = new DelegateProxy(toClass, toTarget, tcMethodName, this);
            Goliath.Interfaces.IDelegate<T> loReturn = (Goliath.Interfaces.IDelegate<T>)java.lang.reflect.Proxy.newProxyInstance(
                    toClass.getClassLoader(), 
                    new Class[] {Goliath.Interfaces.IDelegate.class},
                    loDelegate);


            return loReturn;
        }
        catch (Exception ex)
        {
            throw new Goliath.Exceptions.ObjectNotCreatedException("Unable to create delegate", ex);                        
        }
    }

    /**
     * Turns a function into a delegate.  A delegate can then be treated as a first order object
     * @param <T>
     * @param toTarget The "this" object.  The method will run using "this" as the target
     * @param tcMethodName The name of the method to callback
     * @return the Delegate that can then be invoked
     */
    public static <T extends java.lang.Object> IDelegate<T> build(java.lang.Object toTarget, String tcMethodName, PropertySet toProperties)
    {
        IDelegate<T> loDelegate = build(toTarget, tcMethodName);
        if (loDelegate != null)
        {
            ((DelegateProxy)loDelegate).setPropertySet(toProperties);
        }
        return loDelegate;
    }

    /**
     * Turns a function into a delegate.  A delegate can then be treated as a first order object
     * @param <T>
     * @param toTarget The "this" object.  The method will run using "this" as the target
     * @param tcMethodName The name of the method to callback
     * @return the Delegate that can then be invoked
     */
    public static <T extends java.lang.Object> IDelegate<T> build(java.lang.Object toTarget, String tcMethodName)
    {
        Goliath.Utilities.checkParameterNotNull("toTarget", toTarget);
        Goliath.Utilities.checkParameterNotNull("tcMethodName", tcMethodName);
        
        try
        {
            Class loClass = toTarget.getClass();

            // First attempt to get the method from the class
            MethodDefinition loDefinition = Java.getMethodDefinition(loClass, tcMethodName);
            if (loDefinition == null)
            {
                throw new InvalidParameterException("The method '" + tcMethodName + "' could not be used as an event callback because it was not found found in the class " + loClass.getSimpleName(), "toObject");
            }
            List<AccessibleObject> loMethods = loDefinition.getFunctions();
            Method loMethod = null;
            if (loMethods.size() > 1)
            {
                int lnDistance = 99999;
                for (AccessibleObject loObject : loMethods)
                {
                    Method loAccessibleMethod = (Method)loObject;
                    int lnPropinquity = Java.getPropinquity(loClass, loAccessibleMethod.getDeclaringClass());
                    if (lnPropinquity < lnDistance)
                    {
                        lnDistance = lnPropinquity;
                        loMethod = (Method)loObject;
                    }
                }
            }
            else
            {
                loMethod = (Method)loMethods.get(0);
            }
            return new DelegateProxy(toTarget, loMethod);
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return null;
    }
}
