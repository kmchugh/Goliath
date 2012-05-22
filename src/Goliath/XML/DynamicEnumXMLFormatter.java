/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.XML;

import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.XMLFormatType;
import Goliath.DynamicEnum;


/**
 *
 * @author kmchugh
 */
public class DynamicEnumXMLFormatter extends Goliath.XML.XMLFormatter<DynamicEnum>
{
    
    @Override
    public Class supports()
    {
        return DynamicEnum.class;
    }

    @Override
    protected boolean allowContent(DynamicEnum toObject, XMLFormatType toFormatType)
    {
        return false;
    }

    @Override
    protected List<String> getAttributeList(DynamicEnum toObject)
    {
        return new Goliath.Collections.List<String>(new String[]{"value"});
    }

    @Override
    protected DynamicEnum onCreateObject(Class<DynamicEnum> toClass, PropertySet toAttributes, Object toObject)
    {
        return DynamicEnum.getEnumeration(toClass, toAttributes.<String>getProperty("value"));
    }


    @Override
    protected boolean finishedOnCreate()
    {
        return true;
    }



}