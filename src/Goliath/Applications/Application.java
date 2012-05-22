/* =========================================================
 * Application.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007, 1:34 PM
 * 
 * Description
 * --------------------------------------------------------
 * This is a singleton class meant to represent the actual
 * Application that is running.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.Applications;

import Goliath.Arguments.SingleParameterArguments;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Constants.LogType;
import Goliath.Constants.OperatingSystemName;
import Goliath.DynamicCode.Java;
import Goliath.Environment;
import Goliath.Exceptions.CriticalException;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.Exceptions.InvalidParameterException;
import Goliath.Exceptions.InvalidPathException;
import Goliath.Interfaces.Applications.IApplication;
import Goliath.Interfaces.Applications.IApplicationController;
import Goliath.Interfaces.Applications.IApplicationInstance;
import Goliath.Interfaces.Applications.IApplicationSettings;
import Goliath.Interfaces.Applications.IModule;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.Commands.ICommand;
import Goliath.Interfaces.DynamicCode.IClassLoader;
import Goliath.Interfaces.ISession;
import Goliath.Interfaces.IStartup;
import Goliath.Interfaces.LogHandlers.ILogHandler;
import Goliath.Interfaces.Messaging.IMessageBroker;
import Goliath.Interfaces.PropertyHandlers.IPropertyHandler;
import Goliath.Interfaces.Scheduling.IScheduler;
import Goliath.Interfaces.Security.ISecurityManager;
import Goliath.Session;
import Goliath.Threading.ThreadJob;
import Goliath.Watchers.ObjectWatcher;
import Goliath.Workspace;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

/**
 * This class defined the application that is currently running, there can be only one Application object
 * created.  The Application object should always be manipulated through it's interface rather than directly.
 * For example:
 * <pre>
 *      IApplication loApp = Goliath.Applications.Application.getInstance();
 *      loApp.doSomething();
 * </pre>
 *
 * @see         Goliath.Interfaces.IApplication
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
 **/
public final class Application extends Goliath.Object
        implements IApplication
{

    /**
     * There can be only one application class per application
     */
    private static IApplication g_oApplication = null;
    private String[] m_aArguments = null;
    private Class m_oStartupClass = null;
    private String m_cGUID = null;
    private Goliath.Interfaces.Applications.IApplicationSettings m_oAppSettings = null;
    private Goliath.Interfaces.ISessionManager m_oSessionManager = null;
    private Goliath.Interfaces.Security.ISecurityManager m_oSecurityManager = null;
    private Goliath.Interfaces.PropertyHandlers.IPropertyHandler m_oPropertyHandler = null;
    private Goliath.Collections.List<Goliath.Interfaces.LogHandlers.ILogHandler> m_oLogHandlers;
    private Goliath.Collections.List<IModule> m_oModules = new Goliath.Collections.List<IModule>();
    private Goliath.Collections.List<IStartup> m_oStartups = new Goliath.Collections.List<IStartup>();
    private Goliath.ObjectCache m_oObjectCache = new Goliath.ObjectCache();
    private Goliath.Applications.ApplicationState m_oState = Goliath.Applications.ApplicationState.STARTING();
    private Goliath.Interfaces.DynamicCode.IClassLoader m_oClassLoader = null;
    private IScheduler m_oScheduler;
    private IMessageBroker m_oMessageBroker;
    private LogType m_oLogLevel = null;
    private File m_oLockFile = null;
    private PropertySet m_oDirectories;
    private Workspace m_oCurrentWorkspace;
    private File m_oAppData;
    private IApplicationController m_oApplicationController;
    private Goliath.Collections.HashTable<ISession, IApplicationInstance> m_oApplicationInstances = new Goliath.Collections.HashTable<ISession, IApplicationInstance>();
    private java.lang.Object m_oFontMetrics = null;
    // Cache of Licenses, keyed by the session user GUID and contains each user License file property handler
    private Goliath.Collections.HashTable<String, Goliath.Interfaces.PropertyHandlers.IPropertyHandler> m_oLicenses = new Goliath.Collections.HashTable<String, Goliath.Interfaces.PropertyHandlers.IPropertyHandler>();
    private Timer m_oTimer;
    // TODO: allow different renderers based on session (web session, desktop session)
    private java.lang.Object m_oRenderer = null;
    boolean m_lLocked = false;  // True if a lock file has been created for this app
    private static boolean m_lInitialising;

    /**
     * Returns an Instance of the application
     * @return the application instance
     */
    public static IApplication getInstance()
    {
        if (g_oApplication == null)
        {
            if (!m_lInitialising)
            {
                m_lInitialising = true;

                // create the new application object here.
                g_oApplication = new Application();
            }
        }
        return g_oApplication;
    }

    /**
     * Gets the object cache for the application
     * @return the Object cache object for the application
     */
    @Override
    public Goliath.ObjectCache getObjectCache()
    {
        return m_oObjectCache;
    }

    @Override
    public void scheduleTask(TimerTask toTask, long tnDelay)
    {
        scheduleTask(toTask, tnDelay, -1);
    }

    @Override
    public void scheduleTask(TimerTask toTask, long tnDelay, long tnInterval)
    {
        final String lcThreadName = "Application Scheduler";
        // Ensure the timer is created, the timer will run on its own thread
        if (m_oTimer == null)
        {
            m_oTimer = new Timer("Application Scheduler");
            Session.markAsNotSession(lcThreadName);
        }
        
        // Wrap the executable TimerTask into a ThreadJob so it can be executed in it's own thread
        final ThreadJob<SingleParameterArguments<TimerTask>> loTJ = 
            new ThreadJob<SingleParameterArguments<TimerTask>>(new SingleParameterArguments<TimerTask>(toTask)){

                @Override
                protected void onRun(SingleParameterArguments<TimerTask> toCommandArgs)
                {
                    try
                    {
                        toCommandArgs.getParameter().run();
                    }
                    catch (Throwable ex)
                    {
                        log(ex);
                    }
                }
            };
        
        
        // Prepare a TimerTask to run the thread job
        TimerTask loTask = new TimerTask() {
            @Override
            public void run()
            {
                Goliath.Threading.Thread loThread = new Goliath.Threading.Thread(loTJ, lcThreadName);
                loThread.start();
            }
        };
        
        if (tnInterval > 0)
        {
            m_oTimer.schedule(loTask, tnDelay, tnInterval);
        }
        else
        {
            m_oTimer.schedule(loTask, tnDelay);
        }
    }

    @Override
    public final IApplicationController getApplicationController()
    {
        if (m_oApplicationController == null)
        {
            try
            {
                m_oApplicationController = getApplicationSettings().createApplicationController();
            }
            catch (Throwable ex)
            {
                log(ex);
            }
        }
        return m_oApplicationController;
    }

    @Override
    public final String getGUID()
    {
        if (m_cGUID == null)
        {
            m_cGUID = getPropertyHandlerProperty("Application.GUID", Goliath.Utilities.generateStringGUID());
        }
        return m_cGUID;
    }

    @Override
    public Workspace getCurrentWorkspace()
    {
        if (m_oCurrentWorkspace != null)
        {
            Workspace loWorkspace = m_oAppSettings.createDefaultWorkspace();
            if (loWorkspace == null || !loWorkspace.isValid())
            {
                throw new CriticalException("Default workspace is not valid");
            }
            setCurrentWorkspace(loWorkspace);
        }
        return m_oCurrentWorkspace;
    }

    @Override
    public void setCurrentWorkspace(Workspace toWorkspace)
    {
        if (!toWorkspace.isValid())
        {
            throw new InvalidParameterException("The workspace is invalid", "tcWorkspace");
        }
        // Get the list of workspaces
        List<Workspace> loWorkspaces = getPropertyHandlerProperty("Application.Workspaces", new List<Workspace>());
        if (!loWorkspaces.contains(toWorkspace))
        {
            loWorkspaces.add(toWorkspace);
            setPropertyHandlerProperty("Application.Workspaces", loWorkspaces);
        }
        m_oCurrentWorkspace = toWorkspace;
    }

    @Override
    public void setCurrentWorkspace(String tcWorkspace)
    {
        // Get the list of workspaces
        List<Workspace> loWorkspaces = getPropertyHandlerProperty("Application.Workspaces", new List<Workspace>());
        for (Workspace loWorkspace : loWorkspaces)
        {
            if (loWorkspace.getName().equalsIgnoreCase(tcWorkspace))
            {
                setCurrentWorkspace(loWorkspace);
                return;
            }
        }
        throw new InvalidParameterException("The workspace " + tcWorkspace + " was not found.", "tcWorkspace");
    }

    /**
     * gets the application settings that are currently in use
     *
     * @return  the application settings in use by the application
     */
    @Override
    public IApplicationSettings getApplicationSettings()
    {
        if (m_oAppSettings == null)
        {
            // We can not use the object cache here, becase the property handler has not been loaded yet.
            IList<Class<IApplicationSettings>> loAppSettings = getClasses(IApplicationSettings.class);
            if (loAppSettings.size() == 0)
            {
                StringBuilder loError = new StringBuilder("Could not find appsetting objects in classpath - (Application.getApplicationSettings())");
                for (String lcPath : Goliath.Utilities.getClassPaths())
                {
                    loError.append("\r\n\t");
                    loError.append(lcPath);
                }
                log(loError.toString(), LogType.FATAL());
            }
            else
            {
                return getApplicationSettings(loAppSettings.get(loAppSettings.size() - 1));
            }
        }
        return m_oAppSettings;
    }

    /**
     * gets the application settings that are currently in use
     *
     * @return  the application settings in use by the application
     */
    @Override
    public IApplicationSettings getApplicationSettings(Class<? extends IApplicationSettings> toClass)
    {
        try
        {
            // We will use the last loaded app setting class
            m_oAppSettings = toClass.newInstance();
            Application.getInstance().log("Using " + m_oAppSettings.getClass().getSimpleName() + " as application settings class", LogType.TRACE());
        }
        catch (Throwable e)
        {
            Throwable loEx = new Goliath.Exceptions.Exception(e);
        }

        if (m_oAppSettings == null)
        {
            throw new Goliath.Exceptions.CriticalException("Could not create application settings class - (Application.getApplicationSettings())");
        }

        return m_oAppSettings;
    }

    /**
     * Gets the command line arguments that were used to start the application
     * @return the command line arguments or null if there were none used
     */
    @Override
    public String[] getApplicationArguments()
    {
        return m_aArguments;
    }

    /**
     * Gets the list of classes specified, this is to be used as a helper function for loading classes
     * @param toClass
     * @return
     */
    private <T> IList<Class<T>> getClasses(Class<T> toClass)
    {
        if (m_oPropertyHandler == null)
        {
            return Java.getClasses(toClass, (Class[]) null);
        }
        else
        {
            // The property handler is loaded, so may as well use the object cache
            return getObjectCache().getClasses(toClass);
        }

    }

    /**
     * Gets the security manager for the application
     * @return the security manager
     */
    @Override
    public ISecurityManager getSecurityManager()
    {
        if (m_oSecurityManager == null)
        {
            m_oSecurityManager = getApplicationSettings().createSecurityManager();
        }
        return m_oSecurityManager;
    }

    /**
     * Load all of the Modules that are available
     */
    private void loadModules()
    {
        // Loop through all the startup classes and put them in order
        List<IStartup> loStartupObjects = getObjectCache().getObjects(Goliath.Interfaces.IStartup.class, "getName");
        for (IStartup loStartup : loStartupObjects)
        {
            if (IModule.class.isAssignableFrom(loStartup.getClass()))
            {
                if (!m_oModules.contains((IModule) loStartup))
                {
                    insertStartUp(m_oModules, (IModule) loStartup);
                    Application.getInstance().log("Loaded Module " + loStartup.getName(), Goliath.Constants.LogType.TRACE());
                }
                else
                {
                    Application.getInstance().log("Module already loaded with id " + ((IModule) loStartup).getID(), Goliath.Constants.LogType.WARNING());
                }
            }
            else
            {
                if (!m_oStartups.contains(loStartup))
                {
                    insertStartUp(m_oStartups, loStartup);
                    Application.getInstance().log("Loaded Startup " + loStartup.getName(), Goliath.Constants.LogType.TRACE());
                }
            }
        }

        // Clear the modules from the cache
        getObjectCache().clearCache(IStartup.class);
    }

    private <T extends IStartup> void insertStartUp(List<T> toItems, T toItem)
    {
        if (toItems.size() == 0)
        {
            toItems.add(toItem);
            return;
        }

        if (((IStartup) (toItems.get(0))).getSequence() >= toItem.getSequence())
        {
            toItems.add(0, toItem);
            return;
        }

        if (((IStartup) (toItems.get(toItems.size() - 1))).getSequence() <= toItem.getSequence())
        {
            toItems.add(toItem);
            return;
        }

        int lnCount = 0;
        for (IStartup loItem : toItems)
        {
            if (loItem.getSequence() >= toItem.getSequence())
            {
                toItems.add(lnCount, toItem);
                return;
            }
            lnCount++;
        }
    }

    @Override
    public File getApplicationDirectory()
    {
        if (m_oAppData == null)
        {
            String lcUserHome = System.getProperty("user.home", ".");
            OperatingSystemName loOS = Environment.OS();
            if (loOS.equals(OperatingSystemName.MAC()))
            {
                m_oAppData = new File(lcUserHome, "Library/Application Support/" + Application.getInstance().getName());
            }
            else if (loOS.equals(OperatingSystemName.WINDOWS()))
            {
                String lcAppData = System.getenv("APPDATA");
                if (Goliath.Utilities.isNullOrEmpty(lcAppData))
                {
                    m_oAppData = new File(lcUserHome, '.' + Application.getInstance().getName() + Environment.FILESEPARATOR());
                }
                else
                {
                    m_oAppData = new File(lcAppData, '.' + Application.getInstance().getName() + Environment.FILESEPARATOR());
                }
            }
            else if (loOS.equals(OperatingSystemName.LINUX()))
            {
                m_oAppData = new File(lcUserHome, '.' + Application.getInstance().getName() + Environment.FILESEPARATOR());
            }
            else if (loOS.equals(OperatingSystemName.SOLARIS()))
            {
                m_oAppData = new File(lcUserHome, '.' + Application.getInstance().getName() + Environment.FILESEPARATOR());
            }
            else
            {
                m_oAppData = new File(lcUserHome, '.' + Application.getInstance().getName() + Environment.FILESEPARATOR());
            }
            if (!m_oAppData.exists())
            {
                m_oAppData.mkdirs();
            }
        }
        return m_oAppData;
    }

    /**
     * Load all of the startup classes and execute them
     */
    private void executeStartups()
    {
        for (IStartup loStartup : m_oStartups)
        {
            if (!loStartup.run())
            {
                throw new CriticalException(loStartup.getName() + " Failed to execute correctly");
            }
        }
        // Clear the startups
        m_oStartups = null;
    }

    /**
     * Load all of the startup classes and execute them
     */
    private void executeModules()
    {
        for (IModule loModule : m_oModules)
        {
            loModule.run();
        }
    }

    /**
     * Gets a list of all the modules that are installed
     * @return the list of installed modules
     */
    @Override
    public Goliath.Collections.List<IModule> getModules()
    {
        return m_oModules;
    }

    /**
     * Unload the old application settings and load the new ones
     *
     * @param  toSettings   the application settings to load
     * @throws Goliath.Exceptions.InvalidParameterException if toSettings is null
     */
    @Override
    public void setApplicationSettings(Goliath.Interfaces.Applications.IApplicationSettings toSettings)
            throws Goliath.Exceptions.InvalidParameterException
    {
        // if the settings are the same then don't do anything
        if (m_oAppSettings != null && m_oAppSettings.equals(toSettings))
        {
            return;
        }

        synchronized (this)
        {
            // Check if there is already an application settings
            if (m_oAppSettings != null)
            {
                // need to make sure this unloads correctly first
                m_oAppSettings.deactivate();
            }

            m_oAppSettings = toSettings;
            m_oAppSettings.activate();
        }
    }

    /**
     * Adds a log handler to the application.  All the log
     * handlers that have been added will be called when
     * required to log a message
     * @param toLogHandler the log handler to add
     */
    @Override
    public void addLogHandler(ILogHandler toLogHandler)
    {
        getLogHandlers().add(toLogHandler);
    }

    @Override
    public void removeLogHandler(ILogHandler toLogHandler)
    {
        if (m_oLogHandlers != null)
        {
            m_oLogHandlers.remove(toLogHandler);
        }
    }

    // Gets the directories from the property handler
    private PropertySet getDirectories()
    {
        if (m_oDirectories == null)
        {
            m_oDirectories = getPropertyHandlerProperty("Application.Settings.Directories", new PropertySet());
        }
        return m_oDirectories;
    }

    /**
     * Gets the directory specified and stores an entry for this directory in the
     * Application Settings.  If the directory does not exist, it will be created
     * as a result of this call
     * @param tcDirectoryType The directory to get
     * @return the canonical string of the directory.
     */
    @Override
    public String getDirectory(String tcDirectoryType)
    {
        String lcDirectory = null;
        // If the directory is empty we are not going to look it up, we will create the root
        if (Goliath.Utilities.isNullOrEmpty(tcDirectoryType))
        {
            lcDirectory = (getApplicationSettings().useApplicationDataDirectory() ? getApplicationDirectory().getPath() : "." + Environment.FILESEPARATOR());
        }
        else
        {
            lcDirectory = getDirectories().getProperty(tcDirectoryType);
            
            if (Goliath.Utilities.isNullOrEmpty(lcDirectory))
            {
                lcDirectory = (getApplicationSettings().useApplicationDataDirectory() ? getApplicationDirectory().getPath() : ".") + Environment.FILESEPARATOR() + tcDirectoryType;
                setDirectory(tcDirectoryType, lcDirectory);
            }
        }
        
        // Make sure the directory ends with a File Separator Character and that we are always using a forward slash
        lcDirectory = lcDirectory.replaceAll("([^/|\\\\])$|\\\\", "$1" + "/");
        
        // Create the directory if it does not exist already
        File loFile = new File(lcDirectory);
        // Make sure the directory exists
        try
        {
            Goliath.IO.Utilities.File.create(loFile, true);
            
            // Make sure we have a reference to the correct file
            return lcDirectory;
        }
        catch (IOException ex)
        {
            throw new Goliath.Exceptions.InvalidIOException(ex);
        }
    }

    @Override
    public void setDirectory(String tcDirectoryType, String tcDirectory)
    {
        Goliath.Utilities.checkParameterNotNull("tcDirectoryType", tcDirectoryType);
        Goliath.Utilities.checkParameterNotNull("tcDirectory", tcDirectory);
        getDirectories().put(tcDirectoryType, tcDirectory);
        setPropertyHandlerProperty("Application.Settings.Directories", getDirectories());

    }

    @Override
    public <T> T getLicense(String tcPath)
    {

        String lcUserGUID = Session.getCurrentSession().getUserGUID();
        IPropertyHandler loLicenseHandler = null;
        if (!m_oLicenses.containsKey(lcUserGUID))
        {
            m_oLicenses.put(lcUserGUID, getLicenseHandler(lcUserGUID));
        }
        try
        {
            return m_oLicenses.get(lcUserGUID).<T>getProperty(tcPath);
        }
        catch (Throwable ex)
        {
            return null;
        }
    }

    @Override
    public IPropertyHandler getLicenseHandler(String tcUserGUID)
    {
        try
        {
            return getApplicationSettings().createLicenseHandler(tcUserGUID);
        }
        catch (Throwable ex)
        {
            // What to do if the file is not found
            return null;
        }
    }

    @Override
    public <T> T getPropertyHandlerProperty(String tcPath, T toDefault)
    {
        try
        {
            Application.getInstance().log("Getting property value for " + tcPath, LogType.DEBUG());
            return getPropertyHandler().getProperty(tcPath, toDefault);
        }
        catch (Throwable ex)
        {
            return toDefault;
        }
    }

    @Override
    public <T> T getPropertyHandlerProperty(String tcPath)
    {
        try
        {
            return getPropertyHandler().<T>getProperty(tcPath);
        }
        catch (Throwable ex)
        {
            return null;
        }
    }

    @Override
    public <T> void setPropertyHandlerProperty(String tcPath, T toValue)
    {
        try
        {
            getPropertyHandler().setProperty(tcPath, toValue);
        }
        catch (Throwable ex)
        {
            // Someone tried to set a property before the handler exists
            log(ex);
        }
    }

    /**
     * Gets the property handler that is to be used by the application
     * if a property handler does not exist, then one is created
     * By default a file property handler will be used and will point to a
     * file called ApplicationSettings.xml.  It will use the application name
     * as the root node.
     * @return the property handler for the application
     */
    private IPropertyHandler getPropertyHandler()
    {
        if (m_oPropertyHandler == null)
        {
            m_oPropertyHandler = getApplicationSettings().createPropertyHandler();
        }
        return m_oPropertyHandler;
    }

    /**
     * Gets the log handlers registered to this application
     * @return
     */
    private List<ILogHandler> getLogHandlers()
    {
        if (m_oLogHandlers == null)
        {
            m_oLogHandlers = new List<ILogHandler>();
        }
        if (m_oLogHandlers.size() == 0)
        {
            m_oLogHandlers.add(getApplicationSettings().createLogHandler());
        }
        return m_oLogHandlers;
    }

    /**
     * gets the session manager that is currently in use
     *
     * @return  the session manager in use by the application
     */
    @Override
    public Goliath.Interfaces.ISessionManager getSessionManager()
    {
        // TODO: We want to check what's happening if this is reach before the application has been initialized
        if (m_oSessionManager == null)
        {
            m_oSessionManager = getApplicationSettings().createSessionManager();
        }
        return m_oSessionManager;
    }

    /**
     * Creates an instance of the Application class
     * If the application has been created before, this will throw and
     * error.
     * 
     * This method is public to allow the application class to be 
     * created in standard .jsp applications
     */
    public Application()
    {
        if (g_oApplication == null)
        {
            g_oApplication = this;

            Runtime.getRuntime().addShutdownHook(new Thread()
            {

                @Override
                public void run()
                {
                    if (g_oApplication != null && g_oApplication.getState() != ApplicationState.EXITING() && g_oApplication.getState() != ApplicationState.UNLOADING())
                    {
                        g_oApplication.getState().shutdown();

                        // Now just wait for the application to unload
                        while (!g_oApplication.getState().equals(ApplicationState.EXITING()))
                        {
                            try
                            {
                                Thread.sleep(100);
                            }
                            catch (Throwable e)
                            {
                            }
                        }
                        try
                        {
                            Thread.sleep(100);
                        }
                        catch (Throwable e)
                        {
                        }
                    }
                }
            });
        }
        else
        {
            throw new Goliath.Exceptions.CriticalException("Application object already exists");
        }
    }

    @Override
    public IClassLoader getClassLoader()
    {
        if (m_oClassLoader == null)
        {
            m_oClassLoader = new Goliath.DynamicCode.DynamicClassLoader();

            // Add all of the classpaths to the class loader
            for (String lcPath : Goliath.Utilities.getClassPaths())
            {
                try
                {
                    m_oClassLoader.addPath(lcPath);
                }
                catch (Goliath.Exceptions.InvalidPathException ex)
                {
                    // Nothing to do here because all Goliath exceptions automatically log themselves
                }
            }
        }
        return m_oClassLoader;
    }

    /**
     * Creates a new session with the specified session id
     * If the session already exists, then null is returned
     * @param tcSessionID the session id to use for the session
     * @return A new session, or null if a session with this name already exists
     */
    @Override
    public Goliath.Interfaces.ISession createSession(String tcSessionID)
    {
        return getSessionManager().createSession(tcSessionID);
    }

    /**
     * Creates a new session
     *
     * @return  a new session
     */
    public Goliath.Interfaces.ISession createSession()
    {
        return getSessionManager().createSession();
    }

    /**
     * Gets an existing session based on the session id
     *
     * @param  tcSessionID The session id of the session to get a reference to
     * @return  a session, or null if there were no sessions matching
     */
    @Override
    public Goliath.Interfaces.ISession getSession(String tcSessionID)
    {
        return getSessionManager().getSession(tcSessionID);
    }

    /**
     * Checks if the session specified exists
     * @param tcSessionID the session to check for
     * @return true if the session exists, false otherwise
     */
    @Override
    public boolean hasSession(String tcSessionID)
    {
        return getSessionManager().hasSession(tcSessionID);
    }

    /**
     * Resets an existing session based on the session id
     *
     * @param  tcSessionID The session id of the session to get a reference to
     */
    public void clearSession(String tcSessionID)
    {
        getSessionManager().clearSession(tcSessionID);
    }

    /**
     * Registers a command with the application and command manager for the commands session,
     * or for the current session if the command does not have a session
     * 
     * @param  toCommand    the command to register
     */
    @Override
    public void registerCommand(ICommand<?, ?> toCommand)
    {
        getState().registerCommand(toCommand);
    }

    /**
     * Alerts the user with the specified message
     * @param tcMessage
     */
    @Override
    public void alert(String tcMessage)
    {
        getApplicationSettings().alert(tcMessage, getName());
    }

    /**
     * Logs an event to the log handlers
     *
     * @param  tcMessage         The message to log
     */
    @Override
    public void log(String tcMessage)
    {
        // TODO: Update log to use the Message broker
        log(tcMessage, Goliath.Constants.LogType.EVENT());
    }

    /**
     * Logs and event to the log handlers
     *
     * @param  tcMessage   The Message to log
     * @param  taArgs An array of objects with replacement parameters
     * @see Goliath.Utilities#replaceParameters
     */
    @Override
    public void log(String tcMessage, java.lang.Object... taArgs)
    {
        log(Goliath.IO.Utilities.replaceParameters(tcMessage, taArgs));
    }

    /**
     * Gets the class that was used to start the application
     * @return the class that contains the main method
     */
    @Override
    public Class getStartupClass()
    {
        return m_oStartupClass;
    }

    /**
     * Logs a message to the log handlers
     *
     * @param  tcMessage    The message to log
     * @param  toLogType    The type of this message being logged
     * @param  taArgs An array of objects with replacement parameters
     * @see Goliath.Utilities#replaceParameters
     */
    @Override
    public void log(String tcMessage, Goliath.Constants.LogType toLogType, java.lang.Object... taArgs)
    {
        log(Goliath.IO.Utilities.replaceParameters(tcMessage, taArgs), toLogType);
    }

    /**
     * Logs a message to the log handlers
     *
     * @param  tcMessage    The message to log
     * @param  toLogType   The type of this message being logged
     */
    @Override
    public final void log(String tcMessage, Goliath.Constants.LogType toLogType)
    {
        if (m_oPropertyHandler != null && m_oLogHandlers != null && m_oLogHandlers.size() > 0)
        {
            // Prevent a concurrent modification exception by creating a new list, important here  as many threads hit this
            List<ILogHandler> loHandlers = new List<ILogHandler>(m_oLogHandlers);
            for (ILogHandler loHandler : loHandlers)
            {
                loHandler.log(tcMessage, toLogType);
            }
        }
        else
        {
            if (toLogType == Goliath.Constants.LogType.ERROR())
            {
                System.err.println(tcMessage);
            }
            else
            {
                System.out.println(tcMessage);
            }
        }
    }

    /**
     * Logs an error to the log handlers
     *
     * @param  toException the exception to be logged
     */
    @Override
    public void log(Throwable toException)
    {
        log(toException, Goliath.Constants.LogType.ERROR());
    }

    /**
     * Logs an exception to the log handlers
     *
     * @param  toException the exception to log
     * @param  toLogType the type of message this is
     */
    @Override
    public void log(Throwable toException, Goliath.Constants.LogType toLogType)
    {
        StringBuilder loString = new StringBuilder();
        loString.append(Goliath.Utilities.isNull(toException.toString(), toException.getClass().getName()));
        loString.append(Environment.NEWLINE());
        loString.append("-------------------------------------------------");
        loString.append(Environment.NEWLINE());
        for (StackTraceElement loElement : toException.getStackTrace())
        {
            loString.append("     ");
            loString.append(loElement.toString());
            loString.append(Environment.NEWLINE());
        }
        loString.append("-------------------------------------------------");
        loString.append(Environment.NEWLINE());
        log(loString.toString(), toLogType);
    }

    /**
     * Gets the current state of the application
     * @return the current application state
     */
    @Override
    public ApplicationState getState()
    {
        return m_oState;
    }

    /**
     * Sets the state of the application
     * This has no accessability modified because the
     * default is package, and we only want classes
     * within this package to modify state
     * @param toState the new state of the application
     */
    public void setState(ApplicationState toState)
    {
        m_oState = toState;
    }

    /**
     * Attempts to shut down the application
     */
    @Override
    public void shutdown()
    {
        if (getState().shutdown())
        {
            if (m_oAppSettings != null)
            {
                m_oAppSettings.deactivate();
            }
            getState().shutdown();
        }
    }

    /**
     * Attempts to start the application
     * @param taArgs the arguments for the application
     * @param toStartupClass the startup class that has the main function
     */
    @Override
    public void start(String[] taArgs)
    {
        start(taArgs, getApplicationSettings().getClass());
    }
    
    private void updateClassPath(File toClassDirectory)
    {
        if (toClassDirectory.exists())
        {
            if (toClassDirectory.isDirectory())
            {
                File[] laJars = toClassDirectory.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File toFile)
                    {
                        return toFile.isDirectory() || toFile.getName().matches("(?i).+\\.jar$");
                    }
                });
                
                for (File loFile : laJars)
                {
                    updateClassPath(loFile);
                }
            }
            else
            {
                log("Adding path: " + toClassDirectory.getAbsolutePath());
                try
                {
                    getClassLoader().addFile(toClassDirectory);
                }
                catch (InvalidPathException ex)
                {
                    // This should never be able to happen as we are loading from a list of files
                }
            }
        }
    }

    /**
     * Attempts to start the application
     * @param taArgs the arguments for the application
     * @param toStartupClass the startup class that has the main function
     */
    @Override
    public void start(String[] taArgs, Class<? extends IApplicationSettings> toAppSettingsClass)
    {
        if (getState().start())
        {
            // Make sure all of the .jar files in the lib directory are loadable
            updateClassPath(new File("./lib"));
            
            // TODO: Optimise this method
            // Store the arguments
            m_aArguments = taArgs;

            // This is so that the application will always know the class that contains the main
            // It us used to help get classpaths
            try
            {
                m_oStartupClass = Class.forName(Goliath.DynamicCode.Java.getCallingClassName(2));
            }
            catch (Throwable ex)
            {
                throw new CriticalException("Unable to determine the calling class");
            }

            // Get the application settings class
            getApplicationSettings(toAppSettingsClass);

            // Create the splash screen if needed
            m_oAppSettings.showSplash();

            // Force the creation of an application identifier
            this.getGUID();

            // Get the System Session and update the expiry so the system session will not expire
            ISession loSession = Session.getCurrentSession();
            loSession.setExpiryLength(Long.MAX_VALUE / 2);  // We divide by two here as otherwise we would end up with an overflow.
            loSession.pause();

            // if no application settings class could be loaded we would not get this far.
            // The application would critically shut down.

            // Create the lock file if this is a single instance
            if (m_oAppSettings.isSingleInstance())
            {
                try
                {
                    m_oLockFile = new File(getDirectory("") + getName().replaceAll(" ", "") + ".lck");
                    if (!m_oLockFile.createNewFile())
                    {
                        // TODO: We should be using goliath confirmation controls here
                        // The lock file already exists, so now we need to prompt the user
                        int lnResult = JOptionPane.showConfirmDialog(null,
                                                                     "It appears the application is already running, if you are positive the application is not running click yes to continue, running this application in multiple instances may cause data corruption.  Do you wish to continue?",
                                                                     "Application Lock File Found",
                                                                     JOptionPane.YES_NO_OPTION);
                        if (lnResult == JOptionPane.YES_OPTION)
                        {
                            m_lLocked = true;
                        }
                        else
                        {
                            throw new Goliath.Exceptions.CriticalException("Application is already running so this invocation has been shutdown");
                        }
                    }
                    else
                    {
                        m_lLocked = true;
                    }

                    if (m_lLocked)
                    {
                        /* Create an object watcher to watch the app lock file, if the file is deleted, close the application
                         * There is no need to store a reference to the watcher as it is needed for the lifetime of the application
                         */
                        new ObjectWatcher<File>(m_oLockFile)
                        {

                            @Override
                            public boolean watcherCondition()
                            {
                                return !getObject().exists();
                            }

                            @Override
                            public void watcherAction()
                            {
                                shutdown();
                            }
                        };
                    }
                }
                catch (IOException e)
                {
                    throw new Goliath.Exceptions.CriticalException("Attempt to create lock file failed");
                }

            }

            // After the application settings have been loaded, it is safe to run startup routines
            m_oAppSettings.doSetup();
            
            loSession.resume();

            loadModules();

            executeStartups();
            executeModules();

            // Create the Scheduler
            getScheduler();

            // Create the message broker
            getMessageBroker();

            // Do a garbage collection here as a lot will have been done before this point
            System.gc();

            m_oAppSettings.runInternalTests();

            // set the state to running
            if (m_oState.start())
            {
                m_oAppSettings.hideSplash();
                // Holding loop until the application is actually closing
                while (!this.getState().equals(ApplicationState.EXITING()))
                {
                    try
                    {
                        // If the application is unloading, then start the unloading process
                        if (this.getState().equals(ApplicationState.UNLOADING()))
                        {
                            unload();
                            this.getState().shutdown();
                        }
                        Thread.sleep(100);
                    }
                    catch (Throwable e)
                    {
                    }
                }
            }
        }
        shutdown(0);
    }

    /**
     * Unload the application
     */
    protected void unload()
    {
        // TODO: Implement this fully so that App settings and modules also have a chance to unload
    }

    @Override
    public IScheduler getScheduler()
    {
        if (m_oScheduler == null)
        {
            m_oScheduler = m_oAppSettings.createScheduler();
        }
        return m_oScheduler;
    }

    @Override
    public IMessageBroker getMessageBroker()
    {
        if (m_oMessageBroker == null)
        {
            m_oMessageBroker = m_oAppSettings.createMessageBroker();
        }
        return m_oMessageBroker;
    }

    /**
     * Forces a shutdown of the system, does not clean up.
     * @param tnExitCode the exit code to exit with
     */
    @Override
    public void forceShutdown(int tnExitCode)
    {
        log("Forcing Shutdown");
        shutdown(tnExitCode);

    }

    private void shutdown(int tnExitCode)
    {
        g_oApplication.getState().shutdown();
        // Delete the lock file if it exists
        if (m_oLockFile != null && m_oLockFile.exists() && m_lLocked)
        {
            m_oLockFile.delete();
        }
        log("Closing application with exit code " + Integer.toString(tnExitCode), LogType.TRACE());
        System.exit(tnExitCode);
    }

    /**
     * Gets the current log level of the application.
     * Messages with a log level equal or less than than the log level will be logged
     * @return the LogType of the current log level
     */
    @Override
    public LogType getLogLevel()
    {
        if (m_oLogLevel == null)
        {
            m_oLogLevel = LogType.EVENT();
        }
        return m_oLogLevel;
    }

    /**
     * Sets the current log level of the application
     * Only messages with a log level equal or less than than the log level will be logged
     * @param toLogLevel the log object with the log type
     */
    @Override
    public void setLogLevel(LogType toLogLevel)
    {
        m_oLogLevel = toLogLevel;
    }

    /**
     * Gets the application Name
     * @return the name of the application
     */
    @Override
    public String getName()
    {
        return m_oAppSettings.getName();
    }

    public boolean getRequiresLogin()
    {
        return false;
    }

    /**
     * Sets a property for the application
     * Properties set through this method are not persisted when the application
     * restarts
     * @param <T> The type of the property
     * @param tcProperty the property to set
     * @param toValue the value to set the property to
     * @return true if the application settings were changed because of this call
     */
    @Override
    public <T> boolean setProperty(String tcProperty, T toValue)
    {
        return getApplicationSettings().setProperty(tcProperty, toValue);
    }

    /**
     * Gets the value of a property
     * @param tcProperty the property to get
     * @return the value of the property
     */
    @Override
    public boolean clearProperty(String tcProperty)
    {
        return getApplicationSettings().clearProperty(tcProperty);
    }

    /**
     * Gets the value of a property
     * @param <T> The type of the property
     * @param tcProperty the property to get
     * @return the value of the property
     */
    @Override
    public <T> T getProperty(String tcProperty)
    {
        return getApplicationSettings().<T>getProperty(tcProperty);
    }

    @Override
    public <T> T getProperty(String tcName, T toDefault)
    {
        return getApplicationSettings().<T>getProperty(tcName, toDefault);
    }

    /**
     * Gets the current renderer being used by the application
     * @return the renderer object that is being used by the application
     */
    @Override
    public java.lang.Object getRenderFactory()
    {
        return m_oRenderer;
    }

    /**
     * Sets the renderer that is to be used by the application
     * @param toRenderer the renderer to use
     */
    @Override
    public void setRenderFactory(java.lang.Object toRenderer)
    {
        m_oRenderer = toRenderer;
    }

    @Override
    public void setFontMetric(java.lang.Object toMetric)
    {
        m_oFontMetrics = toMetric;
    }

    @Override
    public java.lang.Object getFontMetric()
    {
        return m_oFontMetrics;
    }
}
