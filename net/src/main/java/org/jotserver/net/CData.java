package org.jotserver.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for writing and reading raw C data types to and from streams. 
 * All methods working with integers are unsigned and use little endian byte ordering. 
 * @author jiddo
 *
 */
public class CData {
	
	private static final String CHARACTER_ENCODING = "ISO-8859-1";
	
	/**
	 * Reads one unsigned byte from the given input stream. 
	 * @param in
	 * 			The input stream to read the byte from.
	 * @return
	 * 			The value of the read byte. This will be in the range 0 - 255.
	 * @throws IOException
	 * 			If a byte could not be read.
	 */
	public static int readByte(InputStream in) throws IOException {
	    return in.read() & 0xFF;
	}
	
	/**
	 * Reads one unsigned 16 bit integer from the given input stream using
	 * little endian byte ordering. I.e. the second byte is the most significant.
	 * @param in
	 * 			The input stream to read the integer from.
	 * @return
	 * 			The value of the read unsigned 16 bit integer.
	 * @throws IOException
	 * 			If the integer could not be read.
	 */
	public static int readU16(InputStream in) throws IOException {
	    int first = readByte(in);
	    int second = readByte(in);
	    int ret = (first) | (second << 8);
	    return ret;
	}
	
	/**
	 * Reads one unsigned 32 bit integer from the given input stream using
	 * little endian byte ordering. I.e. the fourth byte is the most significant.
	 * @param in
	 * 			The input stream to read the integer from.
	 * @return
	 * 			The value of the read unsigned 32 bit integer.
	 * @throws IOException
	 * 			If the integer could not be read.
	 */
	public static long readU32(InputStream in) throws IOException {
	    int a = readByte(in);
	    int b = readByte(in);
	    int c = readByte(in);
	    int d = readByte(in);
	    long ret = ((long) (a | b << 8 | c << 16 | d << 24)) & 0xFFFFFFFFL;
	    return ret;
	}
	
	/**
	 * Reads a string from the given input stream. The string is expected to start
	 * with two bytes making up an unsigned 16 bit integer containing the total length
	 * of the string in bytes. Then the actual string is read. The character encoding 
	 * ISO-8859-1 is expected. 
	 * @param in
	 * 			The input stream to read the string from.
	 * @return
	 * 			The string read from the stream.
	 * @throws IOException
	 * 			If the string could not be fully read.
	 */
	public static String readString(InputStream in) throws IOException {
		int length = readU16(in);
		byte[] b = new byte[length];
		int l = in.read(b, 0, length);
	    String str = new String(b, 0, l, CHARACTER_ENCODING);
	    return str;
	}
	
	
	
	/**
	 * Writes the given value as an unsigned byte to the given output stream. This method only 
	 * provides a reliable result when value is in the range of 0 - 255.
	 * @param out
	 * 			The output stream to write the byte to.
	 * @param value
	 * 			The value that should be written as a byte.
	 * @throws IOException
	 * 			If the byte could not be written.
	 */
	public static void writeByte(OutputStream out, int value) throws IOException {
		out.write(value & 0xFF);
    }
	
	/**
	 * Writes the given value as an unsigned 16 bit integer to the given output stream 
	 * using little endian byte ordering. I.e. the second byte will be the most significant.
	 * This method only provides a reliable result when value is in the range of 0 - 65,535.
	 * @param out
	 * 			The output stream to write the integer to.
	 * @param value
	 * 			The value that should be written as an unsigned 16 bit integer.
	 * @throws IOException
	 * 			If the integer could not be written.
	 */
	public static void writeU16(OutputStream out, int value) throws IOException {
		out.write(value & 0x00FF);
		out.write((value & 0xFF00) >> 8);
    }
	
	/**
	 * Writes the given value as an unsigned 32 bit integer to the given output stream 
	 * using little endian byte ordering. I.e. the fourth byte will be the most significant.
	 * This method only provides a reliable result when value is in the range of 0 - 4,294,967,295.
	 * @param out
	 * 			The output stream to write the integer to.
	 * @param value
	 * 			The value that should be written as an unsigned 32 bit integer.
	 * @throws IOException
	 * 			If the integer could not be written.
	 */
    public static void writeU32(OutputStream out, long value) throws IOException {
    	out.write((int) (value & 0x000000FFL));
    	out.write((int) ((value & 0x0000FF00L) >> 8));
    	out.write((int) ((value & 0x00FF0000L) >> 16));
    	out.write((int) ((value & 0xFF000000L) >> 24));
    }
	
	/**
	 * Writes a string to the given output stream. The string will start with two bytes making up 
	 * an unsigned 16 bit integer containing the total length of the string in bytes. Then follows 
	 * the actual string. The used character encoding is ISO-8859-1. 
	 * @param out
	 * 			The output stream to write the string to.
	 * @param string
	 * 			The string that should be written to the stream.
	 * @throws IOException
	 * 			If the string could not be fully written.
	 */
    public static void writeString(OutputStream out, String string) throws IOException {
    	byte[] b = string.getBytes(CHARACTER_ENCODING);
    	writeU16(out, b.length);
    	out.write(b);
    }

}