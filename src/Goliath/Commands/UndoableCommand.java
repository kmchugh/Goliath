/* =========================================================
 * UndoableCommand.java
 *
 * Author:      kmchugh
 * Created:     17-Jan-2008, 03:12:55
 * 
 * Description
 * --------------------------------------------------------
 * A command that can be undone
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Commands;

import Goliath.Arguments.Arguments;

/**
 * A command that can be undone
 *
 * @param T     The return type for the command
 * @version     1.0 17-Jan-2008
 * @author      kmchugh
**/
public abstract class UndoableCommand<A extends Arguments, T> extends Goliath.Commands.Command<A, T>
        implements Goliath.Interfaces.Commands.IUndoableCommand<A, T>
{

    public UndoableCommand(int tnPriority)
            throws Goliath.Exceptions.InvalidParameterException
    {
        super(tnPriority);
    }

    public UndoableCommand(boolean tlRegister, int tnPriority)
            throws Goliath.Exceptions.InvalidParameterException
    {
        super(tlRegister, tnPriority);
    }

    public UndoableCommand(boolean tlRegister)
    {
        super(tlRegister);
    }

    public UndoableCommand()
    {
        super();
    }

    public boolean undo()
    {
        return onUndo();
    }
    
    protected abstract boolean onUndo();
}
