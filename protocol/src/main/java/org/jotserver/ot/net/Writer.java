package org.jotserver.ot.net;

import java.io.IOException;
import java.io.OutputStream;

public interface Writer {

	public void write(OutputStream out) throws IOException;

}