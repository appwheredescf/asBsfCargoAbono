package com.bansefi.nss.cargoabono.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class RequestCargoAbono {
	@Getter @Setter
	private String usuario;
	@Getter @Setter
	private String password;
	@Getter @Setter
	private String entidad;
	@Getter @Setter
	private String acuerdo;
	@Getter @Setter
	private String terminal;
	@Getter @Setter
	private String fechaContable;
	@Getter @Setter
	private String horaOp;
	@Getter @Setter
	private String tipoOp;
	@Getter @Setter
	private String importe;
	@Getter @Setter
	private String clop;
	@Getter @Setter
	private String subclop;
	@Getter @Setter
	private String concepto;
	@Getter @Setter
	private String moneda;
	@Getter @Setter
	private String intervencion;
}
