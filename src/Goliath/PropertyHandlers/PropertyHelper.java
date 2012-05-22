/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.PropertyHandlers;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Interfaces.PropertyHandlers.IPropertyHelper;
import Goliath.Interfaces.PropertyHandlers.IPropertyIOBehaviour;
import Goliath.PropertyHandlers.PropertyHelpers.DefaultPropertyHelper;

/**
 *
 * @author kenmchugh
 */
public abstract class PropertyHelper extends Goliath.Object
        implements IPropertyHelper
{
    
    private static HashTable<Class<? extends IPropertyIOBehaviour>, HashTable<Class, IPropertyHelper>> g_oHelpers = new HashTable<Class<? extends IPropertyIOBehaviour>, HashTable<Class, IPropertyHelper>>();
    
    public static void loadHelpers()
    {
        if (g_oHelpers.size() == 0)
        {
            List<Class<IPropertyHelper>> loHelpers = Application.getInstance().getObjectCache().getClasses(IPropertyHelper.class);
            for (Class<IPropertyHelper> loHelperClass : loHelpers)
            {
                try
                {
                    IPropertyHelper loHelper = loHelperClass.newInstance();
                    if (!g_oHelpers.containsKey(loHelper.getBehaviour()))
                    {
                        g_oHelpers.put(loHelper.getBehaviour(), new HashTable<Class, IPropertyHelper>());
                    }
                    g_oHelpers.get(loHelper.getBehaviour()).put(loHelper.getObjectType(), loHelper);
                }
                catch (Throwable ignore)
                {}
            }
        }
    }
    
    public static IPropertyHelper create(IPropertyIOBehaviour toBehaviour, java.lang.Object toValue)
    {
        return create(toBehaviour.getClass(), toValue);
    }
    
    public static IPropertyHelper create(Class<? extends IPropertyIOBehaviour> toBehaviour, java.lang.Object toValue)
    {
        Class loCheckClass = null;
        if (toValue.getClass() == Class.class)
        {
            loCheckClass = (Class)toValue;
        }
        else
        {
            loCheckClass = toValue.getClass();
        }
        // Make sure the helpers are loaded
        loadHelpers();
        
        if (g_oHelpers.containsKey(toBehaviour))
        {
            if (g_oHelpers.get(toBehaviour).containsKey(loCheckClass))
            {
                // Found a helper, so return it
                return g_oHelpers.get(toBehaviour).get(loCheckClass);
            }
            else
            {
                Class loUseClass = null;
                
                // We have not found a class, so loop through all available and see if one is applicable
                for (Class loClass : g_oHelpers.get(toBehaviour).keySet())
                {
                    if (loClass.isAssignableFrom(loCheckClass) && loClass != java.lang.Object.class)
                    {
                        if (loUseClass == null || loUseClass.isAssignableFrom(loClass))
                        {
                            loUseClass = loClass;
                        }
                    }
                }
                if (loUseClass == null)
                {
                    loUseClass = DefaultPropertyHelper.class;
                }
                else
                {
                    loUseClass = g_oHelpers.get(toBehaviour).get(loUseClass).getClass();
                }
                IPropertyHelper loReturn = null;
                try
                {
                    loReturn = (IPropertyHelper)loUseClass.newInstance();
                }
                catch (Throwable ignore)
                {
                    loReturn = new DefaultPropertyHelper();
                }
                g_oHelpers.get(toBehaviour).put(loCheckClass, loReturn);
                return loReturn;
            }
        }
        throw new Goliath.Exceptions.InvalidParameterException("Default Property helper for behaviour " + toBehaviour.getName(), "toBehaviour");
    }
    
    public abstract Class<? extends IPropertyIOBehaviour> getBehaviour();
    
    

}
