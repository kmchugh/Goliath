/* =========================================================
 * StringFormatTypes.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * StringFormatTypes are a dynamic enum used in conjunction with
 * the toString and formatString methods to format the resulting string
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Constants;

/**
 * This class defines the formatting types for objects
 * when the toString method is called
 *
 * @see         Goliath.Object#toString()
 * @see         Goliath.Object#toString(Goliath.Constants.StringFormatType)
 * @see         Goliath.Object#formatString(Goliath.Constants.StringFormatType)
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class StringFormatType extends Goliath.DynamicEnum
{
    private static StringFormatType g_oDefault;
    public static StringFormatType DEFAULT()
    {
        if (g_oDefault == null)
        {
            g_oDefault = createEnumeration(StringFormatType.class, "DEFAULT");
        }
        return g_oDefault;
    }
    
    private static StringFormatType g_oXML;
    public static StringFormatType XML()
    {
        if (g_oXML == null)
        {
            g_oXML = createEnumeration(StringFormatType.class, "XML");
        }
        return g_oXML;
    }
    
    private static StringFormatType g_oJSON;
    public static StringFormatType JSON()
    {
        if (g_oJSON == null)
        {
            g_oJSON = createEnumeration(StringFormatType.class, "JSON");
        }
        return g_oJSON;
    }
    
    
    
    
    /**
     * Creates a new instance of a StringFormatType Object 
     *
     * @param tcValue The value for the string format type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    protected StringFormatType(String tcValue)
    {
        super(tcValue);
    }
    
}
