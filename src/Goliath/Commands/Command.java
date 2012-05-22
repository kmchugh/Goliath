/* =========================================================
 * Command.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
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

package Goliath.Commands;

import Goliath.Applications.Application;
import Goliath.Arguments.Arguments;
import Goliath.Collections.List;
import Goliath.Constants.LogType;
import Goliath.Constants.StringFormatType;
import Goliath.Event;
import Goliath.EventDispatcher;
import Goliath.Exceptions.ProcessNotComplete;
import Goliath.Interfaces.Commands.ICommand;
import Goliath.Interfaces.IDelegate;
import Goliath.Interfaces.ISession;
import Goliath.Session;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class participates in the Command Pattern.  Commands sent to the application
 * should be recieved in a command queue and should be processed from there.
 * Commands are considered equal if their names are the same.  Commands that are
 * created without names are given a name based on the class of the command.
 * To initialise a command, if anything extra is required, it should be done by
 * overriding the onInitialise method.  The priority is initialised as zero.  The higher the 
 * priority the more important the command and the sooner it will be executed on a 
 * command queue
 * For example:
 * <pre>
 *      ICommand loCommand = new DeleteItemCommand();
 * </pre>
 *
 * @param <A> the type of command arg this command takes
 * @param <T> the return type of the Execute parameter
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public abstract class Command<A extends Arguments, T> extends Goliath.Object
        implements Goliath.Interfaces.Commands.ICommand<A, T>
        
{

    /**
     * The priority of this command
     */
    private int m_nPriority;
    private long m_nStartTime;
    private long m_nEndTime;
    private long m_nTimeout;
    private float m_nProgress;
    private boolean m_lCancelled;
    private Goliath.Collections.List<Throwable> m_oErrors;
    private T m_oReturn;
    private String m_cName;
    private A m_oArgs;
    private String m_cID;
    private String m_cConfirmMessage;
    private boolean m_lConfirmFirst;
    private Goliath.Interfaces.ISession m_oSession;
    private boolean m_lIsExecuting;
    private Timer m_oTimer;
    private boolean m_lStoreResult;
    private EventDispatcher<CommandEventType, Event<ICommand<A, T>>> m_oEventDispatcher;
    
    // TODO: This should be updated to use an event handler instead
    private String m_cMessage;


    // TODO: Implement a stateless command

    // TODO: Impelement a mechanism for getting multiple results through an event listener
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     */
    protected Command()
    {
        this(false);
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     * @param tcName The name of the command
     */
    protected Command(String tcName)
    {
        this(true, tcName);
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     * @param toArgs The command arguments
     */
    protected Command(A toArgs)
    {
        m_oArgs = toArgs;
        initialiseComponent();
        // Register the command with the application object
        registerCommand();
    }

    /**
     * Creates an instance of the command, also registers the command with the specified session.
     * @param toArgs The arguments for the command
     * @param toSession The session to run the command in
     */
    protected Command(A toArgs, ISession toSession)
    {
        m_oArgs = toArgs;
        initialiseComponent();
        setSession(toSession);
        registerCommand();
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     * @param toArgs The command arguments
     * @param tcName The name of the command
     */
    protected Command(A toArgs, String tcName)
    {
        m_cName = tcName;
        m_oArgs = toArgs;
        initialiseComponent();
        // Register the command with the application object
        registerCommand();
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     *
     * @param  tlRegister   True if you want to register the command immediately
     */
    protected Command(boolean tlRegister)
    {
        // Initialise the object
        initialiseComponent();
        if (tlRegister)
        {
            // Register the command with the application object
            registerCommand();
        }
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     *
     * @param  tlRegister   True if you want to register the command immediately
     * @param tcName The Name of the command
     */
    protected Command(boolean tlRegister, String tcName)
    {
        m_cName = tcName;
        
        // Initialise the object
        initialiseComponent();
        
        if (tlRegister)
        {
            // Register the command with the application object
            registerCommand();
        }
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     *
     * @param  tlRegister   True if you want to register the command immediately
     * @param  tnPriority   The priority of the command
     * @throws Goliath.Exceptions.InvalidParameterException if tnPriority is less than 0
     */
    protected Command(boolean tlRegister, int tnPriority)
            throws Goliath.Exceptions.InvalidParameterException
    {
        if (m_nPriority >= 0)
        {
            m_nPriority = tnPriority;
        }
        else
        {
            throw new Goliath.Exceptions.InvalidParameterException("tnPriority");
        }
        
        // Initialise the object
        initialiseComponent();
        
        if (tlRegister)
        {
            // Register the command with the application object
            registerCommand();
        }
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     *
     * @param  tlRegister   True if you want to register the command immediately
     * @param toArgs The command arguments
     * @throws Goliath.Exceptions.InvalidParameterException if tnPriority is less than 0
     */
    protected Command(boolean tlRegister, A toArgs)
            throws Goliath.Exceptions.InvalidParameterException
    {
        m_oArgs = toArgs;
        
        // Initialise the object
        initialiseComponent();
        
        if (tlRegister)
        {
            // Register the command with the application object
            registerCommand();
        }
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     *
     * @param  tlRegister   True if you want to register the command immediately
     * @param  tnPriority   The priority of the command
     * @param toArgs The command arguments
     * @throws Goliath.Exceptions.InvalidParameterException if tnPriority is less than 0
     */
    protected Command(boolean tlRegister, int tnPriority, A toArgs)
            throws Goliath.Exceptions.InvalidParameterException
    {
        m_oArgs = toArgs;
        
        if (m_nPriority >= 0)
        {
            m_nPriority = tnPriority;
        }
        else
        {
            throw new Goliath.Exceptions.InvalidParameterException("tnPriority");
        }
        
        // Initialise the object
        initialiseComponent();
        
        if (tlRegister)
        {
            // Register the command with the application object
            registerCommand();
        }
    }
    
    /**
     * This creates an instance of the Command object.  This is not directly creatable.  This class must be subclassed.
     *
     * @param  tlRegister   True if you want to register the command immediately
     * @param  tnPriority   The priority of the command
     * @param toArgs The command arguments
     * @param tcName The name of the command
     * @throws Goliath.Exceptions.InvalidParameterException if tnPriority is less than 0
     */
    protected Command(boolean tlRegister, int tnPriority, A toArgs, String tcName)
            throws Goliath.Exceptions.InvalidParameterException
    {
        m_oArgs = toArgs;
        m_cName = tcName;
        
        if (m_nPriority >= 0)
        {
            m_nPriority = tnPriority;
        }
        else
        {
            throw new Goliath.Exceptions.InvalidParameterException("tnPriority");
        }
        
        // Initialise the object
        initialiseComponent();
        
        if (tlRegister)
        {
            // Register the command with the application object
            registerCommand();
        }
    }
    
    /**
     * Creates a new instance of a command object
     *
     * @param  tnPriority   the priority to assign the command
     * @throws  Goliath.Exceptions.InvalidParameterException if the priority is less than 0
     */            
    protected Command(int tnPriority)
        throws Goliath.Exceptions.InvalidParameterException
    {
        if (m_nPriority >= 0)
        {
            m_nPriority = tnPriority;
        }
        else
        {
            throw new Goliath.Exceptions.InvalidParameterException("tnPriority");
        }
        
        // Initialise the object
        initialiseComponent();
        
        registerCommand();
    }
    
    /**
     * Creates a new instance of a command object
     *
     * @param  tnPriority   the priority to assign the command
     * @param toArgs The command arguments
     * @throws  Goliath.Exceptions.InvalidParameterException if the priority is less than 0
     */            
    protected Command(int tnPriority, A toArgs)
        throws Goliath.Exceptions.InvalidParameterException
    {
        m_oArgs = toArgs;
        
        if (m_nPriority >= 0)
        {
            m_nPriority = tnPriority;
        }
        else
        {
            throw new Goliath.Exceptions.InvalidParameterException("tnPriority");
        }
        
        // Initialise the object
        initialiseComponent();
        
        registerCommand();
    }
    
    /**
     * Creates a new instance of a command object
     *
     * @param  tnPriority   the priority to assign the command
     * @param toArgs 
     * @param tcName 
     * @throws  Goliath.Exceptions.InvalidParameterException if the priority is less than 0
     */            
    protected Command(int tnPriority, A toArgs, String tcName)
        throws Goliath.Exceptions.InvalidParameterException
    {
        m_oArgs = toArgs;
        m_cName = tcName;
        
        if (m_nPriority >= 0)
        {
            m_nPriority = tnPriority;
        }
        else
        {
            throw new Goliath.Exceptions.InvalidParameterException("tnPriority");
        }
        
        // Initialise the object
        initialiseComponent();
        
        registerCommand();
    }
    
    @Override
    public String getConfirmMessage()
    {
        return m_cConfirmMessage;
    }
    @Override
    public void setConfirmMessage(String tcMessage)
    {
        m_cConfirmMessage = tcMessage;
    }
    @Override
    public boolean getConfirmFirst()
    {
        return m_lConfirmFirst;
    }
    @Override
    public void setConfirmFirst(boolean tlConfirmFirst)
    {
        m_lConfirmFirst = tlConfirmFirst;        
    }
    
    /**
     * Sets if the result should be stored in the CommandManager for later use
     * @param tlStore true to store after execution of this command
     */
    public void setStoreResult(boolean tlStore)
    {
        m_lStoreResult = tlStore;
    }
    
    
    /**
     * Gets if the result of this command should be stored after execution for future use
     * @return true means the command will be stored
     */
    public boolean getStoreResult()
    {
        return m_lStoreResult;
    }

    /**
     * Checks if the command has started executing yet
     * @return true if the command has started executing otherwise false
     */
    @Override
    public boolean isExecuting()
    {
        return m_lIsExecuting;
    }

    /**

     */

    /**
     * Will set the timeout for the command, if the command is already running, then this count
     * will start from the time the method is called, if the command is not running yet, then this
     * command will be based on when the command is actually executed
     *
     * @param tnMillis the number of milliseconds to wait before the command times out
     */
    @Override
    public void setTimeout(long tnMillis)
    {
        resetTimeoutTimer();
        m_nTimeout = m_nStartTime + tnMillis;
        prepareTimeoutTimer();
    }

    /**
     * Gets the time this command started executing
     * @return the time the command started executing
     */
    @Override
    public long getStartTime()
    {
        return m_nStartTime;
    }

    /**
     * Gets the time the command stopped executing
     * @return the time the command stopped executing
     */
    @Override
    public long getEndTime()
    {
        return m_nEndTime;
    }

    /**
     * The amount of time this command was running for
     * @return the amount of time the command was running for
     */
    @Override
    public long getRunningTime()
    {
        return m_nStartTime - (m_nEndTime > 0 ? m_nEndTime : new Date().getTime());
    }

    /**
     * resets the timeout Timer if required
     */
    private void resetTimeoutTimer()
    {
        if (m_nTimeout >= 0 && m_oTimer != null)
        {
            m_oTimer.cancel();
            m_oTimer = null;
        }
    }

    /**
     * Prepares the timeout for the command
     */
    private void prepareTimeoutTimer()
    {
        if (m_nTimeout > 0)
        {
            if (m_oTimer == null)
            {
                m_oTimer = new Timer(m_cID + "_Timout");
            }

            m_oTimer.schedule(new TimerTask() {

                @Override
                public void run()
                {
                    cancel();
                }
            }, m_nTimeout);
        }
    }


    /**
     * Checks if the command has been registered with the proper session
     * @return true if the command has been registered, false otherwies
     */
    @Override
    public boolean isRegistered()
    {
        return (m_oSession == null) ? m_lIsExecuting : m_oSession.isRegistered(this);
    }

    
    
    /**
     * Gets the current priority
     *
     * @return  the current priority
     */
    @Override
    public int getPriority()
    {
        return m_nPriority;        
    }
    
    /**
     * Gets the progress of the current command.  This should be set in doExecute by the developer
     * periodically if the command is a long command.  If it is not set, it will return 0 until
     * the command is complete at which time it will return 100
     * @return the percentage of progress.
     */
    @Override
    public float getProgress()
    {
        return m_nProgress;
    }
    
    /**
     * Gets the name of the command
     *
     * @return the Name of the command
     */
    @Override
    public String getName()
    {
        return m_cName;        
    }
    
    @Override
    public void setName(String tcName)
    {
        m_cName = tcName;
    }

    @Override
    public String getMessage()
    {
        return (Goliath.Utilities.isNullOrEmpty(m_cMessage) ? this.getName() : m_cMessage);
    }

    @Override
    public void setMessage(String tcValue)
    {
        m_cMessage = tcValue;
    }
    
    /**
     * Sets the progress of this command.  If tnProgress is less than 0 or greater than 100, nothing will happen 
     *
     * @param  tnProgress   the percentage of progress, should be between 0 and 100
     */
    public void setProgress(float tnProgress)
    {
        if (tnProgress >= 0 && tnProgress <= 100 && m_nProgress != tnProgress)
        {
            m_nProgress = tnProgress;
            fireEvent(CommandEventType.ONPROGRESSCHANGED(), new Event<ICommand<A, T>>(this));

            // If the progress is successfully set, we will also refresh the session as it may be we are still
            // running the command
            getSession().renew();
        }
    }
    
    /**
     * Cancels the command.  It is up to the developer to check isCancelled between statements in the 
     * doExecute.  If isCancelled() is true the developer should return from doExecute and return null
     */
    @Override
    public void cancel()
    {
        if (m_lCancelled != true)
        {
            m_lCancelled = true;
            fireEvent(CommandEventType.ONCANCELLED(), new Event<ICommand<A, T>>(this));
        }
    }
    
    /**
     * Checks if the command is cancelled.  The developer should check this between statements during the
     * doExecute.  If it is true the developer should clean up and return null from doExecute
     *
     * @return true if the command has been cancelled.
     */
    @Override
    public boolean isCancelled()
    {
        return m_lCancelled;
    }
    
    /**
     * Registers the command with the application. 
     * If the command is registered multiple times, it will be run multiple times
     */
    @Override
    public final void registerCommand()
    {
        Goliath.Applications.Application.getInstance().registerCommand(this);
    }
    
    /**
     * Sets the priority of this command.
     *
     * @param  tnPriority         the new priority value
     * @throws  Goliath.Exceptions.InvalidParameterException if the priority value is out of range
     */
    public void setPriority(int tnPriority)
        throws Goliath.Exceptions.InvalidParameterException
    {
        if (tnPriority != m_nPriority && tnPriority >= 0)
        {
            m_nPriority = tnPriority;
        }
        else
        {
            if (tnPriority < 0)
            {
                throw new Goliath.Exceptions.InvalidParameterException("tnPriority");
            }
        }
    }
    
    
    /**
     * initialiseComponent starts the initialisation of the object
     */
    private void initialiseComponent()
    {
        m_cID = Goliath.Utilities.generateStringGUID();
        // Set the name if required
        if (Goliath.Utilities.isNullOrEmpty(m_cName))
        {
            m_cName = this.getClass().getSimpleName();
        }
        
        onInitialise();
    }
    
    @Override
    public String getID()
    {
        return m_cID;
    }
    
    
    @Override
    public void setID(String tcID)
    {
        m_cID = tcID;
    }
    
    /**
     * onInitialise can be overridden in sub classes for any initialisation that needs to be completed.
     */            
    protected void onInitialise()
    {         
    }
    
    /**
     * Adds an exception to the error collection
     * @param toException the error to add 
     * @return true if the collection was chaged due to a call to this class
     */
    protected boolean addError(Throwable toException)
    {
        return getErrors().add(toException);
    }
    
    /**
     * Sets the command back to its unexecuted state
     */
    protected void resetCommand()
    {
        getErrors().clear();
        m_oReturn = null;
        resetTimeoutTimer();
        m_lCancelled = false;
        m_nStartTime = 0;
        m_nEndTime = 0;
        setProgress(0);

        fireEvent(CommandEventType.ONRESET(), new Event<ICommand<A, T>>(this));
    }
    
    /**
     * Executes the command with no arguments
     *
     * @return  returns  a value from doExecute
     */
    @Override
    public boolean execute()
    {
        return execute(m_oArgs);
    }
            
    
    /**
     * Executes the command
     *
     * @param  taArgs   a list of parameters
     * @return  returns  a value from doExecute
     */
    @Override
    public boolean execute(A toArgs)
    {
        // Check if the command was already started
        if (m_nProgress > 0 &&  m_nProgress < 100)
        {
            return false;
        }
        else
        {
            this.setProgress(0);
        }
      
        boolean llReturn = true;

        if (toArgs != null)
        {
            setArguments(toArgs);
        }

        // Set progress as the command is starting
        try
        {
            Application.getInstance().log("Session[" + getSession().getSessionID() + "] Processing Command - " + this.getName() + "[" + this.getID() + "]", LogType.TRACE());
            m_lIsExecuting = true;
            m_nStartTime = new Date().getTime();
            fireEvent(CommandEventType.ONSTARTED(), new Event<ICommand<A, T>>(this));

            prepareTimeoutTimer();

            setReturnValue(doExecute());
        }
        catch (Throwable e)
        {
            addError(new Goliath.Exceptions.Exception(e));
            llReturn = false;
        }
        finally
        {
            resetTimeoutTimer();

            m_lIsExecuting = false;
            setProgress(100);
            
            m_nEndTime = new Date().getTime();
            fireEvent(CommandEventType.ONCOMPLETE(), new Event<ICommand<A, T>>(this));
            
            // The command has completed execution, so remove the arguments
            m_oArgs = null;

            Application.getInstance().log("Completed Command - " + this.getName() + "[" + this.getID() + "]", LogType.TRACE());
        }
        return llReturn;   
    }
    
    /**
     * Stops the current thread until this command is completed
     */
    @Override
    public void waitToComplete()
    {
        while (!this.isComplete())
        {
            try
            {
                Goliath.Threading.Thread.sleep(10);
            }
            catch(InterruptedException ex)
            {
                
            }
        }
    }
    
    /**
     * Sets the return value of this command
     * @param toValue the return value
     */
    protected void setReturnValue(T toValue)
    {
        m_oReturn = toValue;        
    }

    /**
     * Checks if the command is completed
     * @return true if the command is complete
     */
    @Override
    public boolean isComplete()
    {
        return m_nProgress == 100;
    }

    @Override
    public boolean hasErrors()
    {
        return m_oErrors != null && m_oErrors.size() > 0;
    }


    
    /**
     * Gets a list of errors that have occured while this command executed
     * @return a list of errors that have occurred
     */
    @Override
    public Goliath.Collections.List<Throwable> getErrors()
    {
        if (m_oErrors == null)
        {
            m_oErrors = new List<Throwable>(0);
        }
        return m_oErrors;
    }
    
    @Override
    public void setArguments(A toArgs)
    {
        this.m_oArgs = toArgs;
    }

    @Override
    public A getArguments()
    {
        return m_oArgs;
    }


    /**
     * Gets the value from this command
     * @return the value from the command
     * @throws Goliath.Exceptions.ProcessNotComplete if this is called before the command has completed
     */
    @Override
    public T getResult() throws ProcessNotComplete
    {
        // Ensure the command is complete first
        if (!isComplete())
        {
            throw new Goliath.Exceptions.ProcessNotComplete("The command has not been completed yet");
        }
        return m_oReturn;                
    }

    /**
     * Gets the result of the command, if the command is not yet complete, this method will continue checking the result
     * for tnTimeout milliseconds and then throw an error if the result is still not available
     * @param tnTimeout the number of milliseconds to wait before timing out the operation
     * @return the result of the command
     * @throws Goliath.Exceptions.ProcessNotComplete if the command does not complete before the timeout
     */
    @Override
    public T getResult(long tnTimeout)
            throws ProcessNotComplete
    {
        if (!isComplete() && !isCancelled())
        {
            try
            {
                Thread.sleep((tnTimeout > 50) ? 50 : tnTimeout);
            }
            catch (InterruptedException ex)
            {}
            return (tnTimeout >0) ? getResult(tnTimeout-50) : getResult();
        }
        return m_oReturn;
    }
    
    
    /**
     * The actual command code resides in this method 
     * @param taArgs a list of paramters
     * @return a value from the doExecute
     * @throws java.lang.Throwable for any errors that occur
     */
    public abstract T doExecute() throws Throwable;
    
    
    @Override
    public final void setSession(Goliath.Interfaces.ISession toSession)
    {
        if (m_oSession != toSession)
        {
            m_oSession = toSession;
            fireEvent(CommandEventType.ONSESSIONCHANGED(), new Event<ICommand<A, T>>(this));
        }
    }
    
    
    @Override
    public final Goliath.Interfaces.ISession getSession()
    {
        if (m_oSession != null)
        {
            return m_oSession;
        }
        return Session.getCurrentSession();
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return this.getClass().getSimpleName() + " - " + this.getName();
    }



    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Command<A, T> other = (Command<A, T>) obj;
        if (!this.m_cID.equals(other.m_cID) && (this.m_cID == null || !this.m_cID.equals(other.m_cID)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 59 * hash + (this.m_cName != null ? this.m_cName.hashCode() : 0);
        return hash;
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
    public boolean removeEventListener(CommandEventType toEvent, IDelegate toCallback)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.removeEventListener(toEvent, toCallback) : false;
    }

    @Override
    public boolean hasEventsFor(CommandEventType toEvent)
    {
        return (m_oEventDispatcher != null) ? m_oEventDispatcher.hasEventsFor(toEvent) : false;
    }

    @Override
    public final void fireEvent(CommandEventType toEventType, Event<ICommand<A, T>> toEvent)
    {
        if (m_oEventDispatcher == null)
        {
            return;
        }
        m_oEventDispatcher.fireEvent(toEventType, toEvent);
    }

    @Override
    public boolean clearEventListeners(CommandEventType toEvent)
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
    public boolean addEventListener(CommandEventType toEvent, IDelegate toCallback)
    {
        if (m_oEventDispatcher == null)
        {
            m_oEventDispatcher = new EventDispatcher<CommandEventType, Event<ICommand<A, T>>>();
        }
        return m_oEventDispatcher.addEventListener(toEvent, toCallback);
    }
    
    
    
    
}
