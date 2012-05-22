/* =========================================================
 * ApplicationState.java
 *
 * Author:      kmchugh
 * Created:     28-May-2008, 15:51:30
 * 
 * The application state defines the current state of the
 * singleton application object and controls the actions
 * that are allowed
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Applications;

import Goliath.Constants.LogType;
import Goliath.Interfaces.Commands.ICommand;
import Goliath.Session;

/**
 * The application state defines the current state of the
 * singleton application object and controls the actions
 * that are allowed
 *
 * @see         Related Class
 * @version     1.0 28-May-2008
 * @author      kmchugh
**/
public abstract class ApplicationState extends Goliath.DynamicEnum
{
    /**
     * Creates a new instance of an ApplicationState Object 
     *
     * @param tcValue The value for the application state
     * @throws Goliath.Exceptions.InvalidParameterException
     */
    protected ApplicationState(String tcValue) 
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }
    
    public void registerCommand(ICommand<?, ?> toCommand)
    {
        // By default do nothing
    }
    
    /**
     * attempts to shutdown the application
     * @return false if the application can not shutdown at this stage
     */
    public boolean shutdown()
    {
        setApplicationState(EXITING());
        return true;
    }
    
    /**
     * attempts to start the application
     * @return false if the application can not start at this stage
     */
    public boolean start()
    {
        return false;
    }
    
    
    
    /**
     * Sets the state of the application
     * @param toState the new application state
     */
    protected void setApplicationState(ApplicationState toState)
    {
        ((Application)Application.getInstance()).setState(toState);
        if (toState != ApplicationState.LOADING())
        {
            ((Application)Application.getInstance()).getApplicationSettings().applicationStateChanged(toState);
        }
    }
    
    
    
    private static ApplicationState g_oStarting;
    public static ApplicationState STARTING()
    {
        if (g_oStarting == null)
        {
            try
            {
                g_oStarting = new Starting();
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oStarting;
    }
    private static ApplicationState g_oLoading;
    public static ApplicationState LOADING()
    {
        if (g_oLoading == null)
        {
            try
            {
                g_oLoading = new Loading();
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oLoading;
    }
    private static ApplicationState g_oRunning;
    public static ApplicationState RUNNING()
    {
        if (g_oRunning == null)
        {
            try
            {
                g_oRunning = new Running();
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oRunning;
    }
    private static ApplicationState g_oUnloading;
    public static ApplicationState UNLOADING()
    {
        if (g_oUnloading == null)
        {
            try
            {
                g_oUnloading = new Unloading();
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oUnloading;
    }
    private static ApplicationState g_oExiting;
    public static ApplicationState EXITING()
    {
        if (g_oExiting == null)
        {
            try
            {
                g_oExiting = new Exiting();
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oExiting;
    }
    
    /**
     * The application is in this state when it is preparing to start
     * up for the first time
     */
    private static class Starting extends ApplicationState
    {
        private Starting()
        {
            super ("STARTING");
        }
        
        @Override
        public boolean start()
        {
            setApplicationState(LOADING());
            return true;
        }
    }
    
    /**
     * The application is in this state when loading its
     * properties and classes
     */     
    private static class Loading extends ApplicationState
    {
        private Loading()
        {
            super ("LOADING");
        }

        @Override
        public boolean start()
        {
            setApplicationState(RUNNING());
            return true;
        }
        
        
    }
    
    /**
     * The application is normally in this state
     */     
    private static class Running extends ApplicationState
    {
        private Running()
        {
            super ("RUNNING");
        }

        @Override
        public boolean shutdown()
        {
            setApplicationState(UNLOADING());
            return true;
        }
        
        @Override
        public void registerCommand(ICommand<?, ?> toCommand)
        {
            if (toCommand.getSession() == null)
            {
                toCommand.setSession(Session.getCurrentSession());
            }
            Application.getInstance().log("Registered Command[" + toCommand.toString() + "] with Session[" + toCommand.getSession().getSessionID() + "]", LogType.TRACE());
            toCommand.getSession().addCommand(toCommand);
        }
    }
    
    /**
     * The application goes in to this state when it is
     * preparing to exit
     */
    private static class Unloading extends ApplicationState
    {
        private Unloading()
        {
            super ("UNLOADING");
        }
    }
    
    /**
     * The application goes into this state when it is
     * about to exit
     */
    private static class Exiting extends ApplicationState
    {
        private Exiting()
        {
            super ("EXITING");
        }
    }
}
