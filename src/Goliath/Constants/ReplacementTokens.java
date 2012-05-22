/* =========================================================
 * ReplacementTokens.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * These are currently being used for replacements in error messages
 * This needs to be moved out to a string table for localisation 
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Constants;

/**
 * This class in not creatable, will be moved out to a string table soon
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public final class ReplacementTokens
{
    // TODO: Move this out to a string table for localisation
    
    private ReplacementTokens()
    {
    }
    
    public static final String METHOD = "<%method%>";
    public static final String PARAMETER = "<%parameter%>";
    public static final String TYPE = "<%type%>";
}
