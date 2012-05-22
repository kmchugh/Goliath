/* =========================================================
 * Date.java
 *
 * Author:      kmchugh
 * Created:     28-Jan-2008, 13:57:42
 * 
 * Description
 * --------------------------------------------------------
 * Utility date class used for making working with dates easier
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

import Goliath.Applications.Application;
import Goliath.Constants.StringFormatType;
import Goliath.Constants.DatePart;
import Goliath.Exceptions.InvalidParameterException;
/**
 * Utility date class used for making working with dates easier
 *
 * @version     1.0 28-Jan-2008
 * @author      kmchugh
**/
public class Date extends Goliath.Object implements java.lang.Comparable<Goliath.Date>  
{

    /**
     * NOTE - this date method uses some of the demoted parts of the java.lang.Date class.
     *        This is buggy for working with dates in BC, so date functions should keep the returned date in AD
     * @param toDate the date being added/subtracted to/from
     * @param tnUnits how many units to add or subtract from the passed date
     * @param toDatePart which part of the date the units to be added/subtracted to/from. E.g. the YEAR, MONTH, etc.
     * @return a new date which is the passed in date with the units added/subtracted to/from the date part
     */
    public static Date addDatePart(Date toDate, long tnUnits, DatePart toDatePart)
            throws InvalidParameterException
    {
        // TODO - fix this so that it can work with the full date range. See comment below
        /**
         * The Date class setYear() and getYear() algorithms return silly values when working in BC date ranges. One can subtract a few days
         * from a BC date and end up well into the future AD!!
         * The program can only guarantee to work in AD for now. See ToDo above.
         */
            // don't allow BC dates for now - see ToDo above.
            if (toDate.getLong() < -62138456748000L)
            {
                throw new Goliath.Exceptions.InvalidParameterException("Date of " + toDate.toString() + " passed into addDatePart must be after Year -1899 (1AD) for date part " + toDatePart + " and units " + tnUnits, "toDate");
            }
            // if passing unit of 0 (neither adding or subtracting) then just return passed in date
            if (tnUnits == 0) return new Date(toDate.getLong());
            // Date part is years
            if (toDatePart.equals(DatePart.YEAR()))
            {
                //for years, the units must be in integer range
                if (tnUnits < Integer.MIN_VALUE || tnUnits > Integer.MAX_VALUE)
                {
                    throw new Goliath.Exceptions.InvalidParameterException("Incremental value of " + tnUnits + " for year increment/decrement must be in Integer range", "tnUnits");
                }
                // don't allow the calculation to take one into BC date
                if ((toDate.getLong() >= -62138456748000L) && (tnUnits < 0) && ((toDate.getLong() + (tnUnits * 31557600000L)) < -62138456748000L))
                {
                    throw new Goliath.Exceptions.InvalidParameterException("Date of " + toDate.toString() + " and year units of " + tnUnits + " passed into addDatePart causes the calculation to drop before -1899 (1AD)", "tnUnits");
                }
                // if the year is going to increment beyond the largest value possible for a Long date then set to max (the value will in fact go below
               // -62106920748000L (into BC) but the test below for < 0 is fine)
                if ((tnUnits > 0) && ((toDate.getLong() + (tnUnits * 31557600000L)) < 0))
                {
                    return new Date(Long.MAX_VALUE);
                }
                // Create a new date object and set its year to the incremented/decremented amount
                java.util.Date loJavaDate = new java.util.Date(toDate.getLong());
                loJavaDate.setYear(loJavaDate.getYear() + (int) tnUnits);
                return new Date(loJavaDate);
            }
            // Date part Months
            else if (toDatePart.equals(DatePart.MONTH()))
            {
                //for months, the units must be in integer range
                if (tnUnits < Integer.MIN_VALUE || tnUnits > Integer.MAX_VALUE)
                {
                    throw new Goliath.Exceptions.InvalidParameterException("Incremental value of  " + tnUnits + " for month increment/decrement must be in Integer range", "tnUnits");
                }
                // Don't let calculation take the date into BC (assume 31 day months for calculation
                if ((toDate.getLong() >= -62138456748000L) && (tnUnits < 0) && ((toDate.getLong() + (tnUnits * 2678400000L)) < -62138456748000L))
                {
                    throw new Goliath.Exceptions.InvalidParameterException("Date of " + toDate.toString() + "and month units of " + tnUnits + " passed into addDatePart causes the calculation to drop before -1899 (1AD)", "tnUnits");
                }
               // if the caclulation takes the date passed the maximum value then set to maximum value (the value will in fact go below
               // -62106920748000L (into BC) but the test below for < 0 is fine)
                if ((tnUnits > 0) && ((toDate.getLong() + (tnUnits * 2678400000L)) < 0))
                {
                    return new Date(Long.MAX_VALUE);
                }
               java.util.Date loJavaDate = new java.util.Date(toDate.getLong());
               loJavaDate.setMonth(loJavaDate.getMonth() + (int) tnUnits);
               return new Date(loJavaDate);
            }
            // Can do direct calculation
            // If the calculation takes the date into BC then error
            if ((tnUnits < 0) && ((toDate.getLong() + (tnUnits * toDatePart.getMultiplier())) < -62138456748000L))
            {
                throw new Goliath.Exceptions.InvalidParameterException("Date of " + toDate.toString() + "and unit type of " + toDatePart.toString() + " units of " + tnUnits + " passed into addDatePart causes the calculation to drop before -1899 (1AD)", "tnUnits");
            }
            // if the caclulation takes the date passed the maximum value then set to maximum value (the value will in fact go below
            // -62106920748000L (into BC) but the test below for < 0 is fine)
            if ((toDate.getLong() > -62138456748000L) && (tnUnits > 0) && ((toDate.getLong() + (tnUnits * toDatePart.getMultiplier())) < 0))
            {
                return new Date(Long.MAX_VALUE);
            }
            return new Date(toDate.getLong() + (tnUnits * toDatePart.getMultiplier()));
    }


    private java.util.Date m_oDate;
    private boolean m_lInitialized;
    private boolean m_lEmptyIsMin;

    /**
     * Creates a new date object, where null is equivalent to the minimum
     * date value it tlEmptyIsMin = true, otherwise null is equivalent to the max value.
     * @param tlEmptyIsMin if true then null is equivalent to the min date value
     *                      if false then null is equivalent to the max date value
     */
    public Date(boolean tlEmptyIsMin)
    {
        m_lEmptyIsMin = tlEmptyIsMin;
        if (m_lEmptyIsMin)
        {
            m_oDate = new java.util.Date(java.lang.Long.MIN_VALUE);
        }
        else
        {
            m_oDate = new java.util.Date(java.lang.Long.MAX_VALUE);            
        }
        m_lInitialized = true;
    }

    public Date(String tcDate)
    {
        m_lEmptyIsMin = true;
        try
        {
            m_oDate = Utilities.Date.getDateFormatter().parse(tcDate);
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
        m_lInitialized = true;


        
    }
    
    /**
     * Creates a new date object, where null is equivalent to the minimum
     * date value, also initialises the value
     * @param toDate The date to initialise this date object with
     */
    public Date(java.util.Date toDate)
    {
        this(toDate, true);
    }
    
    /**
     * Creates a new date object, where null is equivalent to the minimum
     * date value
     * @param toDate The date to initialise this object with
     * @param tlEmptyIsMin if true then null is equivalent to the min date value
     *                      if false then null is equivalent to the max date value
     */
    public Date(java.util.Date toDate, boolean tlEmptyIsMin)
    {
        this(tlEmptyIsMin);
        setDate(toDate);
    }
    
    /**
     * Creates a new date object, where null is equivalent to the minimum date value
     * @param tnDate The long value of the date to initialise this object with
     */
    public Date(long tnDate)
    {
        this(tnDate, true);
    }
    
    /**
     * Creates a new date object, where null is equivalent to the minimum
     * date value
     * @param tnDate The long value of the date to initialise this object with
     * @param tlEmptyIsMin if true then null is equivalent to the min date value
     *                      if false then null is equivalent to the max date value
     */
    public Date(long tnDate, boolean tlEmptyIsMin)
    {
        this(tlEmptyIsMin);
        setDate(new java.util.Date(tnDate));
    }
    
     /**
     * Creates a new date object, where null is equivalent to the minimum
     * date value, initialises with the date and time that this was created
     */
    public Date()
    {
        this(true);
        setDate(new java.util.Date());
    }
    
    /**
     * Gets the date object that this object wraps
     * @return the currently stored date
     */
    public final java.util.Date getDate()
    {
        if (!m_lInitialized || m_oDate == null)
        {
            if (m_lEmptyIsMin)
            {
                m_oDate = new java.util.Date(Long.MIN_VALUE);
            }
            else
            {
                m_oDate = new java.util.Date(Long.MAX_VALUE);                
            }
            m_lInitialized = true;
        }
        return m_oDate;
    }
    
    
    /**
     * Gets the long value of this date
     * @return the long value of this date
     */
    public final long getLong()
    {
        return m_oDate.getTime();
    }
    
    /**
     * Sets the current date
     * @param toDate the date to set this date object to
     */
    public final void setDate(java.util.Date toDate)
    {
        m_oDate = toDate;
        m_lInitialized = true;
    }
    
    /**
     * Checks if the date is currently empty
     * @return true if the date is empty
     */
    public final boolean isEmpty()
    {
        if (m_lEmptyIsMin)
        {
            return m_oDate.getTime() == Long.MIN_VALUE;
        }
        return m_oDate.getTime() == Long.MAX_VALUE;
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return Goliath.Utilities.Date.toString(this);
    }
    
    @Override
    public int compareTo(Date toDate)
    {
        return m_oDate.compareTo(toDate.m_oDate);
    }

    @Override
    public boolean equals(java.lang.Object toObject)
    {
        if (toObject.getClass().isAssignableFrom(this.getClass()))
        {
            // Because some storage of dates is only to seconds, we will
            // assume a date is equal if it is within 1 second
            return Math.abs(m_oDate.getTime() - ((Date)toObject).getDate().getTime()) <= 999;
        }
        return false;
    }



}
