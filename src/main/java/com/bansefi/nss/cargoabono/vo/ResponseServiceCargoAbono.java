package com.bansefi.nss.cargoabono.vo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ResponseServiceCargoAbono extends ResponseService {
	private String FECHACONTABLE;
	private String FECHAOPERA;
	private String HORAOPERACION;
	private String NUM_SEC;
	private String FECHAVALOR;
	private String TITULAR_NOMB_50;
	private String NOMB_PDV;
	private String COD_PLZ_BANCARIA;
	private String RECIBO_BANSEFI_NOMB_50;
	private String TEXT_CODE;
	private String TEXT_ARG1;
	private String ImpSaldo;
	public String getFECHACONTABLE() {
		return FECHACONTABLE;
	}
	public void setFECHACONTABLE(String fECHACONTABLE) {
		FECHACONTABLE = fECHACONTABLE;
	}
	public String getFECHAOPERA() {
		return FECHAOPERA;
	}
	public void setFECHAOPERA(String fECHAOPERA) {
		FECHAOPERA = fECHAOPERA;
	}
	public String getHORAOPERACION() {
		return HORAOPERACION;
	}
	public void setHORAOPERACION(String hORAOPERACION) {
		HORAOPERACION = hORAOPERACION;
	}
	public String getNUM_SEC() {
		return NUM_SEC;
	}
	public void setNUM_SEC(String nUM_SEC) {
		NUM_SEC = nUM_SEC;
	}
	public String getFECHAVALOR() {
		return FECHAVALOR;
	}
	public void setFECHAVALOR(String fECHAVALOR) {
		FECHAVALOR = fECHAVALOR;
	}
	public String getTITULAR_NOMB_50() {
		return TITULAR_NOMB_50;
	}
	public void setTITULAR_NOMB_50(String tITULAR_NOMB_50) {
		TITULAR_NOMB_50 = tITULAR_NOMB_50;
	}
	public String getNOMB_PDV() {
		return NOMB_PDV;
	}
	public void setNOMB_PDV(String nOMB_PDV) {
		NOMB_PDV = nOMB_PDV;
	}
	public String getCOD_PLZ_BANCARIA() {
		return COD_PLZ_BANCARIA;
	}
	public void setCOD_PLZ_BANCARIA(String cOD_PLZ_BANCARIA) {
		COD_PLZ_BANCARIA = cOD_PLZ_BANCARIA;
	}
	public String getRECIBO_BANSEFI_NOMB_50() {
		return RECIBO_BANSEFI_NOMB_50;
	}
	public void setRECIBO_BANSEFI_NOMB_50(String rECIBO_BANSEFI_NOMB_50) {
		RECIBO_BANSEFI_NOMB_50 = rECIBO_BANSEFI_NOMB_50;
	}
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
	public String getImpSaldo() {
		return ImpSaldo;
	}
	public void setImpSaldo(String impSaldo) {
		ImpSaldo = impSaldo;
	}
	public ResponseServiceCargoAbono(int status, String descripcion, String tEXT_CODE, String tXT_ARG1,
			ArrayList<String> errores, String fECHACONTABLE, String fECHAOPERA, String hORAOPERACION, String nUM_SEC,
			String fECHAVALOR, String tITULAR_NOMB_50, String nOMB_PDV, String cOD_PLZ_BANCARIA,
			String rECIBO_BANSEFI_NOMB_50, String tEXT_CODE2, String tEXT_ARG1, String impSaldo) {
		FECHACONTABLE = fECHACONTABLE;
		FECHAOPERA = fECHAOPERA;
		HORAOPERACION = hORAOPERACION;
		NUM_SEC = nUM_SEC;
		FECHAVALOR = fECHAVALOR;
		TITULAR_NOMB_50 = tITULAR_NOMB_50;
		NOMB_PDV = nOMB_PDV;
		COD_PLZ_BANCARIA = cOD_PLZ_BANCARIA;
		RECIBO_BANSEFI_NOMB_50 = rECIBO_BANSEFI_NOMB_50;
		TEXT_CODE = tEXT_CODE2;
		TEXT_ARG1 = tEXT_ARG1;
		ImpSaldo = impSaldo;
	}	
	public ResponseServiceCargoAbono(){}
}
