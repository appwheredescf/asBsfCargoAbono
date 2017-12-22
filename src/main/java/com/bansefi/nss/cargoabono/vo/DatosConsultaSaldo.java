package com.bansefi.nss.cargoabono.vo;

public class DatosConsultaSaldo {
	private String COD_NUMRCO_MONEDA;
	private String NOMB_50;
	private String COD_CSB_OF;
	private String COD_INTERNO_UO;
	public DatosConsultaSaldo(String cOD_NUMRCO_MONEDA, String nOMB_50, String cOD_CSB_OF, String cOD_INTERNO_UO) {
		super();
		COD_NUMRCO_MONEDA = cOD_NUMRCO_MONEDA;
		NOMB_50 = nOMB_50;
		COD_CSB_OF = cOD_CSB_OF;
		COD_INTERNO_UO = cOD_INTERNO_UO;
	}
	public DatosConsultaSaldo(){}
	public String getCOD_NUMRCO_MONEDA() {
		return COD_NUMRCO_MONEDA;
	}
	public void setCOD_NUMRCO_MONEDA(String cOD_NUMRCO_MONEDA) {
		COD_NUMRCO_MONEDA = cOD_NUMRCO_MONEDA;
	}
	public String getNOMB_50() {
		return NOMB_50;
	}
	public void setNOMB_50(String nOMB_50) {
		NOMB_50 = nOMB_50;
	}
	public String getCOD_CSB_OF() {
		return COD_CSB_OF;
	}
	public void setCOD_CSB_OF(String cOD_CSB_OF) {
		COD_CSB_OF = cOD_CSB_OF;
	}
	public String getCOD_INTERNO_UO() {
		return COD_INTERNO_UO;
	}
	public void setCOD_INTERNO_UO(String cOD_INTERNO_UO) {
		COD_INTERNO_UO = cOD_INTERNO_UO;
	}
	
}
