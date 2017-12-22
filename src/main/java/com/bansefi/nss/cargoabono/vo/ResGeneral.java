package com.bansefi.nss.cargoabono.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
public class ResGeneral {
	@Getter @Setter
	private String TRANID;
	@Getter @Setter
	private String ESTATUS;
	@Getter @Setter
	private String CODIGO;
	@Getter @Setter
	private String MENSAJE;
	@Getter @Setter
	private String NUMTASK;
}
