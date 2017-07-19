package com.bansefi.nss.cargoabono.vo;

public class EntPendienteCargAbono extends ResponseService
{
	private Integer IdMovimiento;
	private String Entidad;
	private String TipoOp;
	private String FechaValor;
	private String CajaInt;
	private String FechaOper;
	private String Sucursal;
	private String IdTerminal;
	private String IdEmpleado;
	private String HoraOper;
	private String FechaContable;
	private String DateProcess;
	private String DateCambioStatus;
	private Integer FolioTrans;
	private String DataTrans;
	private Integer StatusPendiente;
	
	public Integer getIdMovimiento() {
		return IdMovimiento;
	}
	public void setIdMovimiento(Integer idMovimiento) {
		IdMovimiento = idMovimiento;
	}
	public String getEntidad() {
		return Entidad;
	}
	public void setEntidad(String entidad) {
		Entidad = entidad;
	}
	public String getTipoOp() {
		return TipoOp;
	}
	public void setTipoOp(String tipoOp) {
		TipoOp = tipoOp;
	}
	public String getFechaValor() {
		return FechaValor;
	}
	public void setFechaValor(String fechaValor) {
		FechaValor = fechaValor;
	}
	public String getCajaInt() {
		return CajaInt;
	}
	public void setCajaInt(String cajaInt) {
		CajaInt = cajaInt;
	}
	public String getFechaOper() {
		return FechaOper;
	}
	public void setFechaOper(String fechaOper) {
		FechaOper = fechaOper;
	}
	public String getSucursal() {
		return Sucursal;
	}
	public void setSucursal(String sucursal) {
		Sucursal = sucursal;
	}
	public String getIdTerminal() {
		return IdTerminal;
	}
	public void setIdTerminal(String idTerminal) {
		IdTerminal = idTerminal;
	}
	public String getIdEmpleado() {
		return IdEmpleado;
	}
	public void setIdEmpleado(String idEmpleado) {
		IdEmpleado = idEmpleado;
	}
	public String getHoraOper() {
		return HoraOper;
	}
	public void setHoraOper(String horaOper) {
		HoraOper = horaOper;
	}
	public String getFechaContable() {
		return FechaContable;
	}
	public void setFechaContable(String fechaContable) {
		FechaContable = fechaContable;
	}
	public String getDateProcess() {
		return DateProcess;
	}
	public void setDateProcess(String dateProcess) {
		DateProcess = dateProcess;
	}
	public String getDateCambioStatus() {
		return DateCambioStatus;
	}
	public void setDateCambioStatus(String dateCambioStatus) {
		DateCambioStatus = dateCambioStatus;
	}
	public Integer getFolioTrans() {
		return FolioTrans;
	}
	public void setFolioTrans(Integer folioTrans) {
		FolioTrans = folioTrans;
	}
	public String getDataTrans() {
		return DataTrans;
	}
	public void setDataTrans(String dataTrans) {
		DataTrans = dataTrans;
	}
	public Integer getStatusPendiente() {
		return StatusPendiente;
	}
	public void setStatusPendiente(Integer statusPendiente) {
		StatusPendiente = statusPendiente;
	}
}
