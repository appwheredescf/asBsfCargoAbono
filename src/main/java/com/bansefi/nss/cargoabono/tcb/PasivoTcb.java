
package com.bansefi.nss.cargoabono.tcb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bansefi.nss.cargoabono.properties.TcbProperties;
import com.bansefi.nss.cargoabono.services.PasivosAcuerdosServices;
import com.bansefi.nss.cargoabono.vo.ResponseConsultaClabe;
import com.bansefi.nss.cargoabono.vo.ResponseDatosCentro;
import com.bansefi.nss.cargoabono.vo.ResponseDatosEmpleado;
import com.bansefi.nss.cargoabono.vo.ResponseFechaHoraTCB;
import com.bansefi.nss.cargoabono.vo.ResponsePersona;
import com.bansefi.nss.cargoabono.vo.ResponseServiceCargoAbono;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion.AF_APNTE_AUDIT_V;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion.AF_APNTE_E;
import com.bansefi.nss.cargoabono.vo.ResponseUltimaTransaccion.AF_AUDIT_AUX;

public class PasivoTcb {
	private static final Logger log = LogManager.getLogger(PasivosAcuerdosServices.class);
	
	public ResponseServiceCargoAbono Cargo(
			String entidad, 
			String acuerdo, 
			String impNom, 
			String concepto, 
			String idTerminal) 
	{
		ResponseServiceCargoAbono response = new ResponseServiceCargoAbono();
		String salida = "";		
		TcbProperties prop = new TcbProperties();
		try
		{
			ResponseFechaHoraTCB fechaHoraTCB = new ResponseFechaHoraTCB();
			fechaHoraTCB = FechaContable(idTerminal);
			String strVista = getVistaCargo(prop.getCARGO_CODTX(), 
					prop.getCARGO_CODAPL(), 
					prop.getCARGO_TIPOOPER(),
					prop.getCARGO_CLOP(), 
					prop.getCARGO_SUBCLOP(), 
					prop.getCARGO_ISO(), 
					entidad, 
					acuerdo,
					impNom, 
					concepto, 
					idTerminal,
					fechaHoraTCB.getFechaOprcn(),
					fechaHoraTCB.getHoraOprcn(),
					fechaHoraTCB.getFechaCble());
			String soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>"
					+ "	<SOAP-ENV:Body> " + strVista+ "	</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"; 

			URL url;
			java.net.URLConnection conn = null;
			try 
			{
				url = new URL(prop.getURL_CARGO());
				try 
				{
					conn = url.openConnection();
				} 
				catch (IOException e) 
				{
					log.info("PasivoTcb - Cargo : View In .- " + strVista);
					log.info("PasivoTcb - Cargo : URL_CARGO .- " + prop.getURL_CARGO());
					log.error("PasivoTcb - Cargo : IOException. " + e.getMessage());
				}
			} 
			catch (MalformedURLException e1) 
			{
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("PasivoTcb - Cargo : View In .- " + strVista);
				log.info("PasivoTcb - Cargo : URL_CARGO .- " + prop.getURL_CARGO());
				log.error("PasivoTcb - Cargo : MalformedURLException. " + e1.getMessage());
			}
			conn.setRequestProperty("SOAPAction", prop.getURL_CARGO());
			conn.setDoOutput(true);
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
				log.info("PasivoTcb - Cargo : View In .- " + strVista);
				log.error("PasivoTcb - Cargo : OutputStreamWriter. " + e2.getMessage());
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
				log.info("PasivoTcb - Cargo : View In .- " + strVista);
				log.error("PasivoTcb - Cargo : BufferedReader. " + e1.getMessage());
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
				NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_IMPUTAC_VTNLLA_PASIVO_TRN_O");
				System.out.println(strVista);
				System.out.println(salida);
				String RTRN_CD = "";
				for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ )
				{
					Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
					Element eElement = (Element) item;
					RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
				}
				
				if(RTRN_CD.equals("1"))
				{
					Element PSV_NOMBRE_TITULAR_V = (Element)doc.getElementsByTagName("PSV_NOMBRE_TITULAR_V").item(0);
					String NOMBRE_TITULAR = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NOMB_50").item(0).getTextContent();
					
					Element INF_RECIBO_BANSEFI_V = (Element)doc.getElementsByTagName("INF_RECIBO_BANSEFI_V").item(0);
					String NOMB_PDV = INF_RECIBO_BANSEFI_V.getElementsByTagName("NOMB_PDV").item(0).getTextContent();
					String RECIBO_BANSEFI_NOMB_50 = INF_RECIBO_BANSEFI_V.getElementsByTagName("NOMB_50").item(0).getTextContent();
					String COD_PLZ_BANCARIA = INF_RECIBO_BANSEFI_V.getElementsByTagName("COD_PLZ_BANCARIA").item(0).getTextContent();
					Element TR_IMPUTAC_VTNLLA_PASIVO_EVT_Z = (Element)doc.getElementsByTagName("TR_IMPUTAC_VTNLLA_PASIVO_EVT_Z").item(0);
					String FECHA_VALOR = TR_IMPUTAC_VTNLLA_PASIVO_EVT_Z.getElementsByTagName("FECHA_VALOR").item(0).getTextContent();
					Element STD_TRN_O_PARM_V = (Element)doc.getElementsByTagName("STD_TRN_O_PARM_V").item(0);
					String FECHA_OPRCN = STD_TRN_O_PARM_V.getElementsByTagName("FECHA_OPRCN").item(0).getTextContent();
					String HORA_OPRCN = STD_TRN_O_PARM_V.getElementsByTagName("HORA_OPRCN").item(0).getTextContent();
					Element PSV_APUNTE_V = (Element)doc.getElementsByTagName("PSV_APUNTE_V").item(0);
					String NUM_SEC = PSV_APUNTE_V.getElementsByTagName("NUM_SEC").item(0).getTextContent();
					
					response.setStatus(1);
					response.setCOD_PLZ_BANCARIA(COD_PLZ_BANCARIA);
					response.setFECHACONTABLE(FECHA_VALOR);
					response.setFECHAVALOR(FECHA_VALOR);
					response.setFECHAOPERA(FECHA_OPRCN);
					response.setHORAOPERACION(HORA_OPRCN);
					response.setNOMB_PDV(NOMB_PDV);
					response.setRECIBO_BANSEFI_NOMB_50(RECIBO_BANSEFI_NOMB_50);
					response.setTITULAR_NOMB_50(NOMBRE_TITULAR);
					response.setNUM_SEC(NUM_SEC);
				}
				else 
				{
					
					log.info("PasivoTcb - Cargo : View In .- " + strVista);
					log.info("PasivoTcb - Cargo : View Out .- " + salida);
					String errores = "";
					NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
					for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
					{
						Node item = STD_MSJ_PARM_V.item(i);
						Element eElement = (Element) item;
						String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
						String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
						errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
					}
					response.setDescripcion(errores);
				}
			}
			catch (Exception e) 
			{
				response.setStatus(-1);
				response.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - Cargo : View In .- " + strVista);
				log.error("PasivoTcb - Cargo : Exception Read Out. " + e.getMessage());
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.error("PasivoTcb - Cargo : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public ResponseServiceCargoAbono Abono(
			String entidad, 
			String acuerdo, 
			String impNom, 
			String concepto, 
			String idTerminal) 
	{
		ResponseServiceCargoAbono response = new ResponseServiceCargoAbono();
		String salida = "";		
		TcbProperties prop = new TcbProperties();
		try
		{
			ResponseFechaHoraTCB fechaHoraTCB = new ResponseFechaHoraTCB();
			fechaHoraTCB = FechaContable(idTerminal);
			String strVista = getVistaAbono(prop.getABONO_CODTX(), 
					prop.getABONO_CODAPL(), 
					prop.getABONO_TIPOOPER(),
					prop.getABONO_CLOP(), 
					prop.getABONO_SUBCLOP(), 
					prop.getABONO_ISO(), 
					entidad, 
					acuerdo,
					impNom, 
					concepto, 
					idTerminal,
					fechaHoraTCB.getFechaOprcn(),
					fechaHoraTCB.getHoraOprcn(),
					fechaHoraTCB.getFechaCble());
			String soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>"
					+ "	<SOAP-ENV:Body> " + strVista+ "	</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"; 

			URL url;
			java.net.URLConnection conn = null;
			try 
			{
				url = new URL(prop.getURL_ABONO());
				try 
				{
					conn = url.openConnection();
				} 
				catch (IOException e) 
				{
					log.info("PasivoTcb - ABONO : View In .- " + strVista);
					log.info("PasivoTcb - ABONO : URL_CARGO .- " + prop.getURL_ABONO());
					log.error("PasivoTcb - ABONO : IOException. " + e.getMessage());
				}
			} 
			catch (MalformedURLException e1) 
			{
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("PasivoTcb - ABONO : View In .- " + strVista);
				log.info("PasivoTcb - ABONO : URL_CARGO .- " + prop.getURL_ABONO());
				log.error("PasivoTcb - ABONO : MalformedURLException. " + e1.getMessage());
			}
			conn.setRequestProperty("SOAPAction", prop.getURL_ABONO());
			conn.setDoOutput(true);
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
				log.info("PasivoTcb - ABONO : View In .- " + strVista);
				log.error("PasivoTcb - ABONO : OutputStreamWriter. " + e2.getMessage());
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
				log.info("PasivoTcb - ABONO : View In .- " + strVista);
				log.error("PasivoTcb - ABONO : BufferedReader. " + e1.getMessage());
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
				
				System.out.println(strVista);
				System.out.println(salida);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
				NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_IMPUT_VTNLLA_PASIVO_2_TRN_O");
				
				String RTRN_CD = "";
				for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ )
				{
					Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
					Element eElement = (Element) item;
					RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
				}
				
				if(RTRN_CD.equals("1"))
				{
					Element PSV_NOMBRE_TITULAR_V = (Element)doc.getElementsByTagName("PSV_NOMBRE_TITULAR_V").item(0);
					String NOMBRE_TITULAR = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NOMB_50").item(0).getTextContent();
					
					Element INF_RECIBO_BANSEFI_V = (Element)doc.getElementsByTagName("INF_RECIBO_BANSEFI_V").item(0);
					String NOMB_PDV = INF_RECIBO_BANSEFI_V.getElementsByTagName("NOMB_PDV").item(0).getTextContent();
					String RECIBO_BANSEFI_NOMB_50 = INF_RECIBO_BANSEFI_V.getElementsByTagName("NOMB_50").item(0).getTextContent();
					String COD_PLZ_BANCARIA = INF_RECIBO_BANSEFI_V.getElementsByTagName("COD_PLZ_BANCARIA").item(0).getTextContent();
					Element TR_IMPUTAC_VTNLLA_PASIVO_EVT_Z = (Element)doc.getElementsByTagName("TR_IMPUTAC_VTNLLA_PASIVO_EVT_Z").item(0);
					String FECHA_VALOR = TR_IMPUTAC_VTNLLA_PASIVO_EVT_Z.getElementsByTagName("FECHA_VALOR").item(0).getTextContent();
					Element STD_TRN_O_PARM_V = (Element)doc.getElementsByTagName("STD_TRN_O_PARM_V").item(0);
					String FECHA_OPRCN = STD_TRN_O_PARM_V.getElementsByTagName("FECHA_OPRCN").item(0).getTextContent();
					String HORA_OPRCN = STD_TRN_O_PARM_V.getElementsByTagName("HORA_OPRCN").item(0).getTextContent();
					
					Element PSV_APUNTE_V = (Element)doc.getElementsByTagName("PSV_APUNTE_V").item(0);
					String NUM_SEC = PSV_APUNTE_V.getElementsByTagName("NUM_SEC").item(0).getTextContent();
					response.setStatus(1);
					response.setCOD_PLZ_BANCARIA(COD_PLZ_BANCARIA);
					response.setFECHACONTABLE(FECHA_VALOR);
					response.setFECHAVALOR(FECHA_VALOR);
					response.setFECHAOPERA(FECHA_OPRCN);
					response.setHORAOPERACION(HORA_OPRCN);
					response.setNOMB_PDV(NOMB_PDV);
					response.setRECIBO_BANSEFI_NOMB_50(RECIBO_BANSEFI_NOMB_50);
					response.setTITULAR_NOMB_50(NOMBRE_TITULAR);
					response.setNUM_SEC(NUM_SEC);
					
				}
				else 
				{
					log.info("PasivoTcb - ABONO : View In .- " + strVista);
					log.info("PasivoTcb - ABONO : View Out .- " + salida);
					String errores = "";
					NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
					for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
					{
						Node item = STD_MSJ_PARM_V.item(i);
						Element eElement = (Element) item;
						String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
						String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
						errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
					}
					response.setDescripcion(errores);
				}
			}
			catch (Exception e)
			{
				response.setStatus(-1);
				response.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - ABONO : View In .- " + strVista);
				log.error("PasivoTcb - ABONO : Exception Read Out. " + e.getMessage());
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.error("PasivoTcb - ABONO : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public ResponseFechaHoraTCB FechaContable(String terminal) 
	{
		ResponseFechaHoraTCB response = new ResponseFechaHoraTCB();
		String salida = "";		
		TcbProperties prop = new TcbProperties();
		try
		{
			String strVista = getVistaFechaContable(terminal);
			String soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>"
					+ "	<SOAP-ENV:Body> " + strVista + "	</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"; 
			URL url;
			java.net.URLConnection conn = null;
			try 
			{
				url = new URL(prop.getURL_FECHA_CTBLE());
				try 
				{
					conn = url.openConnection();
				} 
				catch (IOException e) 
				{
					response.setStatus(-1);
					response.setDescripcion(e.getMessage());
					log.info("PasivoTcb - FechaContable : View In .- " + strVista);
					log.info("PasivoTcb - FechaContable : URL_FECHA_CTBLE .- " + prop.getURL_FECHA_CTBLE());
					log.error("PasivoTcb - FechaContable : openConnection. " + e.getMessage());
				}
			} 
			catch (MalformedURLException e1) 
			{
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("PasivoTcb - FechaContable : View In .- " + strVista);
				log.info("PasivoTcb - FechaContable : URL_FECHA_CTBLE .- " + prop.getURL_FECHA_CTBLE());
				log.error("PasivoTcb - FechaContable : MalformedURLException. " + e1.getMessage());
			}
			conn.setRequestProperty("SOAPAction", prop.getURL_CARGO());
			conn.setDoOutput(true);
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
				log.info("PasivoTcb - FechaContable : View In .- " + strVista);
				log.error("PasivoTcb - FechaContable : OutputStreamWriter. " + e2.getMessage());
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
				log.info("PasivoTcb - FechaContable : View In .- " + strVista);
				log.error("PasivoTcb - FechaContable : BufferedReader. " + e1.getMessage());
			}
			try
			{
				String line = "";
				salida = "";					//LECTURA DE VISTA DE SALIDA 
				while ((line = rd.readLine()) != null) 
				{ 
					salida += line;
				}
				//Eliminar envolvente soap
				salida = salida.replaceAll("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">", "");
				salida = salida.replaceAll("<SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Envelope>", "");
				salida = salida.trim();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
				NodeList STD_TRN_VAL_SRV_O = doc.getElementsByTagName("STD_TRN_VAL_SRV_O");
				
				String RTRN_CD = "";
				for(int i = 0 ; i < STD_TRN_VAL_SRV_O.getLength()  ; i++ )
				{
					Node item = STD_TRN_VAL_SRV_O.item(i);
					Element eElement = (Element) item;
					RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
				}
				
				if(RTRN_CD.equals("1"))
				{
					NodeList STD_APPL_PARM_V = doc.getElementsByTagName("STD_APPL_PARM_V");
					for(int i = 0 ; i < STD_APPL_PARM_V.getLength()  ; i++ )
					{
						Node item = STD_APPL_PARM_V.item(i);
						Element eElement = (Element) item;
						String FECHA_CTBLE = eElement.getElementsByTagName("FECHA_CTBLE").item(0).getTextContent();
						String HORA_OPRCN = eElement.getElementsByTagName("HORA_OPRCN").item(0).getTextContent();
						String FECHA_OPRCN = eElement.getElementsByTagName("FECHA_OPRCN").item(0).getTextContent();
						response.setStatus(1);
						response.setFechaCble(FECHA_CTBLE);
						response.setFechaOprcn(FECHA_OPRCN);
						response.setHoraOprcn(HORA_OPRCN);
					}
				} 
				else {
					response.setStatus(0);
					
					log.info("PasivoTcb - FechaContable : View In .- " + strVista);
					log.info("PasivoTcb - FechaContable : View Out .- " + salida);
					String errores = "";
					NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_MSJ_PARM_V");
					for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
					{
						Node item = STD_MSJ_PARM_V.item(i);
						Element eElement = (Element) item;
						String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
						String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
						errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
					}
					response.setDescripcion(errores);
				}
			}
			catch (Exception e) 
			{
				response.setStatus(-1);
				response.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - FechaContable : View In .- " + strVista);
				log.info("PasivoTcb - FechaContable : View Out .- " + salida);
				log.error("PasivoTcb - FechaContable : Exception Read Out. " + e.getMessage());
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.error("PasivoTcb - FechaContable : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public ResponseUltimaTransaccion UltimasTransacciones(String COD_NRBE_EN, String NUM_SEC_AC, String FECHA_CNTBL) 
	{
		ResponseUltimaTransaccion response = new ResponseUltimaTransaccion();
		String salida = "";		
		TcbProperties prop = new TcbProperties();
		try
		{
			String strVista = getVistaUltimasTransacciones(COD_NRBE_EN, NUM_SEC_AC, FECHA_CNTBL);
			String soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>"
					+ "	<SOAP-ENV:Body> " + strVista + "	</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"; 

			URL url;
			java.net.URLConnection conn = null;
			try {
				url = new URL(prop.getURL_CONS_ULT_TRANS());
				try {
					conn = url.openConnection();
				} catch (IOException e) {
					response.setStatus(-1);
					response.setDescripcion(e.getMessage());
					log.info("PasivoTcb - UltimasTransacciones : View In .- " + strVista);
					log.info("PasivoTcb - UltimasTransacciones : URL_FECHA_CTBLE .- " + prop.getURL_CONS_ULT_TRANS());
					log.error("PasivoTcb - UltimasTransacciones : openConnection. " + e.getMessage());
				}
			} catch (MalformedURLException e1) {
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("PasivoTcb - UltimasTransacciones : View In .- " + strVista);
				log.info("PasivoTcb - UltimasTransacciones : URL_CONS_ULT_TRANS .- " + prop.getURL_CONS_ULT_TRANS());
				log.error("PasivoTcb - UltimasTransacciones : MalformedURLException. " + e1.getMessage());
			}
			conn.setRequestProperty("SOAPAction", prop.getURL_CARGO());
			conn.setDoOutput(true);
			//Send the request
			java.io.OutputStreamWriter wr;
			try {
				wr = new java.io.OutputStreamWriter(conn.getOutputStream());
				wr.write(soapXml);
				wr.flush();
			} catch (IOException e2) {
				response.setStatus(-1);
				response.setDescripcion(e2.getMessage());
				log.info("PasivoTcb - UltimasTransacciones : View In .- " + strVista);
				log.error("PasivoTcb - UltimasTransacciones : OutputStreamWriter. " + e2.getMessage());
			}

			// Read the response
			java.io.BufferedReader rd = null;
			try {
				rd = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
			} catch (IOException e1) {
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("PasivoTcb - UltimasTransacciones : View In .- " + strVista);
				log.error("PasivoTcb - UltimasTransacciones : BufferedReader. " + e1.getMessage());
			}

			try{
				String line = "";
				salida = ""; 
						
				while ((line = rd.readLine()) != null) { 
					//LECTURA DE VISTA DE SALIDA
					if(!line.contains("<ID_ORGN_APNTE>"))
						salida += line;
				}
				//Eliminar envolvente soap
				salida = salida.replaceAll("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">", "");
				salida = salida.replaceAll("<SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Envelope>", "");
				salida = salida.trim();
				
				System.out.println(salida);
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
				NodeList AF_APNTE_E_LST_SRV_O = doc.getElementsByTagName("AF_APNTE_E_LST_SRV_O");
				
				String RTRN_CD = "";
				for(int i = 0 ; i < AF_APNTE_E_LST_SRV_O.getLength()  ; i++ ){
					Node item = AF_APNTE_E_LST_SRV_O.item(i);
					Element eElement = (Element) item;
					RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
				}
				
				if(RTRN_CD.equals("1")){
					ArrayList<AF_APNTE_AUDIT_V> apuntes = new ArrayList<ResponseUltimaTransaccion.AF_APNTE_AUDIT_V>();
					
					NodeList _AF_APNTE_AUDIT_V = doc.getElementsByTagName("AF_APNTE_AUDIT_V");
					for(int i = 0 ; i < _AF_APNTE_AUDIT_V.getLength()  ; i++ ){
						ResponseUltimaTransaccion ultimaTransaccion = new ResponseUltimaTransaccion();
						
						Node item = _AF_APNTE_AUDIT_V.item(i);
						Element eElement = (Element) item;

						NodeList _AF_APNTE_E = eElement.getElementsByTagName("AF_APNTE_E");
						NodeList _AF_AUDIT_AUX = eElement.getElementsByTagName("AF_AUDIT_AUX");
						

						AF_APNTE_AUDIT_V afApnteAudit = ultimaTransaccion.getUltimoApunte(); 
						AF_APNTE_E afApnte = afApnteAudit.getAfApnteE();
						AF_AUDIT_AUX afAudit = afApnteAudit.getAfAuditAux();
						
						Element eAfApunteE = (Element)_AF_APNTE_E.item(0);
						Element efAuditAux = (Element)_AF_AUDIT_AUX.item(0);
						
						afApnte.setCOD_NRBE_EN(eAfApunteE.getElementsByTagName("COD_NRBE_EN").item(0).getTextContent());
						afApnte.setCOD_CENT_UO(eAfApunteE.getElementsByTagName("COD_CENT_UO").item(0).getTextContent());
						afApnte.setPRPDAD_CTA(eAfApunteE.getElementsByTagName("PRPDAD_CTA").item(0).getTextContent());
						afApnte.setNUM_SEC_AC(eAfApunteE.getElementsByTagName("NUM_SEC_AC").item(0).getTextContent());
						afApnte.setNUM_SUBAC(eAfApunteE.getElementsByTagName("NUM_SUBAC").item(0).getTextContent());
						afApnte.setID_EXP_RECLAM(eAfApunteE.getElementsByTagName("ID_EXP_RECLAM").item(0).getTextContent());
						afApnte.setCOD_CTA(eAfApunteE.getElementsByTagName("COD_CTA").item(0).getTextContent());
						afApnte.setCOD_NUMRCO_MONEDA(eAfApunteE.getElementsByTagName("COD_NUMRCO_MONEDA").item(0).getTextContent());
						afApnte.setNUM_SEC(eAfApunteE.getElementsByTagName("NUM_SEC").item(0).getTextContent());
						afApnte.setSGN(eAfApunteE.getElementsByTagName("SGN").item(0).getTextContent());
						afApnte.setIMP_APNTE(eAfApunteE.getElementsByTagName("IMP_APNTE").item(0).getTextContent());
						afApnte.setFECHA_CTBLE(eAfApunteE.getElementsByTagName("FECHA_CTBLE").item(0).getTextContent());
						afApnte.setFECHA_VALOR(eAfApunteE.getElementsByTagName("FECHA_VALOR").item(0).getTextContent());
						afApnte.setIND_1(eAfApunteE.getElementsByTagName("IND_1").item(0).getTextContent());
						afApnte.setIND_2(eAfApunteE.getElementsByTagName("IND_2").item(0).getTextContent());
						afApnte.setIND_3(eAfApunteE.getElementsByTagName("IND_3").item(0).getTextContent());
						afApnte.setIND_4(eAfApunteE.getElementsByTagName("IND_4").item(0).getTextContent());
						afApnte.setIND_5(eAfApunteE.getElementsByTagName("IND_5").item(0).getTextContent());
						afApnte.setIND_6(eAfApunteE.getElementsByTagName("IND_6").item(0).getTextContent());
						afApnte.setIND_7(eAfApunteE.getElementsByTagName("IND_7").item(0).getTextContent());
						afApnte.setIND_8(eAfApunteE.getElementsByTagName("IND_8").item(0).getTextContent());
						afApnte.setIND_9(eAfApunteE.getElementsByTagName("IND_9").item(0).getTextContent());
						afApnte.setIND_10(eAfApunteE.getElementsByTagName("IND_10").item(0).getTextContent());
						afApnte.setCOD_ORGN_APNTE(eAfApunteE.getElementsByTagName("COD_ORGN_APNTE").item(0).getTextContent());
						afApnte.setCONCPT_APNTE(eAfApunteE.getElementsByTagName("CONCPT_APNTE").item(0).getTextContent());
						afApnte.setCOD_ORIGEN(eAfApunteE.getElementsByTagName("COD_ORIGEN").item(0).getTextContent());
						afApnte.setCOD_INTERNO_UO(eAfApunteE.getElementsByTagName("COD_INTERNO_UO").item(0).getTextContent());
						afApnte.setCOD_LINEA(eAfApunteE.getElementsByTagName("COD_LINEA").item(0).getTextContent());
						afApnte.setID_GRP_PD(eAfApunteE.getElementsByTagName("ID_GRP_PD").item(0).getTextContent());
						
						afApnteAudit.setAfApnteE(afApnte);
						
						afAudit.setID_INTERNO_TERM_TN(efAuditAux.getElementsByTagName("ID_INTERNO_TERM_TN").item(0).getTextContent());
						afAudit.setFECHA_OPRCN(efAuditAux.getElementsByTagName("FECHA_OPRCN").item(0).getTextContent());
						afAudit.setHORA_OPRCN(efAuditAux.getElementsByTagName("HORA_OPRCN").item(0).getTextContent());
						afAudit.setCOD_TX(efAuditAux.getElementsByTagName("COD_TX").item(0).getTextContent());
						afAudit.setID_EMPL_AUT(efAuditAux.getElementsByTagName("ID_EMPL_AUT").item(0).getTextContent());
						afAudit.setID_INTERNO_EMPL_EP(efAuditAux.getElementsByTagName("ID_INTERNO_EMPL_EP").item(0).getTextContent());
						afAudit.setCOD_NRBE_EN_1(efAuditAux.getElementsByTagName("COD_NRBE_EN_1").item(0).getTextContent());
						afAudit.setCOD_INTERNO_UO(efAuditAux.getElementsByTagName("COD_INTERNO_UO").item(0).getTextContent());
						afAudit.setCOD_INDIC(efAuditAux.getElementsByTagName("COD_INDIC").item(0).getTextContent());
						afAudit.setFECHA_CTBLE(efAuditAux.getElementsByTagName("FECHA_CTBLE").item(0).getTextContent());
						afApnteAudit.setAfAuditAux(afAudit);
						
						apuntes.add(afApnteAudit);
						if(eAfApunteE.getElementsByTagName("NUM_SEC_AC").item(0).getTextContent().length() > 1)
							response.setUltimoApunte(afApnteAudit);

					}
					response.setApuntes(apuntes);
					response.setStatus(1);
				} else {
					response.setStatus(0);
					
					log.info("PasivoTcb - UltimasTransacciones : View In .- " + strVista);
					log.info("PasivoTcb - UltimasTransacciones : View Out .- " + salida);
					String errores = "";
					NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_MSJ_PARM_V");
					for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ ){
						Node item = STD_MSJ_PARM_V.item(i);
						Element eElement = (Element) item;
						String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
						String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
						errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
					}
					response.setDescripcion(errores);
				}

			}catch (Exception e) {
				response.setStatus(-1);
				response.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - UltimasTransacciones : View In .- " + strVista);
				log.info("PasivoTcb - UltimasTransacciones : View Out .- " + salida);
				log.error("PasivoTcb - UltimasTransacciones : Exception Read Out. " + e.getMessage());
			}
		} catch (Exception e) {
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.error("PasivoTcb - FechaContable : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public ResponseConsultaClabe ConsultaClabe(String NUM_SEC_AC, String COD_NRBE_EN, String ID_INTERNO_TERM_TN) {
		ResponseConsultaClabe response = new ResponseConsultaClabe();
		String salida = "";		
		TcbProperties prop = new TcbProperties();
		try{
			String strVista = getVistaConsultaClabe(NUM_SEC_AC, COD_NRBE_EN, ID_INTERNO_TERM_TN);
			String soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>"
					+ "	<SOAP-ENV:Body> " + strVista + "	</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"; 

			URL url;
			java.net.URLConnection conn = null;
			try {
				url = new URL(prop.getURL_CLABE());
				try {
					conn = url.openConnection();
				} catch (IOException e) {
					response.setStatus(-1);
					response.setDescripcion(e.getMessage());
					log.info("PasivoTcb - ConsultaClabe : View In .- " + strVista);
					log.info("PasivoTcb - ConsultaClabe : URL_CLABE .- " + prop.getURL_CLABE());
					log.error("PasivoTcb - ConsultaClabe : openConnection. " + e.getMessage());
				}
			} catch (MalformedURLException e1) {
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("PasivoTcb - ConsultaClabe : View In .- " + strVista);
				log.info("PasivoTcb - ConsultaClabe : URL_CLABE .- " + prop.getURL_CLABE());
				log.error("PasivoTcb - ConsultaClabe : MalformedURLException. " + e1.getMessage());
			}
			conn.setRequestProperty("SOAPAction", prop.getURL_CLABE());
			conn.setDoOutput(true);
			//Send the request
			java.io.OutputStreamWriter wr;
			try {
				wr = new java.io.OutputStreamWriter(conn.getOutputStream());
				wr.write(soapXml);
				wr.flush();
			} catch (IOException e2) {
				response.setStatus(-1);
				response.setDescripcion(e2.getMessage());
				log.info("PasivoTcb - ConsultaClabe : View In .- " + strVista);
				log.error("PasivoTcb - ConsultaClabe : OutputStreamWriter. " + e2.getMessage());
			}

			// Read the response
			java.io.BufferedReader rd = null;
			try {
				rd = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
			} catch (IOException e1) {
				response.setStatus(-1);
				response.setDescripcion(e1.getMessage());
				log.info("PasivoTcb - ConsultaClabe : View In .- " + strVista);
				log.error("PasivoTcb - ConsultaClabe : BufferedReader. " + e1.getMessage());
			}

			try{
				String line = "";
				salida = ""; 
						
				while ((line = rd.readLine()) != null) { 
					//LECTURA DE VISTA DE SALIDA
					salida += line;
				}
				//Eliminar envolvente soap
				salida = salida.replaceAll("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">", "");
				salida = salida.replaceAll("<SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Body>", "");
				salida = salida.replaceAll("</SOAP-ENV:Envelope>", "");
				salida = salida.trim();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
				NodeList STD_TRN_VAL_SRV_O = doc.getElementsByTagName("TR_CONSULTA_CLABE_TRN_O");
				
				String RTRN_CD = "";
				for(int i = 0 ; i < STD_TRN_VAL_SRV_O.getLength()  ; i++ )
				{
					Node item = STD_TRN_VAL_SRV_O.item(i);
					Element eElement = (Element) item;
					RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
				}
				
				if(RTRN_CD.equals("1"))
				{
					NodeList TF_CTA_CLABE_V = doc.getElementsByTagName("TF_CTA_CLABE_V");
					for(int i = 0 ; i < TF_CTA_CLABE_V.getLength()  ; i++ ){
						Node item = TF_CTA_CLABE_V.item(i);
						Element eElement = (Element) item;
						String COD_NRBE_CLABE_V = eElement.getElementsByTagName("COD_NRBE_CLABE_V").item(0).getTextContent().trim();
						String COD_PLZ_BANCARIA = eElement.getElementsByTagName("COD_PLZ_BANCARIA").item(0).getTextContent().trim();
						String NUM_SEC_AC_CLABE_V = eElement.getElementsByTagName("NUM_SEC_AC_CLABE_V").item(0).getTextContent().trim();
						response.setStatus(1);
						response.setCOD_NRBE_CLABE_V(COD_NRBE_CLABE_V);
						response.setCOD_PLZ_BANCARIA(COD_PLZ_BANCARIA);
						response.setNUM_SEC_AC_CLABE_V(NUM_SEC_AC_CLABE_V);
					}
				} 
				else 
				{
					response.setStatus(0);
					
					log.info("PasivoTcb - ConsultaClabe : View In .- " + strVista);
					log.info("PasivoTcb - ConsultaClabe : View Out .- " + salida);
					String errores = "";
					NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
					for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ ){
						Node item = STD_MSJ_PARM_V.item(i);
						Element eElement = (Element) item;
						String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
						String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
						errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
					}
					response.setDescripcion(errores);
				}
			}
			catch (Exception e) 
			{
				response.setStatus(-1);
				response.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - ConsultaClabe : View In .- " + strVista);
				log.info("PasivoTcb - ConsultaClabe : View Out .- " + salida);
				log.error("PasivoTcb - ConsultaClabe : Exception Read Out. " + e.getMessage());
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.error("PasivoTcb - ConsultaClabe : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public String getVistaCargo(String COD_TX, String COD_APLCCN_SUBAPL,String TIPO_OPRCN, String CLOP, String TIPO_SBCLOP, String COD_NUMRCO_MONEDA, 
									String COD_NRBE_EN, 
									String NUM_SEC_AC,
									String IMP_NOMINAL,
									String TEXTO_REMITENTE,
									String ID_INTERNO_TERM_TN,
									String FECHA_OPRCN,
									String HORA_OPRCN,
									String FECHA_CNTBL){
		String xmlIn = "<TR_IMPUTAC_VTNLLA_PASIVO_TRN>"
				+ "			 <TR_IMPUTAC_VTNLLA_PASIVO_TRN_I>"
				+ "			  <STD_TRN_I_PARM_V>"
				+ "			   <ID_INTERNO_TERM_TN>" + ID_INTERNO_TERM_TN + "</ID_INTERNO_TERM_TN>"
				+ "			   <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			   <NUM_SEC>" + COD_NRBE_EN + "</NUM_SEC>"
				+ "			   <COD_TX>" + COD_TX + "</COD_TX>"
				+ "			   <COD_TX_DI></COD_TX_DI>"
				+ "			  </STD_TRN_I_PARM_V>"
				+ "			  <STD_AUTORIZA_V>"
				+ "			   <IND_BORRADO_AR></IND_BORRADO_AR>"
				+ "			   <DESCR_TX></DESCR_TX>"
				+ "			   <IND_AUT_SOLIC></IND_AUT_SOLIC>"
				+ "			   <IND_ATRIB_MANC_EP></IND_ATRIB_MANC_EP>"
				+ "			   <COD_ESTADO_AR></COD_ESTADO_AR>"
				+ "			   <ID_EMPL_SOL_AUT></ID_EMPL_SOL_AUT>"
				+ "			   <IND_VERIF_ATRIB></IND_VERIF_ATRIB>"
				+ "			   <IND_URG_AR></IND_URG_AR>"
				+ "			   <MOTIVO_ACCION_AUT></MOTIVO_ACCION_AUT>"
				+ "			   <IND_ESCALABLE></IND_ESCALABLE>"
				+ "			   <IMP_AUT>0</IMP_AUT>"
				+ "			   <IMPORTE_AR>0</IMPORTE_AR>"
				+ "			   <AR_AUT_REMOTA_P>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			    <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			    <FECHA_OPRCN>" + FECHA_CNTBL + "</FECHA_OPRCN>"
				+ "			    <HORA_OPRCN>" + HORA_OPRCN + "</HORA_OPRCN>"
				+ "			   </AR_AUT_REMOTA_P>"
				+ "			  <AR_TRN_MSJ_PARM_V_OCCUR>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			  </AR_TRN_MSJ_PARM_V_OCCUR>"
				+ "			  <STD_TARGET_TERMINAL_V_OCCUR>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			  </STD_TARGET_TERMINAL_V_OCCUR>"
				+ "			   <AR_ID_SALTADO_V>"
				+ "			    <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			   </AR_ID_SALTADO_V>"
				+ "			  </STD_AUTORIZA_V>"
				+ "			  <PSV_PAGO_PARC_DA>"
				+ "			   <NOMB_50></NOMB_50>"
				+ "			   <NIF></NIF>"
				+ "			   <FECHA_EMISION></FECHA_EMISION>"
				+ "			   <DOMIC></DOMIC>"
				+ "			  </PSV_PAGO_PARC_DA>"
				+ "			  <PSV_LIBRETA_V>"
				+ "			   <STD_CHAR_01></STD_CHAR_01>"
				+ "			  </PSV_LIBRETA_V>"
				+ "			  <PSV_INTERVENCION_V>"
				+ "			   <STD_CHAR_01></STD_CHAR_01>"
				+ "			  </PSV_INTERVENCION_V>"
				+ "			  <TR_IMPUTAC_VTNLLA_PASIVO_EVT_Y>"
				+ "			   <AC_AC_P>"
				+ "			    <COD_NRBE_EN>" + COD_NRBE_EN + "</COD_NRBE_EN>"
				+ "			    <COD_CENT_UO></COD_CENT_UO>"
				+ "			    <NUM_SEC_AC>" + NUM_SEC_AC + "</NUM_SEC_AC>"
				+ "			   </AC_AC_P>"
				+ "			   <COD_APLCCN_SUBAPL>" +  COD_APLCCN_SUBAPL + "</COD_APLCCN_SUBAPL>"
				+ "			   <PSV_ENTIDAD_DESTINO_V>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			   </PSV_ENTIDAD_DESTINO_V>"
				+ "			   <COD_INTERNO_UO></COD_INTERNO_UO>"
				+ "			   <TIPO_OPRCN>" + TIPO_OPRCN + "</TIPO_OPRCN>"
				+ "			   <FECHA_VALOR>" + FECHA_CNTBL + "</FECHA_VALOR>"
				+ "			   <IMP_NOMINAL>" + IMP_NOMINAL + "</IMP_NOMINAL>"
				+ "			   <PSV_TP_CLOP_V>"
				+ "			    <COD_CLOP_SIST>" + CLOP + "</COD_CLOP_SIST>"
				+ "			    <TIPO_SBCLOP>" + TIPO_SBCLOP + "</TIPO_SBCLOP>"
				+ "			   </PSV_TP_CLOP_V>"
				+ "			   <IND_EMITE_DETALLE></IND_EMITE_DETALLE>"
				+ "			   <TEXTO_REMITENTE>" + TEXTO_REMITENTE + "</TEXTO_REMITENTE>"
				+ "			   <CP_CHQ_PAG_PROPIO_P>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			    <COD_CJ_CHQ_PG></COD_CJ_CHQ_PG>"
				+ "			    <NUM_CHQ_PAG_CP></NUM_CHQ_PAG_CP>"
				+ "			    <COD_CENT_UO></COD_CENT_UO>"
				+ "			    <NUM_SEC_AC></NUM_SEC_AC>"
				+ "			   </CP_CHQ_PAG_PROPIO_P>"
				+ "			  <PSV_ERROR_V_OCCUR>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			  </PSV_ERROR_V_OCCUR>"
				+ "			   <PSV_IMP_AUT_V>"
				+ "			    <IMP_AUT></IMP_AUT>"
				+ "			   </PSV_IMP_AUT_V>"
				+ "			   <PSV_ANULACION_V>"
				+ "			    <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			    <FECHA_OPRCN></FECHA_OPRCN>"
				+ "			    <HORA_OPRCN></HORA_OPRCN>"
				+ "			   </PSV_ANULACION_V>"
				+ "			   <IND_PAG_PARCIAL_CP></IND_PAG_PARCIAL_CP>"
				+ "			   <NUM_TARJETA></NUM_TARJETA>"
				+ "			   <PSV_COD_NUMRCO_MONEDA_V>"
				+ "			    <COD_NUMRCO_MONEDA>" + COD_NUMRCO_MONEDA + "</COD_NUMRCO_MONEDA>"
				+ "			   </PSV_COD_NUMRCO_MONEDA_V>"
				+ "			   <WD_B_NUM_COL_V>"
				+ "			    <MI_NUM_COL></MI_NUM_COL>"
				+ "			   </WD_B_NUM_COL_V>"
				+ "			   <INF_RECIBO_BANSEFI_V>"
				+ "			    <NOMB_PDV></NOMB_PDV>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			    <COD_PLZ_BANCARIA></COD_PLZ_BANCARIA>"
				+ "			    <NUM_SEC_AC></NUM_SEC_AC>"
				+ "			    <COD_DIG_CR_UO></COD_DIG_CR_UO>"
				+ "			    <IMP_INTERES_V>"
				+ "			     <STD_DEC_15Y2></STD_DEC_15Y2>"
				+ "			    </IMP_INTERES_V>"
				+ "			    <IMP_RETENCION_V>"
				+ "			     <STD_DEC_15Y2></STD_DEC_15Y2>"
				+ "			    </IMP_RETENCION_V>"
				+ "			    <NOMB_50></NOMB_50>"
				+ "			    <STD_CHAR_40></STD_CHAR_40>"
				+ "			   </INF_RECIBO_BANSEFI_V>"
				+ "			   <CODIGO_SEGURIDAD></CODIGO_SEGURIDAD>"
				+ "			  </TR_IMPUTAC_VTNLLA_PASIVO_EVT_Y>"
				+ "			  <TR_LB_VALIDAR_LB_EVT_Y>"
				+ "			   <LB_TIPO_SEL_V>"
				+ "			    <STD_CHAR_10></STD_CHAR_10>"
				+ "			   </LB_TIPO_SEL_V>"
				+ "			   <LB_LIBRETA_P>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			    <COD_CENT_UO></COD_CENT_UO>"
				+ "			    <NUM_SEC_AC></NUM_SEC_AC>"
				+ "			    <NUM_LIBRETA></NUM_LIBRETA>"
				+ "			   </LB_LIBRETA_P>"
				+ "			   <IMP_SDO></IMP_SDO>"
				+ "			   <PG_ACTLZD></PG_ACTLZD>"
				+ "			   <ULT_LIN_ACTLZN></ULT_LIN_ACTLZN>"
				+ "			   <IP_OPCION_V>"
				+ "			    <OPCION></OPCION>"
				+ "			   </IP_OPCION_V>"
				+ "			   <COD_NUMRCO_MONEDA></COD_NUMRCO_MONEDA>"
				+ "			  </TR_LB_VALIDAR_LB_EVT_Y>"
				+ "			 </TR_IMPUTAC_VTNLLA_PASIVO_TRN_I>"
				+ "			</TR_IMPUTAC_VTNLLA_PASIVO_TRN>";
		return xmlIn;
	}
	
	public String getVistaAbono(String COD_TX, String COD_APLCCN_SUBAPL,String TIPO_OPRCN, String COD_CLOP_SIST, String TIPO_SBCLOP, String COD_NUMRCO_MONEDA, 
			String COD_NRBE_EN, 
			String NUM_SEC_AC,
			String IMP_NOMINAL,
			String TEXTO_REMITENTE,
			String ID_INTERNO_TERM_TN,
			String FECHA_OPRCN,
			String HORA_OPRCN,
			String FECHA_CNTBL)
	{
		String xmlIn = "<TR_IMPUT_VTNLLA_PASIVO_2_TRN>"
				+ "			 <TR_IMPUT_VTNLLA_PASIVO_2_TRN_I>"
				+ "			  <STD_TRN_I_PARM_V>"
				+ "			   <ID_INTERNO_TERM_TN>" + ID_INTERNO_TERM_TN + "</ID_INTERNO_TERM_TN>"
				+ "			   <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			   <NUM_SEC></NUM_SEC>"
				+ "			   <COD_TX>" + COD_TX + "</COD_TX>"
				+ "			   <COD_TX_DI></COD_TX_DI>"
				+ "			  </STD_TRN_I_PARM_V>"
				+ "			  <STD_AUTORIZA_V>"
				+ "			   <IND_BORRADO_AR></IND_BORRADO_AR>"
				+ "			   <DESCR_TX></DESCR_TX>"
				+ "			   <IND_AUT_SOLIC></IND_AUT_SOLIC>"
				+ "			   <IND_ATRIB_MANC_EP></IND_ATRIB_MANC_EP>"
				+ "			   <COD_ESTADO_AR></COD_ESTADO_AR>"
				+ "			   <ID_EMPL_SOL_AUT></ID_EMPL_SOL_AUT>"
				+ "			   <IND_VERIF_ATRIB></IND_VERIF_ATRIB>"
				+ "			   <IND_URG_AR></IND_URG_AR>"
				+ "			   <MOTIVO_ACCION_AUT></MOTIVO_ACCION_AUT>"
				+ "			   <IND_ESCALABLE></IND_ESCALABLE>"
				+ "			   <IMP_AUT>0</IMP_AUT>"
				+ "			   <IMPORTE_AR>0</IMPORTE_AR>"
				+ "			   <AR_AUT_REMOTA_P>"
				+ "			    <COD_NRBE_EN>" + COD_NRBE_EN + "</COD_NRBE_EN>"
				+ "			    <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			    <FECHA_OPRCN>" + FECHA_CNTBL + "</FECHA_OPRCN>"
				+ "			    <HORA_OPRCN>" + HORA_OPRCN + "</HORA_OPRCN>"
				+ "			   </AR_AUT_REMOTA_P>"
				+ "			  <AR_TRN_MSJ_PARM_V_OCCUR>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			    <AR_TRN_MSJ_PARM_V>"
				+ "			     <TEXT_CODE></TEXT_CODE>"
				+ "			     <TEXT_ARG1></TEXT_ARG1>"
				+ "			    </AR_TRN_MSJ_PARM_V>"
				+ "			  </AR_TRN_MSJ_PARM_V_OCCUR>"
				+ "			  <STD_TARGET_TERMINAL_V_OCCUR>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			    <STD_TARGET_TERMINAL_V>"
				+ "			     <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			     <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			     <COD_ECV_SESION></COD_ECV_SESION>"
				+ "			    </STD_TARGET_TERMINAL_V>"
				+ "			  </STD_TARGET_TERMINAL_V_OCCUR>"
				+ "			   <AR_ID_SALTADO_V>"
				+ "			    <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "			   </AR_ID_SALTADO_V>"
				+ "			  </STD_AUTORIZA_V>"
				+ "			  <PSV_LIBRETA_V>"
				+ "			   <STD_CHAR_01></STD_CHAR_01>"
				+ "			  </PSV_LIBRETA_V>"
				+ "			  <PSV_INTERVENCION_V>"
				+ "			   <STD_CHAR_01></STD_CHAR_01>"
				+ "			  </PSV_INTERVENCION_V>"
				+ "			  <TR_IMPUTAC_VTNLLA_PASIVO_EVT_Y>"
				+ "			   <AC_AC_P>"
				+ "			    <COD_NRBE_EN>" + COD_NRBE_EN + "</COD_NRBE_EN>"
				+ "			    <COD_CENT_UO></COD_CENT_UO>"
				+ "			    <NUM_SEC_AC>" + NUM_SEC_AC + "</NUM_SEC_AC>"
				+ "			   </AC_AC_P>"
				+ "			   <COD_APLCCN_SUBAPL>" + COD_APLCCN_SUBAPL + "</COD_APLCCN_SUBAPL>"
				+ "			   <PSV_ENTIDAD_DESTINO_V>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			   </PSV_ENTIDAD_DESTINO_V>"
				+ "			   <COD_INTERNO_UO></COD_INTERNO_UO>"
				+ "			   <TIPO_OPRCN>" + TIPO_OPRCN + "</TIPO_OPRCN>"
				+ "			   <FECHA_VALOR>" + FECHA_CNTBL + "</FECHA_VALOR>"
				+ "			   <IMP_NOMINAL>" + IMP_NOMINAL + "</IMP_NOMINAL>"
				+ "			   <PSV_TP_CLOP_V>"
				+ "			    <COD_CLOP_SIST>" + COD_CLOP_SIST + "</COD_CLOP_SIST>"
				+ "			    <TIPO_SBCLOP>" + TIPO_SBCLOP + "</TIPO_SBCLOP>"
				+ "			   </PSV_TP_CLOP_V>"
				+ "			   <IND_EMITE_DETALLE></IND_EMITE_DETALLE>"
				+ "			   <TEXTO_REMITENTE>" + TEXTO_REMITENTE + "</TEXTO_REMITENTE>"
				+ "			   <CP_CHQ_PAG_PROPIO_P>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			    <COD_CJ_CHQ_PG></COD_CJ_CHQ_PG>"
				+ "			    <NUM_CHQ_PAG_CP></NUM_CHQ_PAG_CP>"
				+ "			    <COD_CENT_UO></COD_CENT_UO>"
				+ "			    <NUM_SEC_AC></NUM_SEC_AC>"
				+ "			   </CP_CHQ_PAG_PROPIO_P>"
				+ "			  <PSV_ERROR_V_OCCUR>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			    <PSV_ERROR_V>"
				+ "			     <COD_ERROR></COD_ERROR>"
				+ "			     <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "			     <ATRIBUIDO_S_N></ATRIBUIDO_S_N>"
				+ "			    </PSV_ERROR_V>"
				+ "			  </PSV_ERROR_V_OCCUR>"
				+ "			   <PSV_IMP_AUT_V>"
				+ "			    <IMP_AUT></IMP_AUT>"
				+ "			   </PSV_IMP_AUT_V>"
				+ "			   <PSV_ANULACION_V>"
				+ "			    <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "			    <FECHA_OPRCN></FECHA_OPRCN>"
				+ "			    <HORA_OPRCN></HORA_OPRCN>"
				+ "			   </PSV_ANULACION_V>"
				+ "			   <IND_PAG_PARCIAL_CP></IND_PAG_PARCIAL_CP>"
				+ "			   <NUM_TARJETA></NUM_TARJETA>"
				+ "			   <PSV_COD_NUMRCO_MONEDA_V>"
				+ "			    <COD_NUMRCO_MONEDA>" + COD_NUMRCO_MONEDA + "</COD_NUMRCO_MONEDA>"
				+ "			   </PSV_COD_NUMRCO_MONEDA_V>"
				+ "			   <WD_B_NUM_COL_V>"
				+ "			    <MI_NUM_COL></MI_NUM_COL>"
				+ "			   </WD_B_NUM_COL_V>"
				+ "			   <INF_RECIBO_BANSEFI_V>"
				+ "			    <NOMB_PDV></NOMB_PDV>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			    <COD_PLZ_BANCARIA></COD_PLZ_BANCARIA>"
				+ "			    <NUM_SEC_AC></NUM_SEC_AC>"
				+ "			    <COD_DIG_CR_UO></COD_DIG_CR_UO>"
				+ "			    <IMP_INTERES_V>"
				+ "			     <STD_DEC_15Y2></STD_DEC_15Y2>"
				+ "			    </IMP_INTERES_V>"
				+ "			    <IMP_RETENCION_V>"
				+ "			     <STD_DEC_15Y2></STD_DEC_15Y2>"
				+ "			    </IMP_RETENCION_V>"
				+ "			    <NOMB_50></NOMB_50>"
				+ "			    <STD_CHAR_40></STD_CHAR_40>"
				+ "			   </INF_RECIBO_BANSEFI_V>"
				+ "			   <CODIGO_SEGURIDAD></CODIGO_SEGURIDAD>"
				+ "			  </TR_IMPUTAC_VTNLLA_PASIVO_EVT_Y>"
				+ "			  <TR_LB_VALIDAR_LB_EVT_Y>"
				+ "			   <LB_TIPO_SEL_V>"
				+ "			    <STD_CHAR_10></STD_CHAR_10>"
				+ "			   </LB_TIPO_SEL_V>"
				+ "			   <LB_LIBRETA_P>"
				+ "			    <COD_NRBE_EN></COD_NRBE_EN>"
				+ "			    <COD_CENT_UO></COD_CENT_UO>"
				+ "			    <NUM_SEC_AC></NUM_SEC_AC>"
				+ "			    <NUM_LIBRETA></NUM_LIBRETA>"
				+ "			   </LB_LIBRETA_P>"
				+ "			   <IMP_SDO></IMP_SDO>"
				+ "			   <PG_ACTLZD></PG_ACTLZD>"
				+ "			   <ULT_LIN_ACTLZN></ULT_LIN_ACTLZN>"
				+ "			   <IP_OPCION_V>"
				+ "			    <OPCION></OPCION>"
				+ "			   </IP_OPCION_V>"
				+ "			   <COD_NUMRCO_MONEDA></COD_NUMRCO_MONEDA>"
				+ "			  </TR_LB_VALIDAR_LB_EVT_Y>"
				+ "					  <PSV_NOMB_ORDENANTE_V>"
				+ "					   <NOMB_50></NOMB_50>"
				+ "					  </PSV_NOMB_ORDENANTE_V>"
				+ "			 </TR_IMPUT_VTNLLA_PASIVO_2_TRN_I>"
				+ "			</TR_IMPUT_VTNLLA_PASIVO_2_TRN>";
		return xmlIn;
	}
	
	public String getVistaFechaContable(String terminal){
		String vista = "";
		vista =   "<STD_TRN_VAL_SRV>"
				+ "	<STD_TRN_VAL_SRV_I>"
				+ "  <STD_TRN_I_PARM_V>"
				+ "   <ID_INTERNO_TERM_TN>" + terminal + "</ID_INTERNO_TERM_TN>"
				+ "   <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "   <NUM_SEC></NUM_SEC>"
				+ "   <COD_TX>GCI34COU</COD_TX>"
				+ "   <COD_TX_DI></COD_TX_DI>"
				+ "  </STD_TRN_I_PARM_V>"
				+ " </STD_TRN_VAL_SRV_I>"
				+ "</STD_TRN_VAL_SRV>";
		
		return vista;
	}
	
	public String getVistaUltimasTransacciones(String COD_NRBE_EN, String NUM_SEC_AC, String FECHA_CNTBL){
		String COD_NUMRCO_MONEDA = "MXN";
		String SINO = "S";
		String vista = "";
		vista = "						<AF_APNTE_E_LST_SRV>"
				+ "						 <AF_APNTE_E_LST_SRV_I>"
				+ "						  <EVENT_CD>1</EVENT_CD>"
				+ "						  <IDFR_CT_CTA_V>"
				+ "						   <COD_NRBE_EN>" + COD_NRBE_EN + "</COD_NRBE_EN>"
				+ "						   <COD_CENT_UO></COD_CENT_UO>"
				+ "						   <NUM_SEC_AC>" + NUM_SEC_AC + "</NUM_SEC_AC>"
				+ "						   <PRPDAD_CTA>A</PRPDAD_CTA>"
				+ "						   <NUM_SUBAC></NUM_SUBAC>"
				+ "						   <ID_EXP_RECLAM></ID_EXP_RECLAM>"
				+ "						   <COD_CTA>01</COD_CTA>"
				+ "						   <COD_NUMRCO_MONEDA>" + COD_NUMRCO_MONEDA + "</COD_NUMRCO_MONEDA>"
				+ "						  </IDFR_CT_CTA_V>"
				+ "						  <STD_INTERVALO_V>"
				+ "						   <FECHA_DESDE>" + FECHA_CNTBL + "</FECHA_DESDE>"
				+ "						   <FECHA_HASTA>" + FECHA_CNTBL + "</FECHA_HASTA>"
				+ "						  </STD_INTERVALO_V>"
				+ "						  <ORIGEN_INTERVALO_V>"
				+ "						   <COD_ORIGEN_1></COD_ORIGEN_1>"
				+ "						   <COD_ORIGEN_2></COD_ORIGEN_2>"
				+ "						  </ORIGEN_INTERVALO_V>"
				+ "						 <AF_APNTE_IND_V_OCCUR>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>0</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>0</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>0</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>" + SINO + "</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>" + SINO + "</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>" + SINO + "</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>0</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>0</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND>0</COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_V>"
				+ "						   <AF_APNTE_IND_DOM_V_OCCUR>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "							 <AF_APNTE_IND_DOM_V>"
				+ "							  <COD_VALOR_DOM_IND></COD_VALOR_DOM_IND>"
				+ "							 </AF_APNTE_IND_DOM_V>"
				+ "						   </AF_APNTE_IND_DOM_V_OCCUR>"
				+ "						   </AF_APNTE_IND_V>"
				+ "						 </AF_APNTE_IND_V_OCCUR>"
				+ "						  <NUM_SEC_V>"
				+ "						   <NUM_SEC></NUM_SEC>"
				+ "						  </NUM_SEC_V>"
				+ "						  <LINEA_GRUPO_V>"
				+ "						   <COD_LINEA>03</COD_LINEA>"
				+ "						   <ID_GRP_PD>11</ID_GRP_PD>"
				+ "						  </LINEA_GRUPO_V>"
				+ "						  <STD_LST_PARM_V>"
				+ "						   <OPERACION_CONTINUA></OPERACION_CONTINUA>"
				+ "						   <COUNT_REQ_IN>50</COUNT_REQ_IN>"
				+ "						   <START_REC_NR>0</START_REC_NR>"
				+ "						   <OCCURS_NR>50</OCCURS_NR>"
				+ "						   <FIRST_READ_IN>1</FIRST_READ_IN>"
				+ "						   <SOLO_COUNT_REQ_IN>50</SOLO_COUNT_REQ_IN>"
				+ "						  </STD_LST_PARM_V>"
				+ "						  <STD_APPL_PARM_V>"
				+ "						   <COD_NRBE_EN></COD_NRBE_EN>"
				+ "						   <COD_NRBE_EN_FSC></COD_NRBE_EN_FSC>"
				+ "						   <COD_INTERNO_UO></COD_INTERNO_UO>"
				+ "						   <COD_INTERNO_UO_FSC></COD_INTERNO_UO_FSC>"
				+ "						   <COD_CSB_OF></COD_CSB_OF>"
				+ "						   <ID_INTERNO_TERM_TN></ID_INTERNO_TERM_TN>"
				+ "						   <ID_INTERNO_EMPL_EP></ID_INTERNO_EMPL_EP>"
				+ "						   <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "						   <NUM_SEC></NUM_SEC>"
				+ "						   <COD_TX></COD_TX>"
				+ "						   <COD_TX_DI></COD_TX_DI>"
				+ "						   <FECHA_OPRCN></FECHA_OPRCN>"
				+ "						   <HORA_OPRCN></HORA_OPRCN>"
				+ "						   <FECHA_CTBLE></FECHA_CTBLE>"
				+ "						   <COD_IDIOMA></COD_IDIOMA>"
				+ "						   <COD_ISO></COD_ISO>"
				+ "						   <COD_ISO_PAIS_AG></COD_ISO_PAIS_AG>"
				+ "						   <BUFFER></BUFFER>"
				+ "						  </STD_APPL_PARM_V>"
				+ "						 </AF_APNTE_E_LST_SRV_I>"
				+ "						</AF_APNTE_E_LST_SRV>";
		
		return vista;
	}
	
	public String getVistaConsultaClabe(String NUM_SEC_AC, String COD_NRBE_EN, String ID_INTERNO_TERM_TN){
		String vista = "";
		String COD_TX = "STR76OON";
		vista =   "<TR_CONSULTA_CLABE_TRN>"
				+ " <TR_CONSULTA_CLABE_TRN_I>"
				+ "  <TR_CONSULTA_CLABE_EVT_Y>"
				+ "   <AC_AC_P>"
				+ "    <COD_NRBE_EN>" + COD_NRBE_EN + "</COD_NRBE_EN>"
				+ "    <COD_CENT_UO></COD_CENT_UO>"
				+ "    <NUM_SEC_AC>" + NUM_SEC_AC + "</NUM_SEC_AC>"
				+ "   </AC_AC_P>"
				+ "  </TR_CONSULTA_CLABE_EVT_Y>"
				+ "  <STD_TRN_I_PARM_V>"
				+ "   <ID_INTERNO_TERM_TN>" + ID_INTERNO_TERM_TN + "</ID_INTERNO_TERM_TN>"
				+ "   <ID_EMPL_AUT></ID_EMPL_AUT>"
				+ "   <NUM_SEC></NUM_SEC>"
				+ "   <COD_TX>" + COD_TX + "</COD_TX>"
				+ "   <COD_TX_DI></COD_TX_DI>"
				+ "  </STD_TRN_I_PARM_V>"
				+ " </TR_CONSULTA_CLABE_TRN_I>"
				+ "</TR_CONSULTA_CLABE_TRN>";
		
		return vista;
	}
	
	/*Begin E234 03-07*/	
	public ResponseDatosEmpleado ConsultaEmpleado(String entidad, String empleado, String terminal)
	{
		ResponseDatosEmpleado oResponEmp = new ResponseDatosEmpleado();
		String vista = "";
		String salida="";
		
		try
		{
			vista =getVistaConsultaEmplead(entidad,empleado,terminal);
			TcbProperties prop = new TcbProperties();
			String StrUrl=prop.getURL_CONS_SUCURSALES();
							salida =SalidaResponse(vista,StrUrl);
							
							DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
							DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
							NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_CONS_EMPL_TRN_O");
							System.out.println(vista);
							System.out.println(salida);
							String RTRN_CD = "";
							for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ ){
								Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
								Element eElement = (Element) item;
								RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
							}
							
							if(RTRN_CD.equals("1"))
							{
								Element PSV_NOMBRE_TITULAR_V = (Element)doc.getElementsByTagName("TR_CONS_EMPL_EVT_Z").item(0);
								String NOMBRE_EMPL = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NOMB_50").item(0).getTextContent();
								String ID_INTERNO_PE = PSV_NOMBRE_TITULAR_V.getElementsByTagName("ID_INTERNO_PE").item(0).getTextContent();
								String CARGO = PSV_NOMBRE_TITULAR_V.getElementsByTagName("COD_CARGO_EMPL").item(0).getTextContent();
								String fecha = PSV_NOMBRE_TITULAR_V.getElementsByTagName("FECHA_ALTA_PER").item(0).getTextContent();
								String FECHA_NCTO = PSV_NOMBRE_TITULAR_V.getElementsByTagName("FEC_NCTO_CONST_PE").item(0).getTextContent();
								
								
								oResponEmp.setNOMBRE(NOMBRE_EMPL);
								oResponEmp.setCARGO(CARGO);
								oResponEmp.setFECHA_ALTA(fecha);
								oResponEmp.setFECHA_NCTO(FECHA_NCTO);
								oResponEmp.setID_INTERNO_PE(ID_INTERNO_PE);
								oResponEmp.setStatus(1);
							} else 
							{
								oResponEmp.setStatus(-1);
								log.info("PasivoTcb - respServConsultaEmpleado : View In .- " + vista);
								log.info("PasivoTcb - respServConsultaEmpleado : View Out .- " + salida);
								String errores = "";
								NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
								for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ ){
									Node item = STD_MSJ_PARM_V.item(i);
									Element eElement = (Element) item;
									String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
									String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
									errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
								}
								oResponEmp.setDescripcion(errores);
							}
							
						}
		catch (Exception e) 
		{
			oResponEmp.setStatus(-1);
			oResponEmp.setDescripcion(e.getMessage());
							System.out.println(e.getMessage());
							log.info("PasivoTcb - respServConsultaEmpleado : View In .- " + vista);
							log.error("PasivoTcb - respServConsultaEmpleado : Exception Read Out. " + e.getMessage());
		}
			/*End*/
		return oResponEmp;
	}	

	public ResponseDatosCentro ObtenDomicilio(String ID_INTERNO_PE,String ID_DOM,String entidad,String terminal,ResponseDatosCentro oResp)
	{
		String StrReturn ="";
		try
		{
			  String vista= GetVistaDomicilio( ID_INTERNO_PE, ID_DOM, entidad, terminal);
			  TcbProperties prop = new TcbProperties();
			  String StrUrl = prop.getURL_CONS_DOMIC();
			  
			  StrReturn =SalidaResponse(vista,StrUrl);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(StrReturn.getBytes("utf-8")));
				
				NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_PE_CONS_DOMIC_TRN");
				System.out.println(vista);
				
				String RTRN_CD = "";
				for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ )
				{
					Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
					Element eElement = (Element) item;
					RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
				}
				
				if(RTRN_CD.equals("1"))
				{					
					Element PSV_NOMBRE_TITULAR_V = (Element)doc.getElementsByTagName("TR_PE_CONS_DOMIC_EVT_Z").item(0);
					
					String CodPost = PSV_NOMBRE_TITULAR_V.getElementsByTagName("COD_POSTAL_AG").item(0).getTextContent();
					oResp.setCP(CodPost);
							
		
		NodeList TR_IMPUTAC_DOMICIL = doc.getElementsByTagName("COMP_DOMIC_V");
		String TypDoc="";
		String sCalle="";
		String sNumExt="";
		String sNumInt="";
		String sColonia="";
		for(int i = 0 ; i < TR_IMPUTAC_DOMICIL.getLength()  ; i++ )
		{
			Node item = TR_IMPUTAC_DOMICIL.item(i);
			Element eElement = (Element) item;
			TypDoc = eElement.getElementsByTagName("COD_COMP_DOMIC").item(0).getTextContent();
			
			switch(TypDoc)
			{
	            case "01": //calle
	            	sCalle =  eElement.getElementsByTagName("VAL_COMP_DOMIC_DR").item(0).getTextContent();
	               break;
	            case "02": // num exterior
	            	sNumExt = eElement.getElementsByTagName("VAL_COMP_DOMIC_DR").item(0).getTextContent();
	               break;
	            case "05": // num imterior
	            	sNumInt = eElement.getElementsByTagName("VAL_COMP_DOMIC_DR").item(0).getTextContent();
	               break;
	            case "10": // colonia
	            	sColonia = eElement.getElementsByTagName("VAL_COMP_DOMIC_DR").item(0).getTextContent();
	               break;
	            default:
	               break;
          }
		}
		oResp.setCOLONIA(sColonia);
		oResp.setCALLE(sCalle);
		oResp.setNUMEROEXT(sNumExt);
		oResp.setNUMEROINT(sNumInt);
				}
				else 
				{
					log.info("PasivoTcb - respSerDomicilio : View In .- " + vista);
					String errores = "";
					NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
					for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
					{
						Node item = STD_MSJ_PARM_V.item(i);
						Element eElement = (Element) item;
						String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
						String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
						errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
						
					}
					oResp.setDescripcion(errores);
					oResp.setStatus(-1);
				}
		}
		catch(Exception ex)
		{
			oResp.setStatus(-1);
			oResp.setDescripcion(ex.getMessage());
		}
		return oResp;
	}
	
	public ResponseDatosCentro  ConsultaCentro(String entidad, String centro, String terminal)
	{
		ResponseDatosCentro oEnSucDet = new ResponseDatosCentro();
		String vista = "";				
		try
		{
			String salida="";
				vista =getVistaConsultaCentro(entidad,centro,terminal);
				TcbProperties prop = new TcbProperties();
				String Strurl =prop.getURL_CONS_SUCURSALES();
				salida =SalidaResponse(vista,Strurl);
							
							DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
							DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
							
							NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_CONS_CENTRO_TRN_O");
							System.out.println(vista);
							System.out.println(salida);
							String RTRN_CD = "";
							for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ )
							{
								Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
								Element eElement = (Element) item;
								RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
							}
							
							if(RTRN_CD.equals("1"))
							{	
								oEnSucDet.setStatus(1);	
								
								Element PSV_NOMBRE_TITULAR_V = (Element)doc.getElementsByTagName("TR_CONS_CENTRO_EVT_Z").item(0);
								String NOMBRE_Centro = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NOMB_CENT_UO").item(0).getTextContent();
								String FECHA_ALTA = PSV_NOMBRE_TITULAR_V.getElementsByTagName("FECHA_ALTA_UO").item(0).getTextContent();
								String ID_INTERNO_PE = PSV_NOMBRE_TITULAR_V.getElementsByTagName("ID_INTERNO_PE").item(0).getTextContent();
								String ID_DOM = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NUM_DIR_PRAL").item(0).getTextContent();
								oEnSucDet.setNOMBRE(NOMBRE_Centro);
								oEnSucDet.setFECHA_ALTA(FECHA_ALTA);
								oEnSucDet = ObtenDomicilio(ID_INTERNO_PE,ID_DOM,entidad,terminal,oEnSucDet);
								
							} 
							else 
							{
								oEnSucDet.setStatus(-1);	
								log.info("PasivoTcb - respServConsultaCentro : View In .- " + vista);
								log.info("PasivoTcb - respServConsultaCentro : View Out .- " + salida);
								String errores = "";
								NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
								for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
								{
									Node item = STD_MSJ_PARM_V.item(i);
									Element eElement = (Element) item;
									String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
									String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
									errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
								}
								oEnSucDet.setDescripcion(errores);
							}
						}
		catch (Exception e) 
		{
				oEnSucDet.setStatus(-1);
				oEnSucDet.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - respServConsultaCentro : View In .- " + vista);
				log.error("PasivoTcb - respServConsultaCentro : Exception Read Out. " + e.getMessage());
		}
			/*End*/
		return oEnSucDet;
	}
	
	
	
	public ResponsePersona ConsultaPersonaXIdInterno(String entidad, String idPersona, String terminal)
	{
		ResponsePersona oEnSucDet = new ResponsePersona();
		String vista = "";
				
		try
		{
			String salida="";
				vista =GetVistaBusquedaPersonaXIdInterno(entidad,idPersona,terminal);
				TcbProperties prop = new TcbProperties();
				String Strurl =prop.getURL_CONS_NOMBRE();
				salida =SalidaResponse(vista,Strurl);
							
							DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
							DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
							
							NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_CONS_MINIMA_PERSONA_TRN_O");
							
							String RTRN_CD = "";
							for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ )
							{
								Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
								Element eElement = (Element) item;
								RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
							}
							
							if(RTRN_CD.equals("1"))
							{
								oEnSucDet.setStatus(1);	
								Element PSV_NOMBRE_TITULAR_V = (Element)doc.getElementsByTagName("TR_CONS_MINIMA_PERSONA_EVT_Z").item(0);
								String IdInternoPe = PSV_NOMBRE_TITULAR_V.getElementsByTagName("ID_INTERNO_PE").item(0).getTextContent();
								String ApPaterno = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NOMB_IN_APE_1_IN").item(0).getTextContent();
								String ApMaterno = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NOMB_IN_APE_2_IN").item(0).getTextContent();
								String nombre = PSV_NOMBRE_TITULAR_V.getElementsByTagName("NOMB_IN_NOMB_PILA").item(0).getTextContent();
								oEnSucDet.setApMaterno(ApMaterno);
								oEnSucDet.setApPaterno(ApPaterno);
								oEnSucDet.setNombre(nombre);
								oEnSucDet.setIdInternoPe(IdInternoPe);								
							} 
							else 
							{
								oEnSucDet.setStatus(-1);	
								log.info("PasivoTcb - respServConsultaCentro : View In .- " + vista);
								log.info("PasivoTcb - respServConsultaCentro : View Out .- " + salida);
								String errores = "";
								NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
								for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
								{
									Node item = STD_MSJ_PARM_V.item(i);
									Element eElement = (Element) item;
									String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
									String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
									errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
								}
								oEnSucDet.setDescripcion(errores);
							}
						}
		catch (Exception e) 
		{
				oEnSucDet.setStatus(-1);
				oEnSucDet.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - respServConsultaCentro : View In .- " + vista);
				log.error("PasivoTcb - respServConsultaCentro : Exception Read Out. " + e.getMessage());
		}
			/*End*/
		return oEnSucDet;
	}
	
	public ResponsePersona ConsultaAcuerdo(String entidad, String acuerdo, String terminal)
	{
		ResponsePersona oEnSucDet = new ResponsePersona();
		String vista = "";
		try
		{
			String salida="";
				vista =GetVistaBusquedaAcuerdo(entidad,acuerdo,terminal);
				TcbProperties prop = new TcbProperties();
				String Strurl =prop.getURL_CONS_ACUERDO();
				salida =SalidaResponse(vista,Strurl);
							
							DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
							DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(new ByteArrayInputStream(salida.getBytes("utf-8")));
							
							NodeList TR_IMPUTAC_VTNLLA_PASIVO_TRN_O = doc.getElementsByTagName("TR_CONSULTA_ACUERDO_TRN_O");
							System.out.println(vista);
							System.out.println(salida);
							String RTRN_CD = "";
							for(int i = 0 ; i < TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.getLength()  ; i++ ){
								Node item = TR_IMPUTAC_VTNLLA_PASIVO_TRN_O.item(i);
								Element eElement = (Element) item;
								RTRN_CD = eElement.getElementsByTagName("RTRN_CD").item(0).getTextContent();
							}
							
							if(RTRN_CD.equals("1"))
							{		
								oEnSucDet.setStatus(1);	
								
								Element PSV_NOMBRE_TITULAR_V = (Element)doc.getElementsByTagName("PERSONA_AC_V").item(0);
								String IdInternoPe = PSV_NOMBRE_TITULAR_V.getElementsByTagName("ID_INTERNO_PE").item(0).getTextContent();								
								
								oEnSucDet.setIdInternoPe(IdInternoPe);	
							} 
							else 
							{
								oEnSucDet.setStatus(-1);	
								log.info("PasivoTcb - respServConsultaCentro : View In .- " + vista);
								log.info("PasivoTcb - respServConsultaCentro : View Out .- " + salida);
								String errores = "";
								NodeList STD_MSJ_PARM_V = doc.getElementsByTagName("STD_TRN_MSJ_PARM_V");
								for(int i = 0 ; i < STD_MSJ_PARM_V.getLength()  ; i++ )
								{
									Node item = STD_MSJ_PARM_V.item(i);
									Element eElement = (Element) item;
									String TEXT_CODE = eElement.getElementsByTagName("TEXT_CODE").item(0).getTextContent();
									String TEXT_ARG1 = eElement.getElementsByTagName("TEXT_ARG1").item(0).getTextContent();
									errores += TEXT_CODE + "|" + TEXT_ARG1 + ", ";
								}
								oEnSucDet.setDescripcion(errores);
							}
						}
		catch (Exception e) 
		{
				oEnSucDet.setStatus(-1);
				oEnSucDet.setDescripcion(e.getMessage());
				System.out.println(e.getMessage());
				log.info("PasivoTcb - respServConsultaCentro : View In .- " + vista);
				log.error("PasivoTcb - respServConsultaCentro : Exception Read Out. " + e.getMessage());
		}
			/*End*/
		return oEnSucDet;
	}
		
	public String SalidaResponse(String vista,String StrUrl)
	{
		String salida="";
		try
		{
			String soapXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>"
					+ "	<SOAP-ENV:Body> " + vista+ "	</SOAP-ENV:Body>"
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
					log.info("PasivoTcb - SalidaResponse : View In .- " + vista);
					log.info("PasivoTcb - SalidaResponse : URL_CARGO .- " + StrUrl);
					log.error("PasivoTcb - SalidaResponse : IOException. " + e.getMessage());
				}
			} 
			catch (MalformedURLException e1) 
			{
				log.info("PasivoTcb - SalidaResponse : View In .- " + vista);
				log.info("PasivoTcb - SalidaResponse : URL_CARGO .- " + StrUrl);
				log.error("PasivoTcb - SalidaResponse : MalformedURLException. " + e1.getMessage());
			}
			conn.setRequestProperty("SOAPAction", StrUrl);
			conn.setDoOutput(true);
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
				log.info("PasivoTcb - SalidaResponse : View In .- " + vista);
				log.error("PasivoTcb - SalidaResponse : OutputStreamWriter. " + e2.getMessage());
			}

			/*Begin */
			// Read the response
						java.io.BufferedReader rd = null;
						try 
						{
							rd = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
						} 
						catch (IOException e1) 
						{
							log.info("PasivoTcb - SalidaResponse : View In .- " + vista);
							log.error("PasivoTcb - SalidaResponse : BufferedReader. " + e1.getMessage());
						}
						try
						{
							String line = "";
							//LECTURA DE VISTA DE SALIDA
							while ((line = rd.readLine()) != null) 
							{ 				
								salida += line;	
							}
							
							salida = salida.replaceAll("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">", "");
							salida = salida.replaceAll("<SOAP-ENV:Body>", "");
							salida = salida.replaceAll("</SOAP-ENV:Body>", "");
							salida = salida.replaceAll("</SOAP-ENV:Envelope>", "");
							salida = salida.trim();
						}
						catch(Exception ex)
						{
							log.error("PasivoTcb - Cargo : BufferedReader. " + ex.getMessage());
						}
		}
		catch(Exception ex)
		{
			log.error("PasivoTcb - Cargo : BufferedReader. " + ex.getMessage());
		}
		return salida;
	} 

	public String getVistaConsultaCentro(String entidad,String centro,String terminal)
	{
		String vista="";
		StringBuilder Bvista = new StringBuilder();
		String COD_TX = "GCA19CON";
		Bvista.append("<TR_CONS_CENTRO_TRN>");
		Bvista.append("<TR_CONS_CENTRO_TRN_I>");
		Bvista.append("<TR_CONS_CENTRO_EVT_Y>");
		Bvista.append("<UO_CENTRO_P>");
		Bvista.append("<COD_NRBE_EN>"+ entidad+"</COD_NRBE_EN>");
		Bvista.append("<COD_INTERNO_UO>"+ centro+"</COD_INTERNO_UO>");
		Bvista.append("</UO_CENTRO_P>");
		Bvista.append("<PY_PARAM_VVV_P>");
		Bvista.append("<COD_NRBE_EN>"+ entidad+"</COD_NRBE_EN>");
		Bvista.append("<COD_INTERNO_UO>"+ centro+"</COD_INTERNO_UO>");
		Bvista.append("</PY_PARAM_VVV_P>");
		Bvista.append("</TR_CONS_CENTRO_EVT_Y>");
		Bvista.append("<STD_TRN_I_PARM_V>");
		Bvista.append("<ID_INTERNO_TERM_TN>"+ terminal+"</ID_INTERNO_TERM_TN>");
		Bvista.append("<ID_EMPL_AUT></ID_EMPL_AUT>");
		Bvista.append("<NUM_SEC></NUM_SEC>");
		Bvista.append("<COD_TX>"+ COD_TX + "</COD_TX>");
		Bvista.append("<COD_TX_DI></COD_TX_DI>");
		Bvista.append("</STD_TRN_I_PARM_V>");
		Bvista.append("</TR_CONS_CENTRO_TRN_I>");
		Bvista.append("</TR_CONS_CENTRO_TRN>");
		vista =Bvista.toString();
		return vista;
	}
	
	public String getVistaConsultaEmplead(String entidad, String empleado, String terminal)
	{
		String vista = "";
		String COD_TX = "GCA19CON";
		StringBuilder Bvista = new StringBuilder();
		Bvista.append("<TR_CONS_EMPL_TRN>");
		Bvista.append("<TR_CONS_EMPL_TRN_I>");
		Bvista.append("<TR_CONS_EMPL_EVT_Y>");
		Bvista.append("<CLAVE_EMPLEADO_V>");
		Bvista.append("<COD_NRBE_EN>"+ entidad + "</COD_NRBE_EN>");
		Bvista.append("<ID_INTERNO_PE></ID_INTERNO_PE>");
		Bvista.append("<ID_INTERNO_EMPL_EP>"+empleado+ "</ID_INTERNO_EMPL_EP>");
		Bvista.append("</CLAVE_EMPLEADO_V>");
		Bvista.append("</TR_CONS_EMPL_EVT_Y>");
		Bvista.append("<STD_TRN_I_PARM_V>");
		Bvista.append("<ID_INTERNO_TERM_TN>"+ terminal + "</ID_INTERNO_TERM_TN>");
		Bvista.append("<ID_EMPL_AUT></ID_EMPL_AUT>");
		Bvista.append("<NUM_SEC></NUM_SEC>");
		Bvista.append("<COD_TX>"+COD_TX+ "</COD_TX>");
		Bvista.append("<COD_TX_DI></COD_TX_DI>");
		Bvista.append("</STD_TRN_I_PARM_V>");
		Bvista.append("</TR_CONS_EMPL_TRN_I>");
		Bvista.append("</TR_CONS_EMPL_TRN>");
		vista =Bvista.toString();
		
		return vista;
	}
	
	public String GetVistaDomicilio(String idIntPe,String idDom,String entidad,String terminal)
	{
		String vista= "";
		StringBuilder Bvista = new StringBuilder();
		Bvista.append("<TR_PE_CONS_DOMIC_TRN>");
		Bvista.append("<TR_PE_CONS_DOMIC_TRN_I>");
		Bvista.append("<TR_PE_CONS_DOMIC_EVT_Y>");
		Bvista.append("<PE_PERS_P>");
		Bvista.append("<COD_NRBE_EN>"+entidad+"</COD_NRBE_EN>");
		Bvista.append("<ID_INTERNO_PE>"+idIntPe+"</ID_INTERNO_PE>");
		Bvista.append("</PE_PERS_P>");
		Bvista.append("<DR_DOMIC_P>");
		Bvista.append("<COD_NRBE_EN>"+entidad+"</COD_NRBE_EN>");
		Bvista.append("<NUM_DIR>"+idDom+"</NUM_DIR>");
		Bvista.append("</DR_DOMIC_P>");
		Bvista.append("</TR_PE_CONS_DOMIC_EVT_Y>");
		Bvista.append("<STD_TRN_I_PARM_V>");
		Bvista.append("<ID_INTERNO_TERM_TN>"+terminal+"</ID_INTERNO_TERM_TN>");
		Bvista.append("<ID_EMPL_AUT></ID_EMPL_AUT>");
		Bvista.append("<NUM_SEC></NUM_SEC>");
		Bvista.append("<COD_TX>PGE20CON</COD_TX>");
		Bvista.append("<COD_TX_DI></COD_TX_DI>");
		Bvista.append("</STD_TRN_I_PARM_V>");
		Bvista.append("</TR_PE_CONS_DOMIC_TRN_I>");
		Bvista.append("</TR_PE_CONS_DOMIC_TRN>");
	
		vista =Bvista.toString();
			return vista;
		}
	
		public String GetVistaBusquedaPersonaXIdInterno(String entidad,String idInternoP,String IdTerminal)
		{
			String vista= "";
			StringBuilder Bvista = new StringBuilder();
			Bvista.append("<TR_CONS_MINIMA_PERSONA_TRN>");
			Bvista.append("<TR_CONS_MINIMA_PERSONA_TRN_I>");
			Bvista.append("<TR_CONS_MINIMA_PERSONA_EVT_Y>");
			Bvista.append("<PE_PERS_P>");
			Bvista.append("<COD_NRBE_EN>"+entidad+"</COD_NRBE_EN>");
			Bvista.append("<ID_INTERNO_PE>"+idInternoP+"</ID_INTERNO_PE>");
			Bvista.append("</PE_PERS_P>");
			Bvista.append("</TR_CONS_MINIMA_PERSONA_EVT_Y>");
			Bvista.append("<STD_TRN_I_PARM_V>");
			Bvista.append("<ID_INTERNO_TERM_TN>"+IdTerminal+"</ID_INTERNO_TERM_TN>");
			Bvista.append("<ID_EMPL_AUT></ID_EMPL_AUT>");
			Bvista.append("<NUM_SEC></NUM_SEC>");
			Bvista.append("<COD_TX>PGE13CON</COD_TX>");
			Bvista.append("<COD_TX_DI></COD_TX_DI>");
			Bvista.append("</STD_TRN_I_PARM_V>");
			Bvista.append("</TR_CONS_MINIMA_PERSONA_TRN_I>");
			Bvista.append("</TR_CONS_MINIMA_PERSONA_TRN>");
			vista =Bvista.toString();
			return vista;
		} 

		public String GetVistaBusquedaAcuerdo(String entidad,String acuerdo,String IdTerminal)
		{
			String vista= "";
			StringBuilder Bvista = new StringBuilder();
			
			Bvista.append("<TR_CONSULTA_ACUERDO_TRN>");
			Bvista.append("<TR_CONSULTA_ACUERDO_TRN_I>");
			Bvista.append("<ELEVATOR_POSITION>0</ELEVATOR_POSITION>");
			Bvista.append("<SCROLLABLE_OCCURS>50</SCROLLABLE_OCCURS>");
			Bvista.append("<TR_CONSULTA_ACUERDO_EVT_Y>");
			Bvista.append("<AC_AC_P>");
			Bvista.append("<COD_NRBE_EN>"+entidad+"</COD_NRBE_EN>");
			Bvista.append("<COD_CENT_UO></COD_CENT_UO>");
			Bvista.append("<NUM_SEC_AC>"+acuerdo+"</NUM_SEC_AC>");
			Bvista.append("</AC_AC_P>");
			Bvista.append("<AC_IND_CONSU_AC_V>");
			Bvista.append("<STD_SMALL_INT>1</STD_SMALL_INT>");
			Bvista.append("</AC_IND_CONSU_AC_V>");
			Bvista.append("<AC_IND_PROCEDE_GESTION_V>");
			Bvista.append("<STD_SMALL_INT>0</STD_SMALL_INT>");
			Bvista.append("</AC_IND_PROCEDE_GESTION_V>");
			Bvista.append("</TR_CONSULTA_ACUERDO_EVT_Y>");
			Bvista.append("<STD_TRN_I_PARM_V>");
			Bvista.append("<ID_INTERNO_TERM_TN>"+IdTerminal+"</ID_INTERNO_TERM_TN>");
			Bvista.append("<ID_EMPL_AUT></ID_EMPL_AUT>");
			Bvista.append("<NUM_SEC></NUM_SEC>");
			Bvista.append("<COD_TX>GAC11COU</COD_TX>");
			Bvista.append("<COD_TX_DI>xxxx</COD_TX_DI>");
			Bvista.append("</STD_TRN_I_PARM_V>");
			Bvista.append("</TR_CONSULTA_ACUERDO_TRN_I>");
			Bvista.append("</TR_CONSULTA_ACUERDO_TRN>");
			vista =Bvista.toString();
			return vista;
		} 
}
