/* ========================================================
 * NoNullIfEmpty.java
 *
 * Author:      christinedorothy
 * Created:     Jul 27, 2011, 2:26:33 PM
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

package Goliath.Validation.Rules;

import Goliath.Arguments.SingleParameterArguments;
import Goliath.Validation.RuleHandler;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jul 27, 2011
 * @author      christinedorothy
**/
public class NoNullIfEmptyRule extends RuleHandler<java.lang.Object, java.lang.Object, SingleParameterArguments<String>>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, Object toPropertyValue, SingleParameterArguments<String> toArgs)
    {
        Object loValue = Goliath.DynamicCode.Java.<Number>getPropertyValue(toTarget, toArgs.getParameter());
        if (loValue == null || loValue.toString().isEmpty())
        {
            if (toPropertyValue == null || toPropertyValue.toString().isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    protected String onGetFailedMessage(Object toTarget, String tcPropertyName, Object toPropertyValue, SingleParameterArguments<String> toArgs)
    {
        StringBuilder loReturn = new StringBuilder("The value of ");
        loReturn.append(tcPropertyName);
        loReturn.append(" should not be null when ");
        loReturn.append(toArgs.getParameter());
        loReturn.append(" is null.");

        return loReturn.toString();
    }

    @Override
    protected boolean requiresArgs()
    {
        return true;
    }
    
}
