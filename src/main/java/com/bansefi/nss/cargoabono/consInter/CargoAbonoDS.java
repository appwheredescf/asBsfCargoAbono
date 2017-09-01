package com.bansefi.nss.cargoabono.consInter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bansefi.nss.cargoabono.properties.EndpointProperties;
import com.bansefi.nss.cargoabono.vo.DiarioElectronicoRequest;
import com.bansefi.nss.cargoabono.vo.ReqInserMovCarAbo;
import com.bansefi.nss.cargoabono.vo.RespConsMovCarAbon;
import com.bansefi.nss.cargoabono.vo.ResqConsMovCarAbon;
import com.bansefi.nss.cargoabono.vo.ResponseService;

public class CargoAbonoDS {
	private EndpointProperties propDs = new EndpointProperties();
	private static final Logger log = LogManager.getLogger(CargoAbonoDS.class);
	
	public ResponseService InsertaMovCargAbon(ReqInserMovCarAbo request)
	{
		ResponseService response = new ResponseService();
		
		String outputString = "";
		String xml ="";
		try
		{
			String action = "urn:InsertaMovimientoCargoAbono";
						 
			 xml="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
				   +"<soapenv:Header/>"
				   +"<soapenv:Body>"
				   +"<dat:InsertaMovimientoCargoAbono>"
				   +"        <dat:ENTIDAD>"+request.getEntidad()+"</dat:ENTIDAD>"
				   +"      <dat:SUCURSAL>"+request.getSucursal()+"</dat:SUCURSAL>"
				   +"      <dat:TERMINAL>"+request.getTerminal()+"</dat:TERMINAL>"
				   +"      <dat:EMPLEADO>"+request.getEmpleado()+"</dat:EMPLEADO>"
				   +"      <dat:TIPO_OP>"+request.getTipOper()+"</dat:TIPO_OP>"
				   +"      <dat:FECHA_VALOR>"+request.getFechaVal()+"</dat:FECHA_VALOR>"
				   +"      <dat:FECHA_OPERACION>"+request.getFechaOper()+"</dat:FECHA_OPERACION>"
				   +"      <dat:HORAOP>"+request.getHoraOper()+"</dat:HORAOP>"
				   +"      <dat:CAJAINT>"+request.getCajaInt()+"</dat:CAJAINT>"
				   +"      <dat:DATATRANS>"+request.getDataTrans()+"</dat:DATATRANS>"
				   +"      <dat:FOLIOTRANS>"+request.getFolioTrans()+"</dat:FOLIOTRANS>"
				   +"   </dat:InsertaMovimientoCargoAbono>"
				   +"</soapenv:Body>"
				+"</soapenv:Envelope>";
			 
			
			String wsURL = propDs.getUrlConsCargAbo();
			
			outputString=SalidaResponse(xml,wsURL,action,"");
			if(outputString.contains("RespuestaAltaCargoAbono"))
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(outputString.getBytes("utf-8")));
				
				NodeList RespuetaDiario = doc.getElementsByTagName("RespuestaAltaCargoAbono");		
				Node item = RespuetaDiario.item(0);
				Element eElement = (Element) item;
				String StrEstat =	eElement.getElementsByTagName("ESTATUS").item(0).getTextContent();
				String StrError =	eElement.getElementsByTagName("ERROR").item(0).getTextContent();
				String StrIdMov =	eElement.getElementsByTagName("IDMOV").item(0).getTextContent();
				
				response.setStatus(Integer.parseInt(StrEstat));
				response.setTXT_ARG1(StrEstat);
				response.setCOD_TX(StrIdMov);
				response.setDescripcion("EXITO");
			} else {
			
				response.setStatus(0);
				response.setDescripcion(outputString);
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.error("CargoAbonoDS - InsertaMovCargAbon : Exception. " + e.getMessage());
			log.error("CargoAbonoDS - InsertaMovCargAbon : outputString. " + outputString);
			log.error("CargoAbonoDS - InsertaMovCargAbon : input xml. " + xml);
		}
		return response;
	}

	public RespConsMovCarAbon ConsultMovCargAbon(ResqConsMovCarAbon request)
	{
		RespConsMovCarAbon response = new RespConsMovCarAbon();
		
		String outputString = "";
		String xml ="";
		try
		{
			String action = "urn:ConsultaMovimientoCargoAbono";
						 
			 xml="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
			   +"<soapenv:Header/>"
			   +"<soapenv:Body>"
			   +"   <dat:ConsultaMovimientoCargoAbono>"
			   +"      <dat:Entidad>"+request.getEntidad()+"</dat:Entidad>"
			   +"      <dat:Sucursal>"+request.getSucursal()+"</dat:Sucursal>"
			   +"      <dat:Terminal>"+request.getTerminal()+"</dat:Terminal>"
			   +"      <dat:idEmple>"+request.getIdEmpleado()+"</dat:idEmple>"
			   +"   </dat:ConsultaMovimientoCargoAbono>"
			   +"</soapenv:Body>"
			+"</soapenv:Envelope>";
			
			String wsURL = propDs.getUrlConsCargAbo();
			
			outputString=SalidaResponse(xml,wsURL,action,"");
			if(outputString.contains("ConsultaMovimientoCargoAbonoResp"))
			{
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(outputString.getBytes("utf-8")));
				
				NodeList RespuetaDiario = doc.getElementsByTagName("MovimientoCargoAbonoConsulta");		
				Node item = RespuetaDiario.item(0);
				Element eElement = (Element) item;
				String StrIdMov =	eElement.getElementsByTagName("idMovimiento").item(0).getTextContent();
				String StrEntidad =	eElement.getElementsByTagName("entidad").item(0).getTextContent();
				String StrTipOper =	eElement.getElementsByTagName("tipoOp").item(0).getTextContent();
				String StrFecVal =	eElement.getElementsByTagName("fechaValor").item(0).getTextContent();
				String StrCajInt =	eElement.getElementsByTagName("cajaInt").item(0).getTextContent();
				String StrFecOper =	eElement.getElementsByTagName("fechaOperacion").item(0).getTextContent();
				String StrSucurs =	eElement.getElementsByTagName("sucursal").item(0).getTextContent();
				String StrTermina =	eElement.getElementsByTagName("idTerminal").item(0).getTextContent();
				String StrEmple =	eElement.getElementsByTagName("idEmpleado").item(0).getTextContent();
				String StrHoraOp =	eElement.getElementsByTagName("horaOp").item(0).getTextContent();
				String StrFecCont =	eElement.getElementsByTagName("fechaContable").item(0).getTextContent();
				String StrStatPr =	eElement.getElementsByTagName("statusProceso").item(0).getTextContent();
				String StrDatPr =	eElement.getElementsByTagName("dateProceso").item(0).getTextContent();
				String StrFolioTra =	eElement.getElementsByTagName("folioTrans").item(0).getTextContent();
				String StrDataTran =	eElement.getElementsByTagName("dataTrans").item(0).getTextContent();
				
				
				response.setIdMovimiento(StrIdMov);
				response.setEntidad(StrEntidad);
				response.setTipOper(StrTipOper);
				response.setFechValor(StrFecVal);
				response.setCajaInt(StrCajInt);
				response.setFecOper(StrFecOper);
				response.setSucursal(StrSucurs);
				response.setTerminal(StrTermina);
				response.setEmpleado(StrEmple);
				response.setHoraOper(StrHoraOp);
				response.setFecCont(StrFecCont);
				response.setStatProc(StrStatPr);
				response.setDateProc(StrDatPr);
				response.setFolioTrans(StrFolioTra);
				response.setDataTrans(StrDataTran);
				response.setStatus(1);
				response.setDescripcion("EXITO");
			} else {
			
				response.setStatus(0);
				response.setDescripcion(outputString);
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.error("CargoAbonoDS - ConsultMovCargAbon : Exception. " + e.getMessage());
			log.error("CargoAbonoDS - ConsultMovCargAbon : outputString. " + outputString);
			log.error("CargoAbonoDS - ConsultMovCargAbon : input xml. " + xml);
		}
		return response;	
	}
	
	public String SalidaResponse(String vista,String StrUrl,String action,String StrRep)
	{
		String salida="";
		String outputString="";
		try
		{			
			String responseString="";
			URL url = new URL(StrUrl);
			URLConnection connection = url.openConnection();
			//System.out.println(connection);
			HttpURLConnection httpConn = (HttpURLConnection)connection;
			//System.out.println(httpConn);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[vista.length()];
			buffer = vista.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			httpConn.setRequestProperty("Content-Length",
			String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", action);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			OutputStream out = httpConn.getOutputStream();
			out.write(b);
			out.close();
			InputStreamReader isr =
			new InputStreamReader(httpConn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			 
			//Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) 
			{
				outputString = outputString + responseString;
			}
			outputString = outputString.replace("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">", "");
			outputString = outputString.replace("<soapenv:Body>", "");
			if(StrRep.length()>0)
			{
				outputString = outputString.replace("<"+StrRep+" xmlns=\"http://ws.wso2.org/dataservice\">", "");
				outputString = outputString.replace("</"+StrRep+">", "");
			}
			outputString = outputString.replace("</soapenv:Body>", "");
			outputString = outputString.replace("</soapenv:Envelope>", "");
			outputString = outputString.trim();
			salida =outputString;
		}
		catch(Exception ex)
		{
			log.error("CargoAbonoDS -  : SalidaResponse[1]. " + ex.getMessage()+" vista "+vista+" url:  "+ StrUrl +" action "+ action+" res :"+ StrRep+" outputString: "+outputString);
		}
		
		return salida;
	}
}
