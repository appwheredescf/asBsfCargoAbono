
package com.bansefi.nss.cargoabono.properties;

import java.util.Properties;

import com.bansefi.nss.cargoabono.commons.Utils;


public class TcbProperties 
{
	private String URL_SALDOS;
	private String URL_ABONO;
	private String URL_CARGO;
	private String URL_FECHA_CTBLE;
	private String URL_CLABE;
	private String URL_CONS_ULT_TRANS;
	private String URL_CONS_SUCURSALES;
	private String URL_CONS_DOMIC;
	private String URL_CONS_NOMBRE;
	private String URL_CONS_ACUERDO;
	
	private String CARGO_CODTX;
	private String CARGO_CODAPL;
	private String CARGO_ISO;
	private String CARGO_TIPOOPER;
	private String CARGO_CLOP;
	private String CARGO_SUBCLOP;
	private String ABONO_CODTX;
	private String ABONO_CODAPL;
	private String ABONO_ISO;
	private String ABONO_TIPOOPER;
	private String ABONO_CLOP;
	private String ABONO_SUBCLOP;
	

	
	
	private Properties prop = Utils.getProperties("com/bansefi/nss/cargoabono/properties/tcb.properties");
	public TcbProperties()
	{
		
		this.URL_SALDOS = prop.getProperty("URL_SALDOS");
		this.URL_ABONO = prop.getProperty("URL_ABONO");
		this.URL_CARGO = prop.getProperty("URL_CARGO");
		this.URL_FECHA_CTBLE = prop.getProperty("URL_FECHA_CTBLE");
		this.URL_CLABE = prop.getProperty("URL_CLABE");
		this.CARGO_CLOP = prop.getProperty("CARGO_CLOP");
		this.CARGO_CODAPL = prop.getProperty("CARGO_CODAPL");
		this.CARGO_CODTX = prop.getProperty("CARGO_CODTX");
		this.CARGO_ISO = prop.getProperty("CARGO_ISO");
		this.CARGO_SUBCLOP = prop.getProperty("CARGO_SUBCLOP");
		this.CARGO_TIPOOPER = prop.getProperty("CARGO_TIPOOPER");
		this.ABONO_CLOP = prop.getProperty("ABONO_CLOP");
		this.ABONO_CODAPL = prop.getProperty("ABONO_CODAPL");
		this.ABONO_CODTX = prop.getProperty("ABONO_CODTX");
		this.ABONO_ISO = prop.getProperty("ABONO_ISO");
		this.ABONO_SUBCLOP = prop.getProperty("ABONO_SUBCLOP");
		this.ABONO_TIPOOPER = prop.getProperty("ABONO_TIPOOPER");
		this.URL_CONS_ULT_TRANS = prop.getProperty("URL_CONS_ULT_TRANS");
		this.URL_CONS_SUCURSALES= prop.getProperty("URL_CONS_SUCURSALES");
		this.URL_CONS_DOMIC=prop.getProperty("URL_CONS_DOMIC");
		this.URL_CONS_NOMBRE=prop.getProperty("URL_CONS_NOMBRE");
		this.URL_CONS_ACUERDO=prop.getProperty("URL_CONS_ACUERDO");
	}

	
	
	public String getURL_SALDOS() {
		return URL_SALDOS;
	}

	public String getURL_ABONO() {
		return URL_ABONO;
	}

	public String getURL_CARGO() {
		return URL_CARGO;
	}

	public String getURL_FECHA_CTBLE() {
		return URL_FECHA_CTBLE;
	}

	public String getURL_CLABE() {
		return URL_CLABE;
	}

	public String getCARGO_CODTX() {
		return CARGO_CODTX;
	}
	
	public String getCARGO_CODAPL() {
		return CARGO_CODAPL;
	}
	
	public String getCARGO_ISO() {
		return CARGO_ISO;
	}
	
	public String getCARGO_TIPOOPER() {
		return CARGO_TIPOOPER;
	}
	
	public String getCARGO_CLOP() {
		return CARGO_CLOP;
	}
	
	public String getCARGO_SUBCLOP() {
		return CARGO_SUBCLOP;
	}
	
	public String getABONO_CODTX() {
		return ABONO_CODTX;
	}
	
	public String getABONO_CODAPL() {
		return ABONO_CODAPL;
	}
	
	public String getABONO_ISO() {
		return ABONO_ISO;
	}
	
	public String getABONO_TIPOOPER() {
		return ABONO_TIPOOPER;
	}
	
	public String getABONO_CLOP() {
		return ABONO_CLOP;
	}
	
	public String getABONO_SUBCLOP() {
		return ABONO_SUBCLOP;
	}
	
	public String getURL_CONS_ULT_TRANS() {
		return URL_CONS_ULT_TRANS;
	}



	public String getURL_CONS_SUCURSALES() {
		return URL_CONS_SUCURSALES;
	}



	public void setURL_CONS_SUCURSALES(String uRL_CONS_SUCURSALES) {
		URL_CONS_SUCURSALES = uRL_CONS_SUCURSALES;
	}



	public String getURL_CONS_DOMIC() {
		return URL_CONS_DOMIC;
	}



	public void setURL_CONS_DOMIC(String uRL_CONS_DOMIC) {
		URL_CONS_DOMIC = uRL_CONS_DOMIC;
	}



	public String getURL_CONS_NOMBRE() {
		return URL_CONS_NOMBRE;
	}



	public void setURL_CONS_NOMBRE(String uRL_CONS_NOMBRE) {
		URL_CONS_NOMBRE = uRL_CONS_NOMBRE;
	}



	public String getURL_CONS_ACUERDO() {
		return URL_CONS_ACUERDO;
	}



	public void setURL_CONS_ACUERDO(String uRL_CONS_ACUERDO) {
		URL_CONS_ACUERDO = uRL_CONS_ACUERDO;
	}
}
