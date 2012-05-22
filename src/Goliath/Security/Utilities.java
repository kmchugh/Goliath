/* ========================================================
 * Utilities.java
 *
 * Author:      admin
 * Created:     Dec 31, 2011, 7:52:57 AM
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
package Goliath.Security;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Constants.RegularExpression;
import Goliath.Exceptions.InvalidOperationException;
import Goliath.Interfaces.ISession;
import Goliath.Interfaces.Security.ISecurityManager;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 31, 2011
 * @author      admin
 **/
public class Utilities extends Goliath.Object
{
    /**
     * Changes the password of the user that is authenticated in the session passed in
     * @param lcOldPassword the old (current) password for the user
     * @param lcNewPassword the new password for the user
     * @param loSession
     * @param loServet
     * @param toResponse
     * @return true if password change is successful
     */
    public static boolean changeUserPassword(ISession toSession, String tcOldPassword, String tcNewPassword, List<Throwable> toErrors)
    {
        try
        {
            if (!Goliath.Utilities.isNullOrEmpty(tcNewPassword) && !Goliath.Utilities.isNullOrEmpty(tcOldPassword))
            {
                if (Goliath.Utilities.getRegexMatcher(RegularExpression.PASSWORD_STRONG().getValue(), tcNewPassword).matches())
                {
                    User loUser = toSession.getUser();
                    if (loUser == null || !loUser.changePassword(tcOldPassword, tcNewPassword))
                    {
                        toErrors.add(new Goliath.Exceptions.Exception("There was a problem changing the password for the user " + loUser.getGUID(), false));
                    }
                }
                else
                {   
                    toErrors.add(new Goliath.Exceptions.Exception("Must select a password which has 6 - 20 characters, contains at least one lower case, one upper case, one special character, and one number ", false));
                }
            }
            else
            {
                toErrors.add(new Goliath.Exceptions.Exception("The current password and a new password must be provided", false));

            }
        }
        catch (Throwable ex)
        {
            toErrors.add(ex);
        }
        return toErrors.size() == 0;
    }
    
    
    /**
     * Signs the user in to the session that has been passed in
     * @param toSession the session to sign the user in to
     * @param tcUserName the name of the user to sign in
     * @param tcPassword the password for the user
     * @param toErrors if there are any errors, they should be appended to this list
     * @return true if sign in is successful
     */
    public static boolean signinUser(ISession toSession, String tcUserName, String tcPassword, List<Throwable> toErrors)
    {
        // Session must not be authenticated
        if (!toSession.isAuthenticated())
        {
            // Require a user name and password
            if (!Goliath.Utilities.isNullOrEmpty(tcUserName) && !Goliath.Utilities.isNullOrEmpty(tcPassword))
            {
                try
                {
                    // Get the user
                    User loUser = Application.getInstance().getSecurityManager().getUser(tcUserName);
                    // Authenticate the user
                    if (loUser == null || !toSession.authenticate(loUser, tcPassword))
                    {
                        // If there is a problem, ensure the current user is logged out if there is one
                        if (toSession.isAuthenticated())
                        {
                            toSession.reset();
                        }
                        toErrors.add(new Goliath.Exceptions.SecurityException("The user name or password is incorrect.", false));
                    }
                }
                catch (Throwable ex)
                {
                    toErrors.add(ex);
                }
            }
            else
            {
                toErrors.add(new Goliath.Exceptions.Exception("Please provide a user name and a password.", false));
            }
        }
        else
        {
            toErrors.add(new Goliath.Exceptions.Exception("This session is already authenticated", false));
        }
        return toErrors.size() == 0;
    }
    
    /**
     * Get the session and unauthenticates it
     * @param loSession the session to sign the user out of
     * @param toErrors if there are any errors, they should be appended to this list
     * @return true if unauthentication is successful.
     */
    public static boolean signoutUser(ISession toSession, List<Throwable> toErrors)
    {
        if (toSession.isAuthenticated())
        {
            try
            {
                if (!toSession.unauthenticate())
                {
                    toErrors.add(new Goliath.Exceptions.SecurityException("Unable to unauthenticate session", false));
                }
            }
            catch (Throwable ex)
            {
                toErrors.add(ex);
            }
        }
        else
        {
            toErrors.add(new Goliath.Exceptions.SecurityException("The session has never been authenticated", false));
        }
        return toErrors.size() == 0;
    }
    
    /**
     * Deletes the user if possible, this will only delete the user if the user is currently logged in, and knows their
     * password, basically this means that through this method, a user can only delete themselves.
     * 
     * This will log the user out, delete the user, then assign a new anonymous user to the session
     * 
     * @param toSession the session to delete the user of
     * @param tcUserName the name of the user
     * @param tcPassword the password of the user to delete
     * @param toErrors the error list, if any errors occur during this method they should be added to the list
     * @return true if the user was deleted, false otherwise
     */
    public static boolean deleteUser(ISession toSession, String tcPassword, List<Throwable> toErrors)
    {
        if (!Goliath.Utilities.isNullOrEmpty(tcPassword))
        {
            User loUser = toSession.getUser();
            try
            {
                if (loUser.authenticate(tcPassword))
                {
                    toSession.unauthenticate();
                    ISecurityManager loSecurityManager = Application.getInstance().getSecurityManager();
                    if (!loSecurityManager.deleteUser(loUser))
                    {
                        toErrors.add(new Goliath.Exceptions.SecurityException("The user [" + loUser.getGUID() + "] can not be deleted.", false));
                    }
                }
                else
                {
                    toErrors.add(new Goliath.Exceptions.SecurityException("Could not validate user with the supplied password", false));
                }
            }
            catch (Throwable ex)
            {
                toErrors.add(ex);
            }
        }
        else
        {
            toErrors.add(new InvalidOperationException("A username and password must be supplied", false));
        }
        return toErrors.size() == 0;
    }
    
    /**
     * Locks the user that is in the session
     * @param toSession the session to lock
     * @param toErrors the error list, if any errors occur during this method they should be added to the list
     * @return true if the user was locked after this call
     */
    public static boolean lockUser(ISession toSession, List<Throwable> toErrors)
    {
        User loUser = toSession.getUser();
        try
        {
            loUser.lock();
            if (!loUser.isLocked())
            {
                toErrors.add(new Goliath.Exceptions.SecurityException("The user [" + loUser.getGUID() + "] can not be locked.", false));
            }
        }
        catch (Throwable ex)
        {
            toErrors.add(ex);
        }
        return toErrors.size() == 0;
    }
    
    /**
     * Unlocks the specified user
     * @param toSession the session
     * @param tcUserName the user name of the user to unlock
     * @param toErrors the error list, if any errors occur during this method they should be added to the list
     * @return true if the user was unlocked after this call
     */
    public static boolean unlockUser(ISession toSession, String tcUserName, List<Throwable> toErrors)
    {
        // TODO: Check that the current user has permission to unlock a user
        
        try
        {
            User loUser = Application.getInstance().getSecurityManager().getUser(tcUserName);
            if (loUser != null)
            {
                loUser.unlock();
                if (loUser.isLocked())
                {
                    toErrors.add(new Goliath.Exceptions.SecurityException("The user [" + tcUserName + "] can not be unlocked.", false));
                }
            }
            else
            {
                toErrors.add(new Goliath.Exceptions.SecurityException("The user [" + tcUserName + "] may not exist.", false));
            }
        }
        catch (Throwable ex)
        {
            toErrors.add(ex);
        }
        return toErrors.size() == 0;
        
    }
    
    /**
     * Creates a user using the user name and password provided
     * @param toSession the session we are acting in, the newly created user will be logged in to this session
     * @param tcUserName the user name for the new user
     * @param tcDisplayName name to use when displaying the user, if null then the user name will be used
     * @param tcEmail email address for the user, can be null
     * @param tcPassword the password for the new user
     * @param toErrors the error list, if any errors occur during this method they should be added to the list
     * @return true if the user was created successfully
     */
    public static boolean registerUserFromUserName(ISession toSession, String tcUserName, String tcDisplayName, String tcEmail, String tcPassword, List<Throwable> toErrors)
    {
        if (!Goliath.Utilities.isNullOrEmpty(tcUserName) && !Goliath.Utilities.isNullOrEmpty(tcPassword))
        {
            //Password must contains mixed cases,numbers and special characters
            if (Goliath.Utilities.getRegexMatcher(RegularExpression.PASSWORD_STRONG().getValue(), tcPassword).matches())
            {
                ISecurityManager loSecurityManager = Application.getInstance().getSecurityManager();
                User loUser = loSecurityManager.getUser(tcUserName);
                if (loUser == null)
                {
                    try
                    {
                        loUser = loSecurityManager.createUser(tcUserName,
                                    Goliath.Utilities.isNullOrEmpty(tcEmail) ? "anonymous@anonymous.com" : tcEmail,
                                    tcPassword,
                                    Goliath.Utilities.isNullOrEmpty(tcDisplayName) ? tcUserName : tcDisplayName,
                                    "",
                                    true);
                        
                        //Create the account and sign in the user.
                        if (!toSession.authenticate(loUser, tcPassword))
                        {
                            toErrors.add(new Goliath.Exceptions.Exception("User " + tcUserName + " was created, but the server was unable to authenticate session", true));
                        }
                    }
                    catch (Throwable ex)
                    {
                        toErrors.add(ex);
                    }
                }
                else
                {
                    toErrors.add(new Goliath.Exceptions.Exception("A user already exists with the user name " + tcUserName + ".", false));
                }
            }
            else
            {
                toErrors.add(new Goliath.Exceptions.Exception("Must select a password which has 6 - 20 characters, contains at least one lower case, one upper case, one special character, and one number ", false));
            }

        }
        else
        {
            toErrors.add(new Goliath.Exceptions.Exception("A user name and password must be provided.", true));
        }
        return false;
    }
    
    /**
     * Creates a user using the user name and password provided
     * @param toSession the session we are acting in, the newly created user will be logged in to this session
     * @param tcDisplayName name to use when displaying the user, if null then the first part of the email address will be used
     * @param tcEmail email address for the user
     * @param toPassword the generated password will be placed in here, if anything is in here, the generated password will be appended to it and used as the password
     * @param toErrors the error list, if any errors occur during this method they should be added to the list
     * @return true if the user was created successfully
     */
    public static boolean registerUserFromEmail(ISession toSession, String tcDisplayName, String tcEmail, StringBuilder toPassword, List<Throwable> toErrors)
    {
        if (!Goliath.Utilities.isNullOrEmpty(tcEmail) && Goliath.Utilities.getRegexMatcher(RegularExpression.EMAIL_ADDRESS().getValue(), tcEmail).matches())
        {
            toPassword.append(generatePassword());
            String lcPassword = toPassword.toString();
            
            ISecurityManager loSecurityManager = Application.getInstance().getSecurityManager();
            User loUser = loSecurityManager.getUser(tcEmail);
            if (loUser == null)
            {
                try
                {
                    loUser = loSecurityManager.createUser(tcEmail,
                                tcEmail,
                                lcPassword,
                                Goliath.Utilities.isNullOrEmpty(tcDisplayName) ? tcEmail.replaceFirst("@.+$", "") : tcDisplayName,
                                "",
                                true);

                    //Create the account and sign in the user.
                    if (!toSession.authenticate(loUser, lcPassword))
                    {
                        toErrors.add(new Goliath.Exceptions.Exception("User " + tcEmail + " was created, but the server was unable to authenticate session", true));
                    }
                }
                catch (Throwable ex)
                {
                    toErrors.add(ex);
                }
            }
            else
            {
                toErrors.add(new Goliath.Exceptions.Exception("A user already exists with the user name " + tcEmail + ".", false));
            }
        }
        else
        {
            toErrors.add(new Goliath.Exceptions.Exception("An email address must be provided and must be in the correct format.", true));
        }
        return toErrors.size() == 0;
    }
    
    /**
     * Resets the password of the user specified
     * @param toSession the session 
     * @param tcUserName the user name that we are resetting
     * @param toPassword the location of the password, this will be filled with the new password
     * @param toErrors
     * @return 
     */
    public static boolean resetUserPassword(ISession toSession, String tcUserName, StringBuilder toPassword, List<Throwable> toErrors)
    {
        User loUser = Application.getInstance().getSecurityManager().getUser(tcUserName);
        if (loUser != null)
        {
            toPassword.append(generatePassword());
            String lcPassword = toPassword.toString();
            if(!Application.getInstance().getSecurityManager().resetPassword(loUser, lcPassword))
            {
                toErrors.add(new Goliath.Exceptions.Exception("Unable to reset the password for this user.", false));
            }
        }
        else
        {
            toErrors.add(new Goliath.Exceptions.Exception("A user by this name may not exist.", false));
        }
        return toErrors.size() == 0;
    }
    
    /**
     * Generate a string that is usable as a password
     * @return Generated password.
     */
    public static String generatePassword()
    {
        return Goliath.Utilities.generateStringGUID().substring(0, 12).replaceAll("-", "");
    }
    
    /**
     * Creates a new instance of Utilities
     */
    private Utilities()
    {
    }
}
