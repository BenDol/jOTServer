package org.jotserver.ot.net.io;


import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public class TestMessageOutputStream {

    private Mockery context;
    private ByteArrayOutputStream buff;
    private MessageOutputStream out;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        buff = new ByteArrayOutputStream();
        out = new MessageOutputStream(buff);
    }

    @Test
    public void closeClosesUnderlyingStream() throws IOException {
        final OutputStream out = context.mock(OutputStream.class);
        context.checking(new Expectations() {{
            oneOf(out).close();
        }});
        MessageOutputStream mout = new MessageOutputStream(out);
        mout.close();
        context.assertIsSatisfied();
    }

    @Test
    public void flushEmptyWritesNothing() throws IOException {
        assertEquals(0, buff.size());
        out.flush();
        assertEquals(0, buff.size());
    }

    @Test
    public void writeAndFlushSingleByte() throws IOException {
        assertEquals(0, buff.size());
        out.write(123);
        assertEquals(0, buff.size());
        out.flush();
        assertEquals(3, buff.size());
        assertArrayEquals(new byte[] { 1, 0, 123 }, buff.toByteArray());
    }

    @Test
    public void writeAndFushChunks() throws IOException {
        byte[] data1 = new byte[] { 1, 2, 3 };
        byte[] data2 = new byte[] { 4, 5, 6 };
        byte[] total = new byte[data1.length+data2.length];
        System.arraycopy(data1, 0, total, 0, data1.length);
        System.arraycopy(data2, 0, total, data1.length, data2.length);

        assertEquals(0, buff.size());
        out.write(data1);
        out.write(data2);
        out.flush();
        assertEquals(total.length+2, buff.size());

        byte[] result = new byte[buff.size()-2];
        System.arraycopy(buff.toByteArray(), 2, result, 0, buff.size()-2);
        assertArrayEquals(total, result);
    }

    @Test
    public void writeChunk2() throws IOException {
        byte[] data1 = new byte[] { 1, 2, 3 };
        out.write(data1, 1, 1);
        out.flush();
        assertEquals(1+2, buff.size());
    }

}
