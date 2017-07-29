package com.bansefi.nss.cargoabono.properties;

import java.util.Properties;

import com.bansefi.nss.cargoabono.commons.Utils;


public class DsProperties 
{
	private String URL_DIARIO_ELECTRONICO;
	private String URL_ERROR_DESC;
	private Properties prop = Utils.getProperties("com/bansefi/nss/cargoabono/properties/ds.properties");
	public DsProperties()
	{		
		this.URL_DIARIO_ELECTRONICO = prop.getProperty("URL_DIARIO_ELECTRONICO");
		this.URL_DIARIO_ELECTRONICO= prop.getProperty("URL_ERROR_DESC");
	}

	public String getURL_DIARIO_ELECTRONICO() {
		return URL_DIARIO_ELECTRONICO;
	}

	public String getURL_ERROR_DESC() {
		return URL_ERROR_DESC;
	}

	public void setURL_ERROR_DESC(String uRL_ERROR_DESC) {
		URL_ERROR_DESC = uRL_ERROR_DESC;
	}

}
