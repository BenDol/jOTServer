package org.jotserver.ot.net.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CData;


public class MessageOutputStream extends OutputStream {

    private ByteArrayOutputStream buffer;
    private OutputStream out;

    public MessageOutputStream(OutputStream out) {
        buffer = new ByteArrayOutputStream();
        this.out = out;
    }


    public void close() throws IOException {
        flush();
        buffer.close();
        out.close();
    }


    public void flush() throws IOException {
        if(buffer.size() > 0) {
            buffer.flush();
            CData.writeU16(out, buffer.size());
            buffer.writeTo(out);
            buffer.reset();
            out.flush();
        }
    }


    public void write(byte[] b, int off, int len) throws IOException {
        buffer.write(b, off, len);
    }


    public void write(byte[] b) throws IOException {
        buffer.write(b);
    }


    public void write(int b) throws IOException {
        buffer.write(b);
    }

}
