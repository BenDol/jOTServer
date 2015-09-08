package org.jotserver.configuration;

public interface ConfigurationProvider {
	String getString(String key);

	boolean getBoolean(String key);

	int getInt(String key);
}
