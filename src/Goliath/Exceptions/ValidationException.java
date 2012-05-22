/* =========================================================
 * ValidationException.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 10, 2008, 9:21:40 PM
 * 
 * Description
 * --------------------------------------------------------
 * Used for capturing validation errors.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;

import Goliath.Validation.BrokenRulesCollection;

/**
 * used for capturing validation errors
 *
 * @version     1.0 Jan 10, 2008
 * @author      Ken McHugh
**/
public class ValidationException extends Goliath.Exceptions.UncheckedException
{
    private BrokenRulesCollection m_oBrokenRules = null;
    
    /**
    * Creates a new instance of InvalidOperationException
    *
    * @param toBrokenRules   The list of broken rules
    */
    public ValidationException(BrokenRulesCollection toBrokenRules)
    {
        super(toBrokenRules.toString());
        m_oBrokenRules = toBrokenRules;
    }
    
    /**
    * Creates a new instance of InvalidOperationException
    *
    * @param toBrokenRules   The list of broken rules
    * @param toException The inner exception
    */
    public ValidationException(BrokenRulesCollection toBrokenRules, java.lang.Exception toException)
    {
        super(toBrokenRules.toString(), toException);
        m_oBrokenRules = toBrokenRules;
    }
    
    public BrokenRulesCollection getBrokenRules()
    {
        return m_oBrokenRules;        
    }
}
