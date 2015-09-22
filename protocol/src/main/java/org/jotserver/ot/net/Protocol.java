package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.ClientSession;

public interface Protocol {
    void parseFirst(InputStream message) throws IOException;
    void parsePacket(InputStream message) throws IOException;
    void init(ClientSession session);
}
