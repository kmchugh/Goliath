/* ========================================================
 * LoadClassesFromJarCommand.java
 *
 * Author:      kmchugh
 * Created:     Mar 12, 2011, 2:07:33 PM
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
 * ===================================================== */

package Goliath.Commands;

import Goliath.Applications.Application;
import Goliath.Arguments.SingleParameterArguments;
import Goliath.DynamicCode.Java;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.DynamicCode.IClassLoader;
import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Mar 12, 2011
 * @author      kmchugh
**/
public class LoadClassesFromJarCommand extends Command<SingleParameterArguments<File>, Object>
{
    /**
     * Creates a new instance of LoadClassesFromJarCommand
     */
    public LoadClassesFromJarCommand(SingleParameterArguments<File> toArguments)
    {
        this(true, toArguments);
    }

    /**
     * Creates a new instance of LoadClassesFromJarCommand
     */
    public LoadClassesFromJarCommand(boolean tlRegister, SingleParameterArguments<File> toArguments)
    {
        super(tlRegister, toArguments);
        if (tlRegister && !isRegistered())
        {
            this.getSession().start();
            this.getSession().addCommand(this);
        }
    }

    @Override
    public IList<Class> doExecute() throws Throwable
    {
        if (getArguments() != null)
        {
            // TODO: Find out while the arguments would be null
            File loJar = getArguments().getParameter();
            if (loJar != null && loJar.exists())
            {
                loadClassesFromJar(loJar);
            }
        }
        return null;
    }

    /**
     * Loads all of the classes from the jar file specified in to the cache
     * @param tcPathToJarFile the full path to the jar file
     */
    private synchronized static void loadClassesFromJar(File toJarFile)
    {
        // TODO: This needs to be optimised, need to ensure plugin classes get added to the correct classloader and can be unloaded
        if (Java.isJarLoaded(toJarFile))
        {
            return;
        }

        // Mark the jar as processed so it is not loaded again
        Java.addProcessedJar(toJarFile);


        // Create the JarFile reference
        try
        {
            JarFile loJarFile = new JarFile(toJarFile, true, JarFile.OPEN_READ);
            // Load the jar entries for this file
            Enumeration<JarEntry> loJarEntries = loJarFile.entries();
            while(loJarEntries.hasMoreElements())
            {
                JarEntry loEntry = loJarEntries.nextElement();
                String lcClassName = loEntry.getName();

                // We process only if this is a class definition
                if (lcClassName.toLowerCase().endsWith(".class"))
                {
                    // Build up the full class name
                    lcClassName = lcClassName.replaceAll("/", ".").replaceAll(".class", "");

                    // Get the class instance
                    java.lang.Class loClass = null;
                    IClassLoader loLoader = Goliath.Applications.Application.getInstance().getClassLoader();
                    try
                    {
                        loClass = loLoader.loadClass(lcClassName);
                    }
                    catch (Throwable ex)
                    {
                        // The class could not be created, so try using a class loader
                        try
                        {
                            // Try using the application class loader
                            loLoader.addPath(toJarFile.getAbsolutePath());
                            loClass = loLoader.loadClass(lcClassName);
                        }
                        catch (Throwable e)
                        {
                            System.err.println(e.getLocalizedMessage()  + " Could not be loaded");
                            loClass = null;
                        }
                    }

                    if (loClass != null)
                    {
                        Java.getClassDefinition(loClass);
                    }
                }
            }
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
        }
    }
}
