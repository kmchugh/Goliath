/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.XML;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.XMLFormatType;
import java.util.Arrays;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author pStanbridge
 */
public class PropertySetXMLFormatter extends Goliath.XML.XMLFormatter<PropertySet>
{
    
    @Override
    public Class supports()
    {
        return PropertySet.class;
    }

    @Override
    protected boolean allowContent(PropertySet toObject, XMLFormatType toFormatType)
    {
        return true;
    }

    @Override
    protected void onWriteContent(XMLStreamWriter toStream, PropertySet toObject, XMLFormatType toFormatType)
    {
        if (toObject == null || toObject.isEmpty())
        {
            return;
        }

        Object[] loKeys = toObject.getPropertyKeys().toArray();
        Arrays.sort(loKeys);
        for (int i = 0; i < loKeys.length; i++)
        {
            String loKey = (String)loKeys[i];
            java.lang.Object loValue = toObject.getProperty(loKey);

            if (loValue != null)
            {
                try
                {
                    toStream.writeStartElement("Item");

                    toStream.writeStartElement("Property");

                    if (Goliath.DynamicCode.Java.isPrimitive(loKey) && toFormatType == XMLFormatType.TYPED())
                    {
                        toStream.writeAttribute("type", loKey.getClass().getName());
                    }

                    XMLFormatter.appendToXMLStream(loKey, toFormatType, toStream, null);
                    toStream.writeEndElement();


                    toStream.writeStartElement("Value");

                    if (Goliath.DynamicCode.Java.isPrimitive(loValue) && toFormatType == XMLFormatType.TYPED())
                    {
                        toStream.writeAttribute("type", loValue.getClass().getName());
                    }


                    XMLFormatter.appendToXMLStream(loValue, toFormatType, toStream, null);
                    toStream.writeEndElement();

                    toStream.writeEndElement();
                }
                catch(Throwable ex)
                {}
            }
        }
    }

    @Override
    protected void onStartedElement(XMLStreamReader toReader, String tcNodeName, PropertySet toAttributes, Object toObject, XMLFormatType toFormatType)
    {
        if (tcNodeName.equalsIgnoreCase("Item"))
        {
            // We have found an item, so we need to start processing

            // Extract the Key and the Value
            iterateUntilStarting(toReader, "Property");
            Object loKey = fromXMLReader(toReader, toFormatType, null);
            iterateUntilStarting(toReader, "Value");
            Object loValue = fromXMLReader(toReader, toFormatType, null);

            if (loKey != null && loValue != null)
            {
                ((PropertySet) toObject).setProperty((String)loKey, loValue);
            }
        }
    }

    @Override
    protected PropertySet onCreateObject(Class<PropertySet> toClass, PropertySet toAttributes, Object toObject)
    {
        return new PropertySet();
    }


}
