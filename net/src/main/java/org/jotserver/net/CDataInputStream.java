package org.jotserver.net;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper class for the read methods in the CData utility class. This class provides 
 * methods for easily reading C data types from an input stream. 
 * @author jiddo
 *
 */
public class CDataInputStream extends InputStream {

    private InputStream stream;

    /**
     * Creates a new CDataInputStream that reads from the specified input stream.
     * @param stream
     *             The input stream to read data from.
     */
    public CDataInputStream(InputStream stream) {
        this.stream = stream;
    }

    /**
     * Reads a string from this input stream. The string is expected to start
     * with two bytes making up an unsigned 16 bit integer containing the total length
     * of the string in bytes. Then the actual string is read. The character encoding
     * ISO-8859-1 is expected.
     * @return
     *             The string read from the stream.
     * @throws IOException
     *             If the string could not be fully read.
     */
    public String readString() throws IOException {
        return CData.readString(stream);
    }
    
    /**
     * Reads one unsigned byte from this input stream.
     * @return
     *             The value of the read byte. This will be in the range 0 - 255.
     * @throws IOException
     *             If a byte could not be read.
     */
    public int readByte() throws IOException {
        return CData.readByte(stream);
    }
    
    /**
     * Reads one unsigned 16 bit integer from this input stream using
     * little endian byte ordering. I.e. the second byte is the most significant.
     * @return
     *             The value of the read unsigned 16 bit integer.
     * @throws IOException
     *             If the integer could not be read.
     */
    public int readU16() throws IOException {
        return CData.readU16(stream);
    }
    
    /**
     * Reads one unsigned 32 bit integer from this input stream using
     * little endian byte ordering. I.e. the fourth byte is the most significant.
     * @return
     *             The value of the read unsigned 32 bit integer.
     * @throws IOException
     *             If the integer could not be read.
     */
    public long readU32() throws IOException {
        return CData.readU32(stream);
    }
    
    public int read() throws IOException {
        return stream.read();
    }
    
    public int read(byte[] b, int off, int len) throws IOException {
        return stream.read(b, off, len);
    }

    public int read(byte[] b) throws IOException {
        return stream.read(b);
    }

    public int available() throws IOException {
        return stream.available();
    }

    public void close() throws IOException {
        stream.close();
    }

    public synchronized void mark(int arg0) {
        stream.mark(arg0);
    }

    public boolean markSupported() {
        return stream.markSupported();
    }

    public synchronized void reset() throws IOException {
        stream.reset();
    }

    public long skip(long arg0) throws IOException {
        return stream.skip(arg0);
    }
}
