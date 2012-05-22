/* =========================================================
 * Exception.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This is the base exception class.  Creation of an 
 * exception will log it to the application.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Exceptions;

/**
 * This is the base exception class for the goliath framework
 * any exceptions that are subclassed from here will be logged
 * to the application when created
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class Exception extends java.lang.Exception
{
    /**
     * Creates a new instance of an exception
     */
    public Exception()
    {
        this(Goliath.Constants.Strings.A_SYSTEM_ERROR_HAS_OCCURED);
    }

    /**
    * Creates a new instance of Exception
    * Exception is the base ICatalyst Exception Class
    *
    * @param tcMessage          The message for the exception
    */
    public Exception(String tcMessage)
    {
        super(tcMessage);
        logException();
    }

    /**
     * Creates a new exception and allows the specification of a cause
     * By default the message will be the getLocalisedString() from the cause
     * @param toThrowable the cause of the exception
     */
    public Exception(Throwable toThrowable)
    {
        this(toThrowable.getLocalizedMessage(), toThrowable);
    }

    /**
     * Creates a new exception allowing the specification of the cause and a message
     * @param tcMessage the message for the exception
     * @param toThrowable the cause
     */
    public Exception(String tcMessage, Throwable toThrowable)
    {
        this(tcMessage, toThrowable, true);
    }
    
    /**
     * Creates a new exception allowing the specification of the cause and a message
     * @param tcMessage the message for the exception
     * @param toThrowable the cause
     * @param tlLogError true to log the exception
     */
    public Exception(String tcMessage, Throwable toThrowable, boolean tlLogError)
    {
        super(tcMessage, toThrowable);
        if (tlLogError)
        {
            logException();
        }
    }

    /**
     * Creates a new instance of exception and logs the exception if tlLogError is true
     * @param tcMessage the error message
     * @param tlLogError true to log the exception
     */
    public Exception(String tcMessage, boolean tlLogError)
    {
        super(tcMessage);
        if (tlLogError)
        {
            logException();
        }
    }

    @Override
    public String getLocalizedMessage()
    {
        return getMessages();               
    }

    private String getMessages()
    {
        StringBuilder loBuilder = new StringBuilder();
        Goliath.Utilities.appendToStringBuilder(loBuilder,
                Goliath.Utilities.isNull(super.getLocalizedMessage(), this.getClass().getSimpleName()),
                Goliath.Environment.NEWLINE());
        if (this.getCause() != null)
        {
            Goliath.Utilities.appendToStringBuilder(loBuilder,
                Goliath.Utilities.isNull(getCause().getLocalizedMessage(), getCause().getClass().getSimpleName()),
                Goliath.Environment.NEWLINE());
        }
        return loBuilder.toString();
    }

    @Override
    public StackTraceElement[] getStackTrace()
    {
        if (this.getCause()!= null)
        {
            return this.getCause().getStackTrace();
        }
        return super.getStackTrace();
    }
    
    /**
     * Logs the exception to the application
     */
    private void logException()
    {
        Goliath.Applications.Application.getInstance().log(this);
    }


    /**
     * Gets a string representation of the object
     *  To override the representation the formatString method must be overridden
     *
     * @return      The string representation of the object
     */
    @Override
    public final String toString()
    {
        return toString(Goliath.Constants.StringFormatType.DEFAULT());
    }

    /**
     * Gets a string representation of the object
     *  To override the representation the formatString method must be overridden
     *
     * @param toFormat  the format type to apply to the string representation
     * @return      The string representation of the object
     */
    public final String toString(Goliath.Constants.StringFormatType toFormat)
    {
        return formatString(toFormat);
    }

    /**
     * Creates a string representation of the object
     *
     * @param toFormat  the format type to apply to the string representation
     * @return      The string representation of the object
     */
    protected String formatString(Goliath.Constants.StringFormatType toFormat)
    {
        return this.getMessages();
    }
    
    
    
}
