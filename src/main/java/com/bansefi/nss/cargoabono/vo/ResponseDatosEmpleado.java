package com.bansefi.nss.cargoabono.vo;

public class ResponseDatosEmpleado extends ResponseService {
	private String NOMBRE;
	private String FECHA_ALTA;
	private String FECHA_NCTO;
	private String ID_INTERNO_PE;
	private String CARGO;
	
	public String getNOMBRE() {
		return NOMBRE;
	}
	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}
	public String getFECHA_ALTA() {
		return FECHA_ALTA;
	}
	public void setFECHA_ALTA(String fECHA_ALTA) {
		FECHA_ALTA = fECHA_ALTA;
	}
	public String getFECHA_NCTO() {
		return FECHA_NCTO;
	}
	public void setFECHA_NCTO(String fECHA_NCTO) {
		FECHA_NCTO = fECHA_NCTO;
	}
	public String getID_INTERNO_PE() {
		return ID_INTERNO_PE;
	}
	public void setID_INTERNO_PE(String iD_INTERNO_PE) {
		ID_INTERNO_PE = iD_INTERNO_PE;
	}
	public String getCARGO() {
		return CARGO;
	}
	public void setCARGO(String cARGO) {
		CARGO = cARGO;
	}	
}
