package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;

public interface Parser {

	void parse(InputStream in) throws IOException;
}