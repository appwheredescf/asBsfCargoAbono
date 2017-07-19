package com.bansefi.nss.cargoabono.vo;

import lombok.Getter;
import lombok.Setter;

public class ResponDiaPend extends ResponseService 
{
	@Getter    @Setter
	private String NUMSEC;
	
	@Getter    @Setter
	private String HORA_OPERACION;
	
	@Getter    @Setter
	private int COD_RESPUESTA;
	
	@Getter    @Setter
	private String IMP_SDO;
	
	@Getter    @Setter
	private String TERMINAL;
	
	@Getter    @Setter
	private String FEC_PC;
	
	@Getter    @Setter
	private String HORA_PC;
	
}
