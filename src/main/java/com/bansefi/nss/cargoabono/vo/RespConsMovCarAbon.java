package com.bansefi.nss.cargoabono.vo;

import lombok.Getter;
import lombok.Setter;

public class RespConsMovCarAbon extends ResponseService {
	
	@Setter @Getter
	private String idMovimiento;
	
	@Setter @Getter
	private String Entidad;
	
	@Setter @Getter
	private String TipOper;
	
	@Setter @Getter
	private String FechValor;
	
	@Setter @Getter
	private String CajaInt;
	
	@Setter @Getter
	private String FecOper;
	
	@Setter @Getter
	private String Sucursal;
	
	@Setter @Getter
	private String Terminal;
	
	@Setter @Getter
	private String Empleado;
	
	@Setter @Getter
	private String HoraOper;
	
	@Setter @Getter
	private String FecCont;
	
	@Setter @Getter
	private String StatProc;
	
	@Setter @Getter
	private String DateProc;
	
	@Setter @Getter
	private String FolioTrans;
	
	@Setter @Getter
	private String DataTrans;
}
