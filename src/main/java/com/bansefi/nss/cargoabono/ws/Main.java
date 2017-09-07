
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
		String terminal ="12012103";//"12012103";
		String centro="0121";
		CargoAbono cargoAbono = new CargoAbono();
		cargoAbono.ProcesaPendientes(entidad, terminal, centro);
		
		
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
		String CadDesc =  oFunc.decrypt(StrCadEncrip);
		
		
		
		
		String HrOper="10:07:00";
		String nombreCliente="EMILIA RAMIREZ MENDOZA";
		String acuerdo ="70654900";//"22012512";// ;"259719532";
		String sucursal="0121";//"0121";
		String empleado ="E1662129";//"E1662129";
		String idExterno="51";
		
		ServiciosCargoAbono oc = new ServiciosCargoAbono();
		String StResult =oc.GeneraComprobate(terminal,entidad,centro,empleado);
		
		ConsultaSaldo csald = new ConsultaSaldo();
		csald.Comprobante(entidad, sucursal, acuerdo, terminal, HrOper, nombreCliente, idExterno, empleado);
		
		
		
		
		
		
		String Producto="CUENTAHORRO";
		String Importe="17.00";
		String TipOper="A";
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
		String FolioTrans="b3887404-3c12-ae10-c477-7152f61e6189";

		String ImpLetra="";
		String Clabe="";
		String StrHoraOper="";
		String SgnCtbleDi="";
		String SrIdMov="111";
		String producto="";
		String idexterno="";
		//String StrCadEncrip = "{\"acuerdo\":\""+acuerdo+"\",\"impNom\":\""+Importe+"\",\"concepto\":\""+concepto+"\",\"nombreCliente\":\""+nombreCliente+"\",\"producto\":\""+producto+"\",\"idexterno\":\""+idexterno+"\",\"tipoIdExterno\":\""+tipoIdExterno+"\",\"folio\":\""+SrIdMov+"\",\"SigCont\":\""+SgnCtbleDi+"\",\"HoraPc\":\""+StrHoraOper+"\",\"CajInt\":\""+cajaInt+"\",\"Clabe\":\""+Clabe+"\",\"ImpLetr\":\""+ImpLetra+"\"}";

		//JSONObject jsonResultado = new JSONObject();
		//jsonResultado= cargoAbono.ConsultaPendienteDiario(entidad, terminal, centro);
		//
		
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
