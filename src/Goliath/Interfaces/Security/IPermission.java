/* =========================================================
 * IPermission.java
 *
 * Author:      kmchugh
 * Created:     20-Jan-2008, 13:17:42
 * 
 * Description
 * --------------------------------------------------------
 * Represents a permission that can be allowed or denied.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.Security;

import Goliath.Security.AccessType;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 20-Jan-2008
 * @author      kmchugh
**/
public interface IPermission
{
    /**
     * Checks if this permission allows read
     * @return true if allowed
     */
    Boolean canRead();

    /**
     * Checks if this permission allows write
     * @return true if allowed
     */
    Boolean canWrite();

    /**
     * Checks if this permission allows execute
     * @return true if allowed
     */
    Boolean canExecute();

    /**
     * Checks if this permission allows Create
     * @return true if allowed
     */
    Boolean canCreate();

    /**
     * Checks if this permission allows Delete
     * @return true if allowed
     */
    Boolean canDelete();

    /**
     * Gets the name of this permission
     * @return the name of the permission
     */
    String getName();
    
    /**
     * Gets the description for this permission
     * @return the description for this permission
     */
    String getDescription();

    /**
     * Merges two permissions in order to create a new permission value
     * For example if P1 has RWE and P2 has -~WE
     * Because deny always overrides, that would become R~WE
     * Another example:  -W- + R-- becomes RW-
     * -~W- -W~E becomes -~W~E
     * @param toPermission
     */
    void mergePermission(IPermission toPermission);

    /**
     * Checks if the access is allow, denied or even set for this permission
     * @param toAccess the access type to check
     * @return true if set, false if denied, and null if not set
     */
    Boolean checkAccess(AccessType toAccess);

}
