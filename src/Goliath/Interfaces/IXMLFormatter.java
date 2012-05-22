/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces;

import Goliath.Constants.XMLFormatType;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;


/**
 *
 * @author kenmchugh
 */
public interface IXMLFormatter<T>
{
    /**
     * Returns a string based on the object
     *
     * @param  toObject the object to get a string from
     * @return  a string representation of the object
     */
    String toXMLString(T toObject);

    /**
     * Returns a string based on the object
     *
     * @param  toObject the object to get a string from
     * @return  a string representation of the object
     */
    String toXMLString(T toObject, XMLFormatType toFormatType);

    /**
     * Appends the Object to the XML Stream
     * @param toStream the stream to append to
     * @param toObject the object to append
     * @param toFormatType the format type to use
     * @param tcTag the starting tag for the object
     */
    void appendToXMLStream(XMLStreamWriter toStream, T toObject, XMLFormatType toFormatType, String tcTag);

    /**
     * Gets the class that this formatter supports
     * @return the class that this formatter is able to convert to XML
     */
    Class<T> supports();

    /**
     * Parses an object from the xml Stream Reader
     * @param toReader The xml reader that is being used to iterate over the xml
     * @param toFormatType The format the xml is in
     * @param toObject The object that is currently being parsed
     * @return the object that was found in the xml stream
     */
    java.lang.Object fromXMLReader(XMLStreamReader toReader, XMLFormatType toFormatType, java.lang.Object toObject);

}