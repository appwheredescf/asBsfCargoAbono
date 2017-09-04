package com.bansefi.nss.cargoabono.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import com.bansefi.nss.cargoabono.commons.UtilJson;
import com.bansefi.nss.cargoabono.ds.DiarioElectronicoDS;
import com.bansefi.nss.cargoabono.properties.DsProperties;
import com.bansefi.nss.cargoabono.properties.EndpointProperties;
import com.bansefi.nss.cargoabono.properties.TcbProperties;
import com.bansefi.nss.cargoabono.vo.ActualizaStatusMovimientoCargoAbono;
import com.bansefi.nss.cargoabono.vo.EntDataTrans;
import com.bansefi.nss.cargoabono.vo.EntMovCargoAbono;
import com.bansefi.nss.cargoabono.vo.EntResponRegCargAbono;
import com.bansefi.nss.cargoabono.vo.MovimientoCargoAbono;
import com.bansefi.nss.cargoabono.vo.ResponseConsultaSaldo;
import com.bansefi.nss.cargoabono.vo.ResponseFechaActual;
import com.bansefi.nss.cargoabono.vo.ResponseHoraActual;
import com.bansefi.nss.cargoabono.vo.ResponseService;
import com.bansefi.nss.cargoabono.vo.ResponseServiceCargoAbono;
import com.bansefi.nss.cargoabono.vo.ResponseServiceObject;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion.AF_APNTE_AUDIT_V;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion.AF_APNTE_E;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion.AF_AUDIT_AUX;





public class PasivosAcuerdosServices 
{
	private DsProperties propDs = new DsProperties();
	private UtilJson utilJson = UtilJson.getInstance();
	private static final Logger log = LogManager.getLogger(PasivosAcuerdosServices.class);
	DiarioElectronicoDS diario= new DiarioElectronicoDS();
	
	public  ResponseService ConsultaClabe(String acuerdo, String entidad, String terminal) {
		EndpointProperties endpointProperties = new EndpointProperties();
		ResponseService response = new ResponseService();
		String url = endpointProperties.getConsultaClabe();
		String input = "";
		String salida = "";
		try {

			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("acuerdo", acuerdo);
			datosEntrada.put("entidad", entidad);
			datosEntrada.put("terminal", terminal);
			JSONObject jsonObject = new JSONObject().put("ConsultaCLABE", datosEntrada);
			input = jsonObject.toString();
			
			salida = this.utilJson.callRestPost(input,url);//Llamada al REST
			
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("ConsultaCLABEResponse").getJSONObject("return").getJSONObject("RESPONSE");
				String status = jsonResponse.optString("STATUS");
				if(status.equals("1")){
					response.setStatus(1);
					response.setDescripcion(jsonResponse.optString("CLABE"));
				} else {
					response.setStatus(0);
					response.setDescripcion(jsonResponse.optString("ERROR_DESC"));
					log.error("ConsultaClabe Entrada - " + input);
					log.error("ConsultaClabe . - " + jsonResponse.optString("ERROR_DESC"));
				}	
			}
			else
			{
				response.setStatus(0);
				response.setDescripcion("Error al obtener la salida");
			}
			
			
		} catch (Exception ex) {
			response.setStatus(-1);
			response.setDescripcion(ex.getMessage());
			log.error("ConsultaClabe Entrada - " + input);
			log.error("ConsultaClabe" , ex);
		}		
		return response;
	}
	
	public  ResponseConsultaSaldo ConsultaSaldo(String acuerdo, String entidad, String terminal)
	{
		String salida="";
		TcbProperties pProp = new TcbProperties();
		ResponseConsultaSaldo response = new ResponseConsultaSaldo();
		String StrVist="";
		String StrUrl="";
		try
		{
			StrUrl=pProp.getURL_SALDOS();
			
			StrVist="<TR_CONS_SALDOS_VISTA_TRN>"
            +"<TR_CONS_SALDOS_VISTA_TRN_I>"
            +"<TR_CONS_SALDOS_VISTA_EVT_Y>"
             +"<COD_NRBE_EN>"+entidad+"</COD_NRBE_EN>"
             +"<COD_CENT_UO></COD_CENT_UO>"
             +"<NUM_SEC_AC>"+acuerdo+"</NUM_SEC_AC>"
            +"</TR_CONS_SALDOS_VISTA_EVT_Y>"
            +"<STD_TRN_I_PARM_V>"
             +"<ID_INTERNO_TERM_TN>"+terminal+"</ID_INTERNO_TERM_TN>"
             +"<ID_EMPL_AUT></ID_EMPL_AUT>"
             +"<NUM_SEC></NUM_SEC>"
             +"<COD_TX>GAC11COU</COD_TX>"
             +"<COD_TX_DI></COD_TX_DI>"
            +"</STD_TRN_I_PARM_V>"
           +"</TR_CONS_SALDOS_VISTA_TRN_I>"
          +"</TR_CONS_SALDOS_VISTA_TRN>";
			
			String soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>"
					+ "	<SOAP-ENV:Body> " + StrVist+ "	</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"; 

			URL url;
			java.net.URLConnection conn = null;

			try 
			{
				url = new URL(StrUrl);
				try 
				{
					conn = url.openConnection();
				} 
				catch (IOException e) 
				{
					log.info("ConsultaSaldo -  : View In .- " + StrVist);
					log.info("ConsultaSaldo -  : URL_CARGO .- " + StrUrl);
					log.error("ConsultaSaldo - : IOException. " , e);
				}
			} 
			catch (MalformedURLException e1) 
			{
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("CargoIntervencion : View In .- " + StrVist);
				log.info("CargoIntervencion  : URL_CARGO .- " + StrUrl);
				log.error("CargoIntervencion : MalformedURLException. " , e1);
			}
			
			conn.setRequestProperty("SOAPAction", StrUrl);
			conn.setDoOutput(true);
			//System.out.println(soapXml);
			//Send the request
			java.io.OutputStreamWriter wr;
			try 
			{
				wr = new java.io.OutputStreamWriter(conn.getOutputStream());
				wr.write(soapXml);
				wr.flush();
			} 
			catch (IOException e2) 
			{
				response.setStatus(-1);
				response.setDescripcion(e2.getMessage());
				log.info("CargoIntervencion : View In .- " + StrVist);
				log.error("CargoIntervencion : OutputStreamWriter" , e2);
			}

			// Read the response
			java.io.BufferedReader rd = null;
			try 
			{
				rd = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
			} 
			catch (IOException e1) 
			{
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("CargoIntervencion : View In .- " + StrVist);
				log.error("CargoIntervencion : BufferedReader. " , e1);
			}
			
			try
			{
				String line = "";
				while ((line = rd.readLine()) != null) 
				{ 
					//LECTURA DE VISTA DE SALIDA
					salida += line;	
				}
				
				salida = salida.replaceAll("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">", "");
				salida = salida.replaceAll("<SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Envelope>", "");
				salida = salida.trim();
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
				NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_CONS_SALDOS_VISTA_TRN_O");
				//System.out.println(strVista);
				//System.out.println(salida);
				String RTRN_CD = "";
				for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ )
				{
					Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
					Element eElement = (Element) item;
					RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
				}
				
				if(RTRN_CD.equals("1"))
				{
					
					Element PSV_SALDO = (Element)doc.getElementsByTagName("PSV_DISPO_V").item(0);
					String ImpSald = PSV_SALDO.getElementsByTagName("STD_DEC_15Y2").item(0).getTextContent();		
					
					response.setStatus(1);
					response.setDISPO(ImpSald);
					
				}
				
				else 
				{
					
					log.info("CargoIntervencion : View In .- " + StrVist);
					log.info("CargoIntervencion : View Out .- " + salida);
					String errores = "";
					NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
					errores =ObtMensajeTcb(STD_MSJ_PARM_V);
					response.setDescripcion(errores);
			//		Thread.sleep(timeSleep);
				}
			}
			catch (Exception e) 
			{
				response.setStatus(-1);
				response.setDescripcion(e.getMessage());
				//System.out.println(e.getMessage());
				log.info("CargoIntervencion  : View In .- " + StrVist);
				log.error("CargoIntervencion : Exception" , e);
			}
			
			
		}catch(Exception ex){
			
		}
		return response;
	}
	
	public  ResponseConsultaSaldo ConsultaSaldoAnterior(String acuerdo, String entidad, String terminal) 
	{
		EndpointProperties endpointProperties = new EndpointProperties();
		ResponseConsultaSaldo response = new ResponseConsultaSaldo();
		String url = endpointProperties.getConsultaSaldos();
		String input = "";
		String salida = "";
		try {

			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("acuerdo", acuerdo);
			datosEntrada.put("entidad", entidad);
			datosEntrada.put("terminal", terminal);
			JSONObject jsonObject = new JSONObject().put("ConsultaSaldos", datosEntrada);
			input = jsonObject.toString();
			
			salida = this.utilJson.callRestPost(input,url);//Llamada al REST
			
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("ConsultaSaldosResponse").getJSONObject("return").getJSONObject("TR_CONS_SALDOS_VISTA_TRN").getJSONObject("TR_CONS_SALDOS_VISTA_TRN_O");
				Integer status = jsonResponse.getInt("RTRN_CD");
				if(status == 1)
				{
					response.setStatus(1);
					response.setDescripcion("");
					response.setBLOQUEOS(jsonResponse.optString("BLOQUEOS"));
					response.setDISPO(jsonResponse.optString("DISPO"));
					response.setRTRN_CD(jsonResponse.optString("RTRN_CD"));
					response.setSDO_AUT(jsonResponse.optString("SDO_AUT"));
					response.setSDO_CONECT(jsonResponse.optString("SDO_CONECT"));
					response.setSDO_CONTABLE(jsonResponse.optString("SDO_CONTABLE"));
					response.setSDO_INCID(jsonResponse.optString("SDO_INCID"));
					
				} 
				else 
				{
					response.setStatus(0);
					String errores = "";
					JSONObject jsonErrores = jsonResponse.optJSONObject("errores");
					JSONObject jErroNum = jsonResponse.optJSONObject("errores").optJSONObject("error");
					errores = jsonErrores != null ? jsonErrores.toString() : jsonResponse.optJSONArray("errores") != null ? jsonResponse.optJSONArray("errores").toString() : "";
					String StrE = jErroNum.get("numero").toString();
					if(!(StrE.length()>0))
						StrE=errores;
					response.setDescripcion(StrE);
					log.error("ConsultaSaldo Entrada - " + input);
					log.error("ConsultaSaldo . - " + errores);
				}	
			}
			else
			{
				response.setStatus(0);
				
			}
			
		} catch (Exception ex) {
			response.setStatus(-1);
			response.setDescripcion(ex.getMessage());
			log.error("ConsultaSaldo Entrada - " + input);
			log.error("ConsultaSaldo" , ex);
		}		
		return response;
	}
	
	public  ResponseUltimaTransaccion UltimaTransaccion(String acuerdo, String entidad, String terminal, String fechaActual) 
	{
		EndpointProperties endpointProperties = new EndpointProperties();
		ResponseUltimaTransaccion response = new ResponseUltimaTransaccion();
		String url = endpointProperties.getUltimaTransaccion();
		String input = "";
		String salida = "";
		try {

			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("cuenta", acuerdo);
			datosEntrada.put("entidad", entidad);
			datosEntrada.put("terminal", terminal);
			datosEntrada.put("fechaActual", fechaActual);
			JSONObject jsonObject = new JSONObject().put("ConsultaUltimaTransaccion", datosEntrada);
			input = jsonObject.toString();
			
			salida = this.utilJson.callRestPost(input,url);
			
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida + " ");
				
				JSONObject jsonResponse = jsonSalida.getJSONObject("ConsultaUltimaTransaccionResponse").getJSONObject("return").getJSONObject("RESPONSE");
				Integer status = jsonResponse.getInt("STATUS");
				if(status == 1)
				{
					boolean isArray =true;
					response.setStatus(1);
					response.setDescripcion("");
					
					try
					{
						JSONArray jsonApuntesArra = jsonResponse.getJSONObject("DEPOSITOS").getJSONArray("AF_APNTE_AUDIT_V");
						ArrayList<AF_APNTE_AUDIT_V> apuntes = new ArrayList<ResponseUltimaTransaccion.AF_APNTE_AUDIT_V>();
						int a = jsonApuntesArra.length();
						int i = 1;
						for(Object objApunte : jsonApuntesArra ){
							JSONObject jsonApunte = (JSONObject)objApunte;
							JSONObject jsonAfApnte = jsonApunte.getJSONObject("AF_APNTE_E");
							JSONObject jsonAfAudit = jsonApunte.getJSONObject("AF_AUDIT_AUX");
							
							AF_APNTE_AUDIT_V afApnteAudit = response.getUltimoApunte();
							AF_APNTE_E afApnte = afApnteAudit.getAfApnteE();
							AF_AUDIT_AUX afAudit = afApnteAudit.getAfAuditAux();
							
							afApnte.setCOD_NRBE_EN(jsonAfApnte.optString("COD_NRBE_EN"));
							afApnte.setCOD_CENT_UO(jsonAfApnte.optString("COD_CENT_UO"));
							afApnte.setPRPDAD_CTA(jsonAfApnte.optString("PRPDAD_CTA"));
							afApnte.setNUM_SEC_AC(jsonAfApnte.optString("NUM_SEC_AC"));
							afApnte.setNUM_SUBAC(jsonAfApnte.optString("NUM_SUBAC"));
							afApnte.setID_EXP_RECLAM(jsonAfApnte.optString("ID_EXP_RECLAM"));
							afApnte.setCOD_CTA(jsonAfApnte.optString("COD_CTA"));
							afApnte.setCOD_NUMRCO_MONEDA(jsonAfApnte.optString("COD_NUMRCO_MONEDA"));
							afApnte.setNUM_SEC(jsonAfApnte.optString("NUM_SEC"));
							afApnte.setSGN(jsonAfApnte.optString("SGN"));
							afApnte.setIMP_APNTE(jsonAfApnte.optString("IMP_APNTE"));
							afApnte.setFECHA_CTBLE(jsonAfApnte.optString("FECHA_CTBLE"));
							afApnte.setFECHA_VALOR(jsonAfApnte.optString("FECHA_VALOR"));
							afApnte.setIND_1(jsonAfApnte.optString("IND_1"));
							afApnte.setIND_2(jsonAfApnte.optString("IND_2"));
							afApnte.setIND_3(jsonAfApnte.optString("IND_3"));
							afApnte.setIND_4(jsonAfApnte.optString("IND_4"));
							afApnte.setIND_5(jsonAfApnte.optString("IND_5"));
							afApnte.setIND_6(jsonAfApnte.optString("IND_6"));
							afApnte.setIND_7(jsonAfApnte.optString("IND_7"));
							afApnte.setIND_8(jsonAfApnte.optString("IND_8"));
							afApnte.setIND_9(jsonAfApnte.optString("IND_9"));
							afApnte.setIND_10(jsonAfApnte.optString("IND_10"));
							afApnte.setCOD_ORGN_APNTE(jsonAfApnte.optString("COD_ORGN_APNTE"));
							afApnte.setCONCPT_APNTE(jsonAfApnte.optString("CONCPT_APNTE"));
							afApnte.setCOD_ORIGEN(jsonAfApnte.optString("COD_ORIGEN"));
							afApnte.setCOD_INTERNO_UO(jsonAfApnte.optString("COD_INTERNO_UO"));
							afApnte.setCOD_LINEA(jsonAfApnte.optString("COD_LINEA"));
							afApnte.setID_GRP_PD(jsonAfApnte.optString("ID_GRP_PD"));
							
							afApnteAudit.setAfApnteE(afApnte);
							
							afAudit.setID_INTERNO_TERM_TN(jsonAfAudit.optString("ID_INTERNO_TERM_TN"));
							afAudit.setFECHA_OPRCN(jsonAfAudit.optString("FECHA_OPRCN"));
							afAudit.setHORA_OPRCN(jsonAfAudit.optString("HORA_OPRCN"));
							afAudit.setCOD_TX(jsonAfAudit.optString("COD_TX"));
							afAudit.setID_EMPL_AUT(jsonAfAudit.optString("ID_EMPL_AUT"));
							afAudit.setID_INTERNO_EMPL_EP(jsonAfAudit.optString("ID_INTERNO_EMPL_EP"));
							afAudit.setCOD_NRBE_EN_1(jsonAfAudit.optString("COD_NRBE_EN_1"));
							afAudit.setCOD_INTERNO_UO(jsonAfAudit.optString("COD_INTERNO_UO"));
							afAudit.setCOD_INDIC(jsonAfAudit.optString("COD_INDIC"));
							afAudit.setFECHA_CTBLE(jsonAfAudit.optString("FECHA_CTBLE"));
							afApnteAudit.setAfAuditAux(afAudit);
							
							apuntes.add(afApnteAudit);
							if(a == i)
								response.setUltimoApunte(afApnteAudit);
							i++;
						}
						response.setApuntes(apuntes);

					}
					catch(Exception ex)
					{
						isArray =false;
					}
					
					if(isArray == false)
					{
						JSONObject jsonResponseApunt = jsonResponse.getJSONObject("DEPOSITOS").getJSONObject("AF_APNTE_AUDIT_V");
						ArrayList<AF_APNTE_AUDIT_V> apuntes = new ArrayList<ResponseUltimaTransaccion.AF_APNTE_AUDIT_V>();
						
						
							JSONObject jsonApunte = jsonResponseApunt;//;(JSONObject)objApunte;
							JSONObject jsonAfApnte = jsonApunte.getJSONObject("AF_APNTE_E");
							JSONObject jsonAfAudit = jsonApunte.getJSONObject("AF_AUDIT_AUX");
							
							AF_APNTE_AUDIT_V afApnteAudit = response.getUltimoApunte();
							AF_APNTE_E afApnte = afApnteAudit.getAfApnteE();
							AF_AUDIT_AUX afAudit = afApnteAudit.getAfAuditAux();
							
							afApnte.setCOD_NRBE_EN(jsonAfApnte.optString("COD_NRBE_EN"));
							afApnte.setCOD_CENT_UO(jsonAfApnte.optString("COD_CENT_UO"));
							afApnte.setPRPDAD_CTA(jsonAfApnte.optString("PRPDAD_CTA"));
							afApnte.setNUM_SEC_AC(jsonAfApnte.optString("NUM_SEC_AC"));
							afApnte.setNUM_SUBAC(jsonAfApnte.optString("NUM_SUBAC"));
							afApnte.setID_EXP_RECLAM(jsonAfApnte.optString("ID_EXP_RECLAM"));
							afApnte.setCOD_CTA(jsonAfApnte.optString("COD_CTA"));
							afApnte.setCOD_NUMRCO_MONEDA(jsonAfApnte.optString("COD_NUMRCO_MONEDA"));
							afApnte.setNUM_SEC(jsonAfApnte.optString("NUM_SEC"));
							afApnte.setSGN(jsonAfApnte.optString("SGN"));
							afApnte.setIMP_APNTE(jsonAfApnte.optString("IMP_APNTE"));
							afApnte.setFECHA_CTBLE(jsonAfApnte.optString("FECHA_CTBLE"));
							afApnte.setFECHA_VALOR(jsonAfApnte.optString("FECHA_VALOR"));
							afApnte.setIND_1(jsonAfApnte.optString("IND_1"));
							afApnte.setIND_2(jsonAfApnte.optString("IND_2"));
							afApnte.setIND_3(jsonAfApnte.optString("IND_3"));
							afApnte.setIND_4(jsonAfApnte.optString("IND_4"));
							afApnte.setIND_5(jsonAfApnte.optString("IND_5"));
							afApnte.setIND_6(jsonAfApnte.optString("IND_6"));
							afApnte.setIND_7(jsonAfApnte.optString("IND_7"));
							afApnte.setIND_8(jsonAfApnte.optString("IND_8"));
							afApnte.setIND_9(jsonAfApnte.optString("IND_9"));
							afApnte.setIND_10(jsonAfApnte.optString("IND_10"));
							afApnte.setCOD_ORGN_APNTE(jsonAfApnte.optString("COD_ORGN_APNTE"));
							afApnte.setCONCPT_APNTE(jsonAfApnte.optString("CONCPT_APNTE"));
							afApnte.setCOD_ORIGEN(jsonAfApnte.optString("COD_ORIGEN"));
							afApnte.setCOD_INTERNO_UO(jsonAfApnte.optString("COD_INTERNO_UO"));
							afApnte.setCOD_LINEA(jsonAfApnte.optString("COD_LINEA"));
							afApnte.setID_GRP_PD(jsonAfApnte.optString("ID_GRP_PD"));
							
							afApnteAudit.setAfApnteE(afApnte);
							
							afAudit.setID_INTERNO_TERM_TN(jsonAfAudit.optString("ID_INTERNO_TERM_TN"));
							afAudit.setFECHA_OPRCN(jsonAfAudit.optString("FECHA_OPRCN"));
							afAudit.setHORA_OPRCN(jsonAfAudit.optString("HORA_OPRCN"));
							afAudit.setCOD_TX(jsonAfAudit.optString("COD_TX"));
							afAudit.setID_EMPL_AUT(jsonAfAudit.optString("ID_EMPL_AUT"));
							afAudit.setID_INTERNO_EMPL_EP(jsonAfAudit.optString("ID_INTERNO_EMPL_EP"));
							afAudit.setCOD_NRBE_EN_1(jsonAfAudit.optString("COD_NRBE_EN_1"));
							afAudit.setCOD_INTERNO_UO(jsonAfAudit.optString("COD_INTERNO_UO"));
							afAudit.setCOD_INDIC(jsonAfAudit.optString("COD_INDIC"));
							afAudit.setFECHA_CTBLE(jsonAfAudit.optString("FECHA_CTBLE"));
							afApnteAudit.setAfAuditAux(afAudit);
							
							apuntes.add(afApnteAudit);
						
						response.setApuntes(apuntes);

					}
		
				} 
				else 
				{
					response.setStatus(0);
					String errores = "";
					errores = jsonResponse.getString("ERROR_DESC"); 
					response.setDescripcion(errores);
					log.error("UltimaTransaccion Entrada - " + input);
					log.error("UltimaTransaccion . - " + errores);
				}
			}
			else
			{
				response.setStatus(0);
			}
		} 
		catch (Exception ex) 
		{
			response.setStatus(-1);
			response.setDescripcion(ex.getMessage());
			log.error("UltimaTransaccion Entrada - " + input);
			log.error("UltimaTransaccion" + ex);
		}		
		return response;
	}
	
	public  ResponseServiceCargoAbono CargoAbono(
			String entidad,
			String acuerdo, 
			String tipoOp, 
			String fechaValor, 
			String impNom,
			String clopSis,
			String subClop,
			String cajaInt,
			String concepto,
			String fechaOperacion,
			String idOrigenApnt,
			String sucursal,
			String idTerminal,
			String idEmpleado,
			String horaOp,
			String cotTx) 
	{
		
		EndpointProperties endpointProperties = new EndpointProperties();
		ResponseServiceCargoAbono response = new ResponseServiceCargoAbono();
		String url = endpointProperties.getCargoAbono();
		String input = "";
		String salida = "";
		try 
		{
			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("entidad", entidad);
			datosEntrada.put("acuerdo", acuerdo);
			datosEntrada.put("tipoOp", tipoOp);
			datosEntrada.put("fechaValor", fechaValor);
			datosEntrada.put("impNom", impNom);
			datosEntrada.put("clopSis", clopSis);
			datosEntrada.put("subClop", subClop);
			datosEntrada.put("cajaInt", cajaInt);
			datosEntrada.put("concepto", concepto);
			datosEntrada.put("fechaOperacion", fechaOperacion);
			datosEntrada.put("idOrigenApnt", idOrigenApnt);
			datosEntrada.put("sucursal", sucursal);
			datosEntrada.put("idTerminal", idTerminal);
			datosEntrada.put("idEmpleado", idEmpleado);
			datosEntrada.put("horaOp", horaOp);
			datosEntrada.put("cotTx", cotTx);
			
			JSONObject jsonObject = new JSONObject().put("CargoAbono", datosEntrada);
			input = jsonObject.toString();

			salida = this.utilJson.callRestPost(input,url);
			
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("CargoAbonoResponse").getJSONObject("return").getJSONObject("RESPONSE");
				Integer status = jsonResponse.getInt("STATUS");
				if(status == 1)
				{
					response.setStatus(1);
					response.setDescripcion("");
					response.setFECHACONTABLE(jsonResponse.optString("FECHACONTABLE"));
					response.setFECHAOPERA(jsonResponse.optString("FECHAOPERA"));
					response.setHORAOPERACION(jsonResponse.optString("HORAOPERACION"));
					response.setNUM_SEC(jsonResponse.optString("NUM_SEC"));
					response.setFECHAVALOR(jsonResponse.optString("FECHAVALOR"));
				} 
				else 
				{
					response.setStatus(0);
					String errores = "";
					JSONObject jsonErrores = jsonResponse.optJSONObject("ERROR_DESC");
					errores = jsonErrores != null ? jsonErrores.toString() : jsonResponse.optJSONArray("errores") != null ? jsonResponse.optJSONArray("errores").toString() : ""; 
					response.setDescripcion(errores);
					log.error("CargoAbono Entrada - " + input);
					log.error("CargoAbono . - " + errores);
				}	
			}
			else
			{
				response.setStatus(0);
				response.setDescripcion("Error al consultar");
			}
			
		} catch (Exception ex) {
			response.setStatus(-1);
			response.setDescripcion(ex.getMessage());
			log.error("CargoAbono Entrada - " + input);
			log.error("CargoAbono" + ex);
		}		
		return response;
	}
	
	public  ResponseHoraActual HoraActual() 
	{
		EndpointProperties endpointProperties = new EndpointProperties();
		ResponseHoraActual response = new ResponseHoraActual();
		String url = endpointProperties.getGetHora();
		String input = "";
		String salida = "";
		try 
		{

			JSONObject datosEntrada = new JSONObject();
			JSONObject jsonObject = new JSONObject().put("getHora", datosEntrada);
			input = jsonObject.toString();
			
			salida = this.utilJson.callRestPost(input,url);
			
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("getHoraResponse");
				String horaActual = jsonResponse.optString("Hora");
				if(horaActual.length() > 0){
					response.setStatus(1);
					response.setHora(horaActual);
				} else {
					response.setStatus(0);
					response.setDescripcion(salida);
					log.error("HoraActual Entrada - " + input);
					log.error("HoraActual . - " + salida);
				}	
			}
			else
			{
				response.setStatus(0);
			}
			
			
		} catch (Exception ex) {
			response.setStatus(-1);
			response.setDescripcion(ex.getMessage());
			log.error("HoraActual Entrada - " + input);
			log.error("HoraActual" , ex);
		}		
		return response;
	}
	
	public  ResponseFechaActual FechaActual() 
	{
		EndpointProperties endpointProperties = new EndpointProperties();
		ResponseFechaActual response = new ResponseFechaActual();
		String url = endpointProperties.getFechaActual();
		String input = "";
		String salida = "";
		try {

			JSONObject datosEntrada = new JSONObject();
			JSONObject jsonObject = new JSONObject().put("getFechaActual", datosEntrada);
			input = jsonObject.toString();
			
			salida = this.utilJson.callRestPost(input,url);
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("getFechaResponse");
				String fechaActual = jsonResponse.optString("Fecha");
				if(fechaActual.length() > 0)
				{
					response.setStatus(1);
					response.setFecha(fechaActual);
				} else {
					response.setStatus(0);
					response.setDescripcion(salida);
					log.error("HoraActual Entrada - " + input);
					log.error("HoraActual . - " + salida);
				}	
			}
			else
			{
				response.setStatus(0);
			}
			
			
		} 
		catch (Exception ex) 
		{
			response.setStatus(-1);
			response.setDescripcion(ex.getMessage());
			log.error("HoraActual Entrada - " + input);
			log.error("HoraActual", ex);
		}		
		return response;
	}
	
	
	
	/*Begin E234 */
	
	public  ResponseServiceObject ConsultaIdMovCargoAbono(String IdMovimiento) 
	{
		ResponseServiceObject oCargAb = new ResponseServiceObject();
		EndpointProperties endpointProperties = new EndpointProperties();
		
		String url = endpointProperties.getConsultaMovCargoAbono();
		String input = "";
		String salida = "";
		try 
		{
			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("idMovimiento", IdMovimiento);

			JSONObject jsonObject = new JSONObject().put("ObtieneMovimientoCargoAbono", datosEntrada);
			input = jsonObject.toString();
			salida = this.utilJson.callRestPost(input,url);
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("ObtieneMovimientoCargoAbonoResp").getJSONObject("MovimientoCargoAbono");
				MovimientoCargoAbono oResCA = new MovimientoCargoAbono();
				try
				{
					oResCA =
                            (MovimientoCargoAbono) this.utilJson.jsonToObject(
                            		oResCA, jsonResponse.toString(), new ArrayList<String>());
					oCargAb.setEntReturn(oResCA);
					oCargAb.setStatus(1);
				}
				catch(Exception ex)
				{
					log.error("PasivosAcuerdosServices Entrada - " + input);
					oCargAb.setStatus(0);
					oCargAb.setDescripcion(ex.getMessage());
					System.out.println("Error Salida "+ex.getMessage());
				}
			}
			else
			{
				oCargAb.setStatus(0);
				oCargAb.setDescripcion("NO EXISTE EL MOVIMIENTO");
			}
			
		} 
		catch (Exception ex) 
		{
			oCargAb.setStatus(0);
			oCargAb.setDescripcion(ex.getMessage());
			log.error("ConsultaIdMovCargoAbono Entrada - " + input);
			log.error("ConsultaIdMovCargoAbono",ex);
		}		
		return oCargAb;
	}
	
	
	public String ObtCadenaTrans(EntDataTrans oData)
	{
		String StrResult ="";
		try
		{
			JSONObject DatosTrans = new JSONObject();
			DatosTrans.put("ACUERDO",oData.getACUERDO() );
			DatosTrans.put("IMPORTE",oData.getIMPORTE() );
			DatosTrans.put("NOMBRECLIENTE",oData.getNOMBRECLIENTE() );
			DatosTrans.put("CONCEPTO",oData.getCONCEPTO() );
			DatosTrans.put("IDORIGENAPNT",oData.getIDORIGENAPNT() );
			DatosTrans.put("PRODUCTO",oData.getPRODUCTO() );
			DatosTrans.put("CHECKSUM",oData.getCHECKSUM() );
			DatosTrans.put("CODIDEXT",oData.getCODIDEXT() );
			DatosTrans.put("IDEXTERNO",oData.getIDEXTERNO() );
			JSONObject jsonDTrans = new JSONObject().put("DATATRANS", DatosTrans);
			StrResult = jsonDTrans.toString();
		}
		catch(Exception ex)
		{
			log.error("ObtCadenaTrans", ex);
			StrResult="";
		}
		return StrResult;
	}
	
	public EntDataTrans SetDataTrans(String StrInput)
	{
		EntDataTrans oDataTras = new EntDataTrans();
		
		try
		{
			JSONObject jsonSalida = new JSONObject(StrInput);
			JSONObject jsonResponse = jsonSalida.getJSONObject("DATATRANS");
			oDataTras.setACUERDO(jsonResponse.optString("acuerdo"));
			oDataTras.setCODIDEXT(jsonResponse.optString("acuerdo"));
			oDataTras.setCONCEPTO(jsonResponse.optString("concepto"));
			oDataTras.setIMPORTE(jsonResponse.optString("IMPORTE"));
			oDataTras.setNOMBRECLIENTE(jsonResponse.optString("NOMBRECLIENTE"));
			oDataTras.setIDORIGENAPNT(jsonResponse.optString("IDORIGENAPNT"));
			oDataTras.setCODIDEXT(jsonResponse.optString("CODIDEXT"));
			oDataTras.setIDEXTERNO(jsonResponse.optString("IDEXTERNO"));
			
		}
		catch(Exception ex)
		{
			log.error("SetDataTrans" , ex);
		}
		return oDataTras;
	}
	
	
	public  EntResponRegCargAbono RegistraCargoAbono(EntMovCargoAbono oMovCargAbon) 
	{
		EndpointProperties endpointProperties = new EndpointProperties();
		EntResponRegCargAbono response = new EntResponRegCargAbono();
		String url = endpointProperties.getRegistraCargoAbono();
		String input = "";
		String salida = "";
		try 
		{
				
			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("entidad",oMovCargAbon.getEntidad() );
			datosEntrada.put("sucursal",oMovCargAbon.getSucursal() );
			datosEntrada.put("terminal",oMovCargAbon.getTerminal() );
			datosEntrada.put("empleado",oMovCargAbon.getEmpleado() );
			datosEntrada.put("tipoOperacion",oMovCargAbon.getTipoOperacion() );
			datosEntrada.put("fechaValor",oMovCargAbon.getFechaValor() );
			datosEntrada.put("fechaOperacion",oMovCargAbon.getFechaOperacion() );
			datosEntrada.put("horaOperacion",oMovCargAbon.getHoraOperacion() );
			datosEntrada.put("cajaIn",oMovCargAbon.getCajaIn() );
			datosEntrada.put("dataTrans",oMovCargAbon.getDataTrans() );

			JSONObject jsonObject = new JSONObject().put("RegistraMovimientosCargoAbono", datosEntrada);
			input = jsonObject.toString();
			salida = this.utilJson.callRestPost(input,url);
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("RegistraMovimientosCargoAbonoResp").getJSONObject("MovimientosCargoAbono");
				
					response.setStatus(Integer.parseInt( jsonResponse.optString("estatus")));
					response.setIdMovimiento(Integer.parseInt(jsonResponse.optString("idMovimiento")));
					response.setError(jsonResponse.optString("estatus"));
			}
			else
			{
				response.setStatus(0);
				response.setDescripcion("NO EXISTE EL MOVIMIENTO");
			}
			
		} 
		catch (Exception ex) 
		{
			response.setStatus(-1);
			response.setDescripcion(ex.getMessage());
			log.error("RegistraCargoAbono Entrada" + input);
			log.error("RegistraCargoAbono", ex);
		}		
		return response;
	}

	public ResponseService ActStatusMovCargoAbono(ActualizaStatusMovimientoCargoAbono oEntStat)
	{
		String input = "";
		String salida = "";
		ResponseService oResul = new ResponseService();
		try
		{
			EndpointProperties endpointProperties = new EndpointProperties();
			String url = endpointProperties.getActualizaStatusCargAbono();
			
			JSONObject datosEntrada = new JSONObject();
			datosEntrada.put("idMovimiento", oEntStat.getIdMovimiento());
			datosEntrada.put("statusOperacion", oEntStat.getStatusOperacion());
			datosEntrada.put("fechaContable", oEntStat.getFechaContable());
			datosEntrada.put("horaOperacion", oEntStat.getHoraOperacion());
			datosEntrada.put("folio", oEntStat.getFolio());
			
			JSONObject jsonObject = new JSONObject().put("ActualizaStatusMovimientoCargoAbono", datosEntrada);
			input = jsonObject.toString();
			salida = this.utilJson.callRestPost(input,url);
			if(salida.length()>0)
			{
				JSONObject jsonSalida = new JSONObject(salida);
				JSONObject jsonResponse = jsonSalida.getJSONObject("ActualizaStatusMovimientoCargoAbonoResp").getJSONObject("StatusMovimientoCargoAbono");
				oResul.setStatus(Integer.parseInt( jsonResponse.optString("estatus")));
			} else {
				oResul.setStatus(0);
				log.error("ActStatusMovCargoAbono Entrada - " + input);
				log.error("ActStatusMovCargoAbono Salida - " + salida);
			}
			
		}
		catch(Exception ex)
		{
			oResul.setStatus(0);
			log.error("ActStatusMovCargoAbono Error" , ex);
		}
		return oResul;
	}	

	public String ObtMensajeTcb(NodeList STD_MSJ_PARM_V)
	{
		String errores="";
		try
		{
			for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
			{
				
				Node item = STD_MSJ_PARM_V.item(i);
				Element eElement = (Element) item;
				String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(i).getTextContent();
				String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(i).getTextContent();
				String action="urn:getDescError";
				String xml= "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"+
				   "<soapenv:Header/>"+
				   "<soapenv:Body>"+
				      "<dat:getDescError>"+
				         "<dat:CodError>"+TEXT_CODE+"</dat:CodError>"+
				      "</dat:getDescError>"+
				   "</soapenv:Body>"+
				"</soapenv:Envelope>";
				
				String wsURL=this.propDs.getURL_ERROR_DESC();
				String outputString=diario.SalidaResponse(xml,wsURL,action,"");
			
					try
					{
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilderFactory dbFactor = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuild = dbFactory.newDocumentBuilder();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document document = dBuilder.parse(new ByteArrayInputStream(outputString.getBytes("UTF-8")));
						
						NodeList RespuetaDiario = document.getElementsByTagName("ErrorTCB");
						Node item2 = RespuetaDiario.item(i);
						Element eElement2 = (Element) item2;
						String mensaje=eElement2.getElementsByTagName("TextoMensaje").item(0).getTextContent();
						mensaje=mensaje.replaceAll("[������\\-\\+\\.\\^:,]","");//����
						mensaje=mensaje.replaceAll("\\u00FA", "�");
						mensaje=mensaje.replaceAll("\\u00F3", "�");
						mensaje=mensaje.replaceAll("ó", "�");
						mensaje=mensaje.replaceAll("��", " ");
						//mensaje=mensaje.replaceAll("\\u20ac", "");
						mensaje=mensaje.replaceAll("\\u00E9", "�");
						mensaje=mensaje.replaceAll("\\u00E1", "�");
						mensaje=mensaje.replaceAll("\\u00ED", "�");
						errores += TEXT_CODE + "|" + TEXT_ARG1+":"+mensaje+ ", ";
						i=STD_MSJ_PARM_V.getLength();
					}catch(Exception e)
					{
						log.error("ObtMensajeTcb : Exception" , e);
						System.out.println(e.getMessage());
						errores="La obtencion de errores fallo:"+e.getMessage();
					}
				
			}
		}catch(Exception ex){
			errores="";
		}
		return errores;
	}
	/*End E234 */
	
}
