
package com.bansefi.nss.cargoabono.ws;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.json.JSONObject;
import com.bansefi.nss.cargoabono.commons.CantidadLetras;
import com.bansefi.nss.cargoabono.commons.EncryptionDecryption;
import com.bansefi.nss.cargoabono.commons.FuncionesEncryption;
import com.bansefi.nss.cargoabono.commons.UtilJson;
import com.bansefi.nss.cargoabono.consInter.CargoAbonoDS;
import com.bansefi.nss.cargoabono.ds.DiarioElectronicoDS;
import com.bansefi.nss.cargoabono.process.CDatosSucursales;
import com.bansefi.nss.cargoabono.process.CargoAbono;
import com.bansefi.nss.cargoabono.process.ConsultaSaldo;
import com.bansefi.nss.cargoabono.properties.EndpointProperties;
import com.bansefi.nss.cargoabono.services.PasivosAcuerdosServices;
import com.bansefi.nss.cargoabono.tcb.PasivoTcb;
import com.bansefi.nss.cargoabono.vo.DiarioElectronicoRequest;
import com.bansefi.nss.cargoabono.vo.EntDescImpr;
import com.bansefi.nss.cargoabono.vo.EntInsertDiarioElect;
import com.bansefi.nss.cargoabono.vo.ReqInserMovCarAbo;
import com.bansefi.nss.cargoabono.vo.ResponseCargoAbono;
import com.bansefi.nss.cargoabono.vo.ResponseFechaActual;
import com.bansefi.nss.cargoabono.vo.ResponseService;
import com.bansefi.nss.cargoabono.vo.ResqConsMovCarAbon;

public class Main {


	public static void main(String[] args) throws Exception 
	{

		CargoAbono cargoAbono = new CargoAbono();		

String empleado ="DESA0001";//"E1662129";
		
		String ID_INTERNO_PE="16396563";
		String ID_DOM="2";
		String FecCn="2015/01/21";
		String terminal ="12012103";//"12012103";
		String entidad ="0166";
		String centro="0121";
		/**/
		

		//CargoAbono prosCargA = new CargoAbono();				

		cargoAbono.ComprobanteCargoAbono(terminal,entidad,centro,empleado);
		
		
		
		//String nombreCliente="Prueba ";
		String idExterno="1112";
		String Producto="CUENTAHORRO";
		String Importe="1.23";
		String TipOper="A";
		String FecValor="2017-08-21";
		String Concepto="prueba depura   [1.1]";
		String FecOper="2017-08-21";
		String HrOper="10:10:57";
		String CajaInt="A";
		String nombreClien="EMILIA RAMIREZ MENDOZA";
		String IdExt="11122";
		
		String StrClop="01";
		String StrSubClop="0001";

		String sucursal="0121";//"0121";
		
		
		String horaOpr="15:22:00";
		
		
		
		String acuerdo ="11267234";//"22012512";// ;"259719532";
		String impNom="100";
		String concepto="Prueba";
		String nombreCliente="EMILIA RAMIREZ MENDOZA";
		String producto="Produc";
		String idexterno="1111";
		String tipoIdExterno="Descr";
		String SrIdMov="11";
		String SgnCtbleDi="A";
		String StrHoraOper="100110";
		String cajaInt="C";
		
		JSONObject oJResp = cargoAbono.Procesar(entidad, 
				sucursal, 
				empleado, 
				terminal, 
				acuerdo, 
				TipOper, 
				FecValor, 
				Importe, 
				Concepto, 
				FecOper, 
				HrOper, 
				CajaInt, 
				nombreClien, 
				Producto, 
				IdExt, 
				tipoIdExterno);
		
		
		EndpointProperties oPro = new EndpointProperties();
		String Strurl = oPro.getUrlEncripta();
		
		
		
		
		JSONObject datosEntrada = new JSONObject();
		datosEntrada.put("text","{\"acuerdo\":\""+acuerdo+"\",\"impNom\":\""+impNom+"\",\"concepto\":\""+concepto+"\",\"nombreCliente\":\""+nombreCliente+"\",\"producto\":\""+producto+"\",\"idexterno\":\""+idexterno+"\",\"tipoIdExterno\":\""+tipoIdExterno+"\",\"folio\":\""+SrIdMov+"\",\"SigCont\":\""+SgnCtbleDi+"\",\"HoraPc\":\""+StrHoraOper+"\",\"CajInt\":\""+cajaInt+"\"}");
		
		String input =datosEntrada.toString();
		
		CDatosSucursales oClip = new CDatosSucursales();
		ResponseService rsp= oClip.EncriptarDescr(input, Strurl);
		
		datosEntrada = new JSONObject();
		 Strurl = oPro.getUrlDesEncripta();
		datosEntrada.put("text",rsp.getDescripcion());
		input =datosEntrada.toString();
		ResponseService oRep= oClip.EncriptarDescr(input, Strurl);
		
		EntDescImpr oR= oClip.SerealObjImp(oRep.getDescripcion());
		
		
		
		CargoAbonoDS oDs = new CargoAbonoDS();
		/*
		ResqConsMovCarAbon request = new ResqConsMovCarAbon();
		request.setEntidad("0166");
		request.setIdEmpleado("E1663380");
		request.setSucursal("0001");
		request.setTerminal("12000132");
		oDs.ConsultMovCargAbon(request);*/
		
		ReqInserMovCarAbo oIns= new ReqInserMovCarAbo();
		oIns.setCajaInt("C");
		oIns.setDataTrans("DataEncript");
		oIns.setEmpleado("E1663380");
		oIns.setEntidad("0166");
		oIns.setFechaOper("24/08/2017");
		oIns.setFechaVal("24/08/2017");
		oIns.setHoraOper("17:30:10");
		oIns.setSucursal("001");
		oIns.setTerminal("12000132");
		oIns.setTipOper("A");
		
		oDs.InsertaMovCargAbon(oIns);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		PasivoTcb Pas = new PasivoTcb();
		Pas.ConsultaClabe(acuerdo,entidad,terminal);
		
		
		
		
		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();
		

		
		
		//String StrCat="";
	 //StrCat=	CantidadLetras.Convertir("1001023",true);
		
		//1501023
		EndpointProperties prop = new EndpointProperties();
		String SrDesc=prop.getMsgErrorPaso3();
		
		
		cargoAbono.ProcesaPendientes(entidad,terminal,centro);
		
		ConsultaSaldo oSal = new ConsultaSaldo();
		//System.out.println(oSal.Comprobante(entidad, sucursal, "22030373", terminal, horaOpr, nombreCliente, "1234566788", "E1662129"));
		//TFin = System.currentTimeMillis();
		//tiempo = TFin - TInicio;
		  //System.out.println("Tiempo de ejecución en milisegundos: " + tiempo);
		/*///cargoAbono.ProcesaPendientes(entidad, terminal, centro);
		
		*/
		
		
		
		
		//CajaInt="I";
		
		
		StrClop="99";
//,StrClop,StrSubClop
		
		//ws.GeneraComprobate(terminal,entidad,centro);		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
/*		
		
		
		
		// TODO Auto-generated method stub
		String entidad ="0166";
		String sucursal="0121";
		String terminal ="12012103";
		String empleado ="E1662129";
		String acuerdo = "259719532";
		String ID_INTERNO_PE="16396563";
		String ID_DOM="2";
		String FecCn="2015/01/23";
	
		CargoAbono cargoAbono = new CargoAbono();

		String horaOpr="19:10:04";
		String nombreCliente="Prueba ";
		String idExterno="1112";
		String Producto="CUENTAHORRO";
		String Importe="2.15";
		String TipOper="C";
		String FecValor="2017-08-01";
		String Concepto="prueba depura   123";
		String FecOper="2017-08-01";
		String HrOper="19:10:57";
		String CajaInt="A";
		String nombreClien="EMILIA RAMIREZ MENDOZA";
		String IdExt="DVI        ";
		String tipoIdExterno="";
		String StrClop="22";
		String StrSubClop="0054";
		JSONObject prueba;
		JSONObject prueba2;

		
		
		
				
		

		
		
		

		
		
		
		
		prueba=cargoAbono.Procesar(entidad, 
				sucursal, 
				empleado, 
				terminal, 
				acuerdo, 
				TipOper, 
				FecValor, 
				Importe, 
				Concepto, 
				FecOper, 
				HrOper, 
				CajaInt, 
				nombreClien, 
				Producto, 
				IdExt, 
				tipoIdExterno);

*/
		
		

		
		

		
		
		//cargoAbono.ConsultaPendienteDiario(entidad,terminal);
		
		
		/*DiarioElectronicoDS ds = new DiarioElectronicoDS();
		//ds.ObtieneUltimoMovPendiente(entidad, terminal);
		
		
		
				
		
		
		
		
		
		cargoAbono.ProcesarIntervencion(entidad, sucursal, empleado, terminal, acuerdo, TipOper, 
				FecValor, Importe, Concepto, FecOper, HrOper, CajaInt, nombreCliente,
				Producto, IdExt, tipoIdExterno, StrClop, StrSubClop);
		
		

		
		

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		PasivoTcb oPas = new PasivoTcb();
		oPas.UltimasTransacciones(entidad, acuerdo, FecCn);
		
		
		
		String StrImporte="1000.00";
		StrImporte= CantidadLetras.FormatoNumero(StrImporte);
		//System.out.println("Valor cadena "+Cad);
		
		
		
		String StrHora="13:58:31.000-06:00";
		StrHora=StrHora.substring(0, 8);
		System.out.println(StrHora);
		
		
		
		
		
		oPas.ConsultaAcuerdo(entidad, acuerdo, terminal);
		
		oPas.ConsultaPersonaXIdInterno(entidad,ID_INTERNO_PE,terminal);
		
		
		
		
		
		
		
		
		
		/*
		 *
		 * cargoAbono.Procesar(entidad, 
				sucursal, 
				empleado, 
				terminal, 
				acuerdo, 
				"C", 
				"2017/07/06", 
				"10.00", 
				"Pruebas EGG .. E234", 
				"2017/07/06", 
				"10:22:34", 
				"C", 
				"PRUEBA E234", 
				" rere", 
				"1234567890", 
			"51");
		 * */
		
		
		
		
		/*ds.ObtieneNumSec(entidad, sucursal, terminal);
		
		//ds.UltimoMovimiento(terminal, entidad);
		
		

		

				

		
		
		
		
		
		PasivoTcb pasivoTcb = new PasivoTcb();
		pasivoTcb.UltimasTransacciones(entidad, acuerdo, "2017/07/03");
		pasivoTcb.UltimasTransacciones("0166","259719532", "2014/03/05");
		
		PasivosAcuerdosServices wsAc= new PasivosAcuerdosServices();
		wsAc.UltimaTransaccion(acuerdo,entidad,terminal,"05/07/2017");
		
		
		
		pasivoTcb.ConsultaClabe(acuerdo, entidad, terminal);
		
		
			
		
		PasivoTcb oPasiv = new PasivoTcb();

		//oPasiv.respSerDomicilio(ID_INTERNO_PE, ID_DOM, entidad, terminal);
		oPasiv.ConsultaEmpleado(entidad, empleado, terminal);
		oPasiv.ConsultaCentro(entidad, sucursal, terminal);

		
		
		
		
		
		
		
		
		//System.out.println(prueba);
		//System.out.println(prueba2);
		//ds.RegistraCargoAbono(entidad, sucursal, terminal, empleado, "C", "terst", "458.5", "4325", "51485", "0", "2017/05/01", "2017/05/01", "12:00:00");
		
		//pasivoTcb.Abono("0166","259719532","50","pruebas","12012103");

		//prosCargA.ConsultaPendientes(entidad, sucursal, terminal, empleado);

	

		
		//
		
		
		//JSONObject oResp= prosCargA.Procesar("0166", "0001", "DESA0006", "12000108", "11267234", "X", "2017/06/09", "1", "prueba", "2017/06/09", "01:01:01", "C", "Prueba", "11", "1355063", "");
		//System.out.println("Salida  "+oResp.toString());
		
		
		
		
		/*
		String StrFec = prosCargA.ObtieneFecHoy();
		
		System.out.println("Fecha :"+ StrFec);

		EndpointProperties oProper = new EndpointProperties();
		String UrlBd2 = oProper.getUrlDb2();
		String UsrBd2 = oProper.getUsrDb2();
		String PwdBd2 = oProper.getPwdDb2();
		
		BD2Connect bdConn = new BD2Connect();
		boolean Status =bdConn.dbOpenConnect(UrlBd2,UsrBd2,PwdBd2);
		
		if(Status)
		{
			System.out.println("Realiza conexion BD2 ");
		}else
		{
			System.out.println("--- No Realiza conexion BD2 -----");
		}
		
		EntInsertDiarioElect oDia = new EntInsertDiarioElect();
		oDia = prosCargA.InitDiario(oDia);
		
		
		oDia.setCONTRIDA("C");				
		oDia.setSGN_CTBLE_DI("D");
		oDia.setCOD_CLOP_SIST("01");		oDia.setNUM_PUESTO("03");
		oDia.setCOD_ERR_1("0");
		oDia.setCOD_ERR_2("0");				oDia.setCOD_ERR_3("0");
		oDia.setCOD_ERR_4("0");				oDia.setCOD_ERR_5("0");
		oDia.setHORA_OPRCN("00:00:00");
		oDia.setHORA_PC(null);				oDia.setHORA_PC_FINAL(null);
		oDia.setCOD_INTERNO_UO("0001");		oDia.setCOD_INTERNO_UO_FSC(null);
		oDia.setCOD_NRBE_EN("0166");		oDia.setCOD_NRBE_EN_FSC(null);
		oDia.setCOD_TX_DI("xxxx");			oDia.setTIPO_SBCLOP("0001");
		oDia.setFECHA_ANUL(null);			oDia.setFECHA_CTBLE(null);
		oDia.setFECHA_OFF(null);			oDia.setFECHA_OPRCN("2017-06-15");
		oDia.setFECHA_PC(null);				oDia.setFECHA_VALOR(null);
		oDia.setNUM_SEC("574662");			oDia.setCOD_TX("DVI43OOU");
		oDia.setID_EMPL_AUT("");
		oDia.setID_EMPL_OFF("");			oDia.setID_INTERNO_EMPL_EP("E1662129");
		oDia.setID_INTERNO_TERM_TN("12012103");	
		oDia.setID_TERM_OFF("");			oDia.setNUM_SEC_AC("259312650");
		oDia.setIMP_NOMINAL("0");
		oDia.setIMP_SDO("0");				oDia.setDI_TEXT_ARG_1("");
		oDia.setDI_TEXT_ARG_2("");			oDia.setDI_TEXT_ARG_3("");
		oDia.setDI_TEXT_ARG_4("");			oDia.setDI_TEXT_ARG_5("");
		oDia.setCLAVE_ANUL_DI("");			
		*/
		
		
		/*
		oDia.setCONTRIDA("C");				oDia.setMAS_MENOS_DI("");
		oDia.setMODO_TX("0");				oDia.setSGN_CTBLE_DI("D");
		oDia.setCOD_CLOP_SIST("01");		oDia.setNUM_PUESTO("03");
		oDia.setSITU_TX("00");				oDia.setCOD_ERR_1("0");
		oDia.setCOD_ERR_2("0");				oDia.setCOD_ERR_3("0");
		oDia.setCOD_ERR_4("0");				oDia.setCOD_ERR_5("0");
		oDia.setCOD_RESPUESTA("1");			oDia.setCOD_NUMRCO_MONEDA("MXN");
		oDia.setCOD_NUMRCO_MONEDA1("MXN");	oDia.setHORA_OPRCN("00:00:00");
		oDia.setHORA_PC(null);				oDia.setHORA_PC_FINAL(null);
		oDia.setCOD_INTERNO_UO("0001");		oDia.setCOD_INTERNO_UO_FSC(null);
		oDia.setCOD_NRBE_EN("0166");		oDia.setCOD_NRBE_EN_FSC(null);
		oDia.setCOD_TX_DI("xxxx");			oDia.setTIPO_SBCLOP("0001");
		oDia.setFECHA_ANUL(null);			oDia.setFECHA_CTBLE(null);
		oDia.setFECHA_OFF(null);			oDia.setFECHA_OPRCN("2017-06-15");
		oDia.setFECHA_PC(null);				oDia.setFECHA_VALOR(null);
		oDia.setNUM_SEC_ANUL("0");  		oDia.setNUM_SEC_OFF("0");
		oDia.setNUM_SEC("574662");			oDia.setCOD_TX("DVI43OOU");
		oDia.setID_EMPL_ANUL("0");			oDia.setID_EMPL_AUT("");
		oDia.setID_EMPL_OFF("");			oDia.setID_INTERNO_EMPL_EP("E1662129");
		oDia.setID_INTERNO_TERM_TN("12012103");	oDia.setID_TERM_ANUL("0");
		oDia.setID_TERM_OFF("");			oDia.setNUM_SEC_AC("259312650");
		oDia.setIMP_NOMINAL_X("");			oDia.setIMP_NOMINAL("0");
		oDia.setIMP_SDO("0");				oDia.setDI_TEXT_ARG_1("");
		oDia.setDI_TEXT_ARG_2("");			oDia.setDI_TEXT_ARG_3("");
		oDia.setDI_TEXT_ARG_4("");			oDia.setDI_TEXT_ARG_5("");
		oDia.setCLAVE_ANUL_DI("");			oDia.setVALOR_DTLL_TX("Prueba ejecuta E234 ...[1]");
		*/
		
		/*
		if(Status)
		{
			ResponseService oRespDia = bdConn.InsertDiarioElect(oDia);	
		}*/
		
		
		/*
		String cadTest="";
		cadTest="EOAVf2R8uTvkeo2mnKqipZcH3efRaGG8gIjm7HcRWnBNQqU7G26tSJVqGwAOK8402nO0WqPVeS/Qm2/IlQqYjbESj5Otgsv51axw/naZu+xMLX1KX24FpE6Z+n93ciwcJcGw4CyzWzj6OAu4sZyy7d5ZZLzg4FTzFxwuk0FJH9qSHNNz/91UuFJhE44oV7FKc6Sowq234gaUhIfMdebOmdsziS9kvDyM0YdO5lvaVTaLy3QjjVlApz7fwe50oQIEFztkLZBTSKyENZ0sEpuKTOf9IQrvmz2rOeV99XEOdVdvkFBOKfKQ8hhFIVbOlyqxQSyAmdlq6Flpcy9swLx6YA==";
		
		FuncionesEncryption FEncod = new FuncionesEncryption("1234567890123456", "%RVGYTYH/BY%EV#$");
		byte[] encryptedTextBytes = decodeBase64(cadTest.getBytes());
		*/
		
		/*
FuncionesEncryption functionEncryption = new FuncionesEncryption("1234567890123456", "1234567890123456");
		
		String t = functionEncryption.encrypt("Example Datos");// funcionesEncryp = new FuncionesEncryption("9ee153a3df56965e7baf13a7fa1075cc", );
		String d = functionEncryption.decrypt("QpTZGleHIt98bU+EXagZDQ=a");
		System.out.println("Encriptado: "+t+" ::  DesEncrip:  "+d);
		
		  
		FuncionesEncryption FEncrip = new FuncionesEncryption("1234567890123456", "%RVGYTYH/BY%EV#$");
		
		
String StrResult ="";
		cadTest="EOAVf2R8uTvkeo2mnKqipZcH3efRaGG8gIjm7HcRWnBNQqU7G26tSJVqGwAOK8402nO0WqPVeS/Qm2/IlQqYjbESj5Otgsv51axw/naZu+xMLX1KX24FpE6Z+n93ciwcJcGw4CyzWzj6OAu4sZyy7d5ZZLzg4FTzFxwuk0FJH9qSHNNz/91UuFJhE44oV7FKc6Sowq234gaUhIfMdebOmdsziS9kvDyM0YdO5lvaVTaLy3QjjVlApz7fwe50oQIEFztkLZBTSKyENZ0sEpuKTOf9IQrvmz2rOeV99XEOdVdvkFBOKfKQ8hhFIVbOlyqxQSyAmdlq6Flpcy9swLx6YA==";
		StrResult=FEncrip.decrypt(cadTest);
		
		System.out.print(" ::  "+StrResult);
		*/
		
		
		
	//	
		
		//String test = funcionesEncryp.encrypt("Hola");
		
		/* Begin E234 */
		

/*
String CadEncrip="{  'DATATRANS': {    'ACUERDO': '0094060167',    'IMPORTE': '1234567',    'NOMBRECLIENTE': 'NOMBRE00000001 PATERNO00000001 MATERNO00000001',    'CONCEPTO': 'VER CONTADORES CAJA PUESTO',    'IDORIGENAPNT': '0166    0094060167'  }}";
 cadTest ="";// FEnc.encrypt(CadEncrip);
System.out.println(" ::  "+cadTest);
		
		
		
		
		
		
		*/
		//JSONObject oResp02 =prosCargA.ConsultaPendientes(entidad, sucursal, terminal, empleado);
		//System.out.println("Salida  "+oResp02.toString());
		
		
		
		/* End E234 */
		/*
		EndpointProperties endpointProperties = new EndpointProperties();
		CantidadLetras funtionsComm = new CantidadLetras();
		String f = funtionsComm.Convertir("1,635,346.56", true);
		
		
		
		ConsultaSaldo prosConsultaSaldo = new ConsultaSaldo();
		prosConsultaSaldo.Comprobante("0166", "0001", "10895399", "12000108", "01:01:01", "EULALIA MARTINEZ HERNANDEZ","1355063","DESA0006");
		*/
		//prosConsultaSaldo.Comprobante("0166", "70603253", "12000129");
		
		//f.length();*/
	}

	
}
