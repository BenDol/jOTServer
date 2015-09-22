package org.jotserver.ot.net;

import java.io.IOException;
import java.io.OutputStream;

public interface Writer {

    void write(OutputStream out) throws IOException;
}