package com.bansefi.nss.cargoabono.process;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.bansefi.nss.cargoabono.commons.CantidadLetras;
import com.bansefi.nss.cargoabono.commons.FuncionesEncryption;
import com.bansefi.nss.cargoabono.commons.UtilJson;
import com.bansefi.nss.cargoabono.ds.DiarioElectronicoDS;
import com.bansefi.nss.cargoabono.services.PasivosAcuerdosServices;
import com.bansefi.nss.cargoabono.tcb.PasivoTcb;
import com.bansefi.nss.cargoabono.vo.DiarioElectronicoRequest;
import com.bansefi.nss.cargoabono.vo.EntDataTrans;
import com.bansefi.nss.cargoabono.vo.EntInsertDiarioElect;
import com.bansefi.nss.cargoabono.vo.EntPendienteCargAbono;
import com.bansefi.nss.cargoabono.vo.MovimientoCargoAbono;
import com.bansefi.nss.cargoabono.vo.ResponDiaPend;
import com.bansefi.nss.cargoabono.vo.ResponseConsultaClabe;
import com.bansefi.nss.cargoabono.vo.ResponseDatosCentro;
import com.bansefi.nss.cargoabono.vo.ResponseDatosEmpleado;
import com.bansefi.nss.cargoabono.vo.ResponseFechaActual;
import com.bansefi.nss.cargoabono.vo.ResponsePersona;
import com.bansefi.nss.cargoabono.vo.ResponseRegistroDiarioElectronico;
import com.bansefi.nss.cargoabono.vo.ResponseService;
import com.bansefi.nss.cargoabono.vo.ResponseServiceCargoAbono;
import com.bansefi.nss.cargoabono.vo.ResponseServiceObject;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion;



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
								String tipoIdExterno)
	{
		PasivoTcb pasivoTCB = new PasivoTcb();
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonResultado = new JSONObject();
		boolean StatusOper =false;
		String SrStatus="-1";
		String SrIdMov="-999";
		String SrDesc="";
		String SrCod="";
		String SrArg1="";
		/*Begin E234*/
		try
		{
			impNom =impNom.replace(",", "");
			concepto = "DVI        " + concepto + " " + tipoIdExterno + " " + idexterno;
			//Registro de Cargo/Abono en 3 pasos
			//Paso 1
			DiarioElectronicoDS ProcDia = new DiarioElectronicoDS();
			ResponDiaPend RespDia= ProcDia.RegistraCargoAbonoPendiente(entidad, sucursal, terminal, empleado, tipoOp, concepto, impNom, "0", acuerdo, "0",fechaOperacion,cajaInt);
			
			//Paso 2
			if(RespDia.getStatus()==1)
			{
				String StrAcuerdo ="0000000000".substring(acuerdo.length()) + acuerdo;
				ResponseServiceCargoAbono responseMov =  new ResponseServiceCargoAbono();
            	switch (tipoOp) 
            	{
					case "C":
						responseMov = pasivoTCB.Cargo(entidad, StrAcuerdo, impNom, concepto, terminal);
						break;
					case "A":
						responseMov = pasivoTCB.Abono(entidad, StrAcuerdo, impNom, concepto, terminal);
						break;							
				}
            	if(responseMov.getStatus()==1)
            	{
        			//Paso 3
            		RespDia.setTERMINAL(terminal);
            		RespDia.setCOD_RESPUESTA(1);
            		RespDia.setNUMSEC(responseMov.getNUM_SEC());
            		RespDia.setIMP_SDO(impNom);
            		RespDia.setHORA_OPERACION(responseMov.getHORAOPERACION());
            		ResponseService pResp= new ResponseService(); //ProcDia.ActualizaRegistro(RespDia);
            		if(pResp.getStatus()==1)
            		{
            			SrIdMov = responseMov.getNUM_SEC();
        				StatusOper =true;
            		}
            		else
            		{
            			if(responseMov.getHORAOPERACION()!=null)
            				if(responseMov.getHORAOPERACION().length()>0)
            					RespDia.setHORA_OPERACION(responseMov.getHORAOPERACION());
            			
                		RespDia.setCOD_RESPUESTA(2);
            			ResponseService pResp01= ProcDia.ActualizaRegistro(RespDia);
            			SrStatus="0";
            			SrDesc=responseMov.getDescripcion();;
            		}
            		
            	}
            	else
            	{
            		SrIdMov= "-999";
            		SrStatus= "0";
            		SrDesc=responseMov.getDescripcion();
            		
            		
            	}
			}
			else
			{
				SrIdMov= "-998";
        		SrStatus= "0";
        		SrDesc="No registra cargo-abono pendiente";
        		
			}
			
			if(StatusOper)
			{
				jsonResultado.put("idmov", SrIdMov);
				jsonResultado.put("status", "1");
				jsonResultado.put("descripcion","Registro dado de alta");
				
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

	public JSONObject ProcesarIntervencion( String entidad, 
			String sucursal,	String empleado, 
			String terminal,	String acuerdo, 
			String tipoOp,		String fechaValor, 
			String impNom,		String concepto, 
			String fechaOperacion,String horaOp,
			String cajaInt,		String nombreCliente, 
			String producto,	String idexterno, 
			String tipoIdExterno,String StrClop,
			String StrSubClop)
{
PasivoTcb pasivoTCB = new PasivoTcb();
JSONObject jsonResult = new JSONObject();
JSONObject jsonResultado = new JSONObject();
boolean StatusOper =false;
String SrStatus="-1";
String SrIdMov="-999";
String SrDesc="";
String SrCod="";
/*Begin E234*/
try
{
	impNom =impNom.replace(",", "");
	concepto = "DVI        " + concepto + " " + tipoIdExterno + " " + idexterno;
		//Registro de Cargo/Abono en 3 pasos
		//Paso 1
		DiarioElectronicoDS ProcDia = new DiarioElectronicoDS();
		ResponDiaPend RespDia= ProcDia.RegistraCargoAbonoPendienteInter(entidad, sucursal, terminal, empleado,
				tipoOp, concepto, impNom, "0", acuerdo, "0",
				fechaOperacion,cajaInt,StrClop,StrSubClop);
		
		//Paso 2
		if(RespDia.getStatus()==1)
		{
		String StrAcuerdo ="0000000000".substring(acuerdo.length()) + acuerdo;
		ResponseServiceCargoAbono responseMov =  new ResponseServiceCargoAbono();
		switch (tipoOp) 
		{
		case "C":
			responseMov = pasivoTCB.Cargo(entidad, StrAcuerdo, impNom, concepto, terminal);
			break;
		case "A":
			responseMov = pasivoTCB.Abono(entidad, StrAcuerdo, impNom, concepto, terminal);
			break;							
		}
		if(responseMov.getStatus()==1)
		{
		//Paso 3
		RespDia.setTERMINAL(terminal);
		RespDia.setCOD_RESPUESTA(1);
		RespDia.setNUMSEC(responseMov.getNUM_SEC());
		RespDia.setIMP_SDO(impNom);
		
		ResponseService pResp= ProcDia.ActualizaRegistro(RespDia);
		if(pResp.getStatus()==1)
		{
			SrIdMov = responseMov.getNUM_SEC();
			StatusOper =true;
		}
		else
		{
			RespDia.setCOD_RESPUESTA(2);
			ResponseService pResp01= ProcDia.ActualizaRegistro(RespDia);
			SrStatus="0";
			SrDesc="No pasa a estatus 1 :";
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
		
		if(StatusOper)
		{
			jsonResultado.put("idmov", SrIdMov);
			jsonResultado.put("status", "1");
			jsonResultado.put("descripcion","Registro dado de alta");
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
	public JSONObject ConsultaPendientes(String entidad,String sucursal,String terminal,String empleado)
	{
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonResultado = new JSONObject();
		try
		{
			PasivosAcuerdosServices wAcuerdo = new PasivosAcuerdosServices();
			EntPendienteCargAbono oResult = 	wAcuerdo.ConsultaPendientes(sucursal, entidad, terminal, empleado);
			if(oResult.getStatus()>0)
			{
				jsonResultado.put("idMovimiento", oResult.getIdMovimiento());
				jsonResultado.put("entidad", oResult.getEntidad());
				jsonResultado.put("tipoOp", oResult.getTipoOp());
				jsonResultado.put("fechaValor", oResult.getFechaValor());
				jsonResultado.put("sucursal", oResult.getSucursal());
				jsonResultado.put("idTerminal",oResult.getIdTerminal() );
				jsonResultado.put("idEmpleado", oResult.getIdEmpleado());
				jsonResultado.put("horaOp", oResult.getHoraOper());
				jsonResultado.put("fechaContable",oResult.getFechaContable() );
				jsonResultado.put("statusProceso", oResult.getStatusPendiente());
				jsonResultado.put("dateProceso", oResult.getDateProcess());
				jsonResultado.put("dateCambioStatus", oResult.getDateCambioStatus());
				jsonResultado.put("folioTrans", oResult.getFolioTrans().toString());
				jsonResultado.put("dataTrans", oResult.getDataTrans());
			}
			
		}
		catch(Exception ex)
		{
			jsonResultado.put("status", -1);
			jsonResultado.put("descripcion", ex.getMessage());
			log.error("ConsultaPendientes  - " + ex.getMessage());
		}
		jsonResult.put("RespuestaConsultaPendientes", jsonResultado);
		return jsonResult;
	}
	
	public JSONObject ComprobanteCargoAbono(String terminal,String entidad)
	{
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonResultado = new JSONObject();

		PasivosAcuerdosServices oWsAcuerdo = new PasivosAcuerdosServices();
		try
		{
			DiarioElectronicoDS oDElect = new DiarioElectronicoDS();
			ResponseRegistroDiarioElectronico oDiaElect = oDElect.UltimoMovimiento(terminal, entidad);
			if(oDiaElect.getStatus() ==1)
			{
				PasivoTcb pasivoTcb = new PasivoTcb();
				ResponseConsultaClabe oConsClab= pasivoTcb.ConsultaClabe(oDiaElect.getRegistroDiarioElectronico().getNumSecAc(), entidad, terminal);
				
				ResponseFechaActual responseFechaActual = oWsAcuerdo.FechaActual();
				String fechaActual = responseFechaActual.getStatus() == 1 ? responseFechaActual.getFecha() : "";
				String horaOpr = oDiaElect.getRegistroDiarioElectronico().getHoraPc();//Ae234
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
				String contrato = oConsClab.getStatus() == 1 ? oConsClab.getCOD_NRBE_CLABE_V() : "";
				
				String TitularAcuerdo ="";
				String strOfic="";
				
				//pasivoTcb.ConsultaPersonaXIdInterno(entidad,oDiaElect.getRegistroDiarioElectronico().  terminal);
				ResponsePersona oPerNum = pasivoTcb.ConsultaAcuerdo(entidad, oDiaElect.getRegistroDiarioElectronico().getNumSecAc(), terminal);
				if(oPerNum.getStatus()==1)
				{
					ResponsePersona oPersona = pasivoTcb.ConsultaPersonaXIdInterno(entidad, oPerNum.getIdInternoPe(), terminal);
					if(oPersona.getStatus()==1)
					{
						TitularAcuerdo = oPersona.getNombre()+" "+oPersona.getApPaterno()+" "+oPersona.getApMaterno();
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
				jsonResultado.put("producto", "");
				jsonResultado.put("importe_letra", ImporLetra);
				jsonResultado.put("oficina", strOfic);
				jsonResultado.put("contrato", oDiaElect.getRegistroDiarioElectronico().getNumSecAc());
				jsonResultado.put("idExterno", oDiaElect.getRegistroDiarioElectronico().getValorDtllTx());
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

	public JSONObject ConsultaPendienteDiario(String entidad,String terminal)
	{
		JSONObject jsonResultado = new JSONObject();
		JSONObject jDetalle = new JSONObject();
		try	
		{
			DiarioElectronicoDS ds = new DiarioElectronicoDS();
			ResponseServiceObject oDiario =ds.ObtieneUltimoMovPendiente(entidad, terminal);
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
	
	public JSONObject ProcesaPendientes(String entidad,String terminal)
	{
		JSONObject jsonResultado = new JSONObject();
		JSONObject jResultDetalle = new JSONObject();
		boolean bProce=false;
		try
		{
			DiarioElectronicoDS ds = new DiarioElectronicoDS();
			ResponseServiceObject oDiario =ds.ObtieneUltimoMovPendiente(entidad, terminal);
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
						
						String[] ArryhoraPas = horaPas.split(":");
						String[] ArryhoraDia = horaDia.split(":");
						fechaDia =fechaDia.substring(0,10);
						fechaDia=fechaDia.replace("-", "/");
						if(acuerdoPas.equals(acuerdoDia))
						{
							if(importPas.equals(importDia))
							{
								if(fechaPas.equals(fechaDia))
								{
									
									if(ArryhoraPas[0].equals(ArryhoraDia[0]))
									{
										int minPas = Integer.parseInt(ArryhoraPas[1])+10;
										int minDia = Integer.parseInt(ArryhoraDia[1]);
										if(minDia < minPas )
										{

											String StrHrOper = oDiElecDet.getHoraOprcn(); 
											String StrFecPc = oDiElecDet.getFechaPc();
											String StrHrPc = oDiElecDet.getHoraPc();
											
											ResponDiaPend RespDia = new ResponDiaPend();
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
										/*
										if(ArryhoraPas[1].equals(ArryhoraDia[1]))
										{
											
										}*/
									}
									
								}
							}
						}	
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
	/*End E234 */	
}
