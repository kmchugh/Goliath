/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.XML;

import Goliath.Applications.Application;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 *
 * @author kenmchugh
 */
public class XMLDocument extends Goliath.Object
        implements Iterable
{
    private File m_oFile;
    private byte[] m_oByteArray;

    /**
     * Creates a new instance of an in memory XML document
     */
    public XMLDocument()
    {

    }

    /**
     * Creates a new instance of an XML document on disk
     * @param toFile
     */
    public XMLDocument(File toFile)
    {
        m_oFile = toFile;
    }

    /**
     * Checks if the XML Document is in memory
     * @return true if in memory, false if file based
     */
    public boolean isInMemory()
    {
        return m_oFile == null;
    }

    /**
     * Checks if the document exists on disk
     * @return true if the document exists on disk
     */
    public boolean exists()
    {
        return !isInMemory() && m_oFile.exists();
    }

    /**
     * If the file does not exist on disk, this will create it
     * @return true if the file was created, false if it already existed
     * @throws java.io.IOException
     */
    public boolean create() throws IOException
    {
        if (!exists())
        {
            if (m_oFile != null)
            {
                return m_oFile.createNewFile();
            }
            else
            {
                throw new UnsupportedOperationException("Create attempted with no File");
            }
        }
        return false;
    }

    /**
     * Converts a memory based document to a file based document, and creates the file if needed
     * @param toFile The file object to write the object to
     * @return true if the file was created, false if it already existed
     * @throws java.io.IOException
     */
    public boolean create(File toFile) throws IOException
    {
        if (isInMemory())
        {
            m_oFile = toFile;
            return create();
        }
        throw new UnsupportedOperationException("XML Document is already file based");
    }

    private OutputStream getWriter() throws FileNotFoundException
    {
        if (isInMemory())
        {
            return new java.io.ByteArrayOutputStream();
        }
        else
        {
            return new FileOutputStream(m_oFile);
        }
    }

    private InputStream getReader() throws FileNotFoundException
    {
        if (isInMemory())
        {
            return new ByteArrayInputStream(m_oByteArray);
        }
        else
        {
            return new FileInputStream(m_oFile);
        }
    }

    /**
     * Iterates through each of the elements in the document
     * @return The element iterator
     */
    @Override
    public Iterator iterator()
    {
        try
        {
            return new XMLElementIterator(getReader());
        }
        catch (Throwable ex)
        {
            Application.getInstance().log(ex);
            return null;
        }
    }




    /*
     * getElementsByClassName
     * getElementsByTagName
     * getElementsWithAttributeEquals
     * getElementsWithAttribute
     * getVersion
     * getEncoding
     *
    */
}
