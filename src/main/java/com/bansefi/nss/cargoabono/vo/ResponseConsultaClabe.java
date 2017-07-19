package com.bansefi.nss.cargoabono.vo;

public class ResponseConsultaClabe extends ResponseService {
	private String COD_NRBE_CLABE_V;
	private String COD_PLZ_BANCARIA;
	private String NUM_SEC_AC_CLABE_V;
	
	
	public String getCOD_NRBE_CLABE_V() {
		return COD_NRBE_CLABE_V;
	}
	public void setCOD_NRBE_CLABE_V(String cOD_NRBE_CLABE_V) {
		COD_NRBE_CLABE_V = cOD_NRBE_CLABE_V;
	}
	public String getCOD_PLZ_BANCARIA() {
		return COD_PLZ_BANCARIA;
	}
	public void setCOD_PLZ_BANCARIA(String cOD_PLZ_BANCARIA) {
		COD_PLZ_BANCARIA = cOD_PLZ_BANCARIA;
	}
	public String getNUM_SEC_AC_CLABE_V() {
		return NUM_SEC_AC_CLABE_V;
	}
	public void setNUM_SEC_AC_CLABE_V(String nUM_SEC_AC_CLABE_V) {
		NUM_SEC_AC_CLABE_V = nUM_SEC_AC_CLABE_V;
	}
}
