package com.bansefi.nss.cargoabono.vo;

public class ResponseService {
	private int status;
	private String descripcion;
	private String COD_TX;
	private String TXT_ARG1;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCOD_TX() {
		return COD_TX;
	}
	public void setCOD_TX(String cOD_TX) {
		COD_TX = cOD_TX;
	}
	public String getTXT_ARG1() {
		return TXT_ARG1;
	}
	public void setTXT_ARG1(String tXT_ARG1) {
		TXT_ARG1 = tXT_ARG1;
	}
	
	
}
