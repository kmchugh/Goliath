/* =========================================================
 * ApplicationSettings.java
 *
 * Author:      kmchugh
 * Created:     26 November 2007, 15:22
 *
 * Description
 * --------------------------------------------------------
 * The application settings class controls the actual application
 * behaviours
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/
package Goliath.Applications;

import Goliath.Collections.List;
import Goliath.Constants.LogType;
import Goliath.Date;
import Goliath.Interfaces.Applications.IApplicationController;
import Goliath.Interfaces.ISessionManager;
import Goliath.Interfaces.LogHandlers.ILogHandler;
import Goliath.Interfaces.Messaging.IMessageBroker;
import Goliath.Interfaces.PropertyHandlers.IPropertyHandler;
import Goliath.Interfaces.Scheduling.IScheduler;
import Goliath.Interfaces.Security.ISecurityManager;
import Goliath.Messaging.MessageBroker;
import Goliath.Scheduling.Scheduler;
import Goliath.SessionManager;
import Goliath.Workspace;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import Goliath.Test;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

/**
 * ApplicationSettings class is used to control the behaviours
 * of the application
 *
 * @version     1.0 26 November 2007
 * @author      kmchugh
 **/
public abstract class ApplicationSettings<A extends IApplicationController> extends Goliath.Object
        implements Goliath.Interfaces.Applications.IApplicationSettings
{

    private boolean m_lIsActive = false;
    private Object m_oSplashScreen;
    private ILogHandler m_oSplashLogHandler;
    
    Goliath.Collections.PropertySet m_oProperties = new Goliath.Collections.PropertySet();

    /** Creates a new instance of ApplicationSettings */
    public ApplicationSettings()
    {
    }

    @Override
    public final A createApplicationController()
    {
        return onCreateApplicationController();
    }

    protected abstract A onCreateApplicationController();

    @Override
    public void applicationStateChanged(ApplicationState toNewState)
    {
        
    }
    
    

    @Override
    public final void runInternalTests()
    {
        // TODO: Refactor into external class
        boolean llTest = Application.getInstance().getPropertyHandlerProperty("Application.runInternalTests", false);
        if (llTest)
        {
            // Run any tests if required
            // Create HTML file for result.
            File loHTMLTestLog = new File(("./TestSuite_"+ new Date().getDate()+".html").replace(" ", "_"));
            try
            {
                if (loHTMLTestLog.createNewFile())
                {
                    FileOutputStream loFileStream;
                    DataOutputStream loDataOutputStream;

                    int lnPassed = 0;
                    int lnFailed = 0;


                    StringBuilder loBuilder = new StringBuilder();

                    loBuilder.append("<html> <head>  <title> TestSuite Window </title>  </head>   <body>  ");
                    loBuilder.append("<table border=\"1\"> <tr> <th>Test class Name</th> <th>Start Time</th> <th>End Time</th> <th>Result</th> <th>Error Message</th></tr>");

                    List<Class<Test>> loTests = Application.getInstance().getObjectCache().getClasses(Test.class);
                    for (Class<Test> loTestClass : loTests)
                    {
                        try
                        {
                            Test loClass = loTestClass.newInstance();

                            loClass.execute();

                            String loResult = loClass.getTestResult() ;

                            if (loResult.equals("Passed"))
                            {
                                loBuilder.append("<tr style=\"color:#009933\">");
                                lnPassed++;
                            }
                            else
                            {
                                loBuilder.append("<tr style=\"color:#FF6633\">");
                                lnFailed++;
                            }

                            Goliath.Utilities.appendToStringBuilder(loBuilder,
                            "<td>",loClass.getName(),"</td>",
                            "<td>", Goliath.Utilities.Date.toString(new Date(loClass.getStartTime())), "</td>",
                            "<td>",Goliath.Utilities.Date.toString(new Date(loClass.getEndTime())),"</td>",
                            "<td>", loResult ,"</td>",
                            "<td width=\"50%\">",loClass.getErrorMessage(),"</td>");


                        }
                        catch (Throwable ex)
                        {
                            Application.getInstance().log(ex);
                        }
                        loBuilder.append("</tr>");
                    }
                    int lnPercentPassed = 0;
                    if(lnPassed!=0 || lnFailed!=0)
                    {
                        lnPercentPassed = (lnPassed * 100 / ( lnPassed + lnFailed));
                    }

                    loBuilder.append("</table><p/>");

                    loBuilder.append("<table border=1 width=100%> <tr>");

                    if(lnPercentPassed > 0)
                    {
                        Goliath.Utilities.appendToStringBuilder(loBuilder,
                                "<td style=\"background-color:#009933 ; color:white ; width:",
                                Integer.toString(lnPercentPassed),
                                "%\">Passed:",
                                Integer.toString(lnPercentPassed),
                                "%</td>");
                    }


                    if((100 - lnPercentPassed) > 0)
                    {
                        Goliath.Utilities.appendToStringBuilder(loBuilder,
                                "<td style=\"background-color:#FF6633\">Failed:",
                                Integer.toString((100 - lnPercentPassed)),
                                "%</td>");
                    }


                    loBuilder.append("</tr> </table>");
                    loBuilder.append(" </body> </html>");

                    loFileStream = new FileOutputStream(loHTMLTestLog);
                    loDataOutputStream = new DataOutputStream(loFileStream);
                    loDataOutputStream.writeChars(loBuilder.toString());

                }
            }
            catch (IOException ex)
            {
                Application.getInstance().log(ex);
            }
        }
    }

    @Override
    public void doSetup()
    {
        onDoSetup();
    }

    protected abstract void onDoSetup();

    @Override
    public boolean useApplicationDataDirectory()
    {
        return true;
    }
            

    @Override
    public final String getName()
    {
        return onGetName();
    }

    @Override
    public Workspace createDefaultWorkspace()
    {
        Workspace loWorkspace = new Workspace();
        loWorkspace.setName("default");
        String lcDir = Application.getInstance().getDirectory("Workspaces");
        if (new File(lcDir) == new File("./Workspaces"))
        {
            lcDir = new JFileChooser().getFileSystemView().getDefaultDirectory().getAbsolutePath();
            if (allowWorkspaceSelect())
            {
                JFileChooser loChooser = new JFileChooser(new File(lcDir))
                {

                    @Override
                    public void approveSelection()
                    {
                        if (getSelectedFile().isDirectory())
                        {
                            super.approveSelection();
                        }

                    }
                };
                loChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                loChooser.setDialogTitle("Choose a directory for workspaces:");
                if (loChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    File loFile = loChooser.getSelectedFile();
                    lcDir = loFile.getAbsolutePath();
                }
            }
        }
        loWorkspace.setBaseDirectory(lcDir);
        return loWorkspace;
    }

    protected abstract boolean allowWorkspaceSelect();

    protected abstract String onGetName();

    @Override
    public final IMessageBroker createMessageBroker()
    {
        IMessageBroker loReturn = onCreateMessageBroker();
        if (loReturn == null)
        {
            loReturn = new MessageBroker();
        }
        Application.getInstance().log("Created Message Broker [" + loReturn.getClass().getName() + "]", LogType.EVENT());
        return loReturn;
    }

    protected IMessageBroker onCreateMessageBroker()
    {
        return null;
    }

    @Override
    public final IScheduler createScheduler()
    {
        IScheduler loReturn = onCreateScheduler();
        if (loReturn == null)
        {
            //loReturn = new Scheduler();
            loReturn = Scheduler.getInstance();
            loReturn.start();
        }
        Application.getInstance().log("Created Scheduler [" + loReturn.getClass().getName() + "]", LogType.EVENT());
        return loReturn;
    }

    protected IScheduler onCreateScheduler()
    {
        return null;
    }
    /*
     * Create a license handler for a given user GUILD
     */
    @Override
    public final IPropertyHandler createLicenseHandler(String tcUserGUID)
    {
        IPropertyHandler loReturn = onCreateLicenseHandler(tcUserGUID);
        if (loReturn == null)
        {
            // see if the license file exists, if not, then get the application controller to create one
            File loLicenseFile = new File(Application.getInstance().getDirectory("") + tcUserGUID + ".license");
            if (!loLicenseFile.exists())
            {
                Application.getInstance().getApplicationController().getLicenceFromServer();
            }

            loReturn = new Goliath.PropertyHandlers.FilePropertyHandler(Application.getInstance().getDirectory("") + tcUserGUID + ".license", "License", ".");
        }
       // Keeping the log in case we want to log all the user license "property" creations.
        Application.getInstance().log("Created License Handler [" + tcUserGUID + "]", LogType.EVENT());
        return loReturn;
    }


    @Override
    public final IPropertyHandler createPropertyHandler()
    {
        IPropertyHandler loReturn = onCreatePropertyHandler();
        if (loReturn == null)
        {
            loReturn = new Goliath.PropertyHandlers.FilePropertyHandler(Application.getInstance().getDirectory("") + "ApplicationSettings.settings", Goliath.XML.Utilities.makeSafeForXML(Application.getInstance().getName()).replaceAll(" ", ""), ".");
        }
        Application.getInstance().log("Created Property Handler [" + loReturn.getClass().getName() + "]", LogType.EVENT());
        return loReturn;
    }

    @Override
    public final ISecurityManager createSecurityManager()
    {
        ISecurityManager loReturn = onCreateSecurityManager();
        if (loReturn == null)
        {
            // TODO: need to create a file based security manager here for the default.
            try
            {
                loReturn = (ISecurityManager) Class.forName("Goliath.Security.DataSecurityManager").newInstance();
            } catch (Throwable ex)
            {
                Application.getInstance().log(ex);

            }
        }
        Application.getInstance().log("Created Security Manager [" + loReturn.getClass().getName() + "]", LogType.EVENT());
        return loReturn;
    }

    protected ISecurityManager onCreateSecurityManager()
    {
        return null;
    }

    @Override
    public final ISessionManager createSessionManager()
    {
        ISessionManager loReturn = onCreateSessionManager();
        if (loReturn == null)
        {
            loReturn = new SessionManager();

        }
        Application.getInstance().log("Created Session Manager [" + loReturn.getClass().getName() + "]", LogType.EVENT());
        return loReturn;
    }

    protected ISessionManager onCreateSessionManager()
    {
        return null;
    }


    protected IPropertyHandler onCreateLicenseHandler(String tcGUID)
    {
        return null;
    }

    protected IPropertyHandler onCreatePropertyHandler()
    {
        return null;
    }

    @Override
    public final ILogHandler createLogHandler()
    {
        ILogHandler loReturn = onCreateLogHandler();
        if (loReturn == null)
        {
            loReturn = new Goliath.LogHandlers.FileLogHandler(true, Application.getInstance().getDirectory("") + "ApplicationLog.log");
        }

        // Set the log level
        LogType loLogLevel = Application.getInstance().getPropertyHandlerProperty("Application.Logging.LogLevel", LogType.DEBUG());
        Application.getInstance().setLogLevel(loLogLevel);

        Application.getInstance().log("Created Log Handler [" + loReturn.getClass().getName() + "]", LogType.EVENT());
        return loReturn;
    }

    protected ILogHandler onCreateLogHandler()
    {
        return null;
    }

    /**
     * Sets an application property
     * @param <T> The type of property
     * @param tcProperty the name of the property to set
     * @param toValue the value of the property
     * @return true if the property collection was changed due to this call
     */
    @Override
    public <T> boolean setProperty(String tcProperty, T toValue)
    {
        return m_oProperties.setProperty(tcProperty, toValue);
    }

    /**
     * Gets the value of an application property
     * This will return null if the property does not exist
     * @param <T> The type of the property
     * @param tcProperty the property to get the value for
     * @return the value of the property
     */
    @Override
    public <T> T getProperty(String tcProperty)
    {
        return (T) m_oProperties.getProperty(tcProperty);
    }

    /**
     * Gets the value of an application property,
     * If this property does not exist the the value of toProperty will be returned
     * and the property will be set to the value of toProperty
     * @param <T> The type of the property
     * @param toProperty the property to get the value for
     * @return the value of the property
     */
    @Override
    public <T> T getProperty(String tcProperty, T toDefault)
    {
        T loValue = (T)m_oProperties.getProperty(tcProperty);
        if (loValue == null)
        {
            setProperty(tcProperty, toDefault);
            loValue = toDefault;
        }
        return (T) loValue;
    }

    /**
     * Clears the value of an application property,
     * @param tcName the name of the property to clear
     * @return true if a change was made to the property collection because of this call
     */
    @Override
    public boolean clearProperty(String tcName)
    {
        return m_oProperties.clearProperty(tcName);
    }

    /**
     * Checks if the settings are currently active
     *
     * @return  true if they are active
     */
    @Override
    public boolean isActive()
    {
        return m_lIsActive;
    }

    /**
     * Deactivates the settings, for custom behaviour override onDeactivate
     *
     * @see ApplicationSettings#onDeactivate
     */
    @Override
    public final void deactivate()
    {
        try
        {
            onDeactivate();
            m_lIsActive = false;
        } catch (Exception e)
        {
            // Something bad happened and we are unable to deactivate the settings
            Application.getInstance().log(e);
        }
    }

    /**
     * Activates the settings, for custom behaviour override onActivate
     *
     * @see ApplicationSettings#onDeactivate
     */
    @Override
    public final void activate()
    {
        try
        {
            onActivate();
            m_lIsActive = true;
        } catch (Exception e)
        {
            // Something bad happened and we are unable to activate the settings
            Application.getInstance().log(e);
        }
    }

    /**
     * This is the customizable method for activating the settings
     */
    protected void onActivate()
    {
    }

    /**
     * This is the customizable method for deactivating the settings
     */
    protected void onDeactivate()
    {
    }

    /**
     * gets the isSingleInstance field
     * @return - SingleInstance flag
     */
    @Override
    public boolean isSingleInstance()
    {
        return Application.getInstance().getPropertyHandlerProperty("Application.isSingleInstance", "true").equalsIgnoreCase("true");
    }

    /**
     * Shows the splash screen, if the splash screen does not exist, then it will
     * be created with this method.  This method also creates the Log handler for the
     * splash screen
     */
    @Override
    public final void showSplash()
    {
        onBeforeShowSplash();
        Object loSplash = getSplash();
        if(loSplash != null)
        {
            showSplash(loSplash);

            ILogHandler loLogHandler = getSplashScreenLogHandler();
            if (loLogHandler != null)
            {
                Application.getInstance().addLogHandler(loLogHandler);
            }
        }
        onAfterShowSplash();
    }
    
    protected void onBeforeShowSplash()
    {
        
    }
    
    protected void onAfterShowSplash()
    {
        
    }


    /**
     * Hides the splash screen, removes the splash screen log handler,
     * finally, removes the splash screen from memory
     */
    @Override
    public final void hideSplash()
    {
        Object loSplash = getSplash();
        if(loSplash != null)
        {
            hideSplash(loSplash);

            ILogHandler loLogHandler = getSplashScreenLogHandler();
            if (loLogHandler != null)
            {
                Application.getInstance().removeLogHandler(loLogHandler);
                m_oSplashLogHandler = null;
            }

            deleteSplash(loSplash);
            m_oSplashScreen = null;
        }
    }

    /**
     * Gets the splash screen, creates it if it does not exist
     * @return the splash screen
     */
    protected final Object getSplash()
    {
        if (m_oSplashScreen == null)
        {
            m_oSplashScreen = createSplashScreen();
        }
        return m_oSplashScreen;
    }
    
    /**
     * This should be overridden in the sub classes to create a splash screen
     * @return
     */
    protected Object createSplashScreen()
    {
        return null;
    }

    /**
     * Displays the splash screen
     * @param toSplash the splash screen to display
     */
    protected void showSplash(Object toSplash)
    {

    }

    /**
     * Hides the splash screen, should be overridden in the sub classes
     * @param toSplash the splash screen to hide
     */
    protected void hideSplash(Object toSplash)
    {

    }

    /**
     * Deletes the splash screen and disposes as needed
     * @param toSplash the splash screen to delete
     */
    protected void deleteSplash(Object toSplash)
    {

    }

    protected final ILogHandler getSplashScreenLogHandler()
    {
        if (m_oSplashLogHandler == null)
        {
            m_oSplashLogHandler = createSplashScreenLogHandler();
        }
        return m_oSplashLogHandler;
    }

    /**
     * Creates the log handler for the splash screen
     * @return the new log handler
     */
    protected ILogHandler createSplashScreenLogHandler()
    {
        return null;
    }

}
