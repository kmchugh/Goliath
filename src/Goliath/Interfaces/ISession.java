/* =========================================================
 * ISession.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This interface represents a running Session.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.Interfaces;

import Goliath.Applications.ClientInformation;
import Goliath.Constants.SessionEventType;
import Goliath.Event;
import Goliath.Interfaces.Commands.ICommand;
import Goliath.Security.User;

/**
 * This interface represents a running Session
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public interface ISession
        extends IEventDispatcher<SessionEventType, Event>
{
    
    void start();
    
    void cleanUp();

    /**
     * Should be called after the session is created and when it is ready to be used
     */
    void initialiseSession();

    /**
     * Gets the client information for this session
     * @return the client information or null if there is no information
     */
    ClientInformation getClientInformation();

    /**
     * Updates the current client information with the details from the specified information object
     * @param toInfo the information to merge
     */
    void updateClientInformation(ClientInformation toInfo);

    /**
     * Checks if this is the system session or not
     * @return true if this is the system session
     */
    boolean isSystem();

    /**
     * Gets the number of milliseconds before this session will expire
     * @return the number of milliseconds until expiry
     */
    long getRemainingTime();

    /**
     * Returns true if there are commands being processed on this session
     * @return true if there are commands being processed on this session
     */
    boolean hasCommands();
    
    /**
     * Checks if a command is registered with this session
     * @param toCommand the command to check
     * @return true if registered otherwise false
     */
    boolean isRegistered(ICommand toCommand);
    
    /**
     * Returns the session id for this session
     *@return the String representation of the session id
     */
    String getSessionID();

    /**
     * Gets the IP Address for this session if it is known
     * @return the session IP Address if it is known
     */
    String getSessionIP();

    /**
     * Sets the session IP Address
     * @param tcIP the IP Address used for the session
     */
    void setSessionIP(String tcIP);

    /**
     * checks if the session has been authenticated
     * @return  true if the session has been authenticated
     */
    boolean isAuthenticated();
    /**
     * Gets the display name for this session, usually the username
     * @return the display name of the session
     */
    String getDisplayName();

    /**
     * Gets the user name for this session
     *
     * @return The user name for the session
     */
    String getUserName();

    /**
     * Gets the email address for this session
     *
     * @return The email address for the session
     */
    String getEmail();

    /**
     * Gets the GUID of the user of this session
     * @return the GUID
     */
    String getUserGUID();
    
    /**
     * checks if the session has expired
     *
     * @return  true if the session has expired
     */
    boolean isExpired();
    /**
     * gets the date/time this session will expire
     * @return  the date and time the session will expire
     */
    java.util.Date getExpiry();

    /**
     * Sets the maximum length the session expiry length is allowed to be set to
     * @param tnLength the new expiry length
     */
    void setMaximumExpiryLength(long tnLength);

    /**
     * Gets the maximum expiry length, the expiry length can not be set to more than this value
     * @return the maximum expiry length
     */
    long getMaximumExpiryLength();
 
    /**
     * Increments the session expiry by the default time
     * @return true if the session is renewed properly
     */
    boolean renew();
     /**
     * Removes authentication from this session (logs the session out)
     *
     * @return  true if the session was unauthenticated correctly
     */  
    boolean unauthenticate();
    
     /**
     * Resets the session
     *
     * @return  true if the session was reset correctly
     */  
    boolean reset();
    
    void clearProperties();
    
    /**
     * Authenticates the session
     *
     * @param  tcUser   the user to use to authenticate
     * @param tcPassword    the password to use to authenticate
     * @return true if the authentication was successful
     */
    boolean authenticate(User tcUser, String tcPassword);
    
    /**
     * Gets the user for this session 
     *
     * @return the user of the session
     */
    User getUser();
    
    /**
     * Sets a property for the application
     * @param <T> 
     * @param tcProperty the property to set
     * @param toValue the value to set the property to
     * @return true if the application settings were changed because of this call
     */
    <T> boolean setProperty(String tcProperty, T toValue);
    
    /**
     * Gets the value of a property
     * @param <T> 
     * @param tcProperty the property to get
     * @return the value of the property, or null if the property is not found
     */
    <T> T getProperty(String tcProperty);
    
    /**
     * Clears a property from the application
     * @param tcProperty the property to clear
     * @return true if the settings were changed because of this call
     */
    boolean clearProperty(String tcProperty);
    
    /**
    * Set Language for the session
    * @param  toLanguage   Language object of the language to be set for this session
    */
    public void setLanguage(java.lang.Object toLanguage);

    /**
    * Get Language for the session
    * @return Language object of the language set for the session
    */
    public Object getLanguage();
    
    /**
    * Is MultiLingual
    * @return Boolean flag indicating whether or not the session has been set
    *  for multilingual
    */
    public  boolean isMultiLingual();

    /**
     * Pause processing of commands in this session
     */
    void pause();
    
    /**
     * Resume processing of commands in this session
     */
    void resume();

    /**
     * Gets the length of time in milliseconds that is added to the expiry time every time the session is refreshed
     * @return the number of milliseconds
     */
    long getExpiryLength();

    /**
     * Sets the time in milliseconds that will be added to the session expiry time on a refresh
     * @param tnLength the new number of milliseconds
     */
    void setExpiryLength(long tnLength);
    
    void addCommand(ICommand<?, ?> toCommand);
    void addCompletedCommand(ICommand<?, ?> toCommand);
    java.lang.Object popCommandResult(ICommand<?, ?> toCommand);
    
    Goliath.Collections.HashTable<String, Float> getCommandStatus();
    Goliath.Collections.HashTable<ICommand<?, ?>, java.lang.Object> getCommandResults();
}
