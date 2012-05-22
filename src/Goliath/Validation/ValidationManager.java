/* ========================================================
 * ValidationManager.java
 *
 * Author:      kmchugh
 * Created:     Aug 1, 2010, 9:31:31 AM
 *
 * Description
 * --------------------------------------------------------
 * Objects that need validation should be composed with a validation manager.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Validation;

import Goliath.Arguments.Arguments;
import Goliath.Interfaces.IValidatable;
import Goliath.DynamicCode.Java;
import Goliath.Exceptions.InvalidPropertyException;
import Goliath.Collections.List;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Aug 1, 2010
 * @author      kmchugh
**/
public class ValidationManager extends Goliath.Object
        implements IValidatable
{

    private java.lang.Object m_oValidationObject;
    private BrokenRulesCollection m_oBrokenRules;


    /**
     * Creates a new instance of ValidationManager
     */
    public ValidationManager(java.lang.Object toValidationObject)
    {
        m_oValidationObject = toValidationObject;
    }

    /**
     * Adds a rule to the global Validator.  If this rule already exists in the Validator
     * it won't be added again.  Rules added using this method will be used to validate every
     * instance of the class they are being added for, the rules added with this method will be
     * usable by every validation manager that exists
     * @param toRule The rule or constraint to use to validate the objects
     * @param tcProperty the name of the property to validate.  This will use reflection to extract an actual value from the object being validated
     * @param toArgs the arguments that are required to validate an object
     */
    @Override
    public <T extends RuleHandler> void addClassValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs)  throws InvalidPropertyException
    {
        // Check the existence of the property
        if (!Java.getClassDefinition(m_oValidationObject.getClass()).getProperties().contains(tcProperty.toLowerCase()))
        {
             throw new InvalidPropertyException(m_oValidationObject.getClass().getName(), tcProperty);
        }

        
        ClassValidations.addRule(m_oValidationObject.getClass(), toRuleClass, tcProperty, toArgs);
    }

    /**
     * Adds a rule to the Validator.  If this rule already exists in the Validator
     * it won't be added again.  Rules added using this method will be used to validate any object
     * that uses this specific validation manager
     * @param toRule The rule or constraint to use to validate the objects
     * @param tcProperty the name of the property to validate.  This will use reflection to extract an actual value from the object being validated
     * @param toArgs the arguments that are required to validate an object
     */
    @Override
    public <T extends RuleHandler> void addValidationRule(Class<T> toRuleClass, String tcProperty, Arguments toArgs) throws InvalidPropertyException
    {
        // TODO: Implement this properly.  This should be checking a custom rule against the object so the rule should be stored local
        // TODO: Implement adding rules to a specific object as well
        if (!Java.getClassDefinition(m_oValidationObject.getClass()).getProperties().contains(tcProperty.toLowerCase()))
        {
            throw new InvalidPropertyException(m_oValidationObject.getClass().getName(), tcProperty);
        }
    }


    /**
     * Gets the rules that have been broken for the entire object
     * @return the list of broken rules for the object or null if there were no broken rules
     */
    @Goliath.Annotations.NotProperty
    @Override
    public final Goliath.Validation.BrokenRulesCollection getBrokenRules()
    {
        return m_oBrokenRules;
    }
    
    /**
     * Adds the validation exception to the list
     * @param tcRuleName the name of the rule
     * @param ex the exception
     */
    @Override
    public final boolean addValidationException(String tcRuleName, String tcPropertyName, Throwable ex)
    {
        return addBrokenRule(new BrokenRule(tcRuleName, tcPropertyName, ex));
    }

    /**
     * Adds the validation exception to the list
     * @param tcRuleName the name of the rule
     * @param ex the exception
     */
    @Override
    public final boolean addValidationException(String tcRuleName, Throwable ex)
    {
        return addValidationException(tcRuleName, "", ex);
    }

    /**
     * Adds the broken rule to the list
     * @param tcRuleName the name of the rule
     */
    private boolean addBrokenRule(BrokenRule toRule)
    {
        if (m_oBrokenRules == null)
        {
            m_oBrokenRules = new BrokenRulesCollection();
        }
        if (!m_oBrokenRules.contains(toRule))
        {
            return m_oBrokenRules.add(toRule);
        }
        return false;
    }

    /**
     * Gets the rules that have been broken for a specific property
     * @param tcProperty the property to get the broken rules for
     * @return the list of broken rules for the object or null if there were no broken rules
     */
    @Goliath.Annotations.NotProperty
    @Override
    public final Goliath.Validation.BrokenRulesCollection getBrokenRules(String tcProperty)
    {
        return (m_oBrokenRules == null) ? null : ClassValidations.filterBrokenRulesByProperty(m_oBrokenRules, tcProperty);
    }


    /**
     * Checks if the object is valid
     * @return true if the object was valid as of the last check
     */
    @Override
    public final boolean isValid()
    {
        return isValid(false);
    }

    /**
     * Checks if the object is valid
     * @return true if the object was valid as of the last check
     */
    @Override
    public final boolean isValid(boolean tlClearBrokenRules)
    {
        if (tlClearBrokenRules)
        {
            m_oBrokenRules = null;
        }

        if (m_oBrokenRules == null)
        {
            m_oBrokenRules = ClassValidations.validate(m_oValidationObject);
        }
        return m_oBrokenRules.size() == 0;
    }
}
