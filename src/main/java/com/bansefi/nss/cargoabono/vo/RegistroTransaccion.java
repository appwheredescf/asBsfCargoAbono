package com.bansefi.nss.cargoabono.vo;

public class RegistroTransaccion {
	private String FECHA_OPRCN;
	private String HORA_OPRCN;
	public RegistroTransaccion(String fECHA_OPRCN, String hORA_OPRCN) {
		super();
		FECHA_OPRCN = fECHA_OPRCN;
		HORA_OPRCN = hORA_OPRCN;
	}
	public RegistroTransaccion(){}
	public String getFECHA_OPRCN() {
		return FECHA_OPRCN;
	}
	public void setFECHA_OPRCN(String fECHA_OPRCN) {
		FECHA_OPRCN = fECHA_OPRCN;
	}
	public String getHORA_OPRCN() {
		return HORA_OPRCN;
	}
	public void setHORA_OPRCN(String hORA_OPRCN) {
		HORA_OPRCN = hORA_OPRCN;
	}
}
