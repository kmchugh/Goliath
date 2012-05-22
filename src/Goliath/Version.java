/* =========================================================
 * Version.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 10:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This is a representation of versioning for components.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;


/**
 * This class represents a version and version comparison
 * mechanism.
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class Version extends Goliath.Object 
        implements Goliath.Interfaces.IVersion
{
    private String m_cName;
    private String m_cDescription;
    private int m_nMajor;
    private int m_nMinor;
    private int m_nBuild;
    private int m_nRevision;
    private Date m_dRelease;


    /**
     * Creates a new Version Object
     */
    protected Version()
    {
        
    }

    /**
     * Creates a new instance of a version object, attempting to parse the version string
     * @param tcVersionString the version string to attempt to parse the version from.
     */
    public Version(String tcVersionString)
    {
        // TODO: implement parsing the version string
        String[] laParts = tcVersionString.split(" ");

        // Parse out the numbers e.g (2.0.1.0)
        if(laParts.length == 1)
        {
            laParts = laParts[0].split("\\.");
            m_nMajor = Integer.parseInt(laParts[0]);
            if (laParts.length > 1)
            {
                m_nMinor = Integer.parseInt(laParts[1]);
            }
            if (laParts.length > 2)
            {
                m_nBuild = Integer.parseInt(laParts[2]);
            }
            if (laParts.length > 3)
            {
                m_nRevision = Integer.parseInt(laParts[3]);
            }
            m_dRelease = new Date(0);
        }
    }

    /**
     * Creates a new Version object
     *
     * @param  tcName          The name of the item that is being versioned
     * @param  tnMajor         The major version number
     * @param  tnMinor         The minor version number
     * @param  tnBuild         The build version number
     * @param  tnRevision      The revision version number
     * @param  tdRelease       The Release date
     * @param  tcDescription    The Description of the item that is being versioned
     */
    public Version(String tcName, int tnMajor, int tnMinor, int tnBuild, int tnRevision, java.util.Date tdRelease, String tcDescription)
    {
        m_cName = tcName;
        m_nMajor = tnMajor;
        m_nMinor = tnMinor;
        m_nBuild = tnBuild;
        m_nRevision = tnRevision;
        m_dRelease = new Date(tdRelease);
        m_cDescription = tcDescription;
    }
    
    /**
     * Creates a new Version object
     *
     * @param  tcName          The name of the item that is being versioned
     * @param  tnMajor         The major version number
     * @param  tnMinor         The minor version number
     * @param  tnBuild         The build version number
     * @param  tnRevision      The revision version number
     * @param  tcRelease       The Release date in string format
     * @param  tcDescription    The Description of the item that is being versioned
     *
     * @throws Goliath.Exceptions.InvalidParameterException if tcRelease can not be parsed to a date
     */
    public Version(String tcName, int tnMajor, int tnMinor, int tnBuild, int tnRevision, String tcRelease, String tcDescription)
        throws Goliath.Exceptions.InvalidParameterException
    {
        m_cName = tcName;
        m_nMajor = tnMajor;
        m_nMinor = tnMinor;
        m_nBuild = tnBuild;
        m_nRevision = tnRevision;
        m_cDescription = tcDescription;
        m_dRelease = new Date(tcRelease);
    }

    /**
     * Returns the string representation of this object
     *
     * @see   Goliath.Object#toString()
     * @param  toFormat  The format to display the string in
     * @return  The String representation of the object
     */
    @Override
    protected String formatString(Goliath.Constants.StringFormatType toFormat)
    {
        return m_cName + ", V" 
                + Integer.toString(m_nMajor) + "."
                + Integer.toString(m_nMinor) + "."
                + Integer.toString(m_nBuild) + "."
                + Integer.toString(m_nRevision) + "("
                + Utilities.Date.toString(m_dRelease) + ")";
    }
    
    /**
     * Returns the name of the item versioned
     *
     * @return     The name of the versioned item
     */
    @Override
    public String getName()
    {
        return m_cName;
    }

    /**
     * Sets the Name of the item being versioned
     * @param tcName the name of the item
     */
    protected void setName(String tcName)
    {
        m_cName = tcName;
    }
    
    /**
     * Returns the major version number
     *
     * @return     the major number
     */
    @Override
    public int getMajor()
    {
        return m_nMajor;
    }

    /**
     * Sets the major version number of the item being versioned
     * @param tnValue the new value
     */
    protected void setMajor(int tnValue)
    {
        m_nMajor = tnValue;
    }

     /**
     * Returns the minor version number
     *
     * @return      the minor number
     */
    @Override
    public int getMinor()
    {
        return m_nMinor;
    }

    /**
     * Sets the major version number of the item being versioned
     * @param tnValue the new value
     */
    protected void setMinor(int tnValue)
    {
        m_nMinor = tnValue;
    }


     /**
     * Returns the build version number
     *
     * @return        the build number
     */
    @Override
    public int getBuild()
    {
        return m_nBuild;
    }

    /**
     * Sets the build version number of the item being versioned
     * @param tnValue the new value
     */
    protected void setBuild(int tnValue)
    {
        m_nBuild = tnValue;
    }

    
     /**
     * Returns the revision version number
     *
     * @return    the revision number
     */
    @Override
    public int getRevision()
    {
        return m_nRevision;
    }

    /**
     * Sets the revision version number of the item being versioned
     * @param tnValue the new value
     */
    protected void setRevision(int tnValue)
    {
        m_nRevision = tnValue;
    }

    
    /**
     * Returns the release date
     *
     * @return     The release date
     */
    @Override
    public java.util.Date getReleaseDate()
    {
        return m_dRelease.getDate();
    }

    /**
     * Sets the release date of the item being versioned
     * @param tdDate the new date
     */
    protected void setReleaseDate(java.util.Date tdDate)
    {
        m_dRelease = new Date(tdDate);
    }

    /**
     * Returns the description
     *
     * @return     The description
     */
    @Override
    public String getDescription()
    {
        return m_cDescription;
    }

    /**
     * Sets the description of the item being versioned
     * @param tcValue the new description
     */
    protected void setDescription(String tcValue)
    {
        m_cDescription = tcValue;
    }



    
}
