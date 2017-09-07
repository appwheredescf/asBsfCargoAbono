package com.bansefi.nss.cargoabono.process;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.bansefi.nss.cargoabono.commons.CantidadLetras;
import com.bansefi.nss.cargoabono.commons.FuncionesEncryption;
import com.bansefi.nss.cargoabono.commons.UtilJson;
import com.bansefi.nss.cargoabono.consInter.CargoAbonoDS;
import com.bansefi.nss.cargoabono.ds.DiarioElectronicoDS;
import com.bansefi.nss.cargoabono.properties.EndpointProperties;
import com.bansefi.nss.cargoabono.services.PasivosAcuerdosServices;
import com.bansefi.nss.cargoabono.tcb.PasivoTcb;
import com.bansefi.nss.cargoabono.vo.DiarioElectronicoRequest;
import com.bansefi.nss.cargoabono.vo.EntDescImpr;
import com.bansefi.nss.cargoabono.vo.EntInsertDiarioElect;
import com.bansefi.nss.cargoabono.vo.ReqInserMovCarAbo;
import com.bansefi.nss.cargoabono.vo.RespConsMovCarAbon;
import com.bansefi.nss.cargoabono.vo.ResponDiaPend;
import com.bansefi.nss.cargoabono.vo.ResponseConsultaClabe;
import com.bansefi.nss.cargoabono.vo.ResponseDatosCentro;
import com.bansefi.nss.cargoabono.vo.ResponseFechaActual;
import com.bansefi.nss.cargoabono.vo.ResponsePersona;
import com.bansefi.nss.cargoabono.vo.ResponseRegistroDiarioElectronico;
import com.bansefi.nss.cargoabono.vo.ResponseService;
import com.bansefi.nss.cargoabono.vo.ResponseServiceCargoAbono;
import com.bansefi.nss.cargoabono.vo.ResponseServiceObject;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion;
import com.bansefi.nss.cargoabono.vo.ResqConsMovCarAbon;



public class CargoAbono 
{
	private static final Logger log = LogManager.getLogger(CargoAbono.class);
	private UtilJson utilJson = UtilJson.getInstance();
	
	
	public JSONObject Procesar( String entidad, 
								String sucursal,	String empleado, 
								String terminal,	String acuerdo, 
								String tipoOp,		String fechaValor, 
								String impNom,		String concepto, 
								String fechaOperacion,String horaOp,
								String cajaInt,		String nombreCliente, 
								String producto,	String idexterno, 
								String tipoIdExterno,String folioTrans)
	{
		PasivoTcb pasivoTCB = new PasivoTcb();
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonResultado = new JSONObject();
		boolean StatusOper =false;
		String SrStatus="-1";
		String SrIdMov="-999";
		String SrDesc="";
		String StrFeOper="";
		String StrHoraOper="";
		String Clabe="";
		String conceptoDiario= "";
		StringBuilder builder = new StringBuilder();
		String Ordenante = "";
		conceptoDiario = concepto;
		/*Begin E234*/
		try
		{
			/*Begin Veficar en el SQL que no se repita transaccion*/
			boolean bSatDup =false;
			CargoAbonoDS oDs = new CargoAbonoDS();
			ResqConsMovCarAbon request = new ResqConsMovCarAbon();
			request.setEntidad(entidad);
			request.setSucursal(sucursal);
			request.setTerminal(terminal);
			request.setIdEmpleado("1");
			
			RespConsMovCarAbon oMovC = oDs.ConsultMovCargAbon(request);
			if(oMovC.getStatus()==1)
				if(folioTrans.equals(oMovC.getFolioTrans()))
				{
					bSatDup=true;
					try
					{
						EndpointProperties oPro = new EndpointProperties();
						String StrVi = oPro.getENCRIPDESC_IV();
						String StrKey=oPro.getENCRIPDESC_KEY();
						FuncionesEncryption oFunc = new FuncionesEncryption(StrVi,StrKey);
						String StrDesc = oFunc.decrypt(oMovC.getDataTrans());
						
							CDatosSucursales oClip = new CDatosSucursales();
							EntDescImpr res = new EntDescImpr();
							res= oClip.SerealObjImp(StrDesc);//oRepSer.getDescripcion());
							if(res!=null)
							{
								SrIdMov = res.getFolio();
								StrFeOper=oMovC.getFecOper();
								StrHoraOper=oMovC.getHoraOper();
								Clabe=res.getClabe();
							}
							
						
					}catch(Exception ex){
						
					}
				}
					
			/*End Veficar en el SQL que no se repita transaccion*/
			
			if(!bSatDup)
			{
				impNom =impNom.replace(",", "");
				//C DVI        (13)concepto(30) tipoIdExterno(20) idexterno(20)
				try
				{
					concepto= String.format("%-30s", concepto);
					tipoIdExterno= String.format("%-20s", tipoIdExterno);
					idexterno= String.format("%-20s", idexterno);	
				}catch(Exception ex)
				{
					
				}
				if(tipoOp.equals("A")){
					Ordenante = "INGRESO CAJA 010002";
					builder.append("C DVI      ").append(Ordenante);
					for(int i = builder.length();i<178;i++){
						builder.append(" ");
					}
					builder.append(conceptoDiario). append(" ").append(tipoIdExterno).append(" ").append(idexterno);
				}else{
					
					builder.append("DVI        ").append(conceptoDiario);
					for (int i=builder.length();i<90;i++){
						 builder.append(" ");	
					}
					builder.append(tipoIdExterno).append(" ").append(idexterno);
				}
				
				conceptoDiario = builder.toString();
				//log.info("longitud cadena diario electronico: "+conceptoDiario.length());
				//log.info("cadena diario electronico: "+conceptoDiario);
				concepto = "C DVI        " + concepto + " " + tipoIdExterno + " : " + idexterno+"";
				//Registro de Cargo/Abono en 3 pasos
				//Paso 1
				DiarioElectronicoDS ProcDia = new DiarioElectronicoDS();
				ResponDiaPend RespDia= ProcDia.RegistraCargoAbonoPendiente(entidad, sucursal, terminal, empleado, tipoOp, conceptoDiario, impNom, "0", acuerdo, "0",fechaOperacion,cajaInt);
				
				//Paso 2
				if(RespDia.getStatus()==1) 
				{
					
					
					ResponseService StaSql = InsertSql( acuerdo, entidad, terminal, cajaInt, empleado, StrFeOper, "01-01-1999", StrHoraOper,
           				 sucursal, tipoOp, impNom, concepto, nombreCliente, producto ,
           				 idexterno, tipoIdExterno, RespDia.getNUMSEC(), folioTrans,null,"0");
					
					Clabe =StaSql.getTXT_ARG1();
					String StrAcuerdo ="0000000000".substring(acuerdo.length()) + acuerdo;
					ResponseServiceCargoAbono responseMov =  new ResponseServiceCargoAbono();
	            	switch (tipoOp) 
	            	{
						case "C":
							responseMov = pasivoTCB.Cargo(entidad, StrAcuerdo, impNom, concepto, terminal);
							return jsonResult.put("RespuestaCargoAbono", ""); //No va
							//break; 
						case "A":
							responseMov = pasivoTCB.Abono(entidad, StrAcuerdo, impNom, concepto, terminal);
							return jsonResult.put("RespuestaCargoAbono", ""); //No va
							//break;							
					}
	            	 
	            	if(responseMov.getStatus()==1)
	            	{
	        			//Paso 3
	            		RespDia.setTERMINAL(terminal);
	            		RespDia.setCOD_RESPUESTA(1);//1
	            		//---Verificar NumSec  RespDia.setNUMSEC(responseMov.getNUM_SEC());
	            		RespDia.setIMP_SDO(responseMov.getImpSaldo());
	            		RespDia.setHORA_OPERACION(responseMov.getHORAOPERACION());
	            		ResponseService pResp= ProcDia.ActualizaRegistro(RespDia);
	            		StrHoraOper = responseMov.getHORAOPERACION();
	            		StrFeOper = responseMov.getFECHAOPERA();
	            		
	            		
	            		if(pResp.getStatus()==1)//1
	            		{
	            			
	            			ResponseService StaSqlOK = InsertSql( acuerdo, entidad, terminal, cajaInt, empleado, StrFeOper, "01-01-1999", StrHoraOper,
	                  				 sucursal, tipoOp, impNom, concepto, nombreCliente, producto ,
	                  				 idexterno, tipoIdExterno, RespDia.getNUMSEC(), folioTrans,StaSql.getDescripcion(),"1");
	            			
	            			if(StaSqlOK.getTXT_ARG1()!=null)
	            				if(StaSqlOK.getTXT_ARG1().length()>0)
	            					Clabe = StaSqlOK.getTXT_ARG1();
	            			SrIdMov = RespDia.getNUMSEC();
	        				StatusOper =true;	        
	            		}
	            		else
	            		{
	            			//if(responseMov.getHORAOPERACION()!=null)
	            			//	if(responseMov.getHORAOPERACION().length()>0)
	            			//		RespDia.setHORA_OPERACION(responseMov.getHORAOPERACION());
	            			EndpointProperties prop = new EndpointProperties();
	            			SrDesc=prop.getMsgErrorPaso3();
	        				//SrDesc="El movimiento se registro en tcb, no se actualizo movimiento en diario, favor de conctactar a sistemas";
	        				//responseMov.setDescripcion(SrDesc);    			
	                		//RespDia.setCOD_RESPUESTA(2);
	            			//ResponseService pResp01= ProcDia.ActualizaRegistro(RespDia);
	            			SrStatus="0";
	            			//SrDesc=responseMov.getDescripcion();;
	            		}
	            		
	            	}
	            	else
	            	{
	            		try{
	            			
	            			Calendar calendario = new GregorianCalendar();
	            			int hora, minutos, segundos;
	            			hora =calendario.get(Calendar.HOUR_OF_DAY);
	            			minutos = calendario.get(Calendar.MINUTE);
	            			segundos = calendario.get(Calendar.SECOND);
	            			String HrAc =hora + ":" + minutos + ":" + segundos;
	            			
	            			
		            		RespDia.setTERMINAL(terminal);
		            		RespDia.setCOD_RESPUESTA(0);//1
		            		RespDia.setIMP_SDO("0");
		            		RespDia.setHORA_OPERACION(HrAc);
		            		ResponseService pResp= ProcDia.ActualizaRegistro(RespDia);
	            		}
	            		catch(Exception ex){
	            			
	            		}
	            		SrIdMov= "-999";
	            		SrStatus= "0";
	            		SrDesc=responseMov.getDescripcion();
	            		
	            	}
				}
				else
				{
					RespDia.setCOD_RESPUESTA(0);
					RespDia.setTERMINAL(terminal);
					RespDia.setIMP_SDO(impNom);
					RespDia.setNUMSEC("0");
					
	        		ResponseService pResp= ProcDia.ActualizaRegistro(RespDia);	
					
					SrIdMov= "-998";
	        		SrStatus= "0";
	        		SrDesc="No registra cargo-abono";
	        		
				}	
			}
			else
			{
				/*Ya existe movimiento*/
				StatusOper=true;
				
			}
			
			if(StatusOper)
			{
				jsonResultado.put("idmov", SrIdMov);
				jsonResultado.put("status", "1");
				jsonResultado.put("descripcion","Registro dado de alta");
				jsonResultado.put("FechaOper",StrFeOper);
				jsonResultado.put("HoraOper",StrHoraOper);
				jsonResultado.put("Clabe",Clabe);
				
			}
			else
			{
				jsonResultado.put("idmov", SrIdMov);
				jsonResultado.put("status", SrStatus);
				jsonResultado.put("descripcion",SrDesc);
				
			}
			
		}
		catch(Exception ex)
		{
			jsonResultado.put("idmov", "-999");
			jsonResultado.put("status", "-1");
			jsonResultado.put("descripcion", ex.getMessage());
			log.error("Procesar  - " + ex.getMessage());
		}
		/*End E234*/
		jsonResult.put("RespuestaCargoAbono", jsonResultado);
		return jsonResult;
	}
	/*Begin E234 */

	public ResponseService InsertSql(String acuerdo,String entidad,String terminal,String cajaInt,String empleado,String StrFeOper,String FecValor,String StrHoraOper,
			String sucursal,String tipoOp,String impNom,String concepto,String nombreCliente,String producto ,
			String idexterno,String tipoIdExterno,String SrIdMov,String folioTrans,String dataTrans,String StatProc)
	{
		ResponseService oResp = new ResponseService();
		/*Begin Insert into Table -Intermedia*/
		try
		{
			String StrResul=null;
			String Clabe="";
			ReqInserMovCarAbo oIns= new ReqInserMovCarAbo();
			if(dataTrans == null)
			{
				PasivoTcb pasivoTcb = new PasivoTcb();
				ResponseConsultaClabe oConsClab= pasivoTcb.ConsultaClabe(acuerdo, entidad, terminal);
				Clabe=oConsClab.getCOD_NRBE_CLABE_V()+oConsClab.getCOD_PLZ_BANCARIA()+oConsClab.getNUM_SEC_AC_CLABE_V()+" "+oConsClab.getCOD_DIG_CR_CLABE_V();
							
				
				String SgnCtbleDi="H";
				
				if(tipoOp.equals("C"))
					SgnCtbleDi="D";
				
				String ImpLetra = CantidadLetras.Convertir(impNom,true);
				String StrCadEncrip = "{\"acuerdo\":\""+acuerdo+"\",\"impNom\":\""+impNom+"\",\"concepto\":\""+concepto+"\",\"nombreCliente\":\""+nombreCliente+"\",\"producto\":\""+producto+"\",\"idexterno\":\""+idexterno+"\",\"tipoIdExterno\":\""+tipoIdExterno+"\",\"folio\":\""+SrIdMov+"\",\"SigCont\":\""+SgnCtbleDi+"\",\"HoraPc\":\""+StrHoraOper+"\",\"CajInt\":\""+cajaInt+"\",\"Clabe\":\""+Clabe+"\",\"ImpLetr\":\""+ImpLetra+"\"}";
				EndpointProperties oProp = new EndpointProperties();
				String StrIv = oProp.getENCRIPDESC_IV();
				String StrKey= oProp.getENCRIPDESC_KEY();
				FuncionesEncryption oFunc = new FuncionesEncryption(StrIv,StrKey);
				String CadEncrip = oFunc.encrypt(StrCadEncrip);
				
					oIns.setDataTrans(CadEncrip);
					StrResul =CadEncrip;
			}
			else
			{
				oIns.setDataTrans(dataTrans);
				StrResul =dataTrans;
			}
			oIns.setCajaInt(StatProc);// status proceso
			
			oIns.setEmpleado(empleado);
			oIns.setEntidad(entidad);
			oIns.setFechaOper(StrFeOper);
			oIns.setFechaVal(FecValor);
			oIns.setHoraOper(StrHoraOper);
			oIns.setSucursal(sucursal);
			oIns.setTerminal(terminal);
			oIns.setTipOper(tipoOp);
		
				oIns.setFolioTrans(folioTrans);
				CargoAbonoDS oDsMov = new CargoAbonoDS();
				ResponseService statMov = oDsMov.InsertaMovCargAbon(oIns);
				if(statMov.getStatus()==1){
					oResp.setStatus(1);
					oResp.setDescripcion(StrResul);
					oResp.setTXT_ARG1(Clabe);
				}
					
			//}
					
		}catch(Exception ex){
			System.out.println("Error : "+ex.getMessage());
			oResp.setStatus(-1);
			oResp.setDescripcion(null);
		}
		
		/*End Insert into Table -Intermedia*/
		return oResp;
	}
	
	
	public JSONObject ProcesarIntervencion( String entidad, 
			String sucursal,	String empleado, 
			String terminal,	String acuerdo, 
			String tipoOp,		String fechaValor, 
			String impNom,		String concepto, 
			String fechaOperacion,String horaOp,
			String cajaInt,		String nombreCliente, 
			String producto,	String idexterno, 
			String tipoIdExterno,String StrClop,
			String StrSubClop,String folioTrans)
{
PasivoTcb pasivoTCB = new PasivoTcb();
JSONObject jsonResult = new JSONObject();
JSONObject jsonResultado = new JSONObject();
boolean StatusOper =false;
String SrStatus="-1";
String SrIdMov="-999";
String SrDesc="";
String SrCod="";
String Clabe="";
String StrFeOper="";
String StrHoraOper="";
String conceptoDiario= "";
StringBuilder builder = new StringBuilder();
String Ordenante = "";
conceptoDiario = concepto;

/*Begin E234*/
try
{
	
	/*Begin Veficar en el SQL que no se repita transaccion*/
	boolean bSatDup =false;
	CargoAbonoDS oDs = new CargoAbonoDS();
	ResqConsMovCarAbon request = new ResqConsMovCarAbon();
	request.setEntidad(entidad);
	request.setSucursal(sucursal);
	request.setTerminal(terminal);
	request.setIdEmpleado(empleado);
	
	RespConsMovCarAbon oMovC = oDs.ConsultMovCargAbon(request);
	if(oMovC.getStatus()==1)
		if(folioTrans.equals(oMovC.getFolioTrans()))
		{
			bSatDup=true;
			try
			{
				EndpointProperties oPro = new EndpointProperties();
				String EncripIv = oPro.getENCRIPDESC_IV();
				String EncripKey=oPro.getENCRIPDESC_KEY();
				FuncionesEncryption oFunc = new FuncionesEncryption(EncripIv,EncripKey);
				String CadEncrip = oFunc.decrypt(oMovC.getDataTrans());
				
				//String Strurl = oPro.getUrlDesEncripta();
				//ResponseService oRepSer=EncriptaDes(Strurl,oMovC.getDataTrans());
				//if(oRepSer.getStatus()==1)
				//{
					CDatosSucursales oClip = new CDatosSucursales();
					EntDescImpr res = new EntDescImpr();
					res= oClip.SerealObjImp(CadEncrip);
					if(res!=null)
					{
						SrIdMov = res.getFolio();
						StrFeOper=oMovC.getFecOper();
						StrHoraOper=oMovC.getHoraOper();
						Clabe=res.getClabe();
					}
					
				//}
			}catch(Exception ex){
				
			}
			
			
		}
			
	/*End Veficar en el SQL que no se repita transaccion*/
	
	if(!bSatDup){
		impNom =impNom.replace(",", "");
		try
		{
			concepto= String.format("%-30s", concepto);
			tipoIdExterno= String.format("%-20s", tipoIdExterno);
			idexterno= String.format("%-20s", idexterno);	
		}catch(Exception ex)
		{
			
		}
		
		if(tipoOp.equals("A")){
			Ordenante = "ABONOS VARIOS 990002";
			builder.append("C DVI      ").append(Ordenante);
			for(int i = builder.length();i<178;i++){
				builder.append(" ");
			}
			builder.append(conceptoDiario). append(" ").append(tipoIdExterno).append(" ").append(idexterno);
		}else{
			
			builder.append("DVI        ").append(conceptoDiario);
			for (int i=builder.length();i<90;i++){
				 builder.append(" ");	
			}
			builder.append(tipoIdExterno).append(" ").append(idexterno);
		}
		conceptoDiario = builder.toString();
		
		concepto = "DVI        " + concepto + " " + tipoIdExterno + " " + idexterno;
			//Registro de Cargo/Abono en 3 pasos
			//Paso 1
			DiarioElectronicoDS ProcDia = new DiarioElectronicoDS();
			ResponDiaPend RespDia= ProcDia.RegistraCargoAbonoPendienteInter(entidad, sucursal, terminal, empleado,
					tipoOp, conceptoDiario, impNom, "0", acuerdo, "0",
					fechaOperacion,cajaInt,StrClop,StrSubClop);
			
			//Paso 2
			if(RespDia.getStatus()==1)
			{
			String StrAcuerdo ="0000000000".substring(acuerdo.length()) + acuerdo;
			ResponseServiceCargoAbono responseMov =  new ResponseServiceCargoAbono();
			switch (tipoOp) 
			{
			case "C":
				responseMov = pasivoTCB.CargoIntervencion(entidad, StrAcuerdo, impNom, concepto, terminal,StrClop,StrSubClop);
				break;
			case "A":
				responseMov = pasivoTCB.AbonoIntervencion(entidad, StrAcuerdo, impNom, concepto, terminal,StrClop,StrSubClop);
				break;							
			}
			if(responseMov.getStatus()==1)
			{
			//Paso 3
			RespDia.setTERMINAL(terminal);
			RespDia.setCOD_RESPUESTA(1);
			//RespDia.setNUMSEC(responseMov.getNUM_SEC());
			RespDia.setHORA_OPERACION(responseMov.getHORAOPERACION());
			RespDia.setIMP_SDO(responseMov.getImpSaldo());
			
			ResponseService pResp= ProcDia.ActualizaRegistro(RespDia);
			if(pResp.getStatus()==1)
			{
				SrIdMov = RespDia.getNUMSEC(); //responseMov.getNUM_SEC();
				StatusOper =true;
				
				/*Begin Insert into Table -Intermedia*/
				try
				{
					StrFeOper = responseMov.getFECHAOPERA();
					StrHoraOper = responseMov.getHORAOPERACION();
					
					PasivoTcb pasivoTcb = new PasivoTcb();
					ResponseConsultaClabe oConsClab= pasivoTcb.ConsultaClabe(acuerdo, entidad, terminal);
					Clabe=oConsClab.getCOD_NRBE_CLABE_V()+oConsClab.getCOD_PLZ_BANCARIA()+oConsClab.getNUM_SEC_AC_CLABE_V()+" "+oConsClab.getCOD_DIG_CR_CLABE_V();
					ReqInserMovCarAbo oIns= new ReqInserMovCarAbo();
					oIns.setCajaInt(cajaInt);
					
					oIns.setEmpleado(empleado);
					oIns.setEntidad(entidad);
					oIns.setFechaOper(StrFeOper);
					oIns.setFechaVal(responseMov.getFECHAVALOR());
					oIns.setHoraOper(StrHoraOper);
					oIns.setSucursal(sucursal);
					oIns.setTerminal(terminal);
					oIns.setTipOper(tipoOp);
					
					EndpointProperties prop = new EndpointProperties();
					//String Strurl =prop.getUrlEncripta();
					String EncripIv = prop.getENCRIPDESC_IV();
					String EncripKey=prop.getENCRIPDESC_KEY();
					
					FuncionesEncryption oFunc = new FuncionesEncryption(EncripIv,EncripKey);
					
					JSONObject datosEntrada = new JSONObject();
					
					String SgnCtbleDi="H";
					
					if(tipoOp.equals("C"))
						SgnCtbleDi="D";
					
					String ImpLetra = CantidadLetras.Convertir(impNom,true);
					String StrCadEncrip="{\"acuerdo\":\""+acuerdo+"\",\"impNom\":\""+impNom+"\",\"concepto\":\""+concepto+"\",\"nombreCliente\":\""+nombreCliente+"\",\"producto\":\""+producto+"\",\"idexterno\":\""+idexterno+"\",\"tipoIdExterno\":\""+tipoIdExterno+"\",\"folio\":\""+SrIdMov+"\",\"SigCont\":\""+SgnCtbleDi+"\",\"HoraPc\":\""+StrHoraOper+"\",\"CajInt\":\""+cajaInt+"\",\"Clabe\":\""+Clabe+"\",\"ImpLetr\":\""+ImpLetra+"\"}";
					//datosEntrada.put("text","{\"acuerdo\":\""+acuerdo+"\",\"impNom\":\""+impNom+"\",\"concepto\":\""+concepto+"\",\"nombreCliente\":\""+nombreCliente+"\",\"producto\":\""+producto+"\",\"idexterno\":\""+idexterno+"\",\"tipoIdExterno\":\""+tipoIdExterno+"\",\"folio\":\""+SrIdMov+"\",\"SigCont\":\""+SgnCtbleDi+"\",\"HoraPc\":\""+StrHoraOper+"\",\"CajInt\":\""+cajaInt+"\",\"Clabe\":\""+Clabe+"\",\"ImpLetr\":\""+ImpLetra+"\"}");
					//String input =datosEntrada.toString();
					
					String CadDesc = oFunc.encrypt(StrCadEncrip);
					
					//CDatosSucursales oClip = new CDatosSucursales();
					//ResponseService rsp= oClip.EncriptarDescr(input, Strurl);
					//if(rsp.getStatus()==1)
					//{
						oIns.setFolioTrans(folioTrans);
						oIns.setDataTrans(CadDesc);//rsp.getDescripcion());
						CargoAbonoDS oDsMov = new CargoAbonoDS();
						ResponseService statMov = oDsMov.InsertaMovCargAbon(oIns);
					//}
							
				}catch(Exception ex){
					
				}
				/*End Insert into Table -Intermedia*/
				
				
			}
			else
			{
				RespDia.setCOD_RESPUESTA(2);
				ResponseService pResp01= ProcDia.ActualizaRegistro(RespDia);
				SrStatus="0";
				//SrDesc=pResp.getDescripcion();
				EndpointProperties prop = new EndpointProperties();
				SrDesc=prop.getMsgErrorPaso3(); //"El movimiento se registro en tcb, no se actualizo movimiento en diario, favor de contactar a sistemas";
			}
			
			}
			else
			{
			SrIdMov= "-999";
			SrStatus= "0";
			SrDesc=responseMov.getDescripcion();;
			
			}
			}
			else
			{
			SrIdMov= "-999";
			SrStatus= "0";
			SrDesc="No registra cargo-abono pendiente";/*Error de  directorio*/
			}
			
		
	}else
	{
		StatusOper =true;
	}
	
		
		if(StatusOper)
		{
			jsonResultado.put("idmov", SrIdMov);
			jsonResultado.put("status", "1");
			jsonResultado.put("descripcion","Registro dado de alta");
			jsonResultado.put("FechaOper",StrFeOper);
			jsonResultado.put("HoraOper",StrHoraOper);
			jsonResultado.put("Clabe",Clabe);

		}
		else
		{
			jsonResultado.put("idmov", SrIdMov);
			jsonResultado.put("status", SrStatus);
			jsonResultado.put("descripcion",SrDesc);
			
		}

}
catch(Exception ex)
{
	jsonResultado.put("idmov", "-999");
	jsonResultado.put("status", "-1");
	jsonResultado.put("descripcion", ex.getMessage());
log.error("Procesar  - " + ex.getMessage());
}
/*End E234*/
jsonResult.put("RespuestaCargoAbono", jsonResultado);
return jsonResult;
}
	
	public JSONObject ComprobanteCargoAbono(String terminal,String entidad,String centro,String Empleado)
	{
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonResultado = new JSONObject();

		CargoAbonoDS oCons = new CargoAbonoDS();
		try
		{
			ResqConsMovCarAbon request = new ResqConsMovCarAbon();
			request.setEntidad(entidad);
			request.setTerminal(terminal);
			request.setIdEmpleado("1");//Empleado
			request.setSucursal(centro);
			
			RespConsMovCarAbon oCar = oCons.ConsultMovCargAbon(request);
			if(oCar.getStatus()==1)
			{
				try
				{
					//EndpointProperties oPro = new EndpointProperties();
					//JSONObject datosEntrada = new JSONObject();
					 //String Strurl = oPro.getUrlDesEncripta();
					//datosEntrada.put("text",oCar.getDataTrans());
					//String input =datosEntrada.toString();
					EndpointProperties oPro = new EndpointProperties();
					String EncripIv = oPro.getENCRIPDESC_IV();
					String EncripKey=oPro.getENCRIPDESC_KEY();
					
					FuncionesEncryption oFunc = new FuncionesEncryption(EncripIv,EncripKey);
					
					String StrCadDesc =oFunc.decrypt(oCar.getDataTrans());
					
					CDatosSucursales oClip = new CDatosSucursales();
					//ResponseService oRep= oClip.EncriptarDescr(input, Strurl);
					EntDescImpr res = new EntDescImpr();
					
					//if(oRep.getStatus()==1)
						res= oClip.SerealObjImp(StrCadDesc);//oRep.getDescripcion());	
					
					PasivosAcuerdosServices oWsAcuerdo = new PasivosAcuerdosServices();
					ResponseFechaActual responseFechaActual = oWsAcuerdo.FechaActual();
					String fechaActual = responseFechaActual.getStatus() == 1 ? responseFechaActual.getFecha() : "";

					//String ImporLetra= CantidadLetras.Convertir(res.getImpNom(),true);
					
					String sFecSerie="";
					try
					{
						DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date;
						String DateFormatSerie="ddMMyy";
						date = df.parse(fechaActual + " " + oCar.getHoraOper()); 
						SimpleDateFormat sdfSerie = new SimpleDateFormat(DateFormatSerie);
						sFecSerie = sdfSerie.format(date);
					}
					catch(Exception ex){
						
					}
					
					String StrImport=res.getImpNom();
					StrImport=CantidadLetras.FormatoNumero(StrImport);
					
					jsonResultado.put("fecha", fechaActual);//oResCA.getFechaOperacion());
					jsonResultado.put("hora", oCar.getHoraOper());
					jsonResultado.put("nombre", res.getNombreCliente());
					jsonResultado.put("importe", StrImport);
					jsonResultado.put("folio", res.getFolio());
					jsonResultado.put("producto",res.getProducto());
					jsonResultado.put("importe_letra", "( "+res.getImpLetr()+" )");
					jsonResultado.put("oficina", "");
					jsonResultado.put("contrato", res.getAcuerdo());
					jsonResultado.put("idExterno", res.getTipoIdExterno().trim()+" : " +res.getIdexterno().trim());
					jsonResultado.put("clabe",res.getClabe() );
					String StrSerial = terminal+ " "+ sFecSerie+ res.getHoraPc().replace(":", "");
					StrSerial +="  "+oCar.getCajaInt()+" "+res.getSigCont();

					jsonResultado.put("serial", StrSerial);
				}catch(Exception ex){
					
				}				
				
			}

			else
			{
				jsonResultado.put("status", "-1");
				jsonResultado.put("descripcion", oCar.getDescripcion());
			}
		}
		catch (Exception e) 
		{
			jsonResultado.put("status", "-1");
			jsonResultado.put("descripcion", e.getMessage());
			log.error("ComprobanteCargoAbono  - " + e.getMessage());
		}
		jsonResult.put("RespuestaGeneraComprobate", jsonResultado);

		return jsonResult;
	}
	
	public EntInsertDiarioElect InitDiarioRegApunte(EntInsertDiarioElect oDiario)
	{
		try
		{
			oDiario.setMAS_MENOS_DI("0");
			oDiario.setCOD_NUMRCO_MONEDA("MXN");
			oDiario.setCOD_NUMRCO_MONEDA1("MXN");
			oDiario.setMODO_TX("0");
			oDiario.setSITU_TX("00");
			oDiario.setVALOR_DTLL_TX("Registro Apunte");
			oDiario.setID_TERM_ANUL("0");
			oDiario.setID_EMPL_ANUL("0");
			oDiario.setNUM_SEC_ANUL("0");
			oDiario.setCOD_RESPUESTA("1");
			oDiario.setNUM_SEC_OFF("0");
			oDiario.setIMP_NOMINAL_X("00000000000");
			oDiario.setIMP_SDO("0");
			oDiario.setCOD_ERR_1("0");
			oDiario.setCOD_ERR_2("0");
			oDiario.setCOD_ERR_3("0");
			oDiario.setCOD_ERR_4("0");
			oDiario.setCOD_ERR_5("0");
			oDiario.setID_EMPL_AUT("");
			oDiario.setFECHA_VALOR(null);
			oDiario.setFECHA_OFF(null);
			oDiario.setID_TERM_OFF("");
			oDiario.setID_EMPL_OFF("");
			oDiario.setCLAVE_ANUL_DI("");
			oDiario.setDI_TEXT_ARG_1("");
			oDiario.setDI_TEXT_ARG_2("");
			oDiario.setDI_TEXT_ARG_3("");
			oDiario.setDI_TEXT_ARG_4("");
			oDiario.setDI_TEXT_ARG_5("");
		}
		catch(Exception ex)
		{
			log.error("InitDiario  - " + ex.getMessage());
		}
		return oDiario;
	}
	
	public EntInsertDiarioElect InitDiarioRegCargo(EntInsertDiarioElect oDiario)
	{
		try
		{
			oDiario.setCOD_TX("DVI50COU");
			oDiario.setCOD_TX_DI("xxxx");
			oDiario.setFECHA_ANUL("1900/01/01");
			oDiario.setCLAVE_ANUL_DI("");
			oDiario.setCOD_ERR_1("0");
			oDiario.setCOD_ERR_2("0");
			oDiario.setCOD_ERR_3("0");
			oDiario.setCOD_ERR_4("0");
			oDiario.setCOD_ERR_5("0");
			oDiario.setCOD_NUMRCO_MONEDA("MXN");
			oDiario.setCOD_NUMRCO_MONEDA1("MXN");
			oDiario.setCOD_RESPUESTA("0");
			
			oDiario.setDI_TEXT_ARG_1("");
			oDiario.setDI_TEXT_ARG_2("");
			oDiario.setDI_TEXT_ARG_3("");
			oDiario.setDI_TEXT_ARG_4("");
			oDiario.setDI_TEXT_ARG_5("");
		}
		catch(Exception ex)
		{
			log.error("InitDiario  - " + ex.getMessage());
		}
		return oDiario;
	}
	
	public EntInsertDiarioElect InitDiarioRegAbono(EntInsertDiarioElect oDiario)
	{
		try
		{
			oDiario.setMAS_MENOS_DI("0");
			oDiario.setCOD_NUMRCO_MONEDA("MXN");
			oDiario.setCOD_NUMRCO_MONEDA1("MXN");
			oDiario.setMODO_TX("0");
			oDiario.setSITU_TX("00");
			oDiario.setVALOR_DTLL_TX("Registro Apunte");
			oDiario.setID_TERM_ANUL("0");
			oDiario.setID_EMPL_ANUL("0");
			oDiario.setNUM_SEC_ANUL("0");
			oDiario.setCOD_RESPUESTA("1");
			oDiario.setNUM_SEC_OFF("0");
			oDiario.setIMP_NOMINAL_X("00000000000");
			oDiario.setIMP_SDO("0");
			oDiario.setCOD_ERR_1("0");
			oDiario.setCOD_ERR_2("0");
			oDiario.setCOD_ERR_3("0");
			oDiario.setCOD_ERR_4("0");
			oDiario.setCOD_ERR_5("0");
			oDiario.setID_EMPL_AUT("");
			oDiario.setFECHA_VALOR(null);
			oDiario.setFECHA_OFF(null);
			oDiario.setID_TERM_OFF("");
			oDiario.setID_EMPL_OFF("");
			oDiario.setCLAVE_ANUL_DI("");
			oDiario.setDI_TEXT_ARG_1("");
			oDiario.setDI_TEXT_ARG_2("");
			oDiario.setDI_TEXT_ARG_3("");
			oDiario.setDI_TEXT_ARG_4("");
			oDiario.setDI_TEXT_ARG_5("");
		}
		catch(Exception ex)
		{
			log.error("InitDiario  - " + ex.getMessage());
		}
		return oDiario;
	}
	
	public String ObtieneFecHoy()
	{
		String StrResult="";
		try
		{
			Date fechaActual = new Date();
			DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
			StrResult = formatoFecha.format(fechaActual);
			
		}
		catch(Exception ex)
		{
			log.error("InitDiario  - " + ex.getMessage());
		}
		return StrResult;
	}

	public JSONObject ConsultaPendienteDiario(String entidad,String terminal,String centro)
	{
		JSONObject jsonResultado = new JSONObject();
		JSONObject jDetalle = new JSONObject();
		try	
		{
			DiarioElectronicoDS ds = new DiarioElectronicoDS();
			ResponseServiceObject oDiario =ds.ObtieneUltimoMovPendiente(entidad, terminal,centro);
			if(oDiario.getStatus()==1)
			{
				jDetalle.put("status", "1");
				jDetalle.put("descripcion", "");
				//oDiElecDet.getCodNrbeEn(), oDiElecDet.getNumSecAc(), StrFecCont
			}
			else
			{
				jDetalle.put("status", "0");
				jDetalle.put("descripcion", "");
			}
		}
		catch(Exception ex)
		{
			jDetalle.put("status", "0");
			jDetalle.put("descripcion", ex.getMessage());
		}
		jsonResultado.put("ResConsPendiDiario", jDetalle);
		return jsonResultado;
	}
	
	public JSONObject ProcesaPendientes(String entidad,String terminal,String centro)
	{
		JSONObject jsonResultado = new JSONObject();
		JSONObject jResultDetalle = new JSONObject();
		boolean bProce=false;
		try
		{
			DiarioElectronicoDS ds = new DiarioElectronicoDS();
			ResponseServiceObject oDiario =ds.ObtieneUltimoMovPendiente(entidad, terminal,centro);
			if(oDiario.getStatus()==1)
			{
				DiarioElectronicoRequest oDiElecDet = new DiarioElectronicoRequest();
				oDiElecDet = (DiarioElectronicoRequest) oDiario.getEntReturn();
				//Obtiene movimiento de Pasivo
				PasivoTcb PasTcb = new PasivoTcb();
				String StrFecCont =oDiElecDet.getFechaCtble();
				StrFecCont =StrFecCont.replace("-", "/");
				StrFecCont=StrFecCont.substring(0,10);
				
				ResponseUltimaTransaccion oResMovPas=PasTcb.UltimasTransacciones(oDiElecDet.getCodNrbeEn(), oDiElecDet.getNumSecAc(), StrFecCont);
				//Si encontro movimiento 
				if(oResMovPas.getStatus()==1)
				{
					int IndCon=0;
					ResponDiaPend RespDia = new ResponDiaPend();
					for(int ind=0;ind<oResMovPas.getApuntes().size();ind++)
					{
						String acuerdoPas = oResMovPas.getApuntes().get(ind).getAfApnteE().getNUM_SEC_AC();
						String acuerdoDia = oDiElecDet.getNumSecAc();
						String importPas  = oResMovPas.getApuntes().get(ind).getAfApnteE().getIMP_APNTE();
						String importDia  =	oDiElecDet.getImpNominal();
						String fechaPas   = oResMovPas.getApuntes().get(ind).getAfAuditAux().getFECHA_OPRCN();
						String fechaDia   =	oDiElecDet.getFechaPc();
						String StrNumSec  = oResMovPas.getApuntes().get(ind).getAfApnteE().getNUM_SEC();
						String horaPas    = oResMovPas.getApuntes().get(ind).getAfAuditAux().getHORA_OPRCN();
						String horaDia    = oDiElecDet.getHoraPc();
						String SrCodTxPas =oResMovPas.getApuntes().get(ind).getAfApnteE().getSGN();
						String SrCodTxDia =oDiElecDet.getSgnCtbleDi();
						


						fechaDia =fechaDia.substring(0,10);
						fechaDia=fechaDia.replace("-", "/");

						if(importPas.equals(importDia)&& fechaPas.equals(fechaDia) && SrCodTxPas.equals(SrCodTxDia) && acuerdoPas.equals(acuerdoDia))
						{
									
										String time1 = horaDia;//"16:00:00";
										String time2 = horaPas;

										SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
										Date date1 = format.parse(time1);
										Date date2 = format.parse(time2);
										long difference = date2.getTime() - date1.getTime();
										difference = (difference/1000)/60;										
										
										String StrHrOper = oDiElecDet.getHoraOprcn(); 
										String StrFecPc = oDiElecDet.getFechaPc();
										String StrHrPc = oDiElecDet.getHoraPc();
										
										
										if(difference < 5 )
										{											
											
											
											RespDia.setNUMSEC(StrNumSec);
											RespDia.setHORA_OPERACION(StrHrOper);
											RespDia.setCOD_RESPUESTA(1);
											RespDia.setIMP_SDO(importDia);
											RespDia.setTERMINAL(terminal);
											RespDia.setFEC_PC(StrFecPc);
											RespDia.setHORA_PC(StrHrPc);
						            		
						            		ResponseService pResp= ds.ActualizaRegistro(RespDia);
						            		if(pResp.getStatus()==1)
						            		{
						            			IndCon++;
						            			
						            			/*Actualiza SQL Begin*/
						            			try
						            			{
						            				ResqConsMovCarAbon request = new ResqConsMovCarAbon();
						            				request.setEntidad(entidad);
						            				request.setTerminal(terminal);
						            				request.setIdEmpleado("0");
						            				request.setSucursal(centro);
						            				CargoAbonoDS oCons = new CargoAbonoDS();

						            				RespConsMovCarAbon oCar = oCons.ConsultMovCargAbon(request);
						            				if(oCar.getStatus()==1)
						            				{

						            					ResponseService statIn=InsertSql("",entidad,terminal,"",oCar.getEmpleado(),
						            							fechaPas,oCar.getFechValor(),horaPas,oCar.getSucursal(),oCar.getTipOper(),
						            							oDiElecDet.getImpNominal(),"","","",
						            							"","","",oCar.getFolioTrans(),oCar.getDataTrans(),"1");
						            				}
						            			}
						            			catch(Exception ex){
						            				
						            			}						            			
						            			
						            			/*Actualiza SQL End*/
						            			
						            			bProce=true;
						            			jResultDetalle.put("status", "1");
						            			jResultDetalle.put("descripcion", "Movimiento pendiente actualizado");
						            			jResultDetalle.put("idMov",StrNumSec);
						            		}
						            		else
						            		{
						            			bProce=true;
						            			jResultDetalle.put("status", "-1");
						            			jResultDetalle.put("descripcion", "Movimiento pendiente actualizado");
						            			jResultDetalle.put("idMov",StrNumSec);
						            		}		
						            		break;
										}
										
							}
						
					}//end for
					
					if(IndCon==0)
					{
						RespDia.setNUMSEC(oDiElecDet.getNumSec());
						RespDia.setHORA_OPERACION(oDiElecDet.getHoraOprcn());
						RespDia.setCOD_RESPUESTA(0);
						RespDia.setIMP_SDO("0");
						RespDia.setTERMINAL(terminal);
						RespDia.setFEC_PC(oDiElecDet.getFechaPc());
						RespDia.setHORA_PC(oDiElecDet.getHoraPc());
	            		
	            		ResponseService pResp= ds.ActualizaRegistro(RespDia);
	            		bProce =false;
					}
					
					
				}
			}
		}
		catch(Exception ex)
		{
			bProce=true;
			jResultDetalle.put("status", "-1");
			jResultDetalle.put("descripcion", ex.getMessage());
			jResultDetalle.put("idMov","-999");
		}
		if(!bProce)
		{
			jResultDetalle.put("status", "-1");
			jResultDetalle.put("descripcion", "No se realiza operacion");
			jResultDetalle.put("idMov","-999");
		}
		jsonResultado.put("ResProcesaPendiente", jResultDetalle);
		return jsonResultado;
	}
	
	public JSONObject ComprobanteCargoAbonoNOAplica(String terminal,String entidad,String centro,String Empleado)
	{
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonResultado = new JSONObject();

		PasivosAcuerdosServices oWsAcuerdo = new PasivosAcuerdosServices();
		try
		{
			DiarioElectronicoDS oDElect = new DiarioElectronicoDS();
			ResponseRegistroDiarioElectronico oDiaElect = oDElect.UltimoMovimiento(terminal, entidad,centro);
			if(oDiaElect.getStatus() ==1)
			{
				PasivoTcb pasivoTcb = new PasivoTcb();
				ResponseConsultaClabe oConsClab= pasivoTcb.ConsultaClabe(oDiaElect.getRegistroDiarioElectronico().getNumSecAc(), entidad, terminal);
				
				ResponseFechaActual responseFechaActual = oWsAcuerdo.FechaActual();
				String fechaActual = responseFechaActual.getStatus() == 1 ? responseFechaActual.getFecha() : "";
				String horaOpr = oDiaElect.getRegistroDiarioElectronico().getHoraPc();//Ae234
				if(horaOpr.length()<2)
					horaOpr ="00:00:00";
				String DATE_FORMAT= "ddMMyyHHmmss";
				String DateFormatSerie="ddMMyy";
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String stringDate = "";
				String sFecSerie="";
				Date date;
				try 
				{
					date = df.parse(fechaActual + " " + horaOpr);
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
					stringDate = sdf.format(date);
					
					SimpleDateFormat sdfSerie = new SimpleDateFormat(DateFormatSerie);
					sFecSerie = sdfSerie.format(date);	
				} 
				catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String ImporLetra = CantidadLetras.Convertir(oDiaElect.getRegistroDiarioElectronico().getImpNominal(), true);
				String contrato = oConsClab.getCOD_NRBE_CLABE_V()+oConsClab.getCOD_PLZ_BANCARIA()+oConsClab.getNUM_SEC_AC_CLABE_V()+" "+oConsClab.getCOD_DIG_CR_CLABE_V();
				
				String TitularAcuerdo ="";
				String strOfic="";
				String strProdDesc="";
				//pasivoTcb.ConsultaPersonaXIdInterno(entidad,oDiaElect.getRegistroDiarioElectronico().  terminal);
				ResponsePersona oPerNum = pasivoTcb.ConsultaAcuerdo(entidad, oDiaElect.getRegistroDiarioElectronico().getNumSecAc(), terminal);
				if(oPerNum.getStatus()==1)
				{
					ResponsePersona oPersona = pasivoTcb.ConsultaPersonaXIdInterno(entidad, oPerNum.getIdInternoPe(), terminal);
					if(oPersona.getStatus()==1)
					{
						TitularAcuerdo = oPersona.getNombre()+" "+oPersona.getApPaterno()+" "+oPersona.getApMaterno();
						strProdDesc =  oPerNum.getProductoAcuerdo();
					}
				}
		
				/*ResponseDatosEmpleado RspNomEmple = pasivoTcb.ConsultaEmpleado(entidad, oDiaElect.getRegistroDiarioElectronico().getIdInternoEmplEp(), terminal);
				if(RspNomEmple.getStatus()==1)
				{
					NomEmple = RspNomEmple.getNOMBRE();
				}*/
				
				String StrHoraOper =oDiaElect.getRegistroDiarioElectronico().getHoraOprcn();
				if(StrHoraOper.length()>8)
					StrHoraOper=StrHoraOper.substring(0, 8);
				
				ResponseDatosCentro oDatCent = pasivoTcb.ConsultaCentro(entidad, oDiaElect.getRegistroDiarioElectronico().getCodInternoUo(), terminal);
				if(oDatCent.getStatus()==1)
				{
					strOfic = oDiaElect.getRegistroDiarioElectronico().getCodInternoUo()+" "+oDatCent.getNOMBRE();
				}
				
				String StrImporte =oDiaElect.getRegistroDiarioElectronico().getImpNominal();
				
				
				StrImporte= CantidadLetras.FormatoNumero(StrImporte);
						
				jsonResultado.put("fecha", fechaActual);//oResCA.getFechaOperacion());
				jsonResultado.put("hora", StrHoraOper);
				jsonResultado.put("nombre", TitularAcuerdo);
				jsonResultado.put("importe", StrImporte);
				jsonResultado.put("folio", oDiaElect.getRegistroDiarioElectronico().getNumSec());
				jsonResultado.put("producto",strProdDesc);
				jsonResultado.put("importe_letra", "( "+ImporLetra+" )");
				jsonResultado.put("oficina", strOfic);
				jsonResultado.put("contrato", contrato);
				
				String StrIdExter =oDiaElect.getRegistroDiarioElectronico().getValorDtllTx();
				try
				{
					if(StrIdExter.indexOf( '|' )>0){
						String[] campos = StrIdExter.split("\\|");
						StrIdExter =campos[2];	
					}
					
				}catch(Exception ex)
				{
					
				}
				jsonResultado.put("idExterno",StrIdExter );
				String StrSerial = terminal+ " "+ sFecSerie+ StrHoraOper.replace(":", "");
				StrSerial=StrSerial +="  "+oDiaElect.getRegistroDiarioElectronico().getContrida()+" "+oDiaElect.getRegistroDiarioElectronico().getSgnCtbleDi();
				//StrSerial=StrSerial +="  C "+oDiaElect.getRegistroDiarioElectronico().getSgnCtbleDi();
				/*switch(oDiaElect.getRegistroDiarioElectronico().getContrida())
				{
					case "C":
						StrSerial +="  C H";
						break;
					case "A":
						StrSerial +="  C D";
				}*/
				jsonResultado.put("serial", StrSerial);
			}
			else
			{
				jsonResultado.put("status", "-1");
				jsonResultado.put("descripcion", oDiaElect.getDescripcion());
			}
		}
		catch (Exception e) 
		{
			jsonResultado.put("status", "-1");
			jsonResultado.put("descripcion", e.getMessage());
			log.error("ComprobanteCargoAbono  - " + e.getMessage());
		}
		jsonResult.put("RespuestaGeneraComprobate", jsonResultado);

		return jsonResult;
	}

	public ResponseService EncriptaDes(String UrlEncDesc,String StrDataTrans)
	{
		ResponseService oRep= new ResponseService();
		try
		{
			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("text",StrDataTrans);
			String input =datosEntrada.toString();
			CDatosSucursales oClip = new CDatosSucursales();
			 oRep= oClip.EncriptarDescr(input, UrlEncDesc);
			
		}catch(Exception ex)
		{
			oRep.setStatus(-1);
			oRep.setDescripcion(ex.getMessage());
		}
		return oRep;
	}
	/*End E234 */	
}
