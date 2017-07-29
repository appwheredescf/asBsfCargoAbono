package com.bansefi.nss.cargoabono.vo;

import java.util.ArrayList;

public class ResponseService {
	private int status;
	private String descripcion;
	private String TEXT_CODE;
	private String TXT_ARG1;
	private ArrayList<String> errores= new ArrayList<String>();
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
		return TEXT_CODE;
	}
	public void setCOD_TX(String cOD_TX) {
		TEXT_CODE = cOD_TX;
	}
	public String getTXT_ARG1() {
		return TXT_ARG1;
	}
	public void setTXT_ARG1(String tXT_ARG1) {
		TXT_ARG1 = tXT_ARG1;
	}
	
	
}
