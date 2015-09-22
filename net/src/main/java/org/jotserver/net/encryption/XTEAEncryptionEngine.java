package org.jotserver.net.encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;

import org.jotserver.net.CDataInputStream;

/**
 * Encryption engine that implements the XTEA encryption algorithm. 
 * @author jiddo
 *
 */
public class XTEAEncryptionEngine {
    public final static int
        rounds     = 32,
        keySize    = 16,
        blockSize  = 8;

    private final static int
        delta      = 0x9e3779b9,
        decryptSum = 0xc6ef3720;

    public long[] key;

    /**
     * Initializes this encryption engine with the given keys.
     * @param key
     *             An array of keys to be used when encrypting and decrypting data using this
     *             engine. Must contain exactly four elements.
     * @throws InvalidKeyException
     *             If the key is null, or if the key does not contain exactly four elements.
     */
    public void init(long[] key) throws InvalidKeyException {
        if (key == null)
            throw new InvalidKeyException("Null key");
        
        if (key.length != keySize/4)
            throw new InvalidKeyException("Invalid key length (req. " + keySize + " bytes got "+
                                          key.length*4+")");
        
        this.key = key.clone();
    }

    /**
     * Initializes this encryption engine using keys that will be parsed from the given
     * byte array.
     * @param key
     *             An array of bytes that make up exactly 4 unsigned 32 bit integers.
     * @throws InvalidKeyException
     *             If the key is null, or if the key does not contain data for exactly four unsigned
     *             32 bit key elements..
     */
    public void init(byte[] key) throws InvalidKeyException {
        if (key == null)
            throw new InvalidKeyException("Null key");

        if(key.length != keySize)
            throw new InvalidKeyException("Invalid key length (req. " + keySize + " bytes got "+
                    key.length*4+")");

        CDataInputStream cin = new CDataInputStream(new ByteArrayInputStream(key));
        try {
            init(new long[] {cin.readU32(), cin.readU32(), cin.readU32(), cin.readU32()});
        } catch (IOException e) {}
    }

    /**
     * Encrypts the given array of bytes and returns the encrypted message as a byte array.
     * The buffer must contain a multiple of 8 bytes to fit encryption.
     * @param buffer
     *             The array of bytes that should be encrypted. Must have a length which is a
     *             multiple of 8.
     * @return
     *             The encrypted message. Will be the same length as the input.
     * @throws IOException
     *             If the buffer could not be encrypted.
     */
    public byte[] encrypt(byte[] buffer) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encrypt(out, buffer);
        return out.toByteArray();
    }

    /**
     * Encrypts the given array of bytes and writes the encrypted message to the given stream.
     * The buffer must contain a multiple of 8 bytes to fit encryption.
     * @param out
     *             The stream onto which the encrypted message will be written.
     * @param buffer
     *             The array of bytes that should be encrypted. Must have a length which is a
     *             multiple of 8.
     * @throws IOException
     *             If the buffer could not be encrypted.
     */
    public void encrypt(OutputStream out, byte[] buffer) throws IOException {
        encrypt(out, buffer, 0);
    }

    /**
     * Encrypts the given array of bytes and writes the encrypted message to the given stream.
     * The buffer must contain a multiple of 8 bytes to fit encryption.
     * @param out
     *             The stream onto which the encrypted message will be written.
     * @param buffer
     *             The array of bytes that should be encrypted. Must have a length which is a
     *             multiple of 8.
     * @param off
     *             The offset for which the encryption should start in the input buffer.
     * @throws IOException
     *             If the buffer could not be encrypted.
     */
    public void encrypt(OutputStream out, byte[] buffer, int off) throws IOException {
        InputStream in = new ByteArrayInputStream(buffer, off, buffer.length-off);
        encrypt(in, out);
    }

    /**
     * Encrypts the given input stream and writes the encrypted message to the specified
     * output stream. The input stream must contain a multiple of 8 bytes to fit encryption.
     * @param in
     *             The input stream that should be encrypted. Must have a length which is a
     *             multiple of 8.
     * @param out
     *             The stream onto which the encrypted message will be written.
     * @throws IOException
     *             If the stream could not be encrypted.
     */
    public void encrypt(InputStream in, OutputStream out) throws IOException {
        int v0 = readInt(in);
        int v1 = readInt(in);
        int sum = 0;

        for(int i = 0; i < rounds; i++) {
            v0 += ((((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + key[(int) (sum & 3)]));
            sum += delta;
            v1 += ((((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + key[(int) ((sum >>> 11) & 3)]));
        }
        writeInt(out, v0);
        writeInt(out, v1);
    }

    /**
     * Decrypts the message given in the specified byte array and returns the decrypted
     * message as a byte array. The buffer length must be a multiple of 8.
     * @param buffer
     *             The encrypted byte array that should be decrypted.
     * @return
     *             The decrypted message specified in the buffer.
     * @throws IOException
     *             If the buffer could not be decrypted.
     */
    public byte[] decrypt(byte[] buffer) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decrypt(out, buffer);
        return out.toByteArray();
    }

    /**
     * Decrypts the message given in the specified byte array and writes the decrypted
     * message to the given output stream. The buffer length must be a multiple of 8.
     * @param out
     *             The stream to which the decrypted message should be written.
     * @param buffer
     *             The encrypted byte array that should be decrypted.
     * @throws IOException
     *             If the buffer could not be decrypted.
     */
    public void decrypt(OutputStream out, byte[] buffer) throws IOException {
        InputStream in = new ByteArrayInputStream(buffer);
        decrypt(in, out);
    }

    /**
     * Decrypts the message given in the specified input stream and writes the decrypted
     * message to the given output stream. The input stream length must be a multiple of 8.
     * @param in
     *             The encrypted input stream that should be decrypted.
     * @param out
     *             The stream to which the decrypted message should be written.
     * @throws IOException
     *             If the input stream could not be decrypted.
     */
    public void decrypt(InputStream in, OutputStream out) throws IOException {
        int v0 = readInt(in);
        int v1 = readInt(in);
        int sum = decryptSum;

        for(int i = 0; i < rounds; i++) {
            v1 -= ((((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + key[(int) ((sum >>> 11) & 3)]));
            sum -= delta;
            v0 -= ((((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + key[(int) (sum & 3)]));
        }
        writeInt(out, v0);
        writeInt(out, v1);
    }

    /**
     * Decrypts the message given in the specified input stream and returns the decrypted
     * message as a byte array. The input stream length must be a multiple of 8.
     * @param in
     *             The encrypted input stream that should be decrypted.
     * @return
     *             A byte array containing the decrypted message.
     * @throws IOException
     *             If the input stream could not be decrypted.
     */
    public byte[] decrypt(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decrypt(in, out);
        return out.toByteArray();
    }

    /**
     * Reads one 32 bit unsigned integer from the given input stream using little
     * endian byte order.
     * @param in
     *             The input stream to read the integer from.
     * @return
     *             The 32 bit integer that was read from the stream.
     * @throws IOException
     *             If the integer could not be read.
     */
    public int readInt(InputStream in) throws IOException {
        int a = in.read();
        int b = in.read();
        int c = in.read();
        int d = in.read();
        return (((a & 0xff) << 0) | ((b & 0xff) << 8) |
                  ((c & 0xff) << 16) | ((d & 0xff) << 24));
    }

    /**
     * Writes one 32 bit unsigned integer to the given output stream using little
     * endian byte order.
     * @param out
     *             The output stream to write the integer to.
     * @param v
     *             The value that should be written as a 32 bit unsigned integer.
     * @throws IOException
     *             If the integer could not be written.
     */
    public void writeInt(OutputStream out, int v) throws IOException {
         out.write((byte)(0xff & (v >> 0)));
         out.write((byte)(0xff & (v >> 8)));
         out.write((byte)(0xff & (v >> 16)));
         out.write((byte)(0xff & (v >> 24)));
    }

}