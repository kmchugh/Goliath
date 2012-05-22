/* ========================================================
 * FileChannel.java
 *
 * Author:      admin
 * Created:     Dec 25, 2011, 3:10:16 PM
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
package Goliath.IO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Dec 25, 2011
 * @author      admin
 **/
public class FileChannel extends Goliath.Object
{
    private java.nio.channels.FileChannel m_oChannel;

    /**
     * Creates a new instance of FileChannel
     */
    public FileChannel(java.nio.channels.FileChannel toChannel)
    {
        m_oChannel = toChannel;
    }
    
    
    /**
     * Writes a string to a file channel
     * @param tcString the string to write
     * @param tcCharSet the character set to use to encode the string
     * @throws UnsupportedCharsetException if the character set does not exist
     * @throws IOException if we were unable to write
     * @return this, to make the call chainable
     */
    public FileChannel write(String tcString, String tcCharSet)
            throws UnsupportedCharsetException, IOException
    {
        m_oChannel.write(ByteBuffer.wrap(tcString.getBytes(tcCharSet)));
        return this;
    }
    
    /**
     * Writes a string to a file chanel
     * @param tcString the string to write
     * @throws IOException if we were unable to write
     * @return this, to make the call chainable
     */
    public FileChannel write(String tcString)
            throws IOException
    {
        try
        {
            write(tcString, "UTF8");
        }
        catch (UnsupportedCharsetException toEX)
        {
            // UTF8 is standard so this will not happen
        }
        return this;
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Delegated methods
     */

    @Override
    public int hashCode()
    {
        return m_oChannel.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return m_oChannel.equals(o);
    }

    public final boolean isOpen()
    {
        return m_oChannel.isOpen();
    }

    public final void close() throws IOException
    {
        m_oChannel.close();
    }

    public int write(ByteBuffer bb, long l) throws IOException
    {
        return m_oChannel.write(bb, l);
    }

    public final long write(ByteBuffer[] bbs) throws IOException
    {
        return m_oChannel.write(bbs);
    }

    public long write(ByteBuffer[] bbs, int i, int i1) throws IOException
    {
        return m_oChannel.write(bbs, i, i1);
    }

    public int write(ByteBuffer bb) throws IOException
    {
        return m_oChannel.write(bb);
    }

    public final FileLock tryLock() throws IOException
    {
        return m_oChannel.tryLock();
    }

    public FileLock tryLock(long l, long l1, boolean bln) throws IOException
    {
        return m_oChannel.tryLock(l, l1, bln);
    }

    public java.nio.channels.FileChannel truncate(long l) throws IOException
    {
        return m_oChannel.truncate(l);
    }

    public long transferTo(long l, long l1, WritableByteChannel wbc) throws IOException
    {
        return m_oChannel.transferTo(l, l1, wbc);
    }

    public long transferFrom(ReadableByteChannel rbc, long l, long l1) throws IOException
    {
        return m_oChannel.transferFrom(rbc, l, l1);
    }

    public long size() throws IOException
    {
        return m_oChannel.size();
    }

    public int read(ByteBuffer bb, long l) throws IOException
    {
        return m_oChannel.read(bb, l);
    }

    public final long read(ByteBuffer[] bbs) throws IOException
    {
        return m_oChannel.read(bbs);
    }

    public long read(ByteBuffer[] bbs, int i, int i1) throws IOException
    {
        return m_oChannel.read(bbs, i, i1);
    }

    public int read(ByteBuffer bb) throws IOException
    {
        return m_oChannel.read(bb);
    }

    public java.nio.channels.FileChannel position(long l) throws IOException
    {
        return m_oChannel.position(l);
    }

    public long position() throws IOException
    {
        return m_oChannel.position();
    }

    public MappedByteBuffer map(MapMode mm, long l, long l1) throws IOException
    {
        return m_oChannel.map(mm, l, l1);
    }

    public final FileLock lock() throws IOException
    {
        return m_oChannel.lock();
    }

    public FileLock lock(long l, long l1, boolean bln) throws IOException
    {
        return m_oChannel.lock(l, l1, bln);
    }

    public void force(boolean bln) throws IOException
    {
        m_oChannel.force(bln);
    }
    
    
}
