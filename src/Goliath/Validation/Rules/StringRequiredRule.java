/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Validation.Rules;

import Goliath.Arguments.Arguments;
import Goliath.Validation.RuleHandler;

/**
 *
 * @author kmchugh
 */
public class StringRequiredRule extends RuleHandler<java.lang.Object, String, Arguments>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, String toPropertyValue, Arguments toArgs)
    {
        return !Goliath.Utilities.isNullOrEmpty(toPropertyValue);
    }

    @Override
    protected String onGetFailedMessage(Object toObject, String tcPropertyName, String toPropertyValue, Arguments toArgs)
    {
        return "A value is required for " + tcPropertyName;
    }

    @Override
    protected boolean requiresArgs()
    {
        return false;
    }





}
