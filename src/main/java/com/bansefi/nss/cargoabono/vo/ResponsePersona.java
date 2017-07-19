package com.bansefi.nss.cargoabono.vo;

import lombok.Getter;
import lombok.Setter;

public class ResponsePersona extends ResponseService 
{
	
	@Getter @Setter
	public String nombre;

	@Getter @Setter
	public String ApPaterno;
	
	@Getter @Setter
	public String ApMaterno;
	
	@Getter @Setter
	public String IdInternoPe;
}
