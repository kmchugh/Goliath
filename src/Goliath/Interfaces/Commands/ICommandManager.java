/* =========================================================
 * ICommandManager.java
 *
 * Author:      kmchugh
 * Created:     26 November 2007, 16:52
 *
 * Description
 * --------------------------------------------------------
 * Used to manage the commands
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Interfaces.Commands;

import Goliath.Collections.HashTable;

/**
 * Used by the application to register and execute commands
 *
 * @version     1.0 26 November 2007
 * @author      kmchugh
 **/
public interface ICommandManager
{
    /**
     * Gets the pause between completing current commands and checking for new commands
     * @return the amount of time in milliseconds
     */
    int getThreadDelay();
    
    /**
     * Check if this command manager contains the specified command
     * @param toCommand the command that we are checking for
     * @return true if the command is in this command manager, otherwise false
     */
    boolean contains(ICommand toCommand);
    
    /**
     * Sets the pause between completing current commands and checking for new commands
     * @param tnThreadDelay the new time in milliseconds
     */
    void setThreadDelay(int tnThreadDelay);
    
    /**
     * Gets the number of commands that will be processed before relinquishing control
     * If there are less commands to process then control will be returned after processing them
     * @return the number of commands
     */
    int getCommandProcessCount();
    
    
    /**
     * Gets the number of commands that will be processed before relinquishing control
     * If there are less commands to process then control will be returned after processing them
     * @param tnCommandProcessCount the number of commands 
     */
    void setCommandProcessCount(int tnCommandProcessCount);
            
    /**
     * deactivates and unload application settings
     */
    void deactivate();
    
    /**
     * Activates and loads application settings
     */
    void activate();

    /**
     * Returns true if the command manager has commands to process
     * @return true if there are commands to process, false otherwise
     */
    boolean hasCommands();
    
    /**
     * Checks if the settings are currently active
     * @return true if the settings are active
     */
    boolean isActive();
    
    /**
     * Adds a command to the command manager
     *
     * @param  toCommand the command to add
     */
    void addCommand(Goliath.Interfaces.Commands.ICommand<?, ?> toCommand);
    
    void pause();
    void resume();
    
    /**
     * Adds a completed command to the completed list of commands
     * @param toCommand
     */
    public void addCompletedCommand(ICommand<?, ?> toCommand);
    
    public Goliath.Collections.HashTable<ICommand<?, ?>, java.lang.Object> getCommandResults();
    
    HashTable<String, Float> getCommandStatus();
    
    java.lang.Object popCommandResult(ICommand<?, ?> toCommand);

}
