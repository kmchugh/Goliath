/* ========================================================
 * KeyedObjectCache.java
 *
 * Author:      kmchugh
 * Created:     Aug 1, 2010, 2:15:22 PM
 *
 * Description
 * --------------------------------------------------------
 * Allows storage of objects and retrieval by key or property
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Collections.Tuple;
import java.util.concurrent.ConcurrentHashMap;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 1, 2010
 * @author      kmchugh
**/
public class KeyedObjectCache<T extends java.lang.Object>
        extends Goliath.Object
{
    ConcurrentHashMap<Class<T>, List<T>> m_oItems;
    ConcurrentHashMap<Class<T>, ConcurrentHashMap<String, HashTable<java.lang.Object, T>>> m_oKeyedItems;

    /**
     * Creates a new instance of KeyedObjectCache
     */
    public KeyedObjectCache()
    {
    }
    
    public final boolean add(Class<T> toClass, T toObject)
    {
        if (canAddObject(toObject) ? addToItems(toClass, toObject) : false)
        {
            afterObjectAdded(toObject);
            return true;
        }
        return false;
    }
    
    public final boolean add(Class<T> toClass, String tcKey, java.lang.Object toValue, T toObject)
    {
        boolean llContained = contains(toObject);
        if (llContained || canAddObject(toObject))
        {
            if (!llContained)
            {
                addToItems(toClass, toObject);
            }
            else
            {
                if (!canAddObject(toObject))
                {
                    return false;
                }
            }
            if (addToKeyedItems(tcKey, toValue, toClass, toObject))
            {
                afterObjectAdded(toObject);
                return true;
            }
        }
        return false;
    }

    public final boolean contains(T toObject)
    {
        if (m_oItems == null || m_oItems.isEmpty())
        {
            return false;
        }

        Class loStorageClass = toObject.getClass();
        for (Class loClass : m_oItems.keySet())
        {
            if (Goliath.DynamicCode.Java.isEqualOrAssignable(loClass, loStorageClass))
            {
                if (m_oItems.get(loClass).contains(toObject))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean contains(Class<T> toClass, T toObject)
    {
        return (m_oItems == null || !m_oItems.containsKey(toClass)) ? false : m_oItems.get(toClass).contains(toObject);
    }

    public final boolean contains(Class<T> toClass, String tcKey, java.lang.Object toValue)
    {
        return (m_oKeyedItems == null || 
                !m_oKeyedItems.containsKey(toClass) ||
                !m_oKeyedItems.get(toClass).containsKey(tcKey)) ? false : m_oKeyedItems.get(toClass).get(tcKey).containsKey(toValue);
    }

    public final boolean remove(T toObject)
    {
        if (m_oItems == null || !m_oItems.isEmpty())
        {
            return false;
        }

        boolean llReturn = removeFromItems(toObject);
        if (llReturn && m_oKeyedItems != null)
        {
            HashTable<Class<T>, List<Tuple<String, java.lang.Object>>> loRemovals = findKeyedObjects(toObject);
            if (loRemovals.size() > 0)
            {
                for (Class<T> loClass : loRemovals.keySet())
                {
                    for (Tuple<String, java.lang.Object> loRemove : loRemovals.get(loClass))
                    {
                        removeFromKeyedItems(loRemove.getHead(), loRemove.getFromTail(0));
                    }
                }
            }
        }
        return llReturn;
    }

    public final boolean clear()
    {
        if (m_oItems != null)
        {
            m_oItems = null;
            m_oKeyedItems = null;
            return true;
        }
        return false;
    }

    public final boolean clear(Class<T> toClass)
    {
        if (m_oItems != null)
        {
            m_oItems.remove(toClass);
            if (m_oKeyedItems != null)
            {
                m_oKeyedItems.remove(toClass);
            }
            return true;
        }
        return false;
    }

    public final T getObjectForKey(Class<T> toClass, String tcKey, java.lang.Object toValue)
    {
        return (m_oKeyedItems == null || !m_oKeyedItems.containsKey(toClass) || !m_oKeyedItems.get(toClass).containsKey(tcKey)) ? null : m_oKeyedItems.get(toClass).get(tcKey).get(toValue);
    }

    public final List<T> getObjectsWithProperty(Class<T> toClass, String tcProperty, java.lang.Object toValue)
    {
        return getObjectsWithProperty(toClass, tcProperty, toValue, 99999);
    }


    public final List<T> getObjectsWithProperty(Class<T> toClass, String tcProperty, java.lang.Object toValue, int tnMaxCount)
    {
        List<T> loReturn = new List<T>();

        if (m_oItems != null && m_oItems.containsKey(toClass))
        {
            for (T loObject : m_oItems.get(toClass))
            {
                try
                {
                    java.lang.Object loValue = Goliath.DynamicCode.Java.getPropertyValue(loObject, tcProperty);
                    if ((toValue != null && toValue.equals(loValue)) || (toValue == null && loValue == null))
                    {
                        loReturn.add(loObject);
                        if (loReturn.size() >= tnMaxCount)
                        {
                            break;
                        }
                    }
                }
                catch (Throwable ex)
                {}
            }
        }
        return loReturn;
    }

    public final List<T> getObjectsWithProperties(Class<T> toClass, PropertySet toProperties)
    {
        if (m_oItems != null && m_oItems.containsKey(toClass))
        {
            List<T> loReturn = new List<T>(m_oItems.get(toClass));
            List<T> loRemove = new List<T>();

            for (String lcProperty: toProperties.getPropertyKeys())
            {
                java.lang.Object loPropertyValue = toProperties.getProperty(lcProperty);

                for (T loObject : m_oItems.get(toClass))
                {
                    try
                    {
                        java.lang.Object loValue = Goliath.DynamicCode.Java.getPropertyValue(loObject, lcProperty);
                        if ((loPropertyValue != null && !loPropertyValue.equals(loValue)) || loPropertyValue == null && loValue != null)
                        {
                            loRemove.add(loObject);
                        }
                    }
                    catch (Throwable ex)
                    {
                        loRemove.add(loObject);
                    }
                }

                for (T loObject : loRemove)
                {
                    loReturn.remove(loObject);
                }
                loRemove.clear();
            }
            return loReturn;
        }
        return new List<T>(0);
    }

    protected  boolean canAddObject(T toObject)
    {
        return true;
    }

    protected void afterObjectAdded(T toObject)
    {
    }





    private boolean addToKeyedItems(String tcKey, java.lang.Object toValue, Class<T> toClass, T toObject)
    {
        if (m_oKeyedItems == null)
        {
            m_oKeyedItems = new ConcurrentHashMap<Class<T>, ConcurrentHashMap<String, HashTable<java.lang.Object, T>>>();
        }

        if (!m_oKeyedItems.containsKey(toClass))
        {
            m_oKeyedItems.put(toClass, new ConcurrentHashMap<String, HashTable<java.lang.Object, T>>());
        }

        if (!m_oKeyedItems.get(toClass).containsKey(tcKey))
        {
            m_oKeyedItems.get(toClass).put(tcKey, new HashTable<java.lang.Object, T>());
        }

        if (!m_oKeyedItems.get(toClass).get(tcKey).containsKey(toValue))
        {
            return m_oKeyedItems.get(toClass).get(tcKey).put(toValue, toObject) == null;
        }
        return false;
    }





    private boolean addToItems(Class<T> toClass, T toObject)
    {
        if (m_oItems == null)
        {
            m_oItems = new ConcurrentHashMap<Class<T>, List<T>>();
        }

        if (!m_oItems.containsKey(toClass))
        {
            m_oItems.put(toClass, new List<T>());
        }
        List<T> loItems = m_oItems.get(toClass);
        if (!loItems.contains(toObject))
        {
            boolean llReturn = loItems.add(toObject);
            if (llReturn)
            {
                afterObjectAdded(toObject);
            }
            return llReturn;
        }
        return false;
    }

    

    

    

    

    private HashTable<Class<T>, List<Tuple<String, java.lang.Object>>> findKeyedObjects(T toObject)
    {
        HashTable<Class<T>, List<Tuple<String, java.lang.Object>>> loReturn = new HashTable<Class<T>, List<Tuple<String, java.lang.Object>>>();
        if (m_oKeyedItems != null)
        {
            // Search the keyed items to remove the object
            for (Class loClass: m_oKeyedItems.keySet())
            {
                for (String lcKey: m_oKeyedItems.get(loClass).keySet())
                {
                    HashTable<java.lang.Object, T> loObjects = m_oKeyedItems.get(loClass).get(lcKey);
                    for (java.lang.Object loKey : loObjects.keySet())
                    {
                        if (loObjects.get(loKey).equals(toObject))
                        {
                            loReturn.put(loClass, new List<Tuple<String, java.lang.Object>>());
                            loReturn.get(loClass).add(new Tuple<String, java.lang.Object>(lcKey, loKey));
                        }
                    }
                }
            }
        }
        return loReturn;
    }

    private boolean removeFromItems(T toObject)
    {
        boolean llReturn = false;
        if (m_oItems != null)
        {
            List<Class> loClassList = new List(m_oItems.keySet());
            for (Class loClass: loClassList)
            {
                llReturn = m_oItems.get(loClass).remove(toObject) || llReturn;
                if (m_oItems.get(loClass).isEmpty())
                {
                    m_oItems.remove(loClass);
                }
            }
            if (m_oItems.isEmpty())
            {
                m_oItems = null;
            }
        }
        return llReturn;
    }
    
    private boolean removeFromKeyedItems(String tcKey, java.lang.Object toValue)
    {
        boolean llReturn = false;
        if (m_oKeyedItems != null)
        {
            List<Class> loClassList = new List(m_oKeyedItems.keySet());
            for (Class loClass: loClassList)
            {
                if (m_oKeyedItems.get(loClass).containsKey(tcKey) && m_oKeyedItems.get(loClass).get(tcKey).contains(toValue))
                {
                    llReturn = m_oKeyedItems.get(loClass).get(tcKey).remove(toValue) != null || llReturn;
                    if (m_oKeyedItems.get(loClass).get(tcKey).isEmpty())
                    {
                        m_oKeyedItems.get(loClass).remove(tcKey);
                        if (m_oKeyedItems.get(loClass).isEmpty())
                        {
                            m_oKeyedItems.remove(loClass);
                        }
                    }
                }
            }
            if (m_oKeyedItems.isEmpty())
            {
                m_oKeyedItems = null;
            }
        }
        return llReturn;
    }
    
}
