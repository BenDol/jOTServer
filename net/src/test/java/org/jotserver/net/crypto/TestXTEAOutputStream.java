package org.jotserver.net.crypto;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.encryption.XTEAOutputStream;
import org.junit.Before;
import org.junit.Test;

public class TestXTEAOutputStream extends TestXTEA {

    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private ByteArrayOutputStream bout;
    private XTEAOutputStream out;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        bout = new ByteArrayOutputStream();
        out = new XTEAOutputStream(bout, xtea);
    }


    @Test
    public void flushWhenEmptyDoesNothing() throws IOException {
        out.flush();
        assertEquals(0, bout.size());
    }

    @Test
    public void closeClosesUnderlyingOutputStream() throws IOException {
        final OutputStream outputStream = context.mock(OutputStream.class);
        out = new XTEAOutputStream(outputStream, xtea);
        context.checking(new Expectations() {{
            oneOf(outputStream).close();
            allowing(outputStream).flush();
        }});
        out.close();
    }

    @Test
    public void writingLessThenBlockSizeBytesOneAtATimeDoesNotEncrypt() throws IOException {
        out.write(1);
        out.write(2);
        out.write(3);
        out.write(4);
        out.write(5);
        out.write(6);

        assertEquals(0, bout.size());

    }

    @Test
    public void writingBlockSizeBytesOneAtATimeForcesEncryption() throws IOException {
        for(int i = 0; i < dataChunk.length; i++) {
            out.write(dataChunk[i]);
        }
        assertArrayEquals(encrypt(dataChunk), bout.toByteArray());
    }

    @Test
    public void writingBlockSizeBytesInOneChunkForcesEncryption() throws IOException {
        out.write(dataChunk);
        assertArrayEquals(encrypt(dataChunk), bout.toByteArray());
    }

    @Test
    public void writingLessThenBlockSizeBytesInOneChunkDoesNotEncrypt() throws IOException {
        out.write(dataShort);
        assertEquals(0, bout.size());
    }

    @Test
    public void writingMoreThenBlockSizeBytesInOneChunkEncryptsFirstChunks() throws IOException {
        out.write(dataLong);
        assertArrayEquals(encrypt(dataChunk), bout.toByteArray());
    }

    @Test
    public void flushWithBufferedDataPadsAndForcesEncryption() throws IOException {
        out.write(dataShort);
        out.flush();
        assertArrayEquals(encrypt(dataShort), bout.toByteArray());
    }

    @Test
    public void flushAfterWritingMoreThenOneChunkPadsAndForcesEncryption() throws IOException {
        out.write(dataLong);
        out.flush();
        assertArrayEquals(encrypt(dataLong), bout.toByteArray());
    }

    @Test
    public void encryptingLongMessage() throws IOException {
        byte[] data = merge(merge(dataLong, dataLong), merge(dataLong, dataLong));
        out.write(dataLong);
        out.write(dataLong);
        out.write(dataLong);
        out.write(dataLong);
        out.flush();
        assertArrayEquals(encrypt(data), bout.toByteArray());
    }


    private byte[] pad(byte[] b) {
        int l = b.length;
        while(l % 8 != 0) l++;
        byte[] ret = new byte[l];
        for(int i = 0; i < l; i++) {
            if(i < b.length) ret[i] = b[i];
            else ret[i] = XTEAOutputStream.PADDING;
        }
        return ret;
    }

    private byte[] encrypt(byte[] b) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i = 0;
        while(i < b.length) {
            xtea.encrypt(out, pad(b), i);
            i+=8;
        }
        return bout.toByteArray();
    }

}
