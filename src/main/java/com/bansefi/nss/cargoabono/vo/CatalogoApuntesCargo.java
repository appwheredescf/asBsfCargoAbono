package com.bansefi.nss.cargoabono.vo;

import java.util.ArrayList;

public class CatalogoApuntesCargo {
	private ArrayList<ApuntesCargo> PSV_APUNTE_V;
	private String FECHA_VALOR;
	public CatalogoApuntesCargo(ArrayList<ApuntesCargo> pSV_APUNTE_V, String fECHA_VALOR) {
		PSV_APUNTE_V = pSV_APUNTE_V;
		FECHA_VALOR = fECHA_VALOR;
	}
	public CatalogoApuntesCargo(){}
	public ArrayList<ApuntesCargo> getPSV_APUNTE_V() {
		return PSV_APUNTE_V;
	}
	public void setPSV_APUNTE_V(ArrayList<ApuntesCargo> pSV_APUNTE_V) {
		PSV_APUNTE_V = pSV_APUNTE_V;
	}
	public String getFECHA_VALOR() {
		return FECHA_VALOR;
	}
	public void setFECHA_VALOR(String fECHA_VALOR) {
		FECHA_VALOR = fECHA_VALOR;
	}
}
