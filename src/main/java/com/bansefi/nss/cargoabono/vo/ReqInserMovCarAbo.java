package com.bansefi.nss.cargoabono.vo;

import lombok.Getter;
import lombok.Setter;

public class ReqInserMovCarAbo {
	@Getter @Setter
	private String Entidad;
	
	@Getter @Setter
	private String Sucursal;
	
	@Getter @Setter
	private String Terminal;
	
	@Getter @Setter
	private String Empleado;
	
	@Getter @Setter
	private String TipOper;
	
	@Getter @Setter
	private String FechaVal;
	
	@Getter @Setter
	private String FechaOper;
	
	@Getter @Setter
	private String HoraOper;
	
	@Getter @Setter
	private String CajaInt;
	
	@Getter @Setter
	private String DataTrans;
}
