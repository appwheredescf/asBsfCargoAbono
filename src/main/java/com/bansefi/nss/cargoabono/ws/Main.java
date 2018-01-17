
package com.bansefi.nss.cargoabono.ws;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.json.JSONObject;

import com.bansefi.nss.cargoabono.commons.FuncionesEncryption;
import com.bansefi.nss.cargoabono.process.CargoAbono;
import com.bansefi.nss.cargoabono.process.ConsultaSaldo;
import com.bansefi.nss.cargoabono.properties.EndpointProperties;
import com.bansefi.nss.cargoabono.services.PasivosAcuerdosServices;
import com.bansefi.nss.cargoabono.tcb.PasivoTcb;
import com.bansefi.nss.cargoabono.vo.RequestConsultaSaldo;
import com.bansefi.nss.cargoabono.vo.ResponseConsultaSaldo;
import com.bansefi.nss.cargoabono.vo.ResponseServiceCargoAbono;


public class Main {


	public static void main(String[] args) throws Exception {

		
		String time1 = "16:00:00";
		String time2 = "19:00:00";

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1 = format.parse(time1);
		Date date2 = format.parse(time2);
		long difference = date2.getTime() - date1.getTime();
		System.out.println("Dif "+difference+"  "+(difference/6000)/60);
		
		
		
		String entidad ="0166";
		String terminal ="12000122";//"12012103";
		String centro="0001";
		CargoAbono cargoAbono = new CargoAbono();
		//cargoAbono.ProcesaPendientes(entidad, terminal, centro);
		
		
		Calendar calendario = new GregorianCalendar();
		int hora, minutos, segundos;
		hora =calendario.get(Calendar.HOUR_OF_DAY);
		minutos = calendario.get(Calendar.MINUTE);
		segundos = calendario.get(Calendar.SECOND);
		System.out.println(hora + ":" + minutos + ":" + segundos);
		
		
		
		EndpointProperties oPro = new EndpointProperties();
		String StrVi = oPro.getENCRIPDESC_IV();
		String StrKey=oPro.getENCRIPDESC_KEY();
		
		FuncionesEncryption oFunc = new FuncionesEncryption(StrVi,StrKey);
		//String CadEncrip = oFunc.encrypt(StrCadEncrip);
		String StrCadEncrip="GkrXIfANPCXxEbylpmwLIG7/qXWl9Ct3zsUVLK27IKw7ZBMe7hTs5RXWEoGetmskKVHXblkbYgSXlfC2lpM3CkfiNmaGR1pPt7wdiQNjrMLDK92coe1SwS5a9BmAdnwJT/YforyIJJvYcbjG0yOqEjUOaY69rmZU+yJ2WpG1psAilnl5h91BtDjlbV+LEkAMNtDohS9n1n5ENNVh3o4WTcRGKVrwwwqWvd6/Qw4Uag5sZyIBt+nkFM/p5RYb4DFlzT9dH1OjqKCGlh3UPVyNq3exTcocwU5frX+dR4IknOCxKBQ3BxMG9f9szNhCcTQZrmhmrghIqqBnNVcvTAAOdLIgAE8mJFrF4cOidi2FnGsJnqDD1VXFUQJZrrLo6CPlmDk8uZyn4cGJUrjQ3pGSifDFIpTjojoHnYu76ZDJeYESXFkeYglwfTMEm539CVsZmdZifnz/18l/yrV1JcLHyrfaXbcoiJumRJ7YW/LnFZ5OZHDaFj51CW33471487tlAAF9pfOyBAJ8gHW6ycFGZdAYX1iBZ5BjR0lFu7rmEKw=";
		//String CadDesc =  oFunc.decrypt(StrCadEncrip);
		
		
		
		
		String HrOper="11:04:00";
		String nombreCliente="JOSE HERNANDEZ";
		String acuerdo ="0061701322";//"22012512";// ;"259719532";
		String sucursal="0001";//"0121";
		String empleado ="DESA0002";//"E1662129";
		String idExterno="51";
		
		ServiciosCargoAbono oc = new ServiciosCargoAbono();
		//String StResult =oc.GeneraComprobate(terminal,entidad,centro,empleado);

		//System.out.println(oc.GeneraComprobateSaldo("E1660384", "Bansef13", entidad, sucursal, "12000122", empleado, acuerdo, "14:16:00", nombreCliente, idExterno));
		
		
		String Producto="CUENTAHORRO";
		String Importe="17.00";
		String TipOper="A";
		String FecValor="2017-12-01";
		String Concepto="PRUEBA QA";
		String FecOper="2017-12-01";
		
		String IdExt="51";
		String StrClop="99";
		String StrSubClop="0002";
		
		
		
		String tipoIdExterno="IFE";
		String cajaInt="I";
		
		String concepto="Uno ";
		concepto= String.format("%-90s", concepto);
		System.out.print("" +concepto);
		
		
		String uniqueID = UUID.randomUUID().toString();
		System.out.println(" " +uniqueID);
		String FolioTrans="b3887404-3c12-ae10-c477-7152f61e618f";

		String ImpLetra="";
		String Clabe="";
		String StrHoraOper="";
		String SgnCtbleDi="";
		String SrIdMov="111";
		String producto="";
		String idexterno="";
		//String StrCadEncrip = "{\"acuerdo\":\""+acuerdo+"\",\"impNom\":\""+Importe+"\",\"concepto\":\""+concepto+"\",\"nombreCliente\":\""+nombreCliente+"\",\"producto\":\""+producto+"\",\"idexterno\":\""+idexterno+"\",\"tipoIdExterno\":\""+tipoIdExterno+"\",\"folio\":\""+SrIdMov+"\",\"SigCont\":\""+SgnCtbleDi+"\",\"HoraPc\":\""+StrHoraOper+"\",\"CajInt\":\""+cajaInt+"\",\"Clabe\":\""+Clabe+"\",\"ImpLetr\":\""+ImpLetra+"\"}";

		
		/*response = cargoAbono.Procesar("DESA0002","JOAMO009", entidad, 
				 sucursal,	 empleado, 
				 terminal,	 acuerdo, 
				 "C",		 FecValor, 
				 Importe,		 Concepto, 
				 FecOper, HrOper,
				 cajaInt,		 nombreCliente, 
				 Producto,	 IdExt, 
				 tipoIdExterno
				 ,FolioTrans);*/
		//String resp = oc.CargoAbono("E1660384", "Bansef14", entidad, sucursal, empleado, "12000122", acuerdo, "A", FecValor, "1.00", "Prueba abono caja", FecOper, HrOper, "C", nombreCliente, producto, idexterno, tipoIdExterno, FolioTrans);
		//System.out.println(resp);
		
		//String resp1 = oc.CargoAbonoIntervencion("E1660384", "Bansef14", "0166", "0001", "E1660384", "12000122", "0061701322", "C", "2018-01-17", "1.00", "Prueba abono intervencion", "2018-01-17", "13:49:23", "I", "JOSE HERNANDEZ", "CUENTAHORRO", "DVI        ", "DVI        ", "01", "0002", "06f2f9e3-f41d-dc40-54f7-bfaaa0d8b25dg");
		//System.out.println("Cargo Intervencion -->" + resp1);
	}
}
