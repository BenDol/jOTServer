package org.jotserver.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.LinkedList;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * BaseServerInputStream is an implementation of the InputStream specification especially 
 * intended to provide access to data provided through IoBuffer instances. Multiple IoBuffers 
 * can be appended at any time. This InputStream supports all the methods specified in InputStream,
 * including the optional ones. 
 * @author jiddo
 *
 */
public class BaseServerInputStream extends InputStream {
	
	private ArrayDeque<IoBuffer> data;
	private LinkedList<IoBuffer> resetCache;
	private boolean marked;
	
	/**
	 * Creates a new, empty, unmarked instance of BaseServerInputStream. 
	 */
	public BaseServerInputStream() {
		data = new ArrayDeque<IoBuffer>();
		marked = false;
		resetCache = null;
	}
	
	/**
	 * Reads one single byte from this stream and returns it. 
	 * @return
	 * 			The next byte in this stream.
	 * @throws EOFException
	 * 			If there is no more data available on the stream. 
	 */
	public int read() throws IOException {
		if(available() == 0) throw new EOFException("No data available.");
		int ret = data.getFirst().get();
		updateBufferList();
		return ret;
	}
	
	/**
	 * Appends a new IoBuffer to the end of this stream. 
	 * @param buffer
	 * 			The new buffer to append. 
	 */
	public void append(IoBuffer buffer) {
		data.offerLast(buffer);
		updateBufferList();
	}
	
	/**
	 * Calculates the total amount of unread bytes in this stream. 
	 * @return
	 * 			The amount of available bytes in this stream. 
	 */
	public int available() throws IOException {
		int ret = 0;
		for(IoBuffer buf : data) {
			ret += buf.remaining();
		}
		return ret;
	}
	
	/**
	 * Closes this stream and frees all remaining buffers. 
	 * @throws IOException
	 * 			If the stream could not be properly closed. 
	 */
	public void close() throws IOException {
		for(IoBuffer buf : data) {
			buf.free();
		}
		data.clear();
		super.close();
	}
	
	/**
	 * Attempts to skip through n bytes in this stream. If n bytes are not available, it 
	 * will skip to the end of this stream.
	 * @param n
	 * 			The amount of bytes to skip.
	 * @return
	 * 			The actual amount of bytes that was skipped.
	 */
	public long skip(long n) {
		if(n <= 0) return 0;
		long ret = 0;
		int tmp;
		while((tmp = readChunkSize((int)(n-ret))) > 0) {
			data.getFirst().skip(tmp);
			ret += tmp;
			updateBufferList();
		}
		return ret;
	}
	
	/**
	 * Attempts to read len bytes from this stream and store them in the given byte 
	 * array starting at the specified offset in the array.
	 * @param b
	 * 			The byte array in which the read data should be stored.
	 * @param off
	 * 			The offset at which the read data should be placed in the specified byte 
	 * 			array.
	 * @param len
	 * 			The maximum amount of bytes to read. 
	 * @return
	 * 			The actual amount of bytes that was read.
	 */
	public int read(byte[] b, int off, int len) {
		int ret = 0;
		int tmp;
		while((tmp = readChunkSize(len-ret)) > 0) {
			data.getFirst().get(b, off+ret, tmp);
			ret += tmp;
			updateBufferList();
		}
		return ret;
	}
	
	/**
	 * Returns the amount of unread bytes that is available in the first remaining IoBuffer.
	 * @return
	 * 			The amount of unread bytes in the first IoBuffer.
	 */
	private int availableFirst() {
		return data.isEmpty() ? 0 : data.getFirst().remaining();
	}
	
	/**
	 * Given a desired amount of bytes, this method calculates the maximum amount of those 
	 * bytes that can be read in one single chunk, from the currently active IoBuffer. 
	 * @param desired
	 * 			The desired amount of bytes.
	 * @return
	 * 			The maximum amount of bytes, within the desired range, that can be read in 
	 * 			one chunk.
	 */
	private int readChunkSize(int desired) {
		return Math.min(availableFirst(), desired);
	}
	
	/**
	 * Frees the completely read, or finished, IoBuffers from this stream.
	 * @return
	 * 			True if there is still unread bytes in this stream, false if the 
	 * 			stream is empty.
	 */
	private boolean updateBufferList() {
		while(!data.isEmpty() && availableFirst() == 0) {
			popBuffer();
		}
		return !data.isEmpty();
	}
	
	/**
	 * Frees the currently active IoBuffer from this stream. 
	 */
	private synchronized void popBuffer() {
		IoBuffer buf = data.removeFirst();
		if(marked) {
			resetCache.push(buf);
			if(!data.isEmpty()) {
				data.getFirst().mark();
			}
		} else {
			buf.free();
		}
	}
	
	/**
	 * Marks the current position in this stream so that the stream can later be reset 
	 * to this same position. 
	 * @param readLimit
	 * 		The amount of bytes that should be able to be read while still being able to 
	 * 		reset to this marked position. This parameter is ignored in this implementation.
	 */
	public synchronized void mark(int readlimit) {
		if(!data.isEmpty()) {
			data.getFirst().mark();
		}
		if(marked) {
			clearResetCache();
		}
		marked = true;
		resetCache = new LinkedList<IoBuffer>();
	}
	
	/**
	 * Clears all the buffers that has been stored away in order to be restored when resetting 
	 * this stream. 
	 */
	private void clearResetCache() {
		for(IoBuffer buf : resetCache) {
			buf.free();
		}
		resetCache = null;
	}
	
	public boolean markSupported() {
		return true;
	}

	
	/**
	 * Resets this stream to the previously marked position. 
	 * @throws IOException
	 * 			If no position in this stream has previously been marked. 
	 */
	public synchronized void reset() throws IOException {
		if(!marked) {
			throw new IOException("No position marked.");
		}
		resetMarked();
	}
	
	/**
	 * Resets this stream to the previously marked position. 
	 */
	private void resetMarked() {
		if(!data.isEmpty()) {
			data.getFirst().reset();
		}
		for(IoBuffer buf : resetCache) {
			buf.reset();
			data.addFirst(buf);
		}
		resetCache.clear();
	}
	
}
