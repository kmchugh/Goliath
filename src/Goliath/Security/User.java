/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Security;

import Goliath.SecurityEntity;
import Goliath.Constants.StringFormatType;
import Goliath.Interfaces.Security.ISecurityManager;

/**
 *
 * @author kenmchugh
 */
public class User extends SecurityEntity
{

    private boolean m_lAuthenticated = false;

    public User(ISecurityManager toManager, String tcID)
    {
        super(toManager, tcID);
    }

    /**
     * Gets the display name for this user, the display name is the name
     * presented to the user or to other users when referencing this user entity
     * @return the display name
     */
    public String getDisplayName()
    {
        return getSecurityManager().getDisplayName(this);
    }

    /**
     * Sets the users display name
     * @param tcDisplayName the new display name
     */
    public void setDisplayName(String tcDisplayName)
    {
        getSecurityManager().setDisplayName(this, tcDisplayName);
    }

    /**
     * Gets the users primary email address, this is the address that should be
     * used to communicate with the user.  The user should always only have one primary address, and
     * the user can change the address at any time, so it should not be used for idenity
     * @return the email address
     */
    public String getEmailAddress()
    {
        return getSecurityManager().getPrimaryEmail(this);
    }

    /**
     * Sets the primary email address for this user
     * @param tcEmailAddress the new email address
     */
    public void setEmailAddress(String tcEmailAddress)
    {
        // TODO: Need to create a method similar to Goliath.Utilities.checkParameterNotNull that checks a regex for a string paramter
        getSecurityManager().setPrimaryEmail(this, tcEmailAddress);
    }

    /**
     * Sets the password for this entity
     * @param tcPassword the new password
     */
    public void setPassword(String tcPassword)
    {
        getSecurityManager().setPassword(this, tcPassword);
    }

    public boolean changePassword(String tcOldPassword, String tcNewPassword)
    {
        return getSecurityManager().changeOwnPassword(tcOldPassword, tcNewPassword);
    }

    /**
     * Attempts to authenticate the the user using the password specified
     * @param tcPassword the password to use to authenticate the user
     * @return true if authenticated, false otherwise
     */
    public boolean authenticate(String tcPassword)
    {
        ISecurityManager loManager = getSecurityManager();
        // If this session is already authenticated, it will remain authenticated.  This could happen when trying to change the password for example
        boolean llReturn = loManager.authenticate(this, tcPassword);
        m_lAuthenticated = isAuthenticated() || llReturn;
        if (m_lAuthenticated)
        {
            loManager.storeEntity(this);
        }
        return llReturn;
    }

    /**
     * Checks if the user is currently authenticated
     * @return true if authenticated, false if not
     */
    public boolean isAuthenticated()
    {
        return m_lAuthenticated;
    }

    /**
     * Checks if the user is locked
     * @return
     */
    public boolean isLocked()
    {
        return getSecurityManager().getLocked(this);
    }
    
    /**
     * Locks the user
     */
    public void lock()
    {
        getSecurityManager().setLocked(this, true);
    }

    /**
     * unlocks the user
     */
    public void unlock()
    {
        getSecurityManager().setLocked(this, false);
    }

    /**
     * Logs the user out
     * @return
     */
    public boolean unauthenticate()
    {
        m_lAuthenticated = !getSecurityManager().unauthenticate(this);
        return !m_lAuthenticated;
    }

    /**
     * Checks if this user is an anonymous user
     * @return true if this is an anonymous user
     */
    public boolean isAnonymous()
    {
        return getSecurityManager().isAnonymous(this);
    }

    /**
     * Sets this user as an anonymous user
     */
    public void setAnonymous()
    {
        getSecurityManager().setAnonymous(this);
    }
    
    /**
     * Gets the number of times the user has logged in to the system
     * @return the user login count
     */
    public long getLoginCount()
    {
        return getSecurityManager().getLoginCount(this);
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return getDisplayName() + "[" + getName() + "]";
    }
}
