package com.bansefi.nss.cargoabono.vo;

public class RequestConsultaSaldo {
	private String usuario;
	private String password;
	private String entidad;
	private String acuerdo;
	private String terminal;
	
	public RequestConsultaSaldo(String usuario, String password, String entidad, String acuerdo, String terminal) {
		this.usuario = usuario;
		this.password = password;
		this.entidad = entidad;
		this.acuerdo = acuerdo;
		this.terminal = terminal;
	}
	
	public RequestConsultaSaldo(){}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getAcuerdo() {
		return acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	
}
