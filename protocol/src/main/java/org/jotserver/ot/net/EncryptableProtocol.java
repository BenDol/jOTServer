package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import org.jotserver.net.ClientSession;
import org.jotserver.net.encryption.XTEAEncryptionEngine;
import org.jotserver.net.encryption.XTEAInputStream;
import org.jotserver.net.encryption.XTEAOutputStream;
import org.jotserver.ot.net.io.MessageOutputStream;

public abstract class EncryptableProtocol implements Protocol {

    protected ClientSession client;
    protected XTEAEncryptionEngine encryptionEngine;

    public EncryptableProtocol() {
        super();
        this.client = null;
        this.encryptionEngine = null;
    }

    protected OutputStream getEncryptedMessageOutputStream() throws IOException {
        OutputStream socketMessageStream = new MessageOutputStream(client.getOutputStream());
        return new MessageOutputStream(encryptStreamXTEA(socketMessageStream));
    }

    protected OutputStream encryptStreamXTEA(OutputStream out) {
        if(encryptionEngine == null) {
            throw new IllegalStateException("XTEA encryption engine not yet initialized.");
        }
        return new XTEAOutputStream(out, encryptionEngine);
    }

    protected InputStream decryptStreamRSA(InputStream message) throws IOException,
            GeneralSecurityException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, PrivateRSAKey.getInstance());

        InputStream ret = new CipherInputStream(message, cipher);
        if(ret.read() != 0) {
            throw new GeneralSecurityException("First RSA decrypted byte is not zero. Decryption failed.");
        } else {
            return ret;
        }
    }

    protected void initXTEAEngine(long[] keys) throws InvalidKeyException {
        encryptionEngine = new XTEAEncryptionEngine();
        encryptionEngine.init(keys);
    }


    public void init(ClientSession session) {
        client = session;
    }

    protected InputStream getDecryptedInputStream(InputStream message) {
        if(encryptionEngine == null) {
            throw new IllegalStateException("XTEA encryption engine not yet initialized.");
        }
        return new XTEAInputStream(message, encryptionEngine);
    }

}