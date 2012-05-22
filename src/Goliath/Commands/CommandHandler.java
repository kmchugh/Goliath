/* =========================================================
 * CommandHandler.java
 *
 * Author:      kmchugh
 * Created:     09-Apr-2008, 13:56:18
 * 
 * Description
 * --------------------------------------------------------
 * General Class Description.
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
import Goliath.Exceptions.InvalidParameterException;
import Goliath.Interfaces.Commands.ICommand;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 09-Apr-2008
 * @author      kmchugh
**/
public class CommandHandler extends Goliath.Object implements Goliath.Interfaces.Commands.ICommandHandler
{
    private Class<?> m_oInterface = null;
    
    /** Creates a new instance of CommandHandler */
    public CommandHandler(Class<?> toInterface)
    {
        m_oInterface = toInterface;
        if (Goliath.Applications.Application.getInstance().getObjectCache().getObjectCount(toInterface) == 0)
        {
            // TODO: look into if the loadobjects should check if the objects are loaded first.
            Goliath.Applications.Application.getInstance().getObjectCache().loadObjects(toInterface, "getName");
        }
    }

    @Override
    public ICommand<?, ?> handleCommand(String tcName, Arguments toArgs, Goliath.Interfaces.ISession toSession)
    {
        // Check if any commands use this name
        ICommand<?, ?> loCommand = getCommandHandler(tcName, m_oInterface, toArgs);
        if (loCommand != null)
        {
            loCommand.setSession(toSession);
            toSession.addCommand(loCommand);
        }
        return loCommand;
    }
    
    public <T extends Arguments> ICommand<?, ?> getCommandHandler(String tcCommand, Class toInterface, T toArgs)
    {
        List<ICommand<T, ?>> loCommandObjects = (List<ICommand<T, ?>>)Application.getInstance().getObjectCache().getObject(m_oInterface, tcCommand);
        if (loCommandObjects != null && loCommandObjects.size() > 0)
        {
            ICommand<T, ?> loCommand = loCommandObjects.get(0);
            try
            {
                // TODO: Make this not require a new instance (flyweight)
                loCommand = loCommand.getClass().newInstance();
                loCommand.setArguments(toArgs);
            }
            catch (InstantiationException ex)
            {
                new Goliath.Exceptions.Exception(ex);
            }
            catch (IllegalAccessException ex)
            {
                new Goliath.Exceptions.Exception(ex);
            }
            return loCommand;
        }
        throw new InvalidParameterException("No command handler available for the command " + tcCommand, "tcCommand", tcCommand);
    }
}
