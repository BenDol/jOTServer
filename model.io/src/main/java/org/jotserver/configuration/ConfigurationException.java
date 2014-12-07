package org.jotserver.configuration;

@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException {

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ConfigurationException(String arg0) {
		super(arg0);
	}

	public ConfigurationException(Throwable arg0) {
		super(arg0);
	}

}
