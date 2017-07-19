package com.bansefi.nss.cargoabono.vo;

public class EntMovCargoAbono 
{
	private String entidad;
	private String sucursal;
	private String terminal;	
	private String empleado;
	private String tipoOperacion;
	private String fechaValor;
	private String fechaOperacion;
	private String horaOperacion;
	private String cajaIn;
	private String dataTrans;
	
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getFechaValor() {
		return fechaValor;
	}
	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}
	public String getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public String getEmpleado() {
		return empleado;
	}
	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}
	public String getHoraOperacion() {
		return horaOperacion;
	}
	public void setHoraOperacion(String horaOperacion) {
		this.horaOperacion = horaOperacion;
	}
	public String getCajaIn() {
		return cajaIn;
	}
	public void setCajaIn(String cajaIn) {
		this.cajaIn = cajaIn;
	}
	public String getDataTrans() {
		return dataTrans;
	}
	public void setDataTrans(String dataTrans) {
		this.dataTrans = dataTrans;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
}
