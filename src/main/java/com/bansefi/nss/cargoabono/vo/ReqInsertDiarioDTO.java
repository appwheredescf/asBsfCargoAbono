package com.bansefi.nss.cargoabono.vo;

import lombok.Data;
import lombok.experimental.PackagePrivate;

@Data
@PackagePrivate
public class ReqInsertDiarioDTO {
	String idInternoTermTn;
	String numSec;
	String codTx;
	String codTxDi;
	String idInternoEmplEp;
	String contrida;
	String sgnCtbleDi;
	String masMenosDi;
	String numSecAc;
	String impNominal;
	String codNumrcoMoneda;
	String impSdo;
	String codNumrcoMoneda1;
	String codErr1;
	String codErr2;
	String codErr3;
	String codErr4;
	String codErr5;
	String modoTx;
	String situTx;
	String valorDtllTx;
	String idEmplAut;
	String fechaAnul;
	String idTermAnul;
	String idEmplAnul;
	String numSecAnul;
	String codRespuesta;
	String codNrbeEn;
	String codInternoUo;
	String codNrbeEnFsc;
	String codInternoUoFsc;
	String fechaOprcn;
	String horaOprcn;
	String fechaCtble;
	String fechaValor;
	String codClopSist;
	String tipoSbclop;
	String numPuesto;
	String fechaOff;
	String idTermOff;
	String idEmplOff;
	String numSecOff;
	String impNominalX;
	String claveAnulDi;
	String diTextArg1;
	String diTextArg2;
	String diTextArg3;
	String diTextArg4;
	String diTextArg5;
	String fechaPc;
	String horaPc;
	String horaPcFinal;
}
