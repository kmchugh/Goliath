/* =========================================================
 * ObjectCache.java
 *
 * Author:      kmchugh
 * Created:     23-Apr-2008, 08:58:44
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

package Goliath;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.LogType;
import Goliath.DynamicCode.Java;
import Goliath.Interfaces.Collections.IList;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to cache collections of arbitary objec;ts loaded from .jar files
 * All objects loaded must have a default constructor
 * All classes must be found in the classpath
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 23-Apr-2008
 * @author      kmchugh
**/
public class ObjectCache extends Goliath.Object 
{
    private ConcurrentHashMap<Class, HashTable<String, List<?>>> m_oCache = new ConcurrentHashMap<Class, HashTable<String, List<?>>>();
    private ConcurrentHashMap<Class, IList<Class<?>>> m_oClassCache = new ConcurrentHashMap<Class, IList<Class<?>>>();
    private ConcurrentHashMap<Class, String> m_oKeyMethods = new ConcurrentHashMap<Class, String>();

    /** Creates a new instance of ObjectCache */
    public ObjectCache()
    {
    }
    
    public <T> void loadClasses(Class<T> toClass)
    {
        // If there are classes of this type, we don't want to reload.
        if (m_oClassCache.containsKey(toClass) && m_oClassCache.get(toClass).size() > 0)
        {
            return;
        }
        
        if (!m_oClassCache.containsKey(toClass))
        {
            IList<Class<T>> loClass = Java.getClasses(toClass, (Class[])null);

            synchronized(m_oClassCache)
            {
                // The compiler is making us do it this way
                m_oClassCache.put(toClass, new List<Class<?>>(loClass.size()));
                m_oClassCache.get(toClass).addAll(loClass);
            }
        }
    }
    
    public <T> void loadObjects(Class<T> toClass, String tcGetKeyMethod)
    {
        // If there are classes of this type, we don't want to reload.
        if (m_oCache.containsKey(toClass) && m_oCache.get(toClass).size() > 0)
        {
            return;
        }
        
        
        // Get all of the classes
        loadClasses(toClass);
        for (Class<?> loClass : getClasses(toClass))
        {
            try
            {
                T loObject = (T)loClass.newInstance();
                String lcKey = null;
                try
                {
                    Method loMethod = loObject.getClass().getMethod(tcGetKeyMethod, (java.lang.Class<?>[])null);
                    java.lang.Object loKey = loMethod.invoke(loObject, (java.lang.Object[])null);
                    // Do not attempt to enter objects with null keys
                    if (loKey != null)
                    {
                        lcKey = loKey.toString().toLowerCase();
                        Application.getInstance().log("Loaded Object " + toClass.getSimpleName() + "[" + lcKey + "]", LogType.TRACE());
                        putObject(toClass, lcKey, loObject);
                    }
                }
                catch(NoSuchMethodException ex)
                {
                    new Goliath.Exceptions.Exception("Could not execute method " + tcGetKeyMethod + "() on " + loClass.getSimpleName(), ex);                        
                }
                catch(SecurityException ex)
                {
                    new Goliath.Exceptions.Exception("Security exception retrieving method " + tcGetKeyMethod + " on " + loClass.getSimpleName(), ex);                        
                }
                catch(Throwable ex)
                {
                    new Goliath.Exceptions.Exception("Other exception in method " + tcGetKeyMethod + " on " + loClass.getSimpleName(), ex);                        
                }
            }
            catch (Exception e)
            {
                new Goliath.Exceptions.Exception("Could not get cached object " + loClass.getSimpleName(), e);
            }
            catch(Throwable ex)
            {
                new Goliath.Exceptions.Exception("Other exception on cached object " + loClass.getSimpleName(), ex);                        
            }
        }
    }

    public <T> void clearClass(Class<T> toClass)
    {
        if (!m_oClassCache.containsKey(toClass))
        {
            return;
        }
        for (Class loClass : m_oClassCache.keySet())
        {
            if (loClass.isAssignableFrom(toClass))
            {
                synchronized (m_oClassCache)
                {
                    m_oClassCache.get(loClass).remove(toClass);
                    if (m_oClassCache.get(loClass).size() == 0)
                    {
                        m_oClassCache.remove(loClass);
                        break;
                    }
                }
            }
        }
    }
    
    public <T> void clearObject(Class<T> toClass, String tcKey)
    {
        if (!m_oCache.containsKey(toClass))
        {
            return;
        }
        synchronized (m_oCache)
        {
            m_oCache.get(toClass).remove(tcKey);

            if (m_oCache.get(toClass).size() == 0)
            {
                m_oCache.remove(toClass);
            }
        }
    }
    
    public synchronized <T> void putClass(Class<T> toInterface, Class toClass)
    {
        if (!m_oClassCache.containsKey(toInterface))
        {
            m_oClassCache.put(toInterface, new List<Class<?>>());
        }
        if (!m_oClassCache.get(toInterface).contains(toClass))
        {
            m_oClassCache.get(toInterface).add(toClass);
        }
    }
    
    public synchronized <T> void putObject(Class<T> toClass, String tcKey, T toObject)
    {
        if (!m_oCache.containsKey(toClass))
        {
            m_oCache.put(toClass, new Goliath.Collections.HashTable<String, List<?>>());
        }
        if (!m_oCache.get(toClass).containsKey(tcKey))
        {
            m_oCache.get(toClass).put(tcKey, new List<T>());
        }
        if (!m_oCache.get(toClass).get(tcKey).contains(toObject))
        {
            // Doing it this way because of the generics capture
            List<T> loList = (List<T>)m_oCache.get(toClass).get(tcKey);
            loList.add(toObject);
        }
    }
    
    public <T> Goliath.Collections.List<Class<T>> getClasses(Class<T> toClass)
    {
        loadClasses(toClass);
        List<Class<T>> loReturn = new List<Class<T>>(30);
        if (m_oClassCache.containsKey(toClass))
        {
            synchronized(m_oClassCache)
            {
                for (Class<?> loClass : m_oClassCache.get(toClass))
                {
                    if (!loClass.isInterface() && !Java.isAbstract(loClass))
                    {
                        loReturn.add((Class<T>)loClass);
                    }
                }
            }
        }
        return loReturn;
    }
    
    /**
     * Gets the objects from the cache, if the cache contains no objects of this type, then attempts to load the objects as well
     * @param <T> the return type
     * @param toClass the class to load
     * @param tcKey the name of the method to use as the key
     * @return a list of objects of the specified type
     */
    public synchronized <T> Goliath.Collections.List<T> getObjects(Class<T> toClass, String tcKey)
    {
        if (!m_oCache.containsKey(toClass))
        {
            loadObjects(toClass, tcKey);
        }
        return getObjects(toClass);
    }
    
    public synchronized <T> Goliath.Collections.List<T> getObjects(Class<T> toClass)
    {
        if (!m_oCache.containsKey(toClass))
        {
            refreshCache(toClass);
        }
        
        Goliath.Collections.List<T> loReturn = new Goliath.Collections.List<T>();
        if (m_oCache.containsKey(toClass))
        {
            for (List<?> loList : m_oCache.get(toClass).values())
            {
                for (java.lang.Object loObject : loList)
                {
                    if (!loReturn.contains(loObject))
                    {
                        loReturn.add((T)loObject);                
                    }
                }
            }
        }
        return loReturn;
    }

    /**
     * Gets the list of objects that have been loaded for this key
     * @param <T>
     * @param toClass
     * @param tcKey
     * @return
     */
    public synchronized <T> List<T> getObject(Class<T> toClass, String tcKey)
    {
        tcKey = tcKey.toLowerCase();
        List<T> loReturn = null;
        if (m_oCache.containsKey(toClass))
        {
            if (m_oCache.get(toClass).containsKey(tcKey))
            {
                loReturn = (List<T>)m_oCache.get(toClass).get(tcKey);
            }        
        }
        return loReturn;
    }
    
    public synchronized void clearCache()
    {
        m_oClassCache.clear();
        m_oCache.clear();        
    }
    
    public synchronized <T> void clearCache(Class<T> toClass)
    {
        m_oClassCache.remove(toClass);
        m_oCache.remove(toClass);
    }
    
    
    public synchronized void refreshCache()
    {
        clearCache();
        for (Class loClass : m_oKeyMethods.keySet())
        {
            loadObjects(loClass, m_oKeyMethods.get(loClass));
        }
    }
    
    public synchronized <T> void refreshCache(Class<T> toClass)
    {
        clearCache(toClass);
        if (m_oKeyMethods.containsKey(toClass))
        {
            loadObjects(toClass, m_oKeyMethods.get(toClass));
        }
    }
    
     /**
     * Gets the count of the classes loaded into the Hash Table
     *
     *  @return      The integer count representing the number of classes in hash table
     */
    public  int getClassCount()
    {
        return m_oClassCache.size();
    }
    
    
    
     /**
     * Gets the count of the objects loaded into the Hash Table for a given class
     *
      *  @param <T> 
      * @param  toClass , the class to obtain the object count
     *  @return      The integer count representing the number of classes in hash table
     */
      public <T> int getObjectCount(Class<T> toClass)
    {
        if (!m_oCache.containsKey(toClass))
        {
            return 0;
        }
        return m_oCache.get(toClass).size();
             
    }
      
     /**
     * Gets an eumeration of keys in hash table for a given class
     *
      *  @param <T> 
      * @param  toClass , the class to return hash keys
     *  @return      enumeration of keys for class objects
     */   
      public <T> Enumeration<String> getKeys(Class<T> toClass)
      {
           if (!m_oCache.containsKey(toClass))
        {
            return null;
        }
          return m_oCache.get(toClass).keys();
      }
      
      public <T> boolean containsClass(Class<T> toClass)
      {
          return m_oClassCache.containsKey(toClass);
      }
}
