/* =========================================================
 * Thread.java
 *
 * Author:      Ken McHugh
 * Created:     Jan 16, 2008, 4:20:57 AM
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

import Goliath.Applications.Application;

/**
 * Thread class for multi thread management and logging.  This should be used
 * instead of the java.lang.Thread as logging and statistics functions are build
 * in here.
 *
 * @version     1.0 Jan 16, 2008
 * @author      Ken McHugh
**/
public class Thread extends java.lang.Thread
{
    public Thread(ThreadGroup group, Runnable target, String name, long stackSize) 
    {
        super(group, target, name, stackSize);
    }

    public Thread(ThreadGroup group, Runnable target, String name) 
    {
        super(group, target, name);
    }

    public Thread(Runnable target, String name) 
    {
        super(target, name);
    }

    public Thread(ThreadGroup group, String name) 
    {
        super(group, name);
    }
    
    public Thread(String name) 
    {
        super(name);
    }

    public Thread(ThreadGroup group, Runnable target) 
    {
        super(group, target);
    }

    public Thread(Runnable target) 
    {
        super(target);
    }

    public Thread() 
    {
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return super.getContextClassLoader();
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public boolean isInterrupted() {
        return super.isInterrupted();
    }

    @Override
    public void run() 
    {
        try
        {
            super.run();
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public String toString() {
        return super.toString();
    }
    
    
        
}
