/* =========================================================
 * BrokenRule.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 9, 2008, 7:27:43 PM
 * 
 * Description
 * --------------------------------------------------------
 * Represents a rule that has been broken during validation
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Validation;

import Goliath.Arguments.Arguments;

/**
 * Represents a rule that has been broken during validation
 *
 * @version     1.0 Jan 9, 2008
 * @author      Ken McHugh
**/
public class BrokenRule extends Goliath.Object 
{
    private String m_cRuleName;
    private String m_cMessage;
    private String m_cProperty;
    
    /** 
     * Creates a new instance of BrokenRule
     * 
     * @param toMethod the rule method that failed
     */
    public BrokenRule(Goliath.Validation.RuleHandler toHandler, java.lang.Object toObject, String tcPropertyName, java.lang.Object toPropertyValue, Arguments toArgs)
    {
        m_cRuleName = toHandler.getName();
        m_cMessage = toHandler.getFailedMessage(toObject, tcPropertyName,  toPropertyValue, toArgs);
        m_cProperty = tcPropertyName;
    }
    
    /** 
     * Creates a new instance of BrokenRule
     * 
     * @param tcRuleName The name of this rule
     * @param toException The exception that caused the failure
     */
    public BrokenRule(String tcRuleName, String tcProperty, Throwable toException)
    {
        m_cRuleName = tcRuleName;
        m_cMessage = toException.getLocalizedMessage();
        m_cProperty = tcProperty;
    }

    /**
     * Creates a new instance of BrokenRule
     *
     * @param tcRuleName The name of this rule
     * @param toException The exception that caused the failure
     */
    public BrokenRule(String tcRuleName, Throwable toException)
    {
        this(tcRuleName, "", toException);
    }

    /**
     * Gets the message, the message is usually why the rule failed
     * @return the message
     */
    public String getMessage()
    {
        return m_cMessage;
    }
    
    /**
     * Gets the name of the rule that has been broken
     * @return the name of the rule
     */
    public String getRuleName()
    {
        return m_cRuleName;
    }
    
    /**
     * Gets the name of the property that was checked with this rule
     * @return the property name
     */
    public String getProperty()
    {
        return m_cProperty;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BrokenRule other = (BrokenRule) obj;
        if ((this.m_cRuleName == null) ? (other.m_cRuleName != null) : !this.m_cRuleName.equals(other.m_cRuleName)) {
            return false;
        }
        if ((this.m_cProperty == null) ? (other.m_cProperty != null) : !this.m_cProperty.equals(other.m_cProperty)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.m_cRuleName != null ? this.m_cRuleName.hashCode() : 0);
        hash = 97 * hash + (this.m_cProperty != null ? this.m_cProperty.hashCode() : 0);
        return hash;
    }


    
    
    
    
}
