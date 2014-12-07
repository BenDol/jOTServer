package org.jotserver.ot.net.game;

import java.io.IOException;
import java.io.InputStream;

public interface MessageParser {
	public void setContext(ParserContext context);
	public void parse(PacketType type, InputStream message) throws IOException;
}