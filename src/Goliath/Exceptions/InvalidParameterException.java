/* =========================================================
 * InvalidParameterException.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This error is used to notify when an invalid parameter is passed.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;

/**
 * This error is used to notify when an invalid parameter was passed
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class InvalidParameterException extends Goliath.Exceptions.UncheckedException
{
    private String m_cParameterName;
    private Object m_oValue;
    
    /**
    * Creates a new instance of InvalidParameterException
    * InvalidParameterException should be thrown whenever a call to a method occurs with an invalid parameter value
    *
    * @param tcMessage     The message for the exception
    * @param tcParameter   The parameter name that was invalid
    * @param toValue       The invalid value
    */
    public InvalidParameterException(String tcMessage, String tcParameter, java.lang.Object toValue)
    {
        super(tcMessage);
        m_cParameterName = tcParameter;
        m_oValue = toValue;
    }

    /**
    * Creates a new instance of InvalidParameterException
    * InvalidParameterException should be thrown whenever a call to a method occurs with an invalid parameter value
    *
    * @param tcMessage     The message for the exception
    * @param tcParameter   The parameter name that was invalid
    */
    public InvalidParameterException(String tcMessage, String tcParameter)    
    {
        super(tcMessage);
        m_cParameterName = tcParameter;
    }

    /**
    * Creates a new instance of InvalidParameterException
    * InvalidParameterException should be thrown whenever a call to a method occurs with an invalid parameter value
    *
    * @param tcParameter   The parameter name that was invalid
    */
    public InvalidParameterException(String tcParameter)
    {
        super(Goliath.Constants.Strings.AN_INVALID_PARAMETER_VALUE_WAS_PASSED.replaceAll(Goliath.Constants.ReplacementTokens.PARAMETER, tcParameter));
        m_cParameterName = tcParameter;
    }
    
    /**
    * Creates a new instance of InvalidParameterException
    * InvalidParameterException should be thrown whenever a call to a method occurs with an invalid parameter value
    *
    * @param tcParameter   The parameter name that was invalid
    * @param toValue       The invalid value
    */
    public InvalidParameterException(String tcParameter, Object toValue)
    {
        super(Goliath.Constants.Strings.AN_INVALID_PARAMETER_VALUE_WAS_PASSED.replaceAll(Goliath.Constants.ReplacementTokens.PARAMETER, tcParameter));
        m_oValue = toValue;
    }
    
    /**
    * Gets the invalid parameter name if known
     * @return the parameter that was invalid
     */
    public String getParameter()
    {
        return m_cParameterName;
    }
    
    /**
    * Gets the invalid value if known
     * @return the value that was invalid
     */
    public Object getValue()
    {
        return m_oValue;
    }
}
