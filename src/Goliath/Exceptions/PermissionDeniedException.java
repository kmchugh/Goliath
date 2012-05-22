/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Exceptions;

import Goliath.Interfaces.Security.IPermission;
import Goliath.Security.AccessType;
import Goliath.Security.User;

/**
 *
 * @author kenmchugh
 */
public class PermissionDeniedException extends Goliath.Exceptions.UncheckedException
{
    public PermissionDeniedException(String tcMessage, IPermission toPermission, AccessType toAccess)
    {
        super(tcMessage + " " + toAccess.getValue() + " Permission denied for " + toPermission.toString());
    }

    public PermissionDeniedException(User toUser, IPermission toPermission, AccessType toAccess)
    {
        super(toAccess.getValue() + " Permission denied for " + toPermission.toString() + " for user " + toUser.getDisplayName());
    }

    public PermissionDeniedException(User toUser, String tcPermission, AccessType toAccess)
    {
        super(toAccess.getValue() + " Permission denied for " + tcPermission + " for user " + toUser.getDisplayName());
    }

    public PermissionDeniedException(IPermission toPermission, AccessType toAccess)
    {
        super(toAccess.getValue() + " Permission denied for " + toPermission.toString());
    }
    
}
