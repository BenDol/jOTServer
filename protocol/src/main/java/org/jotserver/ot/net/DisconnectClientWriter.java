package org.jotserver.ot.net;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.net.Writer;

public class DisconnectClientWriter implements Writer {
	
	private static final int ERROR_DEFAULT = 0x0A;
	private String message;
	private int error;
	
	public DisconnectClientWriter(String message) {
		this(ERROR_DEFAULT, message);
	}
	
	public DisconnectClientWriter(int error, String message) {
		this.error = error;
		this.message = message;
	}
	
	
	public void write(OutputStream out) throws IOException {
		CData.writeByte(out, error);
		CData.writeString(out, message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setError(int error) {
		this.error = error;
	}

}
