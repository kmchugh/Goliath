/* =========================================================
 * ForceGarbageCollection.java
 *
 * Author:      kenmchugh
 * Created:     Apr 16, 2010, 2:43:29 PM
 *
 * Description
 * --------------------------------------------------------
 * <Description>
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Commands;

/**
 *
 * @author kenmchugh
 */
public class ForceGarbageCollection extends Command
{

    @Override
    public Object doExecute() throws Throwable
    {
        System.gc();
        
        return null;
    }

    

}
