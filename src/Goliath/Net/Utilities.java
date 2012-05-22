/* ========================================================
 * Utilities.java
 *
 * Author:      kmchugh
 * Created:     Apr 2, 2011, 4:17:50 PM
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Net;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import java.net.Socket;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Apr 2, 2011
 * @author      kmchugh
**/
public class Utilities extends Goliath.Object
{
    /**
     * Creates a new instance of Utilities
     */
    private Utilities()
    {
    }

    public static String getDomainServer(String tcDomain)
    {
        try
        {
            // For now just lookup the mail. record
            HashTable<String, String> loEnvironment = new HashTable();
            loEnvironment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

            DirContext loContext = new InitialDirContext(loEnvironment);

            Attributes loAttributes = loContext.getAttributes(tcDomain, new String[] {"MX"});
            Attribute loAttribute = loAttributes.get("MX");

            int lnLevel=999;
            String lcLookup = tcDomain;
            for (int i=0, lnLength = loAttribute.size(); i<lnLength; i++)
            {
                String lcValue = loAttribute.get(i).toString();
                String[] laValues = lcValue.split(" ");
                if (laValues.length == 2)
                {
                    int lnCurrentLevel = Integer.parseInt(laValues[0]);
                    if (lnCurrentLevel < lnLevel)
                    {
                        lcLookup = laValues[1];
                        lnLevel = lnCurrentLevel;
                    }
                }

            }
            Application.getInstance().log("Found mail server at " + lcLookup);
            return lcLookup;
        }
        catch (Throwable ex)
        {
            Application.getInstance().log("Problem receiving MX records for domain " + tcDomain);
            Application.getInstance().log(ex);
        }
        return null;
    }

    /**
     * Creates a socket connection to the specified domains port
     * @param tcDomain the domain
     * @param tnPort the port to connect to
     * @return the Socket, or null if it could not be created
     */
    public static Socket getConnectionFromDomain(String tcDomain, int tnPort)
    {
        String lcAddress = getDomainServer(tcDomain);
        if (lcAddress != null)
        {
            Application.getInstance().log("Connecting to mail server at " + lcAddress);

            try
            {
                return new Socket(lcAddress, tnPort);
            }
            catch (Throwable ex)
            {
                Application.getInstance().log("Problem receiving MX records for domain " + tcDomain);
                Application.getInstance().log(ex);
            }
        }
        return null;
    }


}
