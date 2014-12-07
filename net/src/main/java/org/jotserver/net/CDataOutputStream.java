package org.jotserver.net;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Wrapper class for the write methods in the CData utility class. This class provides 
 * methods for easily writing C data types to an output stream. 
 * @author jiddo
 *
 */
public class CDataOutputStream extends OutputStream {
	
	private OutputStream stream;
	
	/**
	 * Creates a new CDataOutputStream that writes to the specified output stream.
	 * @param stream
	 * 			The output stream to write data to.
	 */
	public CDataOutputStream(OutputStream stream) {
		this.stream = stream;
	}
	
	/**
	 * Writes the given value as an unsigned byte to this output stream. This method only 
	 * provides a reliable result when value is in the range of 0 - 255.
	 * @param value
	 * 			The value that should be written as a byte.
	 * @throws IOException
	 * 			If the byte could not be written.
	 */
	public void writeByte(int value) throws IOException {
		CData.writeByte(stream, value);
	}
	
	/**
	 * Writes the given value as an unsigned 16 bit integer to this output stream 
	 * using little endian byte ordering. I.e. the second byte will be the most significant.
	 * This method only provides a reliable result when value is in the range of 0 - 65,535.
	 * @param value
	 * 			The value that should be written as an unsigned 16 bit integer.
	 * @throws IOException
	 * 			If the integer could not be written.
	 */
	public void writeU16(int value) throws IOException {
		CData.writeU16(stream, value);
	}
	
	/**
	 * Writes the given value as an unsigned 32 bit integer to this output stream 
	 * using little endian byte ordering. I.e. the fourth byte will be the most significant.
	 * This method only provides a reliable result when value is in the range of 0 - 4,294,967,295.
	 * @param value
	 * 			The value that should be written as an unsigned 32 bit integer.
	 * @throws IOException
	 * 			If the integer could not be written.
	 */
	public void writeU32(long value) throws IOException {
		CData.writeU32(stream, value);
	}
	
	/**
	 * Writes a string to this output stream. The string will start with two bytes making up 
	 * an unsigned 16 bit integer containing the total length of the string in bytes. Then follows 
	 * the actual string. The used character encoding is ISO-8859-1. 
	 * @param string
	 * 			The string that should be written to the stream.
	 * @throws IOException
	 * 			If the string could not be fully written.
	 */
	public void writeString(String string) throws IOException {
		CData.writeString(stream, string);
	}
	
	public void write(int val) throws IOException {
		stream.write(val);
	}
	
	public void write(byte[] b, int off, int len) throws IOException {
    	stream.write(b, off, len);
	}

	public void write(byte[] b) throws IOException {
		stream.write(b);
	}
	
	public void close() throws IOException {
		stream.close();
	}

	public void flush() throws IOException {
		stream.flush();
	}
}
