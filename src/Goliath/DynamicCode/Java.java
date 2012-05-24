/* =========================================================
 * Java.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This class is used for dynamically writing and running Java code.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * Jan 29, 2008                     Added methods to search in Jar files for classes filtered by Interfaces and 
 * =======================================================*/
package Goliath.DynamicCode;

import Goliath.Applications.Application;
import Goliath.Applications.ApplicationState;
import Goliath.Arguments.SingleParameterArguments;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Collections.Tree;
import Goliath.Commands.LoadClassesFromJarCommand;
import Goliath.Exceptions.InvalidParameterException;
import Goliath.Exceptions.MethodNotFoundException;
import Goliath.Exceptions.ObjectNotCreatedException;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.Collections.ITree;
import Goliath.Interfaces.Commands.ICommand;
import Goliath.Interfaces.DynamicCode.IClassLoader;
import Goliath.Interfaces.IContainedClassIdentifiable;
import Goliath.Utilities;

import java.lang.reflect.*;

import java.io.*;
import java.lang.annotation.Annotation;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

/**
 * This class is used for writing and executing Java code on the fly
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
 **/
public class Java
{
    // A list of all of the classes and their definitions, used for caching to ensure reflection is fast
    private static HashTable<Class, ClassDefinition> g_oClasses;
    private static List<File> g_oJarList;
    private static Tree<Class> g_oClassTree;
    private static HashTable<Class, List<Class>> g_oInterfaces;
    private static List<String> g_oExclusionList;
    // Hashtable with cache of class methods - the key is class which returns a hash table of key method name with a list of functions with this name
    private static HashTable<String, HashTable<String, List<AccessibleObject>>> g_oMethods;

    public static class MethodDefinition
    {
        private Boolean m_lIsProperty;
        private List<AccessibleObject> m_oFunctions;

        private Method m_oMutator;
        private Method m_oAccessor;
        private List<Annotation> m_oAnnotations;
        private Type m_oReturnType;
        private String m_cName;
        private boolean m_lIsConstructor;


        public MethodDefinition(Class toClass, Method toMethod, String tcName)
        {
            m_cName = tcName;
            addFunction(toMethod);
        }

        public MethodDefinition(Class toClass, Constructor toMethod)
        {
            m_cName = toClass.getSimpleName();
            m_lIsConstructor = true;
            addFunction(toMethod);
        }

        /**
         * Gets the name of this method
         * @return the name of the method
         */
        public String getName()
        {
            return m_cName;
        }

        /**
         * Adds a method call to this method defintion
         * @param toMethod the method to add
         */
        public final void addFunction(AccessibleObject toMethod)
        {
            if (m_oFunctions == null)
            {
                m_oFunctions = new List<AccessibleObject>();
            }

            if (!m_oFunctions.contains(toMethod))
            {
                m_oFunctions.add(toMethod);
            }
            resetValues();
        }

        /**
         * Checks if this method is a constructor method
         * @return true if this is a constructor method
         */
        public boolean isConstructor()
        {
            return m_lIsConstructor;
        }

        /**
         * Gets the list of annotations that are on this method
         * @return the list of annotations
         */
        public List<Annotation> getAnnotations()
        {
            if (m_oAnnotations == null)
            {
                m_oAnnotations = new List<Annotation>();
                List<AccessibleObject> loMethods = getFunctions();
                for (AccessibleObject loMethod : loMethods)
                {
                    Annotation[] laAnnotations = loMethod.getAnnotations();
                    for (int i=0, lnLength = laAnnotations.length; i<lnLength; i++)
                    {
                        if (!m_oAnnotations.contains(laAnnotations[i]))
                        {
                            m_oAnnotations.add(laAnnotations[i]);
                        }
                    }
                }
            }
            return m_oAnnotations;
        }

        /**
         * Gets the specified annotation from this class
         * @param toClass the annotation class to get
         * @return the annotation of the specified type, or null if the annotation does not exist
         */
        public <K extends Annotation> K getAnnotation(Class<K> toClass)
        {
            for (Annotation loAnnotation : getAnnotations())
            {
                if (isEqualOrAssignable(toClass, loAnnotation.getClass()))
                {
                    return (K)loAnnotation;
                }
            }
            return null;
        }

        /**
         * Resets any calculated values on this method
         */
        private void resetValues()
        {
            m_lIsProperty = null;
            m_oReturnType = null;
        }

        /**
         * Gets the list of methods in this class
         * @param tlPublic if public should be included
         * @param tlProtected if protected should be included
         * @param tlPrivate if private should be included
         * @return the list of methods for this class
         */
        public List<AccessibleObject> getFunctions()
        {
            return m_oFunctions == null ? new List<AccessibleObject>(0) : m_oFunctions;
        }

        /**
         * Gets all of the functions with the same parameter count as specified
         * @param tnCount the count of parameters
         * @param tlPublic if public should be included
         * @param tlProtected if protected should be included
         * @param tlPrivate if private should be included
         * @return
         */
        public List<AccessibleObject> getFunctionsWithParameterCount(int tnCount)
        {
            List<AccessibleObject> loReturn = new List<AccessibleObject>(m_oFunctions == null ? 0 : 10);
            if (m_oFunctions != null)
            {
                for (AccessibleObject loObject : m_oFunctions)
                {
                    if (isEqualOrAssignable(Method.class, loObject.getClass()))
                    {
                        if (((Method)loObject).getParameterTypes().length == tnCount)
                        {
                            loReturn.add(loObject);
                        }
                    }
                    else if(isEqualOrAssignable(Constructor.class, loObject.getClass()))
                    {
                        if (((Constructor)loObject).getParameterTypes().length == tnCount)
                        {
                            loReturn.add(loObject);
                        }
                    }
                }
            }
            return loReturn;
        }

        /**
         * Gets the return type for this class
         * @return the return type for the class
         */
        public Type getReturnType()
        {
            if (m_oReturnType == null)
            {
                List<AccessibleObject> loMethods = getFunctions();
                for (AccessibleObject loObject : loMethods)
                {
                    if (isEqualOrAssignable(Method.class, loObject.getClass()))
                    {
                        Type loReturnType = ((Method)loObject).getGenericReturnType();
                        if (loReturnType != null && loReturnType != void.class)
                        {
                            m_oReturnType = ((Method)loObject).getGenericReturnType();
                            break;
                        }
                    }
                    else if(isEqualOrAssignable(Constructor.class, loObject.getClass()))
                    {
                        m_oReturnType = ((Constructor)loObject).getDeclaringClass();
                        break;
                    }
                }
            }
            return m_oReturnType;
        }

        /**
         * Checks if this method is a property method, this means a method in the form getProperty or setProperty and is public
         * @return true if this method is a property method
         */
        public boolean isProperty()
        {
            if (m_lIsProperty == null)
            {
                boolean llPublic = false;
                String lcName = m_cName.toLowerCase();
                if ((!lcName.equals("class") && !lcName.equals("clone")) && getAnnotation(Goliath.Annotations.NotProperty.class) == null)
                {
                    for (AccessibleObject loMethod : getFunctions())
                    {
                        if (isEqualOrAssignable(Method.class, loMethod.getClass()))
                        {
                            Method loActualMethod = (Method)loMethod;

                            llPublic = Modifier.isPublic(loActualMethod.getModifiers()) || llPublic;
                            if (!loActualMethod.isSynthetic())
                            {
                                // TODO: Can optimise call to getParameterTypes
                                if (m_oAccessor == null && loActualMethod.getName().substring(0, 3).equalsIgnoreCase("get") && loActualMethod.getParameterTypes().length == 0)
                                {
                                    m_oAccessor = loActualMethod;
                                }
                                else if (m_oMutator == null && loActualMethod.getName().substring(0, 3).equalsIgnoreCase("set") && loActualMethod.getParameterTypes().length == 1)
                                {
                                    m_oMutator = loActualMethod;
                                }
                                else
                                {
                                    // Do nothing.
                                }
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                m_lIsProperty = llPublic && (m_oAccessor != null || m_oMutator != null);
            }
            return m_lIsProperty;
        }

        /**
         * Gets the accessor method for this property
         * @return the accessor method, or null if there is none
         */
        public Method getAccessor()
        {
            return isProperty() ? m_oAccessor : null;
        }

        /**
         * Gets the mutator method for this property
         * @return the mutator method, or null if there is none
         */
        public Method getMutator()
        {
            return isProperty() ? m_oMutator : null;
        }

        /**
         * Checks if this property can be written to (has a mutator)
         * @return true if this has a mutator
         */
        public boolean canWrite()
        {
            return isProperty() ? m_oMutator != null : false;
        }

        /**
         * Checks if this property can be read from (has an accessor)
         * @return true if this has an accessor
         */
        public boolean canRead()
        {
            return isProperty() ? m_oAccessor != null : false;
        }


    }

    public static class ClassDefinition
    {
        private Class m_oClass;
        private HashTable<String, MethodDefinition> m_oMethods;
        private List<String> m_oProperties;
        private Boolean m_lCanCreate;
        private List<MethodDefinition> m_oConstructors;
        private HashTable<String, Constructor> m_oCachedConstructor;
        private HashTable<Class<? extends Annotation>, List<MethodDefinition>> m_oAnnotationCache;
        private List<Class> m_oInterfaces;

        public ClassDefinition(Class toClass)
        {
            m_oClass = toClass;
        }

        /**
         * Helper function for getting the full list of methods on a class
         * @param toClass the class to get the list of methods
         * @param toMethods the list to populage
         */
        private void createMethodList(Class toClass, List<Method> toMethods)
        {
            for (Method loMethod: toClass.getDeclaredMethods())
            {
                if (!toMethods.contains(loMethod))
                {
                    toMethods.add(0, loMethod);
                }
            }

            Class loSuper = toClass.getSuperclass();
            if (loSuper != null)
            {
                createMethodList(loSuper, toMethods);
            }
        }

        /**
         * Helper function to extract all of the interfaces implemented by this class
         * @param toClass the class to get the interfaces for
         */
        private void extractInterfaces(Class toClass)
        {
            if (m_oInterfaces == null)
            {
                m_oInterfaces = new List<Class>();
            }

            for (Class loClass : toClass.getInterfaces())
            {
                if (!m_oInterfaces.contains(loClass))
                {
                    m_oInterfaces.add(loClass);
                    extractInterfaces(loClass);
                }
            }

            Class loSuper = toClass.getSuperclass();
            if (loSuper != null)
            {
                extractInterfaces(loSuper);
            }
        }

        /**
         * Gets the list of constructors for this class
         * @return the list of constructors for this class
         */
        public List<MethodDefinition> getConstructors()
        {
            if (m_oConstructors == null)
            {
                Constructor[] laConstructors = m_oClass.getDeclaredConstructors();
                m_oConstructors = new List<MethodDefinition>(laConstructors.length);
                for (int i=0, lnLength = laConstructors.length; i<lnLength; i++)
                {
                    m_oConstructors.add(new MethodDefinition(m_oClass, laConstructors[i]));
                }
            }
            return m_oConstructors;
        }

        /**
         * Helper function to parse and load the methods on this class
         */
        private HashTable<String, MethodDefinition> getMethods()
        {
            if (m_oMethods == null)
            {
                // Parse the methods on the class
                List<Method> loMethods = new List<Method>();
                createMethodList(m_oClass, loMethods);
                for (Method loMethod : loMethods)
                {
                    addMethod(loMethod.getName(), loMethod);
                }

                if (m_oMethods == null)
                {
                    m_oMethods = new HashTable<String, MethodDefinition>(0);
                }
            }
            return m_oMethods;
        }

        /**
         * Gets the list of MethodDefinitions that have the specified annotation
         * @param toClass the annotation class
         * @return the list of method definitions
         */
        public List<MethodDefinition> getFunctionsWithAnnotation(Class<? extends Annotation> toClass)
        {
            if (m_oAnnotationCache == null)
            {
                m_oAnnotationCache = new HashTable<Class<? extends Annotation>, List<MethodDefinition>>();
            }

            if (!m_oAnnotationCache.containsKey(toClass))
            {
                m_oAnnotationCache.put(toClass, new List<MethodDefinition>());
                for (MethodDefinition loDefinition : getMethods().values())
                {
                    if (loDefinition.getAnnotation(toClass) != null)
                    {
                        m_oAnnotationCache.get(toClass).add(loDefinition);
                    }
                }
            }
            return m_oAnnotationCache.get(toClass);
        }

        /**
         * Gets the list of interfaces implemented by this class
         * @return the interface list
         */
        public List<Class> getInterfaces()
        {
            if (m_oInterfaces == null)
            {
                // Extract all of the interfaces implemented by this class
                extractInterfaces(m_oClass);
            }
            return m_oInterfaces;
        }

        /**
         * Gets the constructor with a signature that matches.  
         * Passing null will look for the default constructor
         * @param taParams the signature of this constructor
         * @return the constructor specified
         */
        public Constructor getConstructor(Class[] taParams)
        {
            if (m_oCachedConstructor == null)
            {
                m_oCachedConstructor = new HashTable<String, Constructor>();
            }
            if (taParams == null)
            {
                taParams = new Class[0];
            }

            // TODO: We need to also handle contained classes here (When an instance of a class that is declared inside a class is being created)
            
            StringBuilder loBuilder = new StringBuilder();
            // Create the lookup string
            for (Class loClass : taParams)
            {
                loBuilder.append(loClass.getName());
            }
            String lcLookupName = loBuilder.toString().toLowerCase();

            if (!m_oCachedConstructor.containsKey(lcLookupName))
            {
                for (MethodDefinition loDef : getConstructors())
                {
                    List<AccessibleObject> loFunctions = loDef.getFunctionsWithParameterCount(taParams.length);
                    for (AccessibleObject loObject : loFunctions)
                    {

                        if (isEqualOrAssignable(Constructor.class, loObject.getClass()))
                        {
                            if (checkParameters(loObject, taParams))
                            {
                                m_oCachedConstructor.put(lcLookupName, (Constructor)loObject);
                                break;
                            }
                        }
                    }
                }
            }
            return m_oCachedConstructor.get(lcLookupName);
        }


        /**
         * Adds the specified method to this class
         * @param tcName the name of the method
         * @param toMethod the actual method call
         */
        private void addMethod(String tcName, Method toMethod)
        {
            if (m_oMethods == null)
            {
                m_oMethods = new HashTable<String, MethodDefinition>();
            }
            tcName = cleanMethodForSearch(tcName);
            if (!m_oMethods.containsKey(tcName))
            {
                m_oMethods.put(tcName, new MethodDefinition(m_oClass, toMethod, tcName));
            }
            else
            {
                m_oMethods.get(tcName).addFunction(toMethod);
            }
         }

        /**
         * Converts the method name to a friendly name for easy lookup
         * @param tcMethodName the name of the method to search for
         * @return the search friendly method name
         */
        private String cleanMethodForSearch(String tcMethodName)
        {
            tcMethodName = tcMethodName.toLowerCase();
            return (tcMethodName.startsWith("get") || tcMethodName.startsWith("set")) ?
                tcMethodName.substring(3) : tcMethodName;
        }

        /**
         * Gets the list of properties for this class
         * @return the list of properties for this class
         */
        public List<String> getProperties()
        {
            if (m_oProperties == null)
            {
                m_oProperties = new List<String>();
                for (String lcName : getMethods().keySet())
                {
                    MethodDefinition loMethod = m_oMethods.get(lcName);
                    if (loMethod.isProperty() && !m_oProperties.contains(lcName))
                    {
                        m_oProperties.add(lcName);
                    }
                }
            }
            return m_oProperties;
        }

        /**
         * Gets the Method Definition for the specified method on this class
         * @param tcName the name of the method
         * @return the method definition for this method, or null if the method does not exist
         */
        public MethodDefinition getMethod(String tcName)
        {
            return getMethods().get(cleanMethodForSearch(tcName));
        }

        /**
         * Checks if this class is createable, a createable class is one that is
         * not an interface, is not abstract, is not an annotation class, and has
         * a constructor that takes no parameters
         * @return true if this class is createable
         */
        public boolean isCreatable()
        {
            if (m_lCanCreate == null)
            {
                m_lCanCreate = !m_oClass.isInterface() &&
                            !isAbstract(m_oClass) &&
                            !m_oClass.isAnnotation() &&
                            hasConstructor(m_oClass, new Class[]{});
            }
            return m_lCanCreate;
        }




    }

    

    /** Creates a new instance of Java, not publicly creatable */
    private Java()
    {
    }

    public synchronized static boolean isJarLoaded(File toJar)
    {
        return g_oJarList != null && g_oJarList.contains(toJar);
    }

    public synchronized static boolean addProcessedJar(File toJar)
    {
        if (g_oJarList == null)
        {
            g_oJarList = new List<File>();
        }
        if (!g_oJarList.contains(toJar))
        {
            return g_oJarList.add(toJar);
        }
        return false;
    }


    /**
     * Gets the type of the property specified, this will not throw an error if the property is not found,
     * null will be returned instead
     * @param toObject the object to get the property type from
     * @param tcPropertyName the name of the property to get the type of
     * @return the type of this property
     */
    public static Class getPropertyType(java.lang.Object toObject, String tcPropertyName)
    {
        try
        {
            return getPropertyType(toObject.getClass().equals(Class.class) ? (Class)toObject : (Class)toObject.getClass(), tcPropertyName, false);
        }
        catch (Exception ex)
        {
            // Code should never ever get here
            throw new Goliath.Exceptions.CriticalException(ex);
        }
    }

    /**
     * Gets the type of the property specified
     * @param toObject the object to get the property type from
     * @param tcPropertyName the name of the property to get the type of
     * @param tlThrowError marks if an error should be thrown if the property is not found on the class
     * @return the type of this property
     */
    public static Class getPropertyType(java.lang.Object toObject, String tcPropertyName, boolean tlThrowError)
            throws Goliath.Exceptions.InvalidParameterException
    {
        return getPropertyType(toObject.getClass(), tcPropertyName, tlThrowError);
    }

    public static Class getPropertyType(Class toClass, String tcPropertyName, boolean tlThrowError)
            throws Goliath.Exceptions.InvalidParameterException
    {
        MethodDefinition loDefinition = getClassDefinition(toClass).getMethod(tcPropertyName);
        if (loDefinition == null || !loDefinition.isProperty() || loDefinition.getReturnType() == null)
        {
            if (tlThrowError)
            {
                throw new Goliath.Exceptions.InvalidParameterException("The property, " + tcPropertyName + ", was not found on the object type " + toClass.getName(), tcPropertyName);
            }
            else
            {
                return null;
            }
        }
        if (isEqualOrAssignable(Class.class, loDefinition.getReturnType().getClass()))
        {
            return (Class)loDefinition.getReturnType();
        }
        else
        {
            return (Class)((ParameterizedType)loDefinition.getReturnType()).getRawType();
        }
        
    }

    /**
     * Gets a reference to a constructor in the specified class of the specified name, that has the specified
     * parameters
     *
     * @param  toClass  The class to search for the method in
     * @param  taParams     a list of parameter classes
     * @return  returns     a reference to the method
     * @throws Goliath.Exceptions.InvalidParameterException if the method is not found
     */
    public static <T> Constructor<T> getConstructor(Class<T> toClass, Class[] taParams)
            throws Goliath.Exceptions.InvalidParameterException
    {
        return getClassDefinition(toClass).getConstructor(taParams);
    }

    /**
     * Gets the cache of all of the loaded class definitions
     * @param toClass the class definitions
     * @return the list of all the classes that have been loaded
     */
    public static ClassDefinition getClassDefinition(Class toClass)
    {
        if (g_oClasses == null)
        {
            g_oClasses = new HashTable<Class, ClassDefinition>();
        }
        if (!g_oClasses.containsKey(toClass))
        {
            // Load the classes

            try
            {
                // If the class still has not been loaded, then force it here
                ClassDefinition loDefinition = new ClassDefinition(toClass);
                g_oClasses.put(toClass, loDefinition);

                if (g_oClassTree == null)
                {
                    g_oClassTree = new Tree<Class>(java.lang.Object.class);
                }

                ITree loTree = g_oClassTree.getTreeFor(toClass);
                if (loTree == null)
                {
                    loTree = new Tree(toClass);
                    Class loSuperClass = toClass.getSuperclass();
                    if (loSuperClass != null)
                    {
                        getClassDefinition(loSuperClass);
                        g_oClassTree.getTreeFor(loSuperClass).add(loTree);
                    }
                }

                if (g_oInterfaces == null)
                {
                    g_oInterfaces = new HashTable<Class, List<Class>>();
                }

                for (Class loInterface : loDefinition.getInterfaces())
                {
                    if (!g_oInterfaces.containsKey(loInterface))
                    {
                        g_oInterfaces.put(loInterface, new List<Class>());
                    }

                    if (!g_oInterfaces.get(loInterface).contains(toClass))
                    {
                        g_oInterfaces.get(loInterface).add(toClass);
                    }
                }
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        return g_oClasses.get(toClass);
    }

    /**
     * Gets the number of classes that have been loaded in the class list
     * @return the number of loaded classes
     */
    private static int getLoadedClassCount()
    {
        return g_oClasses == null ? 0 : g_oClasses.size();
    }



    /**
     * Checks if a class can be created dynamically with no parameters
     * @param toClass the class to check
     * @return true if the class is creatable
     */
    public static boolean isCreateable(Class toClass)
    {
        return getClassDefinition(toClass).isCreatable();
    }

    /**
     * Gets the accessor method for this specified object, if there is one
     * @param toObject the object to get the accessor for
     * @param tcPropertyName the name of the property to get the accessor for
     * @param tlThrowError true to throw an error if the accessor was not found
     * @return the method used as the accessor
     */
    public static Method getAccessorMethod(java.lang.Object toObject, String tcPropertyName, boolean tlThrowError)
    {
        return getAccessorMethod(isEqualOrAssignable(Class.class, toObject.getClass()) ? (Class)toObject : toObject.getClass(), tcPropertyName, tlThrowError);
    }

    /**
     * Gets the accessor method for this specified object, if there is one
     * @param toClass the class to get the accessor for
     * @param tcPropertyName the name of the property to get the accessor for
     * @param tlThrowError true to throw an error if the accessor was not found
     * @return the method used as the accessor
     */
    public static Method getAccessorMethod(Class toClass, String tcPropertyName, boolean tlThrowError)
    {
        MethodDefinition loMethodDef = getClassDefinition(toClass).getMethod(tcPropertyName);
        if (loMethodDef != null)
        {
            Method loMethod = loMethodDef.getAccessor();
            if (loMethod != null)
            {
                return loMethod;
            }
        }
        // If we got to here, the property was not found

        if (tlThrowError)
        {
            throw new Goliath.Exceptions.InvalidParameterException("The property accessor, " + tcPropertyName + ", was not found on the object type " + toClass.getName(), tcPropertyName, toClass);
        }
        return null;
    }

    /**
     * Gets the mutator method on this object for the specified property
     * @param toObject the object to get the mutator for
     * @param tcPropertyName the name of the property to get the mutator for
     * @param tlThrowError true to throw an error if the accessor was not found
     * @return the mutator method
     */
    public static <T> Method getMutatorMethod(java.lang.Object toObject, String tcPropertyName, boolean tlThrowError)
    {
        return getMutatorMethod(isEqualOrAssignable(Class.class, toObject.getClass()) ? (Class)toObject : toObject.getClass(), tcPropertyName, tlThrowError);
    }

    /**
     * Gets the mutator method on this object for the specified property
     * @param toClass the class to get the mutator for
     * @param tcPropertyName the name of the property to get the mutator for
     * @param tlThrowError true to throw an error if the accessor was not found
     * @return the mutator method
     */
    public static <T> Method getMutatorMethod(Class toClass, String tcPropertyName, boolean tlThrowError)
    {
        MethodDefinition loMethodDef = getClassDefinition(toClass).getMethod(tcPropertyName);
        if (loMethodDef != null)
        {
            Method loMethod = loMethodDef.getMutator();
            if (loMethod != null)
            {
                return loMethod;
            }
        }
        // If we got to here, the property was not found
        
        if (tlThrowError)
        {
            throw new Goliath.Exceptions.InvalidParameterException("The property mutator, " + tcPropertyName + ", was not found on the object type " + toClass.getName(), tcPropertyName, toClass);
        }
        return null;
    }

    /**
     * Gets the value of a property from the object specified.  If the property is not found, the code
     * will attempt to find a similar property with the prefix "get" so for example if "Name" is not found, the
     * code will then attempt "getName" before failing.
     * @param <T> The type of the property
     * @param toObject  the object to get the value from
     * @param tcPropertyName the property to read
     * @return the property value or null if it was not possible to read the property
     */
    public static <T> T getPropertyValue(java.lang.Object toObject, String tcPropertyName)
    {
        try
        {
            return (T) getPropertyValue(toObject, tcPropertyName, false);
        }
        catch (Exception ex)
        {
            // Code should never ever get here
            throw new Goliath.Exceptions.CriticalException(ex);
        }
    }

    /**
     * Gets the value of a property from the object specified.  If the property is not found, the code
     * will attempt to find a similar property with the prefix "get" so for example if "Name" is not found, the
     * code will then attempt "getName" before failing.
     * @param <T> The type of the property
     * @param toObject  the object to get the value from
     * @param tcPropertyName the property to read
     * @param tlThrowError if true then the code will throw an error if the property is not found
     * @return the property value or null if it was not possible to read the property
     * @throws Goliath.Exceptions.InvalidParameterException if the property is not found and tlThrowError is true
     */
    public static <T> T getPropertyValue(java.lang.Object toObject, String tcPropertyName, boolean tlThrowError)
            throws Goliath.Exceptions.InvalidParameterException
    {
        MethodDefinition loDefinition = getClassDefinition(toObject.getClass()).getMethod(tcPropertyName);
        if (loDefinition != null)
        {
            Method loMethod = loDefinition.getAccessor();
            if (loMethod != null)
            {
                try
                {
                    return (T) loMethod.invoke(toObject, new java.lang.Object[]{});
                }
                catch(Throwable ex)
                {
                    Application.getInstance().log("Problem retrieving value from property " + tcPropertyName + " on class " + toObject.getClass().getName());
                    Application.getInstance().log(ex);
                }
            }
        }
        // If we got to there then the accessor was not found, throw an error if needed
        if (tlThrowError)
        {
            throw new InvalidParameterException("The property " + tcPropertyName + " was not found on the object " + toObject.getClass().getName(), "tcPropertyName");
        }
        return null;
    }

    /**
     * Sets the value of a property from the object specified.  If the property is not found, the code
     * will attempt to find a similar property with the prefix "set" so for example if "Name" is not found, the
     * code will then attempt "setName" before failing.
     * @param <T> The type of the property
     * @param toObject  the object to get the value from
     * @param tcPropertyName the property to read
     * @param toValue The new value for the property
     * @return the property value or null if it was not possible to read the property
     */
    public static <T> T setPropertyValue(java.lang.Object toObject, String tcPropertyName, T toValue)
    {
        try
        {
            return (T) setPropertyValue(toObject, tcPropertyName, toValue, false);
        }
        catch (Exception ex)
        {
            // Code should never ever get here
            throw new Goliath.Exceptions.CriticalException(ex);
        }
    }

    /**
     * Sets the value of a property from the object specified.  If the property is not found, the code
     * will attempt to find a similar property with the prefix "set" so for example if "Name" is not found, the
     * code will then attempt "setName" before failing.
     * @param <T> The type of the property
     * @param toObject  the object to get the value from
     * @param tcPropertyName the property to read
     * @param toValue The new value for the property
     * @param tlThrowError if true then the code will throw an error if the property is not found
     * @return the property value or null if it was not possible to read the property
     * @throws Goliath.Exceptions.InvalidParameterException if the property is not found and tlThrowError is true
     */
    public static <T> T setPropertyValue(java.lang.Object toObject, String tcPropertyName, T toValue, boolean tlThrowError)
            throws Goliath.Exceptions.InvalidParameterException
    {
        Method loMethod = getClassDefinition(toObject.getClass()).getMethod(tcPropertyName).getMutator();
        if (loMethod != null)
        {
            try
            {
                if (toValue != null && toValue.getClass() == String.class && loMethod.getParameterTypes()[0] != String.class)
                {
                    return (T) loMethod.invoke(toObject, new java.lang.Object[]{Java.stringToPrimitive((String)toValue, loMethod.getParameterTypes()[0])});
                }
                else
                {
                    return (T) loMethod.invoke(toObject, new java.lang.Object[]{toValue});
                }
            }
            catch(Throwable ex)
            {
                Exception loEx = new Goliath.Exceptions.Exception("Problem setting the value for the property " + tcPropertyName + " on the class " + toObject.getClass().getName(), ex);
                Application.getInstance().log(ex);
            }
        }

        // If we got to here, there was a problem with setting the value, so throw an error if needed
        if (tlThrowError)
        {
            throw new InvalidParameterException("The property " + tcPropertyName + " was not found on the object " + toObject.getClass().getName(), "tcPropertyName");
        }
        return null;
    }

    /**
     * Checks if the provided constructor has the correct parameters
     * @param toConstructor the constructor to check
     * @param taParams the list of parameter classes to check
     * @return true if the constructor parameters match
     */
    public static boolean checkParameters(AccessibleObject toFunction, Class[] taParams)
    {
        Class[] laParameters = (isEqualOrAssignable(Constructor.class, toFunction.getClass())
                ? ((Constructor)toFunction).getParameterTypes() :
                    isEqualOrAssignable(Method.class, toFunction.getClass())
                        ? ((Method)toFunction).getParameterTypes() : null);
        
        // If the size of the parameter arrays don't match then the constructor can't be a match
        if (laParameters.length != ((taParams == null) ? 0 : taParams.length))
        {
            return false;
        }

        for (int i=0; i<laParameters.length; i++)
        {
            if (!isEqualOrAssignable(taParams[i], laParameters[i]))
            {
                return false;
            }
        }

        return true;
    }



    /**
     * Gets the list of methods from the class that are properties
     * @param toClass the class to get the properties for
     * @return the list of property methods
     */
    public static List<String> getPropertyMethods(Class toClass)
    {
        return getClassDefinition(toClass).getProperties();
    }

    /**
     * Gets a reference to a method in the specified class of the specified name, that has the specified return type
     * and parameters
     *
     * @param  toClass  The class to search for the method in
     * @param  tcMethodName  The name of the method to run
     * @param  toReturnType  the type to be returned, can be null
     * @param  taParams     a list of parameter classes
     * @return  returns     a reference to the method
     * @throws Goliath.Exceptions.MethodNotFoundException if the method is not found
     */
    public static Method getMethod(Class toClass, String tcMethodName, Class[] taParams)
    {
        /*
         * Cache handling - put the functions for this class/method into the cache
         */
        // Initialise methods cache if null
        if (g_oMethods == null)
        {
            g_oMethods = new HashTable<String, HashTable<String, List<AccessibleObject>>>();
        }
        // if no entry for this class, then create entries in the cache for this class
        if (!g_oMethods.containsKey(toClass.getName()))
        {
            g_oMethods.put(toClass.getName(), new HashTable<String, List<AccessibleObject>>());
        }
        // if no entry for this method then create entries in the cache for the methods of this class
        if (!g_oMethods.get(toClass.getName()).containsKey(tcMethodName))
        {
            MethodDefinition loDefinition = getClassDefinition(toClass).getMethod(tcMethodName);
            g_oMethods.get(toClass.getName()).put(tcMethodName, loDefinition != null ? loDefinition.getFunctions() : new List<AccessibleObject>(0));
        }

        /*
         * Return the method if found in cache
         */
        for (AccessibleObject loObject : g_oMethods.get(toClass.getName()).get(tcMethodName))
        {
            if (checkParameters(loObject, taParams))
            {
                return (Method) loObject;
            }
        }

        throw new MethodNotFoundException(tcMethodName, taParams);
    }

    /**
     * Returns a list of methods from the class that match the name
     * regardless of the parameters to the method, this is a case insensitive search
     * @param toClass The class to get methods from
     * @param tcMethodName the name of the method to get
     * @param tlIncludePrivate if this is true, then we will also search private methods on this class
     * @return a list of methods that match, or an empty list if none
     */
    public static MethodDefinition getMethodDefinition(Class toClass, String tcMethodName)
    {
        return getClassDefinition(toClass).getMethod(tcMethodName);
    }

    /**
     * Returns a list of methods from the class that have the annotation specified
     * @param <T>
     * @param toClass The class to get methods from
     * @param toAnnotation the Annotation to search for in the methods
     * @return a list of methods that match, or an empty list if none
     */
    public static <T extends Annotation> Goliath.Collections.List<MethodDefinition> getMethodDefinitions(Class toClass, Class<T> toAnnotation)
    {
        return getClassDefinition(toClass).getFunctionsWithAnnotation(toAnnotation);
    }

    /**
     * Gets the annotation if it exists on the property
     * @param <K>
     * @param toObjectClass the object to get the properties for
     * @param tcProperty the specific property to get the annotation from
     * @param toAnnotation the annotation to get
     * @return the annotation if it exists, or null
     */
    public static <K extends Annotation> K getPropertyAnnotation(Class toObjectClass, String tcProperty, Class<K> toAnnotation)
    {
        MethodDefinition loDef = getClassDefinition(toObjectClass).getMethod(tcProperty);
        return loDef != null ? (K)loDef.getAnnotation(toAnnotation) : null;
    }
    
    /**
     * Gets the name of the method that called this method, if tlStrip is true then strips get or set of the 
     * beginning of the method name, so getUserName will become UserName
     * @param tlStrip if true, strips get and set from the beginning of the method name
     * @return the name of the method
     */
    public static String getCallingMethodName(boolean tlStrip)
    {
        return getCallingMethodName(tlStrip, false);
    }

    /**
     * Gets the name of the method that called the method that called this method if tlExtraLevel is true,
     * otherwise gets the name of the method that called this method, 
     * if tlStrip is true then strips get or set of the 
     * beginning of the method name, so getUserName will become UserName
     * @param tlStrip if true, strips get and set from the beginning of the method name
     * @param tlExtraLevel if true, gets the method that called the method that called this method
     * @return the name of the method
     */
    public static String getCallingMethodName(boolean tlStrip, boolean tlExtraLevel)
    {
        java.lang.StackTraceElement[] loStack = Thread.currentThread().getStackTrace();
        int lnLevel = 0;
        while (loStack[lnLevel].getMethodName().equalsIgnoreCase("getStackTrace") || loStack[lnLevel].getMethodName().equalsIgnoreCase("getCallingMethodName"))
        {
            lnLevel++;
        }
        if (tlExtraLevel)
        {
            lnLevel++;
        }
        String lcReturn = (loStack.length > lnLevel) ? loStack[lnLevel].getMethodName() : null;
        return lcReturn == null ? "" : (tlStrip && (lcReturn.indexOf("get") == 0 || lcReturn.indexOf("set") == 0)) ? lcReturn.substring(3) : lcReturn;
    }

    /**
     * Gets the name of the class that called this method, by adjusting tnLevel you can go further up the stack trace,
     * for example tnLevel = 1 would return the name of the class that called the method that called this method.
     * @param tnLevel the number of levels up the stack trace to go in addition
     * @return the name of the class
     */
    public static String getCallingClassName(int tnLevel)
    {
        java.lang.StackTraceElement[] loStack = Thread.currentThread().getStackTrace();
        int lnLevel = 0;
        while (loStack[lnLevel].getMethodName().equalsIgnoreCase("getStackTrace") || loStack[lnLevel].getMethodName().equalsIgnoreCase("getCallingMethodName"))
        {
            lnLevel++;
        }
        lnLevel+=tnLevel;
        return (loStack.length > lnLevel) ? loStack[lnLevel].getClassName() : null;
    }
    
    
    /**
     * Checks if an object is a primitive object or a boxed primitive
     * A String is also considered a primitive
     * @param toObject the object to check
     * @return true if the object is a primitive or a boxed primitive.  False if not, or if the object is null
     */
    public static boolean isPrimitive(java.lang.Object toObject)
    {
        return toObject == null ? false : isPrimitive(toObject.getClass());
    }
    
    /**
     * Checks if a class is a primitive object or a boxed primitive
     * A String is also considered a primitive
     * @param toClass the object to check
     * @return true if the object is a primitive or a boxed primitive.  False if not, or if the object is null
     */
    public static boolean isPrimitive(Class toClass)
    {
        return toClass != null && (toClass.isPrimitive() || (java.lang.Boolean.class.isAssignableFrom(toClass)
                    || java.lang.Byte.class.isAssignableFrom(toClass)
                    || java.lang.Character.class.isAssignableFrom(toClass)
                    || java.lang.Double.class.isAssignableFrom(toClass)
                    || java.lang.Integer.class.isAssignableFrom(toClass)
                    || java.lang.Float.class.isAssignableFrom(toClass)
                    || java.lang.Long.class.isAssignableFrom(toClass)
                    || java.lang.Short.class.isAssignableFrom(toClass)
                    || java.lang.String.class.isAssignableFrom(toClass)
                    ));
    }
    
    /**
     * If the class provided is a boxed primitive or a primitive, then this will return the primitive class, otherwise will return null
     * @param toClass the class to get the primitive value of
     * @return The primitive class or null
     */
    public static Class getPrimitiveClass(Class toClass)
    {
        if (isPrimitive(toClass))
        {
            return toClass;
        }

        if (toClass.equals(java.lang.Boolean.class))
        {
            return boolean.class;
        }
        else if (toClass.equals(java.lang.Byte.class))
        {
            return byte.class;
        }
        else if (toClass.equals(java.lang.Character.class))
        {
            return char.class;
        }
        else if (toClass.equals(java.lang.Double.class))
        {
            return double.class;
        }
        else if (toClass.equals(java.lang.Integer.class))
        {
            return int.class;
        }
        else if (toClass.equals(java.lang.Float.class))
        {
            return float.class;
        }
        else if (toClass.equals(java.lang.Long.class))
        {
            return long.class;
        }
        else if (toClass.equals(java.lang.Short.class))
        {
            return short.class;
        }
        return null;
    }

    /**
     * Converts the string to the class specified
     * @param <T> the return type and the class type
     * @param tcValue the string value to convert
     * @param toClass the class to convert to
     * @return the new instance of toClass that has been parsed from tcValue, or null if this toClass was not a primitive
     */
    public static <T> T stringToPrimitive(String tcValue, Class<T> toClass)
    {
        // Check if the class is a primitive
        if (isPrimitive(toClass))
        {
            if (java.lang.Boolean.class.isAssignableFrom(toClass) || toClass == boolean.class)
            {
                return (T)Boolean.valueOf(tcValue);
            }
            else if (java.lang.Byte.class.isAssignableFrom(toClass) || toClass == byte.class)
            {
                return (T) new Byte(tcValue);
            }
            else if (java.lang.Character.class.isAssignableFrom(toClass) || toClass == char.class)
            {
                return (T) new java.lang.Character(tcValue.charAt(0));
            }
            else if (java.lang.Double.class.isAssignableFrom(toClass) || toClass == double.class)
            {
                return (T) new Double(tcValue);
            }
            else if (java.lang.Integer.class.isAssignableFrom(toClass) || toClass == int.class)
            {
                return (T) new Integer(tcValue);
            }
            else if (java.lang.Float.class.isAssignableFrom(toClass) || toClass == float.class)
            {
                return (T) new Float(tcValue);
            }
            else if (java.lang.Long.class.isAssignableFrom(toClass) || toClass == long.class)
            {
                return (T) new Long(tcValue);
            }
            else if (java.lang.Short.class.isAssignableFrom(toClass) || toClass == short.class)
            {
                return (T) new Short(tcValue);
            }
            else
            {
                return (T)tcValue;
            }
        }
        // Not a primitive so can't do anything for it.
        return null;
    }

    /**
     * Gets the absolute ancestral difference between two classes, returning the number
     * of steps between the two classes, or between where the two classes split, returns -1 if the two classes are not related
     * @param toPrimary the primary class to check, this is considered the "me" class
     * @param toRelation the relation class to check.  Distance between "me" and relation
     * @return the integral difference, or -1 if there is no relation
     */
    public static int getPropinquity(Class toPrimary, Class toRelation)
    {
        Class loCompare = isEqualOrAssignable(toPrimary, toRelation) ? toPrimary : null;
        Class loSecondary = loCompare == toPrimary ? toRelation : toPrimary;
        if (loCompare == null)
        {
            loCompare = isEqualOrAssignable(toRelation, toPrimary) ? toRelation : null;
        }

        int lnReturn = -1;
        while(loSecondary != null && isEqualOrAssignable(loCompare, loSecondary))
        {
            lnReturn++;
            loSecondary = loSecondary.getSuperclass();
            
        }
        return lnReturn;
    }
    
    /**
     * Checks if toLeft and toRight are the same class or if toLeft 
     * is assignable from toRight
     *
     * @param  toLeft   one class to compare
     * @param  toRight  the other class to compare
     * @return  true if both classes are null, both classes are the same, or
     *          if toLeft is assignable from toRight
     */
    public static boolean isEqualOrAssignable(Class toLeft, Class toRight)
    {
        // if both are null return true, or both the same class
        if ((toLeft == null && toRight == null) || (toRight == toLeft))
        {
            return true;
        }

        // if either is null but not the other then return false
        if (toLeft == null || toRight == null)
        {
            return false;
        }

        if (toRight.isPrimitive() || toLeft.isPrimitive())
        {
            // May be that we are trying to box or unbox
            return (((toLeft.equals(boolean.class) || toRight.equals(boolean.class)) &&
               (toLeft.equals(java.lang.Boolean.class) || toRight.equals(java.lang.Boolean.class)))
            || ((toLeft.equals(char.class) || toRight.equals(char.class)) &&
                    (toLeft.equals(java.lang.Character.class) || toRight.equals(java.lang.Character.class)))
            || ((toLeft.equals(byte.class) || toRight.equals(byte.class)) &&
                    (toLeft.equals(java.lang.Byte.class) || toRight.equals(java.lang.Byte.class)))
            || ((toLeft.equals(short.class) || toRight.equals(short.class)) &&
                    (toLeft.equals(java.lang.Short.class) || toRight.equals(java.lang.Short.class)))
            || ((toLeft.equals(int.class) || toRight.equals(int.class)) &&
                    (toLeft.equals(java.lang.Integer.class) || toRight.equals(java.lang.Integer.class)))
            || ((toLeft.equals(long.class) || toRight.equals(long.class)) &&
                    (toLeft.equals(java.lang.Long.class) || toRight.equals(java.lang.Long.class)))
            || ((toLeft.equals(float.class) || toRight.equals(float.class)) &&
                    (toLeft.equals(java.lang.Float.class) || toRight.equals(java.lang.Float.class)))
            || ((toLeft.equals(double.class) || toRight.equals(double.class)) &&
                    (toLeft.equals(java.lang.Double.class) || toRight.equals(java.lang.Double.class))));
        }

        return toLeft.isAssignableFrom(toRight);
    }

    /**
     * Helper function to get the size of primitive types
     * @param toType the type to get the size of
     * @return the size of this type
     */
    private static int getSizeofClass (final Class toType)
    {
        // TODO: Implement this for profiling
        if (toType == int.class)
            return Goliath.Constants.SizeOf.INT_FIELD_SIZE;
        else if (toType == long.class)
            return Goliath.Constants.SizeOf.LONG_FIELD_SIZE;
        else if (toType == short.class)
            return Goliath.Constants.SizeOf.SHORT_FIELD_SIZE;
        else if (toType == byte.class)
            return Goliath.Constants.SizeOf.BYTE_FIELD_SIZE;
        else if (toType == boolean.class)
            return Goliath.Constants.SizeOf.BOOLEAN_FIELD_SIZE;
        else if (toType == char.class)
            return Goliath.Constants.SizeOf.CHAR_FIELD_SIZE;
        else if (toType == double.class)
            return Goliath.Constants.SizeOf.DOUBLE_FIELD_SIZE;
        else if (toType == float.class)
            return Goliath.Constants.SizeOf.FLOAT_FIELD_SIZE;
        else
            throw new IllegalArgumentException ("not primitive: " + toType);
    }

    /**
     * Gets a list of all the packages that are currently loaded
     * @return a list of packages that are currently loaded
     */
    public static java.lang.Package[] getLoadedPackages()
    {
        return java.lang.Package.getPackages();
    }

    /***
     * Searchers for jar files under the toPathToJarDir path
     * @param toPathToJarDir
     * @return the list of jar files under the path
     */
    public static Goliath.Collections.List<String> getJarFiles(String toPathToJarDir)
    {
        List<String> laFiles = new List<String>();

        // Match on all .jar and .war files
        List<File> loFiles = Utilities.getFiles(toPathToJarDir, "(?i).+\\.[w|j]ar$", true);

        for (File loFile : loFiles)
        {
            laFiles.add(loFile.getAbsolutePath());
        }
        return laFiles;
    }
    
    /**
     * Gets the list of superclasses, starts the list at toClass and works all the way to the top
     * @param toClass the class to get the superclasses of
     * @return the list of superclasses
     */
    public static List<Class> getClassHierarchy(Class toClass)
    {
        List<Class> loReturn = new List<Class>();
        {
            while (toClass != null)
            {
                loReturn.add(toClass);
                toClass = toClass.getSuperclass();
            }
        }
        return loReturn;
    }

    /**
     * Gets the list of classes from the class tree that implement the specified interface or class
     * @param <T> The type of the class
     * @param toInterface the interface or class the list must implement or null for all classes
     * @param taConstructorParameters the list of parameters the constructors of the class must adhere to, or null for any constructor
     * @return The list of classes
     */
    public static <T> IList<Class<T>> getClasses(Class<T> toInterface, Class... taConstructorParameters)
    {
        // If the exclusion list is null, then we have never tried to load all of the classes
        if (g_oExclusionList == null)
        {
            // First get files only from the home directory
            String lcMatchString = "(?i).+\\.[w|j]ar$";
            List<File> laFiles = Utilities.getFiles(".", lcMatchString, false);

            // Then add all files from the lib directory
            laFiles.addAll(Utilities.getFiles("./lib", lcMatchString, false));


            // Get the class path
            Goliath.Collections.List<String> loClassPaths = Goliath.Utilities.getClassPaths();

            // for each directory in the class path, load the objects
            for (String lcPath : loClassPaths)
            {
                laFiles.addAll(Utilities.getFiles(lcPath, lcMatchString, true));
            }

            //Get the exclusion list so we know what paths not to bother checking for jar files
            List<String> loExclusionList = null;
            if (g_oExclusionList == null)
            {
                loExclusionList = new List<String>();
                loExclusionList.add(".+derby\\.jar$");
                loExclusionList.add("./3rdParty/.");
                loExclusionList.add("./jfree/.");
                loExclusionList.add("./mysql-connector-java.");
            }

            // Only attempt to load the list if the application is initialised, this will mean that until the application is initialised the exclusion list will
            // not be looked at.
            boolean llAppRunning = Application.getInstance().getState() == ApplicationState.RUNNING();
            if (llAppRunning)
            {
                g_oExclusionList = Application.getInstance().getPropertyHandlerProperty("Application.Settings.ClassPathExclusionList", loExclusionList);
                // Just write out the exclusion list so it can be modified
                if (g_oExclusionList == loExclusionList)
                {
                    Application.getInstance().setPropertyHandlerProperty("Application.Settings.ClassPathExclusionList", loExclusionList);
                }
                loExclusionList = g_oExclusionList;
            }

            // Now we need to pull out all of the classes from the jar files
            // It is now okay to use the dynamic code to do so.
            List<ICommand> loCommands = new List<ICommand>();

            for (File loJar: laFiles)
            {
                // Create a Command Manager to do this, the command manager will run commands on a separate thread
                if (!isPathExcluded(loExclusionList, loJar.getAbsolutePath()))
                {
                    loCommands.add(new LoadClassesFromJarCommand(true, new SingleParameterArguments<File>(loJar)));
                }
            }
            
            boolean llIsReady = false;
            while (!llIsReady)
            {
                llIsReady = true;
                try
                {
                    Thread.sleep(100);

                    for (ICommand loCommand : loCommands)
                    {
                        llIsReady = llIsReady && loCommand.isComplete();
                        
                        if (!llIsReady)
                        {
                            break;
                        }
                        
                        // If the command is not registered, then just execute it on this thread
                        if (!loCommand.isRegistered() && !loCommand.isExecuting())
                        {
                            loCommand.registerCommand();
                        }
                        
                    }
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }
            }
        }

        IList<Class> loReturn = toInterface == null ? g_oClassTree.toFlatList() : null;
        if (toInterface.isInterface())
        {
            loReturn = new List<Class>();
            getClassesForInterface(toInterface, loReturn);
        }
        else
        {
            ITree<Class> loTree = g_oClassTree.getTreeFor(toInterface);
            return loTree == null ? new List<Class<T>>(0) : new List(loTree.toFlatList());
        }
        // Remove any classes that do not have the constructor specified
        if (taConstructorParameters != null)
        {
            List<Class> loRemove = new List<Class>();

            for (Class loClass : loReturn)
            {
                try
                {
                    if (!hasConstructor(loClass, taConstructorParameters))
                    {
                        loRemove.add(loClass);
                    }
                }
                catch (Throwable ex)
                {
                    // Do nothing here
                }
            }
            loReturn.removeAll(loRemove);
        }

        // Have to do this to output an IList<Class<T>>
        IList<Class<T>> loList = new List<Class<T>>(loReturn.size());
        for (Class loClass : loReturn)
        {
            if (!isAbstract(loClass))
            {
                loList.add(loClass);
            }
        }
        return loList;
    }

    /**
     * Helper function to get all of the classes for the specified interface
     * @param toInterface the interface to get the classes for
     * @param toList the list that is added to
     */
    private static void getClassesForInterface(Class toInterface, IList<Class> toList)
    {
        if (g_oInterfaces != null && g_oInterfaces.containsKey(toInterface))
        {
            toList.addAll(g_oInterfaces.get(toInterface));
        }
    }
    
    /**
     * Creates an object from the provided class, using the parameters as parameters to the constructor
     *
     * @param <K>
     * @param  toClass  The class to create an object from
     * @param  taParams     a list of parameters to pass to the constructor
     * @return  returns     a reference to the method
     * @throws Goliath.Exceptions.InvalidParameterException if the method is not found
     */
    public static <T, K extends Goliath.DynamicEnum> T createObject(Class<T> toClass, java.lang.Object[] taParams)
            throws Goliath.Exceptions.InvalidParameterException
    {
        // A Dynamic enum is a core part of the framework.  Each enumeration class within a dynamic enum is a singleton, so is dealt with accordingly
        if (Java.isEqualOrAssignable(Goliath.DynamicEnum.class, toClass))
        {
            List<K> loDEClasses = Goliath.DynamicEnum.getEnumerations((Class<K>) toClass);
            for (Object loDEClass : loDEClasses)
            {
                if (loDEClass.getClass() == toClass)
                {
                    return (T)loDEClass;
                }
            }
            throw new ObjectNotCreatedException("Unable to create object "+toClass.getCanonicalName());
        }
        else
        {
            int lnLength = (taParams != null) ? taParams.length : 0;
            Class[] laClasses = new Class[lnLength];
            for (int i=0; i<lnLength; i++)
            {
                laClasses[i] = taParams[i].getClass();
            }

            // Get the constructor
            try
            {
                Constructor loConstructor = getConstructor(toClass, laClasses);
                if (loConstructor != null)
                {
                    boolean llIsAccessible = loConstructor.isAccessible();
                    if (!llIsAccessible)
                    {
                        loConstructor.setAccessible(true);
                    }

                    java.lang.Object loReturn = loConstructor.newInstance(taParams);

                    if (!llIsAccessible)
                    {
                        loConstructor.setAccessible(false);
                    }

                    return (T)loReturn;
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
        }
    }

    /**
     * Checks if the path specified is excluded from the search for .jar files
     * @param toPathExcluded the path to check
     * @param tcString the exclusion list
     * @return true if the path should be skipped, false otherwise
     */
    private static boolean isPathExcluded(List<String> toPathExcluded, String tcString)
    {
         for (String lcExclusionRegEx : toPathExcluded)
         {
             if(Goliath.Utilities.getRegexMatcher(lcExclusionRegEx, tcString).matches())
             {
                 return true;
             }
         }
        return false;
    }

    /**
     * Gets the contained type of a list
     * @param <K> the generic typed list
     * @param toList the list to get the type of
     * @return the type of the items in a list
     */
    public static <K> Class<K> getContainedClass(java.util.List<K> toList)
    {
        if (isEqualOrAssignable(IContainedClassIdentifiable.class, toList.getClass()))
        {
            return (Class<K>)((IContainedClassIdentifiable)toList).getContainedClass();
        }
        try
        {
            return (Class<K>)((TypeVariable)((ParameterizedType)toList.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getBounds()[0];
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
            return null;
        }
    }
    
    /**
     * Gets the contained type of a list
     * @param <K> the generic typed list
     * @param toList the list to get the type of
     * @return the type of the items in a list
     */
    public static <K> Class<K> getContainedClass(Class<java.util.List<K>> toListClass)
    {
        try
        {
            Type loType = toListClass.getGenericSuperclass();
            if (loType != null && Java.isEqualOrAssignable(ParameterizedType.class, loType.getClass()))
            {
                return (Class<K>)((TypeVariable)((ParameterizedType)loType).getActualTypeArguments()[0]).getBounds()[0];
            }
            else
            {
                return (Class<K>)loType;
            }
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        return null;
    }
    
    /**
     * Helper function to get the list of generic values for this class
     * @param toClass the class to get the template parameters from
     * @return the list of template parameters
     */
    public static List<Class> getGenericClasses(Class toClass)
    {
        List<Class> loReturn = new List<Class>();
        
        TypeVariable[] laGenerics = toClass.getTypeParameters();
        if (laGenerics.length == 0 && toClass.getGenericSuperclass() != null)
        {
            Class loClass = getParameterizedClassFromType(toClass.getGenericSuperclass());
            if (loClass != null)
            {
                loReturn.add(loClass);
            }
        }
        for (TypeVariable loType : laGenerics)
        {
            loReturn.add(getClassFromType(loType));
        }
        return loReturn;
    }
    
    /**
     * Helper method to get the class of the type specified
     * @param toClass the type to get the raw class of
     * @return the class
     */
    public static Class getClassFromType(Type toClass)
    {
        return Java.isEqualOrAssignable(Class.class, toClass.getClass()) 
                ? (Class)toClass 
                : Java.isEqualOrAssignable(ParameterizedType.class, toClass.getClass())
                    ? (Class)((ParameterizedType)toClass).getRawType()
                    : (Class)((TypeVariableImpl)toClass).getBounds()[0];
    }
    
    /**
     * Helper function to get the parameterised class of the specified Type
     * @param toClass the type to get the parameter for
     * @return the class of the parameter, or null if there is no parameter
     */
    public static Class getParameterizedClassFromType(Type toClass)
    {
        return Java.isEqualOrAssignable(Class.class, toClass.getClass()) 
                ? null 
                : Java.isEqualOrAssignable(ParameterizedType.class, toClass.getClass())
                    ? (Class)((ParameterizedType)toClass).getActualTypeArguments()[0]
                    : (Class)((TypeVariableImpl)toClass).getBounds()[0];
    }

    /**
     * Loads the specified class and returns the class object.  This method will not
     * throw an exception, but will return null if the class could not be created
     * @param <T>
     * @param tcClassName the full name of the class to load
     * @return the class, or null if the class did not exist
     */
    public static <T> Class<T> getClass(String tcClassName)
    {
        java.lang.Class loClass = null;

        // We try to load the class
        try
        {
            loClass = Class.forName(tcClassName);
        }
        catch (ClassNotFoundException ex)
        {
            IClassLoader loLoader = Goliath.Applications.Application.getInstance().getClassLoader();
            try
            {
                // Try using the application class loader
                loClass = loLoader.loadClass(tcClassName);
            }
            catch (Throwable e)
            {
                // We were not able to create the sepecified class
                Application.getInstance().log(e);
            }
        }
        catch (Throwable ex)
        {
        }
        return loClass;
    }

    /***
     * checks if toClass has and constructors with the parameters type signature specified by
     * toConstructorParameters. The order of the method signature parameters type does not matter 
     * @param toClass the class to check for the constructor
     * @param toConstructorParameters
     * @return true if the class has a matching constructor
     */
    private static boolean hasConstructor(Class toClass, Class[] toConstructorParameters)
    {
        // TODO: refactor this up to the ClassDefinition
        try
        {
            // If the constructor exists return true
            return toClass.getConstructor(toConstructorParameters) != null;
        }
        catch (java.lang.NoClassDefFoundError ex)
        {
            return false;
        }
        catch (Exception e)
        {
            // Otherwise return false
            return false;
        }
    }

    /***
     * checks if toClass contains toInterface
     * @param toClass
     * @param toInterface
     * @return if toClass implements the interface
     */
    public static boolean containsInterface(Class toClass, Class toInterface)
    {
        // TODO: Refactor up to the Class Definition
        return (toInterface.isAssignableFrom(toClass));
    }

    /**
     * Checks if a class is abstract
     * @param toClass the class to check
     * @return true if the class is an abstract class
     */
    public static boolean isAbstract(Class toClass)
    {
        // TODO: Refactor up to the Class Definition
        return java.lang.reflect.Modifier.isAbstract(toClass.getModifiers());
    }

    


    
   
}
