/* ========================================================
 * File.java
 *
 * Author:      admin
 * Created:     Dec 23, 2011, 1:42:45 PM
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
 * ===================================================== */
package Goliath.IO;

import Goliath.DynamicCode.Java;
import Goliath.Exceptions.FileNotFoundException;

/**
 * Utility class for working with files.  This will work with 
 *
 * @see         Related Class
 * @version     1.0 Dec 23, 2011
 * @author      admin
 **/
public class File extends java.io.File
{
    private boolean m_lTempFile;
    
    /**
     * Creates a new reference to a file.  The will return a reference even on 
     * case sensitive systems.
     * On case sensitive systems if there are mutliple files of the same name,
     * then a reference to the file with the exact case will be returned, or if
     * the case is not exact, then the first of the multiple files will be returned.
     * "First" is an OS specific function.
     * The reference will always be represented as a canonical reference
     * @param tcFileName the path to the file
     * @throws FileNotFoundException
     */
    public File(String tcFileName)
            throws FileNotFoundException
    {
        this(Goliath.IO.Utilities.File.get(tcFileName));
    }
    
    /**
     * Creates a new reference to a file.  this will return a reference even on
     * case sensitive systems.
     * On case sensitive systems if there are mutliple files of the same name,
     * then a reference to the file with the exact case will be returned, or if
     * the case is not exact, then the first of the multiple files will be returned.
     * "First" is an OS specific function.
     * The reference will always be represented as a canonical reference
     * @param toFile the file to get a reference to
     */
    public File(java.io.File toFile)
            throws FileNotFoundException
    {
        super(toFile.getPath());
        if (!toFile.exists())
        {
            throw new FileNotFoundException(toFile ,false);
        }
        
        if (Java.isEqualOrAssignable(Goliath.IO.File.class, toFile.getClass()))
        {
            m_lTempFile = ((Goliath.IO.File)toFile).m_lTempFile;
        }
        
        if (m_lTempFile)
        {
            deleteOnExit();
        }
    }
    
    /**
     * Creates a reference to a file and marks it as a temporary file.  Temporary files will be deleted when the jvm
     * closes.
     * @param toFile the file to mark as temporary
     * @param tlTemporaryFile true to mark as a temporary file, if this is set to true it will cause the
     * file to be removed from the OS when the JVM shuts down cleanly
     * @throws FileNotFoundException 
     */
    public File(java.io.File toFile, boolean tlTemporaryFile)
            throws FileNotFoundException
    {
        this(toFile);
        if (Java.isEqualOrAssignable(Goliath.IO.File.class, toFile.getClass()))
        {
            tlTemporaryFile = tlTemporaryFile || ((Goliath.IO.File)toFile).m_lTempFile;
        }
        
        m_lTempFile = tlTemporaryFile;
        if (m_lTempFile)
        {
            deleteOnExit();
        }
    }
    
    
    /**
     * Checks if this file is in the OS temp directory
     * @return true if this is a temporary file
     */
    public boolean isTemporary()
    {
        return m_lTempFile;
    }
    
    
}
