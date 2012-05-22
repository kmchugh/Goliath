/* ========================================================
 * ValueRequiredRule.java
 *
 * Author:      kmchugh
 * Created:     Dec 13, 2010, 2:31:40 PM
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

import Goliath.Arguments.Arguments;
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
public class ValueRequiredRule extends RuleHandler<java.lang.Object, java.lang.Object, Arguments>
{

    @Override
    protected boolean onExecuteRule(Object toTarget, java.lang.Object toPropertyValue, Arguments toArgs)
    {
        return toPropertyValue != null;
    }

    @Override
    protected String onGetFailedMessage(Object toObject, String tcPropertyName, java.lang.Object toPropertyValue, Arguments toArgs)
    {
        return "A value is required for " + tcPropertyName;
    }

    @Override
    protected boolean requiresArgs()
    {
        return false;
    }





}