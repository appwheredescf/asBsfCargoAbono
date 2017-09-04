
package com.bansefi.nss.cargoabono.ws;

import java.util.UUID;

import com.bansefi.nss.cargoabono.process.CargoAbono;
import com.bansefi.nss.cargoabono.process.ConsultaSaldo;


public class Main {


	public static void main(String[] args) throws Exception {
		
		String entidad ="0166";
		String terminal ="12000105";//"12012103";
		String HrOper="10:07:00";
		String nombreCliente="EMILIA RAMIREZ MENDOZA";
		String acuerdo ="70654900";//"22012512";// ;"259719532";
		String sucursal="0001";//"0121";
		String empleado ="DESA0001";//"E1662129";
		String idExterno="51";
		
		ConsultaSaldo csald = new ConsultaSaldo();
		csald.Comprobante(entidad, sucursal, acuerdo, terminal, HrOper, nombreCliente, idExterno, empleado);
		
		
		CargoAbono cargoAbono = new CargoAbono();
		
		
		
		String Producto="CUENTAHORRO";
		String Importe="101.00";
		String TipOper="C";
		String FecValor="2017-08-21";
		String Concepto="PRUEBA QA";
		String FecOper="2017-08-30";
		
		String IdExt="51";
		String StrClop="99";
		String StrSubClop="0001";
		
		
		
		String tipoIdExterno="IFE";
		String cajaInt="I";
		
		String concepto="Uno ";
		concepto= String.format("%-90s", concepto);
		System.out.print("" +concepto);
		
		
		String uniqueID = UUID.randomUUID().toString();
		System.out.println(" " +uniqueID);
		String FolioTrans="611b335d-cbc9-3f62-b530-240acc56418b";
		
		cargoAbono.Procesar(entidad, 
				 sucursal,	 empleado, 
				 terminal,	 acuerdo, 
				 TipOper,		 FecValor, 
				 Importe,		 Concepto, 
				 FecOper, HrOper,
				 cajaInt,		 nombreCliente, 
				 Producto,	 IdExt, 
				 tipoIdExterno
				 ,FolioTrans);

	}
}
