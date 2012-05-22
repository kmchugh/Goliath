/* =========================================================
 * DatePart.java
 *
 * Author:      pstanbridge
 * Created:     29 Octo er 2010, 20:15
 *
 * Description
 * --------------------------------------------------------
 * This class is used to define the date parts allows for
 * date arithmetic
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Constants;

/**
 * This class is used to define the error type, allows for
 * filtering of log levels
 *
 * @version     1.0 10 December 2007
 * @author      kmchugh
 **/
public class DatePart extends Goliath.DynamicEnum
{

    /**
     * Creates a new instance of an DatePart Object
     * 
     * 
     * @param tcValue   The value of the DateType
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    public DatePart(String tcValue) 
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
 
    }
    
    private static DatePart g_oYear;
    private static DatePart g_oMonth;
    private static DatePart g_oWeek;
    private static DatePart g_oDay;
    private static DatePart g_oHour;
    private static DatePart g_oMinute;
    private static DatePart g_oSecond;
    private static DatePart g_oMilliSecond;
    // Juulian multiplier associated with this date part up to week confirmed at
    // https://svn.mse.jhu.edu/repos/public/dspace/branches/unit-testing/dspace-api/src/main/java/org/dspace/core/Utils.java
    private long g_nMultiplier;
    
    /**
     * Static singleton for Calendar YEAR
     * @return The YEAR date part
     */
    public static DatePart YEAR()
    {
        if (g_oYear == null)
        {
            try
            {
                g_oYear = new DatePart("YEAR");
               // g_oYear.g_nMultiplier = 31557600000L; //Julian year defined as 325.25 days - see http://wapedia.mobi/en/Years
                g_oYear.g_nMultiplier = 0L;

            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oYear;
    }
    
    /**
     * Static singleton for Calendar MONTH
     * @return The MONTH date part
     */
    public static DatePart MONTH()
    {
        if (g_oMonth == null)
        {
            try
            {
                g_oMonth = new DatePart("MONTH");
                g_oMonth.g_nMultiplier = 0L;
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oMonth;
    }
     /**
     * Static singleton for Calendar WEEK
     * @return The WEEK date part
     */
    public static DatePart WEEK()
    {
        if (g_oWeek == null)
        {
            try
            {
                g_oWeek = new DatePart("WEEK");
                g_oWeek.g_nMultiplier = 604800000L;
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oWeek;
    }
    /**
     * Static singleton for Calendar DAY
     * @return The DAY date part
     */
    public static DatePart DAY()
    {
        if (g_oDay == null)
        {
            try
            {
                g_oDay = new DatePart("DAY");
                g_oDay.g_nMultiplier = 86400000L;
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDay;
    }
    
    /**
     * Static singleton for Calendar HOUR
     * @return The HOUR date part
     */
    public static DatePart HOUR()
    {
        if (g_oHour == null)
        {
            try
            {
                g_oHour = new DatePart("HOUR");
                g_oHour.g_nMultiplier = 3600000L;
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oHour;
    }
    
    /**
     * Static singleton for Calendar MINUTE
     * @return The MINUTE date part
     */
    public static DatePart MINUTE()
    {
        if (g_oMinute == null)
        {
            try
            {
                g_oMinute = new DatePart("MINUTE");
                g_oMinute.g_nMultiplier = 60000L;
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oMinute;
    }
    
     
    
    /**
     * Static singleton for Calendar SECOND
     * @return The SECOND date part
     */
    public static DatePart SECOND()
    {
        if (g_oSecond == null)
        {
            try
            {
                g_oSecond = new DatePart("SECOND");
                g_oSecond.g_nMultiplier = 1000L;
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oSecond;
    }


    /**
     * Static singleton for Calendar MILLISECOND
     * @return The MILLISECOND date part
     */
    public static DatePart MILLISECOND()
    {
        if (g_oMilliSecond == null)
        {
            try
            {
                g_oMilliSecond = new DatePart("MILLISECOND");
                g_oMilliSecond.g_nMultiplier = 1l;
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oMilliSecond;
    }

    // get multiplier for each field

    public  long getMultiplier()
    {
        return g_nMultiplier;
    }
}
