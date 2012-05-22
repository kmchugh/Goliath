/* =========================================================
 * DefaultJSONHelper.java
 *
 * Author:      kenmchugh
 * Created:     Oct 6, 2009, 4:17:24 PM
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

import Goliath.Collections.List;
import Goliath.Constants.StringFormatType;
import Goliath.DynamicCode.Java;
import Goliath.Interfaces.IStringFormatter;
import Goliath.Text.StringFormatter;
import java.lang.reflect.Array;

/**
 *
 * @author kenmchugh
 */
public class JSONFormatter<T> extends StringFormatter<T>
        implements IStringFormatter<T>
{

    /**
     * This formatter only supports the JSON format type
     * @return a list containing the JSON Format type
     */
    @Override
    public List<StringFormatType> supportedFormats()
    {
        return new List<StringFormatType>(new StringFormatType[]{StringFormatType.JSON()});
    }

    @Override
    protected void formatComplexProperty(StringBuilder toBuilder, String tcPropertyName, Object toValue, StringFormatter toFormatter, StringFormatType toType)
    {
        toBuilder.append("\"");
        toBuilder.append(tcPropertyName);
        toBuilder.append("\"");
        toBuilder.append(": ");
        
        if (toValue != null)
        {
            StringFormatter loFormatter = StringFormatter.getFormatterFor(toValue, toType);
            loFormatter.appendToStringBuilder(toBuilder, toValue, toType);
        }
        else
        {
            toBuilder.append(formatNullObject());
        }
        
    }

    @Override
    protected void formatForPropertyCount(StringBuilder toBuilder, int tnIndex, int tnCount, StringFormatType toFormatType)
    {
        if (tnIndex < tnCount)
        {
            toBuilder.append(",");
        }
    }

    @Override
    public String formatNullObject()
    {
        return "null";
    }
    
    
    @Override
    public String toString(T toObject)
    {
        StringBuilder loBuilder = new StringBuilder();
        appendPrimitiveString(loBuilder, toObject, StringFormatType.JSON());
        return loBuilder.toString();
    }
    
    

    /**
     * Appends the object string as a primitive
     */
    @Override
    public void appendPrimitiveString(StringBuilder toBuilder, T toObject, StringFormatType toType)
    {
        if (toObject == null)
        {
            toBuilder.append("null");
        }
        else if(Java.isEqualOrAssignable(String.class, toObject.getClass()))
        {
            String lcString = (String)toObject;
            lcString = (lcString.indexOf("\"") >= 0) ? lcString.replaceAll("\"", "\\\\\"") : lcString;
            lcString = (lcString.indexOf("\n") >= 0) ? lcString.replaceAll("\n", "\\\\n"): lcString;
            lcString = (lcString.indexOf("\r") >= 0) ? lcString.replaceAll("\r", "\\\\r"): lcString;

            toBuilder.append("\"");
            toBuilder.append(lcString);
            toBuilder.append("\"");
        }
        else if (toObject.getClass().isArray())
        {
            toBuilder.append(getStartTag(toObject));
            int lnLength = Array.getLength(toObject);
            for (int i=0; i<lnLength; i++)
            {
                java.lang.Object loValue = Array.get(toObject, i);
                StringFormatter loFormatter = StringFormatter.getFormatterFor(loValue, toType);
                loFormatter.appendToStringBuilder(toBuilder, loValue, toType);

                if (i<lnLength-1)
                {
                    toBuilder.append(", ");
                }
            }
            toBuilder.append(getEndTag(toObject));
        }
        else
        {
            toBuilder.append(toObject.toString());
        }
    }

    @Override
    protected String getStartTag(T toObject)
    {
        return toObject.getClass().isArray() ? "[" : "{";
    }

    @Override
    protected String getEndTag(T toObject)
    {
        return toObject.getClass().isArray() ? "]" : "}";
    }
}
