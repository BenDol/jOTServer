package org.jotserver.net.encryption;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class that simplifies reading XTEA encrypted data from a stream. This
 * implementation uses an XTEAEncryptionEngine internally to perform the decryption 
 * process.
 * @author jiddo
 *
 */
public class XTEAInputStream extends FilterInputStream {

	private XTEAEncryptionEngine xtea;
	private byte[] buffer;
	private int readPos;
	
	private boolean marked;
	private ByteArrayOutputStream cache;
	
	/**
	 * Creates a new XTEAInputStream that will read from the given encrypted input 
	 * stream and decrypt the data using the given encryption engine. 
	 * @param in
	 * 			Encrypted input stream to read from.
	 * @param xtea
	 * 			The encryption engine to use for the decryption process.
	 */
	public XTEAInputStream(InputStream in, XTEAEncryptionEngine xtea) {
		super(in);
		this.xtea = xtea;
		buffer = new byte[XTEAEncryptionEngine.blockSize];
		readPos = XTEAEncryptionEngine.blockSize;
		marked = false;
		cache = null;
	}
	
	public int available() throws IOException {
		return super.available()+buffer.length-readPos;
	}

	public void close() throws IOException {
		super.close();
	}

	public synchronized void mark(int readlimit) {
		cache = new ByteArrayOutputStream(readlimit);
		marked = true;
	}

	public boolean markSupported() {
		return true;
	}

	public int read() throws IOException {
		updateBuffer();
		if(readPos >= XTEAEncryptionEngine.blockSize) {
			return -1;
		} else {
			if(marked) {
				synchronized(this) {
					cache.write(buffer[readPos]);
				}
			}
			return buffer[readPos++];
		}
	}

	public int read(byte[] b, int off, int len) throws IOException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream(len);
		
		int readLength = Math.min(len, buffer.length-readPos);
		out.write(buffer, readPos, readLength);
		len -= readLength;
		readPos += readLength;
		
		while(len >= XTEAEncryptionEngine.blockSize && in.available() >= XTEAEncryptionEngine.blockSize) {
			xtea.decrypt(in, out);
			len -= XTEAEncryptionEngine.blockSize;
		}
		
		if(len > 0) {
			updateBuffer();
			
			readLength = Math.min(len, buffer.length-readPos);
			out.write(buffer, readPos, readLength);
			readPos += readLength;
		}
		System.arraycopy(out.toByteArray(), 0, b, off, out.size());
		if(marked) {
			synchronized(this) {
				out.writeTo(cache);
			}
		}
		return out.size();
	}

	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	public synchronized void reset() throws IOException {
		if(marked) {
			cache.write(buffer, readPos, buffer.length-readPos);
			buffer = cache.toByteArray();
			readPos = 0;
			cache.reset();
		} else {
			throw new IOException("Stream not previously marked.");
		}
	}

	public long skip(long n) throws IOException {
		if(!marked) {
			long i = n;
			i -= buffer.length-readPos;
			while(i >= XTEAEncryptionEngine.blockSize) {
				i -= in.skip(XTEAEncryptionEngine.blockSize);
			}
			updateBuffer();
			readPos = (int)i;
			return n;
		} else {
			byte[] b = new byte[(int)n];
			int l = read(b);
			synchronized(this) {
				cache.write(b, 0, l);
			}
			return l;
		}
	}
	
	/**
	 * Checks if the last decrypted chunk has been fully read, and if so it will 
	 * attempt to decrypt a new chunk with the same size as the encryption engine 
	 * block size. 
	 * @throws IOException
	 * 			If the next chunk could not be decrypted or some other IO error occurrs. 
	 */
	private void updateBuffer() throws IOException {
		if(readPos >= XTEAEncryptionEngine.blockSize && in.available() >= XTEAEncryptionEngine.blockSize) {
			buffer = xtea.decrypt(in);
			readPos = 0;
		}
	}

}
