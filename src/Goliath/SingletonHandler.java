/* =========================================================
 * SingletonHandler.java
 *
 * Author:      kmchugh
 * Created:     17-Dec-2007, 23:58:59
 * 
 * Description
 * --------------------------------------------------------
 * Class used to manage objects that are meant to be used 
 * as singletons but have not been coded as such
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

/**
 * Class used to manage objects that are meant to be used 
 * as singletons but have not been coded as such.  Classes
 * using this must be creatable by default constructor
 *
 * @version     1.0 17-Dec-2007
 * @author      kmchugh
**/

public class SingletonHandler extends Goliath.Object
{
    private static java.util.Map<Class, java.lang.Object> m_oSingletons = new java.util.HashMap<Class, java.lang.Object>();
    
    
    /**
     * Get the singleton instance of the class type. 
     *
     * @param <T> The type of the object that is created
     * @param  toClass  The type of object to get or create
     * @return a reference to an object of type toClass
     * @throws Goliath.Exceptions.ObjectNotCreatedException when the singleton object could not be created
     */
    public static <T extends java.lang.Object> T getInstance(Class<T> toClass)
            throws Goliath.Exceptions.ObjectNotCreatedException
    {
        return getInstance(toClass, new Object[]{});
    }

    public static <T extends java.lang.Object> T getInstance(Class<T> toClass, java.lang.Object[] toArgs) throws Goliath.Exceptions.ObjectNotCreatedException
    {
        T loReturn = (T)m_oSingletons.get(toClass);
        if (loReturn == null)
        {
            synchronized (toClass.getClass())
            {
                loReturn = Goliath.DynamicCode.Java.createObject(toClass, toArgs);
                m_oSingletons.put(toClass, loReturn);
            }
        }

        // Double lock handling type return
        return loReturn != null ? loReturn : (T)m_oSingletons.get(toClass);
    }
    /** Creates a new instance of SingletonHandler */
    private SingletonHandler()
    {   
    }
}
