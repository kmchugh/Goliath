/* =========================================================
 * Session.java
 *
 * Author:      kmchugh
 * Created:     26 November 2007, 15:22
 *
 * Description
 * --------------------------------------------------------
 * Controls each session of the application.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath;

import Goliath.Applications.Application;
import Goliath.Applications.ApplicationState;
import Goliath.Applications.ClientInformation;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Commands.CommandManager;
import Goliath.Constants.LogType;
import Goliath.Constants.SessionEventType;
import Goliath.Constants.StringFormatType;
import Goliath.Interfaces.Commands.ICommand;
import Goliath.Interfaces.Commands.ICommandManager;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.ISession;
import Goliath.Security.User;
import java.util.Date;

/**
 * The session object is created for every client of the application.  A client
 * could be a user, another application, even a thread from within the application.
 * In fact the Application itself runs under a system session which allows it access
 * to most areas of the system.
 * 
 *
 * @version     1.0 26 November 2007
 * @author      kmchugh
 **/
public class Session extends Goliath.Object
        implements Goliath.Interfaces.ISession
{
    private static ISession g_oAppSession;
    private final static String g_cSystemSession = Thread.currentThread().getName();
    private static List<String> g_oMainSessionList;
    
    private String m_cSessionID;
    private long m_nExpiry;
    private long m_nExpiryLength;
    private long m_nMaxExpiryLength;
    private String m_cSessionIP;
    private User m_oUser;
    private Goliath.Interfaces.Collections.IPropertySet m_oSet;
    private ICommandManager m_oCommandManager;
    private java.lang.Object m_oLanguage;
    private EventDispatcher<SessionEventType, Event<Session>> m_oEventDispatcher;
    private boolean m_lHasFiredExpiry;
    private ClientInformation m_oClientInformation;
    private Boolean m_lIsSystem;
    private boolean m_lIsStarted;

    public static ISession getCurrentSession()
    {
        // TODO : Move this logic to the SessionManager

        String lcSessionID = Thread.currentThread().getName();

        // If the thread has been marked as not being a session, then get the application session.
        if (g_oMainSessionList != null && g_oMainSessionList.contains(lcSessionID))
        {
            return getAppSession();
        }

        // If the application is running determine the session based on the thread
        if (Application.getInstance().getState() == ApplicationState.RUNNING())
        {
            // If this is the event queue, then get the session that called this
            if (lcSessionID.toLowerCase().startsWith("awt-eventqueue"))
            {
                return getAppSession();
            }
            else
            {
                // Get the current session
                return Application.getInstance().getSessionManager().getSession(lcSessionID);
            }
        }
        else
        {
            // We may be loading, but if this is the system thread, then return the system session, otherwise return the app session
            return isSystemSession(lcSessionID) ? Application.getInstance().getSessionManager().getSession(lcSessionID) : getAppSession();
        }
    }

    /**
     * Marks a thread as not being a session, this will prevent sessions being created for that thread
     * @param tcThreadName the name of the thread to stop sessions from being created for
     */
    public static void markAsNotSession(String tcThreadName)
    {
        if (g_oMainSessionList == null)
        {
            g_oMainSessionList = new List<String>();
        }
        g_oMainSessionList.add(tcThreadName);
    }

    /**
     * Determines if the session id is the system session id or not
     * @param tcSessionID the session id to check
     * @return true if this is the system session id
     */
    public static boolean isSystemSession(String tcSessionID)
    {
        return g_cSystemSession == null || g_cSystemSession.equalsIgnoreCase(tcSessionID);
    }

    /**
     * Gets the system session
     * @return the system session, or null if it does not yet exist
     */
    public static ISession getSystemSession()
    {
        return g_cSystemSession == null ? null : Application.getInstance().getSessionManager().getSession(g_cSystemSession);
    }

    /**
     * Determines if the session id is the application session id or not
     * @param tcSessionID the session id to check
     * @return true if this is the application session id
     */
    public static boolean isApplicationSession(String tcSessionID)
    {
        // The application session may not be required
        return g_oAppSession == null ? false : g_oAppSession.getSessionID().equalsIgnoreCase(tcSessionID);
    }

    /**
     * Gets a reference to the application session if needed
     * @return the application session
     */
    private static ISession getAppSession()
    {
        if (g_oAppSession == null)
        {
            g_oAppSession = new Session();
            g_oAppSession = Application.getInstance().getSessionManager().getSession(Goliath.Utilities.generateStringGUID());

            // As this is the thread of the application, it shouldn't expire until the application exits
            // TODO: Need to check that this is getting refreshed at half the expiry length
            g_oAppSession.setExpiryLength(10*60*1000);
        }
        return g_oAppSession;
    }
    
    /**
     * Creates a new instance of the session object
     *
     * @param  tcSessionID  the session id to use for this session
     */
    public Session(String tcSessionID)
    {
        // TODO: Need to protect this so it can not be called using the system session name from outside the goliath packages

        // Set the session id
        m_cSessionID = tcSessionID;
    }
    
    public Session()
    {
        m_cSessionID = Goliath.Utilities.generateStringGUID();
    }

    @Override
    public ClientInformation getClientInformation()
    {
        return m_oClientInformation;
    }

    @Override
    public void initialiseSession()
    {
        onInitialiseSession();
    }

    /**
     * Updates the current client information with the details from the specified information object
     * @param toInfo the information to merge
     */
    @Override
    public void updateClientInformation(ClientInformation toInfo)
    {
        if (m_oClientInformation == null)
        {
            m_oClientInformation = toInfo;
        }
        else
        {
            for (String tcKey : toInfo.getPropertyKeySet())
            {
                m_oClientInformation.setProperty(tcKey, toInfo.getProperty(tcKey));
            }
        }
    }

    @Override
    public String getSessionIP()
    {
        return m_cSessionIP;
    }

    @Override
    public void setSessionIP(String tcIP)
    {
        // TODO: Regex validation against IPv4 && IPv6
        m_cSessionIP = tcIP;
    }



    private void onInitialiseSession()
    {
        isSystem();
        m_lHasFiredExpiry = false;
        m_oUser = null;
        clearProperties();
        m_nMaxExpiryLength = getDefaultMaximumExpiryLength();
        m_nExpiryLength = getDefaultExpiryLength();
    }
    
    @Override
    public Goliath.Collections.HashTable<ICommand<?, ?>, java.lang.Object> getCommandResults()
    {
        if (m_oCommandManager != null)
        {
            return m_oCommandManager.getCommandResults();
        }
        else
        {
            return null;
        }
    }

    @Override
    public HashTable<String, Float> getCommandStatus()
    {
        if (m_oCommandManager != null)
        {
            return m_oCommandManager.getCommandStatus();
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean isRegistered(ICommand toCommand)
    {
        if (m_oCommandManager != null)
        {
            return m_oCommandManager.contains(toCommand);
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean hasCommands()
    {
        if (m_oCommandManager != null)
        {
            return m_oCommandManager != null && m_oCommandManager.hasCommands();
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public void start()
    {
        startSession();
    }
    
    /**
     * gets the application settings that are currently in use
     *
     * @return  the application settings in use by the application
     */
    protected Goliath.Interfaces.Collections.IPropertySet getPropertySet()
    {
        synchronized(this)
        {
            if (m_oSet == null)
            {
                // TODO: Dynamically load the applications settings.
                try
                {
                    m_oSet = new Goliath.Collections.PropertySet();
                }
                catch (Exception e)
                {
                    
                }
            }
            return m_oSet;
        }
    }
    
    @Override
    public void pause()
    {
        if (m_oCommandManager != null)
        {
            Application.getInstance().log("Pausing Session " + getSessionID(), LogType.TRACE());
            m_oCommandManager.pause();
        }
    }
    @Override
    public void resume()
    {
        Application.getInstance().log("Resuming Session " + getSessionID(), LogType.TRACE());
        if (m_oCommandManager != null)
        {
            m_oCommandManager.resume();
        }
    }
            
    
    @Override
    public void addCompletedCommand(ICommand<?, ?> toCommand)
    {
        if (m_oCommandManager != null)
        {
            m_oCommandManager.addCompletedCommand(toCommand);
        }
    }
    
    /**
     * Sets a property for the session
     * @param <T> The type of the property
     * @param tcProperty the property to set
     * @param toValue the value to set the property to
     * @return true if the application settings were changed because of this call
     */
    @Override
    public <T> boolean setProperty(String tcProperty, T toValue)
    {
        return getPropertySet().setProperty(tcProperty, toValue);
    }
    
    /**
     * Clears a property for the session
     * @param tcProperty the property to clear
     * @return true if the application settings were changed because of this call
     */
    @Override
    public boolean clearProperty(String tcProperty)
    {
        return getPropertySet().clearProperty(tcProperty);
    }

    /**
     * Gets the value of a property, attempts the session first, then the application,
     * if the session was null
     * @param <T> The type of the property
     * @param tcProperty the property to get
     * @return the value of the property, or null if the property did not exist
     */
    @Override
    public <T> T getProperty(String tcProperty)
    {
        T loReturn = (T)getPropertySet().getProperty(tcProperty);
        if (loReturn ==  null)
        {
            return Application.getInstance().<T>getProperty(tcProperty);
        }
        return loReturn;
    }


    /**
     * Gets the length of time in milliseconds that is added to the expiry time every time the session is refreshed
     * @return the number of milliseconds
     */
    @Override
    public long getExpiryLength()
    {
        if (m_nExpiryLength == 0)
        {
            m_nExpiryLength = getDefaultExpiryLength();
        }
        return m_nExpiryLength;
    }

    /**
     * Gets the default expiry length
     * @return the default expiry length
     */
    protected long getDefaultExpiryLength()
    {
        try
        {
            return isSystem() ? 30000L : Application.getInstance().getPropertyHandlerProperty("Application.Settings.DefaultSessionExpiryLength", 30000L);
        }
        catch (Throwable e)
        {
            return 10000L;
        }
    }

    /**
     * Gets the maximum expiry length that is allowed by this session, the expiry length can not be set to more than this number
     * @return the maximum expiry length
     */
    protected long getDefaultMaximumExpiryLength()
    {
        try
        {
            return isSystem() ? 1200000L : Application.getInstance().getPropertyHandlerProperty("Application.Settings.MaximumSessionExpiryLength", 1200000L);
        }
        catch (Throwable e)
        {
            return 1200000L;
        }
    }

    /**
     * Sets the time in milliseconds that will be added to the session expiry time on a refresh
     * @param tnLength the new number of milliseconds
     */
    @Override
    public void setExpiryLength(long tnLength)
    {
        if (tnLength > 0 && m_nExpiryLength != tnLength)
        {
            Application.getInstance().log(
                "Setting new expiry [" + getSessionID() + "] ExpiryLength: " + getExpiryLength() + " new: " + Math.min(m_nMaxExpiryLength, tnLength),
                LogType.TRACE());
            m_nExpiryLength = Math.min(m_nMaxExpiryLength, tnLength);
            renew();
        }
    }

    /**
     * Sets the maximum length the session expiry length is allowed to be set to
     * @param tnLength the new expiry length
     */
    @Override
    public void setMaximumExpiryLength(long tnLength)
    {
        if (tnLength > 0 && m_nMaxExpiryLength != tnLength)
        {
            m_nMaxExpiryLength = tnLength;
            if (m_nExpiryLength > m_nMaxExpiryLength)
            {
                setExpiryLength(tnLength);
            }
        }
    }
    
    /**
     * Gets the maximum expiry length, the expiry length can not be set to more than this value
     * @return the maximum expiry length
     */
    @Override
    public long getMaximumExpiryLength()
    {
        return m_nMaxExpiryLength;
    }



    /**
     * Increments the session expiry by the default time
     * @return true if the session was renewed correctly
     */
    @Override
    public boolean renew()
    {
        if (!this.isExpired())
        {
            m_nExpiry = System.currentTimeMillis() + getExpiryLength();
            Application.getInstance().log("Renewing Session " + getSessionID(), LogType.TRACE());
            return true;
        }
        else
        {
            throw new Goliath.Exceptions.SessionExpiredException();
        }
    }

    /**
     * Removes authentication from this session (logs the session out)
     *
     * @return  true if the session was unauthenticated correctly
     */            
    @Override
    public boolean unauthenticate()
    {
        if (m_oUser == null)
        {
            return true;
        }
        boolean llReturn = m_oUser.unauthenticate();
        if (llReturn)
        {
            reset();
            fireEvent(SessionEventType.ONUNAUTHENTICATED(), new Event<Session>(this));
        }
        return llReturn;
    }
    
    /**
     * resets the session by unauthenticating and clearing all of the
     * session properties
     * @return
     */
    @Override
    public boolean reset()
    {
        Application.getInstance().log("Reseting Session " + getSessionID(), LogType.TRACE());
        boolean llReturn = !isAuthenticated() || unauthenticate();
        m_lHasFiredExpiry = false;
        m_oUser = null;
        clearProperties();
        m_nExpiryLength = getDefaultExpiryLength();
        return llReturn;
    }

    @Override
    public boolean isSystem()
    {
        if (m_lIsSystem == null)
        {
            String lcThreadName = Thread.currentThread().getName();
            Application.getInstance().log("Checking for system session on thread " + lcThreadName, LogType.DEBUG());
            m_lIsSystem = (this.getSessionID().equals(lcThreadName) && lcThreadName.equalsIgnoreCase(g_cSystemSession));
        }
        return m_lIsSystem;
    }


    
    protected synchronized void createSessionUser()
    {
        if (isSystem())
        {
            // Create the system user for the application
            setUser(Goliath.SystemUser.createSystemUser(Application.getInstance().getSecurityManager()));
        }
        else
        {
            // Create the anonymous user for the application
            setUser(Application.getInstance().getSecurityManager().createAnonymousUser());
        }
    }
    
    @Override
    public void clearProperties()
    {
        Application.getInstance().log("Clearing properties for Session " + getSessionID(), LogType.TRACE());
        if (m_oSet != null)
        {
            m_oSet.clear();
        }
    }
    
    /**
     * Authenticates the session
     *
     * @param  toUser   the user to use to authenticate
     * @param tcPassword   the password to use to authenticate
     */
    @Override
    public synchronized boolean authenticate(User toUser, String tcPassword)
    {
        Goliath.Utilities.checkParameterNotNull("toUser", toUser);
        User loUser = getUser();

        setUser(toUser);
        
        // Get the user manager and use that to authenticate the user
        boolean llReturn = toUser.authenticate(tcPassword);
        if (llReturn)
        {
            // Clear out the current session properties
            clearProperties();
            this.setExpiryLength(20 * 60 * 1000);
            renew();
            fireEvent(SessionEventType.ONAUTHENTICATED(), new Event<Session>(this));
        }
        else
        {
            setUser(loUser);
        }
        return llReturn;
    }
    
    /**
     * Returns the session id for this session
     *@return the String representation of the session id
     */
    @Override
    public String getSessionID()
    {
        return m_cSessionID;
    }
    
    /**
     * checks if the session has been authenticated
     * @return  true if the session has been authenticated
     */
    @Override
    public boolean isAuthenticated()
    {
        return getUser().isAuthenticated();
    }

    /**
     * checks if the session has expired
     *
     * @return  true if the session has expired
     */
    @Override
    public boolean isExpired()
    {
        // The main session can not expire
        if(isSystem())
            return false;

        // TODO: Optimise this method
        boolean llReturn = getRemainingTime() <= 0;
        if (llReturn && !m_lHasFiredExpiry)
        {
            m_lHasFiredExpiry = true;
            fireEvent(SessionEventType.ONEXPIRED(), new Event<Session>(this));
        }
        return llReturn;
    }

    /**
     * Gets the number of milliseconds before this session will expire
     * @return the number of milliseconds until expiry
     */
    @Override
    public long getRemainingTime()
    {
        return m_nExpiry - System.currentTimeMillis();
    }

    /**
     * gets the date/time this session will expire
     * @return  the date and time the session will expire
     */
    @Override
    public Date getExpiry()
    {
        return new Date(m_nExpiry);
    }

    /**
     * Gets the display name for this session, usually the username
     * 
     * @return The display name for the session
     */
    @Override
    public String getDisplayName()
    {
        // Store the display name temporarily so we are not doing so much work
        String lcReturn = getProperty("getDisplayName");
        if (lcReturn == null)
        {
            lcReturn = getUser().getDisplayName();
            setProperty("getDisplayName", lcReturn);
        }
        return lcReturn;
    }

    /**
     * Gets the user name for this session
     *
     * @return The user name for the session
     */
    @Override
    public String getUserName()
    {
        // Store the display name temporarily so we are not doing so much work
        String lcReturn = getProperty("getUserName");
        if (lcReturn == null)
        {
            lcReturn = getUser().getName();
            setProperty("getUserName", lcReturn);
        }
        return lcReturn;
    }

    /**
     * Gets the email address for this session
     *
     * @return The email address for the session
     */
    @Override
    public String getEmail()
    {
        // Store the display name temporarily so we are not doing so much work
        String lcReturn = getProperty("getEmail");
        if (lcReturn == null)
        {
            lcReturn = getUser().getEmailAddress();
            setProperty("getEmail", lcReturn);
        }
        return lcReturn;
    }

    /**
     * Gets the user GUID for this session
     *
     * @return The user GUID for the session
     */
    @Override
    public String getUserGUID()
    {
        // Store the display name temporarily so we are not doing so much work
        String lcReturn = getProperty("getUserGUID");
        if (lcReturn == null)
        {
            lcReturn = getUser().getGUID();
            setProperty("getUserGUID", lcReturn);
        }
        return lcReturn;
    }
    
    /**
     * gets the string representation of this object
     * will return the display name if there is one, 
     * otherwise the session id will be used
     *
     * @param  toFormat the format type to get the string in
     * @return  a string representing the object
     */
    @Override
    protected String formatString(StringFormatType toFormat)
    {
        if (!Goliath.Utilities.isNullOrEmpty(getDisplayName()))
        {
            return m_cSessionID + "|" + getUserName() + " - User " + m_oUser.getDisplayName();
        }
        return m_cSessionID;
    }
    
    @Override
    public synchronized User getUser()
    {
        if (m_oUser == null)
        {
            createSessionUser();
        }
        return m_oUser;
    }
    
     /**
     * Sets the user for this session 
      * @param toUser The user to set
      */
    protected void setUser(User toUser)
    {
        Goliath.Utilities.checkParameterNotNull("toUser", toUser);

        m_oUser = toUser;


        if (!Goliath.DynamicCode.Java.isEqualOrAssignable(SystemUser.class, m_oUser.getClass()))
        {
            // Log that the user was changed.
            Application.getInstance().log("User changed for Session [" + getSessionID() + "] to User[" + toUser.getName() + "]");
        }
        else
        {
            Application.getInstance().log("System user assigned for Session [" + getSessionID() + "]");
        }
    }
    
    @Override
    public void addCommand(Goliath.Interfaces.Commands.ICommand<?, ?> toCommand)
    {
        if (!isExpired())
        {

            if (m_oCommandManager == null)
            {
                m_oCommandManager = isSystem() ? new CommandManager(100, 10, Environment.getAvailableProcessors(), this) : new CommandManager(this);
                m_oCommandManager.activate();
            }
            m_oCommandManager.addCommand(toCommand);
        }
        else
        {
            throw new Goliath.Exceptions.SessionExpiredException();
        }
    }
    
    @Override
    public java.lang.Object popCommandResult(ICommand<?, ?> toCommand)
    {
        if (m_oCommandManager != null)
        {
            return m_oCommandManager.popCommandResult(toCommand);
        }
        else
        {
            return null;
        }
    }

    
    private void startSession()
    {
        if (!m_lIsStarted)
        {
            m_lIsStarted = true;
            Application.getInstance().log("Starting Session " + getSessionID(), LogType.TRACE());
            // Just add one second to expiry time, then renew the session.
            m_nExpiry = System.currentTimeMillis() + getExpiryLength();
            this.renew();
        }
    }
    
    // This clears and closes the session
    @Override
    public void cleanUp()
    {
        if (m_oCommandManager != null)
        {
            m_oCommandManager.deactivate();
            m_oCommandManager = null;
        }
        // Clear this session
        Application.getInstance().log("Closing Session " + m_cSessionID, LogType.TRACE());
        Application.getInstance().getSessionManager().clearSession(m_cSessionID);
    }
    
    /**
    * Sets the language for session - should only be set if the language
    * used is not the default language
    *
    * @param  toLanguage  the langauge to use for this session
    */
    @Override
    public void setLanguage(java.lang.Object toLanguage)
    {
        m_oLanguage = toLanguage;
    }

   

    /**
    * gets the language for session
    * Should be null if the session is in the default language
    *
    * @return  langauge used in this session
    */
    @Override
    public java.lang.Object getLanguage()
    {
        return m_oLanguage;
    }

 

    /**
    * returns whether the session is using language other than default
    * will be false if the language object is null otherwise true
    *
    * @return  whether or not the session is using language other than default
    */
    @Override
    public boolean isMultiLingual()
    {
      return getLanguage() != null;
    }



    @Override
    public void suppressEvents(boolean tlSuppress)
    {
        if (m_oEventDispatcher == null)
        {
            return;
        }
        m_oEventDispatcher.suppressEvents(tlSuppress);
    }

    @Override
    public boolean removeEventListener(SessionEventType toEvent, IDelegate toCallback)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.removeEventListener(toEvent, toCallback) : false;
    }

    @Override
    public boolean hasEventsFor(SessionEventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.hasEventsFor(toEvent) : false;
    }

    @Override
    public final void fireEvent(SessionEventType toEventType, Event toEvent)
    {
        if (m_oEventDispatcher == null)
        {
            return;
        }
        m_oEventDispatcher.fireEvent(toEventType, toEvent);
    }

    @Override
    public boolean clearEventListeners(SessionEventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners(toEvent) : false;
    }

    @Override
    public boolean clearEventListeners()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.clearEventListeners() : false;
    }

    @Override
    public boolean areEventsSuppressed()
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.areEventsSuppressed() : false;
    }

    @Override
    public boolean addEventListener(SessionEventType toEvent, IDelegate toCallback)
    {
        if (m_oEventDispatcher == null)
        {
            m_oEventDispatcher = new EventDispatcher<SessionEventType, Event<Session>>();
        }
        return m_oEventDispatcher.addEventListener(toEvent, toCallback);
    }

    
    @Override
    public boolean equals(java.lang.Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Session other = (Session) obj;
        if (!this.m_cSessionID.equals(other.m_cSessionID) && (this.m_cSessionID == null || !this.m_cSessionID.equals(other.m_cSessionID)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 23 * hash + (this.m_cSessionID != null ? this.m_cSessionID.hashCode() : 0);
        return hash;
    }
    
    
    
    
    
    
}
