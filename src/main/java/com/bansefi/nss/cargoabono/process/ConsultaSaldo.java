package com.bansefi.nss.cargoabono.process;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.bansefi.nss.cargoabono.commons.CantidadLetras;
import com.bansefi.nss.cargoabono.ds.DiarioElectronicoDS;
import com.bansefi.nss.cargoabono.services.PasivosAcuerdosServices;
import com.bansefi.nss.cargoabono.tcb.PasivoTcb;
import com.bansefi.nss.cargoabono.vo.ResponNumSec;
import com.bansefi.nss.cargoabono.vo.ResponseConsultaClabe;
import com.bansefi.nss.cargoabono.vo.ResponseConsultaSaldo;
import com.bansefi.nss.cargoabono.vo.ResponseFechaActual;
import com.bansefi.nss.cargoabono.vo.ResponseService;


public class ConsultaSaldo 
{
	public JSONObject Comprobante(String entidad, String centro, String acuerdo, 
			String terminal, String horaOpr, String nombreCliente , String idExterno, 
			String empleado)
	{
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonResultado = new JSONObject();

		try
		{
			//PasivoTcb oPasiv = new PasivoTcb();
			//oPasiv.UltimasTransacciones(COD_NRBE_EN, NUM_SEC_AC, FECHA_CNTBL)
			

			PasivosAcuerdosServices oWsAcuerdo = new PasivosAcuerdosServices(); 
			//OBTIENE LA CUENTA CLABE
			
			PasivoTcb pasivoTcb = new PasivoTcb();
			ResponseConsultaClabe oConsClab = pasivoTcb.ConsultaClabe(acuerdo, entidad, terminal);
			String contrato = oConsClab.getStatus() == 1 ? oConsClab.getCOD_NRBE_CLABE_V() : "";
			
			//CONSULTA EL SALDO
			ResponseConsultaSaldo responseSaldo = oWsAcuerdo.ConsultaSaldo(acuerdo, entidad, terminal);
			if(responseSaldo.getStatus() == 1)
			{
				//OBTIENE EL HORARIO DEL SERVIDOR FALTA HACER LA CONVERCION POR SUCURSAL
				ResponseFechaActual responseFechaActual = oWsAcuerdo.FechaActual();
				String fechaActual = responseFechaActual.getStatus() == 1 ? responseFechaActual.getFecha() : "";
				String importe = responseSaldo.getDISPO();
				importe = String.format ("%.2f", Double.parseDouble(importe));
				String importeLetras = CantidadLetras.Convertir(importe, true);
				String datosCentro = "";//oCen.getStatus() == 1 ? centro + " "  + oCen.getNOMBRE() :  "";
				
				//SE GENERA FOLIO
				
				String DATE_FORMAT= "ddMMyyHHmmss";
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String stringDate = "";
				Date date;
				try {
					if(horaOpr.length()<2)
						horaOpr ="00:00:00";
					date = df.parse(fechaActual + " " + horaOpr);
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
					stringDate = sdf.format(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				DiarioElectronicoDS oRegDiaElec = new DiarioElectronicoDS();
				
				ResponNumSec oNumSec = new ResponNumSec();
				
				oNumSec = oRegDiaElec.ObtieneNumSec(entidad, centro, terminal);				
				if(oNumSec.getStatus()==1)
				{
					//REGISTRA OPERACION DIARIO ELECTRONICO
					String concepto="Impresion de Saldo";
					String numSec=Integer.toString( oNumSec.getNUMSEC() );
					//String fechaOperacion=responseFechaActual.getFecha();
					ResponseService responseDiarioElectronico = oRegDiaElec.RegistraImprSaldo(entidad, centro, terminal, empleado,  concepto, acuerdo, numSec);
					String folio = responseDiarioElectronico.getStatus() == 1 ? numSec : "";
					folio += " " + terminal;
					folio += stringDate;
					
					//FORMATO DE FECHA CLIENTE
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					date = df.parse(fechaActual + " 12:00:00");
					String fechaCliente = sdf.format(date);
					
					importe=CantidadLetras.FormatoNumero(importe);
					
					jsonResultado.put("fecha", fechaCliente);
					jsonResultado.put("hora", horaOpr);
					jsonResultado.put("nombre", nombreCliente);
					jsonResultado.put("imp_sdo", importe);
					jsonResultado.put("entidad", entidad);
					jsonResultado.put("sucursal", centro);
					jsonResultado.put("terminal", terminal);
					jsonResultado.put("acuerdo", acuerdo);
					jsonResultado.put("cotitulares", "");
					jsonResultado.put("folio", folio);
					jsonResultado.put("importe_letra", "( " +importeLetras +" )");
					jsonResultado.put("oficina", datosCentro);
					jsonResultado.put("contrato", contrato);
					
					jsonResultado.put("status", "1");
					jsonResultado.put("descripcion", "");

				}
				else
				{
					jsonResultado.put("status", "0");
					jsonResultado.put("descripcion", responseSaldo.getDescripcion());
				}
			}
			else 
			{
				jsonResultado.put("status", "0");
				jsonResultado.put("descripcion", responseSaldo.getDescripcion());				
			}
		} 
		catch (Exception e) 
		{
			jsonResultado.put("status", "-1");
			jsonResultado.put("descripcion", e.getMessage());
		}
		jsonResult.put("RespuestaGeneraComprobateSaldo", jsonResultado);

		return jsonResult;
	}
	
}
