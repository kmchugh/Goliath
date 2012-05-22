/* =========================================================
 * ListJSONHandler.java
 *
 * Author:      kenmchugh
 * Created:     Oct 6, 2009, 4:44:58 PM
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

package Goliath.JSON.Formatter;

import Goliath.Constants.StringFormatType;
import Goliath.Text.StringFormatter;



/**
 *
 * @author kenmchugh
 */
public class ListJSONHelper<T extends java.util.List> extends JSONFormatter<T>
{
    public Class<T> getSupportedClass()
    {
        return (Class<T>)java.util.List.class;
    }

    @Override
    public boolean allowContent()
    {
        return false;
    }

    @Override
    public void appendPrimitiveString(StringBuilder toBuilder, T toObject, StringFormatType toType)
    {
        toBuilder.append(getStartTag(toObject));
        int lnLength = toObject.size();
        int lnCount = 0;
        for (Object loItem : toObject)
        {
            StringFormatter loFormatter = StringFormatter.getFormatterFor(loItem, toType);
            loFormatter.appendToStringBuilder(toBuilder, loItem, toType);
            lnCount++;
            if (lnLength != lnCount)
            {
                toBuilder.append(", ");
            }
        }
        toBuilder.append(getEndTag(toObject));
    }
    
    @Override
    protected String getEndTag(T toObject)
    {
        return "]";
    }

    @Override
    protected String getStartTag(T toObject)
    {
        return "[";
    }
}
