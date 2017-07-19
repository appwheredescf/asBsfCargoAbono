package com.bansefi.nss.cargoabono.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
	
	public static Properties getProperties(String propertiesFile) {
		Properties properties = null;
		try {
			properties = new Properties();
			InputStream is = Utils.class.getClassLoader().getResourceAsStream(propertiesFile);
			properties.load(is);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		return properties;
	}
}
