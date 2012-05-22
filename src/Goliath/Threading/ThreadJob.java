/* =========================================================
 * ThreadJob.java
 *
 * Author:      kmchugh
 * Created:     03-Mar-2008, 12:37:02
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
package Goliath.Threading;

import Goliath.Arguments.Arguments;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 03-Mar-2008
 * @author      kmchugh
 **/
public abstract class ThreadJob<T extends Arguments> implements Runnable
{
    private T m_oCommandArgs;
    
    public ThreadJob(T toCommandArgs)
    {
        m_oCommandArgs = toCommandArgs;
    }
    
    public final void run()
    {
        onRun(m_oCommandArgs);
    }
    
    protected abstract void onRun(T toCommandArgs);
}
   
