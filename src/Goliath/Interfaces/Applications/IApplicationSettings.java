/* =========================================================
 * IApplicationSettings.java
 *
 * Author:      kmchugh
 * Created:     26 November 2007, 15:24
 *
 * Description
 * --------------------------------------------------------
 * Application Setting interface, controls the application
 * and all the application behaviours.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Interfaces.Applications;

import Goliath.Applications.ApplicationState;
import Goliath.ClientType;
import Goliath.Interfaces.ISessionManager;
import Goliath.Interfaces.LogHandlers.ILogHandler;
import Goliath.Interfaces.Messaging.IMessageBroker;
import Goliath.Interfaces.PropertyHandlers.IPropertyHandler;
import Goliath.Interfaces.Scheduling.IScheduler;
import Goliath.Interfaces.Security.ISecurityManager;
import Goliath.Workspace;

/**
 * Application Setting interface, controls the application
 * and all the application behaviours.
 *
 * @version     1.0 26 November 2007
 * @author      kmchugh
 **/
public interface IApplicationSettings
{
    /**
     * This is called when a client type could not be determined.
     * @param tcIdentifier the identifier of the client type
     * @return a new default unknown client type object
     */
    ClientType createUnknownClientType(String tcIdentifier);

    /**
     * Alerts the user with the specified message
     * @param tcMessage the message to use to alert the user
     */
    void alert(String tcMessage, String tcTitle);

    /**
     * Specifies if the application should use the Application Data directory as the
     * root rather than the application root
     * @return true if we should be using the Application Data directory
     */
    boolean useApplicationDataDirectory();

    /**
     * Creates the default workspace for this application
     * @return the default workspace
     */
    Workspace createDefaultWorkspace();

    /**
     * Creates the Application Controller for this application
     * @return the Application controller
     */
    IApplicationController createApplicationController();
    

    /**
     * Gets the name of the application
     * @return The application name
     */
    String getName();

    /**
     * Run the internal test suite if required
     */
    void runInternalTests();
    /**
     * Creates a license handler for a user GUILD
     * @return a new property handler
     */
    IPropertyHandler createLicenseHandler(String tcUserGUID);
    /**
     * Creates a property handler for the application
     * @return a new property handler
     */
    IPropertyHandler createPropertyHandler();
    
    /**
     * Creates a log handler for the application
     * @return a new log handler
     */
    ILogHandler createLogHandler();

    /**
     * Creates a Session Manager for the application
     * @return a new session handler
     */
    ISessionManager createSessionManager();

    /**
     * Creates a Session Manager for the application
     * @return a new session handler
     */
    ISecurityManager createSecurityManager();


    /**
     * Creates the message broker for the application
     * @return the message broker for the application
     */
    IMessageBroker createMessageBroker();


    /**
     * Creates the scheduler for the application
     * @return the scheduler for the application
     */
    IScheduler createScheduler();
    
    /**
     * deactivates and unload application settings
     */
    void deactivate();
    
    /**
     * Activates and loads application settings
     */
    void activate();
    
    /**
     * Checks if the settings are currently active
     * @return true if the settings are active
     */
    boolean isActive();
    
    void doSetup();
      
    /**
     * Sets an application property to the specified value
     * @param <T> The type of the property
     * @param tcProperty the name of the property to set
     * @param toValue the value
     * @return true if the properties of the application were changed because of this call
     */
    <T> boolean setProperty(String tcProperty, T toValue);
    
    /**
     * Clears the value of an application property
     * @param tcProperty the property to clear
     * @return true if the property collection was changed due to this call
     */
    boolean clearProperty(String tcProperty);

    /**
     * Gets the value of an application property
     * This will return null if the property does not exist
     * @param <T> The type of the property
     * @param tcProperty the property to get the value for
     * @return the value of the property
     */
    <T> T getProperty(String tcProperty);
    
    /**
     * Gets the value of an application property,
     * If this property does not exist the the value of toProperty will be returned
     * and the property will be set to the value of toProperty
     * @param <T> The type of the property
     * @param toProperty the property to get the value for
     * @return the value of the property
     */
    <T> T getProperty(String tcName, T toDefault);
    
    /**
     * Gets whether the application to be single instance
     * @return - whether or not the application is single instance 
     */
     boolean isSingleInstance();
     
     /**
      * Shows the splash screen if needed
      */
     void showSplash();

     /**
      * Hides the splash screen and removes it from memory
      */
     void hideSplash();
     
     /**
      * Called when the application state changes
      */
     void applicationStateChanged(ApplicationState toNewState);
     
     
    
}
