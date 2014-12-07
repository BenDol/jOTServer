package org.jotserver.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 * BaseServerOutputStream is an implementation of the OutputStream specification which 
 * is especially intended to write stream data to a specified IoSession object. All bytes
 * written to this stream will be buffered and is only written to the underlying session 
 * when flushed.
 * @author jiddo
 *
 */
public class BaseServerOutputStream extends OutputStream {

	private IoSession session;
	private ByteArrayOutputStream stream;
	
	/**
	 * Creates a new BaseServerOutputStream connected to the given IoSession.
	 * @param session
	 * 			The IoSession instance to associate with this stream. 
	 */
	public BaseServerOutputStream(IoSession session) {
		this.session = session;
		stream = new ByteArrayOutputStream();
	}

	
	public void write(int b) throws IOException {
		stream.write(b);
	}
	
	
	public void write(byte[] b, int off, int len) throws IOException {
		stream.write(b, off, len);
	}

	
	public void write(byte[] b) throws IOException {
		stream.write(b);
	}
	
	
	public void flush() throws IOException {
		stream.flush();
		if(stream.size() > 0) {
			session.write(IoBuffer.wrap(stream.toByteArray()));
			stream.reset();
		}
	}
	
	
	public void close() throws IOException {
		flush();
		stream.close();
		try {
			session.close(false).await();
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Returns the IoSession instance associated with this stream.
	 * @return
	 * 			The session associated with this stream.
	 */
	public IoSession getSession() {
		return session;
	}
	
	/**
	 * Returns the amount of bytes that is currently buffered in this stream, and that 
	 * will be written to the associated session once flushed.
	 * @return
	 * 			The amount of buffered bytes in this stream.
	 */
	public int size() {
		return stream.size();
	}

}
