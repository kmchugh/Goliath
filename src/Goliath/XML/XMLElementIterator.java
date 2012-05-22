/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.XML;

import java.io.InputStream;
import java.util.Iterator;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author kenmchugh
 */
public class XMLElementIterator
        implements Iterator
{
    private InputStream m_oStream;
    private XMLStreamReader m_oReader;

    public XMLElementIterator(InputStream toStream) throws XMLStreamException
    {
        m_oStream = toStream;
        m_oReader = XMLInputFactory.newInstance().createXMLStreamReader(toStream);
    }

    @Override
    public boolean hasNext()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object next()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
