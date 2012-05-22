/* =========================================================
 * FileIOBehaviour.java
 *
 * Author:      pstanbridge
 * Created:     10-Dec-2008, 13:28:24
 * 
 * Description
 * --------------------------------------------------------
 * This is a concrete IOBehaviour for writing XML Format
 * logging
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.LogHandlers;

import Goliath.Exceptions.InvalidIOException;
import javax.xml.stream.*;
import Goliath.Session;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
public class XMLIOBehaviour extends DefaultLogIOBehaviour
{

    private File m_oMsgFile;
    private String m_cFile;
    private XMLStreamWriter m_oWriter;
    private XMLOutputFactory m_oOutputFactory;
    private Timer m_oTimer;
    private TimerTask m_oTimerTask;

    /** Constructor creates new file if it doesn't exist
     * @param tlAppend - whether or not to append new messages to an existing file.
     *                   If the file does not already exist a new one will be created regardless
     *                   NOTE: Append is being ignored because XML Wellformedness
     * @param tcFile - the file name (including path)
     * @throws Goliath.Exceptions.InvalidIOException
     */
    public XMLIOBehaviour(boolean tlAppend, String tcFile)
    {
       
        // create and/or open file
        m_cFile = tcFile;
        try
        {
            openFile(false, m_cFile);
            m_oWriter.writeStartDocument();
            m_oWriter.writeStartElement("LogFile");
            printHeader();
            
        }
        catch (Exception e)
        {
            
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
       // setTimer();
    }
    
    /** Log an error
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logFatal(Date tdDate, String tcMessage) 
    {
        try
        {
        log("Fatal", Goliath.Utilities.Date.toString(tdDate), tcMessage);
        }
        catch (Exception e)
        {
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
      
    }

    /** Log an error
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logError(Date tdDate, String tcMessage)
    {
      
        try
        {
        log("Error",  Goliath.Utilities.Date.toString(tdDate), tcMessage);
        }
        catch (Exception e)
        {
       throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
     
        
    }

    /** Log an warning
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logWarning(Date tdDate, String tcMessage)
    {
        try
        {
        log("Warning",  Goliath.Utilities.Date.toString(tdDate), tcMessage);
        }
        catch (Exception e)
        {
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
    }

    /** Log an event
     * 
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logEvent(Date tdDate, String tcMessage)
    {
        try
        {
        log("Event", Goliath.Utilities.Date.toString(tdDate), tcMessage);
        }
        catch (Exception e)
        {
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
    }
    
    /** Log a trace
     * 
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logTrace(Date tdDate, String tcMessage)
    {
        try
        {
        log("Trace", Goliath.Utilities.Date.toString(tdDate), tcMessage);
        }
        catch (Exception e)
        {
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
    }
    
    /** Log a debug
     * 
     * @param  tdDate - Date and time of log
     * @param  tcMessage - Message being logged
     */
    @Override
    public void logDebug(Date tdDate, String tcMessage)
    {
        try
        {
        log("Debug", Goliath.Utilities.Date.toString(tdDate), tcMessage);
        }
        catch (Exception e)
        {
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
    }

    /** Create and open file logging file
     *  if the file is to be appended then existing file will be open and messages
     *  will be appended, otherwise a new file will be created. 
     * otherwise create new file
     * @param tlAppend - append messages to an existing file (if it exists, otherwise will create a new file)
     * @param tcFile Name of file to create (including path) and open
     * @throws Goliath.Exceptions.InvalidIOException 
     */
    private void openFile(boolean tlAppend, String tcFile) throws Goliath.Exceptions.InvalidIOException
    {
        //
        // Assign file name (including full path) to omsgFile
        m_oMsgFile = new File(tcFile);
        // Create a new file NOTE: Ignoring append - not sure whether this is 
        // a good idea to append to XML document because of well formedness
        try
        {
            m_oMsgFile.createNewFile();
            m_oOutputFactory = XMLOutputFactory.newInstance();
            m_oWriter = m_oOutputFactory.createXMLStreamWriter(new FileOutputStream(m_oMsgFile, false));
        }
        catch (Exception e)
        {
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
    }

    /** Print header to log file
     */
    private void printHeader() 
    {
        // TODO: Make this header changeable
       try
       {
       m_oWriter.writeAttribute("javaClassVersion", System.getProperty("java.class.version"));
       m_oWriter.writeAttribute("javaVendor", System.getProperty("java.vendor"));
       m_oWriter.writeAttribute("javaVendorURL", System.getProperty("java.vendor.url"));
       m_oWriter.writeAttribute("javaOperatingSystem", System.getProperty("os.name")); 
       m_oWriter.writeAttribute("osArchitecture", System.getProperty("os.arch"));
       m_oWriter.writeAttribute("avaliableProcessors", Integer.toString(Runtime.getRuntime().availableProcessors()));
       m_oWriter.writeAttribute("memoryUsed", Long.toString(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())); 
       m_oWriter.writeAttribute("memoryTotal", Long.toString(Runtime.getRuntime().totalMemory()));
       }
       catch (Exception e)
       {
       throw new Goliath.Exceptions.InvalidIOException(e, false);
       }
       }

    /** Set file close timer for 4 seconds
     */
    private void setTimer()
    {
        m_oTimerTask = new CloseFile();
        if (m_oTimer != null)
        {
            m_oTimer.cancel();
        }
        m_oTimer = new Timer();
        m_oTimer.schedule(m_oTimerTask, 5000);
    }


    /**
     * Write message to log
     *
     * @param  tcMessage Message being logged
     */
    private void log(String tcType, String tcDate, String tcMessage)
    {
        /**
         * if writer is null (closed through timer) then recreate writer as append and reset timer
         */
        if (m_oWriter == null)
        {
            openFile(true, m_cFile);
            setTimer();
        }
        // write the message 
        try
        {
        m_oWriter.writeStartElement("LogItem");
        m_oWriter.writeAttribute("type", tcType);
        m_oWriter.writeAttribute("time", tcDate);
        m_oWriter.writeAttribute("sessionID", Session.getCurrentSession().toString());
        if (Session.getCurrentSession().getUser().toString() == null)
        {
           m_oWriter.writeAttribute("user", "No User"); 
        }
        else
        {
           m_oWriter.writeAttribute("user", Session.getCurrentSession().getUser().toString());
        }
           m_oWriter.writeCharacters(tcMessage);
        m_oWriter.writeEndElement();
        m_oWriter.flush();
        }
        catch (Exception e)
        {
           throw new Goliath.Exceptions.InvalidIOException(e, false);
            
        }
    }

        public void endLog()
        {
          try
          {
           m_oWriter.writeEndElement();
           m_oWriter.writeEndDocument();
            m_oWriter.close();
          }
                 catch (Exception e)
        {
           throw new Goliath.Exceptions.InvalidIOException(e, false);

        }

            m_oWriter = null;

        }

    /**
     * TimerTask class that will close the oWriter file when the timer 
     * reaches its maximum period. 
     * @version     1.0 14-Dec-2007
     * @author      Peter
     **/
    class CloseFile extends TimerTask 
    {
        @Override
        public void run() 
        {
           try 
           {
           m_oWriter.writeEndElement();
           m_oWriter.writeEndDocument();
            m_oWriter.close();
            m_oWriter = null;
           }
           catch (Exception e)
        {
            throw new Goliath.Exceptions.InvalidIOException(e, false);
        }
    }
  
    }
}
    



