package com.bansefi.nss.cargoabono.properties;

import java.util.Properties;

import com.bansefi.nss.cargoabono.commons.Utils;


public class EndpointProperties 
{
	private String consultaClabe;
	private String consultaSaldos;
	private String ultimaTransaccion;
	private String cargoAbono;
	private String getHora;
	private String fechaActual;
	private String datosCentro;
	private String datosEmpleado;
	private String diarioElectronico;
	private String codTxCargo; //Begin AE234
	private String codTxAbono;
	private String codTxDi;
	private String clopCargo;
	private String subClopCargo;
	private String clopAbono;
	private String subClopAbono;
	private String connSucursales;
	private String connBansefi;
	private String connSucUsr;
	private String connSucPwd;
	private String connBanUsr;
	private String connBanPwd;
	private String consultaMovCargoAbono;
	private String registraCargoAbono;
	private String actualizaStatusCargAbono;
	private String urlDb2;
	private String usrDb2;
	private String pwdDb2;
	private String estatusDiaElect;
	private String MsgErrorPaso3;
	private String UrlConsCargAbo;
	private String UrlEncripta;
	private String UrlDesEncripta;
	//End AE234
	
	private Properties prop = Utils.getProperties("com/bansefi/nss/cargoabono/properties/endpoint.properties");
	public EndpointProperties()
	{
		
		this.consultaClabe = prop.getProperty("consultaClabe");
		this.consultaSaldos = prop.getProperty("consultaSaldos");
		this.ultimaTransaccion = prop.getProperty("ultimaTransaccion");
		this.cargoAbono = prop.getProperty("cargoAbono");
		this.getHora = prop.getProperty("getHora");
		this.fechaActual = prop.getProperty("fechaActual");
		this.datosCentro = prop.getProperty("datosCentro");
		this.datosEmpleado = prop.getProperty("datosEmpleado");
		this.diarioElectronico = prop.getProperty("diarioElectronico");
		
		this.codTxCargo=prop.getProperty("codTxCargo");
		this.codTxAbono=prop.getProperty("codTxAbono");
		this.codTxDi=prop.getProperty("codTxDi");
		this.clopCargo=prop.getProperty("clopCargo");
		this.subClopCargo=prop.getProperty("subClopCargo");
		this.clopAbono=prop.getProperty("clopAbono");
		this.subClopAbono=prop.getProperty("subClopAbono");
		this.connSucursales = prop.getProperty("ConnSucursales");
		this.connBansefi= prop.getProperty("ConnBansefi");
		this.connSucUsr =prop.getProperty("ConnSucUsr");
		this.connSucPwd =prop.getProperty("ConnSucPwd");
		this.connBanUsr =prop.getProperty("ConnBanUsr");
		this.connBanPwd =prop.getProperty("ConnBanPwd");
		this.consultaMovCargoAbono =prop.getProperty("consultaMovCargoAbono");
		this.registraCargoAbono=prop.getProperty("registraCargoAbono");
		this.actualizaStatusCargAbono=prop.getProperty("actualizaStatusCargAbono");
		this.urlDb2 =prop.getProperty("urlDb2");
		this.usrDb2 =prop.getProperty("usrDb2");
		this.pwdDb2 =prop.getProperty("pwdDb2");
		this.estatusDiaElect =prop.getProperty("estatusDiaElect");
		this.setMsgErrorPaso3(prop.getProperty("ErrorMsgPaso3"));
		this.UrlConsCargAbo=prop.getProperty("URL_CONSMOVCARG");
		this.UrlEncripta=prop.getProperty("URL_ENCRIPTAR");
		this.UrlDesEncripta=prop.getProperty("URL_DESENCRIP");
	}

	public String getConsultaClabe() {
		return consultaClabe;
	}

	public String getConsultaSaldos() {
		return consultaSaldos;
	}

	public void setConsultaSaldos(String consultaSaldos) {
		this.consultaSaldos = consultaSaldos;
	}

	public String getUltimaTransaccion() {
		return ultimaTransaccion;
	}

	public void setUltimaTransaccion(String ultimaTransaccion) {
		this.ultimaTransaccion = ultimaTransaccion;
	}

	public String getCargoAbono() {
		return cargoAbono;
	}

	public void setCargoAbono(String cargoAbono) {
		this.cargoAbono = cargoAbono;
	}

	public String getGetHora() {
		return getHora;
	}

	public void setGetHora(String getHora) {
		this.getHora = getHora;
	}

	public String getFechaActual() {
		return fechaActual;
	}

	public String getDatosCentro() {
		return datosCentro;
	}

	public void setDatosCentro(String datosCentro) {
		this.datosCentro = datosCentro;
	}

	public String getDatosEmpleado() {
		return datosEmpleado;
	}

	public void setDatosEmpleado(String datosEmpleado) {
		this.datosEmpleado = datosEmpleado;
	}

	public String getDiarioElectronico() {
		return diarioElectronico;
	}

	public void setDiarioElectronico(String diarioElectronico) {
		this.diarioElectronico = diarioElectronico;
	}

	public String getCodTxCargo() {
		return codTxCargo;
	}

	public void setCodTxCargo(String codTxCargo) {
		this.codTxCargo = codTxCargo;
	}

	public String getCodTxAbono() {
		return codTxAbono;
	}

	public void setCodTxAbono(String codTxAbono) {
		this.codTxAbono = codTxAbono;
	}

	public String getCodTxDi() {
		return codTxDi;
	}

	public void setCodTxDi(String codTxDi) {
		this.codTxDi = codTxDi;
	}

	public String getClopCargo() {
		return clopCargo;
	}

	public void setClopCargo(String clopCargo) {
		this.clopCargo = clopCargo;
	}

	public String getSubClopCargo() {
		return subClopCargo;
	}

	public void setSubClopCargo(String subClopCargo) {
		this.subClopCargo = subClopCargo;
	}

	public String getClopAbono() {
		return clopAbono;
	}

	public void setClopAbono(String clopAbono) {
		this.clopAbono = clopAbono;
	}

	public String getSubClopAbono() {
		return subClopAbono;
	}

	public void setSubClopAbono(String subClopAbono) {
		this.subClopAbono = subClopAbono;
	}

	public String getConnSucursales() {
		return connSucursales;
	}

	public void setConnSucursales(String connSucursales) {
		this.connSucursales = connSucursales;
	}

	public String getConnBansefi() {
		return connBansefi;
	}

	public void setConnBansefi(String connBansefi) {
		this.connBansefi = connBansefi;
	}

	public String getConnSucUsr() {
		return connSucUsr;
	}

	public void setConnSucUsr(String connSucUsr) {
		this.connSucUsr = connSucUsr;
	}

	public String getConnSucPwd() {
		return connSucPwd;
	}

	public void setConnSucPwd(String connSucPwd) {
		this.connSucPwd = connSucPwd;
	}

	public String getConnBanUsr() {
		return connBanUsr;
	}

	public void setConnBanUsr(String connBanUsr) {
		this.connBanUsr = connBanUsr;
	}

	public String getConnBanPwd() {
		return connBanPwd;
	}

	public void setConnBanPwd(String connBanPwd) {
		this.connBanPwd = connBanPwd;
	}

	public String getConsultaMovCargoAbono() {
		return consultaMovCargoAbono;
	}

	public void setConsultaMovCargoAbono(String consultaMovCargoAbono) {
		this.consultaMovCargoAbono = consultaMovCargoAbono;
	}

	public String getRegistraCargoAbono() {
		return registraCargoAbono;
	}

	public void setRegistraCargoAbono(String registraCargoAbono) {
		this.registraCargoAbono = registraCargoAbono;
	}

	public String getActualizaStatusCargAbono() {
		return actualizaStatusCargAbono;
	}

	public void setActualizaStatusCargAbono(String actualizaStatusCargAbono) {
		this.actualizaStatusCargAbono = actualizaStatusCargAbono;
	}

	public String getUrlDb2() {
		return urlDb2;
	}

	public void setUrlDb2(String urlDb2) {
		this.urlDb2 = urlDb2;
	}

	public String getUsrDb2() {
		return usrDb2;
	}

	public void setUsrDb2(String usrDb2) {
		this.usrDb2 = usrDb2;
	}

	public String getPwdDb2() {
		return pwdDb2;
	}

	public void setPwdDb2(String pwdDb2) {
		this.pwdDb2 = pwdDb2;
	}

	public String getEstatusDiaElect() {
		return estatusDiaElect;
	}

	public void setEstatusDiaElect(String estatusDiaElect) {
		this.estatusDiaElect = estatusDiaElect;
	}

	public String getMsgErrorPaso3() {
		return MsgErrorPaso3;
	}

	public void setMsgErrorPaso3(String msgErrorPaso3) {
		MsgErrorPaso3 = msgErrorPaso3;
	}

	public String getUrlConsCargAbo() {
		return UrlConsCargAbo;
	}

	public void setUrlConsCargAbo(String urlConsCargAbo) {
		UrlConsCargAbo = urlConsCargAbo;
	}

	public String getUrlEncripta() {
		return UrlEncripta;
	}

	public void setUrlEncripta(String urlEncripta) {
		UrlEncripta = urlEncripta;
	}

	public String getUrlDesEncripta() {
		return UrlDesEncripta;
	}

	public void setUrlDesEncripta(String urlDesEncripta) {
		UrlDesEncripta = urlDesEncripta;
	}

	
}
