/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.PropertyHandlers.PropertyHelpers;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Interfaces.PropertyHandlers.IPropertyIOBehaviour;
import Goliath.PropertyHandlers.FileIOBehaviour;
import Goliath.PropertyHandlers.PropertyHelper;
import org.w3c.dom.Element;

/**
 *
 * @author kenmchugh
 */
public class DefaultPropertyHelper extends PropertyHelper
{

    // TODO: PropertyHandlers should be using generics
    @Override
    public Class<? extends IPropertyIOBehaviour> getBehaviour()
    {
        return FileIOBehaviour.class;
    }

    @Override
    public Class getObjectType()
    {
        return java.lang.Object.class;
    }

    @Override
    public final boolean isEqual(Object toSource, Object toNewValue)
    {
        return onIsEqual(convertToElement(toSource), toNewValue);
    }

    @Override
    public final <T> T readProperty(Object toSource, Class<T> toClass)
    {
        return onReadProperty(convertToElement(toSource), toClass);
    }

    @Override
    public final boolean removeProperty(Object toSource, Object toNewValue)
    {
        return onRemoveProperty(convertToElement(toSource), toNewValue);
    }

    @Override
    public final boolean writeProperty(Object toSource, Object toNewValue)
    {
        try
        {
            setType(toSource, toNewValue.getClass());
            return onWriteProperty(convertToElement(toSource), toNewValue);
        }
        catch (Throwable ex)
        {
            throw new Goliath.Exceptions.InvalidIOException(ex);
        }
    }

    @Override
    public final Class getType(Object toSource)
    {
        try
        {
            return onGetType(convertToElement(toSource));
        }
        catch (Throwable ex)
        {
            Exception loEx = new Goliath.Exceptions.Exception(ex);
            return null;
        }
    }

    @Override
    public final boolean setType(Object toSource, Class toClass)
    {
        Class loType = getType(toSource);
        onSetType(convertToElement(toSource), toClass);
        return loType == null || loType != toClass;
    }
    
    
    
    private Element convertToElement(Object toSource)
    {
        if (!org.w3c.dom.Element.class.isAssignableFrom(toSource.getClass()))
        {
            throw new Goliath.Exceptions.InvalidParameterException("Must be an org.w3c.dom.Element class", "toSource");
        }
        return (Element)toSource;
    }
    
    protected Class onGetType(Element toSource)
    {
        try
        {
            String lcClassName = toSource.getAttribute("type");
            if (Goliath.Utilities.isNullOrEmpty(lcClassName))
            {
                // The type has not been written yet
                return null;
            }
            return Class.forName(toSource.getAttribute("type"));
        }
        catch (Throwable ex)
        {
            Exception loEx = new Goliath.Exceptions.Exception(ex);
            return null;
        }
    }
    
    protected void onSetType(Element toSource, Class toClass)
    {
        toSource.setAttribute("type", toClass.getName());
    }
    
    // Default methods
    protected boolean onIsEqual(Element toSource, Object toNewValue)
    {
        // Check for null
        if (toSource == null && toNewValue == null)
        {
            return true;
        }
        Object loStoredValue = readProperty(toSource, getType(toSource));
        if (loStoredValue == null && toNewValue == null)
        {
            return true;
        }
        return loStoredValue != null && loStoredValue.equals(toNewValue);
    }
    
    protected <T> T onReadProperty(Element toSource, Class<T> toClass)
    {
        if (Goliath.DynamicCode.Java.isPrimitive(toClass))
        {
            return Goliath.DynamicCode.Java.stringToPrimitive(toSource.getTextContent(), toClass);
        }
        // We will not read in anything we can not handle

        // TODO: Implement default get/set reading as the onWriteProperty writes below
        return null;
    }
    
    protected boolean onRemoveProperty(Element toSource, Object toNewValue)
    {
        toSource.getParentNode().removeChild(toSource);
        return true;
    }
    
    protected boolean onWriteProperty(Element toSource, Object toNewValue)
    {
        if (Goliath.DynamicCode.Java.isPrimitive(toNewValue))
        {
            toSource.setTextContent(toNewValue.toString());
            return true;
        }
        
        // We did not really know what the property So we have attempted to getProperties to write
        List<String> loProperties = Goliath.DynamicCode.Java.getPropertyMethods(toNewValue.getClass());
        if (loProperties == null || loProperties.size() == 0)
        {
            toSource.setTextContent(toNewValue.toString());
        }
        else
        {
            for (String lcProperty : loProperties)
            {
                try
                {
                    Object loValue = Goliath.DynamicCode.Java.getPropertyValue(toNewValue, lcProperty);
                    Element loElement = toSource.getOwnerDocument().createElement(lcProperty);
                    toSource.appendChild(loElement);
                    PropertyHelper.create(getBehaviour(), loValue).writeProperty(loElement, loValue);
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                }

            }
        }
        return false;
    }
    
}
