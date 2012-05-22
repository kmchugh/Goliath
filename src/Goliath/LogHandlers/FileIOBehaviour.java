/* =========================================================
 * FileIOBehaviour.java
 *
 * Author:      pstanbridge
 * Created:     13-Dec-2007, 15:29:24
 * 
 * Description
 * --------------------------------------------------------
 * This is a concrete IOBehaviour for writing plain text
 * logging
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.LogHandlers;

import Goliath.Applications.Application;
import Goliath.Environment;
import Goliath.Exceptions.InvalidIOException;
import Goliath.IO.FileChannel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 14-Dec-2007
 * @author      Peter
 **/
public class FileIOBehaviour extends DefaultLogIOBehaviour
{
    private FileChannel m_oChannel;

    /** Constructor creates new file if it doesn't exist
     * @param tlAppend - whether or not to append new messages to an existing file.
     *                   If the file does not already exist a new one will be created regardless
     * @param tcFile - the file name (including path)
     * @throws Goliath.Exceptions.InvalidIOException
     */
    public FileIOBehaviour(boolean tlAppend, String tcFile)
    {
        // create and/or open file
        try
        {
            File loFile = new File(tcFile);
            Goliath.IO.Utilities.File.create(loFile);
            // Ensure the file exists
            m_oChannel = new Goliath.IO.FileChannel(new FileOutputStream(loFile, true).getChannel());
            printHeader();
        }
        catch (FileNotFoundException ex)
        {
            // This should not happen as we are allowing creation of the file if it does not exist
        }
        catch (InvalidIOException ex)
        {
            // Because this is in the log, we don't want to send this to the log again.
            System.err.print(ex.getMessage());
        }
        catch (IOException ex)
        {
            System.err.print(ex.getMessage());
        }
    }
    
    /** Log an error
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logFatal(Date tdDate, String tcMessage)
    {
        String lcLogMessage ="Fatal ---  " + Goliath.Utilities.Date.toString(tdDate) + "  " + tcMessage;
        log(lcLogMessage);
        System.err.println(lcLogMessage);
    }

    /** Log an error
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logError(Date tdDate, String tcMessage)
    {
        String lcLogMessage ="Error ---  " + Goliath.Utilities.Date.toString(tdDate) + "  " + tcMessage;
        log(lcLogMessage);
        System.err.println(lcLogMessage);
    }

    /** Log an warning
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logWarning(Date tdDate, String tcMessage)
    {
        String lcLogMessage ="Warning ---  " + Goliath.Utilities.Date.toString(tdDate) + "  " + tcMessage;
        log(lcLogMessage);
    }

    /** Log an event
     * 
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logEvent(Date tdDate, String tcMessage)
    {
        String lcLogMessage ="Event ---  " + Goliath.Utilities.Date.toString(tdDate) + "  " + tcMessage;
        log(lcLogMessage);
    }
    
    /** Log a trace
     * 
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logTrace(Date tdDate, String tcMessage)
    {
        String lcLogMessage = "Trace ---  " + Goliath.Utilities.Date.toString(tdDate) + "  " + tcMessage;
        log(lcLogMessage);
    }
    
    /** Log a debug
     * 
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logDebug(Date tdDate, String tcMessage)
    {
        String lcLogMessage ="Debug ---  " + Goliath.Utilities.Date.toString(tdDate) + "  " + tcMessage;
        log(lcLogMessage);
    }

    /** Print header to log file
     */
    private void printHeader()
    {
        try
        {
            // TODO: Load all version numbers and display in the log as well.
            m_oChannel.write("*****************************************************************************" + Environment.NEWLINE() +
                        "   F I L E   L O G   H A N D L E R   S T A R T E D   " + Goliath.Utilities.Date.toString(new Date())+ Environment.NEWLINE() +
                        "-----------------------------------------------------------------------------"+ Environment.NEWLINE() +
                        "   Application " + Application.getInstance().getName() + "(" + Application.getInstance().getGUID() + ")" + Environment.NEWLINE() +
                        "   Java Class Version " + System.getProperty("java.class.version")+ Environment.NEWLINE() +
                        "   Java Vendor " + System.getProperty("java.vendor")+ Environment.NEWLINE() +
                        "   Java Vendor URL " + System.getProperty("java.vendor.url")+ Environment.NEWLINE() +
                        "   Java Vendor URL " + System.getProperty("java.vendor.url")+ Environment.NEWLINE() +
                        "   Java Operating System " + System.getProperty("os.name")+ Environment.NEWLINE() +
                        "   OS Architecture " + System.getProperty("os.arch")+ Environment.NEWLINE() +
                        "   Available Processors: " + Integer.toString(Runtime.getRuntime().availableProcessors()) + Environment.NEWLINE() +
                        "   Memory (used/total  :  max): " + Long.toString(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "/" + Long.toString(Runtime.getRuntime().totalMemory()) + "  :  " + Long.toString(Runtime.getRuntime().maxMemory())+ Environment.NEWLINE() +
                        "*****************************************************************************" + Environment.NEWLINE());
        }
        catch (IOException ex)
        {
            System.err.print(ex.getMessage());
        }
    }

    


    /**
     * Write message to log
     *
     * @param  tcMessage Message being logged
     */
    private void log(String tcMessage)
    {
        try
        {
            m_oChannel.write(tcMessage + Environment.NEWLINE());
        }
        catch (IOException ex)
        {
            System.err.print(ex.getMessage());
        }
    }
}
    



