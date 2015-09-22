package org.jotserver.net.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.CData;
import org.jotserver.net.CDataInputStream;
import org.junit.Before;
import org.junit.Test;

public class TestCDataInputStream {

    private byte[] inData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
    private CDataInputStream in;
    private InputStream inRaw;
    private Mockery context;
    private InputStream ms;
    private CDataInputStream msIn;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};

        ms = context.mock(InputStream.class);
        msIn = new CDataInputStream(ms);
        in = new CDataInputStream(new ByteArrayInputStream(inData));
        inRaw = new ByteArrayInputStream(inData);
    }

    @Test
    public void read() throws IOException {
        assertEquals(inRaw.read(), in.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] in1 = new byte[inData.length];
        byte[] in2 = new byte[inData.length];
        inRaw.read(in1);
        in.read(in2);
        assertArrayEquals(in1, in2);
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        byte[] in1 = new byte[inData.length+2];
        byte[] in2 = new byte[inData.length+2];
        inRaw.read(in1, 1, inData.length);
        in.read(in2, 1, inData.length);
        assertArrayEquals(in1, in2);
    }

    @Test
    public void testSkip() throws IOException {
        context.checking(new Expectations() {{
            oneOf(ms).skip(10);
        }});
        msIn.skip(10);
        context.assertIsSatisfied();
    }

    @Test
    public void testAvailable() throws IOException {
        context.checking(new Expectations() {{
            oneOf(ms).available();
        }});
        msIn.available();
        context.assertIsSatisfied();
    }

    @Test
    public void testClose() throws IOException {
        context.checking(new Expectations() {{
            oneOf(ms).close();
        }});
        msIn.close();
        context.assertIsSatisfied();
    }

    @Test
    public void testMark() throws IOException {
        context.checking(new Expectations() {{
            oneOf(ms).mark(10);
        }});
        msIn.mark(10);
        context.assertIsSatisfied();
    }

    @Test
    public void testReset() throws IOException {
        context.checking(new Expectations() {{
            oneOf(ms).reset();
        }});
        msIn.reset();
        context.assertIsSatisfied();
    }

    @Test
    public void testMarkSupported() throws IOException {
        context.checking(new Expectations() {{
            oneOf(ms).markSupported();
        }});
        msIn.markSupported();
        context.assertIsSatisfied();
    }

    @Test
    public void readString() throws IOException {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        String string = "TestString";
        CData.writeString(out1, string);
        CDataInputStream in = new CDataInputStream(new ByteArrayInputStream(out1.toByteArray()));
        assertEquals(string, in.readString());
    }

    @Test
    public void readByte() throws IOException {
        assertEquals(CData.readByte(inRaw), in.readByte());
    }

    @Test
    public void readU16() throws IOException {
        assertEquals(CData.readU16(inRaw), in.readU16());
    }

    @Test
    public void readU32() throws IOException {
        assertEquals(CData.readU32(inRaw), in.readU32());
    }

}
