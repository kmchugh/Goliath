/* =========================================================
 * Module.java
 *
 * Author:      kmchugh
 * Created:     09-Apr-2008, 20:37:13
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

import Goliath.Constants.StringFormatType;
import Goliath.Property;
import Goliath.Applications.Startup;

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
public abstract class Module extends Startup implements Goliath.Interfaces.Applications.IModule
{
    /** Creates a new instance of Module */
    protected Module()
    {
    }

    @Override
    protected String onGetPropertyPath()
    {
        return "Application.Settings.Modules";
    }
    
    

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Module other = (Module) obj;
        if (this.getID() != null && !this.getID().equals(other.getID()))
        {
            return false;
        }
        if (this.getID() == null && other.getID() != null)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 71 * hash + (this.getID() != null ? this.getID().hashCode() : 0);
        hash = 71 * hash + (this.getID() != null ? this.getID().hashCode() : 0);
        return hash;
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return getID();                
    }
    
    @Override
    protected abstract String onGetName();
    
    @Override
    public final String getImageSource()
    {
        String lcReturn = onGetImageSource();
        if (lcReturn == null)
        {
            lcReturn = Application.getInstance().getPropertyHandlerProperty(getPropertyPath() + ".DefaultModuleImage", "./images/DefaultModule.jpg");
        }
        return lcReturn;
    }
    
    @Override
    public final String getDefaultContext()
    {
        String lcReturn = onGetDefaultContext();
        if (lcReturn == null)
        {
            lcReturn = Application.getInstance().getPropertyHandlerProperty(getPropertyPath() + "." + getClass().getSimpleName().replaceAll(" ", "") + ".DefaultContext", "/" + getClass().getSimpleName().replaceAll(" ", "") + "/index.gsp");
        }
        return lcReturn;
    }
    
    protected String onGetDefaultContext()
    {
        return null;
    }
    
    protected String onGetImageSource()
    {
        return null;
    }
    
    @Override
    public abstract String getID();
    
    @Override
    public abstract String getDescription();
}
