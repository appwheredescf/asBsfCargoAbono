package com.bansefi.nss.cargoabono.vo;

public class ResponseFechaHoraTCB extends ResponseService {
	private String fechaCble;
	private String fechaOprcn;
	private String horaOprcn;
	
	public String getFechaCble() {
		return fechaCble;
	}
	public void setFechaCble(String fechaCble) {
		this.fechaCble = fechaCble;
	}
	public String getFechaOprcn() {
		return fechaOprcn;
	}
	public void setFechaOprcn(String fechaOprcn) {
		this.fechaOprcn = fechaOprcn;
	}
	public String getHoraOprcn() {
		return horaOprcn;
	}
	public void setHoraOprcn(String horaOprcn) {
		this.horaOprcn = horaOprcn;
	}
	
	
}
