/* ========================================================
 * MinimumLengthRule.java
 *
 * Author:      kmchugh
 * Created:     Dec 13, 2010, 2:40:30 PM
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
import Goliath.DynamicCode.Java;
import Goliath.Validation.RuleHandler;
import java.lang.reflect.Array;
import java.util.List;


        
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
public class MinimumLengthRule extends RuleHandler<java.lang.Object, java.lang.Object, SingleParameterArguments<Number>>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, java.lang.Object toPropertyValue, SingleParameterArguments<Number> toArgs)
    {
        if (toPropertyValue != null)
        {
            if (Java.isEqualOrAssignable(List.class, toPropertyValue.getClass()))
            {
                return ((List)toPropertyValue).size() >= toArgs.getParameter().intValue();
            }
            else if (Java.isEqualOrAssignable(String.class, toPropertyValue.getClass()))
            {
                return ((String)toPropertyValue).length() >= toArgs.getParameter().intValue();
            }
            else if (toPropertyValue.getClass().isArray())
            {
                return Array.getLength(toPropertyValue) >= toArgs.getParameter().intValue();
            }
        }
        return false;
    }

    @Override
    protected String onGetFailedMessage(Object toObject, String tcPropertyName, java.lang.Object toPropertyValue, SingleParameterArguments<Number> toArgs)
    {
        return "Value of " + tcPropertyName + "[" + toPropertyValue.toString() + "]" + " is longer than " + toArgs.getParameter().toString();
    }

    @Override
    protected boolean requiresArgs()
    {
        return true;
    }
}