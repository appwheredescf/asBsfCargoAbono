package com.bansefi.nss.cargoabono.vo;

public class SaldoAutorizado {
	private String IMP_SDO;

	public SaldoAutorizado(String iMP_SDO) {
		IMP_SDO = iMP_SDO;
	}
	public SaldoAutorizado(){}
	public String getIMP_SDO() {
		return IMP_SDO;
	}
	public void setIMP_SDO(String iMP_SDO) {
		IMP_SDO = iMP_SDO;
	}
}
