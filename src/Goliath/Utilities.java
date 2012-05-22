/* =========================================================
 * Utilities.java
 *
 * Author:      kmchugh
 * Created:     13 November 2007, 11:45
 *
 * Description
 * --------------------------------------------------------
 * This is a utilities class that contains methods for different
 * basic functions in the framework.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/
package Goliath;

import Goliath.Constants.LogType;
import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Exceptions.CriticalException;
import Goliath.Exceptions.InvalidParameterException;
import Goliath.Interfaces.Commands.ICommand;
import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Node;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * The utilities class contains methods for different functions
 * within the framework
 * For example:
 * <pre>
 *      String lcGUID = Goliath.Utilities.generateStringGUID();
 * </pre>
 *
 * @version     1.0 13 November 2007
 * @author      kmchugh
 **/
public final class Utilities
{

    private static Goliath.Collections.List<String> m_aClassPaths;
    private static HashTable<String, Pattern> g_oMatchers;
    
    /**
     * Date related utility functions
     */
    public static class Date
    {
        private static SimpleDateFormat m_oDateFormat;
        private static SimpleDateFormat g_oRFC1123;
    
        
        /**
         * Gets the system date formatter
         * @return the system date formatter
         */
        public static SimpleDateFormat getDateFormatter()
        {
            if (m_oDateFormat == null)
            {
                String lcDateFormat = Application.getInstance().getPropertyHandlerProperty("Application.Settings.Constants.DateFormat", "yyyy/MM/dd HH:mm:ss.SSS");
                m_oDateFormat = new SimpleDateFormat(lcDateFormat);
            }
            return m_oDateFormat;
        } 
        
        /**
         * Converts a date to a string
         * @param toDate the date to convert
         * @return the string representation of the date
         */
        public static String toString(java.util.Date toDate)
        {
            return getDateFormatter().format(toDate);
        }

        /**
         * Converts a date to a string
         * @param toDate the date to convert
         * @return the string representation of the date
         */
        public static String toString(Goliath.Date toDate)
        {
            return toString(toDate.getDate());
        }
        
        /**
         * Gets an RFC1123 string representation of a string
         * @param toDate the date to get the string representation for
         * @return the string representation of the date
         */
        public static String getRFC1123Date(Goliath.Date toDate)
        {
            return getRFC1123Date(toDate.getDate());
        }

        /**
         * Gets an RFC1123 string representation of a string
         * @param toDate the date to get the string representation for
         * @return the string representation of the date
         */
        public static String getRFC1123Date(java.util.Date toDate)
        {
            if (g_oRFC1123 == null)
            {
                g_oRFC1123 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            }
            return g_oRFC1123.format(toDate);
        }

        /**
         * Parses a Date from the string provided if it is in RFC1123 format
         * @param tcDate the date to parse
         * @return the Date value
         * @throws ParseException if the string is not in the correct format 
         */
        public static Goliath.Date parseRFC1123Date(String tcDate)
                throws ParseException
        {
            if (g_oRFC1123 == null)
            {
                g_oRFC1123 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            }
            return new Goliath.Date(g_oRFC1123.parse(tcDate));
        }
        
        /**
         * Gets an RFC822 string representation of a string
         * @param toDate the date to get the string representation for
         * @return the string representation of the date
         */
        public static String getRFC822Date(Goliath.Date toDate)
        {
            return getRFC1123Date(toDate.getDate());
        }
        
        /**
         * Gets an RFC822 string representation of a string
         * @param toDate the date to get the string representation for
         * @return the string representation of the date
         */
        public static String getRFC822Date(java.util.Date toDate)
        {
            return getRFC1123Date(toDate);
        }

        /**
         * Parses a Date from the string provided if it is in RFC822 format
         * @param tcDate the date to parse
         * @return the Date value
         * @throws ParseException if the string is not in the correct format 
         */
        public static Goliath.Date parseRFC822Date(String tcDate)
                throws ParseException
        {
            return parseRFC822Date(tcDate);
        }
        
    }
           

    public static void addClassPath(String tcClassPath)
    {
        if (m_aClassPaths.size() == 0)
        {
            addCurrentClassPaths();
        }

        if (!m_aClassPaths.contains(tcClassPath))
        {
            m_aClassPaths.add(tcClassPath);
        }
    }

    /**
     * Gets the version information for the application that is running
     * @return the version information for this application
     */
    public static LibraryVersion getApplicationVersion()
    {
        List<LibraryVersion> loVersions = Application.getInstance().getObjectCache().getObjects(LibraryVersion.class, "getName");
        Package loPackage = Application.getInstance().getStartupClass().getPackage();
        for (LibraryVersion loVersion : loVersions)
        {
            if (loVersion.getClass().getPackage() == loPackage)
            {
                return loVersion;
            }
        }

        return null;
    }

    private static void addCurrentClassPaths()
    {
        String lcPath = System.getProperty("user.dir");
        if (lcPath.toLowerCase().endsWith(".jar"))
        {
            lcPath = lcPath.substring(0, lcPath.lastIndexOf(Environment.FILESEPARATOR()) + 1);
        }
        else
        {
            lcPath = lcPath + Environment.FILESEPARATOR();
        }

        m_aClassPaths.add(lcPath);
        m_aClassPaths.add(lcPath + "lib" + Environment.FILESEPARATOR());
    }

    /** Creates a new instance of Utilities */
    public Utilities()
    {
    }

    /**
     * Generates a random GUID, currently this GUID is based on the system time
     *
     * @return  a string version of a GUID
     */
    public static String generateStringGUID()
    {
        return java.util.UUID.randomUUID().toString();
    }
    
    /**
     * Gets the Maximum size of the heap in bytes
     * If the heap attempts goes over this size, there will be an OutOfMemoryException thrown
     * @return the maximum size of the heap
     */
    public static long getMaxHeapSize()
    {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * Gets the current size of the heap in bytes
     * @return the current size of the heap
     */
    public static long getCurrentHeapSize()
    {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * Gets the number of bytes free on the heap
     * @return the number of bytes free on the heap
     */
    public static long getFreeHeapSize()
    {
        return Runtime.getRuntime().freeMemory();
    }


    /**
     * Gets the regex matcher from the cache, if it doesn't exist, creates it
     * @param tcRegex the regular expression to use to match
     * @param tcMatchString the string we are matching against
     * @return the Matcher object to use for extracting the matches
     */
    public static Matcher getRegexMatcher(String tcRegex, String tcMatchString)
    {
        if (g_oMatchers == null)
        {
            g_oMatchers = new HashTable<String, Pattern>();
        }
        if (!g_oMatchers.containsKey(tcRegex))
        {
            g_oMatchers.put(tcRegex, Pattern.compile(tcRegex));
        }
        return g_oMatchers.get(tcRegex).matcher(tcMatchString);
    }

    /**
     * Gets the date of the latest last modified date from all the files
     * and directories in the specified directory.  This does not search sub directories
     * @param tcDirectory the directory to get the last modified date for
     * @return the latest last modified date from all the files and directories in tcDirectory, -1 if there are no files or directories
     */
    public static long getLastModifiedFromDirectory(String tcDirectory)
    {
        File loFile = new File(tcDirectory);
        long lnDate = -1;

        if (loFile.exists())
        {
            for (String lcFile : loFile.list())
            {
                loFile = new File(tcDirectory + lcFile);
                lnDate = Math.max(lnDate, loFile.lastModified());
            }
        }
        return lnDate;
    }

    /**
     * Converts the path from the specified file to a relative path from the root(installation) directory of the application
     * @param toFile the file to get the relative path for
     * @return the relative path for the file specified
     */
    public static String getRelativePath(File toFile)
    {
        String lcString = new File(".").getAbsolutePath();
        return "." + toFile.getPath().replace(lcString, "");
    }

    public static List<File> getFiles(String tcDirectory, String tcRegexMatchString, boolean tlIncludeSubDirectories)
    {
        List<File> loReturn = new List<File>();

        File loRoot = new File(tcDirectory);
        if (loRoot.exists() && loRoot.isDirectory())
        {
            FilenameFilter loFilter = null;

            if (!isNullOrEmpty(tcRegexMatchString))
            {
                final String lcMatch = tcRegexMatchString;

                loFilter = new FilenameFilter()
                {

                    @Override
                    public boolean accept(File toFile, String tcName)
                    {
                        return Goliath.Utilities.getRegexMatcher(lcMatch, tcName).matches() || (new File(toFile.getAbsolutePath() + Environment.FILESEPARATOR() + tcName).isDirectory());
                    }
                };
            }
            File[] laFiles = loRoot.listFiles(loFilter);
            for (File loFile : laFiles)
            {
                // if the file is a directory and if including children, get the list
                if (loFile.isDirectory())
                {
                    // Check if we are wanting to include subdirectories
                    if (tlIncludeSubDirectories)
                    {
                        loReturn.addAll(getFiles(loFile.getAbsolutePath(), tcRegexMatchString, tlIncludeSubDirectories));
                    }
                }
                else
                {
                    loReturn.add(loFile);
                }
            }
        }

        return loReturn;
    }

    /**
     * Returns toReplacement if toObject is null 
     *
     * @param <T> The type of object
     * @param  toObject the object to return if it is not null
     * @param  toReplacement the object to return if toObject is null
     * @return  toReplacement if toObject is null, otherwise returns toObject
     */
    public static <T> T isNull(T toObject, T toReplacement)
    {
        return toObject == null ? toReplacement : toObject;
    }

    public static String stripHTMLTags(String tcHTMLString)
    {
        try
        {
            return tcHTMLString.replaceAll("\\]\\]>", "").replaceAll("<!\\[(CDATA|cdata)\\[", "").replaceAll("<(.|\n)+?>", "").replaceAll("&nbsp;", " ");
        }
        catch (Throwable ex)
        {
            Application.getInstance().log("Goliath.Utilities.stripHTMLTags, Unable to strip HTML Tags for string \n" + tcHTMLString, LogType.ERROR());
            return tcHTMLString;
        }
    }

    /**
     * Checks if a string is null or empty 
     *
     * @param  tcString the string to check
     * @return  true if the string is null or empty
     */
    public static boolean isNullOrEmpty(String tcString)
    {
        return tcString == null || tcString.isEmpty();
    }

    /**
     * Returns true if toCompare matches an object in the array specified
     * @param <T> The type of objects being compared
     * @param toCompare the object to compare
     * @param taCompareTo the list of objects to compare to
     * @return true if toCompare is not null and is contained in taCompareTo, otherwise returns false
     */
    public static <T> boolean isOneOf(T toCompare, T[] taCompareTo)
    {
        if (toCompare == null || taCompareTo == null || taCompareTo.length == 0)
        {
            return false;
        }

        for (T loObject : taCompareTo)
        {
            if (toCompare.equals(loObject))
            {
                return true;
            }
        }
        return false;
    }

    /***
     * Trims a string down to a specific length if it is longer than the specified length
     * Otherwise does not change the string.
     * Trims from the right of the string
     * @param tnLength  The maxmimum length of the resulting string
     * @param tcString the String to trim
     * @return A string with a lenght of no more than tnLength
     */
    public static String trimToLength(int tnLength, String tcString)
    {
        if (Goliath.Utilities.isNullOrEmpty(tcString) || tcString.length() < tnLength)
        {
            return tcString;
        }
        return tcString.substring(0, tnLength);
    }
    
    /**
     * Returns a string representation of a byte array
     * @param taBytes the byte array to convert to string
     * @return A string version of the byte array
     */
    public static String toString(byte[] taBytes)
    {
        try
        {
            return new String(taBytes, "utf-8");
        }
        catch (Throwable ex)
        {
            return new String(taBytes);
        }
    }

    /**
     * Gets the directory where the jar files are for the application
     * @return the directory with the jar files
     */
    public static String getClassDir()
    {
        return getClassDir(Goliath.Utilities.class);
    }

    /**
     * Gets the directory where the jar files are for the specified class
     * @param toClass the class to use to find a path
     * @return the directory with the jar files
     */
    public static String getClassDir(Class toClass)
    {
        return (new java.io.File(toClass.getProtectionDomain().getCodeSource().getLocation().getFile().substring(1))).getParentFile().getPath();
    }

    /**
     * Gets a list of all the current class paths for the application
     * @return a list of the current class paths
     */
    public static Goliath.Collections.List<String> getClassPaths()
    {
        if (m_aClassPaths == null)
        {
            m_aClassPaths = new List<String>();
            addCurrentClassPaths();
        }
        return m_aClassPaths;
    }

    public static boolean inList(boolean tlIgnoreCase, String tcCompare, String... taArgs)
    {
        boolean llFound = false;
        for (String lcString : taArgs)
        {
            if (tlIgnoreCase)
            {
                llFound = tcCompare.equalsIgnoreCase(lcString);
            }
            else
            {
                llFound = tcCompare.equals(lcString);
            }
            if (llFound)
            {
                return llFound;
            }
        }
        return llFound;
    }

    /**
     * Attempts to convert a string to the specified type
     * @param <T> The type of object to create
     * @param toClass the class to create
     * @param lcValue the value to convert
     * @return an object of type T derived from lcValue
     */
    public static <T> T fromString(Class<T> toClass, Node loValue)
    {
        return fromString(toClass, Goliath.XML.Utilities.toString(loValue));
    }

    /**
     * Attempts to convert a string to the specified type
     * @param <T> The type of object to create
     * @param toClass the class to create
     * @param lcValue the value to convert
     * @return an object of type T derived from lcValue
     */
    public static <T> T fromString(Class<T> toClass, String lcValue)
    {
        String lcClassName = toClass.getSimpleName();
        if (lcValue == null || lcClassName.equalsIgnoreCase("string"))
        {
            return (T) lcValue;
        }
        else if (lcClassName.equalsIgnoreCase("int") || lcClassName.equalsIgnoreCase("integer"))
        {
            return (T) new Integer(lcValue);
        }
        else if (lcClassName.equalsIgnoreCase("date"))
        {
            java.util.Date loDate = null;
            try
            {
                // Check if it is a long
                long lnLong = Long.parseLong(lcValue);
                if (lcValue.equals(Long.toString(lnLong)))
                {
                    loDate = new java.util.Date(lnLong);
                }
            }
            catch (NumberFormatException ex)
            {
                // value wasn't a long
                loDate = null;
            }

            if (loDate == null)
            {
                try
                {
                    return (T) Date.getDateFormatter().parse(lcValue);
                }
                catch (ParseException ex)
                {
                    Application.getInstance().log(ex);
                }
            }

            if (Goliath.Date.class.isAssignableFrom(toClass))
            {
                return (T) new Goliath.Date(loDate);
            }
            return (T) loDate;
        }
        else
        {
            // Attempt a cast
            return (T) lcValue;
        }
    }

    /**
     * Encodes a string so it is usable in xml and html
     * @param tcString the string to decode
     * @return the decoded string
     */
    public static String decode(String tcString)
    {
        try
        {
            return java.net.URLDecoder.decode(tcString, "UTF-8");
        }
        catch (Exception ex)
        {
            Application.getInstance().log(ex);
            return tcString;
        }
    }

    /**
     * Encodes a string so it is usable in xml and html
     * @param tcString the string to decode
     * @return the decoded string
     */
    public static String encode(String tcString)
    {
        try
        {
            return java.net.URLEncoder.encode(tcString, "UTF-8").replaceAll("\\+", "%20");
        }
        catch (Exception ex)
        {
            Application.getInstance().log("Error encoding string to URL [" + tcString + "]", LogType.ERROR());
            Application.getInstance().log(ex);
            return "";
        }
    }

    /**
     * Encrypts the string using the MD5 encryption
     * @param tcString the string to encrypt
     * @return the encrypted string
     */
    public static String encryptMD5(String tcString)
    {
        StringBuilder loBuffer = new StringBuilder(tcString);
        try
        {
            java.security.MessageDigest loMD = java.security.MessageDigest.getInstance("MD5");
            StringBuilder loResult = new StringBuilder();
            for (byte lnB : loMD.digest(loBuffer.toString().getBytes()))
            {
                loResult.append(Integer.toHexString((lnB & 0xf0) >>> 4));
                loResult.append(Integer.toHexString(lnB & 0x0f));
            }
            return loResult.toString();
        }
        catch (java.security.NoSuchAlgorithmException ex)
        {
            throw new CriticalException("MD5 does not appear to be supported", ex);
        }
    }

    public static String encodeBase64(String tcString)
    {
        BASE64Encoder loEncoder = new BASE64Encoder();
        return loEncoder.encode(tcString.getBytes());
    }

    public static String decodeBase64(String tcString)
    {
        BASE64Decoder loEncoder = new BASE64Decoder();
        try
        {
            return toString(loEncoder.decodeBuffer(tcString));
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
            return "";
        }
    }

    /**
     * Converts a String to a long
     * @param toObject the string to convert
     * @return the Long value
     */
    public static Long parseLong(String toObject)
    {
        try
        {
            return new Long(Long.parseLong(toObject));
        }
        catch (Exception ex)
        {
            return new Long(Long.parseLong("0"));
        }
    }

    /**
     * Checks if the specified parameter is null or not, if the parameter is null then an
     * exception will be thrown
     * @param tcParameterName the name of the parameter, this is displayed in the error message
     * @param toValue The value of the parameter, this is checked if it is null or not
     */
    public static void checkParameterNotNull(String tcParameterName, java.lang.Object toValue)
    {
        if (toValue == null)
        {
            throw new InvalidParameterException(tcParameterName, toValue);
        }
    }

    /**
     * Waits for the specified command to complete
     * @param toCommand the command to wait for
     */
    public static void waitForCommand(ICommand toCommand)
    {
        // Because we are waiting for this command, it means the thread has basically stopped until
        // the command has completed executing, so first thing we want to do is to make sure
        // the command is actually running

        if (toCommand.isRegistered())
        {
            waitForCommand(toCommand, 100f);
        }
        else
        {
            // The command was not registered, so just execute it inline
            toCommand.execute();
        }
    }

    /**
     * Waits for the specified command to get to a certain progress level
     * @param toCommand the command to wait for
     * @param tnProgress the progress percent to wait until
     */
    public static void waitForCommand(ICommand toCommand, float tnProgress)
    {
        while (toCommand.getProgress() < tnProgress)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (Throwable ignore)
            {
            }
        }
    }

    /**
     * Helper function to append multiple strings to a string builder
     * @param toBuilder the builder to append to
     * @param taStrings the list of strings
     */
    public static void appendToStringBuilder(StringBuilder toBuilder, String... taStrings)
    {
        for (int i = 0;  i<taStrings.length; i++)
        {
            toBuilder.append(taStrings[i]);
        }
    }
}
