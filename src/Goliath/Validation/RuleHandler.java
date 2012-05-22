/* =========================================================
 * RuleHandler.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 10, 2008, 12:20:45 AM
 * 
 * Description
 * --------------------------------------------------------
 * A delegate method for checking rules against an object 
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Validation;

import Goliath.Exceptions.InvalidParameterException;
import Goliath.Arguments.Arguments;

/**
 * A delegate method for checking rules against an object
 *
 * @version     1.0 Jan 10, 2008
 * @author      Ken McHugh
**/
public abstract class RuleHandler<O extends java.lang.Object, P extends java.lang.Object, A extends Arguments> extends Goliath.Object
{
    private final String m_cName;

    public RuleHandler(String tcName)
            throws Goliath.Exceptions.ObjectNotCreatedException
    {
        m_cName = tcName;
    }

    public RuleHandler()
            throws Goliath.Exceptions.ObjectNotCreatedException
    {
       m_cName = getClass().getSimpleName();
    }
    
    /**
     * Starts the method
     * @param toTarget the object that is being checked by the rule
     * @param toArgs the arguments for the rule
     * @return true if the rule passed
     */
    public final boolean validate(O toTarget, P toPropertyValue, A toArgs)
    {
        boolean llReturn = false;

        try
        {
            // If arguments are required but not provided, fail the rule
            if (requiresArgs() && toArgs == null)
            {
                throw new InvalidParameterException("Arguments were required but not passed", "toArgs");
            }
            llReturn = onExecuteRule(toTarget, toPropertyValue, toArgs);
        }
        catch (Throwable ex)
        {
            toArgs.setMessage(ex.toString());
            llReturn = false;
        }
        return llReturn;
    }
    
    /**
     * Gets the name of the method that will be run by this rule
     * @return the name of the method
     */
    public final String getName()
    {
        return m_cName;
    }

    public final String getFailedMessage(O toTarget, String tcPropertyName, P toPropertyValue, A toArgs)
    {
        String lcString = onGetFailedMessage(toTarget, tcPropertyName, toPropertyValue, toArgs);
        if (Goliath.Utilities.isNullOrEmpty(lcString))
        {
            // Generate a default message
            lcString = "The property " + tcPropertyName + " failed to validate against " + getName() + " becase it's value was [" + ((toPropertyValue == null) ? "null" : toPropertyValue.toString());
        }
        return lcString;
    }

    /**
     * Gets the list of failed messages from the
     * @param toObject the server connection that could not be connected to
     * @param tcPropertyName the property that we were validating with this rule
     * @param toPropertyValue the value of the property we were validating
     * @param toArgs the arguments provided
     * @return the error string
     */
    protected String onGetFailedMessage(O toTarget, String tcPropertyName, P toPropertyValue, A toArgs)
    {
        return null;
    }

    /**
     * Called when the rule needs to be executed
     * @param toTarget the target object of the rule, this is the object being validated
     * @param tcPropertyValue the value of the property being assessed for validity
     * @param toArgs The arguments paassed to the Rule
     * @return true if this property passes the validation rule, otherwise false
     */
    protected abstract boolean onExecuteRule(O toTarget, P toPropertyValue, A toArgs);

    /**
     * Determines if arguments are required for this rule
     * @return true if arguments are required, false if they are not
     */
    protected abstract boolean requiresArgs();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RuleHandler other = (RuleHandler) obj;
        if ((this.m_cName == null) ? (other.m_cName != null) : !this.m_cName.equals(other.m_cName)) {
            return false;
        }
        
        return true;
    }



    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.m_cName != null ? this.m_cName.hashCode() : 0);
        return hash;
    }



}
