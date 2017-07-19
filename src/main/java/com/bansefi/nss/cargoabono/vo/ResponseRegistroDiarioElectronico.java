package com.bansefi.nss.cargoabono.vo;

public class ResponseRegistroDiarioElectronico extends ResponseService {
	private DiarioElectronicoRequest registroDiarioElectronico;

	public DiarioElectronicoRequest getRegistroDiarioElectronico() {
		return registroDiarioElectronico;
	}

	public void setRegistroDiarioElectronico(DiarioElectronicoRequest registroDiarioElectronico) {
		this.registroDiarioElectronico = registroDiarioElectronico;
	}
}
