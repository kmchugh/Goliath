/* =========================================================
 * IApplication.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This interface represents the Application.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.Interfaces.Applications;

import Goliath.Applications.ApplicationState;
import Goliath.Constants.LogType;
import Goliath.Interfaces.Messaging.IMessageBroker;
import Goliath.Interfaces.Scheduling.IScheduler;
import Goliath.Workspace;
import Goliath.Interfaces.PropertyHandlers.IPropertyHandler;
import java.io.File;
import java.util.TimerTask;

/**
 * This represents the Application
 * <pre>
 *      IApplication loApplication = Goliath.Applications.Application.getInstance();
 * </pre>
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
 **/
public interface IApplication
{
    /**
     * Gets the application settings for the applicaton
     * @return the application settings
     */
    IApplicationSettings getApplicationSettings();

    /**
     * Gets the application settings for the application, if the settings do not exist, then
     * the specified class will be created to use instead
     * @param toAppSettingsClass the application settings class to use
     * @return the application settings class used by the application
     */
    IApplicationSettings getApplicationSettings(Class<? extends IApplicationSettings> toAppSettingsClass);

    /**
     * Gets the application controller for this application
     * @return the application controller
     */
    IApplicationController getApplicationController();

    /**
     * Gets the current workspace, if a workspace has not been set, then will
     * create and set a default
     * @return the current workspace, this method will not return null
     */
    Workspace getCurrentWorkspace();

    /**
     * Sets the workspace to the specified workspace, this will also store the
     * workspace to the application settings
     * @param toWorkspace the new workspace
     */
    void setCurrentWorkspace(Workspace toWorkspace);

    /**
     * Gets the command line arguments that were used to start the application
     * @return the command line arguments or null if there were none used
     */
    String[] getApplicationArguments();

    /**
     * Sets the workspace to the specified workspace, this will attempt
     * to load the workspace from the application settings.
     * @param toWorkspace the new workspace
     */
    void setCurrentWorkspace(String tcWorkspace);

    /**
     * Gets the GUID for the application
     * @return a string guid that identifies this version of the apllication
     */
    String getGUID();

    /**
     * Gets the scheduler for the application
     * @return the scheduler
     */
    IScheduler getScheduler();

    /**
     * Gets the message broker for the application
     * @return the message broker
     */
    IMessageBroker getMessageBroker();

    /**
     * Gets the name of the application
     * @return the name of the application
     */
    String getName();

    /**
     * Gets the Application Directory that should be used to write all the applications persistant data
     * @return the application directory
     */
    File getApplicationDirectory();

    /**
     * Sets a property for the application
     * Properties set in this way are not persistend when the application restarts
     * @param <T> The type of the property
     * @param tcProperty the property to set
     * @param toValue the value to set the property to
     * @return true if the application settings were changed because of this call
     */
    <T> boolean setProperty(String tcProperty, T toValue);

    /**
     * Clears a property from the application
     * @param tcProperty the property to clear
     * @return true if the property collection was changed due to this call
     */
    boolean clearProperty(String tcProperty);

    /**
     * Gets the value of a property
     * @param <T> The type of the property
     * @param tcProperty the property to get
     * @return the value of the property, or null if the property did not exist
     */
    <T> T getProperty(String tcProperty);

    /**
     * Gets the value of a property
     * @param <T> The type of the property
     * @param tcProperty the property to get
     * @param toDefault the default value of the property if one has not been specified
     * @return the value of the property
     */
    <T> T getProperty(String tcProperty, T toDefault);
    /**
     * Gets the handler for the license
     * @param tcUserGUID the User GUID for the license
     * @return the license handler
     */
     IPropertyHandler getLicenseHandler(String tcUserGUID);
     /**
      * Gets the value of a license
      * @param <T> The return type for the license component requested
      * @param tcLicenseComponent The name (path) of the component to return
      * @return The value of the license component requested
      */
     <T> T getLicense(String tcLicenseComponent);


    /**
     * Gets the property from the property handler
     * @param <T>
     * @param tcProperty the full name and path of the property
     * @param toDefault the default value if this property has not been specified already
     * @return the value of the property
     */
    <T> T getPropertyHandlerProperty(String tcProperty, T toDefault);

    /**
     * Gets the property from the property handler
     * @param <T>
     * @param tcProperty the full name and path of the property
     * @return the value of the property
     */
    <T> T getPropertyHandlerProperty(String tcProperty);
    
    /**
     * Sets the value of the specified property
     * @param <T>
     * @param tcProperty the full name and path of the property
     * @param toValue the value to set the property to
     */
    <T> void setPropertyHandlerProperty(String tcProperty, T toValue);
    
    /**
     * Gets the ObjectCache for the application
     * @return the Application level object cache
     */
    Goliath.ObjectCache getObjectCache();
            
    /**
     * Gets a reference to the session manager in use
     * @return a reference to the session manager
     */
    Goliath.Interfaces.ISessionManager getSessionManager();
    
    /**
     * Gets a reference to the security manager being used by the application
     * @return a reference to the security manager
     */
    Goliath.Interfaces.Security.ISecurityManager getSecurityManager();

    /**
     * Gets the directory that has been specified for the area passed in
     * @param tcDirectoryType the type of directory
     * @return the directory specified, a file separator character is appended to the directory if it is not already there
     */
    String getDirectory(String tcDirectoryType);

    /**
     * Sets the specified directory type to the directory passed in
     * @param tcDirectoryType the type of directory
     * @param tcDirectory the directory for the type
     */
    void setDirectory(String tcDirectoryType, String tcDirectory);
           

    /**
     * Sets the application settings handler
     * @param toAppSettings the app settings handler to use
     */
    void setApplicationSettings(Goliath.Interfaces.Applications.IApplicationSettings toAppSettings);
    /**
     * Adds a new log handler to the application
     * @param toLogHandler the log handler to add
     */
    void addLogHandler(Goliath.Interfaces.LogHandlers.ILogHandler toLogHandler);

    void removeLogHandler(Goliath.Interfaces.LogHandlers.ILogHandler toLogHandler);

    /**
     * Forces the application to shutdown quickly.  Does not allow clean up time.
     * @param tnExitCode The exit code to use when exiting the application
     */
    void forceShutdown(int tnExitCode);

    /**
     * Shuts down the application cleanly.  Runs cleanup code before exiting
     */
    void shutdown();
    
    /**
     * Starts the application
     * @param taArgs the arguments to pass to the application settings class
     */    
    void start(String[] taArgs);

    /**
     * Starts the application using the specified application settings class
     * @param taArgs the arguments for startup
     * @param toAppSettingsClass the app settings class to use
     */
    void start(String[] taArgs, Class<? extends IApplicationSettings> toAppSettingsClass);

    /**
     * Alerts the user with the specified message
     * @param tcMessage
     */
    void alert(String tcMessage);

    /**
     * Logs an event to the log handlers
     *
     * @param  tcMessage         The message to log
     */
    void log(String tcMessage);

    /**
     * Logs and event to the log handlers
     *
     * @param  tcMessage   The Message to log
     * @param  taArgs An array of objects with replacement parameters
     * @see Goliath.Utilities#replaceParameters
     */
    void log(String tcMessage, java.lang.Object... taArgs);

    /**
     * Logs a message to the log handlers
     *
     * @param  tcMessage    The message to log
     * @param  toLogType    The type of this message being logged
     * @param  taArgs An array of objects with replacement parameters
     * @see Goliath.Utilities#replaceParameters
     */
    void log(String tcMessage, Goliath.Constants.LogType toLogType, Object... taArgs);

    /**
     * Logs a message to the log handlers
     *
     * @param  tcMessage    The message to log
     * @param  toLogType   The type of this message being logged
     */
    void log(String tcMessage, Goliath.Constants.LogType toLogType);

    /**
     * Logs an error to the log handlers
     *
     * @param  toException the exception to be logged
     */
    void log(Throwable toException);

    /**
     * Logs an exception to the log handlers
     *
     * @param  toException the exception to log
     * @param  toLogType the type of message this is
     */
    void log(Throwable toException, Goliath.Constants.LogType toLogType);
    
    /**
     * Registers a command with the command manager.  
     * @param toCommand the command to register.
     */
    void registerCommand(Goliath.Interfaces.Commands.ICommand<?, ?> toCommand);
    
    /**
     * Gets the current state of the application
     * @return the application state
     */
    ApplicationState getState();
    
    /**
     * Gets the current classloader from the application
     * @return The application level class loader
     */
    Goliath.Interfaces.DynamicCode.IClassLoader getClassLoader();
    
    /**
     * Gets the current log level of the application.
     * Messages with a log level equal or less than than the log level will be logged
     * @return the LogType of the current log level
     */
    LogType getLogLevel();

    /**
     * Sets the current log level of the application
     * Only messages with a log level equal or less than than the log level will be logged
     * @param toLogLevel the log object with the log type
     */
    void setLogLevel(LogType toLogLevel);

    /**
     * Gets an existing session based on the session id
     *
     * @param  tcSessionID The session id of the session to get a reference to
     * @return  a session, or null if there were no sessions matching
     */
    Goliath.Interfaces.ISession getSession(String tcSessionID);

    /**
     * Checks if the session specified exists
     * @param tcSessionID the session to check for
     * @return true if the session exists, false otherwise
     */
    boolean hasSession(String tcSessionID);

    /**
     * Creates a new session with the specified session id
     * If the session already exists, then null is returned
     * @param tcSessionID the session id to use for the session
     * @return A new session, or null if a session with this name already exists
     */
    Goliath.Interfaces.ISession createSession(String tcSessionID);
    
    /**
     * Gets the class that started the application
     * @return the class with the main method
     */
    Class getStartupClass();
    
    java.lang.Object getRenderFactory();
    void setRenderFactory(java.lang.Object toRenderer);
    
    Goliath.Collections.List<IModule> getModules();
    
    void setFontMetric(java.lang.Object toMetric);
    
    java.lang.Object getFontMetric();

    void scheduleTask(TimerTask toTask, long tnDelay);

    void scheduleTask(TimerTask toTask, long tnDelay, long tnInterval);
    
    // TODO: Directory Control, through settings
    // TODO:  IO Control, through settings
    // TODO:  Log Handlers, through settings
    // TODO:  System Monitors, through settings
    // TODO:  Setting Handlers, through settings
    // TODO:  Session Handlers, through settings
}
