/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Validation.Rules;

import Goliath.Collections.List;
import Goliath.Arguments.SingleParameterArguments;
import Goliath.Validation.RuleHandler;

/**
 *
 * @author kmchugh
 */
public class OneOfNotEmptyRule extends RuleHandler<java.lang.Object, java.lang.Number, SingleParameterArguments<List<String>>>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, Number toPropertyValue, SingleParameterArguments<List<String>> toArgs)
    {
        int lnCount = 0;
        for (String lcField : toArgs.getParameter())
        {
            Object loValue = Goliath.DynamicCode.Java.<Number>getPropertyValue(toTarget, lcField);
            if (loValue == null || loValue.toString().isEmpty())
            {
                lnCount++;
                if (lnCount > 1)
                {
                    return false;
                }
            }
        }
        return lnCount == 1;
    }


    @Override
    protected String onGetFailedMessage(Object toTarget, String tcPropertyName, Number toPropertyValue, SingleParameterArguments<List<String>> toArgs)
    {
        StringBuilder loBuilder = new StringBuilder("The following fields are null or empty ");
        loBuilder.append(" [");


        for (String lcField : toArgs.getParameter())
        {
            Object loValue = Goliath.DynamicCode.Java.<Number>getPropertyValue(toTarget, lcField);
            if (loValue == null || loValue.toString().isEmpty())
            {
                loBuilder.append(lcField + " ");
            }
        }
        loBuilder.append("]");
        return loBuilder.toString();
    }

    @Override
    protected boolean requiresArgs()
    {
        return true;
    }
}