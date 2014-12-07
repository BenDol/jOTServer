package org.jotserver.configuration;

public interface ConfigurationProvider {
	public String getString(String key);

	public boolean getBoolean(String key);

	public int getInt(String key);
}
