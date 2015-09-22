package org.jotserver.net.encryption;

import java.io.ByteArrayInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class that simplifies writing XTEA encrypted data to a stream. This
 * implementation uses an XTEAEncryptionEngine internally to perform the encryption 
 * process. Note that this stream may only be used by one thread, and this is always 
 * the thread that created it.
 * @author jiddo
 *
 */
public class XTEAOutputStream extends FilterOutputStream {

    public static final byte PADDING = 0x33;

    private XTEAEncryptionEngine xtea;
    private byte[] buffer;
    private int readPos;

    private Thread thread;

    /**
     * Creates a new XTEAOutputStream that will write encrypted data to the given output
     * stream. Data will be encrypted using the given encryption engine.
     * @param out
     *             Encrypted output stream to write to.
     * @param xtea
     *             The encryption engine to use for the encryption process.
     */
    public XTEAOutputStream(OutputStream out, XTEAEncryptionEngine xtea) {
        super(out);
        thread = Thread.currentThread();
        this.xtea = xtea;
        buffer = new byte[XTEAEncryptionEngine.blockSize];
        readPos = 0;
    }

    /**
     * Makes sure that the current thread is the same as the one that created this stream.
     * If not, then an error message will be printed to the standard output.
     */
    private void check() {
        if(!thread.equals(Thread.currentThread())) {
            System.out.println("Critical! Both " + thread + " and " + Thread.currentThread() + " is using the same encryption stream!");
        }
    }

    /**
     * Checks if the currently active chunk is full, and if so it will be encrypted,
     * flushed to the underlying output stream and reset to accept new data.
     * @throws IOException
     *             If the current chunk could not be encrypted.
     */
    private void encryptBuffer() throws IOException {
        check();
        if(readPos >= buffer.length) {
            xtea.encrypt(out, buffer);
            readPos = 0;
        }
    }

    /**
     * Pads the remainder of the current chunk with the specified padding byte until it
     * reaches its maximum size.
     */
    private void padBuffer() {
        check();
        for(int i = readPos; i < buffer.length; i++) {
            buffer[i] = PADDING;
        }
        readPos = buffer.length;
    }

    public void close() throws IOException {
        check();
        super.close();
    }

    public void flush() throws IOException {
        check();
        if(readPos > 0) {
            padBuffer();
            encryptBuffer();
        }
        super.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException {
        check();
        while(readPos < buffer.length && len > 0) {
            buffer[readPos] = b[off];
            readPos++;
            off++;
            len--;
        }
        encryptBuffer();
        InputStream in = new ByteArrayInputStream(b, off, len);
        while(len >= buffer.length) {
            xtea.encrypt(in, out);
            off += buffer.length;
            len -= buffer.length;
        }
        if(len > 0) {
            readPos += in.read(buffer, readPos, len);
        }
    }

    public void write(byte[] b) throws IOException {
        check();
        write(b, 0, b.length);
    }

    public void write(int b) throws IOException {
        check();
        buffer[readPos++] = (byte)b;
        encryptBuffer();
    }

}
