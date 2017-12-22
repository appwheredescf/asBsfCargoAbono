package com.bansefi.nss.cargoabono.vo;

public class DatosReciboCargo {
	private String NOMB_PDV;
	private String COD_NRBE_EN;
	private String COD_PLZ_BANCARIA;
	private String NUM_SEC_AC;
	private String COD_DIG_CR_UO;
	private String NOMB_50;
	public DatosReciboCargo(String nOMB_PDV, String cOD_NRBE_EN, String cOD_PLZ_BANCARIA, String nUM_SEC_AC,
			String cOD_DIG_CR_UO, String nOMB_50) {
		NOMB_PDV = nOMB_PDV;
		COD_NRBE_EN = cOD_NRBE_EN;
		COD_PLZ_BANCARIA = cOD_PLZ_BANCARIA;
		NUM_SEC_AC = nUM_SEC_AC;
		COD_DIG_CR_UO = cOD_DIG_CR_UO;
		NOMB_50 = nOMB_50;
	}
	public DatosReciboCargo(){}
	public String getNOMB_PDV() {
		return NOMB_PDV;
	}
	public void setNOMB_PDV(String nOMB_PDV) {
		NOMB_PDV = nOMB_PDV;
	}
	public String getCOD_NRBE_EN() {
		return COD_NRBE_EN;
	}
	public void setCOD_NRBE_EN(String cOD_NRBE_EN) {
		COD_NRBE_EN = cOD_NRBE_EN;
	}
	public String getCOD_PLZ_BANCARIA() {
		return COD_PLZ_BANCARIA;
	}
	public void setCOD_PLZ_BANCARIA(String cOD_PLZ_BANCARIA) {
		COD_PLZ_BANCARIA = cOD_PLZ_BANCARIA;
	}
	public String getNUM_SEC_AC() {
		return NUM_SEC_AC;
	}
	public void setNUM_SEC_AC(String nUM_SEC_AC) {
		NUM_SEC_AC = nUM_SEC_AC;
	}
	public String getCOD_DIG_CR_UO() {
		return COD_DIG_CR_UO;
	}
	public void setCOD_DIG_CR_UO(String cOD_DIG_CR_UO) {
		COD_DIG_CR_UO = cOD_DIG_CR_UO;
	}
	public String getNOMB_50() {
		return NOMB_50;
	}
	public void setNOMB_50(String nOMB_50) {
		NOMB_50 = nOMB_50;
	}
}
