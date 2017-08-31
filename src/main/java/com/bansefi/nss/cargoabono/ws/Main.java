
package com.bansefi.nss.cargoabono.ws;

import com.bansefi.nss.cargoabono.process.CargoAbono;


public class Main {


	public static void main(String[] args) throws Exception {
		CargoAbono cargoAbono = new CargoAbono();
		String empleado ="DESA0001";//"E1662129";
		String terminal ="12000105";//"12012103";
		String entidad ="0166";
		String Producto="CUENTAHORRO";
		String Importe="100.00";
		String TipOper="C";
		String FecValor="2017-08-21";
		String Concepto="PRUEBA QA";
		String FecOper="2017-08-30";
		String HrOper="10:07:00";
		String IdExt="51";
		String StrClop="99";
		String StrSubClop="0001";
		String sucursal="0001";//"0121";
		String acuerdo ="70654900";//"22012512";// ;"259719532";
		String nombreCliente="EMILIA RAMIREZ MENDOZA";
		String tipoIdExterno="IFE";
		String cajaInt="I";
		
		cargoAbono.ProcesarIntervencion(entidad, 
				 sucursal,	 empleado, 
				 terminal,	 acuerdo, 
				 TipOper,		 FecValor, 
				 Importe,		 Concepto, 
				 FecOper, HrOper,
				 cajaInt,		 nombreCliente, 
				 Producto,	 IdExt, 
				 tipoIdExterno, StrClop,
				 StrSubClop);

	}
}
