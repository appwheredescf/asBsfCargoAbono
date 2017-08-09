package com.bansefi.nss.cargoabono.ds;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import javax.wsdl.Output;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bansefi.nss.cargoabono.properties.DsProperties;
import com.bansefi.nss.cargoabono.properties.TcbProperties;
import com.bansefi.nss.cargoabono.services.PasivosAcuerdosServices;
import com.bansefi.nss.cargoabono.tcb.PasivoTcb;
import com.bansefi.nss.cargoabono.vo.DiarioElectronicoRequest;
import com.bansefi.nss.cargoabono.vo.ResponDiaPend;
import com.bansefi.nss.cargoabono.vo.ResponNumSec;
import com.bansefi.nss.cargoabono.vo.ResponseFechaHoraTCB;
import com.bansefi.nss.cargoabono.vo.ResponseRegistroDiarioElectronico;
import com.bansefi.nss.cargoabono.vo.ResponseService;
import com.bansefi.nss.cargoabono.vo.ResponseServiceObject;

public class DiarioElectronicoDS 
{
	private DsProperties propDs = new DsProperties();
	private static final Logger log = LogManager.getLogger(PasivosAcuerdosServices.class);

	private ResponseService InsertaDiario(DiarioElectronicoRequest request)
	{
		ResponseService response = new ResponseService();
		
		String outputString = "";
		String xml ="";
		try
		{
			String action = "urn:InsertaDiarioElectronico";
			
			String StPcFec = request.getFechaPc();
				StPcFec =StPcFec.replace("/", "-");
			String StFecConr= request.getFechaCtble();
				StFecConr =StFecConr.replace("/", "-");
			String StrFecOper =request.getFechaOprcn();
				StrFecOper =StrFecOper.replace("/", "-");
			
			 xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
					+"<soapenv:Header/>"
				    +"<soapenv:Body>"
					+"<dat:InsertaDiarioElectronico>"
					+"<dat:idInternoTermTn>" + request.getIdInternoTermTn() + "</dat:idInternoTermTn>"
					+"<dat:numSec>"+ request.getNumSec() + "</dat:numSec>"
					+"<dat:codTx>"+  request.getCodTx() + "</dat:codTx>"
					+"<dat:codTxDi>" + request.getCodTxDi() + "</dat:codTxDi>"
					+"<dat:idInternoEmplEp>"+ request.getIdInternoEmplEp() +"</dat:idInternoEmplEp>"
					+"<dat:contrida>" + request.getContrida() + "</dat:contrida>"
					+"<dat:sgnCtbleDi>" + request.getSgnCtbleDi() + "</dat:sgnCtbleDi>"
					+"<dat:masMenosDi>" +  request.getMasMenosDi() + "</dat:masMenosDi>"
					+"<dat:numSecAc>" + request.getNumSecAc() + "</dat:numSecAc>"
					+"<dat:impNominal>" + request.getImpNominal() + "</dat:impNominal>"
					+"<dat:codNumrcoMoneda>" + request.getCodNumrcoMoneda() + "</dat:codNumrcoMoneda>"
					+"<dat:impSdo>" + request.getImpSdo() + "</dat:impSdo>"
					+"<dat:codNumrcoMoneda1>" + request.getCodErr1() + "</dat:codNumrcoMoneda1>"
					+"<dat:codErr1>" + request.getCodErr1() + "</dat:codErr1>"
					+"<dat:codErr2>" + request.getCodErr2() + "</dat:codErr2>"
					+"<dat:codErr3>" + request.getCodErr3() + "</dat:codErr3>"
					+"<dat:codErr4>" + request.getCodErr4() + "</dat:codErr4>"
					+"<dat:codErr5>" + request.getCodErr5() + "</dat:codErr5>"
					+"<dat:modoTx>" + request.getModoTx() + "</dat:modoTx>"
					+"<dat:situTx>" + request.getSituTx() + "</dat:situTx>"
					+"<dat:valorDtllTx>" + request.getValorDtllTx() + "</dat:valorDtllTx>"
					+"<dat:idEmplAut>" + request.getIdEmplAut() + "</dat:idEmplAut>"
					+"<dat:fechaAnul xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://ws.wso2.org/dataservice\"/>"
					+"<dat:idTermAnul>" + request.getIdTermAnul() + "</dat:idTermAnul>"
					+"<dat:idEmplAnul>" + request.getIdEmplAnul() + "</dat:idEmplAnul>"
					+"<dat:numSecAnul>" + request.getNumSecAnul() + "</dat:numSecAnul>"
					+"<dat:codRespuesta>" + request.getCodRespuesta() + "</dat:codRespuesta>"
					+"<dat:codNrbeEn>" + request.getCodNrbeEn() + "</dat:codNrbeEn>"
					+"<dat:codInternoUo>" + request.getCodInternoUo() + "</dat:codInternoUo>"
					+"<dat:codNrbeEnFsc>" + request.getCodNrbeEnFsc() + "</dat:codNrbeEnFsc>"
					+"<dat:codInternoUoFsc>" + request.getCodInternoUoFsc() + "</dat:codInternoUoFsc>"
					+"<dat:fechaOprcn>" + StrFecOper + "</dat:fechaOprcn>"
					+"<dat:horaOprcn>" + request.getHoraOprcn() + "</dat:horaOprcn>"
					+"<dat:fechaCtble>" +StFecConr  + "</dat:fechaCtble>"
					+"<dat:fechaValor xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://ws.wso2.org/dataservice\"/>"
					+"<dat:codClopSist>" + request.getCodClopSist() + "</dat:codClopSist>"
					+"<dat:tipoSbclop>" + request.getTipoSbclop() + "</dat:tipoSbclop>"
					+"<dat:numPuesto>" + request.getNumPuesto() + "</dat:numPuesto>"
					+"<dat:fechaOff xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://ws.wso2.org/dataservice\"/>"
					+"<dat:idTermOff/>"
					+"<dat:idEmplOff/>"
					+"<dat:numSecOff>" + request.getNumSecOff() + "</dat:numSecOff>"
					+"<dat:impNominalX>" + request.getImpNominalX() + "</dat:impNominalX>"
					+"<dat:claveAnulDi>" + request.getClaveAnulDi() + "</dat:claveAnulDi>"
					+"<dat:diTextArg1>" + request.getDiTextArg1() + "</dat:diTextArg1>"
					+"<dat:diTextArg2>" + request.getDiTextArg2() + "</dat:diTextArg2>"
					+"<dat:diTextArg3>" + request.getDiTextArg3() + "</dat:diTextArg3>"
					+"<dat:diTextArg4>" + request.getDiTextArg4() + "</dat:diTextArg4>"
					+"<dat:diTextArg5>" + request.getDiTextArg5() + "</dat:diTextArg5>"
					+"<dat:fechaPc>" + StPcFec + "</dat:fechaPc>"
					+"<dat:horaPc>" + request.getHoraPc() + "</dat:horaPc>"
				+"</dat:InsertaDiarioElectronico>"
				+"</soapenv:Body>"
			   +"</soapenv:Envelope>";

			
			
			String wsURL = propDs.getURL_DIARIO_ELECTRONICO();
			
			outputString=SalidaResponse(xml,wsURL,action,"");
			if(outputString.contains("SUCCESSFUL"))
			{
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
			log.error("DiarioElectronicoDS - InsertaDiario : Exception. " + e.getMessage());
			log.error("DiarioElectronicoDS - InsertaDiario : outputString. " + outputString);
			log.error("DiarioElectronicoDS - InsertaDiario : input xml. " + xml);
		}
		return response;
	}
	
	public ResponDiaPend RegistraCargoAbonoPendiente(  String entidad, 
					String centro,				String terminal, 
					String empleado,			String tipoOp,
					String concepto,			String importe,
					String importaSldo,			String numSecAc,
					String numSec	 ,String fechaOperacion,String cajaInt)
	{
		ResponDiaPend response = new ResponDiaPend();
try
{
	PasivoTcb pasivoTCB = new PasivoTcb();
	ResponseFechaHoraTCB fechaHora = pasivoTCB.FechaContable(terminal);
	String horaOprn = "";
	String fechaContable = "";
	fechaOperacion =fechaOperacion.replace("/", "-");
	
	if(fechaHora.getStatus() == 1)
	{
		TcbProperties oPropTcb = new TcbProperties();
		String StrCodTx =oPropTcb.getABONO_CODTX();
		String SgnCtbleDi="H";
		String SubClop ="0002";
		
		if(tipoOp.equals("C"))
		{
			StrCodTx =oPropTcb.getCARGO_CODTX();
			SgnCtbleDi="D";
			SubClop="0001";
		}
		
		horaOprn = fechaHora.getHoraOprcn();
		fechaContable = fechaHora.getFechaCble();
		fechaContable =fechaContable.replace("/", "-");
		//fechaOperacion=fechaHora.getFechaOprcn();
		DecimalFormat df = new DecimalFormat("#.00");
		importe = df.format(Double.parseDouble(importe.replace("$", "")));
		importaSldo = df.format(Double.parseDouble(importaSldo.replace("$", "")));
		
		DiarioElectronicoRequest requestDiario = new DiarioElectronicoRequest();
		requestDiario.setClaveAnulDi("");
		requestDiario.setCodClopSist("01");
		requestDiario.setCodErr1("0");
		requestDiario.setCodErr2("0");
		requestDiario.setCodErr3("0");
		requestDiario.setCodErr4("0");
		requestDiario.setCodErr5("0");
		requestDiario.setCodInternoUo(centro);
		requestDiario.setCodInternoUoFsc(centro);
		requestDiario.setCodNrbeEn(entidad);
		requestDiario.setCodNrbeEnFsc(entidad);
		requestDiario.setCodNumrcoMoneda("MXN");
		requestDiario.setCodNumrcoMoneda1("MXN");
		requestDiario.setCodRespuesta("0");
		requestDiario.setCodTx(StrCodTx);
		requestDiario.setCodTxDi("");
		requestDiario.setContrida(cajaInt);
		requestDiario.setDiTextArg1("");
		requestDiario.setDiTextArg2("");
		requestDiario.setDiTextArg3("");
		requestDiario.setDiTextArg4("");
		requestDiario.setDiTextArg5("");
		requestDiario.setFechaCtble(fechaContable);
		requestDiario.setFechaOprcn(fechaOperacion);
		requestDiario.setFechaValor(fechaContable);
		requestDiario.setHoraOprcn(horaOprn);
		requestDiario.setIdInternoEmplEp(empleado);
		requestDiario.setIdInternoTermTn(terminal);
		requestDiario.setImpNominal(importe);
		String ImpNominalX = importe.replace(".", "");
		requestDiario.setImpNominalX(StringUtils.leftPad(ImpNominalX, 13, "0"));
		requestDiario.setImpSdo(importaSldo);
		requestDiario.setMasMenosDi("0");
		requestDiario.setModoTx("0");
		
		String StrPuesto="00";
		if(terminal.length()>2)
			StrPuesto = terminal.substring(terminal.length()-2);
		requestDiario.setNumPuesto(StrPuesto);
		requestDiario.setNumSec(numSec);
		requestDiario.setNumSecAc(numSecAc);
		requestDiario.setNumSecAnul("0");
		requestDiario.setNumSecOff("0");
		requestDiario.setSgnCtbleDi(SgnCtbleDi);
		requestDiario.setSituTx("00");
		requestDiario.setTipoSbclop(SubClop);
		requestDiario.setValorDtllTx(concepto);
		requestDiario.setIdTermAnul("");
		requestDiario.setIdEmplAnul("");
		requestDiario.setFechaPc(fechaHora.getFechaOprcn());
		requestDiario.setHoraPc(horaOprn);
		ResponseService responseDia = InsertaDiario(requestDiario);
		response.setStatus(0);
		if(responseDia.getStatus()==1)
		{
			response.setStatus(1);
			response.setFEC_PC(fechaHora.getFechaOprcn());
			response.setHORA_PC(horaOprn);
			response.setHORA_OPERACION(horaOprn);
		}
		else
		{
			response.setCOD_TX(responseDia.getCOD_TX());
			response.setTXT_ARG1(response.getTXT_ARG1());
			
		}
	} 
	else 
	{
		response.setStatus(0);
		response.setDescripcion(fechaHora.getDescripcion());
		
		
	}
}
	catch(Exception ex)
	{
		response.setCOD_RESPUESTA(-1);
		response.setDescripcion(ex.getMessage());
		log.error("DiarioElectronicoDS - RegistraCargoAbonoPendiente : Exception. " + ex.getMessage());
	}

		return response;
	}

	public ResponDiaPend RegistraCargoAbonoPendienteInter(  String entidad, 
			String centro,				String terminal, 
			String empleado,			String tipoOp,
			String concepto,			String importe,
			String importaSldo,			String numSecAc,
			String numSec	 ,
			String fechaOperacion,String cajaInt,
			String StrClop,String StrSubClop)
{
ResponDiaPend response = new ResponDiaPend();
try
{
PasivoTcb pasivoTCB = new PasivoTcb();
ResponseFechaHoraTCB fechaHora = pasivoTCB.FechaContable(terminal);
String horaOprn = "";
String fechaContable = "";
fechaOperacion =fechaOperacion.replace("/", "-");

if(fechaHora.getStatus() == 1)
{
TcbProperties oPropTcb = new TcbProperties();
String StrCodTx =oPropTcb.getABONO_CODTX();
String SgnCtbleDi="H";

if(tipoOp.equals("C"))
{
	StrCodTx =oPropTcb.getCARGO_CODTX();
	SgnCtbleDi="D";
}

horaOprn = fechaHora.getHoraOprcn();
fechaContable = fechaHora.getFechaCble();
fechaContable =fechaContable.replace("/", "-");
//fechaOperacion=fechaHora.getFechaOprcn();
DecimalFormat df = new DecimalFormat("#.00");
importe = df.format(Double.parseDouble(importe.replace("$", "")));
importaSldo = df.format(Double.parseDouble(importaSldo.replace("$", "")));

DiarioElectronicoRequest requestDiario = new DiarioElectronicoRequest();
requestDiario.setClaveAnulDi("");
requestDiario.setCodClopSist(StrClop);
requestDiario.setCodErr1("0");
requestDiario.setCodErr2("0");
requestDiario.setCodErr3("0");
requestDiario.setCodErr4("0");
requestDiario.setCodErr5("0");
requestDiario.setCodInternoUo(centro);
requestDiario.setCodInternoUoFsc(centro);
requestDiario.setCodNrbeEn(entidad);
requestDiario.setCodNrbeEnFsc(entidad);
requestDiario.setCodNumrcoMoneda("MXN");
requestDiario.setCodNumrcoMoneda1("MXN");
requestDiario.setCodRespuesta("0");

requestDiario.setCodTx(StrCodTx);
requestDiario.setCodTxDi("");
requestDiario.setContrida(cajaInt);
requestDiario.setDiTextArg1("");
requestDiario.setDiTextArg2("");
requestDiario.setDiTextArg3("");
requestDiario.setDiTextArg4("");
requestDiario.setDiTextArg5("");
requestDiario.setFechaCtble(fechaContable);
requestDiario.setFechaOprcn(fechaOperacion);
requestDiario.setFechaValor(fechaContable);
requestDiario.setHoraOprcn(horaOprn);
requestDiario.setIdInternoEmplEp(empleado);
requestDiario.setIdInternoTermTn(terminal);
requestDiario.setImpNominal(importe);
String ImpNominalX = importe.replace(".", "");
requestDiario.setImpNominalX(StringUtils.leftPad(ImpNominalX, 13, "0"));
requestDiario.setImpSdo(importaSldo);
requestDiario.setMasMenosDi("0");
requestDiario.setModoTx("0");
requestDiario.setNumPuesto("00");
requestDiario.setNumSec(numSec);
requestDiario.setNumSecAc(numSecAc);
requestDiario.setNumSecAnul("0");
requestDiario.setNumSecOff("0");
requestDiario.setSgnCtbleDi(SgnCtbleDi);
requestDiario.setSituTx("00");
requestDiario.setTipoSbclop(StrSubClop);
requestDiario.setValorDtllTx(concepto);
requestDiario.setIdTermAnul("");
requestDiario.setIdEmplAnul("");
requestDiario.setFechaPc(fechaHora.getFechaOprcn());
requestDiario.setHoraPc(horaOprn);
ResponseService responseDia = InsertaDiario(requestDiario);
response.setStatus(0);
if(responseDia.getStatus()==1)
{
	response.setStatus(1);
	response.setFEC_PC(fechaHora.getFechaOprcn());
	response.setHORA_PC(horaOprn);
	response.setHORA_OPERACION(horaOprn);
}
} 
else 
{
response.setStatus(0);
response.setDescripcion(fechaHora.getDescripcion());
}
}
catch(Exception ex)
{
response.setCOD_RESPUESTA(-1);
response.setDescripcion(ex.getMessage());
log.error("DiarioElectronicoDS - RegistraCargoAbonoPendiente : Exception. " + ex.getMessage());
}

return response;
}

	
	
	public ResponseRegistroDiarioElectronico UltimoMovimiento(String terminal, String entidad)
	{
		ResponseRegistroDiarioElectronico response = new ResponseRegistroDiarioElectronico();
		String action = "urn:ConsultaUltimoMovimiento";
		String xml = "";
		String outputString = "";
		try
		{
			xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
					+"<soapenv:Header/>"
				    +"<soapenv:Body>"
				    + " <dat:ConsultaUltimoMovimiento>"
				    + "	 <dat:TERMINAL>" + terminal + "</dat:TERMINAL>"
				    + "  <dat:ENTIDAD>" + entidad +"</dat:ENTIDAD>"
				    + " </dat:ConsultaUltimoMovimiento>"
				+"</soapenv:Body>"
			   +"</soapenv:Envelope>";

			String wsURL = propDs.getURL_DIARIO_ELECTRONICO();
			
			outputString= SalidaResponse(xml,wsURL,action,"ConsultaUltimoMovimientoResp");
			
			if(outputString.contains("RespuetaDiario"))
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(outputString.getBytes("utf-8")));
				
				NodeList RespuetaDiario = doc.getElementsByTagName("RespuetaDiario");
				System.out.println(xml);
				System.out.println(outputString);

				for(int i = 0 ; i < RespuetaDiario.getLength()  ; i++ )
				{
					Node item = RespuetaDiario.item(i);
					Element eElement = (Element) item;
					DiarioElectronicoRequest diario = new DiarioElectronicoRequest();

					diario.setContrida(eElement.getElementsByTagName("CONTRIDA").item(0).getTextContent());
					diario.setMasMenosDi(eElement.getElementsByTagName("MAS_MENOS_DI").item(0).getTextContent());					
					diario.setModoTx(eElement.getElementsByTagName("MODO_TX").item(0).getTextContent());
					diario.setSgnCtbleDi(eElement.getElementsByTagName("SGN_CTBLE_DI").item(0).getTextContent());
					diario.setCodClopSist(eElement.getElementsByTagName("COD_CLOP_SIST").item(0).getTextContent());					
					diario.setNumPuesto(eElement.getElementsByTagName("NUM_PUESTO").item(0).getTextContent());
					diario.setCodErr1(eElement.getElementsByTagName("COD_ERR_1").item(0).getTextContent());
					diario.setCodErr2(eElement.getElementsByTagName("COD_ERR_2").item(0).getTextContent());
					diario.setCodErr3(eElement.getElementsByTagName("COD_ERR_3").item(0).getTextContent());
					diario.setCodErr4(eElement.getElementsByTagName("COD_ERR_4").item(0).getTextContent());
					diario.setCodErr5(eElement.getElementsByTagName("COD_ERR_5").item(0).getTextContent());
					diario.setCodNumrcoMoneda(eElement.getElementsByTagName("COD_NUMRCO_MONEDA").item(0).getTextContent());
					diario.setHoraOprcn(eElement.getElementsByTagName("HORA_OPRCN").item(0).getTextContent());
					diario.setHoraPc(eElement.getElementsByTagName("HORA_PC").item(0).getTextContent());
					diario.setCodInternoUo(eElement.getElementsByTagName("COD_INTERNO_UO").item(0).getTextContent());
					diario.setCodInternoUoFsc(eElement.getElementsByTagName("COD_INTERNO_UO_FSC").item(0).getTextContent());
					diario.setCodNrbeEn(eElement.getElementsByTagName("COD_NRBE_EN").item(0).getTextContent());					
					diario.setCodNrbeEnFsc(eElement.getElementsByTagName("COD_NRBE_EN_FSC").item(0).getTextContent());
					diario.setCodTxDi(eElement.getElementsByTagName("COD_TX_DI").item(0).getTextContent());					
					diario.setTipoSbclop(eElement.getElementsByTagName("TIPO_SBCLOP").item(0).getTextContent());					
					diario.setFechaAnul(eElement.getElementsByTagName("FECHA_ANUL").item(0).getTextContent());				
					diario.setFechaCtble(eElement.getElementsByTagName("FECHA_CTBLE").item(0).getTextContent());
					diario.setFechaOff(eElement.getElementsByTagName("FECHA_OFF").item(0).getTextContent());
					diario.setFechaOprcn(eElement.getElementsByTagName("FECHA_OPRCN").item(0).getTextContent());
					diario.setFechaPc(eElement.getElementsByTagName("FECHA_PC").item(0).getTextContent());
					diario.setFechaValor(eElement.getElementsByTagName("FECHA_VALOR").item(0).getTextContent());
					diario.setNumSecAnul(eElement.getElementsByTagName("NUM_SEC_ANUL").item(0).getTextContent());
					diario.setNumSecOff(eElement.getElementsByTagName("NUM_SEC_OFF").item(0).getTextContent());
					diario.setNumSec(eElement.getElementsByTagName("NUM_SEC").item(0).getTextContent());
					diario.setCodTx(eElement.getElementsByTagName("COD_TX").item(0).getTextContent());					
					diario.setIdEmplAnul(eElement.getElementsByTagName("ID_EMPL_ANUL").item(0).getTextContent());
					diario.setIdEmplAut(eElement.getElementsByTagName("ID_EMPL_AUT").item(0).getTextContent());
					diario.setIdEmplOff(eElement.getElementsByTagName("ID_EMPL_OFF").item(0).getTextContent());
					diario.setIdInternoEmplEp(eElement.getElementsByTagName("ID_INTERNO_EMPL_EP").item(0).getTextContent());
					diario.setIdInternoTermTn(eElement.getElementsByTagName("ID_INTERNO_TERM_TN").item(0).getTextContent());
					diario.setIdTermAnul(eElement.getElementsByTagName("ID_TERM_ANUL").item(0).getTextContent());
					diario.setNumSecAc(eElement.getElementsByTagName("NUM_SEC_AC").item(0).getTextContent());
					diario.setImpNominalX(eElement.getElementsByTagName("IMP_NOMINAL_X").item(0).getTextContent());
					diario.setImpNominal(eElement.getElementsByTagName("IMP_NOMINAL").item(0).getTextContent());
					diario.setImpSdo(eElement.getElementsByTagName("IMP_SDO").item(0).getTextContent());
					diario.setDiTextArg1(eElement.getElementsByTagName("DI_TEXT_ARG_1").item(0).getTextContent());
					diario.setDiTextArg2(eElement.getElementsByTagName("DI_TEXT_ARG_2").item(0).getTextContent());
					diario.setDiTextArg3(eElement.getElementsByTagName("DI_TEXT_ARG_3").item(0).getTextContent());
					diario.setDiTextArg4(eElement.getElementsByTagName("DI_TEXT_ARG_4").item(0).getTextContent());
					diario.setDiTextArg5(eElement.getElementsByTagName("DI_TEXT_ARG_5").item(0).getTextContent());					
					diario.setClaveAnulDi(eElement.getElementsByTagName("CLAVE_ANUL_DI").item(0).getTextContent());
					diario.setValorDtllTx(eElement.getElementsByTagName("VALOR_DTLL_TX").item(0).getTextContent());
					diario.setCodRespuesta(eElement.getElementsByTagName("COD_RESPUESTA").item(0).getTextContent());
					diario.setCodNumrcoMoneda1(eElement.getElementsByTagName("COD_NUMRCO_MONEDA1").item(0).getTextContent());
					response.setRegistroDiarioElectronico(diario);
					response.setStatus(1);
				}
			}
			else 
			{
				response.setStatus(0);
				response.setDescripcion("Datos incorrectos");
				log.error("DiarioElectronicoDS - UltimoMovimiento : Error. " + outputString);
			}

		}
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.info("DiarioElectronicoDS - UltimoMovimiento : In. " + xml);
			log.info("DiarioElectronicoDS - UltimoMovimiento : Out. " + outputString);
			log.error("DiarioElectronicoDS - UltimoMovimiento : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public ResponseService ObtieneMovimiento(String terminal, String fechaPc, String horaPc, 
			String numSec, String horaOprcn, String codRespuesta)
	{
		ResponseService response = new ResponseService();
		String action = "urn:ObtenerMovimiento";
		String xml = "";
		String outputString = "";
		try{
			xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
					+"<soapenv:Header/>"
				    +"<soapenv:Body>"
				    + " <dat:ObtenerMovimiento>"
				    + "	 <dat:TERMINAL>" + terminal + "</dat:TERMINAL>"
				    + "  <dat:FECHA>" + fechaPc +"</dat:FECHA>"
				    + "  <dat:HORA>" + horaPc +"</dat:HORA>"
				    + " </dat:ObtenerMovimiento>"
				+"</soapenv:Body>"
			   +"</soapenv:Envelope>";
			   

			String wsURL = propDs.getURL_DIARIO_ELECTRONICO();
			outputString= SalidaResponse(xml,wsURL,action,"ConsultaUltimoMovimientoResp");
			
			if(outputString.contains("RespuetaDiario"))
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(outputString.getBytes("utf-8")));
				
				NodeList RespuetaDiario = doc.getElementsByTagName("RespuetaDiario");
				System.out.println(xml);
				System.out.println(outputString);

				for(int i = 0 ; i < RespuetaDiario.getLength()  ; i++ )
				{
					Node item = RespuetaDiario.item(i);
					Element eElement = (Element) item;
					
					response.setStatus(1);
				}
			} 
			else 
			{
				response.setStatus(0);
				response.setDescripcion("Datos incorrectos");
				log.error("DiarioElectronicoDS - ObtieneMovimiento : Error. " + outputString);
			}

		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.info("DiarioElectronicoDS - ObtieneMovimiento : In. " + xml);
			log.info("DiarioElectronicoDS - ObtieneMovimiento : Out. " + outputString);
			log.error("DiarioElectronicoDS - ObtieneMovimiento : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public ResponseService ActualizaRegistro(ResponDiaPend oResp)
	{
		ResponseService response = new ResponseService();
		String action = "urn:ActualizaDiario";
		String xml = "";
		String outputString = "";
		try
		{
			oResp.setFEC_PC(oResp.getFEC_PC().replace("/", "-"));
			
			xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
					+"<soapenv:Header/>"
				    +"<soapenv:Body>"
				    + " <dat:ActualizaDiario>"
				    + "	 <dat:NUM_SEC>" + oResp.getNUMSEC() + "</dat:NUM_SEC>"
				    + "	 <dat:HORA_OPRCN>" + oResp.getHORA_OPERACION() + "</dat:HORA_OPRCN>"
				    + "	 <dat:COD_RESPUESTA>" + oResp.getCOD_RESPUESTA() + "</dat:COD_RESPUESTA>"
				    + "	 <dat:IMP_SDO>" + oResp.getIMP_SDO() + "</dat:IMP_SDO>"
				    + "	 <dat:TERMINAL>" + oResp.getTERMINAL() + "</dat:TERMINAL>"
				    + "  <dat:FECHA_PC>" + oResp.getFEC_PC() +"</dat:FECHA_PC>"
				    + "  <dat:HORA_PC>" + oResp.getHORA_PC() +"</dat:HORA_PC>"
				    + " </dat:ActualizaDiario>"
				+"</soapenv:Body>"
			   +"</soapenv:Envelope>";
			
			String wsURL = propDs.getURL_DIARIO_ELECTRONICO();
			outputString= SalidaResponse(xml,wsURL,action,"ConsultaUltimoMovimientoResp");
			
			if(outputString.contains("SUCCESSFUL"))
			{
				response.setStatus(1);
				response.setDescripcion("EXITO");
			} 
			else 
			{
				response.setStatus(0);
				response.setDescripcion(outputString);
			}

		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.info("DiarioElectronicoDS - ActualizaRegistro : In. " + xml);
			log.info("DiarioElectronicoDS - ActualizaRegistro : Out. " + outputString);
			log.error("DiarioElectronicoDS - ActualizaRegistro : Exception. " + e.getMessage());
			
		}
		return response;
	}
	
	public ResponseService RegistraImprSaldo(  String entidad, 
			String centro, 			String terminal,			String empleado,
			String concepto,		String numSecAc,			String numSec)
	{
		ResponseService response = new ResponseService();

		PasivoTcb pasivoTCB = new PasivoTcb();
		ResponseFechaHoraTCB fechaHora = pasivoTCB.FechaContable(terminal);
		String horaOprn = "";
		String fechaContable = "";
		String FecOperacion="";
		if(fechaHora.getStatus() == 1)
		{

			horaOprn = fechaHora.getHoraOprcn();
			fechaContable = fechaHora.getFechaCble();
			FecOperacion = fechaHora.getFechaOprcn();
			DecimalFormat df = new DecimalFormat("#.00");
		
		
			DiarioElectronicoRequest requestDiario = new DiarioElectronicoRequest();
			requestDiario.setClaveAnulDi("");
			requestDiario.setCodClopSist("");
			requestDiario.setCodErr1("0");
			requestDiario.setCodErr2("0");
			requestDiario.setCodErr3("0");
			requestDiario.setCodErr4("0");
			requestDiario.setCodErr5("0");
			requestDiario.setCodInternoUo(centro);
			requestDiario.setCodInternoUoFsc(centro);
			requestDiario.setCodNrbeEn(entidad);
			requestDiario.setCodNrbeEnFsc(entidad);
			requestDiario.setCodNumrcoMoneda("MXN");
			requestDiario.setCodNumrcoMoneda1("MXN");
			requestDiario.setCodRespuesta("0");
			requestDiario.setCodTx("DVI50COU");
			requestDiario.setCodTxDi("xxxx");
			requestDiario.setContrida("");
			requestDiario.setDiTextArg1("");
			requestDiario.setDiTextArg2("");
			requestDiario.setDiTextArg3("");
			requestDiario.setDiTextArg4("");
			requestDiario.setDiTextArg5("");
			requestDiario.setFechaCtble(fechaContable);
			requestDiario.setFechaOprcn(FecOperacion);
			requestDiario.setFechaValor(fechaContable);
			requestDiario.setHoraOprcn(horaOprn);
			requestDiario.setIdInternoEmplEp(empleado);
			requestDiario.setIdInternoTermTn(terminal);
			requestDiario.setImpNominal("0");
		
			requestDiario.setImpNominalX("0");
			requestDiario.setImpSdo("0");
			requestDiario.setMasMenosDi("0");
			requestDiario.setModoTx("0");
			requestDiario.setNumPuesto("00");
			requestDiario.setNumSec(numSec);
			requestDiario.setNumSecAc(numSecAc);
			requestDiario.setNumSecAnul("0");
			requestDiario.setNumSecOff("0");
			requestDiario.setSgnCtbleDi("D");
			requestDiario.setSituTx("00");
			requestDiario.setTipoSbclop("0001");
			requestDiario.setValorDtllTx(concepto);
			requestDiario.setIdTermAnul("");
			requestDiario.setIdEmplAnul("");
			requestDiario.setFechaPc(fechaHora.getFechaOprcn());
			requestDiario.setHoraPc(horaOprn);
			response = InsertaDiario(requestDiario);
				if(response.getStatus()==1)
				{
					response.setStatus(1);
					response.setDescripcion(horaOprn);
				}
				else
					response.setStatus(-1);
		} 
		else 
		{
			response.setStatus(0);
			response.setDescripcion(fechaHora.getDescripcion());
		}
		return response;
}

	public ResponseService ActualizaNumSec( String entidad,String centro,String terminal)
	{
		ResponseService response = new ResponseService();
		String action = "UpdateNumSec";
		String xml = "";
		String outputString = "";
		try
		{
			xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
			  +" <soapenv:Header/>"
			  +" <soapenv:Body>"
			  +"    <dat:UpdateNumSec>"
			  +"       <dat:ENTIDAD>"+entidad+"</dat:ENTIDAD>"
			  +"       <dat:CENTRO>"+centro+"</dat:CENTRO>"
			  +"       <dat:TERMINAL>"+terminal+"</dat:TERMINAL>"
			  +"    </dat:UpdateNumSec>"
			  +" </soapenv:Body>"
			  +"</soapenv:Envelope>";
			
			
			String wsURL = propDs.getURL_DIARIO_ELECTRONICO();
			outputString= SalidaResponse(xml,wsURL,action,"UpdateNumSec");
			
			if(outputString.contains("SUCCESSFUL"))
			{
				response.setStatus(1);
				response.setDescripcion("EXITO");
			} 
			else 
			{
				response.setStatus(0);
				response.setDescripcion(outputString);
			}

		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.info("DiarioElectronicoDS - ActualizaRegistro : In. " + xml);
			log.info("DiarioElectronicoDS - ActualizaRegistro : Out. " + outputString);
			log.error("DiarioElectronicoDS - ActualizaRegistro : Exception. " + e.getMessage());
		}
		return response;
	}
	
	public ResponNumSec ObtieneNumSec( String entidad,String centro,String terminal)
	{
		ResponNumSec response = new ResponNumSec();
		String action = "GetNumSec";
		String xml = "";
		String outputString = "";
		try
		{
			
			xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">"
			  +" <soapenv:Header/>"
			  +" <soapenv:Body>"
			  +"    <dat:GetNumSec>"
			  +"       <dat:ENTIDAD>"+entidad+"</dat:ENTIDAD>"
			  +"       <dat:CENTRO>"+centro+"</dat:CENTRO>"
			  +"       <dat:TERMINAL>"+terminal+"</dat:TERMINAL>"
			  +"    </dat:GetNumSec>"
			  +" </soapenv:Body>"
			  +"</soapenv:Envelope>";
			
			
			String wsURL = propDs.getURL_DIARIO_ELECTRONICO();
			outputString= SalidaResponse(xml,wsURL,action,"GetNumSec");
			
			if(outputString.contains("num_sec"))
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(outputString.getBytes("utf-8")));
				
				NodeList RespuetaDiario = doc.getElementsByTagName("NumSecM");			
				for(int i = 0 ; i < RespuetaDiario.getLength()  ; i++ )
				{
					Node item = RespuetaDiario.item(i);
					Element eElement = (Element) item;
					String StrNumSec =	eElement.getElementsByTagName("num_sec").item(0).getTextContent();
					String StrNumPue =	eElement.getElementsByTagName("num_puesto").item(0).getTextContent();
					response.setNUMPUESTO(StrNumPue);
					response.setNUMSEC(Integer.parseInt(StrNumSec));
					
					ResponseService oResp = ActualizaNumSec( entidad, centro, terminal);
					if(oResp.getStatus()==1)
						response.setStatus(1);	
					else
						response.setStatus(-1);
				}
			} 
			else 
			{
				response.setStatus(0);
				response.setDescripcion(outputString);
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.info("DiarioElectronicoDS - ActualizaRegistro : In. " + xml);
			log.info("DiarioElectronicoDS - ActualizaRegistro : Out. " + outputString);
			log.error("DiarioElectronicoDS - ActualizaRegistro : Exception. " + e.getMessage());
			
		}
		return response;
	}	

	public ResponseServiceObject ObtieneUltimoMovPendiente(String entidad,String terminal)
	{
		ResponseServiceObject response = new ResponseServiceObject();
		String action = "ConsultaUltimoMovimientoPendiente";
		String xml = "";
		String outputString = "";
		try
		{
			StringBuilder Bvista = new StringBuilder();
			Bvista.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://ws.wso2.org/dataservice\">");
			Bvista.append("<soapenv:Header/>");
			Bvista.append(" <soapenv:Body>");
			Bvista.append("   <dat:ConsultaUltimoMovimientoPendiente>");
			Bvista.append("       <dat:TERMINAL>"+terminal+"</dat:TERMINAL>");
			Bvista.append("       <dat:ENTIDAD>"+entidad+"</dat:ENTIDAD>");
			Bvista.append("   </dat:ConsultaUltimoMovimientoPendiente>");
			Bvista.append(" </soapenv:Body>");
			Bvista.append("</soapenv:Envelope>");
			  
			xml =Bvista.toString();
			
			String wsURL = propDs.getURL_DIARIO_ELECTRONICO();
			outputString= SalidaResponse(xml,wsURL,action,"ConsultaUltimoMovimientoPendiente");
			
			if(outputString.contains("ConsultaUltimoMovimientoPendienteResp"))
			{
				DiarioElectronicoRequest oDiarioElec = new DiarioElectronicoRequest();
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new ByteArrayInputStream(outputString.getBytes("utf-8")));
				
				NodeList RespuetaDiario = doc.getElementsByTagName("RespuetaDiarioMovimiento");			
				for(int i = 0 ; i < RespuetaDiario.getLength()  ; i++ )
				{
					Node item = RespuetaDiario.item(i);
					Element eElement = (Element) item;
					String StrContrida =	eElement.getElementsByTagName("CONTRIDA").item(0).getTextContent();
					String StrMasMenosDi =	eElement.getElementsByTagName("MAS_MENOS_DI").item(0).getTextContent();
					String StrMODO_TX =	eElement.getElementsByTagName("MODO_TX").item(0).getTextContent();
					String StrSGN_CTBLE_DI =	eElement.getElementsByTagName("SGN_CTBLE_DI").item(0).getTextContent();
					String StrCOD_CLOP_SIST =	eElement.getElementsByTagName("COD_CLOP_SIST").item(0).getTextContent();
					String StrNUM_PUESTO =	eElement.getElementsByTagName("NUM_PUESTO").item(0).getTextContent();
					String StrSITU_TX =	eElement.getElementsByTagName("SITU_TX").item(0).getTextContent();
					String StrCOD_RESPUESTA =	eElement.getElementsByTagName("COD_RESPUESTA").item(0).getTextContent();
					String StrCOD_NUMRCO_MONEDA =	eElement.getElementsByTagName("COD_NUMRCO_MONEDA").item(0).getTextContent();
					String StrHORA_OPRCN =	eElement.getElementsByTagName("HORA_OPRCN").item(0).getTextContent();
					String StrHORA_PC =	eElement.getElementsByTagName("HORA_PC").item(0).getTextContent();
					//String StrHORA_PC_FINAL =	eElement.getElementsByTagName("HORA_PC_FINAL").item(0).getTextContent();
					String StrCOD_INTERNO_UO =	eElement.getElementsByTagName("COD_INTERNO_UO").item(0).getTextContent();
					String StrCOD_INTERNO_UO_FSC =	eElement.getElementsByTagName("COD_INTERNO_UO_FSC").item(0).getTextContent();
					String StrCOD_NRBE_EN =	eElement.getElementsByTagName("COD_NRBE_EN").item(0).getTextContent();
					String StrCOD_NRBE_EN_FSC =	eElement.getElementsByTagName("COD_NRBE_EN_FSC").item(0).getTextContent();
					String StrCOD_TX_DI =	eElement.getElementsByTagName("COD_TX_DI").item(0).getTextContent();
					String StrTIPO_SBCLOP =	eElement.getElementsByTagName("TIPO_SBCLOP").item(0).getTextContent();
					String StrFECHA_CTBLE =	eElement.getElementsByTagName("FECHA_CTBLE").item(0).getTextContent();
					String StrFECHA_OPRCN =	eElement.getElementsByTagName("FECHA_OPRCN").item(0).getTextContent();
					String StrFECHA_PC =	eElement.getElementsByTagName("FECHA_PC").item(0).getTextContent();
					String StrNUM_SEC =	eElement.getElementsByTagName("NUM_SEC").item(0).getTextContent();
					String StrCOD_TX =	eElement.getElementsByTagName("COD_TX").item(0).getTextContent();
					String StrID_INTERNO_EMPL_EP =	eElement.getElementsByTagName("ID_INTERNO_EMPL_EP").item(0).getTextContent();
					String StrID_INTERNO_TERM_TN =	eElement.getElementsByTagName("ID_INTERNO_TERM_TN").item(0).getTextContent();
					String StrNUM_SEC_AC =	eElement.getElementsByTagName("NUM_SEC_AC").item(0).getTextContent();
					String StrIMP_NOMINAL =	eElement.getElementsByTagName("IMP_NOMINAL").item(0).getTextContent();
					String StrVALOR_DTLL_TX =	eElement.getElementsByTagName("VALOR_DTLL_TX").item(0).getTextContent();
					
					oDiarioElec.setContrida(StrContrida);
					oDiarioElec.setMasMenosDi(StrMasMenosDi);
					oDiarioElec.setModoTx(StrMODO_TX);
					oDiarioElec.setSgnCtbleDi(StrSGN_CTBLE_DI);
					oDiarioElec.setCodClopSist(StrCOD_CLOP_SIST);
					oDiarioElec.setNumPuesto(StrNUM_PUESTO);
					oDiarioElec.setSituTx(StrSITU_TX);
					oDiarioElec.setCodRespuesta(StrCOD_RESPUESTA);
					oDiarioElec.setCodNumrcoMoneda(StrCOD_NUMRCO_MONEDA);
					oDiarioElec.setHoraOprcn(StrHORA_OPRCN);
					oDiarioElec.setHoraPc(StrHORA_PC);
					oDiarioElec.setCodInternoUo(StrCOD_INTERNO_UO);
					oDiarioElec.setCodInternoUoFsc(StrCOD_INTERNO_UO_FSC);
					oDiarioElec.setCodNrbeEn(StrCOD_NRBE_EN);
					oDiarioElec.setCodNrbeEnFsc(StrCOD_NRBE_EN_FSC);
					oDiarioElec.setCodTxDi(StrCOD_TX_DI);
					oDiarioElec.setTipoSbclop(StrTIPO_SBCLOP);
					oDiarioElec.setFechaCtble(StrFECHA_CTBLE);
					oDiarioElec.setFechaOprcn(StrFECHA_OPRCN);
					oDiarioElec.setFechaPc(StrFECHA_PC);
					oDiarioElec.setNumSec(StrNUM_SEC);
					oDiarioElec.setCodTx(StrCOD_TX);
					oDiarioElec.setIdInternoEmplEp(StrID_INTERNO_EMPL_EP);
					oDiarioElec.setIdInternoTermTn(StrID_INTERNO_TERM_TN);
					oDiarioElec.setNumSecAc(StrNUM_SEC_AC);
					oDiarioElec.setImpNominal(StrIMP_NOMINAL);
					oDiarioElec.setValorDtllTx(StrVALOR_DTLL_TX);
					response.setStatus(1);
					response.setEntReturn(oDiarioElec);
				}
			} 
			else 
			{
				response.setStatus(0);
				response.setDescripcion(outputString);
			}
		} 
		catch (Exception e) 
		{
			response.setStatus(-1);
			response.setDescripcion(e.getMessage());
			log.info("DiarioElectronicoDS - ActualizaRegistro : In. " + xml);
			log.info("DiarioElectronicoDS - ActualizaRegistro : Out. " + outputString);
			log.error("DiarioElectronicoDS - ActualizaRegistro : Exception. " + e.getMessage());
			
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
			log.error("PasivoTcb - Cargo : SalidaResponse. " + ex.getMessage());
		}
		
		return salida;
	} 
	
}
