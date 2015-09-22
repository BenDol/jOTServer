package org.jotserver.ot.net.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;

public class MessageInputStream extends FilterInputStream {

    private int available;
    private int marked;

    public MessageInputStream(InputStream in) throws IOException {
        super(in);
        available = 0;
        marked = 0;
        parseHeader();
    }

    private void parseHeader() throws IOException {
        if(!in.markSupported()) {
            throw new IllegalArgumentException("Stream rewind not supported.");
        }
        if(in.available() >= 2) {
            in.mark(2);
            int l = CData.readU16(in);
            if(in.available() >= l) {
                available = l;
            } else {
                in.reset();
                throw new IOException("Not enough data in message. ");
            }
        } else {
            throw new IOException("Failed to parse header. Not sufficient data.");
        }
    }


    public int read() throws IOException {
        if(available >= 1) {
            available -= 1;
            return in.read();
        } else {
            return -1;
        }
    }


    public int available() throws IOException {
        return available;
    }


    public void close() throws IOException {
        super.close();
    }


    public synchronized void mark(int readlimit) {
        in.mark(available);
        marked = available;
    }


    public boolean markSupported() {
        return true;
    }


    public int read(byte[] b, int off, int len) throws IOException {
        if(available <= 0) return -1;

        len = Math.min(len, available);
        int l = in.read(b, off, len);
        available -= l;
        return l;
    }


    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }


    public synchronized void reset() throws IOException {
        in.reset();
        available = marked;
    }


    public long skip(long n) throws IOException {
        n = Math.min(n, available);
        long l = in.skip(n);
        available -= l;
        return l;
    }

}
