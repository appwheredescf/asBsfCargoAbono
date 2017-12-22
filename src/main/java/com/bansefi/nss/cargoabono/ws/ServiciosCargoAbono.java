package com.bansefi.nss.cargoabono.ws;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.bansefi.nss.cargoabono.process.ConsultaSaldo;
import com.bansefi.nss.cargoabono.process.CargoAbono;;

public class ServiciosCargoAbono {
	private static final Logger log = LogManager.getLogger(ServiciosCargoAbono.class);

	// Metodo para realizar Cargo/Abono Caja
	public String CargoAbono(String usuario, String password, String entidad, String sucursal, String empleado,
			String terminal, String acuerdo, String tipoOp, String fechaValor, String impNom, String concepto,
			String fechaOperacion, String horaOp, String cajaInt, String nombreCliente, String producto,
			String idexterno, String tipoIdExterno, String folioTrans) {
		// String result =
		// "{RespuestaCargoAbono:{status:0,descripcion:pruebaReturn,idmov:111}}";
		JSONObject jsonResult = new JSONObject();
		try {
			CargoAbono processCargoAbono = new CargoAbono();
			/* Proceso de Diario Electronico comentado E234 09-06-2017 */
			jsonResult = processCargoAbono.Procesar(usuario, password, entidad, sucursal, empleado, terminal, acuerdo,
					tipoOp, fechaValor, impNom, concepto, fechaOperacion, horaOp, cajaInt, nombreCliente, producto,
					idexterno, tipoIdExterno, folioTrans);
		} catch (Exception ex) {
			log.error("CargoAbono principal  - " + ex.getMessage());
		}
		return jsonResult.toString();
	}

	// Metodo para realizar Cargo/Abono Intervencion
	public String CargoAbonoIntervencion(String usuario, String password, String entidad, String sucursal,
			String empleado, String terminal, String acuerdo, String tipoOp, String fechaValor, String impNom,
			String concepto, String fechaOperacion, String horaOp, String cajaInt, String nombreCliente,
			String producto, String idexterno, String tipoIdExterno, String StrClop, String StrSubClop,
			String folioTrans) {
		// String result =
		// "{RespuestaCargoAbono:{status:0,descripcion:pruebaReturn,idmov:111}}";
		JSONObject jsonResult = new JSONObject();
		try {
			CargoAbono processCargoAbono = new CargoAbono();
			/* Proceso de Diario Electronico comentado E234 09-06-2017 */
			jsonResult = processCargoAbono.ProcesarIntervencion(usuario, password, entidad, sucursal, empleado,
					terminal, acuerdo, tipoOp, fechaValor, impNom, concepto, fechaOperacion, horaOp, cajaInt,
					nombreCliente, producto, idexterno, tipoIdExterno, StrClop, StrSubClop, folioTrans);
		} catch (Exception ex) {
			log.error("CargoAbono principal  - " + ex.getMessage());

		}
		return jsonResult.toString();
	}

	public String GeneraComprobate(String terminal, String entidad, String centro, String empleado) {
		/*
		 * String result =""; String result =
		 * "{RespuestaGeneraComprobate:{'fecha': '27/04/2017', " +
		 * "'hora': '13:47:52', " + "'nombre': 'JUAN MARTINEZ ZUAREZ', " +
		 * "'importe': '190.50', " + "'folio': '562463', " +
		 * "'producto': 'V003', " +
		 * "'importe_letra': '(CIENTO NOVENTA PESOS 50/100 M.N.)', " +
		 * "'oficina': '0243 TEZIUTLAN', " +
		 * "'contrato': '166180000145570291', " +
		 * "'serial': '12024309 270417134752 C D' }}";
		 */
		/* Begin E234 */
		JSONObject jResult = new JSONObject();
		CargoAbono ProcessCA = new CargoAbono();

		jResult = ProcessCA.ComprobanteCargoAbono(terminal, entidad, centro, empleado);

		return jResult.toString();
		/* End E234 */
	}

	// Metodo para generar comprobande de Consulta de saldos el cual retorna un
	// JSON en modo de string
	public String GeneraComprobateSaldo(String usuario, String password, String entidad, String sucursal,
			String terminal, String empleado, String acuerdo, String horaOpr, String nombreCliente, String idExterno) {
		JSONObject result = new JSONObject();
		/*
		 * String result =
		 * "{RespuestaGeneraComprobateSaldo:{ 'fecha': '21/04/2017', " +
		 * "'hora': '14:50:48', " + "'nombre': 'PEDRO LOPEZ PEREZ', " +
		 * "'imp_sdo': '$8,150,751.00', " + "'entidad': '0166', " +
		 * "'sucursal': '0121', " + "'terminal': '12012103', " +
		 * "'acuerdo': '0259679371', " + "'cotitulares': '', " +
		 * "'folio': '28869 2104201714504812012103', " +
		 * "'importe_letra': '(OCHO MILLONES CIENTO CINCUENTA MIL SETECIENTOS CINCUENTA Y UNO PESOS 00/100 M.N.)', "
		 * + "'oficina': ' ', " + "'contrato': '166180002596793719' }}";
		 */

		ConsultaSaldo processConsultaSaldo = new ConsultaSaldo();
		result = processConsultaSaldo.Comprobante(usuario, password, entidad, sucursal, acuerdo, terminal, horaOpr,
				nombreCliente, idExterno, empleado);
		return result.toString();
	}

	public String ProcesaPendientes(String entidad, String terminal, String centro) {
		JSONObject result = new JSONObject();
		CargoAbono processCargoAbono = new CargoAbono();
		result = processCargoAbono.ProcesaPendientes(entidad, terminal, centro);
		return result.toString();
	}

	public String ConsultaPendienteDiario(String entidad, String terminal, String centro) {
		JSONObject result = new JSONObject();
		CargoAbono processCargoAbono = new CargoAbono();
		result = processCargoAbono.ConsultaPendienteDiario(entidad, terminal, centro);
		return result.toString();
	}

	private static final String ORIGINAL = "¡·…ÈÕÌ”Û⁄˙—Ò‹¸";
	private static final String REPLACEMENT = "AaEeIiOoUuNnUu";

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
