/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.PropertyHandlers.PropertyHelpers;

import Goliath.Interfaces.PropertyHandlers.IPropertyHelper;
import Goliath.PropertyHandlers.PropertyHelper;
import org.w3c.dom.Element;

/**
 *
 * @author kenmchugh
 */
public class ListPropertyHelper extends DefaultPropertyHelper
{

    @Override
    public Class getObjectType()
    {
        return java.util.List.class;
    }

    @Override
    protected <T> T onReadProperty(Element toSource, Class<T> toClass)
    {
        try
        {
            // TODO : Need to write the generic arguments as well
            java.util.List loList = (java.util.List)toClass.newInstance();
            
            // Get the type in the collection
            IPropertyHelper loHelper = null;
            for (int i=0; i<toSource.getChildNodes().getLength(); i++)
            {
                if (!Element.class.isAssignableFrom(toSource.getChildNodes().item(i).getClass()))
                {
                    continue;
                }
                Element loItem = (Element)toSource.getChildNodes().item(i);
                if (loHelper == null)
                {
                    loHelper = PropertyHelper.create(getBehaviour(), getType(loItem));
                }
                Object loObject = loHelper.readProperty(loItem, getType(loItem));
                loList.add(loObject);
            }
            
            return (T)loList;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    @Override
    protected boolean onWriteProperty(Element toSource, Object toNewValue)
    {
        // Clear the current collection
        for (int i= toSource.getChildNodes().getLength() -1; i > 0; i--)
        {
            toSource.removeChild(toSource.getChildNodes().item(i));
        }
        
        java.util.List loList = (java.util.List)toNewValue;
        IPropertyHelper loHelper = null;
        for (Object loObject : loList)
        {
            if (loHelper == null)
            {
                loHelper = PropertyHelper.create(getBehaviour(), loObject);
            }
            Element loElement = toSource.getOwnerDocument().createElement("Item");
            toSource.appendChild(loElement);
            loHelper.writeProperty(loElement, loObject);
        }
        return true;
    }
}
