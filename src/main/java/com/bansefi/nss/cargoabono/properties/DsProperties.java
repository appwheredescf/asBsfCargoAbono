package com.bansefi.nss.cargoabono.properties;

import java.util.Properties;

import com.bansefi.nss.cargoabono.commons.Utils;


public class DsProperties 
{
	private String URL_DIARIO_ELECTRONICO;

	private Properties prop = Utils.getProperties("com/bansefi/nss/cargoabono/properties/ds.properties");
	public DsProperties()
	{		
		this.URL_DIARIO_ELECTRONICO = prop.getProperty("URL_DIARIO_ELECTRONICO");		
	}

	public String getURL_DIARIO_ELECTRONICO() {
		return URL_DIARIO_ELECTRONICO;
	}

}
