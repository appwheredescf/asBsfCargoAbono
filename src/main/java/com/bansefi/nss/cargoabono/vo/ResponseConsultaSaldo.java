package com.bansefi.nss.cargoabono.vo;

public class ResponseConsultaSaldo extends ResponseService {
	private String SDO_CONTABLE;
	private String SDO_RETEN;
	private String SDO_AUT;
	private String DISPO;
	private String SDO_INCID;
	private String SDO_CONECT;
	private String BLOQUEOS;
	private String RTRN_CD;
	public String getSDO_CONTABLE() {
		return SDO_CONTABLE;
	}
	public void setSDO_CONTABLE(String sDO_CONTABLE) {
		SDO_CONTABLE = sDO_CONTABLE;
	}
	public String getSDO_RETEN() {
		return SDO_RETEN;
	}
	public void setSDO_RETEN(String sDO_RETEN) {
		SDO_RETEN = sDO_RETEN;
	}
	public String getSDO_AUT() {
		return SDO_AUT;
	}
	public void setSDO_AUT(String sDO_AUT) {
		SDO_AUT = sDO_AUT;
	}
	public String getDISPO() {
		return DISPO;
	}
	public void setDISPO(String dISPO) {
		DISPO = dISPO;
	}
	public String getSDO_INCID() {
		return SDO_INCID;
	}
	public void setSDO_INCID(String sDO_INCID) {
		SDO_INCID = sDO_INCID;
	}
	public String getSDO_CONECT() {
		return SDO_CONECT;
	}
	public void setSDO_CONECT(String sDO_CONECT) {
		SDO_CONECT = sDO_CONECT;
	}
	public String getBLOQUEOS() {
		return BLOQUEOS;
	}
	public void setBLOQUEOS(String bLOQUEOS) {
		BLOQUEOS = bLOQUEOS;
	}
	public String getRTRN_CD() {
		return RTRN_CD;
	}
	public void setRTRN_CD(String rTRN_CD) {
		RTRN_CD = rTRN_CD;
	}
}
