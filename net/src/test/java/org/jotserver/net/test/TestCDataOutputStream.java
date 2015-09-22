package org.jotserver.net.test;


import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.CData;
import org.jotserver.net.CDataOutputStream;
import org.junit.Before;
import org.junit.Test;

public class TestCDataOutputStream {

    private ByteArrayOutputStream actual;
    private CDataOutputStream out;
    private ByteArrayOutputStream expected;
    private Mockery context;
    private OutputStream mo;
    private CDataOutputStream mout;

    @Before
    public void setUp() throws Exception {
        expected = new ByteArrayOutputStream();
        actual = new ByteArrayOutputStream();
        out = new CDataOutputStream(actual);

        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        mo = context.mock(OutputStream.class);
        mout = new CDataOutputStream(mo);
    }

    @Test
    public void writeU16() throws IOException {
        int value = 1337;
        CData.writeU16(expected, value);
        out.writeU16(value);
        assertStreams(expected, actual);
    }

    @Test
    public void writeU32() throws IOException {
        int value = 13371337;
        CData.writeU32(expected, value);
        out.writeU32(value);
        assertStreams(expected, actual);
    }

    @Test
    public void writeString() throws IOException {
        String string = "TestString";
        CData.writeString(expected, string);
        out.writeString(string);
        assertStreams(expected, actual);
    }

    @Test
    public void writeByte() throws IOException {
        int value = 123;
        CData.writeByte(expected, value);
        out.writeByte(value);
        assertStreams(expected, actual);
    }

    @Test
    public void write() throws IOException {
        context.checking(new Expectations() {{
            oneOf(mo).write(10);
        }});
        mout.write(10);
        context.assertIsSatisfied();
    }

    @Test
    public void writeByteArray() throws IOException {
        final byte[] arr = new byte[] {1, 2, 3};
        context.checking(new Expectations() {{
            oneOf(mo).write(arr);
        }});
        mout.write(arr);
        context.assertIsSatisfied();
    }

    @Test
    public void writeByteArrayIntInt() throws IOException {
        final byte[] arr = new byte[] {1, 2, 3};
        context.checking(new Expectations() {{
            oneOf(mo).write(arr, 1, 2);
        }});
        mout.write(arr, 1, 2);
        context.assertIsSatisfied();
    }

    @Test
    public void close() throws IOException {
        context.checking(new Expectations() {{
            oneOf(mo).close();
        }});
        mout.close();
        context.assertIsSatisfied();
    }

    @Test
    public void flush() throws IOException {
        context.checking(new Expectations() {{
            oneOf(mo).flush();
        }});
        mout.flush();
        context.assertIsSatisfied();
    }

    private void assertStreams(ByteArrayOutputStream s1, ByteArrayOutputStream s2) {
        assertArrayEquals(s1.toByteArray(), s2.toByteArray());
    }

}
