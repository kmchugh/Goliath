/* ========================================================
 * MaximumLengthRule.java
 *
 * Author:      kmchugh
 * Created:     Dec 13, 2010, 2:40:22 PM
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
 * @version     1.0 Dec 13, 2010
 * @author      kmchugh
**/
public class RegExRule extends RuleHandler<java.lang.Object, String, SingleParameterArguments<String>>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, String toPropertyValue, SingleParameterArguments<String> toArgs)
    {
        if (toPropertyValue != null)
        {
                return Goliath.Utilities.getRegexMatcher(toArgs.getParameter(), toPropertyValue).matches();
        }

        return true;
    }

    @Override
    protected String onGetFailedMessage(Object toObject, String tcPropertyName, String toPropertyValue, SingleParameterArguments<String> toArgs)
    {
        return "Value of " + tcPropertyName + "[" + toPropertyValue.toString() + "]" + " does not match regular expression " + toArgs.getParameter().toString();
    }

    @Override
    protected boolean requiresArgs()
    {
        return true;
    }
}