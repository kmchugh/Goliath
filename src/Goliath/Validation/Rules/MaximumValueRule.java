/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Validation.Rules;

import Goliath.Arguments.SingleParameterArguments;
import Goliath.Validation.RuleHandler;

/**
 *
 * @author kmchugh
 */
public class MaximumValueRule extends RuleHandler<java.lang.Object, java.lang.Number, SingleParameterArguments<Number>>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, Number toPropertyValue, SingleParameterArguments<Number> toArgs)
    {
        return toPropertyValue != null && toArgs.getParameter() != null && toPropertyValue.doubleValue() <= toArgs.getParameter().doubleValue();
    }

    @Override
    protected String onGetFailedMessage(Object toObject, String tcPropertyName, Number toPropertyValue, SingleParameterArguments<Number> toArgs)
    {
        return "Value of " + tcPropertyName + "[" + toPropertyValue.toString() + "]" + " is larger than " + toArgs.getParameter().toString();
    }

    @Override
    protected boolean requiresArgs()
    {
        return true;
    }
}
