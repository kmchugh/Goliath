/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.PropertyHandlers;

/**
 *
 * @author kenmchugh
 */
public interface IPropertyHelper 
{
    /**
     * Gets the behaviour that this helper is for
     * @return the behaviour for the helper
     */
    Class<? extends IPropertyIOBehaviour> getBehaviour();
    
    /**
     * Gets the object class that this helper is for
     * @return the class that this helper will help with
     */
    Class getObjectType();
    
    /**
     * Checks if the source and the value are equal
     * @param toSource the source, or persistant value
     * @param toNewValue the value to check the persistant value against
     * @return true if they are equal
     */
    boolean isEqual(java.lang.Object toSource, java.lang.Object toNewValue);
    
    /**
     * Writes a property to the source and returns true if the property was handled correctly
     * @param toSource The source to write to, for example and xml node if writing to an xml file
     * @param toNewValue the value of the property
     * @return true if the type was handled correctly, false if the property could not be handled correctly
     */
    boolean writeProperty(java.lang.Object toSource, java.lang.Object toNewValue);
    
    /**
     * Removes a property from the source
     * @param toSource the source to remove the property from
     * @param toNewValue the property being removed
     * @return true if the value was removed correctly
     */
    boolean removeProperty(java.lang.Object toSource, java.lang.Object toNewValue);
    
    /**
     * Reads a property value from the source and returns it
     * @param toSource the source to read the value from
     * @param toClass the class that should be returned
     * @return the object or value read from the source
     */
    <T> T readProperty(java.lang.Object toSource, Class<T> toClass);
    
    /**
     * Gets the type from the source
     * @param toSource the source to get the type from
     * @return the source
     */
    Class getType(java.lang.Object toSource);
    
    /**
     * Sets the type to the source
     * @param toSource the source to set the type for
     * @param toClass the class to set to
     * @return true if the type actually changed
     */
    boolean setType(java.lang.Object toSource, Class toClass);


}
