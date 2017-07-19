package com.bansefi.nss.cargoabono.vo;

public class ResponseDiarioElectronico {
	private int status;
	private String descripcion;
	private String numsec;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNumsec() {
		return numsec;
	}
	public void setNumsec(String numsec) {
		this.numsec = numsec;
	}
}
