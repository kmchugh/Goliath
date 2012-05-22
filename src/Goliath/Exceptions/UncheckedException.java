/* =========================================================
 * UncheckedExceptions.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 16, 2008, 4:13:51 AM
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
 * =======================================================*/

package Goliath.Exceptions;

/**
 * Exceptions that are subclassed from this class are only checked at runtime
 * so a method is not required to declare throwing them
 *
 * @version     1.0 Jan 16, 2008
 * @author      Ken McHugh
**/
public class UncheckedException extends java.lang.RuntimeException
{
    /**
     * Creates a new instance of an exception
     */
    public UncheckedException()
    {
        this(Goliath.Constants.Strings.A_SYSTEM_ERROR_HAS_OCCURED);
    }

    /**
    * Creates a new instance of Exception
    * Exception is the base ICatalyst Exception Class
    *
    * @param tcMessage          The message for the exception
    */
    public UncheckedException(String tcMessage)
    {
        this(tcMessage, true);
    }

    /**
    * Creates a new instance of Exception
    * Exception is the base ICatalyst Exception Class
    *
     * @param toException   The inner exception that occured
     * @param tlLogError if true the error will be logged using the logger
    */
    public UncheckedException(String tcMessage, boolean tlLogError)
    {
        super(tcMessage);
        if (tlLogError)
        {
            logException();
        }
    }
    
    /**
    * Creates a new instance of Exception
    * Exception is the base ICatalyst Exception Class
    *
    * @param tcMessage          The message for the exception
    * @param toInnerException   The inner exception that occured
    */
    public UncheckedException(String tcMessage, Throwable toInnerException)
    {
        super(tcMessage, toInnerException);
        logException();
    }

    /**
    * Creates a new instance of Exception
    * Exception is the base ICatalyst Exception Class
    *
    * @param toException   The inner exception that occured
    */
    public UncheckedException(Throwable toException)
    {
        super(toException);
        logException();
    }
    
    /**
    * Creates a new instance of Exception
    * Exception is the base ICatalyst Exception Class
    *
     * @param toException   The inner exception that occured
     * @param tlLogError if true the error will be logged using the logger
    */
    public UncheckedException(Throwable toException, boolean tlLogError)
    {
        super(toException);
        if (tlLogError)
        {
            logException();
        }
    }
    
    /**
     * Logs the exception to the application
     */
    private void logException()
    {
        Goliath.Applications.Application.getInstance().log(this);
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
