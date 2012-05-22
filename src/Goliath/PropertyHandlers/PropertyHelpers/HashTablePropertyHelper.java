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
public class HashTablePropertyHelper extends DefaultPropertyHelper
{

    @Override
    public Class getObjectType()
    {
        return java.util.Hashtable.class;
    }

    @Override
    protected <T> T onReadProperty(Element toSource, Class<T> toClass)
    {
        try
        {
            java.util.Hashtable loList = (java.util.Hashtable)toClass.newInstance();

            // Get the type in the collection
            IPropertyHelper loKeyHelper = null;
            IPropertyHelper loItemHelper = null;
            for (int i=0; i<toSource.getChildNodes().getLength(); i++)
            {
                if (!Element.class.isAssignableFrom(toSource.getChildNodes().item(i).getClass()))
                {
                    continue;
                }
                Element loHash = (Element)toSource.getChildNodes().item(i);
                Element loKey = null;
                Element loItem = null;
                for (int j=0; j<loHash.getChildNodes().getLength(); j++)
                {
                    Object loElementObject = loHash.getChildNodes().item(j);
                    if (Element.class.isAssignableFrom(loElementObject.getClass()))
                    {
                        if (((Element)loElementObject).getTagName().equalsIgnoreCase("key"))
                        {
                            loKey = (Element)loElementObject;
                        }
                        if (((Element)loElementObject).getTagName().equalsIgnoreCase("item"))
                        {
                            loItem = (Element)loElementObject;
                        }
                    }
                }
                
                if (loKeyHelper == null)
                {
                    loKeyHelper = PropertyHelper.create(getBehaviour(), getType(loKey));
                }

                if (loItemHelper == null)
                {
                    loItemHelper = PropertyHelper.create(getBehaviour(), getType(loItem));
                }


                Object loKeyObject = loKeyHelper.readProperty(loKey, getType(loKey));
                Object loItemObject = loItemHelper.readProperty(loItem, getType(loItem));

                loList.put(loKeyObject, loItemObject);
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

        java.util.Hashtable loList = (java.util.Hashtable)toNewValue;
        IPropertyHelper loHelper = null;
        for (Object loObject : loList.keySet())
        {
            if (loHelper == null)
            {
                loHelper = PropertyHelper.create(getBehaviour(), loObject);
            }
            Element loElement = toSource.getOwnerDocument().createElement("Hash");

            Element loKey = toSource.getOwnerDocument().createElement("key");
            loElement.appendChild(loKey);

            Element loItem = toSource.getOwnerDocument().createElement("item");
            loElement.appendChild(loItem);

            toSource.appendChild(loElement);

            loHelper.writeProperty(loKey, loObject);
            loHelper.writeProperty(loItem, loList.get(loObject));
        }
        return true;
    }
}
