/* =========================================================
 * Workspace.java
 *
 * Author:      kenmchugh
 * Created:     Sep 21, 2010, 9:55:54 AM
 *
 * Description
 * --------------------------------------------------------
 * The workspace is the area where all items related to a
 * working area are encapsulated.  Database, files, etc,
 * are all based on the workspace.
 *
 * All directories retrieved from the workspace are based
 * on the workspace location
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath;

import java.io.File;

/**
 *
 * @author kenmchugh
 */
public class Workspace extends Goliath.Object
{
    private String m_cName;
    private String m_cBaseDirectory;

    public Workspace()
    {
    }

    /**
     * Checks if the workspace is actually valid
     * @return true if it is
     */
    public boolean isValid()
    {
        return !Goliath.Utilities.isNullOrEmpty(m_cName) && !Goliath.Utilities.isNullOrEmpty(m_cName);
    }

    /**
     * Gets the name of this workspace
     * @return the name
     */
    public String getName()
    {
        return m_cName;
    }

    /**
     * Sets the name of this workspace, this can only be set once
     * @param tcName the name of the workspace
     */
    public void setName(String tcName)
    {
        if (!Goliath.Utilities.isNullOrEmpty(m_cName) || Goliath.Utilities.isNullOrEmpty(tcName))
        {
            throw new Goliath.Exceptions.UnsupportedOperationException("The name has either already been set or you are trying to set it to null");
        }
        m_cName = tcName;
    }

    /**
     * Gets the base directory of this workspace
     * @return the base directory of this work space
     */
    public String getBaseDirectory()
    {
        return m_cBaseDirectory;
    }

    /**
     * Retrieves a directory from the workspace, if the directory does not exist, then it will be created
     * here as well
     * @param tcDirectory the directory to get.  The directory can be in the format "DirectoryName" or "DirectoryName/SubDirectory"
     * @return
     */
    @Goliath.Annotations.NotProperty
    public File getDirectory(String tcDirectory)
    {
        if (tcDirectory.contains(".."))
        {
            throw new UnsupportedOperationException("Not allowed to specify .. as a directory path");
        }

        if (tcDirectory.startsWith("/") || tcDirectory.startsWith(Environment.FILESEPARATOR()))
        {
            tcDirectory = tcDirectory.substring(1);
        }

        File loFile = new File(m_cBaseDirectory + tcDirectory);
        if (!loFile.exists())
        {
            if (!loFile.mkdirs())
            {
                throw new Goliath.Exceptions.UnsupportedOperationException("The path - [" + tcDirectory + "] could not be created.");
            }
        }
        if (!loFile.isDirectory())
        {
            throw new Goliath.Exceptions.UnsupportedOperationException("The path - [" + tcDirectory + "] is not a directory.");
        }
        return loFile;
    }

    /**
     * Sets the base directory
     * @param tcDirectory
     */
    public void setBaseDirectory(String tcDirectory)
    {
        if (!Goliath.Utilities.isNullOrEmpty(m_cBaseDirectory) || Goliath.Utilities.isNullOrEmpty(tcDirectory))
        {
            throw new Goliath.Exceptions.UnsupportedOperationException("The directory has either already been set or you are trying to set it to null");
        }

        // Make sure the variable is actually a directory, or can be created as a directory
        File loFile = new File(tcDirectory);
        if (!loFile.exists())
        {
            if (!loFile.mkdirs())
            {
                throw new Goliath.Exceptions.UnsupportedOperationException("The path - [" + tcDirectory + "] could not be created.");
            }
        }

        if (!loFile.isDirectory())
        {
            throw new Goliath.Exceptions.UnsupportedOperationException("The path - [" + tcDirectory + "] is not a directory.");
        }

        m_cBaseDirectory = loFile.getAbsolutePath();
    }

    @Override
    public boolean equals(java.lang.Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Workspace other = (Workspace) obj;
        if ((this.m_cName == null) ? (other.m_cName != null) : !this.m_cName.equals(other.m_cName))
        {
            return false;
        }
        if ((this.m_cBaseDirectory == null) ? (other.m_cBaseDirectory != null) : !this.m_cBaseDirectory.equals(other.m_cBaseDirectory))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + (this.m_cName != null ? this.m_cName.hashCode() : 0);
        hash = 53 * hash + (this.m_cBaseDirectory != null ? this.m_cBaseDirectory.hashCode() : 0);
        return hash;
    }

    
}
