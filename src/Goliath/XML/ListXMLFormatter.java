/* =========================================================
 * IListXMLFormatter.java
 *
 * Author:      kenmchugh
 * Created:     Jun 4, 2010, 3:43:36 PM
 *
 * Description
 * --------------------------------------------------------
 * <Description>
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.XML;

import Goliath.Collections.PropertySet;
import Goliath.Constants.XMLFormatType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author kenmchugh
 */
public class ListXMLFormatter extends Goliath.XML.XMLFormatter<List>
{
    @Override
    public Class supports()
    {
        return List.class;
    }

    @Override
    protected boolean allowContent(List toObject, XMLFormatType toFormatType)
    {
        return true;
    }

    @Override
    protected Goliath.Collections.List<String> getAttributeList(List toObject)
    {
        return new Goliath.Collections.List<String>(new String[]{"size"});
    }

    @Override
    protected void onWriteContent(XMLStreamWriter toStream, List toObject, XMLFormatType toFormatType)
    {
        if (toObject == null || toObject.isEmpty())
        {
            return;
        }

        // Make a copy of the list to avoid concurrent modifications
        List loList = new ArrayList(toObject);
        for (Object loObject : loList)
        {
            if (loObject != null)
            {
                try
                {
                    toStream.writeStartElement("Item");

                    if (Goliath.DynamicCode.Java.isPrimitive(loObject) && toFormatType == XMLFormatType.TYPED())
                    {
                        toStream.writeStartElement(loObject.getClass().getSimpleName());
                        toStream.writeAttribute("type", loObject.getClass().getName());
                        XMLFormatter.appendToXMLStream(loObject, toFormatType, toStream, null);
                        toStream.writeEndElement();
                    }
                    else
                    {
                        XMLFormatter.appendToXMLStream(loObject, toFormatType, toStream, null);
                    }
                    
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
            iterateToNextPosition(toReader);

            // We have found an item, so we need to start processing
            Object loValue = fromXMLReader(toReader, toFormatType, null);
            
            if (loValue != null)
            {
                ((List) toObject).add(loValue);
            }
        }
    }
}

