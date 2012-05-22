/* =========================================================
 * RegularExpression.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 10, 2008, 11:16:00 PM
 * 
 * Description
 * --------------------------------------------------------
 * Library of regular expressions
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Constants;

import Goliath.Exceptions.InvalidParameterException;

/**
 * A library of regular expressions
 *
 * @version     1.0 Jan 10, 2008
 * @author      Ken McHugh
**/
public class RegularExpression extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of a RegularExpression Object 
     *
     * @param tcValue The regular expression
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public RegularExpression(String tcValue) 
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }
    
    private static RegularExpression g_oUSSocialSecurityNumber;
    /**
     *  Static singleton for DEFAULT formatting
     * @return The regular expression string for a social security number
     */
    public static RegularExpression US_SOCIAL_SECURITY_NUMBER()
    {
        if (g_oUSSocialSecurityNumber == null)
        {
            try
            {
                g_oUSSocialSecurityNumber = new RegularExpression("^\\d{3}-\\d{2}-\\d{4}$");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oUSSocialSecurityNumber;
    }
    
    private static RegularExpression g_oEmailAddress;
    /**
     *  Matches to an email address
     * @return The regular expression string for an email address
     */
    public static RegularExpression EMAIL_ADDRESS()
    {
        if (g_oEmailAddress == null)
        {
            try
            {
                // \b[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b
                g_oEmailAddress = new RegularExpression("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oEmailAddress;
    }

    private static RegularExpression g_oURL;
    /**
     * Matches to a URL
     * @return the regular expression that will match a URL
     */
    public static RegularExpression URL()
    {
        if (g_oURL == null)
        {
            try
            {
                g_oURL = new RegularExpression("^(https?:/)?/?(?:[\\w_-]+(?:\\.[\\w_\\-]+)|localhost:[\\d]+)[\\w/\\.]+/?.+$");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oURL;
    }


    private static RegularExpression g_oPasswordStrong;

    /**
     * Matches a password that is 6 - 20 characters, contains at least one lower case, one upper case, one special character, and one number
     * @return the password regex
     */
    public static RegularExpression PASSWORD_STRONG()
    {
        if (g_oPasswordStrong == null)
        {
            try
            {
                g_oPasswordStrong = new RegularExpression("(?=^.{6,20}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+-}{\"\":;'?/>.<,]).*$");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oPasswordStrong;

    }

    private static RegularExpression g_oPhoneNumber_US;
    
    /**
     * Matches a password that is 6 - 20 characters, contains at least one lower case, one upper case, one special character, and one number
     * @return the password regex
     */
    public static RegularExpression PHONENUMBER_US()
    {
        if (g_oPhoneNumber_US == null)
        {
            try
            {
                g_oPhoneNumber_US = new RegularExpression("/^(1[-\\s.])?(\\()?\\d{3}(?(2)\\))[-\\s.]?\\d{3}[-\\s.]?\\d{4}$/");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oPhoneNumber_US;

    }
    
    private static RegularExpression g_oGUID;
    
    /**
     * Matches a regular GUID type string
     * @return the guid regex
     */
    public static RegularExpression GUID()
    {
        if (g_oGUID == null)
        {
            try
            {
                g_oGUID = new RegularExpression("^(\\{){0,1}[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}(\\}){0,1}$");
            }
            catch (InvalidParameterException ex)
            {}
        }
        return g_oGUID;
    }

}
