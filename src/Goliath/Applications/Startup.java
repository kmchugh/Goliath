/* =========================================================
 * Startup.java
 *
 * Author:      Ken McHugh
 * Created:     Feb 19, 2008, 7:23:50 PM
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

package Goliath.Applications;

import Goliath.Constants.LogType;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 19, 2008
 * @author      Ken McHugh
**/
public abstract class Startup 
        extends Goliath.Object
        implements Goliath.Interfaces.IStartup
{
    /** Creates a new instance of Startup */
    protected Startup()
    {
    }
    
    protected final String getPropertyPath()
    {
        return onGetPropertyPath();
    }
    
    protected String onGetPropertyPath()
    {
        return "Application.Settings.Startups";
    }

    @Override
    public final String getName()
    {
        return onGetName();
    }
    
    protected String onGetName()
    {
        return getClass().getName();
    }
    
    private String getFullPropertyPath()
    {
        return getPropertyPath() + "." + getName().replaceAll("\\.", "_").replaceAll(" ", "_").replaceAll("<", "").replaceAll(">", "").replaceAll("/", "").replaceAll("\\(", "").replaceAll("\\)", "");
    }

    @Override
    public final boolean run()
    {
        if (getAllowRun())
        {
            try
            {
                boolean llReturn = true;
                if (this.isFirstRun())
                {
                    Application.getInstance().log("Executing Startup " + getName() + " for first run", LogType.EVENT());
                    llReturn = this.onFirstRun();
                    if (llReturn)
                    {
                        Application.getInstance().setPropertyHandlerProperty(getFullPropertyPath() + ".HasRun", llReturn);
                        Application.getInstance().log("Completed Executing Startup " + getName() + " for first run", LogType.TRACE());
                    }
                }
                if (llReturn)
                {
                    Application.getInstance().log("Executing Startup " + getName(), LogType.EVENT());
                    llReturn = llReturn && this.onRun();
                    Application.getInstance().log("Finished Executing Startup " + getName(), LogType.TRACE());
                }
                return llReturn;
            } 
            catch (Throwable ex)
            {
                Application.getInstance().log(new Goliath.Exceptions.Exception("Error executing startup class " + this.getName(), ex, false));
            }
        }
        return false;
    }
    
    /**
     * This will be run if this is the first time this startup module has been run
     * @return true if successful
     */
    protected abstract boolean onFirstRun();
    
    /**
     * This will be run every time this startup module is run
     * @return true if successful
     */
    protected abstract boolean onRun();
    
    /**
     * The ordering of startup modules
     * @return
     */
    @Override
    public final int getSequence()
    {
        return onGetSequence();
    }
    
    protected int getDefaultSequence()
    {
        return 100;
    }
    
    protected int onGetSequence()
    {
        int lnSequence = Application.getInstance().getPropertyHandlerProperty(getFullPropertyPath() + ".Sequence", getDefaultSequence());
        
        // Convert the sequence to a zero based index
        lnSequence = lnSequence -1;
        if (lnSequence < 0)
        {
            lnSequence = 0;
        }
        return lnSequence;
    }

    @Override
    public boolean isFirstRun()
    {
        return onIsFirstRun();
    }
    
    protected boolean onIsFirstRun()
    {
        return !Application.getInstance().getPropertyHandlerProperty(getFullPropertyPath() + ".HasRun", false);
    }
    
    private boolean getAllowRun()
    {
        return Application.getInstance().getPropertyHandlerProperty(getFullPropertyPath() + ".Include", true);
    }
    
    
}
