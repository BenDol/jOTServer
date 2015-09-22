package org.jotserver.ot.net;


import static org.junit.Assert.*;

import org.junit.Test;

public class TestPrivateRSAKey {

    @Test
    public void globalInstance() {
        PrivateRSAKey k1 = PrivateRSAKey.getInstance();
        PrivateRSAKey k2 = PrivateRSAKey.getInstance();
        assertSame(k1, k2);
    }

    @Test
    public void reportsRSAAlgorithm() {
        PrivateRSAKey k = PrivateRSAKey.getInstance();
        assertEquals("RSA", k.getAlgorithm());
    }

    @Test
    public void nullEncodedAndFormat() {
        PrivateRSAKey k = PrivateRSAKey.getInstance();
        assertNull(k.getEncoded());
        assertNull(k.getFormat());
    }
}
