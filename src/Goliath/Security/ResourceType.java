/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Security;

/**
 *
 * @author kenmchugh
 */
public class ResourceType extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of a StringFormatType Object
     *
     * @param tcValue The value for the string format type
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public ResourceType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static ResourceType g_oDatabase;
    public static ResourceType DATABASE()
    {
        if (g_oDatabase == null)
        {
            try
            {
                g_oDatabase = new ResourceType("DATABASE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDatabase;
    }

    private static ResourceType g_oTable;
    public static ResourceType TABLE()
    {
        if (g_oTable == null)
        {
            try
            {
                g_oTable = new ResourceType("TABLE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oTable;
    }

    private static ResourceType g_oTableRow;
    public static ResourceType TABLEROW()
    {
        if (g_oTableRow == null)
        {
            try
            {
                g_oTableRow = new ResourceType("TABLEROW");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oTableRow;
    }

    private static ResourceType g_oFileDirectory;
    public static ResourceType FILEDIRECTORY()
    {
        if (g_oFileDirectory == null)
        {
            try
            {
                g_oFileDirectory = new ResourceType("FILEDIRECTORY");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oFileDirectory;
    }

    private static ResourceType g_oFile;
    public static ResourceType FILE()
    {
        if (g_oFile == null)
        {
            try
            {
                g_oFile = new ResourceType("FILE");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oFile;
    }

}
