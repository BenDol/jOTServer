package org.jotserver.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jotserver.configuration.ConfigurationException;
import org.jotserver.configuration.ConfigurationProvider;


public class PropertiesAccessor implements ConfigurationProvider {

	protected Properties properties;
	protected File parent;

	public PropertiesAccessor(String file) throws FileNotFoundException, IOException {
		super();
		parent = new File(file).getAbsoluteFile().getParentFile();
		properties = new Properties();
		FileInputStream in = new FileInputStream(file);
		try {
			properties.load(in);
		} finally {
			in.close();
		}
	}
	
	public PropertiesAccessor(Properties properties) {
		super();
		this.properties = properties;
	}
	
	protected String getParentPath() {
		return parent.getAbsolutePath() + File.separator;
	}
	
	public String getString(String key) {
		String ret = properties.getProperty(key);
		if(ret != null) {
			return ret;
		} else {
			throw new ConfigurationException("Configuration property " + key + " missing.");
		}
	}
	
	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public int getInt(String key) {
		return toInt(key, getString(key));
	}
	
	public int getInt(String key, int defaultValue) {
		return toInt(key, get(key, String.valueOf(defaultValue)));
	}
	
	private int toInt(String key, String value) {
		try {
			return Integer.parseInt(value);
		} catch(NumberFormatException e) {
			throw new ConfigurationException("Configuration property " + key + " with value + " + value + " is not a valid integer.");
		}
	}
	
	public boolean getBoolean(String key) {
		return toBoolean(key, getString(key));
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		return toBoolean(key, get(key, String.valueOf(defaultValue)));
	}
	
	private boolean toBoolean(String key, String val) {
		if(val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("true")) {
			return true;
		} else if(val.equalsIgnoreCase("no") || val.equalsIgnoreCase("false")) {
			return false;
		} else {
			try {
				int i = Integer.parseInt(val);
				return i != 0;
			} catch(NumberFormatException e) {
				throw new ConfigurationException("Configuration property " + key + " with value " + val + " is not a valid boolean.");
			}
		}
	}

}