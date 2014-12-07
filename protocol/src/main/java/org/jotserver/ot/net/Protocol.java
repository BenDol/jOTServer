package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.ClientSession;

public interface Protocol {
	public void parseFirst(InputStream message) throws IOException;
	public void parsePacket(InputStream message) throws IOException;
	public void init(ClientSession session);
}
