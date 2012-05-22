/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Security;

import Goliath.Collections.List;
import Goliath.Date;
import Goliath.Exceptions.PermissionDeniedException;
import Goliath.Security.Group;
import Goliath.Security.PermissionType;
import Goliath.SecurityEntity;
import Goliath.Security.User;

/**
 * The security manager interface
 * @author kenmchugh
 */
public interface ISecurityManager
{
    
    String getGUID(SecurityEntity toEntity);

    void setGUID(SecurityEntity toEntity, String tcGUID);

    String getName(SecurityEntity toEntity);

    void setName(SecurityEntity toEntity, String tcValue);

    String getDescription(Group toGroup);

    void setDescription(Group toGroup, String tcDescription);

    long getLoginCount(User toUser);

    long getID(SecurityEntity toEntity);

    User getUser(String tcUserName);

    Group getGroup(String tcGUID);

    User getUserByGUID(String tcGUID);

    void storeEntity(SecurityEntity toEntity);

    User createUser(String tcUserName, String tcEmail, String tcPassword, String tcDisplayName, String tcDescription);

    User createUser(String tcUserName, String tcEmail, String tcPassword, String tcDisplayName, String tcDescription, boolean tlUseSystem);

    User createSystemUser(String tcUserName, String tcEmail, String tcPassword);

    User createAnonymousUser();

    Group createGroup(String tcGUID);

    void checkPermission(PermissionType toType) throws PermissionDeniedException;

    boolean getLocked(User toUser);

    void setLocked(User toUser, boolean tlLocked);

    boolean changePassword(User toUser, String tcOldPassword, String tcNewPassword);

    boolean changeOwnPassword(String tcOldPassword, String tcNewPassword);

    boolean resetPassword(User toUser, String tcNewPassword);

    boolean resetOwnPassword(String tcNewPassword);

    boolean deleteGroup(Group toGroup);

    boolean deleteUser(User toUser);

    List<Group> getMembershipList();

    List<IPermission> getPermissionList();

    boolean addAsMember(SecurityEntity toEntity, Group toGroup);

    boolean addPermission(SecurityEntity toEntity, IPermission toPermission);

    String getDisplayName(User toUser);

    void setDisplayName(User toUser, String tcDisplayName);

    String getPrimaryEmail(User toUser);

    void setPrimaryEmail(User toUser, String tcEmail);

    Date getExpiry(SecurityEntity toEntity);

    void setExpiry(SecurityEntity toEntity, Date toExpiry);

    void setPassword(User toUser, String tcPassword);

    boolean authenticate(User toUser, String tcPassword);

    boolean unauthenticate(User toUser);

    boolean isExpired(SecurityEntity toEntity);

    void setAnonymous(User toUser);

    boolean isAnonymous(User toUser);

}
