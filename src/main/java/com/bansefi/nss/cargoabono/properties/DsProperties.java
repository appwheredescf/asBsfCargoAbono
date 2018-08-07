package com.bansefi.nss.cargoabono.properties;

import java.util.Properties;

import com.bansefi.nss.cargoabono.commons.Utils;


public class DsProperties {
	private String URL_DIARIO_ELECTRONICO;
	private String URL_ERROR_DESC;
	private String URL_INSERTA_DIARIO;
	private Properties prop = Utils.getProperties("com/bansefi/nss/cargoabono/properties/ds.properties");
	public DsProperties() {
		this.URL_DIARIO_ELECTRONICO = prop.getProperty("URL_DIARIO_ELECTRONICO");
		this.URL_ERROR_DESC= prop.getProperty("URL_ERROR_DESC");
		this.URL_INSERTA_DIARIO = prop.getProperty("URL_INSERTA_DIARIO");
	}

	public String getURL_DIARIO_ELECTRONICO() {
		return URL_DIARIO_ELECTRONICO;
	}
	public String getURL_ERROR_DESC() {
		return URL_ERROR_DESC;
	}
	public String getURL_INSERTA_DIARIO(){return URL_INSERTA_DIARIO;}
	public void setURL_ERROR_DESC(String uRL_ERROR_DESC) {
		URL_ERROR_DESC = uRL_ERROR_DESC;
	}

}
