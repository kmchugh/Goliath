/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Security;

import Goliath.Security.ResourceType;

/**
 *
 * @author kenmchugh
 */
public interface IResourcePermission extends IPermission
{
    /**
     * Gets the name of the resource that this permission is referring to
     * @return the name of the resource
     */
    String getResourceName();

    /**
     * Gets the name of the resource that this permission is referring to
     * @return the name of the resource
     */
    String getResourceGUID();

    /**
     * Gets the id of the resource that this permission is referring to
     * @return the resource id
     */
    long getResourceID();

    /**
     * Gets the type of this resource permission
     * @return the resource type
     */
    ResourceType getResourceType();


}
