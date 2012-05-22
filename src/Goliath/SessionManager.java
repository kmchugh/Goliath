/* =========================================================
 * SessionManager.java
 *
 * Author:      kmchugh
 * Created:     26 November 2007, 16:55
 *
 * Description
 * --------------------------------------------------------
 * Manages Sessions in the application
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath;

import Goliath.Applications.*;
import Goliath.Arguments.SingleParameterArguments;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Constants.LogType;
import Goliath.Interfaces.ISession;
import Goliath.Threading.ThreadJob;

/**
 * Manages sessions in the application
 *
 * @version     1.0 26 November 2007
 * @author      kmchugh
 **/
public class SessionManager extends Goliath.Object implements Goliath.Interfaces.ISessionManager
{
    private HashTable<String, Goliath.Interfaces.ISession> m_oSessions = new HashTable<String, Goliath.Interfaces.ISession>();
    private boolean m_lIsActive = false;
    
    // TODO: This should be a singleton
    /** Creates a new instance of SessionManager */
    public SessionManager()
    {
        Goliath.Threading.ThreadJob loThreadJob = new ThreadJob<SingleParameterArguments<ISession>>(new SingleParameterArguments(null))
        {
            @Override
            protected void onRun(SingleParameterArguments<ISession> toCommandArgs)
            {
                try
                {
                    int lnCount = 0;
                    while(true)
                    {
                        if (m_oSessions.size() > 0)
                        {
                            checkSessions();
                        }
                        lnCount++;
                        if (lnCount >= 120)
                        {
                            lnCount = 0;
                        }
                        Goliath.Threading.Thread.sleep(500);
                    }
                }
                catch (Exception toException)
                {
                    // TODO: See what needs to be done here
                    Application.getInstance().log(toException);
                }
            }
        };

        Goliath.Threading.Thread loThread = new Goliath.Threading.Thread(loThreadJob);
        loThread.setName("SessionManager");
        loThread.start();
    }
    
    
    
    private void checkSessions()
    {
        List<ISession> loSessions = new List<ISession>();
        for (ISession loSession : m_oSessions.values())
        {
            if (loSession.isExpired())
            {
                loSessions.add(loSession);
            }
        }
        for (ISession loSession : loSessions)
        {
            loSession.cleanUp();
        }
    }

    /**
     * Creates a new session
     *
     * @return  a new session
     */
    @Override
    public Goliath.Interfaces.ISession createSession()
    {
        return createSession(generateID());
    }
    
    /**
     * Generates a random session id
     *
     * @return  a string containing the id
     */
    protected String generateID()
    {
        return Goliath.Utilities.generateStringGUID();        
    }

    /**
     * Checks if the Session manager is active
     *
     * @return true if it is active
     */
    @Override
    public boolean isActive()
    {
        return m_lIsActive;
    }

    /**
     * Deactivates and unloads the SessionManager
     */
    @Override
    public final void deactivate()
    {
        try
        {
            onDeactivate();
            m_lIsActive = false;
        }
        catch (Exception e)
        {
            // Something bad happened and we are unable to deactivate the settings
            Application.getInstance().log(e);
        }
    }

    /**
     * Activates the Session Manager
     */
    @Override
   public final void activate()
    {
        try
        {
            onActivate();
            m_lIsActive = true;
        }
        catch (Exception e)
        {
            // Something bad happened and we are unable to activate the settings
            Application.getInstance().log(e);
        }
    }
   
   /**
    * Custom code for deactivation
    */
   protected void onDeactivate()
   {
   }

   /**
    * Custom code for activation
    */
   protected void onActivate()
   {
   }

   /**
    * Gets a session or creates one if it does not already exist
    * @param tcSessionID
    * @return the session related to the session string
    */
    @Override
    public Goliath.Interfaces.ISession getSession(String tcSessionID)
    {
        Goliath.Interfaces.ISession loSession = m_oSessions.get(tcSessionID);
        if (loSession == null)
        {
             loSession = createSession(tcSessionID);
        }
        return loSession;
    }

    /**
     * Checks if the session specified exists
     * @param tcSessionID the session to check for
     * @return true if the session exists, false otherwise
     */
    @Override
    public boolean hasSession(String tcSessionID)
    {
        return m_oSessions != null && m_oSessions.containsKey(tcSessionID);
    }
    
    /**
    * Clears a session or creates one if it does not already exist
    * @param tcSessionID
    */
    @Override
    public void clearSession(String tcSessionID)
    {
        ISession loSession = null;
        if (m_oSessions.containsKey(tcSessionID))
        {
            loSession = getSession(tcSessionID);
            m_oSessions.remove(tcSessionID);
            Application.getInstance().log("Clearing " + loSession.getClass().getSimpleName() + " - " + tcSessionID, LogType.TRACE());
            loSession.clearProperties();
        }
    }

    
    @Override
    public final Goliath.Interfaces.ISession createSession(String tcSessionID)
    {
        if (!m_oSessions.containsKey(tcSessionID))
        {
            Goliath.Interfaces.ISession loSession = onCreateSession(tcSessionID);
            // Force the system session if needed
            m_oSessions.put(loSession.getSessionID(), loSession);
            loSession.initialiseSession();
            Application.getInstance().log("Created " + loSession.getClass().getSimpleName() + " - " + tcSessionID, LogType.TRACE());
            loSession.start();
            return loSession;
        }
        // The session already existed so could not be created
        return null;        
    }
    
    
    protected Goliath.Interfaces.ISession onCreateSession(String tcSessionID)
    {
        return new Goliath.Session(tcSessionID);        
    }
}
