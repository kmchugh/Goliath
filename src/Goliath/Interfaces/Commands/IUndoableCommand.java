/* =========================================================
 * IUndoableCommand.java
 *
 * Author:      kmchugh
 * Created:     17-Jan-2008, 03:10:21
 * 
 * Description
 * --------------------------------------------------------
 * Represents a command that is undoable
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Interfaces.Commands;

import Goliath.Arguments.Arguments;

/**
 * Represents a command that is undoable
 *
 * @param T     The return type of the execution of the command
 * @version     1.0 17-Jan-2008
 * @author      kmchugh
**/
public interface IUndoableCommand<A extends Arguments, T> extends Goliath.Interfaces.Commands.ICommand<A, T>
{
    /**
     * Undoes the command and returns true if the command was successfully undone
     * @return true if the command was undone correctly.
     */
    boolean undo();
}
