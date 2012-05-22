/* =========================================================
 * XMLFormatTypes.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * XMLFormatTypes are a dynamic enum used in conjunction with
 * the toXML and formatXML methods to format the resulting XML
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
 * when the toXML method is called
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public final class XMLFormatType extends Goliath.DynamicEnum
{
    public XMLFormatType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }
    
    private static XMLFormatType g_oDefault;
    public static XMLFormatType DEFAULT()
    {
        if (g_oDefault == null)
        {
            try
            {
                g_oDefault = new XMLFormatType("DEFAULT");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDefault;
    }

    private static XMLFormatType g_oDetailed;
    public static XMLFormatType DETAILED()
    {
        if (g_oDetailed == null)
        {
            try
            {
                g_oDetailed = new XMLFormatType("DETAILED");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDetailed;
    }

    private static XMLFormatType g_oTyped;
    public static XMLFormatType TYPED()
    {
        if (g_oTyped == null)
        {
            try
            {
                g_oTyped = new XMLFormatType("TYPED");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oTyped;
    }

    private static XMLFormatType g_oGSP;
    public static XMLFormatType GSP()
    {
        if (g_oGSP == null)
        {
            try
            {
                g_oGSP = new XMLFormatType("GSP");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oGSP;
    }
    
}

