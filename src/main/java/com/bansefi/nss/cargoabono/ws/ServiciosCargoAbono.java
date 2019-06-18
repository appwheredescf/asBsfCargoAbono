package com.bansefi.nss.cargoabono.ws;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import com.bansefi.nss.cargoabono.process.ConsultaSaldo;
import com.bansefi.nss.cargoabono.process.CargoAbono;

public class ServiciosCargoAbono {

    private static final Logger log = LogManager.getLogger(ServiciosCargoAbono.class);

    /**
     * Metodo para realizar Cargo/Abono contra Caja
     * @param
     * @return
     */
    public String CargoAbono(String usuario, String password, String entidad, String sucursal, String empleado,
                             String terminal, String acuerdo, String tipoOp, String fechaValor, String impNom, String concepto,
                             String fechaOperacion, String horaOp, String cajaInt, String nombreCliente, String producto,
                             String idexterno, String tipoIdExterno, String folioTrans) {
        JSONObject jsonResult = new JSONObject();
        try {
            CargoAbono processCargoAbono = new CargoAbono();
            jsonResult = processCargoAbono.Procesar(usuario, password, entidad, sucursal, empleado, terminal, acuerdo,
                    tipoOp, fechaValor, impNom, concepto, fechaOperacion, horaOp, cajaInt, nombreCliente, producto,
                    idexterno, tipoIdExterno, folioTrans);
        } catch (Exception ex) {
            log.error("CargoAbono principal  - " + ex.getMessage());
        }
        return jsonResult.toString();
    }

    /**
     * Metodo para realizar Cargo/Abono contra Intervencion
     * @param
     * @return
     */
    public String CargoAbonoIntervencion(String usuario, String password, String entidad, String sucursal,
                                         String empleado, String terminal, String acuerdo, String tipoOp, String fechaValor, String impNom,
                                         String concepto, String fechaOperacion, String horaOp, String cajaInt, String nombreCliente,
                                         String producto, String idexterno, String tipoIdExterno, String StrClop, String StrSubClop,
                                         String folioTrans) {
        JSONObject jsonResult = new JSONObject();
        try {
            CargoAbono processCargoAbono = new CargoAbono();
            jsonResult = processCargoAbono.ProcesarIntervencion(usuario, password, entidad, sucursal, empleado,
                    terminal, acuerdo, tipoOp, fechaValor, impNom, concepto, fechaOperacion, horaOp, cajaInt,
                    nombreCliente, producto, idexterno, tipoIdExterno, StrClop, StrSubClop, folioTrans);
        } catch (Exception ex) {
            log.error("CargoAbono principal  - " + ex.getMessage());
        }
        return jsonResult.toString();
    }

    /**
     * Metodo para generar comprobante de Cargo/Abono
     * @param
     * @return
     */
    public String GeneraComprobate(String terminal, String entidad, String centro, String empleado) {
        JSONObject jResult = new JSONObject();
        CargoAbono ProcessCA = new CargoAbono();
        jResult = ProcessCA.ComprobanteCargoAbono(terminal, entidad, centro, empleado);
        return jResult.toString();
    }

    /**
     * Metodo para generar comprobande de Consulta de saldos el cual retorna un JSON en modo de string
     * @param
     * @return
     */
    public String GeneraComprobateSaldo(String usuario, String password, String entidad, String sucursal,
                                        String terminal, String empleado, String acuerdo, String horaOpr, String nombreCliente, String idExterno) {
        JSONObject result = new JSONObject();
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

    private static final String ORIGINAL = "��������������";
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
