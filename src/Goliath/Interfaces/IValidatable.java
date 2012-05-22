/* ========================================================
 * IValidatable.java
 *
 * Author:      kmchugh
 * Created:     Aug 1, 2010, 9:32:38 AM
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

package Goliath.Interfaces;

import Goliath.Arguments.Arguments;
import Goliath.Validation.RuleHandler;
import Goliath.Exceptions.InvalidPropertyException;



/**
 * Implements a validation manager on an object, allowing an object to be validated
 * To implement only the following code is needed:
 * <pre>
    private ValidationManager m_oValidationManager;


    private ValidationManager getValidationManger()
    {
        if (m_oValidationManager == null)
        {
            m_oValidationManager = new ValidationManager(this);
        }
        return m_oValidationManager;
    }

    @Override
    public <T extends RuleHandler> void addClassValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs)
    {
        getValidationManager().addClassValidationRule(toRuleClass, tcProperty, toArgs);
    }

    @Override
    public <T extends RuleHandler> void addValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs)
    {
        getValidationManager().addValidationRule(toRuleClass, tcProperty, toArgs);
    }

    @Override
    public BrokenRulesCollection getBrokenRules()
    {
        return (m_oValidationManager != null) ? m_oValidationManager.getBrokenRules() : null;
    }

    @Override
    public BrokenRulesCollection getBrokenRules(String tcProperty)
    {
        return (m_oValidationManager != null) ? m_oValidationManager.getBrokenRules(tcProperty) : null;
    }

    @Override
    public boolean addValidationException(String tcRuleName, Throwable toException)
    {
        return getValidationManager().addValidationException(tcRuleName, toException);
    }

    @Override
    public boolean isValid()
    {
        return isValid(isDirty());
    }

    @Override
    public boolean isValid(boolean tlClearBrokenRules)
    {
        return (m_oValidationManager != null) ? m_oValidationManager.isValid(tlClearBrokenRules) : true;
    }
 *
 * 
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 1, 2010
 * @author      kmchugh
**/
public interface IValidatable
{
    /**
     * Adds a rule to the global Validator for the class specified.  If this rule already exists in the Validator
     * it won't be added again.  Rules added using this method will be used to validate every
     * instance of the class they are being added for, the rules added with this method will be
     * usable by every validation manager that exists
     * @param toRule The rule or constraint to use to validate the objects
     * @param tcProperty the name of the property to validate.  This will use reflection to extract an actual value from the object being validated
     * @param toArgs the arguments that are required to validate an object
     * @throws InvalidPropertyException if the property does not exist on the class the rule is being added to
     */
    <T extends RuleHandler> void addClassValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs) throws InvalidPropertyException;

    /**
     * Adds a rule to the Validator for this specific object.  If this rule already exists in the Validator
     * it won't be added again.
     * @param toRule The rule or constraint to use to validate the objects
     * @param tcProperty the name of the property to validate.  This will use reflection to extract an actual value from the object being validated
     * @param toArgs the arguments that are required to validate an object
     */
    <T extends RuleHandler> void addValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs) throws InvalidPropertyException;;

    /**
     * Gets the rules that have been broken for the entire object
     * @return the list of broken rules for the object or null if there were no broken rules
     */
    Goliath.Validation.BrokenRulesCollection getBrokenRules();

    /**
     * Gets the rules that have been broken for a specific property
     * @param tcProperty the property to get the broken rules for
     * @return the list of broken rules for the object or null if there were no broken rules
     */
    Goliath.Validation.BrokenRulesCollection getBrokenRules(String tcProperty);

    /**
     * Adds the validation exception to the list
     * @param tcRuleName
     * @param toException
     * @return true if the validation exceptoin list was changed due to this call
     */
    boolean addValidationException(String tcRuleName, Throwable toException);

    /**
     * Adds the validation exception to the list
     * @param tcRuleName
     * @param tcPropertyName the property to associate this error with
     * @param toException
     * @return true if the validation exceptoin list was changed due to this call
     */
    boolean addValidationException(String tcRuleName, String tcPropertyName, Throwable toException);

    /**
     * Checks if the object is valid, this will not clear and revalidate.
     * @return true if the object was valid as of the last check
     */
    boolean isValid();

    /**
     * Checks if the object is valid
     * @param tlClearBrokenRules if true, then the broken rules will be cleared and the object revalidated, if false then the value from last check will be returned
     * @return true if the object was valid as of the last check
     */
    boolean isValid(boolean tlClearBrokenRules);

}
