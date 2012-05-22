/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.XML;

import Goliath.Collections.PropertySet;
import Goliath.Constants.XMLFormatType;
import java.util.Arrays;
import java.util.Hashtable;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author pStanbridge
 */
public class HashTableXMLFormatter extends Goliath.XML.XMLFormatter<Hashtable<?,?>>
{
    @Override
    public Class supports()
    {
        return Hashtable.class;
    }

    @Override
    protected boolean allowContent(Hashtable toObject, XMLFormatType toFormatType)
    {
        return true;
    }

    @Override
    protected Goliath.Collections.List<String> getAttributeList(Hashtable toObject)
    {
        return new Goliath.Collections.List<String>(new String[]{"size"});
    }

    @Override
    protected void onWriteContent(XMLStreamWriter toStream, Hashtable<?, ?> toObject, XMLFormatType toFormatType)
    {
        if (toObject == null || toObject.isEmpty())
        {
            return;
        }

        Object[] loKeys = toObject.keySet().toArray();
        Arrays.sort(loKeys);
        for (int i = 0; i < loKeys.length; i++)
        {
            java.lang.Object loKey = loKeys[i];
            java.lang.Object loValue = toObject.get(loKeys[i]);

            if (loValue != null)
            {
                try
                {
                    toStream.writeStartElement("Item");

                    toStream.writeStartElement("Key");
                    
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
            iterateUntilStarting(toReader, "Key");
            Object loKey = fromXMLReader(toReader, toFormatType, null);
            iterateUntilStarting(toReader, "Value");
            Object loValue = fromXMLReader(toReader, toFormatType, null);

            if (loKey != null && loValue != null)
            {
                ((Hashtable) toObject).put(loKey, loValue);
            }
        }
    }
}
