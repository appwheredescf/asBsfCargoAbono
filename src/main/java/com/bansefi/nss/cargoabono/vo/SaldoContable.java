package com.bansefi.nss.cargoabono.vo;

public class SaldoContable {
	private String IMP_SDO;

	public SaldoContable(String iMP_SDO) {
		super();
		IMP_SDO = iMP_SDO;
	}
	public SaldoContable(){}
	public String getIMP_SDO() {
		return IMP_SDO;
	}
	public void setIMP_SDO(String iMP_SDO) {
		IMP_SDO = iMP_SDO;
	}
}
