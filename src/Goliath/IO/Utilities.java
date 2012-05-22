/* ========================================================
 * Utilities.java
 *
 * Author:      kmchugh
 * Created:     Feb 17, 2011, 9:16:09 AM
 *
 * Description
 * --------------------------------------------------------
 * File and IO based utilities
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.IO;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.DynamicCode.Java;
import Goliath.Environment;
import Goliath.Exceptions.FileAlreadyExistsException;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Exceptions.MethodNotFoundException;
import Goliath.Interfaces.Collections.IPropertySet;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * File and IO based utilities
 * @author kmchugh
 */
public class Utilities
{
    /**
     * Not createable
     */
    private Utilities()
    {
    }

    /**
     * Gets a reference to the directory this application is in
     * @return the applications installation directory
     */
    public Goliath.IO.File getApplicationDirectory()
    {
        try
        {
            return new Goliath.IO.File(
                Application.getInstance().getApplicationSettings().getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        }
        catch (FileNotFoundException ex)
        {
            // It is possible that this happens
            Application.getInstance().log(ex);
        }
        return null;
    }

    /**
     * Gets the Users Home directory
     * @return the Users home directory
     */
    public Goliath.IO.File getUserHomeDirectory()
    {
        try
        {
            return new Goliath.IO.File(System.getProperty("user.home"));
        }
        catch (FileNotFoundException ex)
        {
            // This should not happen as the user will always have a home dir so if the app has access...
            Application.getInstance().log(ex);
        }
        return null;
    }

    /**
     * Gets the Users desktop directory
     * @return the Users desktop directory
     */
    public Goliath.IO.File getDesktopDirectory()
    {
        try
        {
            Goliath.IO.File loFile = getUserHomeDirectory();
            return loFile != null ? loFile.getName().matches("(?i).+desktop/?$") ? loFile :
                new Goliath.IO.File(File.get(loFile.getAbsolutePath() + Environment.FILESEPARATOR() + "desktop")) : null;
        }
        catch (FileNotFoundException ex)
        {
            // It is possible that this happens
            Application.getInstance().log(ex);
        }
        return null;
    }

    /**
     * Gets the Users documents directory
     * @return the Users documents directory
     */
    public Goliath.IO.File getDocumentsDirectory()
    {
        try
        {
            Goliath.IO.File loFile = getUserHomeDirectory();
            return loFile != null ? loFile.getName().matches("(?i).+desktop/?$") ?
                new Goliath.IO.File(File.get(loFile.getAbsolutePath() + Environment.FILESEPARATOR() + ".." + Environment.FILESEPARATOR() + "documents")) :
                new Goliath.IO.File(File.get(loFile.getAbsolutePath() + Environment.FILESEPARATOR() + "documents")) : null;
        }
        catch (FileNotFoundException ex)
        {
            // It is possible that this happens
            Application.getInstance().log(ex);
        }
        return null;
    }

    /**
     * File related IO helper utilities
     */
    public static class File
    {
        /**
         * Helper function to compress a file or directory
         * @param toSource the file or directory
         * @param toDest the output location
         * @param toStream the deflater stream that is being used to compress
         * @return true if compression was okay
         */
        private static boolean compress(java.io.File toSource, java.io.File toDest, DeflaterOutputStream toStream, java.io.File toRoot)
        {
            if (toSource.isFile())
            {
                FileChannel loChannel = null;
                
                try
                {
                
                    // Write the file to the stream
                    loChannel = new FileInputStream(toSource).getChannel();
                    loChannel.transferTo(0, loChannel.size(), Channels.newChannel(toStream));
                    return true;
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                    return false;
                }
                finally
                {
                    if (loChannel != null)
                    {
                        try
                        {
                            loChannel.close();
                        }
                        catch(Throwable ex)
                        {
                        }
                    }
                }
            }
            else
            {
                try
                {
                    // Create a new ZipEntry and write the zip entry
                    java.io.File[] laFiles = toSource.listFiles();
                    for (java.io.File loFile : laFiles)
                    {
                        ((ZipOutputStream)toStream).putNextEntry(new ZipEntry(loFile.getAbsolutePath().replace(toRoot.getAbsolutePath(), "") + (loFile.isDirectory() ? "/" : "")));
                        compress(loFile, toDest, toStream, toRoot);
                        ((ZipOutputStream)toStream).closeEntry();

                    }
                    return true;
                }
                catch (Throwable ex)
                {
                    Application.getInstance().log(ex);
                    return false;
                }
            }
        }
        
        /**
         * Compresses the source file to the destination
         * @param toSource the source file, the file to compress
         * @param toDest the resulting compressed file
         * @param tlOverwrite if true and toDest exists, it will be overwritten
         * @return true if the operation was successful
         */
        public static boolean compress(java.io.File toSource, java.io.File toDest, boolean tlOverwrite)
        {
            if ((!toSource.exists()) ||
                (toDest.exists() && ! tlOverwrite))
            {
                return false;
            }
            
            DeflaterOutputStream loGZipStream = null;
            
            try
            {
                if(!toDest.exists())
                {
                    Goliath.IO.Utilities.File.create(toDest);
                }
                
                loGZipStream = toSource.isFile() ? new GZIPOutputStream(new FileOutputStream(toDest, false)) : new ZipOutputStream(new FileOutputStream(toDest, false));
                return compress(toSource, toDest, loGZipStream, toSource);
            }
            catch(Throwable ex)
            {
                Application.getInstance().log(ex);
                return false;
            }
            finally
            {
                if (loGZipStream != null)
                {
                    try
                    {
                        loGZipStream.close();
                    }
                    catch (Throwable ex)
                    {}
                }
            }
        }

        /**
         * Populates toProperties from the values of toObject, if toSecondary is not null then any properties
         * not found in toObject will be populated from toSecondary if the property exists
         * @param toProperties the property set to build the properties from
         * @param toObject the object to get the property values from
         * @param toSecondary the secondary object to retrive the values from
         * @return toProperties is returned to allow chaining
         */
        public static PropertySet populatePropertyHash(PropertySet toProperties, java.lang.Object toObject, IPropertySet toSecondary)
        {
            // Make a copy to protect from concurrent modification
            List<String> lcKeySet = new List(toProperties.keySet());
            for (String lcParam : lcKeySet)
            {
                /* Look for a property method, or a property type method, a property type method is a method
                 * of the same name which returns a value and takes no parameters
                 */
                Method loMethod = null;
                if (toObject != null)
                {
                    try
                    {
                        loMethod = Java.getMethod(toObject.getClass(), lcParam, new Class[0]);
                    }
                    catch (MethodNotFoundException ex)
                    {
                        try
                        {
                            loMethod = Java.getMethod(toObject.getClass(), "get" + lcParam, new Class[0]);
                        }
                        catch (MethodNotFoundException ex1)
                        {
                            // Do nothing here
                        }
                    }
                }
                Object loValue = null;
                if (loMethod != null && loMethod.getReturnType() != null)
                {
                    try
                    {
                        loValue = loMethod.invoke(toObject, (java.lang.Object[])null);
                    }
                    catch(Throwable ex)
                    {
                        // Do nothing here, the call wasn't valid
                    }
                }
                toProperties.setProperty(lcParam, loValue == null ? toSecondary != null ? toSecondary.getProperty(lcParam) : loValue : loValue);
            }
            return toProperties;
        }

        /**
         * Examines the string passed and extracts a list of parameters from the file.
         * tcRegex must be a regular expression which contains a group match
         * @param toFile the file to examine
         * @param tcRegex the regular expression that finds the replacement parameters
         * @return the list of properties that have been found
         */
        public static PropertySet extractParameters(String tcContents, String tcRegex)
        {
            // TODO: Implement a streaming version of this
            PropertySet loReturn = new PropertySet();
            Matcher loMatcher = Goliath.Utilities.getRegexMatcher(tcRegex, tcContents);
            while(loMatcher.find())
            {
                String lcValue = loMatcher.group(1);
                if (!loReturn.contains(lcValue))
                {
                    loReturn.setProperty(lcValue, null);
                }
            }
            return loReturn;
        }

        /**
         * Examines the string passed and extracts a list of parameters from the file.
         * tcRegex must be a regular expression which contains a group match
         * @param toFile the file to examine
         * @return the list of properties that have been found
         */
        public static PropertySet extractParameters(String tcContents)
        {
            return extractParameters(tcContents, "(?i)\\{\\{(\\w+)}}");
        }

        /**
         * Examines the file passed and extracts a list of parameters from the file.
         * tcRegex must be a regular expression which contains a group match
         * @param toFile the file to examine
         * @param tcRegex the regular expression that finds the replacement parameters
         * @return the list of properties that have been found
         */
        public static PropertySet extractParameters(Goliath.IO.File toFile, String tcRegex)
        {
            return extractParameters(Goliath.IO.Utilities.File.toString(toFile), tcRegex);
        }

        /**
         * Examines the file passed and extracts a list of parameters from the file
         * @param toFile The file to examine
         * @return the property set with the names of all the parameters
         */
        public static PropertySet extractParameters(Goliath.IO.File toFile)
        {
            return extractParameters(toFile, "(?i)\\{\\{(\\w+)}}");

        }


        /**
         * Copies the source file to the destination, If the destination already
         * exists and tlOverwrite is ture then the destination file will be replaced
         * @param toSource the source file
         * @param toDestination the destination
         * @param tlOverwrite if the destination exists, should it be replaced
         * @throws FileNotFoundException if the source does not exist
         * @throws FileAlreadyExistsException if toDestination already exists, and tlOverwrite is false
         * @throws IOException if the destination could not be created
         */
        public static void copy(java.io.File toSource, java.io.File toDestination, boolean tlOverwrite)
            throws FileAlreadyExistsException, FileNotFoundException, IOException
        {
            if (toDestination.exists() && ! tlOverwrite)
            {
                throw new FileAlreadyExistsException(toDestination);
            }
            Goliath.IO.File loFile = new Goliath.IO.File(toSource);

            // Make sure the destination exists, this will get right down to creating the sub directories if needed
            File.create(toDestination);

            // If the create did not work an IOException would be thrown and we would not get here
            FileChannel loSource = new FileInputStream(toSource).getChannel();
            FileChannel loDestination = new FileOutputStream(toDestination).getChannel();

            long lnCount=0;
            long lnSize = loSource.size();
            try
            {
                // Statement after while is empty on purpose
                while((lnCount += loDestination.transferFrom(loSource, 0, lnSize-lnCount))>0);
            }
            finally
            {
                if (loSource != null)
                {
                    loSource.close();
                }
                if (loDestination != null)
                {
                    loDestination.close();
                }
            }
        }

        /**
         * Moves the file from the source to the destinaion, if tlOverwrite is false and toDestination
         * already exists, then this will throw a file already exists error.
         * @param toSource the source file
         * @param toDestination the destination file
         * @param tlOverwrite overwrite the destination if it does not exist
         */
        public static void move(java.io.File toSource, java.io.File toDestination, boolean tlOverwrite)
                throws FileAlreadyExistsException, FileNotFoundException, IOException
        {
            copy(toSource, toDestination, tlOverwrite);
            toSource.delete();
        }
        
        /***
         * Creates a file, also creates the directory structure if it does not already exist
         * @param toFile the file to create
         * @return true if the file is created, false if it already exists
         * @throws IOException
         */
        public static boolean create(java.io.File toFile)
                throws IOException
        {
            return create(toFile, false);
        }
        
        /**
         * Creates a file or directory and the directory structure underneath if it does not already exist
         * @param toFile the File or directory
         * @param tlIsDirectory true if this is a directory, false if it is a file
         * @return true if the file was created, false if it already exists
         */
        public static boolean create(java.io.File toFile, boolean tlIsDirectory)
                throws IOException
        {
            // Elminiate problems in case insensitive OS' by getting the file
            toFile = File.get(toFile);
            if (!toFile.exists())
            {
                if (tlIsDirectory)
                {
                    return toFile.mkdirs();
                }
                else
                {
                    // Ensure the directories exist
                    toFile.getParentFile().mkdirs();
                    return toFile.createNewFile();
                }
            }
            return false;
        }
        
        /**
         * Creates a file or directory from the string specified, it also creates the 
         * directory structure if it does not already exist
         * @param tcFile the file to create
         * @param tlIsDirectory true if this should result in the creation of a directory
         * @return true if created, false if it already exists
         */
        public static boolean create(String tcFile, boolean tlIsDirectory)
                throws IOException
        {
            return create(File.get(tcFile), tlIsDirectory);
        }

        /**
         * Resolves tcPath to a location relative to toRoot.
         * tcPath starts with:
         *
         * File Separator (/ or \) - path will be relative to the current working directory
         * . - path will be relative to the directory of toRoot
         * .. - path will be at the parent of toRoot
         * anything else - considered as .
         *
         * NOTE: For linux based systems it is not possible to use this method to resolve when tcPath is absolute as the
         * first character would be a /
         *
         * @param toRoot the root to resolve from
         * @param tcPath the path to resolve
         * @return the path relative to toRoot
         */
        public static java.io.File getRelativeLocation(java.io.File toRoot, String tcPath)
        {
            java.io.File loReturn = null;
            if (tcPath.startsWith(".") || tcPath.startsWith(".."))
            {
                loReturn = new java.io.File(toRoot.getAbsoluteFile().getParent() + Environment.FILESEPARATOR() + new java.io.File(tcPath).getPath());
            }
            else
            {
                loReturn = new java.io.File(new java.io.File(".").getAbsolutePath() + Environment.FILESEPARATOR() + new java.io.File(tcPath).getPath());
            }
            // If the file exists return the canonical path.
            try
            {
                return loReturn.exists() ? loReturn.getCanonicalFile() : loReturn;
            }
            catch(Throwable ex)
            {
                return loReturn;
            }
        }

        /**
         * Gets a temporary version of the file specified in the path.  The temporary version
         * of the file is simply the path resolved to the application tmp directory with the
         * extension appended.  For example if calling this with
         * getTemporaryFile("c:\test\myfile.txt", "cache");
         * The file returned will be {application path}\{application temp directory}\text\myfile.txt.cache
         *
         * The file returned may or may not exist in the file system
         * @return The file representing the location of where a temporary version of the file specified would be stored
         */
        public static java.io.File getTemporary(String tcPath, String tcExtension)
        {
            // Attempt to resolve the directory by removing the current directory from the path
            return new java.io.File(Application.getInstance().getDirectory("tmp") + new java.io.File(".").toURI().relativize(new java.io.File(tcPath).toURI()).getPath() + "." +tcExtension);
        }

        /**
         * Gets a file reference to a temporary file in the OS specific temp directory.
         * This call will result in the creation of a temporary file.
         * @param tcExtension the extension to use when creating this file
         * @return the file that has been created for temporary storage
         */
        public static Goliath.IO.File getTemporary(String tcExtension)
                throws IOException
        {
            try
            {
                Goliath.IO.File loReturn = new Goliath.IO.File(java.io.File.createTempFile("gol", "." + tcExtension), true);
                return loReturn;
            }
            catch (FileNotFoundException ex)
            {
                java.io.File loFile = new java.io.File(Application.getInstance().getDirectory("tmp") + Goliath.Utilities.generateStringGUID() + "." + tcExtension);
                try
                {
                    Goliath.IO.Utilities.File.create(loFile);
                    return new Goliath.IO.File(loFile, true);
                }
                catch(FileNotFoundException ex1)
                {
                    // This should not be able to happen as the file has been created
                }
            }
            return null;
        }

        /**
         * Gets a file reference to a temporary file in the OS specific temp directory.
         * This call will result in the creation of a temporary file.
         * @return the file that has been created for temporary storage
         */
        public static Goliath.IO.File getTemporary()
                throws IOException
        {
            return File.getTemporary("tmp");
        }

        /**
         * Gets a reference to a file and attempts to make sure the reference
         * is correct even on case sensitive systems.  If on a case sensitive
         * file system and if the toFile parameter is not an exact match, then
         * a new reference will be returned with the canonical reference to an
         * exact case sensitive match.  If there are multiple matches, which
         * can only happen on case sensitive systems, a reference to the first
         * file will be returned.  The definition of first is OS dependent.
         * If the file is not found then the toFile parameter is returned,
         * @param toFile the file to get a proper reference to
         * @return the file if it exists, the canonical reference to the file if it
         *  is on a case sensitive system and the toParameter did not match, or toFile if
         * the file does not exist on the system.
         */
        public static java.io.File get(java.io.File toFile)
        {
            if (!toFile.exists())
            {
                java.io.File loParent = toFile.getParentFile();
                if (loParent != null)
                {
                    // Get the parent recursivley if it doesn't exist
                    loParent = loParent.exists() ? loParent : File.get(loParent);
                    if (loParent.exists())
                    {
                        final String lcFile = toFile.getName();
                        for (java.io.File loFile : loParent.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(java.io.File toFile, String tcString)
                            {
                                return toFile.isDirectory() && tcString.equalsIgnoreCase(lcFile);
                            }
                        }))
                        {
                            try
                            {
                                // If any file matched, return it
                                return loFile.getCanonicalFile();
                            }
                            catch(IOException ex)
                            {
                            }
                        }
                    }
                }
            }
            return toFile;
        }

        /**
         * Gets a reference to a file and attempts to make sure the reference
         * is correct even on case sensitive systems.  If on a case sensitive
         * file system and if the toFile parameter is not an exact match, then
         * a new reference will be returned with the canonical reference to an
         * exact case sensitive match.  If there are multiple matches, which
         * can only happen on case sensitive systems, a reference to the first
         * file will be returned.  The definition of first is OS dependent.
         * If the file is not found then the a file object referencing tcPath parameter is returned,
         * @param tcPath the file to get a proper reference to
         * @return the file if it exists, the canonical reference to the file if it
         *  is on a case sensitive system and the toParameter did not match, or a
         * file object referencing tcPath even if it did not exist
         */
        public static java.io.File get(String tcPath)
        {
            return File.get(new java.io.File(tcPath));
        }

        /**
         * Reads the contents of the specified file into a string
         * @param toFile the file to read the contents of
         * @return the string contents of the file
         */
        public static String toString(java.io.File toFile)
        {
            return toString(toFile, "utf-8");
        }

        /**
         * Reads the contents of the specified file translating using tcCharSet.
         * If the file does not exist, this will return null
         * @param toFile the file to read
         * @param tcCharSet the character set to read using
         * @return the string representation of the file or null if the file doesn't exist or
         * can not be read
         */
        public static String toString(java.io.File toFile, String tcCharSet)
        {
            if (toFile != null && toFile.exists())
            {
                try
                {
                    return Stream.toString(new FileInputStream(toFile), tcCharSet);
                }
                catch (Throwable toException)
                {
                    Application.getInstance().log(toException);
                }
            }
            return null;
        }
    }


    /**
     * Stream related helper functions
     */
    public static class Stream
    {
        // Reads the stream specified and pushes it in to a string
        public static String toString(InputStream toStream)
        {
            return toString(toStream, "utf-8");
        }

        // Reads the stream specified and pushes it to a string
        public static String toString(InputStream toStream, String tcCharSet)
        {
            ReadableByteChannel loChannel = java.nio.channels.Channels.newChannel(toStream);
            Charset loCharset = Charset.forName(tcCharSet);
            StringBuilder loBuilder = new StringBuilder();
            ByteBuffer loBuffer = ByteBuffer.allocateDirect(1024);

            try
            {
                while (loChannel.read(loBuffer) > 0)
                {
                    loBuffer.flip();
                    loBuilder.append(loCharset.decode(loBuffer));
                    loBuffer.compact();
                }
                loChannel.close();
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
            return loBuilder.toString();
        }
    }

    // TODO : replace parameters as data is being streamed


    /**
     * This takes a parameterised string and replaces the parameters with the values from the property set.
     * @param  tcString     the string to be formatted
     * @param  taParameters a property set with the values to replace
     * @return  returns     a new string formatted with the replacement tokens
     */
    public static String replaceParameters(String tcString, PropertySet toParameters)
    {
        return replaceParameters(tcString, toParameters, true);
        
    }
    
    /**
     * This takes a parameterised string and replaces the parameters with the values from the property set.  If the
     * property set does not have the property and tlReplaceAll is false, then the property will not be replaced,
     * if tlReplaceAll is true then the property will be replaced with an empty string if the it does not exist in
     * the property set
     * @param tcString the string to replace parameters in
     * @param toParameters the list of parameters
     * @param tlReplaceAll if true replaces all the parameters
     * @return the string with replacements
     */
    public static String replaceParameters(String tcString, PropertySet toParameters, boolean tlReplaceAll)
    {
        // TODO: Move this to Goliath.IO
        if (toParameters != null && toParameters.size() > 0)
        {
            StringBuffer loBuilder = new StringBuffer();

            Matcher loMatcher = Goliath.Utilities.getRegexMatcher("(?i)\\{\\{(\\w+)}}", tcString);
            while (loMatcher.find())
            {
                Object loValue = toParameters.getProperty(loMatcher.group(1));
                loMatcher.appendReplacement(loBuilder, loValue == null ? (tlReplaceAll ? "" : "{{" + loMatcher.group(1) + "}}") : loValue.toString());
            }
            loMatcher.appendTail(loBuilder);
            return loBuilder.toString();
        }
        return tcString;
    }

    public static String replaceParameters(String tcString, java.lang.Object... taArgs)
    {
        PropertySet loProperties = new PropertySet();
        for (int i=0, lnLength = taArgs.length; i<lnLength; i++)
        {
            loProperties.setProperty(Integer.toString(i), taArgs[i]);
        }
        return replaceParameters(tcString, loProperties);
    }




}
