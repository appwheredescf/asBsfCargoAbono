package com.bansefi.nss.cargoabono.vo;

import java.util.ArrayList;

public class ResponseService {
	private int status;
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
	public ResponseService(int status, String descripcion, String tEXT_CODE, String tXT_ARG1,
			ArrayList<String> errores) {
		this.status = status;
		this.descripcion = descripcion;
		TEXT_CODE = tEXT_CODE;
		TXT_ARG1 = tXT_ARG1;
		this.errores = errores;
	}
	public ResponseService(){}
	public String getTEXT_CODE() {
		return TEXT_CODE;
	}
	public void setTEXT_CODE(String tEXT_CODE) {
		TEXT_CODE = tEXT_CODE;
	}
	public String getTXT_ARG1() {
		return TXT_ARG1;
	}
	public void setTXT_ARG1(String tXT_ARG1) {
		TXT_ARG1 = tXT_ARG1;
	}
	public ArrayList<String> getErrores() {
		return errores;
	}
	public void setErrores(ArrayList<String> errores) {
		this.errores = errores;
	}
	private String descripcion;
	private String TEXT_CODE;
	private String TXT_ARG1;
	private String COD_TX;
	private ArrayList<String> errores= new ArrayList<String>();
	public String getCOD_TX() {
		return COD_TX;
	}
	public void setCOD_TX(String cOD_TX) {
		COD_TX = cOD_TX;
	}
	
}
