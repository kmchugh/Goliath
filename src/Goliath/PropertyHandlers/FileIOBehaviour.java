
/* =========================================================
 * FileIOBehaviour.java
 *
 * Author:      vbayon
 * Created:     30-Jan-2008, 13:05:34
 * 
 * Description
 * --------------------------------------------------------
 * General behaviour for properties that are persisted on XML files
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.PropertyHandlers;

import Goliath.Collections.HashTable;
import Goliath.Collections.PropertySet;
import Goliath.Exceptions.FileNotFoundException;
import java.io.File;

import Goliath.Interfaces.PropertyHandlers.IPropertyIOBehaviour;
import java.util.Timer;
import java.util.TimerTask;



/**
 * General behaviour for properties that are persisted on XML files without encryption
 * 
 *
 * @see         Goliath.DynamicCode.Java#getProperties
 * @version     1.0 30-Jan-2008
 * @author      vbayon
 **/
public class FileIOBehaviour implements IPropertyIOBehaviour
{
    private class WriteFileTask extends TimerTask
    {
        private FileIOBehaviour m_oBehaviour;

        public WriteFileTask(FileIOBehaviour toBehaviour)
        {
            m_oBehaviour = toBehaviour;
        }

        @Override
        public void run()
        {
            m_oBehaviour.writeProperties();
        }
    }


    private HashTable <String, PropertySet> m_oProperties;
    private String m_cFileName;
    private String m_cRoot;
    private String m_cSeparator;
    private long m_nFileModified;
    private File m_oDocFile;
    private boolean m_lModified;
    private int m_nWriteDelay;
    private Timer m_oTimer;
    private TimerTask m_oTimerTask;




    /**
     * Creates a new instance of the FileIOBehavious.  The file IO Behaviour will write all properties to file in xml format
     * @param tcFileName The name of the file to write to
     * @param tcRootNode The root node for the document
     * @param tcSeparator The character used to separate name spaces in the properties
     */
    public FileIOBehaviour(String tcFileName, String tcRootNode, String tcSeparator)
    {
        m_cRoot = tcRootNode;
        m_cSeparator = tcSeparator;
        m_cFileName = tcFileName;
        m_lModified = false;

        /* We will wait 1/2 second from the last update before writing any changes to disk, this will
         * make sure we don't have too  many writes going
         */
        m_nWriteDelay = 500;
    }


    /**
     * Forces a read and store of all of the properties in the property file into the property set
     */
    private synchronized void readProperties()
    {
        boolean llExists = false;
        if (m_oDocFile == null)
        {
            m_oDocFile = new File(m_cFileName);
            llExists = m_oDocFile.exists();
        }

        if (llExists)
        {

            if (m_oDocFile.lastModified() != m_nFileModified)
            {
                // The file has been changed since last we read it.

                // Reset the cache
                m_nFileModified = m_oDocFile.lastModified();

                try
                {
                    m_oProperties = Goliath.XML.Utilities.fromXML(m_oDocFile, m_cRoot);
                }
                catch (FileNotFoundException ex)
                {
                    // This can not happen because we have already checked for the file
                    m_oProperties = new HashTable<String, PropertySet>();
                }
            }
        }
        else
        {
            if (m_oProperties == null)
            {
                m_oProperties = new HashTable<String, PropertySet>();
            }
        }
    }

    @Override
    public synchronized <K> void setProperty(String tcName, K toValue)
    {
        // TODO: Need to refactor everywhere that passes a property to just pass the path and the value
        readProperties();
        String lcPath = parsePath(tcName);
        String lcPropertyName = parsePropertyName(tcName);

        if (!m_oProperties.containsKey(lcPath))
        {
            m_oProperties.put(lcPath, new PropertySet());
        }

        K loValue = (K)m_oProperties.get(lcPath).getProperty(lcPropertyName);
        if (loValue != toValue)
        {
            m_oProperties.get(lcPath).setProperty(lcPropertyName, toValue);
            setModified(true);
        }
    }

    @Override
    public synchronized <K> K getProperty(String tcName, K toDefault)
    {
        // TODO: Put a breakpoint here to test what is happening when the system is idle
        readProperties();
        String lcPath = parsePath(tcName);
        String lcPropertyName = parsePropertyName(tcName);

        if (!m_oProperties.containsKey(lcPath) || !m_oProperties.get(lcPath).containsKey(lcPropertyName))
        {
            // Add in the default
            setProperty(tcName, toDefault);
        }
        return (K)m_oProperties.get(lcPath).getProperty(lcPropertyName);
    }


    @Override
    public synchronized <K> K getProperty(String tcName)
    {
        // TODO: Put a breakpoint here to test what is happening when the system is idle
        readProperties();
        String lcPath = parsePath(tcName);
        String lcPropertyName = parsePropertyName(tcName);

        return (K) ((m_oProperties.containsKey(lcPath) && m_oProperties.get(lcPath).hasProperty(lcPropertyName)) ?
            m_oProperties.get(lcPath).getProperty(lcPropertyName) :
            null);
    }

    private String parsePath(String tcName)
    {
        return (tcName.lastIndexOf(m_cSeparator) >= 0 ?
            tcName.substring(0, tcName.lastIndexOf(m_cSeparator))
            :
            m_cSeparator);
    }

    private String parsePropertyName(String tcName)
    {
        return (tcName.lastIndexOf(m_cSeparator) >= 0 ?
            tcName.substring(tcName.lastIndexOf(m_cSeparator) + 1)
            :
            tcName).toLowerCase();
    }

    private void writeProperties()
    {
        if (m_lModified)
        {
            // Writes the properties to disk
            Goliath.XML.Utilities.toXMLFile(m_oProperties, m_oDocFile, m_cRoot);
            m_lModified = false;
        }
    }

    private void setModified(boolean tlModified)
    {
        if (m_lModified != tlModified)
        {
            m_lModified = tlModified;
        }

        if (m_lModified)
        {
            if (m_oTimer == null)
            {
                m_oTimer = new Timer("Application Settings Timer Task");
            }
            if (m_oTimerTask != null)
            {
                m_oTimerTask.cancel();
                m_oTimer.purge();
            }
            m_oTimerTask = new WriteFileTask(this);
            m_oTimer.schedule(m_oTimerTask, m_nWriteDelay);
        }
    }
}
