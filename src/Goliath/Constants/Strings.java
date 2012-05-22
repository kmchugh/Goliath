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
 * This class is currently being used for error message control
 * This will need to be moved out to a string table for localisation
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public final class Strings
{
    // TODO: Add localisation
    private Strings()
    {
    }
    
    public static final String A_SYSTEM_ERROR_HAS_OCCURED = "A system error has occurred";
    public static final String AN_INVALID_OPERATION_WAS_ATTEMPTED = "An invalid operation was attempted";
    public static final String AN_INVALID_PARAMETER_VALUE_WAS_PASSED = "The parameter <%parameter%> has an invalid value";
    public static final String METHOD_NOT_IMPLEMENTED = "<%method%> is not implemented in the type <%type%>";
    public static final String OBJECT_NOT_CREATED = "Object <%type%> was not created";
    public static final String TYPE_HAS_NO_PUBLIC_DEFAULT_CONSTRUCTOR = "Type <%type%> has no public default constructor";
    public static final String TYPE_IS_NOT_A_CLASS = "Type <%type%> is not a class";
    
}
