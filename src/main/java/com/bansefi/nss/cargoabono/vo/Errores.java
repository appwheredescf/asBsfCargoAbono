package com.bansefi.nss.cargoabono.vo;

public class Errores {
	private String TEXT_CODE;
	private String TEXT_ARG1;
	public Errores(String tEXT_CODE, String tEXT_ARG1) {
		TEXT_CODE = tEXT_CODE;
		TEXT_ARG1 = tEXT_ARG1;
	}
	public Errores(){}
	public String getTEXT_CODE() {
		return TEXT_CODE;
	}
	public void setTEXT_CODE(String tEXT_CODE) {
		TEXT_CODE = tEXT_CODE;
	}
	public String getTEXT_ARG1() {
		return TEXT_ARG1;
	}
	public void setTEXT_ARG1(String tEXT_ARG1) {
		TEXT_ARG1 = tEXT_ARG1;
	}
	
}
