/* =========================================================
 * CommonBusinessRules.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 10, 2008, 9:44:24 PM
 * 
 * Description
 * --------------------------------------------------------
 * A library of common business rules to be reused
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Validation;

import Goliath.Validation.Rules.*;

/**
 * A library of common business rules to be reused
 *
 * @version     1.0 Jan 10, 2008
 * @author      Ken McHugh
**/
public class CommonBusinessRules 
{
    
    private static RuleHandler g_oMINVALUE;
    private static RuleHandler g_oMAXVALUE;
    private static RuleHandler g_oSTRINGREQUIRED;
    private static RuleHandler g_oONEOFNOTEMPTY;
    private static RuleHandler g_oONEOFMINVALUE;
    
    public static RuleHandler STRINGREQUIRED()
    {
        if (g_oSTRINGREQUIRED == null)
        {
            g_oSTRINGREQUIRED = new StringRequiredRule();
        }
        return g_oSTRINGREQUIRED;
    }
    public static RuleHandler MINVALUE()
    {
        if (g_oMINVALUE == null)
        {
            g_oMINVALUE = new MinimumValueRule();
        }
        return g_oMINVALUE;
    }
    public static RuleHandler MAXVALUE()
    {
        if (g_oMAXVALUE == null)
        {
            g_oMAXVALUE = new MaximumValueRule();
        }
        return g_oMAXVALUE;
    }
    public static RuleHandler ONEOFNOTEMPTY()
    {
        if (g_oONEOFNOTEMPTY == null)
        {
            g_oONEOFNOTEMPTY = new OneOfNotEmptyRule();
        }
        return g_oONEOFNOTEMPTY;
    }
    public static RuleHandler ONEOFMINVALUE()
    {
        if (g_oONEOFMINVALUE == null)
        {
            g_oONEOFMINVALUE = new OneOfMinValueRule();
        }
        return g_oONEOFMINVALUE;
    }
}
