package com.bansefi.nss.cargoabono.vo;

public class ResponseConsulPendi 
{
	private Integer idMovimiento;
	private String entidad;
	private String tipoOp;
	private String fechaValor;
	private String sucursal;
	private String idTerminal;
	private String idEmpleado;
	private String horaOp;
	private String fechaContable;
	private Integer statusProceso;
	private String dateProceso;
	private String dateCambioStatus;
	private String folioTrans;
	private String dataTrans;
	
	public Integer getIdMovimiento() {
		return idMovimiento;
	}
	public void setIdMovimiento(Integer idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getTipoOp() {
		return tipoOp;
	}
	public void setTipoOp(String tipoOp) {
		this.tipoOp = tipoOp;
	}
	public String getFechaValor() {
		return fechaValor;
	}
	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getIdTerminal() {
		return idTerminal;
	}
	public void setIdTerminal(String idTerminal) {
		this.idTerminal = idTerminal;
	}
	public String getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public String getHoraOp() {
		return horaOp;
	}
	public void setHoraOp(String horaOp) {
		this.horaOp = horaOp;
	}
	public String getFechaContable() {
		return fechaContable;
	}
	public void setFechaContable(String fechaContable) {
		this.fechaContable = fechaContable;
	}
	public Integer getStatusProceso() {
		return statusProceso;
	}
	public void setStatusProceso(Integer statusProceso) {
		this.statusProceso = statusProceso;
	}
	public String getDateProceso() {
		return dateProceso;
	}
	public void setDateProceso(String dateProceso) {
		this.dateProceso = dateProceso;
	}
	public String getDateCambioStatus() {
		return dateCambioStatus;
	}
	public void setDateCambioStatus(String dateCambioStatus) {
		this.dateCambioStatus = dateCambioStatus;
	}
	public String getFolioTrans() {
		return folioTrans;
	}
	public void setFolioTrans(String folioTrans) {
		this.folioTrans = folioTrans;
	}
	public String getDataTrans() {
		return dataTrans;
	}
	public void setDataTrans(String dataTrans) {
		this.dataTrans = dataTrans;
	}
	
}
