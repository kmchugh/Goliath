/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Validation.Rules;

import Goliath.Collections.List;
import Goliath.Arguments.DoubleParameterArguments;
import Goliath.Validation.RuleHandler;

/**
 *
 * @author kmchugh
 */
public class OneOfMinValueRule extends RuleHandler<java.lang.Object, java.lang.Number, DoubleParameterArguments<List<String>, Number>>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, Number toPropertyValue, DoubleParameterArguments<List<String>, Number> toArgs)
    {
        int lnCount = 0;
        Number loMin = toArgs.getParameter2();

        for (String lcField : toArgs.getParameter1())
        {
            Number loValue = Goliath.DynamicCode.Java.<Number>getPropertyValue(toTarget, lcField);
            if (loValue != null && loValue.floatValue() < loMin.floatValue())
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
    protected String onGetFailedMessage(Object toObject, String tcPropertyName, Number toPropertyValue, DoubleParameterArguments<List<String>, Number> toArgs)
    {
        Number loMin = toArgs.getParameter2();

        StringBuilder loBuilder = new StringBuilder("The following fields have a value less than ");
        loBuilder.append(loMin);
        loBuilder.append(" [");


        for (String lcField : toArgs.getParameter1())
        {
            Number loValue = Goliath.DynamicCode.Java.<Number>getPropertyValue(toObject, lcField);
            if (loValue != null && loValue.floatValue() < loMin.floatValue())
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