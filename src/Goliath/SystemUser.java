package Goliath;

import Goliath.Interfaces.Security.ISecurityManager;
import Goliath.Security.User;

/**
 *
 * @author kenmchugh
 */
final class SystemUser extends User
{
    private static SystemUser g_oSystem;

    static User createSystemUser(ISecurityManager toManager)
    {
        // Can only create the system user from the system session
        if (!Session.getCurrentSession().isSystem())
        {
            throw new UnsupportedOperationException("Unable to create the system user");
        }
        // It is possible to have multiple "main" threads simply because of the processes running in main, so the system user might already exist
        return (g_oSystem != null) ? g_oSystem : new SystemUser(toManager);
    }

    private SystemUser(ISecurityManager toManager)
    {
        super(toManager, "SYSTEMUSER");
        if (g_oSystem != null)
        {
            throw new UnsupportedOperationException("Unable to create the system user");
        }
        g_oSystem = this;
        initialiseComponent();
    }

    private void initialiseComponent()
    {
    }

    @Override
    public boolean authenticate(String tcPassword)
    {
        return true;
    }

    @Override
    public Date getExpiry()
    {
        return null;
    }

    @Override
    public boolean isAuthenticated()
    {
        return true;
    }

    @Override
    public boolean isExpired()
    {
        return false;
    }

    @Override
    public void lock()
    {
    }

    @Override
    public void setEmailAddress(String tcEmailAddress)
    {
        // TODO: Implement a system email address for emailing errors and logs
        throw new UnsupportedOperationException("Function not available to System User");
    }

    @Override
    public void setPassword(String tcPassword)
    {   
    }

    @Override
    public boolean unauthenticate()
    {
        return false;
    }

    @Override
    public void unlock()
    {
    }
}
