
package com.bansefi.nss.cargoabono.ws;

import com.bansefi.nss.cargoabono.process.CargoAbono;


public class Main {


	public static void main(String[] args) throws Exception {
		CargoAbono cargoAbono = new CargoAbono();
		String empleado ="DESA0001";//"E1662129";
		String terminal ="12000102";//"12012103";
		String entidad ="0166";
		String Producto="CUENTAHORRO";
		String Importe="100.00";
		String TipOper="A";
		String FecValor="2017-08-21";
		String Concepto="prueba depura   [1.1]";
		String FecOper="2017-08-30";
		String HrOper="10:07:00";
		String IdExt="11122";
		String StrClop="99";
		String StrSubClop="0002";
		String sucursal="0001";//"0121";
		String acuerdo ="70654900";//"22012512";// ;"259719532";
		String nombreCliente="EMILIA RAMIREZ MENDOZA";
		String tipoIdExterno="Descr";
		String cajaInt="I";
		
		cargoAbono.ProcesarIntervencion(entidad, sucursal, empleado, terminal, acuerdo, TipOper, 
				FecValor, Importe, Concepto, FecOper, HrOper, cajaInt, nombreCliente,
				Producto, IdExt, tipoIdExterno, StrClop, StrSubClop);

	}

	
}
