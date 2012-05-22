/* =========================================================
 * ICommand.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 4:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This interface represents a Command.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.Commands;

import Goliath.Arguments.Arguments;
import Goliath.Commands.CommandEventType;
import Goliath.Event;
import Goliath.Exceptions.ProcessNotComplete;
import Goliath.Interfaces.IEventDispatcher;

/**
 * This interface represents a Command
 * @param A     The type of command arguments the command can take
 * @param T     The return type for the execute method
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public interface ICommand<A extends Arguments , T>
        extends IEventDispatcher<CommandEventType, Event<ICommand<A, T>>>
{
    // TODO: This should be A extends Goliath.Interfaces.Commands.CommandArgs
     /**
     * Executes the command
      * @param taArgs   a list of arguments
      * @return true if the command completed successfully
      */
    boolean execute(A taArgs);

    /**
    * Executes the command using the null command args object
    * @return true if the command completed successfully
    */
    boolean execute();
    
    /**
     * Checks if the command is cancelled.  The developer should check this between statements during the
     * doExecute.  If it is true the developer should clean up and return null from doExecute
     *
     * @return true if the command has been cancelled.
     */
    boolean isCancelled();
    
    /**
     * Gets the progress of the current command.  This should be set in doExecute by the developer
     * periodically if the command is a long command.  If it is not set, it will return 0 until
     * the command is complete at which time it will return 100
     * @return the percentage of progress.
     */
    float getProgress();
    
    /**
     * Cancels the command.  It is up to the developer to check isCancelled between statements in the 
     * doExecute.  If isCancelled() is true the developer should return from doExecute and return null
     */
    void cancel();
    
    /**
     * gets the priority of this command 
     *
     * @return  the priority of the command
     */
    int getPriority();
    
    /**
     * Registers the command with the application.
     * If the command is registered multiple times,
     * the command will be executed multiple times
     */
    void registerCommand();
    
    /**
     * gets the name of this command 
     *
     * @return  the name of the command
     */
    String getName();
    void setName(String tcName);

    String getMessage();
    void setMessage(String tcValue);
    
    /**
     * Returns the result from the command
     * @return the result from the command executing
     * @throws Goliath.Exceptions.ProcessNotComplete when this is called before the command has completed
     */
    T getResult() throws ProcessNotComplete;

    /**
     * Gets the result of the command, if the command is not yet complete, this method will continue checking the result
     * for tnTimeout milliseconds and then throw an error if the result is still not available
     * @param tnTimeout the number of milliseconds to wait before timing out the operation
     * @return the result of the command
     * @throws Goliath.Exceptions.ProcessNotComplete if the command does not complete before the timeout
     */
    T getResult(long tnTimeout) throws ProcessNotComplete;
    
    /**
     * Checks if the command is completed
     * @return true if the command is complete
     */
    boolean isComplete();
    
    /**
     * Gets a list of errors that have occured while this command executed
     * @return a list of errors that have occurred
     */
    Goliath.Collections.List<Throwable> getErrors();
    
    /**
     * Gets the id for this session
     * @return the session id
     */
    String getID();
    void setID(String tcID);
    
    /**
     * Stops the current thread until this command is completed
     */
    void waitToComplete();
    
    /**
     * Sets the arguments for when execute is called on this command
     * @param toArgs the Arguments for the command
     */
    void setArguments(A toArgs);

    /**
     * Gets the arguments for this command if they have been set
     * @return the arguments, or null if not yet set
     */
    A getArguments();
    
    /**
     * Sets the session for this commmand
     * @param toSession the session to use for this command
     */
    void setSession(Goliath.Interfaces.ISession toSession);

    /**
     * Checks if this command has any errors
     * @return true if there were errors in this command
     */
    boolean hasErrors();
    
    /**
     * Gets the session for this command
     * @return The session that this command runs under
     */
    Goliath.Interfaces.ISession getSession();

    /**
     * Checks if the command has started executing yet
     * @return true if the command has started executing otherwise false
     */
    public boolean isExecuting();


    /**
     * Checks if the command has been registered with the proper session
     * @return true if the command has been registered, false otherwies
     */
    public boolean isRegistered();

    /**
     * Will set the timeout for the command, if the command is already running, then this count
     * will start from the time the method is called, if the command is not running yet, then this
     * command will be based on when the command is actually executed
     *
     * @param tnMillis the number of milliseconds to wait before the command times out
     */
    void setTimeout(long tnMillis);

    /**
     * Gets the time this command started executing
     * @return the time the command started executing
     */
    long getStartTime();

    /**
     * Gets the time the command stopped executing
     * @return the time the command stopped executing
     */
    long getEndTime();

    /**
     * The amount of time this command was running for
     * @return the amount of time the command was running for
     */
    long getRunningTime();
    
    /**
     * Sets if the result should be stored in the CommandManager for later use
     * @param tlStore true to store after execution of this command
     */
    void setStoreResult(boolean tlStore);
    
    /**
     * Gets if the result of this command should be stored after execution for future use
     * @return true means the command will be stored
     */
    boolean getStoreResult();

    
    /**
     * If this is true then the user should have to confirm this command before it runs
     * @return true if the user should have to confirm this command
     */
    boolean getConfirmFirst();
    void setConfirmFirst(boolean tlConfirmFirst);
    String getConfirmMessage();
    void setConfirmMessage(String tcMessage);
}
