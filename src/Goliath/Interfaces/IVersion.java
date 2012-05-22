/* =========================================================
 * IVersion.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This interface represents a Version.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces;

/**
 * This interface represents a Version
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public interface IVersion
{
     /**
     * Gets the name of the component 
     * @return the name of the component
     */
    String getName();
    
     /**
     * Gets the major build number of the component 
     * @return the major build number of the component
     */
    int getMajor();
    
    /**
     * Gets the minor build number of the component 
     * @return the minor build number of the component
     */
    int getMinor();
    
    /**
     * Gets the build build number of the component 
     * @return the build build number of the component
     */
    int getBuild();
    
    /**
     * Gets the revision build number of the component 
     * @return the revision build number of the component
     */
    int getRevision();
    
    /**
     * Gets the release date of the component 
     * @return the release date of the component
     */
    java.util.Date getReleaseDate();

    /**
     * Returns the description
     *
     * @return     The description
     */
     String getDescription();
}
