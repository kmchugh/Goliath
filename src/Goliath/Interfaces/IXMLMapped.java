/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces;

import Goliath.Constants.XMLFormatType;

/**
 *
 * @author kenmchugh
 */
public interface IXMLMapped
{
    @Goliath.Annotations.NotProperty
    Goliath.Interfaces.Collections.IList<IXMLMapped> getObjectList();

    @Goliath.Annotations.NotProperty
    IXMLMapped getByID(Long tnID);

    @Goliath.Annotations.NotProperty
    IXMLMapped getByGUID(String tcGUID);

    /**
     * Gets an xml representation of the object
     *
     * @return      The xml representation of the object
     */
    String toXML();

    /**
     * Gets an xml representation of the object
     *
     * @param toFormat  the format type to apply to the xml representation
     * @return      The xml representation of the object
     */
    String toXML(Goliath.Constants.XMLFormatType toFormat);

    /**
     * Creates an xml representation of the object
     *
     * @param toFormat  the format type to apply to the xml representation
     * @return      The xml representation of the object
     */
    String formatXML(Goliath.Constants.XMLFormatType toFormat);

    void appendToXML(StringBuilder toBuilder, XMLFormatType toFormat);

}
