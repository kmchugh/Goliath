/* =========================================================
 * ICommandHandler.java
 *
 * Author:      kmchugh
 * Created:     09-Apr-2008, 13:55:20
 * 
 * Description
 * --------------------------------------------------------
 * General Interface Description.
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
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 09-Apr-2008
 * @author      kmchugh
**/
public interface ICommandHandler
{
    
    public ICommand handleCommand(String tcName, Arguments toArgs, Goliath.Interfaces.ISession toSession);

}
