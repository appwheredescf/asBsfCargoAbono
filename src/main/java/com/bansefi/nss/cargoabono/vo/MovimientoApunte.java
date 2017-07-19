package com.bansefi.nss.cargoabono.vo;

import java.util.ArrayList;

public class MovimientoApunte {
	
	private String status;
	private String errorDesc;
	private ArrayList<AF_APNTE_AUDIT_V> afApnteAuditVs;
	private AF_APNTE_AUDIT_V ultimaTransaccion;
	
	public class AF_APNTE_AUDIT_V {
		private AF_APNTE_E afApnte;
		private AF_AUDIT_AUX afAuditAux;
		public AF_APNTE_E getAfApnte() {
			return afApnte;
		}
		public void setAfApnte(AF_APNTE_E afApnte) {
			this.afApnte = afApnte;
		}
		public AF_AUDIT_AUX getAfAuditAux() {
			return afAuditAux;
		}
		public void setAfAuditAux(AF_AUDIT_AUX afAuditAux) {
			this.afAuditAux = afAuditAux;
		}
	}
	
	public class AF_APNTE_E{
		private String COD_NRBE_EN;
		private String PRPDAD_CTA;
		private String NUM_SEC_AC;
		private String ID_EXP_RECLAM;
		private String COD_CTA;
		private String COD_NUMRCO_MONEDA;
		private String NUM_SEC;
		private String SGN;
		private String IMP_APNTE;
		private String FECHA_CTBLE;
		private String FECHA_VALOR;
		private String IND_1;
		private String IND_2;
		private String IND_3;
		private String IND_4;
		private String IND_5;
		private String IND_6;
		private String IND_7;
		private String IND_8;
		private String IND_9;
		private String IND_10;
		private String COD_ORGN_APNTE;
		private String CONCPT_APNTE;
		private String COD_ORIGEN;
		private String COD_INTERNO_UO;
		private String COD_LINEA;
		private String ID_GRP_PD;
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
		public String getID_EXP_RECLAM() {
			return ID_EXP_RECLAM;
		}
		public void setID_EXP_RECLAM(String iD_EXP_RECLAM) {
			ID_EXP_RECLAM = iD_EXP_RECLAM;
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
		public String getSGN() {
			return SGN;
		}
		public void setSGN(String sGN) {
			SGN = sGN;
		}
		public String getIMP_APNTE() {
			return IMP_APNTE;
		}
		public void setIMP_APNTE(String iMP_APNTE) {
			IMP_APNTE = iMP_APNTE;
		}
		public String getFECHA_CTBLE() {
			return FECHA_CTBLE;
		}
		public void setFECHA_CTBLE(String fECHA_CTBLE) {
			FECHA_CTBLE = fECHA_CTBLE;
		}
		public String getFECHA_VALOR() {
			return FECHA_VALOR;
		}
		public void setFECHA_VALOR(String fECHA_VALOR) {
			FECHA_VALOR = fECHA_VALOR;
		}
		public String getIND_1() {
			return IND_1;
		}
		public void setIND_1(String iND_1) {
			IND_1 = iND_1;
		}
		public String getIND_2() {
			return IND_2;
		}
		public void setIND_2(String iND_2) {
			IND_2 = iND_2;
		}
		public String getIND_3() {
			return IND_3;
		}
		public void setIND_3(String iND_3) {
			IND_3 = iND_3;
		}
		public String getIND_4() {
			return IND_4;
		}
		public void setIND_4(String iND_4) {
			IND_4 = iND_4;
		}
		public String getIND_5() {
			return IND_5;
		}
		public void setIND_5(String iND_5) {
			IND_5 = iND_5;
		}
		public String getIND_6() {
			return IND_6;
		}
		public void setIND_6(String iND_6) {
			IND_6 = iND_6;
		}
		public String getIND_7() {
			return IND_7;
		}
		public void setIND_7(String iND_7) {
			IND_7 = iND_7;
		}
		public String getIND_8() {
			return IND_8;
		}
		public void setIND_8(String iND_8) {
			IND_8 = iND_8;
		}
		public String getIND_9() {
			return IND_9;
		}
		public void setIND_9(String iND_9) {
			IND_9 = iND_9;
		}
		public String getIND_10() {
			return IND_10;
		}
		public void setIND_10(String iND_10) {
			IND_10 = iND_10;
		}
		public String getCOD_ORGN_APNTE() {
			return COD_ORGN_APNTE;
		}
		public void setCOD_ORGN_APNTE(String cOD_ORGN_APNTE) {
			COD_ORGN_APNTE = cOD_ORGN_APNTE;
		}
		public String getCONCPT_APNTE() {
			return CONCPT_APNTE;
		}
		public void setCONCPT_APNTE(String cONCPT_APNTE) {
			CONCPT_APNTE = cONCPT_APNTE;
		}
		public String getCOD_ORIGEN() {
			return COD_ORIGEN;
		}
		public void setCOD_ORIGEN(String cOD_ORIGEN) {
			COD_ORIGEN = cOD_ORIGEN;
		}
		public String getCOD_INTERNO_UO() {
			return COD_INTERNO_UO;
		}
		public void setCOD_INTERNO_UO(String cOD_INTERNO_UO) {
			COD_INTERNO_UO = cOD_INTERNO_UO;
		}
		public String getCOD_LINEA() {
			return COD_LINEA;
		}
		public void setCOD_LINEA(String cOD_LINEA) {
			COD_LINEA = cOD_LINEA;
		}
		public String getID_GRP_PD() {
			return ID_GRP_PD;
		}
		public void setID_GRP_PD(String iD_GRP_PD) {
			ID_GRP_PD = iD_GRP_PD;
		}		
	}
	
	public class AF_AUDIT_AUX
	{
		private String ID_INTERNO_TERM_TN;
		private String FECHA_OPRCN;
		private String HORA_OPRCN;
		private String COD_TX;
		private String ID_EMPL_AUT;
		private String ID_INTERNO_EMPL_EP;
		private String COD_NRBE_EN_1;
		private String COD_INTERNO_UO;
		private String COD_INDIC;
		private String FECHA_CTBLE;
		public String getID_INTERNO_TERM_TN() {
			return ID_INTERNO_TERM_TN;
		}
		public void setID_INTERNO_TERM_TN(String iD_INTERNO_TERM_TN) {
			ID_INTERNO_TERM_TN = iD_INTERNO_TERM_TN;
		}
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
		public String getCOD_TX() {
			return COD_TX;
		}
		public void setCOD_TX(String cOD_TX) {
			COD_TX = cOD_TX;
		}
		public String getID_EMPL_AUT() {
			return ID_EMPL_AUT;
		}
		public void setID_EMPL_AUT(String iD_EMPL_AUT) {
			ID_EMPL_AUT = iD_EMPL_AUT;
		}
		public String getID_INTERNO_EMPL_EP() {
			return ID_INTERNO_EMPL_EP;
		}
		public void setID_INTERNO_EMPL_EP(String iD_INTERNO_EMPL_EP) {
			ID_INTERNO_EMPL_EP = iD_INTERNO_EMPL_EP;
		}
		public String getCOD_NRBE_EN_1() {
			return COD_NRBE_EN_1;
		}
		public void setCOD_NRBE_EN_1(String cOD_NRBE_EN_1) {
			COD_NRBE_EN_1 = cOD_NRBE_EN_1;
		}
		public String getCOD_INTERNO_UO() {
			return COD_INTERNO_UO;
		}
		public void setCOD_INTERNO_UO(String cOD_INTERNO_UO) {
			COD_INTERNO_UO = cOD_INTERNO_UO;
		}
		public String getCOD_INDIC() {
			return COD_INDIC;
		}
		public void setCOD_INDIC(String cOD_INDIC) {
			COD_INDIC = cOD_INDIC;
		}
		public String getFECHA_CTBLE() {
			return FECHA_CTBLE;
		}
		public void setFECHA_CTBLE(String fECHA_CTBLE) {
			FECHA_CTBLE = fECHA_CTBLE;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public ArrayList<AF_APNTE_AUDIT_V> getAfApnteAuditVs() {
		return afApnteAuditVs;
	}

	public void setAfApnteAuditVs(ArrayList<AF_APNTE_AUDIT_V> afApnteAuditVs) {
		this.afApnteAuditVs = afApnteAuditVs;
	}

	public AF_APNTE_AUDIT_V getUltimaTransaccion() {
		return ultimaTransaccion;
	}

	public void setUltimaTransaccion(AF_APNTE_AUDIT_V ultimaTransaccion) {
		this.ultimaTransaccion = ultimaTransaccion;
	}
	
}
