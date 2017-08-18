package com.bansefi.nss.cargoabono.ws;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.bansefi.nss.cargoabono.process.ConsultaSaldo;
import com.bansefi.nss.cargoabono.process.CargoAbono;;

public class ServiciosCargoAbono 
{
	private static final Logger log = LogManager.getLogger(ServiciosCargoAbono.class);
	 
	public String CargoAbono(String entidad,String sucursal, String empleado, String terminal, String acuerdo, String tipoOp, String fechaValor, String impNom, String concepto, String fechaOperacion, String horaOp, String cajaInt, String nombreCliente, String producto, String idexterno, String tipoIdExterno)
	{
		//String result = "{RespuestaCargoAbono:{status:0,descripcion:pruebaReturn,idmov:111}}";
		JSONObject jsonResult = new JSONObject();
		try
		{
			CargoAbono processCargoAbono = new CargoAbono();
			/* Proceso de Diario Electronico comentado E234 09-06-2017 */
			jsonResult =processCargoAbono.Procesar(entidad, sucursal, empleado, terminal, acuerdo, tipoOp, fechaValor, impNom, concepto, fechaOperacion, horaOp, cajaInt, nombreCliente, producto, idexterno, tipoIdExterno);
		}
		catch(Exception ex)
		{
			log.error("CargoAbono principal  - " + ex.getMessage());			
		}
		return  jsonResult.toString();
	}
	
	public String CargoAbonoIntervencion(String entidad,String sucursal, String empleado,
			String terminal, String acuerdo, String tipoOp, String fechaValor, 
			String impNom, String concepto, String fechaOperacion, String horaOp,
			String cajaInt, String nombreCliente, String producto, String idexterno, 
			String tipoIdExterno,String StrClop,String StrSubClop)
	{
		//String result = "{RespuestaCargoAbono:{status:0,descripcion:pruebaReturn,idmov:111}}";
		JSONObject jsonResult = new JSONObject();
		try
		{
			CargoAbono processCargoAbono = new CargoAbono();
			/* Proceso de Diario Electronico comentado E234 09-06-2017 */
			jsonResult =processCargoAbono.ProcesarIntervencion(entidad, sucursal, empleado, terminal, 
					acuerdo, tipoOp, fechaValor, impNom, concepto, 
					fechaOperacion, horaOp, cajaInt, nombreCliente, producto, 
					idexterno, tipoIdExterno,StrClop,StrSubClop);
		}
		catch(Exception ex)
		{
			log.error("CargoAbono principal  - " + ex.getMessage());
			
		}
		return  jsonResult.toString();
	}	
	public String ConsultaPendientes(String entidad, String sucursal, String terminal, String empleado){
		/*String result = "{RespuestaConsultaPendientes:{ idMovimiento:2340,"
												+ "entidad:'0166',"
												+ "tipoOp:'A',"
												+ "fechaValor:'2017/04/27',"
												+ "sucursal:'0243',"
												+ "idTerminal:'12024309',"
												+ "idEmpleado:'E1662029',"
												+ "horaOp:'13:47:52',"
												+ "fechaContable:'2014/03/04',"
												+ "statusProceso:2,"
												+ "dateProceso:'2017-04-27T13:47:45.39',"
												+ "dateCambioStatus:'2017-04-27T13:47:50.667',"
												+ "folioTrans:'562463',"
												+ "dataTrans:'EOAVf2R8uTvkeo2mnKqipZcH3efRaGG8gIjm7HcRWnAa9zpYVLaTvbLSF6I/4gGJuvCJdgjWKEEPVzBSpWISyBRdMOGi4flcL0uaeEz8AyQf+Ze8dNbaHdoME6MtzREW8/ypW718BTHSB6OEHkl0vc9b97HkHWxeNh4SCBKaTmbpdMW1GVStCbOFmAdA2t39ogoAGYv1uZOLEX6FGiJ5vGYauq3KUGGTyom7BYIfogkvCAFU+/4Le52++k1z9Zsq7nbi54Vi0FyKm1GJT4Ozr2isNZ2CndzJOI5TqlZ1eKgQLBJROOwvnJKJ8h0mNgwfhUd7iiw9Gz8B3ar4CNiyySnNYOfKewmFDDS+l1WgNp5pM+i8R4ghoY7dgbnx9exrzdMFIeq6y7wwvVAZtefywPhpoDZCq3pN+HhU5/6ehnetcIUlBwSBPGhU33AJwmv/ESwGoLehUtQ+m7gd/LO6Wg=='}}";
		*/
		JSONObject jsonResult = new JSONObject();
		try
		{
			CargoAbono processCargoAbono = new CargoAbono();
			
			jsonResult = processCargoAbono.ConsultaPendientes(entidad, sucursal, terminal, empleado);	
		}
		catch(Exception ex)
		{
			JSONObject jsonResultado = new JSONObject();
			jsonResultado.put("error", -1);
			jsonResultado.put("descripcion", ex.getMessage());
			jsonResult.put("RespuestaConsultaPendientes", jsonResultado);
		}
		
		return jsonResult.toString();
	}
	
	public String GeneraComprobate(String terminal,String entidad,String centro)
	{
		
		/*String result ="";
		String result = "{RespuestaGeneraComprobate:{'fecha': '27/04/2017', "
				+ "'hora': '13:47:52', "
				+ "'nombre': 'JUAN MARTINEZ ZUAREZ', "
				+ "'importe': '190.50', "
				+ "'folio': '562463', "
				+ "'producto': 'V003', "
				+ "'importe_letra': '(CIENTO NOVENTA PESOS 50/100 M.N.)', "
				+ "'oficina': '0243 TEZIUTLAN', "
				+ "'contrato': '166180000145570291', "
				+ "'serial': '12024309 270417134752 C D' }}";*/
		/*Begin E234*/
		JSONObject jResult = new JSONObject();
		CargoAbono ProcessCA = new CargoAbono();
		
		jResult = ProcessCA.ComprobanteCargoAbono(terminal,entidad,centro);
		
		return jResult.toString();
		/*End E234*/
	}
	
	public String GeneraComprobateSaldo(String entidad, String sucursal, String terminal, String empleado, String acuerdo,String horaOpr, String nombreCliente, String idExterno){
		JSONObject result = new JSONObject();
		/*
		String result = "{RespuestaGeneraComprobateSaldo:{ 'fecha': '21/04/2017', "
		+ "'hora': '14:50:48', "
		+ "'nombre': 'PEDRO LOPEZ PEREZ', "
		+ "'imp_sdo': '$8,150,751.00', "
		+ "'entidad': '0166', "
		+ "'sucursal': '0121', "
		+ "'terminal': '12012103', "
		+ "'acuerdo': '0259679371', "
		+ "'cotitulares': '', "
		+ "'folio': '28869 2104201714504812012103', "
		+ "'importe_letra': '(OCHO MILLONES CIENTO CINCUENTA MIL SETECIENTOS CINCUENTA Y UNO PESOS 00/100 M.N.)', "
		+ "'oficina': ' ', "
		+ "'contrato': '166180002596793719' }}";*/
		
		ConsultaSaldo processConsultaSaldo = new ConsultaSaldo();
		
		result = processConsultaSaldo.Comprobante(entidad, sucursal, acuerdo, terminal, horaOpr, nombreCliente,idExterno,empleado);
		
		return result.toString();
	}

	public String ProcesaPendientes(String entidad,String terminal,String centro)
	{
		JSONObject result = new JSONObject();
		CargoAbono processCargoAbono = new CargoAbono();
		result= processCargoAbono.ProcesaPendientes(entidad, terminal,centro);
		
		return result.toString();
	}
	
	public String ConsultaPendienteDiario(String entidad,String terminal,String centro)
	{
		JSONObject result = new JSONObject();
		CargoAbono processCargoAbono = new CargoAbono();
		result= processCargoAbono.ConsultaPendienteDiario(entidad, terminal,centro);
		
		return result.toString();
	}
	
	private static final String ORIGINAL
				    = "¡·…ÈÕÌ”Û⁄˙—Ò‹¸";
	private static final String REPLACEMENT
				    = "AaEeIiOoUuNnUu";
				public static String stripAccents(String str) {
				if (str == null) {
				    return null;
				}
				char[] array = str.toCharArray();
				for (int index = 0; index < array.length; index++) {
				    int pos = ORIGINAL.indexOf(array[index]);
				    if (pos > -1) {
				        array[index] = REPLACEMENT.charAt(pos);
				    }
				}
				return new String(array);
				}
	
}
