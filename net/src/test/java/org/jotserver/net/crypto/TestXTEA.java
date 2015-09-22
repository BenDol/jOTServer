package org.jotserver.net.crypto;

import org.jotserver.net.encryption.XTEAEncryptionEngine;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class TestXTEA {

    static final byte[] validByteKey = new byte[] { 113, -32, 73, 125, 113, 55, -20, -20, -56, -76, -86, -28, -33, 127, -31, -53 };
    static final byte[] dataChunk = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    static final byte[] dataShort = new byte[] { 1, 2, 3, 4 };
    static final byte[] dataLong = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    protected XTEAEncryptionEngine xtea;

    @Before
    public void setUp() throws Exception {
        xtea = new XTEAEncryptionEngine();
        xtea.init(validByteKey);
    }

    protected byte[] merge(byte[] b1, byte[] b2) {
        byte[] ret = new byte[b1.length + b2.length];
        for(int i = 0; i < b1.length + b2.length; i++) {
            if(i < b1.length) ret[i] = b1[i];
            else ret[i] = b2[i-b1.length];
        }
        return ret;
    }

}