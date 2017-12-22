package com.bansefi.nss.cargoabono.vo;

public class ApuntesCargo {
	private String COD_NRBE_EN;
	private String PRPDAD_CTA;
	private String NUM_SEC_AC;
	private String COD_CTA;
	private String COD_NUMRCO_MONEDA;
	private String NUM_SEC;
	public ApuntesCargo(String cOD_NRBE_EN, String pRPDAD_CTA, String nUM_SEC_AC, String cOD_CTA,
			String cOD_NUMRCO_MONEDA, String nUM_SEC) {
		COD_NRBE_EN = cOD_NRBE_EN;
		PRPDAD_CTA = pRPDAD_CTA;
		NUM_SEC_AC = nUM_SEC_AC;
		COD_CTA = cOD_CTA;
		COD_NUMRCO_MONEDA = cOD_NUMRCO_MONEDA;
		NUM_SEC = nUM_SEC;
	}
	public ApuntesCargo(){}
	public String getCOD_NRBE_EN() {
		return COD_NRBE_EN;
	}
	public void setCOD_NRBE_EN(String cOD_NRBE_EN) {
		COD_NRBE_EN = cOD_NRBE_EN;
	}
	public String getPRPDAD_CTA() {
		return PRPDAD_CTA;
	}
	public void setPRPDAD_CTA(String pRPDAD_CTA) {
		PRPDAD_CTA = pRPDAD_CTA;
	}
	public String getNUM_SEC_AC() {
		return NUM_SEC_AC;
	}
	public void setNUM_SEC_AC(String nUM_SEC_AC) {
		NUM_SEC_AC = nUM_SEC_AC;
	}
	public String getCOD_CTA() {
		return COD_CTA;
	}
	public void setCOD_CTA(String cOD_CTA) {
		COD_CTA = cOD_CTA;
	}
	public String getCOD_NUMRCO_MONEDA() {
		return COD_NUMRCO_MONEDA;
	}
	public void setCOD_NUMRCO_MONEDA(String cOD_NUMRCO_MONEDA) {
		COD_NUMRCO_MONEDA = cOD_NUMRCO_MONEDA;
	}
	public String getNUM_SEC() {
		return NUM_SEC;
	}
	public void setNUM_SEC(String nUM_SEC) {
		NUM_SEC = nUM_SEC;
	}
}
