/* =========================================================
 * DynamicClassLoader.java
 *
 * Author:      kmchugh
 * Created:     29-May-2008, 09:35:04
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
package Goliath.DynamicCode;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import Goliath.Exceptions.InvalidPathException;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 29-May-2008
 * @author      kmchugh
 **/
public class DynamicClassLoader extends URLClassLoader
        implements Goliath.Interfaces.DynamicCode.IClassLoader
{

    private Goliath.Collections.List<String> m_cURLs = new Goliath.Collections.List<String>();

    /**
     * Creates an instance of the class loader
     */
    public DynamicClassLoader()
    {
        super(new URL[]{}, DynamicClassLoader.class.getClassLoader());
    }
    
    @Override
    public void addFile(File toFile)
             throws InvalidPathException
    {
        if (toFile.exists())
        {
            try
            {
                if (toFile.getName().matches("(?i).+\\.jar$"))
                {
                    addPath(toFile.getAbsolutePath());
                }
                else
                {
                    this.addURL(toFile.toURI().toURL());
                }
            }
            catch (MalformedURLException e)
            {
                throw new InvalidPathException("Invalid class path " + toFile.getAbsolutePath(), e);
            }
        }
    }

    @Override
    public void addPath(String tcURL) throws InvalidPathException
    {
        try
        {
            File loFile = new File(tcURL);
            if(loFile.exists())
            {
                if (loFile.getName().matches("(?i).+\\.jar$"))
                {
                    this.addURL(new URL("jar:" + loFile.toURI() + "!/"));
                }
                else
                {
                    this.addURL(loFile.toURI().toURL());
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new InvalidPathException("Invalid class path " + tcURL, e);
        }
    }

    @Override
    public void addURL(URL toURL)
    {
        if (!m_cURLs.contains(toURL.toExternalForm().toLowerCase()))
        {
            m_cURLs.add(toURL.toExternalForm().toLowerCase());
            super.addURL(toURL);
        }
    }

    @Override
    public Class<?> loadClass(String tcName) throws ClassNotFoundException
    {
        Class<?> loClass = null;
        // First try the system class loader
        try
        {
            loClass = this.getSystemClassLoader().loadClass(tcName);
        }
        catch (ClassNotFoundException ex)
        {
            loClass = super.loadClass(tcName, true);            
        }
        return loClass;
    }
    
    
    
    // TODO: implement reloadable class http://exampledepot.com/egs/java.lang/ReloadClass.html
    // TODO: Implement find resources in order to compile all graphics, sounds into a binary file and retrieve from here
}
