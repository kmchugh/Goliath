/* =========================================================
 * BrokenRulesCollection.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 10, 2008, 12:46:00 AM
 * 
 * Description
 * --------------------------------------------------------
 * Represents a list of broken validation rules, a broken validation
 * rule is a rule that has failed it's test
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Validation;

/**
 * Represents a list of broken validation rules, a broken validation
 * rule is a rule that has failed it's test
 *
 * @version     1.0 Jan 10, 2008
 * @author      Ken McHugh
**/

public class BrokenRulesCollection extends Goliath.Collections.List<Goliath.Validation.BrokenRule> 
{
    /** Creates a new instance of BrokenRulesCollection */
    public BrokenRulesCollection()
    {
    }
    
    /**
     * Gets the first broken rule from the list where the property name matches
     * tcProperty
     * @param tcPropertyName the property name to get the broken rule for
     * @return the broken rule that matches the property
     */
    public BrokenRule getFirstBrokenRule(String tcPropertyName)
    {
        for (BrokenRule loRule : this)
        {
            if (loRule.getProperty().equals(tcPropertyName))
            {
                return loRule;
            }
        }
        return null;
    }

    /**
     * Adds an exception to the broken rule collection
     * @param tcMethod The method that failed
     * @param toException The exception that occurred that caused the failure
     * @return true if this method changed the collection
     */
    public boolean add(String tcName, Throwable toException)
    {
        return add(new BrokenRule(tcName, toException));
    }
    
    /**
     * removes a broken rule from the collection
     * @param toRuleMethod the rule method to remove from the collection
     * @return true if this method changed the collection
     */
    public boolean remove(String tcName)
    {
        return add(new BrokenRule(tcName, null));
    }
    
    @Override
    public String toString() 
    {
        java.lang.StringBuilder loString = new java.lang.StringBuilder();
        boolean llFirst = true;
        for (BrokenRule loRule : this)
        {
            if (llFirst)
            {
                llFirst = false;
            }
            else
            {
                loString.append(Goliath.Environment.NEWLINE());
            }
            loString.append(loRule.getMessage());
        }
        return loString.toString();
    }
}
