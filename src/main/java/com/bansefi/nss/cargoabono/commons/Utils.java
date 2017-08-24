package com.bansefi.nss.cargoabono.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

public class Utils {
	static String uniqueID = UUID.randomUUID().toString();
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
	
	public static void ReportLogProceso(Logger log, String metodo, String operacion, String mensaje){
		log.info(uniqueID + " " + metodo + " - " + operacion + " : " + mensaje);
	}
}
